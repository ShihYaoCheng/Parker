package com.jas.parker.module.map;

import android.util.Log;

import com.google.gson.Gson;
import com.jas.parker.type.ParkerDataObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by User on 2016/11/2.
 */
public class GetDynamicDataFromDB implements Runnable {
    int dynamicParkingLot;
    private String originalId;
    private final String IP = "http://140.121.102.131:8080/DynamicParkingLotServlet/DynamicParkingLotServlet";
    //主機SERVLET位置

    public GetDynamicDataFromDB(String id){
        originalId = id;
    }
    @Override
    public void run() {
        HttpURLConnection conn = null;
        try {
            String resalt = "";

            //http://140.121.197.135/舊的
            //http://140.121.102.131/新的
            Log.v("TAG1", IP+"?originalId=" + originalId);
            URL url = new URL(IP+"?originalId=" + originalId);

            //實驗室401 http servlet
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            Log.v("TAG1", "connect success");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            resalt = reader.readLine();
            Log.v("TAG1",resalt);
            reader.close();

            dynamicParkingLot = Integer.valueOf(resalt);
            //parkerDataObjects = gson.fromJson(jsonStr, ParkerDataObject[].class);
            Log.v("TAG1","resalt:"+dynamicParkingLot);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setDynamicParkingLot(int p) {
        dynamicParkingLot = p;
    }
    public int getDynamicParkingLot(){
        return dynamicParkingLot;
    }

}