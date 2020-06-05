<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <c:if test="${empty meal.id}">
        <title>Add</title>
    </c:if>
    <c:if test="${!empty meal.id}">
        <title>Edit</title>
    </c:if>
</head>
<body>
<form action="meals" method="POST">
    <input type="hidden" name="id" value="${meal.id}">
    <fmt:parseDate value="${meal.dateTime}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
    <fmt:formatDate value="${parsedDate}" type="date" pattern="dd.MM.yyyy HH:mm" var="dateTime"/>
    <label for="dateTime">dateTime</label>
    <input type="text" name="dateTime" id="dateTime" value="${dateTime}">
    <label for="description">description</label>
    <input type="text" name="description" id="description" value="${meal.description}">
    <label for="calories">calories</label>
    <input type="text" name="calories" id="calories" value="${meal.calories}">
    <c:if test="${empty meal.id}">
        <input type="submit" value="Add new meal">
    </c:if>
    <c:if test="${!empty meal.id}">
        <input type="submit" value="Edit meal">
    </c:if>
</form>
</body>
</html>
