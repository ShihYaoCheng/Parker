package com.jas.parker.type;

/**
 * Created by User on 2016/4/15.
 */
public class PreviousHoursInfoObject {
    private String startCharging;
    private String previousHoursAmountFee;
    private String previousHoursFeeCycle;

    public PreviousHoursInfoObject() {
        startCharging = null;
        previousHoursAmountFee = null;
        previousHoursFeeCycle = null;
    }
    public PreviousHoursInfoObject(String _startCharging, String _previousHoursAmountFee, String _previousHoursFeeCycle) {
        // TODO Auto-generated constructor stub
        startCharging = _startCharging;
        previousHoursAmountFee = _previousHoursAmountFee;
        previousHoursFeeCycle = _previousHoursFeeCycle;
    }

    public String getStartCharging() {
        return startCharging;
    }

    public void setStartCharging(String startCharging) {
        this.startCharging = startCharging;
    }

    public String getPreviousHoursAmountFee() {
        return previousHoursAmountFee;
    }

    public void setPreviousHoursAmountFee(String previousHoursAmountFee) {
        this.previousHoursAmountFee = previousHoursAmountFee;
    }

    public String getPreviousHoursFeeCycle() {
        return previousHoursFeeCycle;
    }

    public void setPreviousHoursFeeCycle(String previousHoursFeeCycle) {
        this.previousHoursFeeCycle = previousHoursFeeCycle;
    }

}
