package com.jas.parker.fragment;

/**
 * Created by bluej on 2016/4/12.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.jas.parker.R;
import com.jas.parker.module.record.DataManager;
import com.jas.parker.module.record.IMGViewPager;


public class ParkingRecordFragment extends Fragment {


    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static Uri fileUri;
    private static DataManager dataManager;
    private static View view;
    private static ViewPager picViewPager;
    TextView textView;
    ImageButton cameraButton;
    ImageButton openNote;
    //  IMGViewPager.ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    IMGViewPager imgViewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = new DataManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /*
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        */
        try {
            view = inflater.inflate(R.layout.content_record_photos, container, false);
        } catch (InflateException e) {

        }


        textView = (TextView) view.findViewById(R.id.noteTextView);
        if (textView != null) {
            textView.setText(dataManager.readNote());
        }

        cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //使用Intent拍照
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = dataManager.getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        ImageView defaultImg=(ImageView)view.findViewById(R.id.default_img);
        defaultImg.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //使用Intent拍照
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = dataManager.getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });


        openNote = (ImageButton) view.findViewById(R.id.noteButton);
        openNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //更改
               // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                final View noteView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_note, null);
                EditText edtNoteText = (EditText) noteView.findViewById(R.id.noteText);
                edtNoteText.setText(dataManager.readNote());
              //  edtNoteText.requestFocus();
                edtNoteText.setSelectAllOnFocus(true);





                new AlertDialog.Builder(getActivity())
                        .setTitle("請填入紀錄資訊")
                        .setView(noteView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                EditText editText = (EditText) noteView.findViewById(R.id.noteText);

                                if (editText != null) {
                                    Log.e("ExNote", editText.getText().toString());
                                    if (!dataManager.saveNote(editText.getText().toString())) {
                                        Log.e("ExNote", "儲存錯誤");
                                    }
                                    Log.e("ExNote", "Value is: " + editText.getText());
                                    //更新回去
                                    //錯誤待修正
                                    if (textView != null) {
                                        textView.setText(editText.getText().toString());
                                    }
                                } else {
                                    Log.e("ExNote", "EditText not found!");
                                }

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dataManager.deleteNote();
                                if (textView != null) {
                                    textView.setText("");
                                }
                            }
                        })
                        .show()
                        ;
            }
        });


        picViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        picViewPager.setOffscreenPageLimit(5);



        ViewPagerAsync viewPagerLoader = new ViewPagerAsync();
        viewPagerLoader.execute();

        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent

                imgViewPager.updateData();
                imgViewPager.setIMGAdapter();
                imgViewPager.refreshViewPager();

                /*
                ViewPagerAsync viewPagerLoader = new ViewPagerAsync();
                viewPagerLoader.execute();
                */

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "取消照相", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "拍攝失敗", Toast.LENGTH_LONG).show();
            }
        }


    }






    private class ViewPagerAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            //執行前 設定可以在這邊設定
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            /*
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */

            if(imgViewPager==null) {
                imgViewPager = new IMGViewPager(getChildFragmentManager(), picViewPager);
            }else{
            imgViewPager.updateData();}
            return null;
        }


        @Override
        protected void onPostExecute(Void voids) {
            //執行後 完成背景任務
            super.onPostExecute(voids);
            imgViewPager.setIMGAdapter();

            imgViewPager.refreshViewPager();

        }
    }

}