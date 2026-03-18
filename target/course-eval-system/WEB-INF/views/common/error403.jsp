<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Access Denied" />
<%@ include file="header.jsp" %>
<div class="error-page">
    <h1>403</h1>
    <p>You don't have permission to access this page.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go Home</a>
</div>
<%@ include file="footer.jsp" %>
