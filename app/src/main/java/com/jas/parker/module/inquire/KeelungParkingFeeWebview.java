package com.jas.parker.module.inquire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

import com.jas.parker.R;

public class KeelungParkingFeeWebview extends AppCompatActivity {
    WebView keelungWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //progress bar
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_keelung_parking_fee_webview);
        //看見藍色progress bar
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS,Window.PROGRESS_VISIBILITY_ON);

        keelungWebview = (WebView)findViewById(R.id.webview_keelung);

        keelungWebview.loadUrl("http://park.klcg.gov.tw/");
    }
}
