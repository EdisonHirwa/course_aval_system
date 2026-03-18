<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Already Responded" />
<%@ include file="../common/header.jsp" %>

<div class="empty-state" style="margin-top:4rem;">
    <div style="font-size:4rem;">&#10003;</div>
    <h2>You've already completed this survey</h2>
    <p class="text-muted">You have already submitted a response for: <strong>${survey.title}</strong></p>
    <a href="${pageContext.request.contextPath}/survey/list" class="btn btn-primary">Browse Other Surveys</a>
</div>

<%@ include file="../common/footer.jsp" %>
