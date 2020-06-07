package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
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
    private static String MEALS = "meals";
    private static String MEAL = "meal.jsp";
    private MealDao dao;

    @Override
    public void init() throws ServletException {
        dao = new MealDaoInMemoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        int id;
        Meal meal;
        switch (action.toLowerCase()) {
            case "delete":
                id = Integer.parseInt(request.getParameter("id"));
                dao.delete(id);
                response.sendRedirect(MEALS);
                break;
            case "edit":
                id = Integer.parseInt(request.getParameter("id"));
                meal = dao.get(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher(MEAL).forward(request, response);
                break;
            case "add":
                meal = new Meal(LocalDateTime.now(), "", 1);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher(MEAL).forward(request, response);
                break;
            default:
                refreshMeals(request, response);
        }
    }

    private void refreshMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Meal> meals = dao.getAll();
        List<MealTo> mealToList = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("mealToList", mealToList);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime;
        String dateParam = req.getParameter("dateTime").replace(' ', 'T');
        ;
        if (req.getHeader("user-agent").contains("Firefox"))
            dateTime = LocalDateTime.parse(dateParam, DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm"));
        else
            dateTime = LocalDateTime.parse(dateParam, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        int id = req.getParameter("id").isEmpty() ? 0 : Integer.parseInt(req.getParameter("id"));
        if (id == 0) {
            dao.add(new Meal(dateTime, description, calories));
        } else {
            Meal meal = new Meal(id, dateTime, description, calories);
            dao.update(id, meal);
        }
        resp.sendRedirect(MEALS);
    }
}
