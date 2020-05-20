package com.tzutalin.dlibtest.user.model;

import android.os.UserManager;

public class User {

    String userId;
    private static UserManager instance;

    private static class LazyHolder{
        public static final User INSTANCE = new User();
    }

    private User(){
    }

    static public User getInstance(){
        return LazyHolder.INSTANCE;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
