package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.GenerateId;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealDaoInMemoryImpl implements MealDao {
    private static List<Meal> meals;

    public MealDaoInMemoryImpl() {
        super();
        meals = new CopyOnWriteArrayList<Meal>() {{
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        }};
        for (Meal meal : meals) {
            meal.setId(GenerateId.getId());
        }
    }

    @Override
    public void addMeal(Meal meal) {
        if (meal.getId() == 0) meal.setId(GenerateId.getId());
        meals.add(meal);
    }

    @Override
    public Meal getMeal(int id) {
        return meals.stream().filter(meal -> meal.getId() == id).findFirst().get();
    }

    @Override
    public List<Meal> getMeals() {
        return meals;
    }

    @Override
    public void deleteMeal(int id) {
        meals.remove(getMeal(id));
    }

    @Override
    public void updateMeal(Meal meal) {
        deleteMeal(meal.getId());
        addMeal(meal);
    }
}
