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
 * Created by alex2 on 2016/7/11.
 */
public class Favourite extends FileDataManager {
    private final static String filename = "favourite.txt"; //檔案名稱
    private static ArrayList<String> favourites;

    public Favourite() {
        super(filename);  //檔案名稱 給FileDataManager 組成儲存目錄
        favourites = read(); //創建時先把資料檔案讀取進來 利用父類別的給FileDataManager read();
    }

    public void addFavourite(String favourite) {
        favourites.add(favourite);
        write(false, favourites); //不複寫原本檔案
    }

    public void addFavouriteParkerDataObject(ParkerDataObject parkerDataObject) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(parkerDataObject).getAsJsonObject();
        addFavourite(jsonObject);
    }

    public void addFavourite(JsonObject ParkerDataObject) {
        JsonObject favourite = new JsonObject();

        favourite.add("id", ParkerDataObject.get("id"));
        favourite.add("lotName", ParkerDataObject.get("name"));
        favourite.add("lotAddress", ParkerDataObject.get("address"));
        favourite.add("xAxis", ParkerDataObject.get("xAxis"));
        favourite.add("yAxis", ParkerDataObject.get("yAxis"));
        favourite.add("tel", ParkerDataObject.get("tel"));
        favourite.add("payex", ParkerDataObject.get("payex"));
        favourite.add("serviceTime", ParkerDataObject.get("serviceTime"));

        favourites.add(favourite.toString());
        write(false, favourites); //不複寫原本檔案
    }


    public void deleteFavourite(int index) {
        favourites.remove(index);
        write(true, favourites); //要複寫原本檔案
    }


    public ArrayList<String> getFavourites() {
        return favourites;
    }
}
