package com.jas.parker.module.inquire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.jas.parker.R;

import org.apache.http.util.EncodingUtils;

public class TaipeiParkingFeeWebview extends AppCompatActivity {
    WebView taipeiWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //progress bar
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_taipei_parking_fee_webview);
        //看見藍色progress bar
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,Window.PROGRESS_VISIBILITY_ON);

        taipeiWebview = (WebView)findViewById(R.id.webview_taipei);

        String plateNumber = InquireTaipeiActivity.TaipeiPlateNumber;
        int carOrMotor = InquireTaipeiActivity.TaipeiCarOrMotor;
        String carOrMotorString = "";
        if(carOrMotor == 1){
            carOrMotorString = "C";
        }else if(carOrMotor == 2){
            carOrMotorString = "M";
        }

        //台北
        String postData = "CarID="+plateNumber+"&CarType="+carOrMotorString;
        taipeiWebview.postUrl("http://parkingfee.pma.gov.tw/Home/Index/1028", EncodingUtils.getBytes(postData, "BASE64"));
    }
}
