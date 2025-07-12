package com.jas.parker.module.calculation;

/**
 * Created by alex2 on 2016/3/16.
 */
public class testJsonString {
    //1 : timesFee ; 2 : one intervalInfo ; 3 : two or more intervalInfo

    String jsonString1 = "{\"carFeeInfo\":{\"weekday\":{\"previousHoursInfo\":{},\"intervalInfo\":[{\"interval\": \"0~24\",\"weekdayFee\": \"30\",\"feeCycle\": \"30\"}]},\"weekendAndHoliday\":{\"previousHoursInfo\":{},\"intervalInfo\":[{\"interval\": \"0~24\",\"weekdayFee\": \"50\",\"feeCycle\": \"60\"}]}},\"motorFeeInfo\":{\"weekday\":{\"previousHoursInfo\":{},\"intervalInfo\":[]},\"weekendAndHoliday\":{\"previousHoursInfo\":{},\"intervalInfo\":[]}}}";
    String jsonString2 = "{\"carFeeInfo\":{\"weekday\":{\"previousHoursInfo\":{},\"intervalInfo\":[{\"interval\": \"0~18\",\"weekdayFee\": \"30\",\"feeCycle\": \"60\"},{\"interval\": \"18~20\",\"weekdayFee\": \"40\",\"feeCycle\": \"60\"},{\"interval\": \"20~0\",\"weekdayFee\": \"30\",\"feeCycle\": \"60\"}]},\"weekendAndHoliday\":{\"previousHoursInfo\":{},\"intervalInfo\":[]}},\"motorFeeInfo\":{\"weekday\":{\"previousHoursInfo\":{},\"intervalInfo\":[]},\"weekendAndHoliday\":{\"previousHoursInfo\":{},\"intervalInfo\":[]}}}";
    String jsonString3 = "{\"carFeeInfo\":{\"weekday\":{\"timesFee\": \"50\",\"previousHoursInfo\":{},\"intervalInfo\":[]},\"weekendAndHoliday\":{\"previousHoursInfo\":{},\"intervalInfo\":[]}},\"motorFeeInfo\":{\"weekday\":{\"previousHoursInfo\":{},\"intervalInfo\":[]},\"weekendAndHoliday\":{\"previousHoursInfo\":{},\"intervalInfo\":[]}}}";


}
