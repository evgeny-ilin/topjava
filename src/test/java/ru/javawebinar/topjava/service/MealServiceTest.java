package ru.javawebinar.topjava.service;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    private static Map<String, Long> timeMap = new HashMap<>();
    private static long start;
    @Rule
    public final TestRule watchman = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            start = System.nanoTime();
            super.starting(description);
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
            long time = System.nanoTime() - start;
            timeMap.put(description.getMethodName(), time);
            log.info(String.format("Test %s completed %d ns", description.getMethodName(), time));
        }
    };
    @Autowired
    private MealService mealService;

    @AfterClass
    public static void finish() {
        String format = "|%1$-25s|%2$-10s|\n";
        String line = String.join("", Collections.nCopies(37, "-"));
        StringBuilder sb = new StringBuilder().append("\n");
        sb.append(line).append("\n");
        sb.append(String.format(format, "Name of test", "Time"));
        sb.append(line).append("\n");
        timeMap.forEach((key, value) -> sb.append(String.format(format, key, value)));
        sb.append(line);
        log.info(sb.toString());
    }

    @Test
    @Fetch(FetchMode.SELECT)
    public void get() throws Exception {
        Meal actual = mealService.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
    }

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
        assertThrows(NotFoundException.class, () -> mealService.update(MEAL1, ADMIN_ID));
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