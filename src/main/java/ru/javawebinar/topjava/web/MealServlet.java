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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private static String MEALS = "meals";
    private static String MEAL = "meal.jsp";
    private MealDao dao;
    private static final DateTimeFormatter formatterFirefox = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm");
    private static final DateTimeFormatter formatterOthers = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void init() throws ServletException {
        dao = new MealDaoInMemoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet");
        String action = request.getParameter("action");
        switch (action == null ? "" : action.toLowerCase()) {
            case "delete": {
                int id;
                id = Integer.parseInt(request.getParameter("id"));
                log.debug("Delete meal id = {}", id);
                dao.delete(id);
                response.sendRedirect(MEALS);
                break;
            }
            case "edit": {
                int id;
                Meal meal;
                id = Integer.parseInt(request.getParameter("id"));
                log.debug("Edit meal id = {}", id);
                meal = dao.get(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher(MEAL).forward(request, response);
                break;
            }
            case "add": {
                Meal meal;
                meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1);
                request.setAttribute("meal", meal);
                log.debug("Add meal: {}", meal);
                request.getRequestDispatcher(MEAL).forward(request, response);
                break;
            }
            default: {
                refreshMeals(request, response);
            }
        }
    }

    private void refreshMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Refresh meals");
        List<Meal> meals = dao.getAll();
        log.trace("Meals {}", meals.stream().map(Meal::toString).limit(20).collect(Collectors.joining(", ")));
        List<MealTo> mealToList = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, 2000);
        log.trace("MealsTo {}", mealToList.stream().map(MealTo::toString).limit(20).collect(Collectors.joining(", ")));
        request.setAttribute("mealToList", mealToList);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doPost");
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime;
        String dateParam = req.getParameter("dateTime").replace(' ', 'T');
        if (req.getHeader("user-agent").contains("Firefox")) {
            dateTime = LocalDateTime.parse(dateParam, formatterFirefox);
            log.debug("dateTime for Firefox {}", dateTime);
        } else {
            dateTime = LocalDateTime.parse(dateParam, formatterOthers);
            log.debug("dateTime for others {}", dateTime);
        }

        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        int id = req.getParameter("id").isEmpty() ? 0 : Integer.parseInt(req.getParameter("id"));
        if (id == 0) {
            log.debug("Add new meal: dateTime = {} description = {} calories = {}", dateTime, description, calories);
            dao.add(new Meal(dateTime, description, calories));
        } else {
            Meal meal = new Meal(id, dateTime, description, calories);
            log.debug("Update meal {}", meal);
            dao.update(meal);
        }
        resp.sendRedirect(MEALS);
    }
}
