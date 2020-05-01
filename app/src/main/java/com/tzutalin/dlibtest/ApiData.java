package com.tzutalin.dlibtest;

public class ApiData {
    private String[] rect;
    private String driver;
    private String[][] landmarks;
    private String frame;

    ApiData(){

    }

    public String[] getRect (){
        return rect;
    }

    public void setRect (String[] rect){
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

    public String[][] getLandmarks ()
    {
        return landmarks;
    }

    public void setLandmarks (String[][] landmarks)
    {
        this.landmarks = landmarks;
    }

    public String getFrame ()
    {
        return frame;
    }

    public void setFrame (String frame)
    {
        this.frame = frame;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [rect = "+rect+", driver = "+driver+", landmarks = "+landmarks+", frame = "+frame+"]";
    }

}