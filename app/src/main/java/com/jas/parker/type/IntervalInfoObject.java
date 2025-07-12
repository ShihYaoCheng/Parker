package com.jas.parker.type;

/**
 * Created by User on 2016/4/15.
 */
public class IntervalInfoObject {
    private String interval;
    private String timeChargeParkingFee;
    private String feeCycle;

    public IntervalInfoObject() {
        // TODO Auto-generated constructor stub
        interval = null;
        timeChargeParkingFee = null;
        feeCycle = null;
    }
    public IntervalInfoObject(String _interval, String _timeChargeParkingFee, String _feeCycle) {
        // TODO Auto-generated constructor stub
        interval = _interval;
        timeChargeParkingFee = _timeChargeParkingFee;
        feeCycle = _feeCycle;
    }

    public String getinterval() {
        return interval;
    }

    public void setinterval(String interval) {
        this.interval = interval;
    }

    public String gettimeChargeParkingFee() {
        return timeChargeParkingFee;
    }

    public void settimeChargeParkingFee(String timeChargeParkingFee) {
        this.timeChargeParkingFee = timeChargeParkingFee;
    }

    public String getfeeCycle() {
        return feeCycle;
    }

    public void setfeeCycle(String feeCycle) {
        this.feeCycle = feeCycle;
    }

}