<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="All Surveys" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#128203; All Surveys</h1>
</div>

<table class="table">
    <thead>
        <tr><th>Title</th><th>Course</th><th>Initiator</th><th>Access</th><th>Status</th><th>Responses</th><th>Created</th></tr>
    </thead>
    <tbody>
        <c:forEach var="s" items="${surveys}">
        <tr>
            <td>${s.title}</td>
            <td><span class="badge">${s.courseCode}</span> ${s.courseTitle}</td>
            <td>${s.initiatorName}</td>
            <td>${s.accessType}</td>
            <td><span class="badge badge-${s.status == 'PUBLISHED' ? 'success' : s.status == 'DRAFT' ? 'warning' : 'secondary'}">${s.status}</span></td>
            <td>${s.responseCount}</td>
            <td>${s.createdAt}</td>
        </tr>
        </c:forEach>
        <c:if test="${empty surveys}">
        <tr><td colspan="7" class="text-center text-muted">No surveys found.</td></tr>
        </c:if>
    </tbody>
</table>

<%@ include file="../common/footer.jsp" %>
