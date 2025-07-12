package com.jas.parker.module.calculation;

import java.util.ArrayList;

/**
 * Created by alex2 on 2016/3/21.
 */
public class FeeInfo {
    private String timesFee;
    private String freePeriod;
    private String maximumFee;
    private String startCharging;
    private String previousHoursAmountFee;
    private String previousHoursFeeCycle;
    private ArrayList<IntervalInfo> intervalInfo;

    public FeeInfo(){
        timesFee = null;
        freePeriod = null;
        maximumFee = null;
        startCharging = null;
        previousHoursAmountFee = null;
        previousHoursFeeCycle = null;
        intervalInfo = new ArrayList<IntervalInfo>();
    }

    public ArrayList<IntervalInfo> getIntervalInfo() {
        return intervalInfo;
    }

    public String getFreePeriod() {
        return freePeriod;
    }

    public String getMaximumFee() {
        return maximumFee;
    }

    public String getPreviousHoursAmountFee() {
        return previousHoursAmountFee;
    }

    public String getPreviousHoursFeeCycle() {
        return previousHoursFeeCycle;
    }

    public String getStartCharging() {
        return startCharging;
    }

    public String getTimesFee() {
        return timesFee;
    }

    public void setFreePeriod(String freePeriod) {
        this.freePeriod = freePeriod;
    }

    public void setIntervalInfo(ArrayList<IntervalInfo> intervalInfo) {
        this.intervalInfo = intervalInfo;
    }

    public void setMaximumFee(String maximumFee) {
        this.maximumFee = maximumFee;
    }

    public void setPreviousHoursAmountFee(String previousHoursAmountFee) {
        this.previousHoursAmountFee = previousHoursAmountFee;
    }

    public void setPreviousHoursFeeCycle(String previousHoursFeeCycle) {
        this.previousHoursFeeCycle = previousHoursFeeCycle;
    }

    public void setStartCharging(String startCharging) {
        this.startCharging = startCharging;
    }

    public void setTimesFee(String timesFee) {
        this.timesFee = timesFee;
    }
}