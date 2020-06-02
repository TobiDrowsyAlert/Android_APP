package com.tzutalin.dlibtest.domain;

import java.util.regex.Pattern;

public class RequestRegisterDTO {

    private String userId;
    private String userPassword;
    private String userPasswordConfirm;
    private String userEmail;
    private String userName;


    public RequestRegisterDTO(){

    }

    public RequestRegisterDTO(String userId, String userPassword, String userPasswordConfirm, String userEmail, String userName){
        this.userId = userId;
        this.userPassword = userPassword;
        this.userPasswordConfirm = userPasswordConfirm;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPasswordConfirm(){
        return userPasswordConfirm;
    }

}
