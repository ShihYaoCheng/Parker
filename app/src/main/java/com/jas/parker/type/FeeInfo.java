package com.jas.parker.type;

/**
 * Created by User on 2016/4/15.
 */
public class FeeInfo {
    private VehicleFeeInfo carFeeInfo;
    private VehicleFeeInfo motorFeeInfo;

    public FeeInfo(){
        carFeeInfo = new VehicleFeeInfo();
        motorFeeInfo = new VehicleFeeInfo();;
    }
    public FeeInfo(VehicleFeeInfo _carFeeInfo, VehicleFeeInfo _motorFeeInfo) {
        // TODO Auto-generated constructor stub
        setcarFeeInfo(_carFeeInfo);
        setmotorFeeInfo(_motorFeeInfo);
    }

    public VehicleFeeInfo getcarFeeInfo() {
        return carFeeInfo;
    }

    public void setcarFeeInfo(VehicleFeeInfo carFeeInfo) {
        this.carFeeInfo = carFeeInfo;
    }

    public VehicleFeeInfo getmotorFeeInfo() {
        return motorFeeInfo;
    }

    public void setmotorFeeInfo(VehicleFeeInfo motorFeeInfo) {
        this.motorFeeInfo = motorFeeInfo;
    }

}
