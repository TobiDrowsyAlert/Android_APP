package com.tzutalin.dlibtest.domain;

import java.util.Date;

public class RequestFeedbackDTO {
    private Boolean isCorrect;
    private String date;
    private String userId;

    public RequestFeedbackDTO(){

    }

    public RequestFeedbackDTO(String date){
        this.date = date;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
