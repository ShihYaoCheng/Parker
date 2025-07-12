package com.jas.parker.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.jas.parker.R;
import com.jas.parker.activity.HomeActivity;
import com.jas.parker.activity.TimerActivity;
import com.jas.parker.module.calculation.FeeCalculation;

/**
 * Created by alex2 on 2016/7/4.
 */
public class CalculationThread extends Thread {

    private Boolean stopFlag = false;
    private Long startTime;
    private Long spentTime;
    private FeeCalculation calculation;
    private String maxFee;
    private String lotInfo;
    private SharedPreferences sharedPreferences;

    private int tempFee;

    private Context context;
    private Handler handler;

    public CalculationThread(Long startTime, String lotInfo, String maxFee, Context context) {
        handler = new Handler();
        this.startTime = startTime;
        this.context = context;
        this.maxFee = maxFee;
        this.lotInfo = lotInfo;
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        calculation = new FeeCalculation(lotInfo);
        tempFee = 0;
    }

    public void run() {
        spentTime = System.currentTimeMillis() - startTime;

        if (stopFlag)
            handler.removeCallbacks(this);

        if (Integer.parseInt(calculation.feeCalculation(spentTime)) >= Integer.parseInt(maxFee)) {
            this.maxFeeNotification(context);
            stopFlag = true;
            sharedPreferences.edit().putBoolean("isFeeNotificationServiceRunning",false).commit();
        } else {
            if (tempFee != Integer.parseInt(calculation.feeCalculation(spentTime))) { //如果偵測到費率變化 發出通知

                tempFee = Integer.parseInt(calculation.feeCalculation(spentTime));

                this.changFeeNotification(context);
            }
            handler.postDelayed(this, 60000); //60s計算一次是否有達到通知金額
        }
    }

    public void stopThread() {
        stopFlag = true;
    }

    public void maxFeeNotification(Context context){  //設定到達設定金額時通知樣式
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Parker已達到設定金額")
                        .setContentText("通知費用設定金額：" + maxFee)
                        .setVisibility(Notification.VISIBILITY_PUBLIC) // 螢幕鎖定顯示通知的完整內容
                        .setDefaults(Notification.DEFAULT_ALL) //振動 音效 LED燈
                        .setAutoCancel(true); //點擊後刪除;

        sendNotification(mBuilder);
    }
    public void changFeeNotification(Context context) { //設定計費金額改變時通知樣式

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Parker費率通知啟動")
                        .setContentText("通知費用設定金額：" + maxFee)
                        .setVisibility(Notification.VISIBILITY_PUBLIC) // 螢幕鎖定顯示通知的完整內容
                        .setAutoCancel(true) //點擊後刪除
                        .setProgress(Integer.parseInt(maxFee), Integer.parseInt(calculation.feeCalculation(spentTime)), false);
        //進度條 目前金額 以及最大金額時的進度

        sendNotification(mBuilder);
    }

    public void sendNotification(NotificationCompat.Builder mBuilder){ //送出通知
        int notifyID = 0; //通知的優先權  Sets an ID for the notification, so it can be updated

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();

        resultIntent.setClass(context.getApplicationContext(), HomeActivity.class);
        bundle.putString("lotInfo", lotInfo);
        bundle.putLong("startTime", startTime);
        bundle.putString("maxFee", maxFee);

        resultIntent.putExtras(bundle);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context.getApplicationContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // mId allows you to update the notification later on.
        mNotificationManager.notify(notifyID, mBuilder.build());

    }
}
