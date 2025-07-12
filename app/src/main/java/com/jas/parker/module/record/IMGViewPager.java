package com.jas.parker.module.record;

/**
 * Created by bluej on 2016/3/27.
 */

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jas.parker.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IMGViewPager {

    public static final String TAG = "IMGViewPager";

    public static ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    public static File IMGs[];
    public static DataManager picManager;
    public static ViewPager picViewPager;
    public static FragmentManager ActivityFragmentManager;

    public IMGViewPager(FragmentManager fragmentManager, ViewPager thePicViewPager) {

        picManager = new DataManager();
        IMGs = picManager.getAllIMG();
        picViewPager = thePicViewPager;
        ActivityFragmentManager = fragmentManager;
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(fragmentManager);

    }

    public static void setIMGAdapter() {

        picViewPager.setAdapter(imageFragmentPagerAdapter);




    }

    public static void refreshViewPager(){


        imageFragmentPagerAdapter.notifyDataSetChanged();
    }

    /*
    public void setDefaultIMG(){
        picViewPager.set
                defultPagerAdapter = new ImageFragmentPagerAdapter(ActivityFragmentManager);

    }
    */

    public static void updateData(){
        IMGs = picManager.getAllIMG();

    }



    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {

        public ImageFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            if (IMGs == null) return 0;
            return IMGs.length;
        }

        @Override
        public Fragment getItem(int position) {

           return SwipeFragment.newInstance(position);
        }



    }

    public static class SwipeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            //imageFragmentPagerAdapter.notifyDataSetChanged();

            View swipeView = inflater.inflate(R.layout.viewpager_imageview, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
            ImageButton deleteButton = (ImageButton) swipeView.findViewById(R.id.btn_delete);
            TextView picTime = (TextView) swipeView.findViewById(R.id.picTime);
            Bundle bundle = getArguments();
            final int position = bundle.getInt("position");
            String fileName = IMGs[position].getName();

            SimpleDateFormat oriSDF = new SimpleDateFormat("yyyyMMdd_HHmmss");

            Date picDate=null;

            try {
                picDate =oriSDF.parse(fileName.substring(4,19));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String picDataStr="無法取得時間";
            SimpleDateFormat newSDF = new SimpleDateFormat("MM月dd日 HH:mm:ss");
            if(picDate!=null){
            picDataStr=newSDF.format(picDate);}
            picTime.setText(picDataStr);


            //將放置的圖片進行處理
            //imageView.setImageBitmap(RotationIMG.rotateImage(ScaledDownIMG.decodeSampledBitmapFromFile(IMGs[position], 480, 270), 90));


           imageView.setImageBitmap(RotationIMG.rotateImage(ScaledDownIMG.decodeSampledBitmapFromFile(IMGs[position], 960, 540), 90));

            //imageView.setImageBitmap(BitmapFactory.decodeFile(IMGs[position].getPath()));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent IMGIntent = new Intent(Intent.ACTION_VIEW);
                    IMGIntent.setDataAndType(Uri.fromFile(IMGs[position]), "image/*");
                    startActivityForResult(IMGIntent, 101);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final View deleteView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete, null);

                    new AlertDialog.Builder(getActivity())
                            .setTitle("確認刪除?")

                            .setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                    IMGs[position].delete();

                                    updateData();
                                    setIMGAdapter();

                                    refreshViewPager();

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                }
                            })

                            .show()
                    ;

                   // refreshViewPager();

                }
            });


            return swipeView;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

            imageFragmentPagerAdapter.notifyDataSetChanged();
        }


        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }


}


