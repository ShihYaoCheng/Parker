package com.jas.parker.type;

/**
 * Created by User on 2016/3/31.
 */
import com.google.gson.Gson;

import java.io.Serializable;

public class ParkerDataObject implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;

    private int id;

    private String originalId;
    private String taiwanCity ;
    private String taiwanArea;
    private String name;
    private String type;
    private String summary;
    private String address;
    private double xAxis;
    private double yAxis;
    private String tel;
    private String payex;
    private int totalCar;
    private int totalMotor;
    private String serviceTime;
    private FeeInfo feeInfo;

    private String simpleFeeType;
    private int simpleFee;

    private double Distance;

    public ParkerDataObject( int id,String originalId,String taiwanCity, String taiwanArea, String name ,
                             String type,String summary,String address,double xAxis,double yAxis,String tel,
                             String payex,int totalCar,int totalMotor,String serviceTime,String feeInfo
            ,String simpleFeeType,int simpleFee) {
        Gson gson = new Gson();

        setId(id);
        //System.out.println(originalId);
        setOriginalId(originalId);
        setTaiwanCity(taiwanCity);
        setTaiwanArea(taiwanArea);
        setName(name);
        setType(type);
        setSummary(summary);
        setAddress(address);
        setXAxis(xAxis);
        setYAxis(yAxis);
        setTel(tel);
        setPayex(payex);
        setTotalCar(totalCar);
        setTotalMotor(totalMotor);
        setServiceTime(serviceTime);
        setFeeInfo(gson.fromJson(feeInfo, FeeInfo.class));

        setSimpleFeeType(simpleFeeType);
        setSimpleFee(simpleFee);

    }

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getOriginalId(){
        return this.originalId ;
    }
    public void setOriginalId(String originalId){
        this.originalId =originalId;
    }

    public String getTaiwanArea(){
        return this.taiwanArea;
    }
    public void setTaiwanArea(String taiwanArea){
        this.taiwanArea=taiwanArea;
    }
    public String getTaiwanCity(){
        return this.taiwanCity;
    }
    public void setTaiwanCity(String taiwanCity){
        this.taiwanCity=taiwanCity;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name=name;
    }

    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type=type;
    }

    public String getSummary(){
        return this.summary;
    }
    public void setSummary(String summary){
        this.summary=summary;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address=address;
    }

    public double getXAxis(){
        return this.xAxis;
    }
    public void setXAxis(double xAxis){
        this.xAxis=xAxis;
    }

    public double getYAxis(){
        return this.yAxis;
    }
    public void setYAxis(double yAxis){
        this.yAxis=yAxis;
    }

    public String getTel(){
        return this.tel;
    }
    public void setTel(String tel){
        this.tel=tel;
    }

    public String getPayex(){
        return this.payex;
    }
    public void setPayex(String payex){
        this.payex=payex;
    }

    public int getTotalCar(){
        return this.totalCar;
    }
    public void setTotalCar(int totalCar){
        this.totalCar =totalCar;
    }

    public int getTotalMotor(){
        return this.totalMotor;
    }
    public void setTotalMotor(int totalMotor){
        this.totalMotor=totalMotor;
    }

    public String getServiceTime(){
        return this.serviceTime;
    }
    public void setServiceTime(String serviceTime){
        this.serviceTime=serviceTime;
    }

    public FeeInfo getFeeInfo(){
        return this.feeInfo ;
    }
    public void setFeeInfo(FeeInfo FeeInfo){
        this.feeInfo =FeeInfo;
    }

    public String getSimpleFeeType(){
        return this.simpleFeeType ;
    }
    public void setSimpleFeeType(String simpleFeeType){
        this.simpleFeeType = simpleFeeType;

    }

    public int getSimpleFee(){
        return this.simpleFee;
    }
    public void setSimpleFee(int simpleFee) {
        this.simpleFee = simpleFee;

    }
    //寫入跟使用者距離
    public void setDistance(double distance)
    {
        Distance = distance;
    }

    //取的跟使用者距離
    public double getDistance()
    {
        return Distance;
    }




    public String toString() {
        return name + " - " + tel;
    }

}