package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_1_ID = START_SEQ + 2;

    public static final Meal USER_MEAL_1 = new Meal(USER_MEAL_1_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "User_Завтрак", 500);
    public static final Meal USER_MEAL_2 = new Meal(USER_MEAL_1_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "User_Обед", 1000);
    public static final Meal USER_MEAL_3 = new Meal(USER_MEAL_1_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "User_Ужин", 500);
    public static final Meal USER_MEAL_4 = new Meal(USER_MEAL_1_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "User_Ночные бдения", 500);

    public static final int USER_UPDATED_MEAL_ID = USER_MEAL_1_ID;
    public static final Meal USER_UPDATED_MEAL = new Meal(USER_UPDATED_MEAL_ID, LocalDateTime.of(2021, Month.JANUARY, 30, 20, 0), "Updated", 1000);

    public static final Meal USER_NEW_MEAL = new Meal(null, LocalDateTime.of(2021, Month.JANUARY, 01, 10, 0), "User_Завтрак", 50);

    public static final int ADMIN_MEAL_ID = START_SEQ + 6;
    public static final Meal ADMIN_MEAL_1 = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Admin_Еда в пограничное время", 100);
    public static final Meal ADMIN_MEAL_2 = new Meal(ADMIN_MEAL_ID + 1, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Admin_Завтрак", 1000);
    public static final Meal ADMIN_MEAL_3 = new Meal(ADMIN_MEAL_ID + 2, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Admin_Обед", 500);
    public static final Meal ADMIN_MEAL_4 = new Meal(ADMIN_MEAL_ID + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Admin_Ужин", 410);
}
