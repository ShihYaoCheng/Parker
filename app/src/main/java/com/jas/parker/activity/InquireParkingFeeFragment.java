package com.jas.parker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.jas.parker.R;
import com.jas.parker.module.inquire.InquireTaipeiActivity;
import com.jas.parker.module.inquire.KeelungParkingFeeWebview;
import com.jas.parker.module.inquire.NewTaipeiParkingFeeWebview;



public class InquireParkingFeeFragment extends Fragment {
    private Button btnTaipei,btnNewTaipei,btnKeelung;


    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_contact for this fragment


        try {
            view = inflater.inflate(R.layout.content_inquire_parking_fee, container, false);
        } catch (InflateException e) {

        }

        btnTaipei = (Button)view.findViewById(R.id.btn_taipei);
        btnNewTaipei = (Button)view.findViewById(R.id.btn_newTaipei);
        btnKeelung = (Button)view.findViewById(R.id.btn_keelung);

        btnTaipei.setOnClickListener(taipeiBtnOnClickListener);
        btnNewTaipei.setOnClickListener(newTaipeiBtnOnClickListener);
        btnKeelung.setOnClickListener(keelungBtnOnClickListener);

        return view;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private Button.OnClickListener taipeiBtnOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(),InquireTaipeiActivity.class);

            startActivity(intent);
        }
    };
    private Button.OnClickListener newTaipeiBtnOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            changeToFragment(NewTaipeiParkingFeeWebview.class);

        }
    };
    private Button.OnClickListener keelungBtnOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(),KeelungParkingFeeWebview.class);
            startActivity(intent);
        }
    };


    private void changeToFragment(Class fragmentClass){



        Fragment fragment = null;


        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


    }

}