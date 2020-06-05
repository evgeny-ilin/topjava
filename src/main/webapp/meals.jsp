<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>dateTime</th>
        <th>description</th>
        <th>calories</th>
        <th>excess</th>
    </tr>
    <c:forEach items="${mealToList}" var="mealTo">
        <tr style="color: ${mealTo.excess ? 'red' : 'green'}">
            <td>${mealTo.dateTime}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td>${mealTo.excess}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>