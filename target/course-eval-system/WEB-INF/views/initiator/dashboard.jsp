<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Initiator Dashboard" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#128203; My Surveys</h1>
    <a href="${pageContext.request.contextPath}/initiator/surveys/new" class="btn btn-primary">+ Create Survey</a>
</div>
<%@ include file="../common/alerts.jsp" %>

<c:choose>
<c:when test="${empty surveys}">
    <div class="empty-state">
        <p>&#128203; No surveys yet. Create your first survey!</p>
        <a href="${pageContext.request.contextPath}/initiator/surveys/new" class="btn btn-primary">Create Survey</a>
    </div>
</c:when>
<c:otherwise>
<div class="survey-grid">
    <c:forEach var="s" items="${surveys}">
    <div class="survey-card">
        <div class="survey-card-header">
            <span class="badge badge-${s.status == 'PUBLISHED' ? 'success' : s.status == 'DRAFT' ? 'warning' : 'secondary'}">${s.status}</span>
            <span class="badge">${s.courseCode}</span>
        </div>
        <h3>${s.title}</h3>
        <p class="text-muted">${s.courseTitle}</p>
        <p class="survey-meta">&#128100; ${s.responseCount} responses &bull; ${s.accessType}</p>
        <div class="survey-card-actions">
            <a href="${pageContext.request.contextPath}/initiator/surveys/${s.id}/questions" class="btn btn-sm btn-secondary">Questions</a>
            <a href="${pageContext.request.contextPath}/initiator/surveys/${s.id}/results" class="btn btn-sm btn-info">Results</a>
            <a href="${pageContext.request.contextPath}/initiator/surveys/edit/${s.id}" class="btn btn-sm btn-secondary">Edit</a>
            <c:if test="${s.status == 'DRAFT'}">
                <form method="post" action="${pageContext.request.contextPath}/initiator/surveys/publish/${s.id}" style="display:inline">
                    <button class="btn btn-sm btn-success">Publish</button>
                </form>
            </c:if>
            <c:if test="${s.status == 'PUBLISHED'}">
                <form method="post" action="${pageContext.request.contextPath}/initiator/surveys/close/${s.id}" style="display:inline">
                    <button class="btn btn-sm btn-warning">Close</button>
                </form>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/initiator/surveys/delete/${s.id}" style="display:inline"
                  onsubmit="return confirm('Delete this survey?')">
                <button class="btn btn-sm btn-danger">Delete</button>
            </form>
        </div>
    </div>
    </c:forEach>
</div>
</c:otherwise>
</c:choose>

<%@ include file="../common/footer.jsp" %>
