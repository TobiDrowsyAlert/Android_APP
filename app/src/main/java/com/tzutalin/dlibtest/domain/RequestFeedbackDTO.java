package com.tzutalin.dlibtest.domain;

import java.util.Date;

    public class RequestFeedbackDTO {
        private int logNo;
        private Boolean isCorrect;
        private Boolean isFeedback;
        private String date;
        private String userId;

    public RequestFeedbackDTO(){

    }

    public void setLogNo(int logNo){this.logNo = logNo;}

    public int getLogNo(){return logNo;}

    public RequestFeedbackDTO(String date){
        this.date = date;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Boolean getIsFeedback(){return isFeedback;}

    public void setIsFeedback(Boolean isFeedback){this.isFeedback = isFeedback;};

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
