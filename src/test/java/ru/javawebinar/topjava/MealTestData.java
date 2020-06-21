package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealTestData {
    public static final int MEAL_ID = 100002;
    public static final Meal MEAL = new Meal(MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final int ADMIN_MEAL_ID = 100005;
    public static final int UPDATED_MEAL_ID = 100002;
    public static final List<Meal> USER_MEALS = Arrays.asList(
            new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(100003, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(100002, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500)

    );

    public static Meal getUpdated() {
        return new Meal(UPDATED_MEAL_ID, LocalDateTime.of(2021, Month.JANUARY, 30, 20, 0), "Updated", 1000);
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2021, Month.JANUARY, 01, 10, 0), "Завтрак", 50);
    }

    public static List<Meal> filtered() {
        return Arrays.asList(
                new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(100003, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000)
        );
    }
}
