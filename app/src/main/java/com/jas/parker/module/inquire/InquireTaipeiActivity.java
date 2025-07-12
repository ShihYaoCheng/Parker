package com.jas.parker.module.inquire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jas.parker.R;

public class InquireTaipeiActivity extends AppCompatActivity {

    private Button btnTaipeiInquire;
    private EditText edtTaipeiPlateNumber;
    private RadioButton radTaipeiCar,radTaipeiMotor;
    private RadioGroup radGroupTaipeiVehicles;

    public static String TaipeiPlateNumber;
    public static int TaipeiCarOrMotor;//0預設 1汽車 2機車

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire_taipei);

        //抓元件
        btnTaipeiInquire = (Button)findViewById(R.id.btn_taipei_inquire);
        edtTaipeiPlateNumber = (EditText)findViewById(R.id.edt_taipei_plateNumber);
        radGroupTaipeiVehicles = (RadioGroup)findViewById(R.id.radioGroup_taipei_vehicles);
        radTaipeiCar = (RadioButton)findViewById(R.id.radioButton_taipei_car);
        radTaipeiMotor = (RadioButton)findViewById(R.id.radioButton_taipei_motor);
        //監聽器
        btnTaipeiInquire.setOnClickListener(btnInquireListener);
        radGroupTaipeiVehicles.setOnCheckedChangeListener(radGroupVehiclesListener);

        TaipeiCarOrMotor = 0;

    }

    private Button.OnClickListener btnInquireListener = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            TaipeiPlateNumber = edtTaipeiPlateNumber.getText().toString();

            Log.v("TAG1", TaipeiPlateNumber);


            if(radGroupTaipeiVehicles.getCheckedRadioButtonId() != R.id.radioButton_taipei_car && radGroupTaipeiVehicles.getCheckedRadioButtonId() != R.id.radioButton_taipei_motor){
                Toast.makeText(getApplicationContext(), "請選擇車種", Toast.LENGTH_SHORT).show();
            }else if(edtTaipeiPlateNumber.getText().toString().equals("") ){
                Toast.makeText(getApplicationContext(), "請輸入車號", Toast.LENGTH_SHORT).show();
            }else{
                //new一個intent物件，並指定Activity切換的class
                Intent intent = new Intent();
                intent.setClass(InquireTaipeiActivity.this,TaipeiParkingFeeWebview.class);

                startActivity(intent);
            }

        }
    };
    private RadioGroup.OnCheckedChangeListener radGroupVehiclesListener = new RadioGroup.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radioButton_taipei_car) {
                TaipeiCarOrMotor = 1;
            } else if (checkedId == R.id.radioButton_taipei_motor) {
                TaipeiCarOrMotor = 2;
            }
        }
    };
}
