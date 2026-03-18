<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${course.id == 0 ? 'New Course' : 'Edit Course'}" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>${course.id == 0 ? '+ New Course' : '&#9998; Edit Course'}</h1>
    <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-secondary">&#8592; Back</a>
</div>

<div class="form-card">
    <form method="post" action="${pageContext.request.contextPath}/admin/courses/save">
        <input type="hidden" name="id" value="${course.id}" />
        <div class="form-group">
            <label>Course Code *</label>
            <input type="text" name="code" value="${course.code}" required placeholder="e.g. CS101" />
        </div>
        <div class="form-group">
            <label>Course Title *</label>
            <input type="text" name="title" value="${course.title}" required placeholder="e.g. Introduction to Programming" />
        </div>
        <div class="form-group">
            <label>Description</label>
            <textarea name="description" rows="4" placeholder="Brief description of this course...">${course.description}</textarea>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Course</button>
            <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
