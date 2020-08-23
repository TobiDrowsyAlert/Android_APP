package com.tzutalin.dlibtest.Utility;

public class AppState {

    private static AppState appState = new AppState();
    private AppState(){}
    public boolean isStrecthing = false;

    static public AppState getInstance(){
        return appState;
    }

    public Boolean getIsStrecthing(){
        return isStrecthing;
    }

    public void setIsStrecthing(Boolean isStrecthing){
        this.isStrecthing = isStrecthing;
    }
}
