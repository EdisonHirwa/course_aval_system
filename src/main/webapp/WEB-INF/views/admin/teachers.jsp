<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Manage Teachers" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#128101; Teachers</h1>
</div>
<%@ include file="../common/alerts.jsp" %>

<table class="table">
    <thead>
        <tr><th>Name</th><th>Username</th><th>Email</th><th>Status</th><th>Registered</th><th>Actions</th></tr>
    </thead>
    <tbody>
        <c:forEach var="t" items="${teachers}">
        <tr>
            <td>${t.fullName}</td>
            <td>${t.username}</td>
            <td>${t.email}</td>
            <td>
                <span class="badge badge-${t.status == 'ACTIVE' ? 'success' : t.status == 'PENDING' ? 'warning' : 'danger'}">
                    ${t.status}
                </span>
            </td>
            <td>${t.createdAt}</td>
            <td>
                <c:if test="${t.status == 'PENDING'}">
                    <form method="post" action="${pageContext.request.contextPath}/admin/teachers/approve/${t.id}" style="display:inline">
                        <button class="btn btn-sm btn-success">Approve</button>
                    </form>
                    <form method="post" action="${pageContext.request.contextPath}/admin/teachers/reject/${t.id}" style="display:inline">
                        <button class="btn btn-sm btn-danger">Reject</button>
                    </form>
                </c:if>
                <c:if test="${t.status == 'ACTIVE'}">
                    <form method="post" action="${pageContext.request.contextPath}/admin/teachers/reject/${t.id}" style="display:inline">
                        <button class="btn btn-sm btn-warning">Suspend</button>
                    </form>
                </c:if>
                <form method="post" action="${pageContext.request.contextPath}/admin/teachers/delete/${t.id}" style="display:inline"
                      onsubmit="return confirm('Delete this teacher?')">
                    <button class="btn btn-sm btn-danger">Delete</button>
                </form>
            </td>
        </tr>
        </c:forEach>
        <c:if test="${empty teachers}">
        <tr><td colspan="6" class="text-center text-muted">No teachers registered yet.</td></tr>
        </c:if>
    </tbody>
</table>

<%@ include file="../common/footer.jsp" %>
