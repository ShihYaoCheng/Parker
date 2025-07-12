package com.jas.parker.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jas.parker.R;
import com.jas.parker.fragment.*;
import com.jas.parker.module.calculation.FeeCalculation;
import com.jas.parker.service.FeeNotificationService;

import java.text.DecimalFormat;
import java.util.Calendar;


/**
 * Created by alex2 on 2016/3/29.
 */
public class TimerActivity extends AppCompatActivity {
    private FeeCalculation calculation;
    private Long startTime;
    private String maxFee;
    private String lotInfo;
    private Long seconds;
    private Long minutes;
    private Long hours;
    private Long spentTime;
    private Long minutesVariety;
    private  double lat;
    private double lon;

    private Intent intent;
    private Bundle bundle;
    private Handler handler;
    private Calendar calendar;
    private SharedPreferences sharedPreferences;

    private Switch notificationSwitch;
    private Button stopButton;
    private Button navigation;
    private EditText notificationFee;
    private TextView startTimeView;

    private Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notificationFee = (EditText) findViewById(R.id.notificationFee);
        startTimeView = (TextView) findViewById(R.id.startTime);
        notificationSwitch = (Switch) findViewById(R.id.switch_notification);
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);


        minutesVariety = (long) -1;
        seconds = (long) 0;
        minutes = (long) 0;
        hours = (long) 0;
        lotInfo = null;
        intent = new Intent();
        bundle = this.getIntent().getExtras();
        handler = new Handler();
        calendar = Calendar.getInstance();

        //紀錄開始時間
        startTime = bundle.containsKey("startTime") ? bundle.getLong("startTime") : System.currentTimeMillis();
        calendar.setTimeInMillis(startTime);
        startTimeView.setText("入場時間:" + calendar.get(Calendar.HOUR_OF_DAY) + "點" + calendar.get(Calendar.MINUTE)+"分"
                +calendar.get(Calendar.SECOND)+"秒");

        lotInfo = bundle.getString("lotInfo");

        //在開始時 把startTime & lotInfo 放入 SharedPreferences
        sharedPreferences.edit().putLong("startTime", startTime).putString("lotInfo", lotInfo).commit();

        //判定通知Service是否還在執行 如果傳過來的值有最大金額上限(設定通知) 將會直接把通知設好
        if (sharedPreferences.getBoolean("isFeeNotificationServiceRunning", false)) {

            maxFee = bundle.getString("maxFee");
            notificationFee.setText(maxFee);
            notificationFee.setEnabled(false);
            notificationSwitch.setChecked(true);
        }

        try {
            calculation = new FeeCalculation(lotInfo);
            //設定定時要執行的方法
            //設定Delay的時間
            handler.postDelayed(updateTimer, 1000);
        } catch (NullPointerException e) {
            Toast.makeText(getApplication(), "停車場資料錯誤", Toast.LENGTH_LONG).show();
        }

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (notificationFee.getText().toString().isEmpty()) {
                        Toast.makeText(buttonView.getContext(), "提醒：您未設定金額" + notificationFee.getText(), Toast.LENGTH_SHORT).show();
                        buttonView.setChecked(false);

                    } else {
                        //service->notification set //
                        maxFee = notificationFee.getText().toString();
                        bundle.putString("parkerInfo", lotInfo);
                        bundle.putString("maxFee", maxFee);
                        bundle.putLong("startTime", startTime);
                        intent.putExtras(bundle);
                        intent.setClass(TimerActivity.this, FeeNotificationService.class);

                        //啟動流程 :service->notification set end
                        startService(intent); //啟動 service
                        //showDialog(); 即將更新
                        Toast.makeText(buttonView.getContext(), "設定通知金額為：" + notificationFee.getText(), Toast.LENGTH_LONG).show();

                        //把最大值的參數放入 SharedPreferences
                        sharedPreferences.edit().putString("maxFee", maxFee).commit();
                    }
                    // The toggle is enabled
                } else {
                    Toast.makeText(buttonView.getContext(), "提醒：通知設定關閉", Toast.LENGTH_SHORT).show();
                    notificationFee.setEnabled(true);

                    intent.setClass(TimerActivity.this, FeeNotificationService.class);
                    //停止service
                    stopService(intent);

                    //標記FeeNotificationService已經停止
                    sharedPreferences.edit().putBoolean("isFeeNotificationServiceRunning", false).commit();

                    //The toggle is disabled
                }
            }
        });

        navigation = (Button) findViewById(R.id.button_navigation);
        navigation.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //開啟googleMap導航
                //Uri gmmIntentUri = Uri.parse("google.map:q=25.132203, 121.739350&mode=d");
                try {
                    //從SharedPreferences讀取紀錄(從FeeFragment中)的經緯度
                    lat = Double.parseDouble(sharedPreferences.getString("Y",""));
                    lon = Double.parseDouble(sharedPreferences.getString("X",""));
                   // Toast.makeText(view.getContext(), "lat:"+lat+",lon:"+lon, Toast.LENGTH_SHORT).show();
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon + "&mode=w");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }else{
                        Toast.makeText(view.getContext(), "無法判定可使用導航的應用程式", Toast.LENGTH_SHORT).show();
                    }

                } catch (NullPointerException e) {
                    Toast.makeText(view.getContext(), "提醒：未設定座標無法導航", Toast.LENGTH_SHORT).show();
                }
            }
        }));


        stopButton = (Button) findViewById(R.id.button_stop);
        stopButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stopButton.getText().toString()) {
                    case " 停止計時":
                        handler.removeCallbacks(null);
                        Toast.makeText(view.getContext(), "提醒：請再點擊一次", Toast.LENGTH_SHORT).show();
                        stopButton.setText(" 結束紀錄");
                        break;
                    case " 結束紀錄":

                        Log.d("test", String.valueOf(sharedPreferences.getBoolean("isTimerStarting", false)));

                        intent.setClass(TimerActivity.this, FeeNotificationService.class);

                        //結束FeeNotificationService
                        stopService(intent);

                        //標記計時計算費率已經停止
                        sharedPreferences.edit().putBoolean("isTimerStarting", false).commit();
                        //標記FeeNotificationService已經停止
                        sharedPreferences.edit().putBoolean("isFeeNotificationServiceRunning", false).commit();

                        finish();
                        break;
                    default:
                        Toast.makeText(getApplication(), "錯誤選項", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }));
    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView timer = (TextView) findViewById(R.id.timer);
            final TextView fee = (TextView) findViewById(R.id.fee);                                 //display current fee
            final TextView countdown = (TextView) findViewById(R.id.countdown);                     //display countdown time
            DecimalFormat decimalFormat = new DecimalFormat("00");
            spentTime = System.currentTimeMillis() - startTime;


            //計算目前已過小時數
            hours = (spentTime / 1000) / 60 / 60 % 24;
            //計算目前已過分鐘數
            minutes = (spentTime / 1000) / 60 % 60;
            //計算目前已過秒數
            seconds = (spentTime / 1000) % 60;

            timer.setText(decimalFormat.format(hours) + ":" + decimalFormat.format(minutes) + ":" + decimalFormat.format(seconds));          //<<UI>> show current time to UI

            if (minutesVariety != minutes) {                                                            //分鐘變化時計算費率
                minutesVariety = minutes;
                fee.setText(calculation.feeCalculation(spentTime));                                     //計算現在費率
                countdown.setText(decimalFormat.format(calculation.countdown()));                       //<<UI>> show countdown time to UI
            }

            handler.postDelayed(this, 1000);

        }
    };


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //bug 回到上一頁但是他是銷毀activity所以fee fragment 不會再 onCreate() 所以不會判定有在計時
            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void showDialog(){
        NumberPicker picker =  new NumberPicker(this); //設計NumberPicker樣式
        picker.setMinValue(20);
        picker.setMaxValue(500);
        picker.setValue(60);


        new AlertDialog.Builder(this)
                .setTitle("停車場資訊")
                .setView(picker)
                .setPositiveButton("確定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){


                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface arg0, int arg1){


                    }
                })
                .show();
    }
}


