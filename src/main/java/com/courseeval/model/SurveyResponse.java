package com.courseeval.model;

public class SurveyResponse {
    private int id;
    private int respondentId;
    private int questionId;
    private Integer optionId;
    private String textAnswer;

    public SurveyResponse() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRespondentId() { return respondentId; }
    public void setRespondentId(int respondentId) { this.respondentId = respondentId; }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public Integer getOptionId() { return optionId; }
    public void setOptionId(Integer optionId) { this.optionId = optionId; }

    public String getTextAnswer() { return textAnswer; }
    public void setTextAnswer(String textAnswer) { this.textAnswer = textAnswer; }
}
