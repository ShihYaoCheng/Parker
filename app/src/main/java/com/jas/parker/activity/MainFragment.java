package com.jas.parker.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jas.parker.R;
import com.jas.parker.fragment.FeeFragment;
import com.jas.parker.fragment.MapsFragment;
import com.jas.parker.fragment.ParkingRecordFragment;
import com.jas.parker.module.calculation.Timer;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_contact for this fragment
        View v = inflater.inflate(R.layout.content_main, container, false);



        
        return v;
    }





}
