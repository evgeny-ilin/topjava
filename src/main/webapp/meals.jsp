<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
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
        <th>action</th>
    </tr>
    <c:forEach items="${mealToList}" var="mealTo">
        <tr style="color: ${mealTo.excess ? 'red' : 'green'}">
            <fmt:parseDate value="${mealTo.dateTime}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
            <fmt:formatDate value="${parsedDate}" type="date" pattern="dd.MM.yyyy HH:mm" var="dateTime"/>
            <td>${dateTime}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td>${mealTo.excess}</td>
            <td>
                <a href="${pageContext.request.contextPath}/meals?action=edit&mealId=${mealTo.id}">edit</a>
                <a href="${pageContext.request.contextPath}/meals?action=delete&mealId=${mealTo.id}">delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<h2>Add</h2>
<a href="${pageContext.request.contextPath}/meals?action=add">Add new meal</a>
</body>
</html>