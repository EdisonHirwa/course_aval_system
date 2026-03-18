<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Account Registration" />
<%@ include file="header.jsp" %>

<div class="auth-card">
    <h2>&#128203; Create Account</h2>
    <%@ include file="alerts.jsp" %>
    <form method="post" action="${pageContext.request.contextPath}/register">
        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="fullName" required placeholder="Your full name" />
        </div>
        <div class="form-group">
            <label>Register As</label>
            <select name="role" class="form-control" required>
                <option value="RESPONDENT">Student / Respondent</option>
                <option value="TEACHER">Teacher</option>
                <option value="INITIATOR">Survey Initiator</option>
                <option value="ADMIN">Administrator</option>
            </select>
        </div>
        <div class="form-group">
            <label>Username</label>
            <input type="text" name="username" required placeholder="Choose a username" />
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" required placeholder="your@email.com" />
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required placeholder="Min. 6 characters" minlength="6" />
        </div>
        <button type="submit" class="btn btn-primary btn-block">Register</button>
    </form>
    <p class="auth-footer">Already registered? <a href="${pageContext.request.contextPath}/login">Login</a></p>
    <p class="auth-footer text-muted small">Note: Teacher and Initiator accounts require admin approval.</p>
</div>

<%@ include file="footer.jsp" %>
