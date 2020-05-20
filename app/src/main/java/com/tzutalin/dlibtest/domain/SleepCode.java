package com.tzutalin.dlibtest.domain;

public enum SleepCode {

    INT_BLINK(100, "눈 깜빡임" ), INT_BLIND(101, "눈 감음"), INT_YAWN(200, "하품"), INT_DRIVER_AWAY(300, "운전자 이탈"),
    INT_DRIVER_AWARE_FAIL(301, "정면 주시 실패"), INT_NORMAL(400, "정상");

    int code;
    String reason;

    SleepCode(int code,String reason){
        this.reason = reason;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

}
