package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoField;
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
        List<UserMealWithExcess> mealsToOpt2 = filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToOpt2.forEach(System.out::println);

        System.out.println();
        System.out.println("filteredByStreams");
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println();
        System.out.println("filteredByStreamsOptional2");
        System.out.println(filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // return filtered list with excess. Implement by cycles

        List<UserMealWithExcess> filteredMeals = new ArrayList<>(meals.size());
        List<LocalDate> excessDays = getExcessDays(meals,caloriesPerDay);//O(n)

        //O(n+m), in worse case m=n -> O(n+m) = O(2n) = O(n)
        for (UserMeal meal:meals) {//O(n)
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                LocalDate mealDay = LocalDate.from(meal.getDateTime());
                boolean excess = excessDays.contains(mealDay); //O(m)
                UserMealWithExcess mealWithExcess = new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        excess);
                filteredMeals.add(mealWithExcess);//O(1)
            }
        }

        return filteredMeals;
    }

    private static List<LocalDate> getExcessDays (List<UserMeal> meals, int maxCaloriesPerDay) {

        if (meals == null || meals.isEmpty()) return new ArrayList<>();

        Map<LocalDate,Integer> caloriesPerDay = new HashMap<>();

        //O(n)
        for (UserMeal meal:meals) {
            LocalDate mealDay = LocalDate.from(meal.getDateTime());
            caloriesPerDay.merge(mealDay,meal.getCalories(), Integer::sum);//O(1)??
        }

        List<LocalDate> excessDays = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry:caloriesPerDay.entrySet()) {
            if (entry.getValue() > maxCaloriesPerDay)
                excessDays.add(entry.getKey());
        }

        return excessDays;
    }

    /*
    Реализовать метод `UserMealsUtil.filteredByStreams` через Java 8 Stream API.
     */
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        Map<LocalDate,Integer> caloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(x->LocalDate.from(x.getDateTime()),Collectors.summingInt(UserMeal::getCalories)));

        List<UserMeal> filteredMeals = meals.stream()
                .filter(userMeal ->
                        userMeal.getDateTime().toLocalTime().isAfter(startTime) &&
                        userMeal.getDateTime().toLocalTime().isBefore(endTime))
                .collect(Collectors.toList());

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal:filteredMeals) {
            LocalDate mealDay = LocalDate.from(meal.getDateTime());
            boolean excess = caloriesPerDay.get(mealDay) > maxCaloriesPerDay;
            result.add(new UserMealWithExcess(meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories(),
                    excess));
        }

        return result;
    }

    /* Optional 2
        циклом за 1 проход по List<UserMeal>
        без циклов по другим коллекциям
        решение должно быть рабочим в общем случае (не только при запуске main)
     */
    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        Map<LocalDate,Integer> caloriesPerDay = new HashMap<>();

        List<UserMealWithExcess> result = new ArrayList<>();

        //O(n+m), in worse case m=n -> O(n+m) = O(2n) = O(n)
        for (UserMeal meal:meals) {//O(n)
            LocalDate mealDay = LocalDate.from(meal.getDateTime());

            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                UserMealWithExcess mealWithExcess = new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        false);
               result.add(mealWithExcess);
            }

            caloriesPerDay.merge(mealDay,meal.getCalories(), Integer::sum);

            //No cycles )))
            if (caloriesPerDay.get(mealDay) > maxCaloriesPerDay) {
                result.stream()
                        .filter(x->LocalDate.from(x.getDateTime()).equals(mealDay))
                        .forEach(x -> x.setExcess(true));
            }
        }

        return result;
    }

    /*
    через Stream API за 1 проход по исходному списку meals.streem()
    нельзя использовать внешние коллекции, не являющиеся частью коллектора или 2 раза проходить по исходному списку (в том числе модифицированному, например отфильтрованному). Т.е. в решении не должно быть 2 раза meal.stream() (в том числе неявно, в составных коллекторах)
    возможно дополнительные проходы по частям списка
     */
    public static List<UserMealWithExcess> filteredByStreamsOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> result = new ArrayList<>();
        //план разобраться с тем как сделать свой коллектор
        return result;
    }
}
