package com.jas.parker.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jas.parker.R;
import com.jas.parker.fragment.FeeFragment;
import com.jas.parker.fragment.MapsFragment;
import com.jas.parker.fragment.ParkingRecordFragment;
import com.jas.parker.fragment.SettingsFragment;
import com.jas.parker.module.inquire.InquireNewTaipeiActivity;
import com.jas.parker.module.inquire.NewTaipeiParkingFeeWebview;
import com.jas.parker.service.FeeNotificationService;

import java.util.ArrayList;
import java.util.List;

public  class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static int FAVOURITE=1;
    private final static int HISTORY=2;
    private final static int REPORT=3;
    private final static int SEARCH=4;
    private final static int INQUIRE=5;
    public static  NavigationView navigationView;
    public static  SharedPreferences sharedPref;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.tab_position_selector,
            R.drawable.tab_camera_selector,
            R.drawable.tab_timer_selector

    };

    private Intent intent;
    private Bundle bundle;
    private SharedPreferences sharedPreferences;

    private ParkingRecordFragment parkingRecordFragment;
    private FeeFragment feeFragment;
    private MapsFragment mapsFragment;
    private TimerFragment timerFragment;

    private static ViewPagerAdapter adapter;

    public static void refreshViewPager(){


        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //測試用關掉通知

        Intent intent = new Intent();
        intent.setClass(this, FeeNotificationService.class);

        //結束FeeNotificationService
        stopService(intent);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {
                    // if (true) {
                    //  Launch app intro
                    Intent i = new Intent(HomeActivity.this, IntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();


        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = MainFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }



            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //讀取車牌號碼
         sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String license_plates = sharedPref.getString("license_plates", "");
        Log.e("parker", license_plates);

        if (navigationView.getHeaderCount() > 0) {
            View header = navigationView.getHeaderView(0);

            TextView license_platesET = (TextView) header.findViewById(R.id.license_plates);
            license_platesET.setText(license_plates);
        }

        //設定tablayout

        parkingRecordFragment = new ParkingRecordFragment();
        feeFragment = new FeeFragment();
        mapsFragment = new MapsFragment();


        intent = new Intent();
        bundle = new Bundle();
        sharedPreferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });




        setupViewPager(viewPager);



        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        //設定tablayout

    }

    //設定tablayout


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(mapsFragment, "定位");
        adapter.addFrag(parkingRecordFragment, "拍照");

        //判定是否在計費中 決定載入哪個頁面
        if (sharedPreferences.getBoolean("isTimerStarting", false)) {
            timerFragment = new TimerFragment();

            //發現有時 intent為空
            if(intent!=null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            //
            adapter.addFrag(timerFragment, "計時計費");

        }else {
            adapter.addFrag(feeFragment, "計時計費");
        }
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {return mFragmentList.get(position);}

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
            // 不顯示標題
            //return mFragmentTitleList.get(position);
        }
    }


    //

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent();

         if (id == R.id.nav_manage) {

            intent.setClass(HomeActivity.this, SettingsActivity.class);

        } else if (id == R.id.nav_send) {

             Bundle bundle = new Bundle();
             bundle.putInt("fragmentID",REPORT );
             intent.putExtras(bundle);
             intent.setClass(HomeActivity.this, NewActivity.class);



        } else if (id == R.id.nav_favourite) {

             Bundle bundle = new Bundle();
             bundle.putInt("fragmentID",FAVOURITE );
             intent.putExtras(bundle);
             intent.setClass(HomeActivity.this, NewActivity.class);

        } else if (id == R.id.nav_history) {
             Bundle bundle = new Bundle();
             bundle.putInt("fragmentID",HISTORY );
             intent.putExtras(bundle);
             intent.setClass(HomeActivity.this, NewActivity.class);

        } else if (id == R.id.nav_search) {

             Bundle bundle = new Bundle();
             bundle.putInt("fragmentID",SEARCH );
             intent.putExtras(bundle);
             intent.setClass(HomeActivity.this, NewActivity.class);

        } else if (id == R.id.nav_Inquire) {

             Bundle bundle = new Bundle();
             bundle.putInt("fragmentID",INQUIRE );
             intent.putExtras(bundle);
             intent.setClass(HomeActivity.this, NewActivity.class);


        } else if (id == R.id.nav_intro) {

             intent.setClass(HomeActivity.this, IntroActivity.class);


         }
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public static void refreshLicensePlate(){

        //讀取車牌號碼

        String license_plates = sharedPref.getString("license_plates", "");
        Log.e("parker", license_plates);

        if (navigationView.getHeaderCount() > 0) {
            View header = navigationView.getHeaderView(0);

            TextView license_platesET = (TextView) header.findViewById(R.id.license_plates);
            license_platesET.setText(license_plates);
        }


    }





}
