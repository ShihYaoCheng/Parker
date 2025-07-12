package com.jas.parker.type;

import com.google.gson.*;

import java.util.ArrayList;

/**
 * Created by alex2 on 2016/6/30.
 */
public class Lot {
    private String id;
    private String lotName;
    private String lotAddress;
    private String xAxis;
    private String yAxis;
    private String tel;
    private String payex;
    private String serviceTime;

    public Lot(JsonObject object) {
        this.id = nullProcessor(object.get("id"));
        this.lotName = nullProcessor(object.get("lotName"));
        this.lotAddress = nullProcessor(object.get("lotAddress"));
        this.xAxis = nullProcessor(object.get("xAxis"));
        this.yAxis = nullProcessor(object.get("yAxis"));
        this.tel = nullProcessor(object.get("tel"));
        this.payex = nullProcessor(object.get("payex"));
        this.serviceTime = nullProcessor(object.get("serviceTime"));
    }

    public String nullProcessor(JsonElement element) {
        return element.isJsonNull() ? "政府開放資料未提供此資訊" : element.getAsString();
    }

    //處理每行json資料的接口 並將它包裝成Arraylist<Lot>
    public static ArrayList<Lot> fromJson(ArrayList<String> jsonStrings) {

        ArrayList<Lot> lotsInfo = new ArrayList<Lot>();

        for (int i = 0; i < jsonStrings.size(); ++i) {
            JsonObject lotJsonInfo = new JsonParser().parse(jsonStrings.get(i)).getAsJsonObject();
            Lot lotInfo = new Lot(lotJsonInfo);
            lotsInfo.add(lotInfo);
        }
        return lotsInfo;
    }

    public String getId() {
        return id;
    }

    public String getLotAddress() {
        return lotAddress;
    }

    public String getLotName() {
        return lotName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLotAddress(String lotLocation) {
        this.lotAddress = lotLocation;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getPayex() {
        return payex;
    }

    public void setPayex(String payex) {
        this.payex = payex;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }
}
