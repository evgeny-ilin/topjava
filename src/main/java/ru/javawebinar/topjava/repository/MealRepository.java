package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.function.Predicate;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal, int authUserId);

    // false if not found
    boolean delete(int id, int authUserId);

    // null if not found
    Meal get(int id, int authUserId);

    Collection<Meal> getFiltered(Predicate<Meal> filter, int authUserId);
}