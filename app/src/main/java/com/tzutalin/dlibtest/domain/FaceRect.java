package com.tzutalin.dlibtest.domain;

import android.graphics.Rect;

public class FaceRect {
    int[] rect = new int[4];

    public FaceRect(Rect bounds){
        setFaceRect(bounds);
    }

    public void setFaceRect(Rect bounds){
        rect[0] = bounds.left;
        rect[1] = bounds.top;
        rect[2] = bounds.right;
        rect[3] = bounds.bottom;
    }

    public int[] getRect(){
        return rect;
    }
}
