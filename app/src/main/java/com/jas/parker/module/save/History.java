package com.jas.parker.module.save;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jas.parker.type.ParkerDataObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by alex2 on 2016/7/20.
 */
public class History extends FileDataManager{
    private final static String filename = "history.txt"; //檔案名稱
    private static ArrayList<String> historys ;

    public History(){
        super(filename);  //檔案名稱 給FileDataManager 組成儲存目錄
        historys = read(); //創建時先把資料檔案讀取進來 利用父類別的給FileDataManager read();
    }

    public void addHistory(String history) {
        historys.add(history);
        write(false , historys); //不複寫原本檔案
    }


    public void deleteHistory(int index) {
        historys.remove(index);
        write(true,historys); //要複寫原本檔案
    }

    public void clearHistorys(){
        historys.clear();
        write(true , historys);
    }

    public void addHistoryParkerDataObject(ParkerDataObject parkerDataObject){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(parkerDataObject).getAsJsonObject();
        addHistory(jsonObject);
    }

    public void addHistory(JsonObject ParkerDataObject){
        JsonObject favourite = new JsonObject();

        favourite.add("id",ParkerDataObject.get("id"));
        favourite.add("lotName",ParkerDataObject.get("name"));
        favourite.add("lotAddress",ParkerDataObject.get("address"));
        favourite.add("xAxis",ParkerDataObject.get("xAxis"));
        favourite.add("yAxis",ParkerDataObject.get("yAxis"));
        favourite.add("tel",ParkerDataObject.get("tel"));
        favourite.add("payex",ParkerDataObject.get("payex"));
        favourite.add("serviceTime",ParkerDataObject.get("serviceTime"));

        historys.add(favourite.toString());
        write(false ,historys); //不複寫原本檔案
    }

    public ArrayList<String> getHistorys() {
        return historys;
    }
}
