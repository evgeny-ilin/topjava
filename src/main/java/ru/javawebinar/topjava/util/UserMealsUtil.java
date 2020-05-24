package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // return filtered list with excess. Implement by cycles
        meals.sort(Comparator.comparing(UserMeal::getDateTime)); //O(n log(n))

        List<UserMealWithExcess> filteredMeals = new ArrayList<>();
        List<LocalDate> excessDays = getExcessDays(meals,caloriesPerDay);

        //O(n+m), в худшем случае мы каждый день превысили норму m=n -> O(n+m) = O(2n) = O(n)
        for (UserMeal meal:meals) {//O(n)
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                LocalDate mealDay = LocalDate.from(meal.getDateTime());
                boolean excess = excessDays.contains(mealDay); //O(m)
                UserMealWithExcess mealWithExcess = new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        excess);
                filteredMeals.add(mealWithExcess);//O(n), можно сделать O(1), если сразу создать список размером meals.size
            }
        }

        return filteredMeals;
    }

    private static List<LocalDate> getExcessDays (List<UserMeal> meals, int caloriesPerDay) {

        if (meals == null || meals.isEmpty()) return new ArrayList<LocalDate>();

        List<LocalDate> excessDays = new ArrayList<>();
        int sumCalories = 0;
        LocalDate day = LocalDate.from(meals.get(0).getDateTime());


        //O(n)
        for (UserMeal meal:meals
             ) {
            LocalDate mealDay = LocalDate.from(meal.getDateTime());
            if (mealDay.compareTo(day) == 0)
                sumCalories += meal.getCalories();
            else {
                if (sumCalories > caloriesPerDay)
                    excessDays.add(mealDay);
                day = mealDay;
                sumCalories = meal.getCalories();
            }
        }

        if (sumCalories > caloriesPerDay)
            excessDays.add(day);

        return excessDays;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }
}
