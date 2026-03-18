<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Manage Courses" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#127979; Courses</h1>
    <a href="${pageContext.request.contextPath}/admin/courses/new" class="btn btn-primary">+ New Course</a>
</div>
<%@ include file="../common/alerts.jsp" %>

<table class="table">
    <thead>
        <tr><th>Code</th><th>Title</th><th>Description</th><th>Actions</th></tr>
    </thead>
    <tbody>
        <c:forEach var="c" items="${courses}">
        <tr>
            <td><span class="badge">${c.code}</span></td>
            <td>${c.title}</td>
            <td>${c.description}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/courses/edit/${c.id}" class="btn btn-sm btn-secondary">Edit</a>
                <a href="${pageContext.request.contextPath}/admin/courses/${c.id}/assign" class="btn btn-sm btn-info">Assign Teachers</a>
                <form method="post" action="${pageContext.request.contextPath}/admin/courses/delete/${c.id}" style="display:inline"
                      onsubmit="return confirm('Delete this course?')">
                    <button class="btn btn-sm btn-danger">Delete</button>
                </form>
            </td>
        </tr>
        </c:forEach>
        <c:if test="${empty courses}">
        <tr><td colspan="4" class="text-center text-muted">No courses yet. Create one!</td></tr>
        </c:if>
    </tbody>
</table>

<%@ include file="../common/footer.jsp" %>
