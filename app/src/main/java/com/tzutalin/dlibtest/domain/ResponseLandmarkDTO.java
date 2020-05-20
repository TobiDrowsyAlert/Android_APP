package com.tzutalin.dlibtest.domain;

import java.util.Date;

public class ResponseLandmarkDTO {
    private int status_code;
    private int sleep_step;
    private String curTime;
    private String userId;


    public void setCode(int code){
        this.status_code = status_code;
    }

    public int getCode(){
        return status_code;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public int getSleep_step() {
        return sleep_step;
    }

    public void setSleep_step(int sleep_step) {
        this.sleep_step = sleep_step;
    }

    public String getCurTime() {
        return curTime;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
