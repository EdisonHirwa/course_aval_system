<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Survey Results" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#128202; Results: ${survey.title}</h1>
    <a href="javascript:history.back()" class="btn btn-secondary">&#8592; Back</a>
</div>

<div class="results-meta">
    <span>&#127979; <strong>${survey.courseCode}</strong> — ${survey.courseTitle}</span>
    <span>&#128100; <strong>${totalRespondents}</strong> respondents</span>
    <span class="badge badge-${survey.status == 'PUBLISHED' ? 'success' : 'secondary'}">${survey.status}</span>
</div>

<c:choose>
<c:when test="${totalRespondents == 0}">
    <div class="empty-state">
        <p>&#128203; No responses yet.</p>
    </div>
</c:when>
<c:otherwise>
<c:forEach var="q" items="${questions}" varStatus="status">
<div class="results-question-card">
    <div class="question-header">
        <span class="question-number">Q${status.index + 1}</span>
        <span class="badge badge-info">${q.questionType}</span>
    </div>
    <p class="question-text">${q.questionText}</p>

    <c:choose>
    <c:when test="${q.questionType == 'TEXT'}">
        <div class="text-answers">
            <c:forEach var="opt" items="${q.options}">
                <div class="text-answer-item">&#8220;${opt.optionText}&#8221;</div>
            </c:forEach>
            <c:if test="${empty q.options}">
                <p class="text-muted">No text answers submitted.</p>
            </c:if>
        </div>
    </c:when>
    <c:otherwise>
        <c:forEach var="opt" items="${q.options}">
            <c:set var="pct" value="${totalRespondents > 0 ? (opt.voteCount * 100 / totalRespondents) : 0}" />
            <div class="result-bar-row">
                <span class="result-option-label">${opt.optionText}</span>
                <div class="result-bar-track">
                    <div class="result-bar-fill" style="width:${pct}%"></div>
                </div>
                <span class="result-bar-count">${opt.voteCount} <small>(${pct}%)</small></span>
            </div>
        </c:forEach>
    </c:otherwise>
    </c:choose>
</div>
</c:forEach>
</c:otherwise>
</c:choose>

<%@ include file="../common/footer.jsp" %>
