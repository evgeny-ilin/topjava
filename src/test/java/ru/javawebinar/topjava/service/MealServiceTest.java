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
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.AbstractTestData.ADMIN_ID;
import static ru.javawebinar.topjava.AbstractTestData.USER_ID;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
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
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertEquals(MEAL, meal);
    }

    @Test
    public void getAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertNull(repository.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> filteredMeals = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30), LocalDate.of(2020, Month.JANUARY, 31), USER_ID);
        assertArrayEquals(USER_MEALS.toArray(), filteredMeals.toArray());
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertArrayEquals(USER_MEALS.toArray(), meals.toArray());
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertEquals(updated, service.get(updated.getId(), USER_ID));
    }

    @Test
    public void updateAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID));
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertEquals(newMeal, created);
    }
}