<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Teacher Dashboard" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#128218; Teacher Dashboard</h1>
</div>
<%@ include file="../common/alerts.jsp" %>

<div class="section">
    <h2>My Courses</h2>
    <c:choose>
    <c:when test="${empty courses}">
        <p class="text-muted">You have not been assigned to any courses yet.</p>
    </c:when>
    <c:otherwise>
    <div class="course-tags">
        <c:forEach var="c" items="${courses}">
            <span class="badge badge-lg">${c.code} — ${c.title}</span>
        </c:forEach>
    </div>
    </c:otherwise>
    </c:choose>
</div>

<div class="section">
    <h2>Surveys for My Courses</h2>
    <c:choose>
    <c:when test="${empty surveys}">
        <div class="empty-state">
            <p>&#128203; No surveys linked to your courses yet.</p>
        </div>
    </c:when>
    <c:otherwise>
    <table class="table">
        <thead>
            <tr><th>Survey</th><th>Course</th><th>Access</th><th>Status</th><th>Responses</th><th>Actions</th></tr>
        </thead>
        <tbody>
            <c:forEach var="s" items="${surveys}">
            <tr>
                <td>${s.title}</td>
                <td><span class="badge">${s.courseCode}</span></td>
                <td>${s.accessType}</td>
                <td><span class="badge badge-${s.status == 'PUBLISHED' ? 'success' : s.status == 'DRAFT' ? 'warning' : 'secondary'}">${s.status}</span></td>
                <td>${s.responseCount}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/teacher/surveys/${s.id}/results" class="btn btn-sm btn-info">View Results</a>
                    <c:if test="${s.status == 'PUBLISHED'}">
                        <a href="${pageContext.request.contextPath}/survey/respond/${s.id}" class="btn btn-sm btn-secondary">Preview</a>
                    </c:if>
                </td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    </c:otherwise>
    </c:choose>
</div>

<%@ include file="../common/footer.jsp" %>
