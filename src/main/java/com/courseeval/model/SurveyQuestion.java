package com.courseeval.model;

import java.util.List;

public class SurveyQuestion {
    private int id;
    private int surveyId;
    private String questionText;
    private String questionType; // SINGLE, MULTIPLE, TEXT
    private int displayOrder;
    private List<SurveyOption> options;

    public SurveyQuestion() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSurveyId() { return surveyId; }
    public void setSurveyId(int surveyId) { this.surveyId = surveyId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public List<SurveyOption> getOptions() { return options; }
    public void setOptions(List<SurveyOption> options) { this.options = options; }
}
