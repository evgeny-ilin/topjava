<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <link href="./resources/css/style.css" rel="stylesheet" type="text/css"/>
    <title>Meals</title>
</head>
<body class="ten" id="bkg">
<p style="text-align: center;">
    <a style="color: #2335e7; border-radius: 25px; border: 3px solid #1a2ceb;" href="index.html">Home</a>
    <a style="color: #2335e7; border-radius: 25px; border: 3px solid #1a2ceb;"
       href="${pageContext.request.contextPath}/meals?action=add">Add new meal</a>
</p>

<table>
    <tr style="font-size: 230%; font-weight: bold">
        <th>dateTime</th>
        <th>description</th>
        <th>calories</th>
        <th>action</th>
    </tr>
    <jsp:useBean id="mealToList" type="java.util.List<ru.javawebinar.topjava.model.MealTo>" scope="request"/>
    <c:forEach items="${mealToList}" var="mealTo">
        <tr style="color: ${mealTo.excess ? 'C933FF' : '3BDB49'}">
            <fmt:parseDate value="${mealTo.dateTime}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
            <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy-MM-dd HH:mm" var="dateTime"/>
            <td>${dateTime}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td>
                <a href="${pageContext.request.contextPath}/meals?action=edit&id=${mealTo.id}">edit</a>
                <a href="${pageContext.request.contextPath}/meals?action=delete&id=${mealTo.id}">delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>