<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Page Not Found" />
<%@ include file="header.jsp" %>
<div class="error-page">
    <h1>404</h1>
    <p>The page you're looking for doesn't exist.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go Home</a>
</div>
<%@ include file="footer.jsp" %>
