package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

@Controller
public class MealRestController extends AbstractMealController {
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        if (startDate == null) startDate = LocalDate.MIN;
        if (endDate == null) endDate = LocalDate.MAX;
        if (startTime == null) startTime = LocalTime.MIN;
        if (endTime == null) endTime = LocalTime.MAX;

        LocalDate finalStartDate = startDate;
        LocalDate finalEndDate = endDate;
        LocalTime finalStartTime = startTime;
        LocalTime finalEndTime = endTime;
        Predicate<Meal> filter = meal -> meal.getDate().isAfter(finalStartDate) &&
                meal.getDate().isBefore(finalEndDate) &&
                DateTimeUtil.isBetweenHalfOpen(meal.getTime(), finalStartTime, finalEndTime);
        return service.getFiltered(filter);
    }

}