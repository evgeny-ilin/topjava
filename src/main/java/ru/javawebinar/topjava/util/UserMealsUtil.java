package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),

                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),

                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println("filteredByCycles");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println();
        System.out.println("filteredByCyclesOptional2");
        System.out.println(filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println();
        System.out.println("filteredByStreams");
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println();
        System.out.println("filteredByStreamsOptional2");
        System.out.println(filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDay = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate mealDay = meal.getDate();
            caloriesPerDay.merge(mealDay, meal.getCalories(), Integer::sum);//O(1)
        }

        List<UserMealWithExcess> filteredMeals = new ArrayList<>(meals.size());
        for (UserMeal meal : meals) {
            addMealWithExcess(startTime, endTime, maxCaloriesPerDay, meal, filteredMeals, caloriesPerDay);
        }
        return filteredMeals;
    }

    static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDay = new HashMap<>();
        return getMealWithExcess(meals, startTime, endTime, maxCaloriesPerDay, caloriesPerDay, 0);
    }

    private static List<UserMealWithExcess> getMealWithExcess(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay, Map<LocalDate, Integer> caloriesPerDay, int idx) {
        UserMeal meal = meals.get(idx);
        List<UserMealWithExcess> mealWithExcessList = new ArrayList<>();
        caloriesPerDay.merge(meal.getDate(), meal.getCalories(), Integer::sum);
        if (idx == meals.size() - 1) return mealWithExcessList;

        mealWithExcessList.addAll(getMealWithExcess(meals, startTime, endTime, maxCaloriesPerDay, caloriesPerDay, ++idx));

        addMealWithExcess(startTime, endTime, maxCaloriesPerDay, meal, mealWithExcessList, caloriesPerDay);
        return mealWithExcessList;
    }

    private static void addMealWithExcess(LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay, UserMeal meal, List<UserMealWithExcess> mealWithExcessList, Map<LocalDate, Integer> caloriesPerDay) {
        if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
            LocalDate mealDay = meal.getDate();
            boolean excess = caloriesPerDay.get(mealDay) > maxCaloriesPerDay;
            UserMealWithExcess mealWithExcess = new UserMealWithExcess(meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories(),
                    excess);
            mealWithExcessList.add(mealWithExcess);
        }
    }

    static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal ->
                        TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> {
                    boolean excess = caloriesPerDay.get(meal.getDate()) > maxCaloriesPerDay;
                    return new UserMealWithExcess(meal.getDateTime(),
                            meal.getDescription(),
                            meal.getCalories(),
                            excess);
                })
                .collect(Collectors.toList());
    }

    static List<UserMealWithExcess> filteredByStreamsOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        return meals.stream()
                .collect(new UserMealCollector(maxCaloriesPerDay, startTime, endTime));
    }
}
