package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    Meal add(Meal meal);

    Meal get(int id);

    List<Meal> getAll();

    void delete(int id);

    Meal update(int id, Meal meal);
}
