<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${survey.id == 0 ? 'New Survey' : 'Edit Survey'}" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>${survey.id == 0 ? '+ New Survey' : '&#9998; Edit Survey'}</h1>
    <a href="${pageContext.request.contextPath}/initiator/dashboard" class="btn btn-secondary">&#8592; Back</a>
</div>
<%@ include file="../common/alerts.jsp" %>

<div class="form-card">
    <form method="post" action="${pageContext.request.contextPath}/initiator/surveys/save">
        <input type="hidden" name="id" value="${survey.id}" />
        <div class="form-group">
            <label>Survey Title *</label>
            <input type="text" name="title" value="${survey.title}" required placeholder="e.g. End-of-Term Evaluation - CS101" />
        </div>
        <div class="form-group">
            <label>Description</label>
            <textarea name="description" rows="3" placeholder="Brief description of this evaluation...">${survey.description}</textarea>
        </div>
        <div class="form-group">
            <label>Course *</label>
            <select name="courseId" required>
                <option value="">-- Select Course --</option>
                <c:forEach var="c" items="${courses}">
                    <option value="${c.id}" ${survey.courseId == c.id ? 'selected' : ''}>${c.code} - ${c.title}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>Access Type</label>
            <select name="accessType">
                <option value="AUTHENTICATED" ${survey.accessType == 'AUTHENTICATED' ? 'selected' : ''}>Authenticated Users Only</option>
                <option value="GUEST" ${survey.accessType == 'GUEST' ? 'selected' : ''}>Allow Guests (email required)</option>
            </select>
        </div>
        <div class="form-group">
            <label>Status</label>
            <select name="status">
                <option value="DRAFT" ${survey.status == 'DRAFT' || survey.status == null ? 'selected' : ''}>Draft</option>
                <option value="PUBLISHED" ${survey.status == 'PUBLISHED' ? 'selected' : ''}>Published</option>
                <option value="CLOSED" ${survey.status == 'CLOSED' ? 'selected' : ''}>Closed</option>
            </select>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">${survey.id == 0 ? 'Create & Add Questions' : 'Save Changes'}</button>
            <a href="${pageContext.request.contextPath}/initiator/dashboard" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
