<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Available Surveys" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#128203; Available Surveys</h1>
</div>
<%@ include file="../common/alerts.jsp" %>

<c:choose>
<c:when test="${empty surveys}">
    <div class="empty-state">
        <p>&#128203; No surveys are currently available.</p>
    </div>
</c:when>
<c:otherwise>
<div class="survey-grid">
    <c:forEach var="s" items="${surveys}">
    <div class="survey-card">
        <div class="survey-card-header">
            <span class="badge">${s.courseCode}</span>
            <span class="badge badge-${s.accessType == 'GUEST' ? 'info' : 'warning'}">
                ${s.accessType == 'GUEST' ? 'Open' : 'Login Required'}
            </span>
        </div>
        <h3>${s.title}</h3>
        <p class="text-muted">${s.courseTitle}</p>
        <c:if test="${not empty s.description}">
            <p class="survey-desc">${s.description}</p>
        </c:if>
        <div class="survey-card-actions">
            <a href="${pageContext.request.contextPath}/survey/respond/${s.id}" class="btn btn-primary btn-sm">
                &#9998; Take Survey
            </a>
        </div>
    </div>
    </c:forEach>
</div>
</c:otherwise>
</c:choose>

<%@ include file="../common/footer.jsp" %>
