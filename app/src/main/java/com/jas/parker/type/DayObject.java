package com.jas.parker.type;

/**
 * Created by User on 2016/4/15.
 */
import com.jas.parker.type.IntervalInfoObject;
import com.jas.parker.type.PreviousHoursInfoObject;

import java.util.ArrayList;

public class DayObject {
    private String timesFee;
    private String freePeriod;
    private String maximumFee;
    private PreviousHoursInfoObject previousHoursInfo;
    private ArrayList<IntervalInfoObject> intervalInfo;

    public DayObject() {
        // TODO Auto-generated constructor stub
        timesFee = null;
        freePeriod = null;
        maximumFee = null;
        previousHoursInfo = new PreviousHoursInfoObject();
        setIntervalInfo(new ArrayList<IntervalInfoObject>());
    }
    public DayObject(String _timesFee, String _freePeriod, String _maximumFee, PreviousHoursInfoObject _previousHoursInfo, IntervalInfoObject[] _intervalInfo) {
        // TODO Auto-generated constructor stub
        timesFee = _timesFee;
        freePeriod = _freePeriod;
        maximumFee = _maximumFee;
        previousHoursInfo = _previousHoursInfo;
        setIntervalInfo(new ArrayList<IntervalInfoObject>());
    }

    public String getTimesFee() {
        return timesFee;
    }

    public void setTimesFee(String timesFee) {
        this.timesFee = timesFee;
    }

    public String getFreePeriod() {
        return freePeriod;
    }

    public void setFreePeriod(String freePeriod) {
        this.freePeriod = freePeriod;
    }

    public String getMaximumFee() {
        return maximumFee;
    }

    public void setMaximumFee(String maximumFee) {
        this.maximumFee = maximumFee;
    }

    public PreviousHoursInfoObject getPreviousHoursInfo() {
        return previousHoursInfo;
    }

    public void setPreviousHoursInfo(PreviousHoursInfoObject previousHoursInfo) {
        this.previousHoursInfo = previousHoursInfo;
    }
    public ArrayList<IntervalInfoObject> getIntervalInfo() {
        return intervalInfo;
    }
    public void setIntervalInfo(ArrayList<IntervalInfoObject> intervalInfo) {
        this.intervalInfo = intervalInfo;
    }


}
