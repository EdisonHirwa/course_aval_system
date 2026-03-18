<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Login" />
<%@ include file="header.jsp" %>

<div class="auth-card">
    <h2>&#128274; Sign In</h2>
    <%@ include file="alerts.jsp" %>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="form-group">
            <label>Username</label>
            <input type="text" name="username" required placeholder="Enter username" />
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required placeholder="Enter password" />
        </div>
        <button type="submit" class="btn btn-primary btn-block">Login</button>
    </form>
    <p class="auth-footer">Teacher? <a href="${pageContext.request.contextPath}/register">Register here</a></p>
    <p class="auth-footer text-muted small">Demo: admin / admin123</p>
</div>

<%@ include file="footer.jsp" %>
