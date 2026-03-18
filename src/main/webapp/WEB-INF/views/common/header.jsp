<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle != null ? pageTitle : 'Course Evaluation System'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <div class="nav-brand">
        <a href="${pageContext.request.contextPath}/">&#127979; Course Eval</a>
    </div>
    <div class="nav-links">
        <c:choose>
            <c:when test="${sessionScope.loggedUser != null}">
                <c:if test="${sessionScope.loggedUser.roleName == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/admin/courses">Courses</a>
                    <a href="${pageContext.request.contextPath}/admin/teachers">Teachers</a>
                    <a href="${pageContext.request.contextPath}/admin/users">Users</a>
                    <a href="${pageContext.request.contextPath}/admin/surveys">Surveys</a>
                </c:if>
                <c:if test="${sessionScope.loggedUser.roleName == 'INITIATOR'}">
                    <a href="${pageContext.request.contextPath}/initiator/dashboard">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/initiator/surveys/new">New Survey</a>
                    <a href="${pageContext.request.contextPath}/survey/list">Browse Surveys</a>
                </c:if>
                <c:if test="${sessionScope.loggedUser.roleName == 'TEACHER'}">
                    <a href="${pageContext.request.contextPath}/teacher/dashboard">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/survey/list">Browse Surveys</a>
                </c:if>
                <span class="nav-user">&#128100; ${sessionScope.loggedUser.fullName}</span>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Logout</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/survey/list">Surveys</a>
                <a href="${pageContext.request.contextPath}/login">Login</a>
                <a href="${pageContext.request.contextPath}/register">Register</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>
<div class="container">
