package com.jas.parker.module.map;

/**
 * Created by User on 2016/3/31.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.jas.parker.type.BusinessDataObject;
import com.jas.parker.type.ParkerDataObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class GetDataFromDB implements Runnable {
    ParkerDataObject[] parkerDataObjects = new ParkerDataObject[0];
    BusinessDataObject[] businessDataObjects = new BusinessDataObject[0];
    private double x;
    private double y;
    private final String parkerDataIP = "http://140.121.102.131:8080/GetNewParkerData/GetNewParkerData";
    private  final String BusinessDataIP = "http://140.121.102.131:8080/BusinessDataServlet/BusinessDataServlet";

    //主機SERVLET位置

    public GetDataFromDB(double lat,double lon){
        x = lon;
        y = lat;
    }
    @Override
    public void run() {
        HttpURLConnection conn = null;

        try {

            Gson gson = new Gson();
            String parkerJsonStr = "";
            String businessJsonStr = "";
            String xStr = String.valueOf(x);
            String yStr = String.valueOf(y);
           // Log.v("TAG1", xStr);
            //Log.v("TAG1", yStr);
            //http://140.121.197.135/舊的
            //http://140.121.102.131/新的
            Log.v("TAG1", parkerDataIP+"?posX=" + xStr + "&posY=" + yStr);
            Log.v("TAG1", BusinessDataIP+"?posX=" + xStr + "&posY=" + yStr);
            URL parkerUrl = new URL(parkerDataIP+"?posX=" + xStr + "&posY=" + yStr);
            URL businessUrl = new URL(BusinessDataIP+"?posX=" + xStr + "&posY=" + yStr);

            //實驗室401 http servlet
            //parkerData資料界接
            conn = (HttpURLConnection) parkerUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            Log.v("TAG1", "parkerData資料界接 connect success");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            parkerJsonStr = reader.readLine();
            Log.v("TAG1","parkerJsonStr:"+parkerJsonStr);
            reader.close();
            parkerDataObjects = gson.fromJson(parkerJsonStr, ParkerDataObject[].class);
            //Log.v("TAG1",gson.toJson(parkerDataObjects));

            //businessData資料界接
            conn = (HttpURLConnection) businessUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            Log.v("TAG1", "businessData資料界接 connect success");
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            businessJsonStr = reader.readLine();
            Log.v("TAG1","businessJsonStr:"+businessJsonStr);
            reader.close();
            businessDataObjects = gson.fromJson(businessJsonStr, BusinessDataObject[].class);

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
    public void setParkerDataObjects(ParkerDataObject[] odObject) {
        parkerDataObjects = odObject;
    }
    public ParkerDataObject[] getParkerDataObjects(){
        return parkerDataObjects;
    }
    public void setBusinessDataObjects(BusinessDataObject[] odObject) {
        businessDataObjects = odObject;
    }
    public BusinessDataObject[] getBusinessDataObjects(){
        return businessDataObjects;
    }

}