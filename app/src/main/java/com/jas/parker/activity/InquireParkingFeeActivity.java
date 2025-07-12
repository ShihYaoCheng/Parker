package com.jas.parker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jas.parker.R;
import com.jas.parker.module.inquire.InquireTaipeiActivity;
import com.jas.parker.module.inquire.KeelungParkingFeeWebview;
import com.jas.parker.module.inquire.NewTaipeiParkingFeeWebview;
import com.jas.parker.service.FeeNotificationService;


public class InquireParkingFeeActivity extends AppCompatActivity {
    private Button btnTaipei,btnNewTaipei,btnKeelung;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.content_inquire_parking_fee);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        btnTaipei = (Button)findViewById(R.id.btn_taipei);
        btnNewTaipei = (Button)findViewById(R.id.btn_newTaipei);
        btnKeelung = (Button)findViewById(R.id.btn_keelung);

        btnTaipei.setOnClickListener(taipeiBtnOnClickListener);
        btnNewTaipei.setOnClickListener(newTaipeiBtnOnClickListener);
        btnKeelung.setOnClickListener(keelungBtnOnClickListener);

    }




    private Button.OnClickListener taipeiBtnOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),InquireTaipeiActivity.class);

            startActivity(intent);
        }
    };
    private Button.OnClickListener newTaipeiBtnOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),NewTaipeiParkingFeeWebview.class);
            startActivity(intent);


        }
    };
    private Button.OnClickListener keelungBtnOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),KeelungParkingFeeWebview.class);
            startActivity(intent);
        }
    };



}