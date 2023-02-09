<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table, th, td {
            border: 1px solid black;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
    <c:forEach items="${mealsTo}" var="mealTo">
        <jsp:useBean id="mealTo" scope="request" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr style="color:${mealTo.excess ? "red" : "green"}">
            <td>${f:formatLocalDateTime(mealTo.dateTime, 'dd.MM.yyyy HH:mm')}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>