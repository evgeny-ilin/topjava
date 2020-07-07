package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class MealServiceTest extends AbstractTest {

    @Test
    public void delete() throws Exception {
        mealService.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> mealService.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> mealService.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() throws Exception {
        Meal created = mealService.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void get() throws Exception {
        Meal actual = mealService.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> mealService.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateNotOwn() throws Exception {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> mealService.update(MEAL1, ADMIN_ID));
        Assert.assertEquals("Not found entity with id=" + MEAL1_ID, exception.getMessage());
    }

    @Test
    public void getAll() throws Exception {
        MEAL_MATCHER.assertMatch(mealService.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        MEAL_MATCHER.assertMatch(mealService.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() throws Exception {
        MEAL_MATCHER.assertMatch(mealService.getBetweenInclusive(null, null, USER_ID), MEALS);
    }
}