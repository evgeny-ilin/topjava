package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class UserMealCollector implements Collector<UserMeal, CaloriesPerDay, Stream<UserMealWithExcess>> {
    private int maxCaloriesPerDay;

    UserMealCollector(int maxCaloriesPerDay) {
        this.maxCaloriesPerDay = maxCaloriesPerDay;
    }

    /**
     * A function that creates and returns a new mutable result container.
     *
     * @return a function which returns a new, mutable result container
     */
    @Override
    public Supplier<CaloriesPerDay> supplier() {
        return () -> new CaloriesPerDay(maxCaloriesPerDay);
    }

    /**
     * A function that folds a value into a mutable result container.
     *
     * @return a function which folds a value into a mutable result container
     */
    @Override
    public BiConsumer<CaloriesPerDay, UserMeal> accumulator() {
        return CaloriesPerDay::addMeal;
    }

    /**
     * A function that accepts two partial results and merges them.  The
     * combiner function may fold state from one argument into the other and
     * return that, or may return a new result container.
     *
     * @return a function which combines two partial results into a combined
     * result
     */
    @Override
    public BinaryOperator<CaloriesPerDay> combiner() {
        return ((caloriesPerDay, caloriesPerDay2) -> {
            caloriesPerDay.addAll(caloriesPerDay2.getAll());
            return caloriesPerDay;
        });
    }

    /**
     * Perform the final transformation from the intermediate accumulation type
     * {@code A} to the final result type {@code R}.
     *
     * <p>If the characteristic {@code IDENTITY_FINISH} is
     * set, this function may be presumed to be an identity transform with an
     * unchecked cast from {@code A} to {@code R}.
     *
     * @return a function which transforms the intermediate result to the final
     * result
     */
    @Override
    public Function<CaloriesPerDay, Stream<UserMealWithExcess>> finisher() {
        return (caloriesPerDay) -> {
            List<UserMealWithExcess> userMealWithExcessList = caloriesPerDay.getMealWithExcessList();
            Map<LocalDate, Integer> mCaloriesPerDay = caloriesPerDay.getCaloriesPerDayMap();
            for (UserMealWithExcess mealWithExcess : userMealWithExcessList) {
                mealWithExcess.setExcess(mCaloriesPerDay.get(mealWithExcess.getDate()) > caloriesPerDay.maxCaloriesPerDay);
            }
            return userMealWithExcessList.stream();
        };
    }

    /**
     * Returns a {@code Set} of {@code Collector.Characteristics} indicating
     * the characteristics of this Collector.  This set should be immutable.
     *
     * @return an immutable set of collector characteristics
     */
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED));
    }
}
