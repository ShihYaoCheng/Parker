package com.jas.parker.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


public class FeeNotificationService extends Service {
    private CalculationThread calculationThread;
    private String parkerInfo;
    private Long startTime;
    private String maxFee;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sharedPreferences.edit().putBoolean("isFeeNotificationServiceRunning",true).commit();
        Bundle bundle = intent.getExtras();
        parkerInfo = (String) bundle.get("parkerInfo");
        maxFee = (String) bundle.get("maxFee");
        startTime =  bundle.getLong("startTime");
        Log.d("test",String.valueOf(startTime));
        calculationThread = new CalculationThread(startTime,parkerInfo, maxFee, getApplicationContext());

        calculationThread.start();
        //去計算是否目前金額高於通知金額，確定後發出通知

        return START_REDELIVER_INTENT;
        //Service被系統殺掉,
        //重啟且Intent會重傳。 透過以上的參數, 放在onStartCommand的return參數就可以使用重啟的功能了。
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy(){

        calculationThread.stopThread();
        //停止計算是否目前金額高於通知金額
        super.onDestroy();
    }

}
