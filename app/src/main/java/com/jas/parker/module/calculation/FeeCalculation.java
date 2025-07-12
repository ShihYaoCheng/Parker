package com.jas.parker.module.calculation;

import java.util.Calendar;


/**
 * Created by alex2 on 2016/3/29.
 */
public class FeeCalculation extends JsonProcessor {
    private long currentTime;               //單位:minute
    private int currentHour;
    private int counter;
    private int currentFee;
    private int maximumFee;
    private int feeCycle;
    private int notificationFee;
    private String totalFee;
    private FeeInfo info;
    private Calendar c;
    private String logCalculation;   //紀錄每次新增費率的過程
    private boolean weekdayFlag;

    public FeeCalculation() {
        currentTime = 0;
        currentTime = 0;               //單位:minute
        currentHour = 0;
        counter = 0;
        currentFee = 0;
        maximumFee = 99999;
        feeCycle = 0;
        notificationFee = 0;
        totalFee = "0";
        info = new FeeInfo();
        c = Calendar.getInstance();
        logCalculation = "";   //紀錄每次新增費率的過程
        weekdayFlag = (c.get(Calendar.DAY_OF_WEEK) == 1 || c.get(Calendar.DAY_OF_WEEK) == 7) ? false : true; //recognize true workday or false holiday

    }

    public FeeCalculation(String jsonString) {
        this(); //宣告變數的建構者

        this.info = processor(jsonString, weekdayFlag);          //copy processed data

        this.maximumFee = info.getMaximumFee() != null ? Integer.parseInt(info.getMaximumFee()) : maximumFee;
    }


    public String feeCalculation(Long spentTime) {
        this.currentTime = (spentTime / 1000) / 60;    //tansfer to minutes (spentTime / 1000)/60

        if (info.getTimesFee() != null) {               //計次費率
            currentFee = Integer.parseInt(info.getTimesFee());
            logCalculation += "計次金額：" + currentFee + "\n";
        } else {                                      //計時費率達到單日上限處理
            if (currentFee < maximumFee) {
                hourCalculation();
            } else {
                currentFee = maximumFee;
                logCalculation += "到達本日計費上限" + currentFee + "\n";
            }
        }
        totalFee = String.valueOf(currentFee);        //Test will be delete
        // totalFee = String.valueOf(Integer.parseInt(totalFee) + currentFee);   //換日單日上限費率處理
        return totalFee;
    }

    public void hourCalculation() {
        int freePeriod = info.getFreePeriod() != null ? Integer.getInteger(info.getFreePeriod()) : 0;
        if (info.getFreePeriod() != null || currentTime >= freePeriod) {         //前多少免費時間處理
            if (info.getStartCharging() == null) {
                intervalCalculation();
            }
        } else {
            currentFee = 0;
        }
    }

    public void intervalCalculation() {
        if (info.getIntervalInfo().size() == 1) {                                 //單時段計費
            int fee = Integer.parseInt(info.getIntervalInfo().get(0).getWeekdayFee());
            feeCycle = (int) (Double.parseDouble(info.getIntervalInfo().get(0).getFeeCycle()) * 60);//0.5 && 1 *60週期

            currentFee = (int) ((currentTime / feeCycle) + 1) * fee;        //+1 because 當還沒到那時間點時就會計費
            logCalculation += "單時段計費金額：" + fee + "\n";
        } else {                                                                  //多時段計費
            for (int i = 0; i < info.getIntervalInfo().size(); ++i) {

                currentHour = c.get(Calendar.HOUR_OF_DAY);

                feeCycle = Integer.parseInt(info.getIntervalInfo().get(i).getFeeCycle());


                if ((currentTime / feeCycle) + 1 > counter) {                          //+1 because 當還沒到那時間點時就會計費
                    final String interval = info.getIntervalInfo().get(i).getInterval();

                    String[] parts = interval.split("~");                           //時間區隔解析
                    int startTime = Integer.parseInt(parts[0]);
                    int endTime = Integer.parseInt(parts[1]) == 0 ? 24 : Integer.parseInt(parts[1]);
                    int fee = 0;

                    counter = (int) (currentTime / feeCycle) + 1;                   //+1 because 當還沒到那時間點時就會計費

                    if (endTime > startTime) {            //在該計時區間內 example 10-18
                        if (currentHour < endTime && currentHour >= startTime) {
                            fee = Integer.parseInt(info.getIntervalInfo().get(i).getWeekdayFee());
                        } else {
                            --counter;
                        }
                    } else {                               // 跨日時段 example 18-10
                        if (currentHour < 24 && currentHour >= startTime) {
                            fee = Integer.parseInt(info.getIntervalInfo().get(i).getWeekdayFee());
                        } else if (currentHour < endTime && currentHour >= 0) {
                            fee = Integer.parseInt(info.getIntervalInfo().get(i).getWeekdayFee());
                        } else {
                            --counter;
                        }
                    }
                    currentFee = currentFee + fee;
                    logCalculation += "單時段計費金額  ：" + fee + "\n";
                }
            }
        }
    }

    public long countdown() {
        //Log.d("1", String.valueOf(feeCycle));            //*************OPEN WHY 多時段 30->60->30
        return feeCycle == 0 ? 0 : (feeCycle - (currentTime % feeCycle));
    }

    public void setNotificationFee(int notificationFee) {
        this.notificationFee = notificationFee;
    }

    public int getNotificationFee() {
        return notificationFee;
    }

    public int getCurrentFee() {
        return currentFee;
    }

    public String getLogCalculation() {
        return logCalculation;
    }
}
