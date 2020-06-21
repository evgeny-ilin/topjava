package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app-jdbc.xml",
        "classpath:spring/spring-test-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_1_ID, USER_ID);
        assertThat(meal).isEqualToComparingFieldByField(USER_MEAL_1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_1_ID, USER_ID);
        assertNull(repository.get(USER_MEAL_1_ID, USER_ID));
    }

    @Test
    public void deleteAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND_MEAL_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        final List<Meal> filteredMeals = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30), LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        final List<Meal> FILTERED_USER_MEALS = Arrays.asList(
                USER_MEAL_3,
                USER_MEAL_2,
                USER_MEAL_1
        );
        assertEquals(FILTERED_USER_MEALS, filteredMeals);
    }

    @Test
    public void getAll() {
        final List<Meal> meals = service.getAll(USER_ID);
        final List<Meal> ALL_USER_MEALS = Arrays.asList(
                USER_MEAL_4,
                USER_MEAL_3,
                USER_MEAL_2,
                USER_MEAL_1
        );
        assertEquals(ALL_USER_MEALS, meals);
    }

    @Test
    public void update() {
        service.update(USER_UPDATED_MEAL, USER_ID);
        assertThat(service.get(USER_UPDATED_MEAL_ID, USER_ID)).isEqualToComparingFieldByField(USER_UPDATED_MEAL);
    }

    @Test
    public void updateAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.update(USER_UPDATED_MEAL, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(USER_NEW_MEAL, USER_ID);
        Integer newId = created.getId();
        USER_NEW_MEAL.setId(newId);
        assertThat(created).isEqualToComparingFieldByField(USER_NEW_MEAL);
        assertThat(service.get(newId, USER_ID)).isEqualToComparingFieldByField(USER_NEW_MEAL);
    }
}