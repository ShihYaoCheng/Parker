package com.jas.parker.module.calculation;

/**
 * Created by alex2 on 2016/3/21.
 */
public class IntervalInfo {
    private String interval;
    private String weekdayFee;
    private String feeCycle;

    public IntervalInfo(){
        interval = null;
        weekdayFee = null;
        feeCycle = null;
    }
    public String getFeeCycle() {
        return feeCycle;
    }

    public String getInterval() {
        return interval;
    }

    public String getWeekdayFee() {
        return weekdayFee;
    }

    public void setFeeCycle(String feeCycle) {
        this.feeCycle = feeCycle;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setWeekdayFee(String weekdayFee) {
        this.weekdayFee = weekdayFee;
    }
}
