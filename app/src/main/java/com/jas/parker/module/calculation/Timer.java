package com.jas.parker.module.calculation;

import java.util.Calendar;

/**
 * Created by alex2 on 2016/3/23.
 */
public class Timer  {
    private String currentTime;
    private int currentHour;
    private int currentMinute;

    public Timer(){
        Calendar c = Calendar.getInstance();
        currentTime = c.getTime().toString();
        currentHour = c.get(Calendar.HOUR_OF_DAY);
        currentMinute = c.get(Calendar.MINUTE);
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    public void setCurrentMinute(int currentMinute) {
        this.currentMinute = currentMinute;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public int getCurrentMinute() {
        return currentMinute;
    }

    public String getCurrentTime() {
        return currentTime;
    }
}
