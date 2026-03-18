<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Manage Questions" />
<%@ include file="../common/header.jsp" %>

<div class="page-header">
    <h1>&#10067; Questions: ${survey.title}</h1>
    <a href="${pageContext.request.contextPath}/initiator/dashboard" class="btn btn-secondary">&#8592; Dashboard</a>
</div>
<%@ include file="../common/alerts.jsp" %>

<!-- Existing questions -->
<c:if test="${not empty questions}">
<div class="section">
    <h2>Current Questions</h2>
    <c:forEach var="q" items="${questions}" varStatus="status">
    <div class="question-card">
        <div class="question-header">
            <span class="question-number">Q${status.index + 1}</span>
            <span class="badge badge-info">${q.questionType}</span>
            <form method="post" action="${pageContext.request.contextPath}/initiator/surveys/${survey.id}/questions/delete/${q.id}"
                  style="display:inline;margin-left:auto" onsubmit="return confirm('Delete this question?')">
                <button class="btn btn-sm btn-danger">&#128465;</button>
            </form>
        </div>
        <p class="question-text">${q.questionText}</p>
        <c:if test="${not empty q.options}">
        <ul class="option-list">
            <c:forEach var="opt" items="${q.options}">
                <li>&#9675; ${opt.optionText}</li>
            </c:forEach>
        </ul>
        </c:if>
    </div>
    </c:forEach>
</div>
</c:if>

<!-- Add new question -->
<div class="form-card">
    <h2>+ Add Question</h2>
    <form method="post" action="${pageContext.request.contextPath}/initiator/surveys/${survey.id}/questions/add" id="questionForm">
        <div class="form-group">
            <label>Question Text *</label>
            <textarea name="questionText" rows="2" required placeholder="Enter your question here..."></textarea>
        </div>
        <div class="form-group">
            <label>Question Type</label>
            <select name="questionType" id="questionType" onchange="toggleOptions()">
                <option value="SINGLE">Single Choice</option>
                <option value="MULTIPLE">Multiple Choice</option>
                <option value="TEXT">Open Text</option>
            </select>
        </div>
        <div id="optionsSection">
            <label>Answer Options <span class="text-muted">(one per line, min 2)</span></label>
            <div id="optionsList">
                <div class="option-input-row">
                    <input type="text" name="optionTexts" placeholder="Option 1" />
                    <button type="button" class="btn btn-sm btn-secondary" onclick="addOption()">+</button>
                </div>
                <div class="option-input-row">
                    <input type="text" name="optionTexts" placeholder="Option 2" />
                </div>
            </div>
            <button type="button" class="btn btn-sm btn-secondary" onclick="addOption()" style="margin-top:8px">+ Add Option</button>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Add Question</button>
        </div>
    </form>
</div>

<div style="text-align:center;margin:2rem 0;">
    <a href="${pageContext.request.contextPath}/initiator/dashboard" class="btn btn-success">&#10003; Done — Back to Dashboard</a>
</div>

<script>
function toggleOptions() {
    var type = document.getElementById('questionType').value;
    document.getElementById('optionsSection').style.display = (type === 'TEXT') ? 'none' : 'block';
}
function addOption() {
    var list = document.getElementById('optionsList');
    var row = document.createElement('div');
    row.className = 'option-input-row';
    var idx = list.querySelectorAll('input').length + 1;
    row.innerHTML = '<input type="text" name="optionTexts" placeholder="Option ' + idx + '" />'
                  + '<button type="button" class="btn btn-sm btn-danger" onclick="this.parentNode.remove()">&#x2212;</button>';
    list.appendChild(row);
}
</script>

<%@ include file="../common/footer.jsp" %>
