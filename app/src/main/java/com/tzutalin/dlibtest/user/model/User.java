package com.tzutalin.dlibtest.user.model;

import android.os.UserManager;

public class User {

    UserDTO userDTO;
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
}
