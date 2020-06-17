package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int authUserId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(authUserId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> oldMeal.getUserId() == authUserId ? meal : oldMeal);
    }

    @Override
    public boolean delete(int id, int authUserId) {
        return repository.values().removeIf(meal -> meal.getId() == id && meal.getUserId() == authUserId);
    }

    @Override
    public Meal get(int id, int authUserId) {
        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != authUserId) {
            return null;
        }
        return meal;
    }

    @Override
    public Collection<Meal> getFiltered(Predicate<Meal> filter, int authUserId) {
        log.info("Repository getAll for user = {}", authUserId);
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == authUserId)
                .filter(filter)
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }
}

