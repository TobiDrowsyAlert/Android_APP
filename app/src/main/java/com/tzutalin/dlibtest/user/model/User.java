package com.tzutalin.dlibtest.user.model;

import android.os.UserManager;

public class User {

    UserDTO userDTO;
    String userId;
    double avgStage;
    int currentStage;
    Boolean isWeakTime;
    private static UserManager instance;

    private static class LazyHolder{
        public static final User INSTANCE = new User();
    }

    private User(){
    }

    static public User getInstance(){
        return LazyHolder.INSTANCE;
    }

    public void setUserDTO(UserDTO userId) {
        this.userDTO = userId;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public double getAvgStage() {
        return avgStage;
    }

    public void setAvgStage(double avgStage) {
        this.avgStage = avgStage;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public Boolean getWeakTime() {
        return isWeakTime;
    }

    public void setWeakTime(Boolean weakTime) {
        isWeakTime = weakTime;
    }
}
