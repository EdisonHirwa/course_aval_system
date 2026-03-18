<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="All Users" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#128100; All Users</h1>
</div>
<%@ include file="../common/alerts.jsp" %>

<table class="table">
    <thead>
        <tr><th>Name</th><th>Username</th><th>Email</th><th>Role</th><th>Status</th><th>Actions</th></tr>
    </thead>
    <tbody>
        <c:forEach var="u" items="${users}">
        <tr>
            <td>${u.fullName}</td>
            <td>${u.username}</td>
            <td>${u.email}</td>
            <td><span class="badge badge-info">${u.roleName}</span></td>
            <td><span class="badge badge-${u.status == 'ACTIVE' ? 'success' : u.status == 'PENDING' ? 'warning' : 'danger'}">${u.status}</span></td>
            <td>
                <c:if test="${u.roleName != 'ADMIN'}">
                <form method="post" action="${pageContext.request.contextPath}/admin/users/delete/${u.id}" style="display:inline"
                      onsubmit="return confirm('Delete user ${u.username}?')">
                    <button class="btn btn-sm btn-danger">Delete</button>
                </form>
                </c:if>
            </td>
        </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="../common/footer.jsp" %>
