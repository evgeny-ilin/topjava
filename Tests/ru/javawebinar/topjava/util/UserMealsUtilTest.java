package ru.javawebinar.topjava.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtilTest {

    private List<UserMeal> meals;

    @Before
    public void setUp() throws Exception {
        meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
    }

    //Cycles---------------------------------------------------------------------------------------------
    @Test
    public void filteredByCyclesBreakfast() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false));
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByCyclesEmptyList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        List<UserMeal> mealsEmptyList = new ArrayList<>();

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCycles(mealsEmptyList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByCyclesOneInList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false));
        meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByCyclesNoneInList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(9, 0), 2000);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByCyclesDinner() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, false));
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCycles(meals, LocalTime.of(19, 0), LocalTime.of(22, 0), 2000);

        Collections.sort(expected, Comparator.comparing(UserMealWithExcess::getDateTime));
        Collections.sort(actual, Comparator.comparing(UserMealWithExcess::getDateTime));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByCyclesMidnightDinner() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCycles(meals, LocalTime.of(0, 0), LocalTime.of(2, 0), 2000);

        Collections.sort(expected, Comparator.comparing(UserMealWithExcess::getDateTime));
        Collections.sort(actual, Comparator.comparing(UserMealWithExcess::getDateTime));

        Assert.assertEquals(expected, actual);
    }

    //Cycles Optional2-------------------------------------------------------------------------------------
    @Test
    public void filteredByCyclesOptional2Breakfast() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false));
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Collections.sort(expected, Comparator.comparing(UserMealWithExcess::getDateTime));
        Collections.sort(actual, Comparator.comparing(UserMealWithExcess::getDateTime));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByCyclesOptional2Dinner() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, false));
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCyclesOptional2(meals, LocalTime.of(19, 0), LocalTime.of(22, 0), 2000);

        Collections.sort(expected, Comparator.comparing(UserMealWithExcess::getDateTime));
        Collections.sort(actual, Comparator.comparing(UserMealWithExcess::getDateTime));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByCyclesOptional2MidnightDinner() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCyclesOptional2(meals, LocalTime.of(0, 0), LocalTime.of(2, 0), 2000);

        Collections.sort(expected, Comparator.comparing(UserMealWithExcess::getDateTime));
        Collections.sort(actual, Comparator.comparing(UserMealWithExcess::getDateTime));

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void filteredByCyclesOptional2EmptyList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        List<UserMeal> mealsEmptyList = new ArrayList<>();

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCyclesOptional2(mealsEmptyList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByCyclesOptional2OneInList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false));
        meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByCyclesOptional2NoneInList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(9, 0), 2000);
        Assert.assertEquals(expected, actual);
    }

    //Streams--------------------------------------------------------------------------------------------
    @Test
    public void filteredByStreamsBreakfast() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false));
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsEmptyList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        List<UserMeal> mealsEmptyList = new ArrayList<>();

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreams(mealsEmptyList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsOneInList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false));
        meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsNoneInList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(9, 0), 2000);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsDinner() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, false));
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreams(meals, LocalTime.of(19, 0), LocalTime.of(22, 0), 2000);

        Collections.sort(expected, Comparator.comparing(UserMealWithExcess::getDateTime));
        Collections.sort(actual, Comparator.comparing(UserMealWithExcess::getDateTime));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsMidnightDinner() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(2, 0), 2000);

        Collections.sort(expected, Comparator.comparing(UserMealWithExcess::getDateTime));
        Collections.sort(actual, Comparator.comparing(UserMealWithExcess::getDateTime));

        Assert.assertEquals(expected, actual);
    }

    //Streams Optional2--------------------------------------------------------------------------------------
    @Test
    public void filteredByStreamsOptional2Breakfast() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false));
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsOptional2EmptyList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        List<UserMeal> mealsEmptyList = new ArrayList<>();

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreamsOptional2(mealsEmptyList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsOptional2OneInList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false));
        meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsOptional2NoneInList() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(9, 0), 2000);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsOptional2Dinner() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, false));
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreamsOptional2(meals, LocalTime.of(19, 0), LocalTime.of(22, 0), 2000);

        Collections.sort(expected, Comparator.comparing(UserMealWithExcess::getDateTime));
        Collections.sort(actual, Comparator.comparing(UserMealWithExcess::getDateTime));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreamsOptional2MidnightDinner() {
        List<UserMealWithExcess> expected = new ArrayList<>();
        expected.add(new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, true));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreamsOptional2(meals, LocalTime.of(0, 0), LocalTime.of(2, 0), 2000);

        Collections.sort(expected, Comparator.comparing(UserMealWithExcess::getDateTime));
        Collections.sort(actual, Comparator.comparing(UserMealWithExcess::getDateTime));

        Assert.assertEquals(expected, actual);
    }
}