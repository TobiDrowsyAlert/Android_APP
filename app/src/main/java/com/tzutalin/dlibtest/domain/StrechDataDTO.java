package com.tzutalin.dlibtest.domain;

public class StrechDataDTO {
    Boolean start;
    Boolean end;
    int count;
    Boolean right;
    Boolean left;

    StrechDataDTO(){
        start = false;
        end = false;
        count = 0;
        right = false;
        left = false;
    }

    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getRight() {
        return right;
    }

    public void setRight(Boolean right) {
        this.right = right;
    }

    public Boolean getLeft() {
        return left;
    }

    public void setLeft(Boolean left) {
        this.left = left;
    }
}
