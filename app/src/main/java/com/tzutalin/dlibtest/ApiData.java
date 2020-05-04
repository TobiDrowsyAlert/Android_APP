package com.tzutalin.dlibtest;

public class ApiData {
    private int[] rect;
    private String driver;
    private int[][] landmarks;
    private int frame;

    ApiData(){

    }

    public int[] getRect (){
        return rect;
    }

    public void setRect (int[] rect){
        this.rect = rect;
    }

    public String getDriver ()
    {
        return driver;
    }

    public void setDriver (String driver)
    {
        this.driver = driver;
    }

    public int[][] getLandmarks ()
    {
        return landmarks;
    }

    public void setLandmarks (int[][] landmarks)
    {
        this.landmarks = landmarks;
    }

    public int getFrame ()
    {
        return frame;
    }

    public void setFrame (int frame)
    {
        this.frame = frame;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [rect = "+rect+", driver = "+driver+", landmarks = "+landmarks+", frame = "+frame+"]";
    }

}