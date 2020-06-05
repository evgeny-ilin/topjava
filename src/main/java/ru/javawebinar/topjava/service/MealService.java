package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalTime;
import java.util.List;

public interface MealService {
    List<MealTo> getMealsTo(LocalTime startTime, LocalTime endTime, int caloriesPerDay);
}
