package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {
    void addMeal(Meal meal);

    Meal getMeal(int id);

    List<Meal> getMeals();

    void deleteMeal(int id);

    void updateMeal(Meal meal);
}
