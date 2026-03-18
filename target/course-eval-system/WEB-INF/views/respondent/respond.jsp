<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Respond to Survey" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#9998; ${survey.title}</h1>
    <a href="${pageContext.request.contextPath}/survey/list" class="btn btn-secondary">&#8592; Back</a>
</div>

<div class="survey-info-bar">
    <span>&#127979; ${survey.courseCode} — ${survey.courseTitle}</span>
    <span class="badge">${survey.accessType == 'GUEST' ? 'Guest Access' : 'Authenticated'}</span>
</div>

<c:if test="${not empty survey.description}">
<div class="survey-description-box">
    ${survey.description}
</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/survey/respond/${survey.id}/submit" id="surveyForm">

    <!-- Guest email field -->
    <c:if test="${survey.accessType == 'GUEST' && sessionScope.loggedUser == null}">
    <div class="form-card guest-email-card">
        <div class="form-group">
            <label>&#128231; Your Email <span class="text-muted">(for confirmation)</span></label>
            <input type="email" name="guestEmail" required placeholder="your@email.com" />
        </div>
    </div>
    </c:if>

    <!-- Questions -->
    <c:forEach var="q" items="${questions}" varStatus="status">
    <div class="survey-question-card">
        <div class="question-header">
            <span class="question-number">Q${status.index + 1}</span>
            <span class="badge badge-info">${q.questionType}</span>
        </div>
        <p class="question-text">${q.questionText}</p>

        <c:choose>
            <c:when test="${q.questionType == 'TEXT'}">
                <textarea name="question_${q.id}" rows="3" placeholder="Write your answer here..." class="full-width"></textarea>
            </c:when>
            <c:when test="${q.questionType == 'SINGLE'}">
                <div class="options-group">
                    <c:forEach var="opt" items="${q.options}">
                    <label class="radio-option">
                        <input type="radio" name="question_${q.id}" value="${opt.id}" required />
                        <span>${opt.optionText}</span>
                    </label>
                    </c:forEach>
                </div>
            </c:when>
            <c:when test="${q.questionType == 'MULTIPLE'}">
                <div class="options-group">
                    <c:forEach var="opt" items="${q.options}">
                    <label class="checkbox-option">
                        <input type="checkbox" name="question_${q.id}_opt${opt.id}" value="${opt.id}" />
                        <span>${opt.optionText}</span>
                    </label>
                    </c:forEach>
                </div>
            </c:when>
        </c:choose>
    </div>
    </c:forEach>

    <div class="form-actions" style="text-align:center;margin:2rem 0;">
        <button type="submit" class="btn btn-primary btn-lg"
                onclick="return confirm('Submit your responses? This cannot be undone.')">
            &#10003; Submit Survey
        </button>
    </div>
</form>

<%@ include file="../common/footer.jsp" %>
