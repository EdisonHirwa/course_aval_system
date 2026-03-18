<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Assign Teachers" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#128101; Assign Teachers to ${course.code}: ${course.title}</h1>
    <a href="${pageContext.request.contextPath}/admin/courses" class="btn btn-secondary">&#8592; Back</a>
</div>
<%@ include file="../common/alerts.jsp" %>

<div class="form-card">
    <form method="post" action="${pageContext.request.contextPath}/admin/courses/${course.id}/assign">
        <p class="text-muted">Select one or more teachers to assign to this course.</p>
        <div class="checkbox-list">
            <c:forEach var="t" items="${allTeachers}">
                <c:if test="${t.status == 'ACTIVE'}">
                <label class="checkbox-item">
                    <input type="checkbox" name="teacherIds" value="${t.id}"
                        <c:forEach var="aid" items="${assignedIds}">
                            <c:if test="${aid == t.id}">checked</c:if>
                        </c:forEach> />
                    <span>${t.fullName} <small class="text-muted">(${t.username})</small></span>
                </label>
                </c:if>
            </c:forEach>
            <c:if test="${empty allTeachers}">
                <p class="text-muted">No approved teachers available.</p>
            </c:if>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Assignments</button>
        </div>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
