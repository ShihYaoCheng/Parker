package com.jas.parker.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jas.parker.R;

public class ReportFragment extends Fragment {

    String[] addresses;
    EditText edt_text;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_contact for this fragment


        try {
            view = inflater.inflate(R.layout.content_report, container, false);
        } catch (InflateException e) {

        }

        addresses=new String[1];
        addresses[0]="jasno1studio@gmail.com";

        Button btn_report=(Button)view.findViewById(R.id.btn_report);
        btn_report.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View view){
                composeEmail(addresses,edt_text.getText().toString());


            }


        });

        edt_text=(EditText)view.findViewById(R.id.edt_subject);

        return view;


    }


    public void composeEmail(String[] addresses, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Parker問題回報");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}

