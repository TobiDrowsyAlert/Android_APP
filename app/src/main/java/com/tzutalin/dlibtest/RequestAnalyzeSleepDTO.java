package com.tzutalin.dlibtest;

public class RequestAnalyzeSleepDTO {
    private int[] rect;
    private Boolean driver;
    private int[][] landmarks;
    private int frame;
    private Boolean isCorrect;

    public RequestAnalyzeSleepDTO(){

    }

    public RequestAnalyzeSleepDTO(int[] rect, Boolean driver, int[][] landmarks, int frame, Boolean isCorrect){
        this.rect = rect;
        this.driver = driver;
        this.landmarks = landmarks;
        this.frame = frame;
        this.isCorrect = isCorrect;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public int[] getRect (){
        return rect;
    }

    public void setRect (int[] rect){
        this.rect = rect;
    }

    public Boolean getDriver ()
    {
        return driver;
    }

    public void setDriver (Boolean driver)
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

    public void setRequestAnalyzeSleepDTO(int[] rect, Boolean driver, int[][] landmarks, int frame, Boolean isCorrect){
        this.rect = rect;
        this.driver = driver;
        this.landmarks = landmarks;
        this.frame = frame;
        this.isCorrect = isCorrect;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [rect = "+rect+", driver = "+driver+", landmarks = "+landmarks+", frame = "+frame+"]";
    }

}