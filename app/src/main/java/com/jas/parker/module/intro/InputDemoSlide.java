package com.jas.parker.module.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jas.parker.R;
import com.jas.parker.activity.HomeActivity;


public final class InputDemoSlide extends Fragment {

    private static final String DATA_TEXT = "com.github.paolorotolo.appintroexample.slides.InputDemoSlide_text";


    private AppCompatEditText editText;


    private String text;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            text = savedInstanceState.getString(DATA_TEXT);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DATA_TEXT, editText.getText().toString());
        //
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slide_input, container, false);

        editText = (AppCompatEditText)view.findViewById(R.id.slide_input_edittext);




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();



        editText.setText(text);

    }
    public void setLicensePlates(){


        String license_plates =editText.getText().toString();
        HomeActivity.sharedPref.edit().putString("license_plates",license_plates).commit();
        Log.e("parker","偵測到車牌");
        Log.e("parker",license_plates);

    }
}
