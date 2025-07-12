package com.jas.parker.module.record;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by bluej on 2016/3/26.
 */
public class DataManager {

    //確認是否有插入SDCard
    private static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    //讀取SDCard圖片，型態為Bitmap
    private static Bitmap getBitmapFromExternalStorageDirectory(String file) {
        try {
            String sd = Environment.getExternalStorageDirectory().toString();
            Bitmap bitmap = BitmapFactory.decodeFile(sd + "/" + file);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File[] getAllIMG() {

        File IMGDir = new File(Environment.getExternalStorageDirectory(), "/Parker/photos");
        Log.d("ExCamera", IMGDir.getAbsolutePath());
        if (!checkSDCard()) {
            Log.d("ExCamera", "沒有SD卡");
            return null;
        }

        if (!IMGDir.exists()) {
            IMGDir.mkdirs();
            Log.d("ExCamera", "創建中");

        }


        FilenameFilter filefilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg");
            }
        };

        File IMGFiles[] = IMGDir.listFiles(filefilter);
        if (IMGFiles != null) {
            IMGFiles = fileSort(IMGFiles);
        }
        return IMGFiles;


    }

    public static File[] fileSort(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            }
        });
        return files;
    }


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type) {

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Parker/photos");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("ExCamera", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public void deleteNote() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Parker/note.txt");
        if (file.exists()) {
            file.delete();
        }

    }

    public boolean saveNote(String noteText) {

        File filePath = new File(Environment.getExternalStorageDirectory(), "/Parker");

        try {
            if (!checkSDCard()) {
                Log.e("ExNote", "無SD");
                return false;
            }
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            //寫成文字檔
            FileWriter fw = new FileWriter(filePath.getPath() + "/note.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(noteText);
            bw.close();
            Log.e("ExNote", "成功寫入");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public String readNote() {
        String noteText = "";
        File filename = new File(Environment.getExternalStorageDirectory() + "/Parker", "note.txt");
        try {
            if (!checkSDCard()) {
                Log.e("ExNote", "無SD");
                return null;
            }
            if (!filename.exists()) {
                Log.e("ExNote", "沒有之前資料");
                return null;
            }

            FileInputStream fin = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            String line = "", wholeData = "";

            while ((line = reader.readLine()) != null) {
                wholeData = wholeData + line + " ";

            }
            noteText = wholeData;
            reader.close();
            fin.close();
            return noteText;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

}
