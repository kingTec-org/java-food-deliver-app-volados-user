package gofereats.datamodels.location;


/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.location
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/*****************************************************************
 Model Class location fetching
 ****************************************************************/


/**
 * Created by trioangle on 6/6/18.
 */

public class GetLocationModel {
    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("user_address")
    @Expose
    private ArrayList<LocationList> locationList = new ArrayList<>();

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<LocationList> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<LocationList> locationList) {
        this.locationList = locationList;
    }
}
