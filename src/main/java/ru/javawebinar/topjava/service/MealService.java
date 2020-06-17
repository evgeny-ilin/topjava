package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Service
public class MealService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(repository.getFiltered(Objects::nonNull, authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(Predicate<Meal> filter) {
        return MealsUtil.getTos(repository.getFiltered(filter, authUserId()), authUserCaloriesPerDay());
    }

    public MealTo get(int id) {
        Meal meal = checkNotFoundWithId(repository.get(id, authUserId()), id);
        return getMealTo(meal);
    }

    public MealTo create(Meal meal) {
        return getMealTo(checkNotFound(repository.save(meal, authUserId()), meal.toString()));
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id, authUserId()), id);
    }

    public void update(Meal meal) {
        checkNotFoundWithId(repository.save(meal, authUserId()), meal.getId());
    }

    private MealTo getMealTo(Meal meal) {
        List<Meal> meals = (List<Meal>) repository.getFiltered(meal1 -> meal1.getDate().equals(meal.getDate()), authUserId());
        List<MealTo> mealTos = MealsUtil.getTos(meals, authUserCaloriesPerDay());
        boolean excess = mealTos.get(0).isExcess();
        return MealsUtil.createTo(meal, excess);
    }
}