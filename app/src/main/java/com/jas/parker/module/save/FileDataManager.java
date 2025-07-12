package com.jas.parker.module.save;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jas.parker.type.ParkerDataObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by alex2 on 2016/8/7.
 */
public class FileDataManager {

        private String directory ; //儲存目錄
        private File file;

        public FileDataManager(String filename){
            directory = Environment.getExternalStorageDirectory() + "/" + "Parker" + "/" + filename;
            file = new File(directory);

            if (!file.exists())  //如果檔案不存在 創建檔案
                file.getParentFile().mkdirs();
        }

        public  ArrayList<String> read() {
            ArrayList<String> fileDatas = new ArrayList<String>();
            String thisLine;
            try {
                BufferedReader in
                        = new BufferedReader(new FileReader(directory));
                fileDatas.clear(); //讀檔之前清空arraylist

                while ((thisLine = in.readLine()) != null) {
                    fileDatas.add(thisLine);
                }
                in.close();
            } catch (Exception e) {
                Log.d("test", e.getMessage());
            }
            return fileDatas;
        }

        public void write(Boolean isOverwrite , ArrayList<String> fileDatas){
            try {
                PrintWriter out
                        = new PrintWriter(new BufferedWriter(new FileWriter(directory)), !isOverwrite);

                //true : 從原本資料往後寫
                //false : 不append 複寫掉原本資料
                //所以才!isOverwrite
                //系統SDCARD路徑+檔案名稱
                for (int i = 0; i < fileDatas.size(); ++i)
                    out.write(fileDatas.get(i).toString() + "\n");
                //每段資料換行
                out.close();
            } catch (Exception e) {
                Log.d("test", e.getMessage());
            }
        }
    }

