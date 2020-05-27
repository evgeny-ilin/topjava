package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CaloriesPerDay {
    final int maxCaloriesPerDay;
    private Map<LocalDate, Integer> caloriesPerDayMap;
    private List<UserMealWithExcess> mealWithExcessList;

    CaloriesPerDay(int maxCaloriesPerDay) {
        caloriesPerDayMap = new HashMap<>();
        mealWithExcessList = new ArrayList<>();
        this.maxCaloriesPerDay = maxCaloriesPerDay;
    }

    void addMeal(UserMeal meal) {
        caloriesPerDayMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        mealWithExcessList.add(new UserMealWithExcess(meal.getDateTime(),
                meal.getDescription(),
                meal.getCalories(),
                false));
    }

    CaloriesPerDay getAll() {
        return this;
    }

    void addAll(CaloriesPerDay mCaloriesPerDayToAdd) {
        caloriesPerDayMap.putAll(mCaloriesPerDayToAdd.caloriesPerDayMap);
        mealWithExcessList.addAll(mCaloriesPerDayToAdd.mealWithExcessList);
    }

    List<UserMealWithExcess> getMealWithExcessList() {
        return mealWithExcessList;
    }

    Map<LocalDate, Integer> getCaloriesPerDayMap() {
        return caloriesPerDayMap;
    }
}
