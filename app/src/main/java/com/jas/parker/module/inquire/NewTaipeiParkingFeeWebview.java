package com.jas.parker.module.inquire;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;

import com.jas.parker.R;

import org.apache.http.util.EncodingUtils;

import java.util.ArrayList;


public class NewTaipeiParkingFeeWebview extends Fragment {
    WebView mWebview;
    ArrayList<String> hiddenInput = new ArrayList<String>();
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        try {
            view = inflater.inflate(R.layout.activity_new_taipei_parking_fee_webview, container, false);
        } catch (InflateException e) {

        }

        mWebview = (WebView)view.findViewById(R.id.webview_newTaipei);
        //设置WebView支持JavaScript
        mWebview.getSettings().setJavaScriptEnabled(true);

        CatchWebsiteParameters cwp = new CatchWebsiteParameters();
        Thread cmpT = new Thread(cwp);
        cmpT.start();
        try {
            cmpT.join();
            Log.v("TAG1", "CatchWebsiteParameters join");
        } catch (InterruptedException e) {
            Log.v("TAG1", "CatchWebsiteParameters join fail");
            e.printStackTrace();
        }
        hiddenInput = cwp.getHiddenInput();
        Log.v("TAG1","0:"+hiddenInput.get(0));//__VIEWSTATE
        Log.v("TAG1","1:"+hiddenInput.get(1));//__VIEWSTATEGENERATOR
        mWebview.loadUrl("https://ebill.ba.org.tw/MPP/MobileDefault.aspx?tabid=265");

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*
        //progress bar
        getActivity().getWindow().requestFeature(Window.FEATURE_PROGRESS);

        //看見藍色progress bar
        getActivity().getWindow().setFeatureInt(Window.FEATURE_PROGRESS,Window.PROGRESS_VISIBILITY_ON);
        */
/*
        String plateNumber = InquireNewTaipeiActivity.plateNumber;
        int carOrMotor = InquireNewTaipeiActivity.carOrMotor;
        String carOrMotorString = "";
        if(carOrMotor == 1){
            carOrMotorString = "C";
        }else if(carOrMotor == 2){
            carOrMotorString = "M";
        }
*/
        //抓網站隱藏輸入

        //HttpUtility.UrlEncode()

/*
        //    post方式传送参数 clientID = cid & username = name  "__EVENTTARGET=\"\""        +"&__EVENTARGUMENT=\"\""+
        String postData = "__EVENTTARGET=&__EVENTARGUMENT="
                +"&__VIEWSTATE="+hiddenInput.get(0)
                +"&__VIEWSTATEGENERATOR="+hiddenInput.get(1)
                +"&ctl00$Desktopthreepanes1$ThreePanes$ctl13$VehicleType=2"
                +"&ctl00$Desktopthreepanes1$ThreePanes$ctl13$CarNo=758-NMV"
                +"&ctl00$Desktopthreepanes1$ThreePanes$ctl13$OkButton=確+認";
        Log.v("TAG1",postData);
        //mWebview.postUrl("https://ebill.ba.org.tw/CPP/DesktopDefault.aspx?TabIndex=4&TabId=102", EncodingUtils.getBytes(postData, "BASE64"));
*/

/*
        String postData2 = "__VIEWSTATE="+hiddenInput.get(0)
                +"&__VIEWSTATEGENERATOR="+hiddenInput.get(1)
                +"&ctl00$Desktopthreepanes1$ThreePanes$ctl13$VehicleType="+carOrMotor
                +"&ctl00$Desktopthreepanes1$ThreePanes$ctl13$CarNo="+plateNumber
                +"&ctl00$Desktopthreepanes1$ThreePanes$ctl13$OkButton=確+認";
        mWebview.postUrl("https://ebill.ba.org.tw/CPP/DesktopDefault.aspx?TabIndex=4&TabId=102", EncodingUtils.getBytes(postData2, "BASE64"));
        Log.v("TAG1",postData2);
*/




    }


}
