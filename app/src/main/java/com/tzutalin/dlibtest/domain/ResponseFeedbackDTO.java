package com.tzutalin.dlibtest.domain;

import java.util.Date;

public class ResponseFeedbackDTO {
    private Boolean isCorrect;
    private String date;

    public ResponseFeedbackDTO(){

    }

    public ResponseFeedbackDTO(String date){
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
}
