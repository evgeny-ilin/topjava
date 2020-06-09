<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <link href="./resources/css/meal.css" rel="stylesheet" type="text/css" media="screen"/>
    <title>${meal.id == 0 ? 'Add meal' : 'Edit meal'}</title>
</head>
<body id="bkg" style="display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;">

<form class="contact_form" action="meals" method="POST" name="contact_form">
    <input type="hidden" name="id" value="${meal.id}">
    <c:set var="browserName" value="${header[\"user-agent\"]}" scope="session"/>
    <fmt:parseDate value="${meal.dateTime}" type="date" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
    <c:set var="browserDate" value="${meal.dateTime}"/>
    <c:if test="${browserName.contains('Firefox') == true}">
        <fmt:formatDate value="${parsedDate}" type="date" pattern="yyyy-MM-dd HH:mm" var="browserDate"/>
    </c:if>
    <ul>
        <li>
            <h2>Input data</h2>
            <span class="required_notification">* Denotes Required Field</span>
        </li>
        <li>
            <label for="dateTime">dateTime</label>
            <input
                    placeholder="31-12-2020 14:55"
                    type="datetime-local"
                    name="dateTime"
                    id="dateTime"
                    value="${browserDate}"
                    required>
            <span class="form_hint">DD-MM-YYYY HH:MM</span>
        </li>
        <li>
            <label for="description">description</label>
            <textarea name="description" cols="40" rows="6" required>${meal.description}</textarea>
        </li>
        <li>
            <label for="calories">calories</label>
            <input type="number" min="1" name="calories" id="calories" value="${meal.calories}" required>
        </li>
        <li>
            <%--            <input  type="submit" value=${meal.id == 0 ? "Add new meal" : "Edit meal"}>--%>
            <button class="submit" type="submit">${meal.id == 0 ? "Add new meal" : "Edit meal"}</button>
        </li>
    </ul>
</form>
</body>
</html>
