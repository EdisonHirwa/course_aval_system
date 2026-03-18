<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Admin Dashboard" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#9881; Administrator Dashboard</h1>
</div>
<%@ include file="../common/alerts.jsp" %>

<div class="stats-grid">
    <div class="stat-card">
        <div class="stat-icon">&#127979;</div>
        <div class="stat-number">${totalCourses}</div>
        <div class="stat-label">Courses</div>
        <a href="${pageContext.request.contextPath}/admin/courses" class="stat-link">Manage</a>
    </div>
    <div class="stat-card">
        <div class="stat-icon">&#128101;</div>
        <div class="stat-number">${totalUsers}</div>
        <div class="stat-label">Users</div>
        <a href="${pageContext.request.contextPath}/admin/users" class="stat-link">Manage</a>
    </div>
    <div class="stat-card">
        <div class="stat-icon">&#128203;</div>
        <div class="stat-number">${totalSurveys}</div>
        <div class="stat-label">Surveys</div>
        <a href="${pageContext.request.contextPath}/admin/surveys" class="stat-link">View All</a>
    </div>
    <div class="stat-card stat-card-warning">
        <div class="stat-icon">&#9203;</div>
        <div class="stat-number">${pendingTeachers.size()}</div>
        <div class="stat-label">Pending Approvals</div>
        <a href="${pageContext.request.contextPath}/admin/teachers" class="stat-link">Review</a>
    </div>
</div>

<c:if test="${not empty pendingTeachers}">
<div class="section">
    <h2>&#9888; Pending Teacher Approvals</h2>
    <table class="table">
        <thead>
            <tr><th>Name</th><th>Username</th><th>Email</th><th>Registered</th><th>Actions</th></tr>
        </thead>
        <tbody>
            <c:forEach var="t" items="${pendingTeachers}">
            <tr>
                <td>${t.fullName}</td>
                <td>${t.username}</td>
                <td>${t.email}</td>
                <td>${t.createdAt}</td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/admin/teachers/approve/${t.id}" style="display:inline">
                        <button class="btn btn-success btn-sm">Approve</button>
                    </form>
                    <form method="post" action="${pageContext.request.contextPath}/admin/teachers/reject/${t.id}" style="display:inline">
                        <button class="btn btn-danger btn-sm">Reject</button>
                    </form>
                </td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</c:if>

<%@ include file="../common/footer.jsp" %>
