package com.jas.parker.fragment;

import android.app.ActivityManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import com.google.android.gms.maps.model.LatLng;

import com.jas.parker.R;
import com.jas.parker.activity.TimerActivity;
import com.jas.parker.activity.TimerFragment;
import com.jas.parker.service.FeeNotificationService;
import com.jas.parker.type.ParkerDataObject;
import com.jas.parker.module.map.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FeeFragment extends Fragment {
    private static final String TAG = "FeeFragment";

    public static ParkerDataObject[] parkers;

    private Spinner spinner;
    private Button start;
    private ImageButton location;
    private TextView showFeeInfo;
    private TextView showCity;
    private TextView timeSetting;
    private ImageButton timeEdit;

    private Long startTime;
    private String maxFee;
    private String choiceLot;
    private ArrayList<String> lot;

    private Gson gson;
    private Calendar calendar;
    private SharedPreferences sharedPreferences;
    private DecimalFormat decimalFormat;

    private Intent intent;
    private Bundle bundle;

    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_contact for this fragment
        View v = inflater.inflate(R.layout.content_fee_2, container, false);

        //UI 元件
        spinner = (Spinner) v.findViewById(R.id.spinner_lot);
        start = (Button) v.findViewById(R.id.btn_start);
        location = (ImageButton) v.findViewById(R.id.imageButton_location);
        showFeeInfo = (TextView) v.findViewById(R.id.feeInfo_show);
        showCity = (TextView) v.findViewById(R.id.city_show);
        timeSetting = (TextView) v.findViewById(R.id.time_setting);
        //timeEdit = (ImageButton) v.findViewById(R.id.btn_time_edit);

        sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);

        //new
        choiceLot = "{}";
        gson = new Gson();
        lot = new ArrayList<String>();
        startTime = System.currentTimeMillis();
        intent = new Intent();
        bundle = new Bundle();
        decimalFormat = new DecimalFormat("00");
        calendar = Calendar.getInstance();//取得時間


        showFeeInfo.setMovementMethod(new ScrollingMovementMethod());


        timeSetting.setText(""+decimalFormat.format(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + decimalFormat.format(calendar.get(Calendar.MINUTE)));
        timeSetting.setOnClickListener(onClickListener);
       // timeEdit.setOnClickListener(onClickListener);

        location.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create an ArrayAdapter using the string array and a default spinner item_contact
                adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, lot);
                parkers = MapsFragment.parkerDataObjects;
                lot.clear();
/*
                //重新定位
                GPSLocation location = new GPSLocation(getActivity());
                double latitude = location.getLatitude();//現在緯度
                double longitude = location.getLongitude();//現在經度
                LatLng nowPlace = new LatLng(latitude, longitude);
*/
                //資料不存在 提醒使用者
                if (parkers.length == 0) {
                    Toast.makeText(getContext(), "提醒：請確定GPS開啟或附近沒有停車場", Toast.LENGTH_SHORT).show();
                }

                /*  發生一次閃退 待解決<OPEN>
                java.lang.NullPointerException: Attempt to get length of null array
                at com.jas.parker.fragment.FeeFragment$2.onClick(FeeFragment.java:152)
                */

                for (int i = 0; i < MapsFragment.parkerDataObjects.length; ++i) {
                    // Toast.makeText(getActivity().getApplication(), parkers[i].getName(), Toast.LENGTH_SHORT).show();
                    lot.add(parkers[i].getName());
                    // Specify the item_contact to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // Apply the adapter to the spinner
                }
                spinner.setAdapter(adapter);

            }
        }));

        //按下按鈕 開始費率計算
        start.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (choiceLot.isEmpty()) {
                    Toast.makeText(view.getContext(), "提醒:請選擇停車場", Toast.LENGTH_SHORT).show();
                } else {
                    intent.setClass(getActivity(), TimerActivity.class);
                    /*
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    */
                    //資料放入sharedPreferences 由 TimerFragment提取資料去做運算
                    sharedPreferences.edit().putString("lotInfo", choiceLot).commit();
                    sharedPreferences.edit().putLong("startTime", startTime).commit();
                    sharedPreferences.edit().putBoolean("isTimerStarting", true).commit();
                    //標記計時計算費率已經開始

                    //替換頁面
                    TimerFragment timerFragment = new TimerFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    //timerFragment.setArguments(bundle);
                    transaction.replace(R.id.content_fee, timerFragment);
                    transaction.commit();

                }
            }
        }));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                choiceLot = gson.toJson(parkers[position].getFeeInfo());       //選擇停車場後將資訊準備放入bundle
              //  showCity.setText(parkers[position].getTaiwanCity());
                showFeeInfo.setText("停車場名稱：" + nullToNo(parkers[position].getName()) + "\n" +
                        "地址：" + nullToNo(parkers[position].getAddress()) + "\n" +
                        "電話：" + nullToNo(parkers[position].getTel()) + "\n" +
                        "簡介：" + nullToNo(parkers[position].getSummary()) + "\n" +
                        "收費資訊：" + nullToNo(parkers[position].getPayex()));

                //Record 選擇停車場的座標 放入 sharedPreferences
                sharedPreferences.edit().putString("Y",String.valueOf(parkers[position].getYAxis())).putString("X",String.valueOf(parkers[position].getXAxis())).commit();

                /* test
                LatLng temp = new LatLng(parkers[position].getYAxis(), parkers[position].getXAxis());
                MapsFragment.recordPlace = temp;
                */
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        return v;
    }


    //判斷抓到的資料是否為null
    public String nullToNo(String ori) {
        if (ori == null) {
            ori = "市政府未提供資訊";
        }
        return ori;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() { //時間平移的Listener
        @Override
        public void onClick(View view) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    //判別時間是往前調整 還是往後
                    calendar.set(Calendar.MINUTE, minute );
                    calendar.set(Calendar.HOUR_OF_DAY, hour );
                    startTime = calendar.getTimeInMillis();
                    timeSetting.setText(""+decimalFormat.format(hour) + ":" + decimalFormat.format(minute));
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

            timePickerDialog.show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
