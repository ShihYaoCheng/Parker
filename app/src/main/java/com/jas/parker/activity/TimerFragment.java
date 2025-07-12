package com.jas.parker.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jas.parker.R;
import com.jas.parker.fragment.FeeFragment;
import com.jas.parker.module.calculation.FeeCalculation;
import com.jas.parker.service.FeeNotificationService;

import java.text.DecimalFormat;
import java.util.Calendar;


/**
 * Created by alex2 on 2016/3/29.
 */
public class TimerFragment extends Fragment {
    private static final String TAG = "TimerFragment";

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



    private View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent();

        minutesVariety = (long) -1;
        seconds = (long) 0;
        minutes = (long) 0;
        hours = (long) 0;
        lotInfo = null;
        intent = new Intent();
        handler = new Handler();
        calendar = Calendar.getInstance();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_contact for this fragment
        view = inflater.inflate(R.layout.content_timer_2, container, false);

        notificationFee = (EditText) view.findViewById(R.id.notificationFee);
        startTimeView = (TextView) view.findViewById(R.id.startTime);
        notificationSwitch = (Switch) view.findViewById(R.id.switch_notification);
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);


        try {
            lotInfo = sharedPreferences.getString("lotInfo", "{}");
            startTime = sharedPreferences.getLong("startTime", 0);

            calendar.setTimeInMillis(startTime);
            startTimeView.setText("" + calendar.get(Calendar.HOUR_OF_DAY) + "點" + calendar.get(Calendar.MINUTE)+"分"
                    +calendar.get(Calendar.SECOND)+"秒");
        }
        catch (Exception e){
            e.printStackTrace();
        }


        //判定通知Service是否還在執行 如果傳過來的值有最大金額上限(設定通知) 將會直接把通知設好
        if (sharedPreferences.getBoolean("isFeeNotificationServiceRunning", false)) {
            maxFee = sharedPreferences.getString("maxFee", "0");
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
            Toast.makeText(getActivity(), "停車場資料錯誤", Toast.LENGTH_LONG).show();
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
                        bundle = new Bundle();
                        bundle.putString("parkerInfo", lotInfo);
                        bundle.putString("maxFee", maxFee);
                        bundle.putLong("startTime", startTime);
                        intent.putExtras(bundle);
                        intent.setClass(getActivity(), FeeNotificationService.class);

                        //啟動流程 :service->notification set end
                        getActivity().startService(intent); //啟動 service
                        //showDialog(); 即將更新
                        Toast.makeText(buttonView.getContext(), "設定通知金額為：" + notificationFee.getText(), Toast.LENGTH_LONG).show();

                        //把最大值的參數放入 SharedPreferences
                        sharedPreferences.edit().putString("maxFee", maxFee).commit();
                    }
                    // The toggle is enabled
                } else {
                    Toast.makeText(buttonView.getContext(), "提醒：通知設定關閉", Toast.LENGTH_SHORT).show();
                    notificationFee.setEnabled(true);

                    intent.setClass(getActivity(), FeeNotificationService.class);
                    //停止service
                    getActivity().stopService(intent);

                    //標記FeeNotificationService已經停止
                    sharedPreferences.edit().putBoolean("isFeeNotificationServiceRunning", false).commit();

                    //The toggle is disabled
                }
            }
        });

        navigation = (Button) view.findViewById(R.id.button_navigation);
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
                    if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }else{
                        Toast.makeText(view.getContext(), "無法判定可使用導航的應用程式", Toast.LENGTH_SHORT).show();
                    }

                } catch (NullPointerException e) {
                    Toast.makeText(view.getContext(), "提醒：未設定座標無法導航", Toast.LENGTH_SHORT).show();
                }
            }
        }));


        stopButton = (Button) view.findViewById(R.id.button_stop);
        stopButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stopButton.getText().toString()) {
                    case " 停止計時":
                     //   handler.removeCallbacks(null);
                        Toast.makeText(view.getContext(), "提醒：請再點擊一次", Toast.LENGTH_SHORT).show();
                        stopButton.setText(" 結束紀錄");
                        stopButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.timer_end_btn_selector));
                        stopButton.setTextColor(getResources().getColor(R.color.timer_end_text_btn_selector));
                        break;
                    case " 結束紀錄":

                        //改成getactivity
                        intent.setClass(getActivity(), FeeNotificationService.class);

                        //結束FeeNotificationService
                        getActivity().stopService(intent);

                        //標記計時計算費率已經停止
                        sharedPreferences.edit().putBoolean("isTimerStarting", false).commit();
                        //標記FeeNotificationService已經停止
                        sharedPreferences.edit().putBoolean("isFeeNotificationServiceRunning", false).commit();
                       // 不要結束
                       // finish();

                        FeeFragment feeFragment = new FeeFragment();
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_timer, feeFragment);
                        transaction.commit();
                        break;
                    default:
                        Toast.makeText(getActivity().getApplication(), "錯誤選項", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }));

        return view;

    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView timer = (TextView) view.findViewById(R.id.timer);
            final TextView fee = (TextView) view.findViewById(R.id.fee);                                 //display current fee
            final TextView countdown = (TextView) view.findViewById(R.id.countdown);                     //display countdown time
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
}

//通知功能目前將它移動至service中
/*
    //固定要執行的方法
    public void showNotification(String content) {
        int notifyID = 9999;
        try {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_notify_coins)
                            .setContentTitle("Parker費率提醒")
                            .setContentText("到達您設定的金額:" + content);


            Intent resultIntent = new Intent(this, TimerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("parker", parkerInfo);
            bundle.putLong("startTime", startTime);
            resultIntent.putExtras(bundle);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(TimerActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(
                    notifyID,
                    mBuilder.build());

            notificationFlag = false;

        } catch (Exception e) {
            Log.d("1", e.getMessage());

        }
    }
*/
