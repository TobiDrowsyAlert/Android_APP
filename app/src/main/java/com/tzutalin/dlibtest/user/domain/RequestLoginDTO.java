package com.tzutalin.dlibtest.user.domain;

public class RequestLoginDTO {

    String userId;
    String userPassword;

    public RequestLoginDTO(){

    }

    public String getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
