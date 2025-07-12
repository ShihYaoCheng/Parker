package com.jas.parker.type;

/**
 * Created by User on 2016/11/4.
 */
public class BusinessDataObject {

    private String uniformNumbers;
    private String businessName ;
    private String businessAddress;
    private double xAxis;
    private double yAxis;

    public BusinessDataObject(String numbers, String name ,String address ,double xAxis,double yAxis) {
        // TODO Auto-generated constructor stub
        setUniformNumbers(numbers);
        setBusinessName(name);
        setBusinessAddress(address);
        setXAxis(xAxis);
        setYAxis(yAxis);
    }
    public String getUniformNumbers(){
        return this.uniformNumbers;
    }
    public void setUniformNumbers(String uniformNumbers){
        this.uniformNumbers=uniformNumbers;
    }
    public String getBusinessName(){
        return this.businessName;
    }
    public void setBusinessName(String businessName){
        this.businessName=businessName;
    }
    public String getBusinessAddress(){
        return this.businessAddress;
    }
    public void setBusinessAddress(String businessAddress){
        this.businessAddress=businessAddress;
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

}
