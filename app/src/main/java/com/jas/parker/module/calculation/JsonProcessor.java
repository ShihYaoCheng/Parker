package com.jas.parker.module.calculation;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by alex2 on 2016/3/14.
 */
public class JsonProcessor {

    private String jsonString;
    private FeeInfo info;

    public JsonProcessor() {
        jsonString = null;
        info = new FeeInfo();
    }

    public void setJsonString(String _jsonString) {
        jsonString = _jsonString;
    }

    public String getJsonString() {
        return jsonString;
    }

    public String toString() {
        String allInfo = "TimesFee:" + info.getTimesFee() + "\n" +
                "FreePeriod:" + info.getFreePeriod() + "\n" +
                "MaximumFee:" + info.getMaximumFee() + "\n" +
                "StartCharging:" + info.getStartCharging() + "\n" +
                "PreviousHoursAmountFee:" + info.getPreviousHoursAmountFee() + "\n" +
                "PreviousHoursFeeCycle:" + info.getPreviousHoursAmountFee() + "\n";

        for (IntervalInfo e : info.getIntervalInfo()) {
            allInfo += e.getInterval() + "\n" + e.getWeekdayFee() + "\n" + e.getFeeCycle() + "\n";
        }
        return allInfo;


    }

    public FeeInfo processor(String jsonString, boolean weekdayFlag) {           //if flag == true(workday) else false(holiday)

        JsonObject feeInfo = new JsonParser().parse(jsonString).getAsJsonObject();
        if (feeInfo.has("carFeeInfo")) {
            JsonObject carFeeInfo = feeInfo.getAsJsonObject("carFeeInfo");
            if (carFeeInfo.has("weekday") && weekdayFlag) {
                this.feeInfoProcess(carFeeInfo.getAsJsonObject("weekday"));
            } else if (carFeeInfo.has("weekendAndHoliday")) {
                this.feeInfoProcess(carFeeInfo.getAsJsonObject("weekendAndHoliday"));
            }
        }
        return info;
    }

    public void feeInfoProcess(JsonObject carFeeInfo) {

        if (carFeeInfo.has("timesFee")) {                                                               //如果找到計次費率，下方計時資訊就不用找了
            info.setTimesFee(carFeeInfo.get("timesFee").getAsString());                                 // pay once time 計次收費金額
        } else {
            if (carFeeInfo.has("freePeriod")) {
                info.setFreePeriod(carFeeInfo.get("freePeriod").getAsString());                         //免費停車時間
            }
            if (carFeeInfo.has("maximumFee")) {
                info.setMaximumFee(carFeeInfo.get("maximumFee").getAsString());                        //單日收費上限
            }
            if (carFeeInfo.has("previousHoursInfo")) {
                this.previousHoursInfoProcess(carFeeInfo.getAsJsonObject("previousHoursInfo"));
            }
            if (carFeeInfo.has("intervalInfo")) {
                this.intervalInfoProcess(carFeeInfo.getAsJsonArray("intervalInfo"));
            }
        }
    }

    public void previousHoursInfoProcess(JsonObject previousHoursInfo) {
        if (previousHoursInfo.has("startCharging"))                                                     //第幾小時開始收費
            info.setStartCharging(previousHoursInfo.get("startCharging").getAsString());

        if (previousHoursInfo.has("previousHoursAmountFee"))                                          //前幾小時多少錢
            info.setStartCharging(previousHoursInfo.get("previousHoursAmountFee").getAsString());

        if (previousHoursInfo.has("previousHoursFeeCycle"))                                            //前幾小時的收費周期
            info.setStartCharging(previousHoursInfo.get("previousHoursFeeCycle").getAsString());

    }

    public void intervalInfoProcess(JsonArray intervalInfo) {

        for (int i = 0; i < intervalInfo.size(); ++i) {

            String interval = intervalInfo.get(i).getAsJsonObject().get("interval").getAsString();
            String weekdayFee = intervalInfo.get(i).getAsJsonObject().get("timeChargeParkingFee").getAsString();
            String feeCycle = intervalInfo.get(i).getAsJsonObject().get("feeCycle").getAsString();

            IntervalInfo eachIntervalInfo = new IntervalInfo();
            eachIntervalInfo.setInterval(interval);
            eachIntervalInfo.setWeekdayFee(weekdayFee);
            eachIntervalInfo.setFeeCycle(feeCycle);

            info.getIntervalInfo().add(eachIntervalInfo);                                               //計時收費區間、計時收費金額、計時收費週期
        }
    }
}
