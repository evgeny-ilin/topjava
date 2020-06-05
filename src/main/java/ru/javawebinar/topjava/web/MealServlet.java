package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private static String MEALS = "meals.jsp";
    private static String MEAL = "meal.jsp";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private MealService mealService = new MealServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            mealService.deleteMeal(mealId);
            refreshMeals(request, response);
        } else if (action.equalsIgnoreCase("edit")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealService.getMeal(mealId);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher(MEAL).forward(request, response);
        } else if (action.equalsIgnoreCase("add")) {
            request.getRequestDispatcher(MEAL).forward(request, response);
        } else {
            refreshMeals(request, response);
        }
    }

    private void refreshMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Meal> meals = mealService.getMeals();
        List<MealTo> mealToList = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("mealToList", mealToList);
        request.getRequestDispatcher(MEALS).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), formatter);
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        int id = req.getParameter("id").isEmpty() ? 0 : Integer.parseInt(req.getParameter("id"));
        if (id == 0) {
            Meal meal = new Meal(dateTime, description, calories);
            mealService.addMeal(meal);
        } else {
            Meal meal = mealService.getMeal(id);
            meal.setDateTime(dateTime);
            meal.setDescription(description);
            meal.setCalories(calories);
            mealService.updateMeal(meal);
        }
        refreshMeals(req, resp);
    }
}
