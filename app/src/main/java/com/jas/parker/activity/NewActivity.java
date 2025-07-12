package com.jas.parker.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jas.parker.R;
import com.jas.parker.module.inquire.InquireNewTaipeiActivity;

public class NewActivity extends AppCompatActivity {

    private final static int FAVOURITE=1;
    private final static int HISTORY=2;
    private final static int REPORT=3;
    private final static int SEARCH=4;
    private final static int INQUIRE=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        int fragmentId = getIntent().getExtras().getInt("fragmentID");
        Fragment fragment = null;
        Class fragmentClass = null;


        if (fragmentId == FAVOURITE) {
            fragmentClass = FavouriteFragment.class;
            ActionBar actionbar = getSupportActionBar();
            actionbar.setTitle("常用停車場");
        } else if (fragmentId == HISTORY) {

            fragmentClass = HistoryFragment.class;
            ActionBar actionbar = getSupportActionBar();
            actionbar.setTitle("歷史紀錄");
        }else if(fragmentId==REPORT){

            fragmentClass = ReportFragment.class;
            ActionBar actionbar = getSupportActionBar();
            actionbar.setTitle("回報");

        }else if(fragmentId==SEARCH){

            fragmentClass = FindParkingLotFragment.class;
            ActionBar actionbar = getSupportActionBar();
            actionbar.setTitle("尋找停車位");

        }else if(fragmentId==INQUIRE){

            fragmentClass = InquireParkingFeeFragment.class;
            ActionBar actionbar = getSupportActionBar();
            actionbar.setTitle("查停車費");

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


    }

}
