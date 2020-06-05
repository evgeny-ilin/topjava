package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDaoInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealServiceImpl implements MealService {
    private MealDaoInMemoryImpl mealDaoInMemoryImpl = new MealDaoInMemoryImpl();

    @Override
    public void addMeal(Meal meal) {
        mealDaoInMemoryImpl.addMeal(meal);
    }

    @Override
    public Meal getMeal(int id) {
        return mealDaoInMemoryImpl.getMeal(id);
    }

    @Override
    public List<Meal> getMeals() {
        return mealDaoInMemoryImpl.getMeals();
    }

    @Override
    public void deleteMeal(int id) {
        mealDaoInMemoryImpl.deleteMeal(id);
    }

    @Override
    public void updateMeal(Meal meal) {
        mealDaoInMemoryImpl.updateMeal(meal);
    }
}
