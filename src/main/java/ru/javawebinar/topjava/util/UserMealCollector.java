package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class UserMealCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>> {
    private int maxCaloriesPerDay;
    private Map<LocalDate, Integer> caloriesPerDayMap;
    private LocalTime startTime, endTime;

    UserMealCollector(int maxCaloriesPerDay, LocalTime startTime, LocalTime endTime) {
        this.maxCaloriesPerDay = maxCaloriesPerDay;
        this.startTime = startTime;
        this.endTime = endTime;
        caloriesPerDayMap = new HashMap<>();
    }

    @Override
    public Supplier<List<UserMeal>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
        return (userMeals, meal) -> {
            caloriesPerDayMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                userMeals.add(meal);
            }
        };
    }

    @Override
    public BinaryOperator<List<UserMeal>> combiner() {
        return ((userMeals, userMeals2) -> {
            userMeals.addAll(userMeals2);
            return userMeals;
        });
    }

    @Override
    public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
        return (meals) -> {
            List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
            for (UserMeal meal : meals) {
                UserMealWithExcess mealWithExcess = new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesPerDayMap.get(meal.getDate()) > maxCaloriesPerDay);
                userMealWithExcessList.add(mealWithExcess);
            }
            return userMealWithExcessList;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED));
    }
}
