package com.tzutalin.dlibtest.domain;

import android.graphics.Point;

import java.util.ArrayList;

public class FaceLandmark {
    int landmark[][];

    public FaceLandmark(ArrayList<Point> landmarks, float resizeRatio){
        landmark = new int[landmarks.size()][2];
        int count = 0;
        for (Point point : landmarks) {
            int pointX = (int) (point.x * resizeRatio);
            int pointY = (int) (point.y * resizeRatio);
            landmark[count][0] = pointX;
            landmark[count][1] = pointY;
            count++;
        }
    }

    public int[][] getLandmark(){
        return landmark;
    }
}
