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

public class UserMealCollector implements Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>> {
    private int maxCaloriesPerDay;
    private Map<LocalDate, Integer> caloriesPerDayMap;
    private LocalTime startTime;
    private LocalTime endTime;

    UserMealCollector(int maxCaloriesPerDay, LocalTime startTime, LocalTime endTime) {
        this.maxCaloriesPerDay = maxCaloriesPerDay;
        this.startTime = startTime;
        this.endTime = endTime;
        caloriesPerDayMap = new HashMap<>();
    }

    @Override
    public Supplier<List<UserMealWithExcess>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
        return (userMeals, meal) -> {
            caloriesPerDayMap.merge(meal.getDate(), meal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)) {
                userMeals.add(new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        false));
            }
        };
    }

    @Override
    public BinaryOperator<List<UserMealWithExcess>> combiner() {
        return ((userMeals, userMeals2) -> {
            userMeals.forEach(meal -> meal.setExcess(caloriesPerDayMap.get(meal.getDate()) > maxCaloriesPerDay));
            userMeals2.forEach(meal -> meal.setExcess(caloriesPerDayMap.get(meal.getDate()) > maxCaloriesPerDay));
            userMeals.addAll(userMeals2);
            return userMeals;
        });
    }

    @Override
    public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED));
    }
}
