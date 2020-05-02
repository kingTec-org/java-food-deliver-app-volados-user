package gofereats.datamodels.searchcategory;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.searchcategory
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import gofereats.datamodels.main.home.RestaurantListModel;

/**
 * Created by trioangle on 6/8/18.
 */

/*****************************************************************
 ResultList  Model  Class
 ****************************************************************/

public class ResultListModel {

    @SerializedName("status_message")
    @Expose
    private String statusmessage;

    @SerializedName("status_code")
    @Expose
    private String statuscode;

   /* @SerializedName("count")
    @Expose
    private int resultcount;*/


    @SerializedName("category")
    @Expose
    private ArrayList<RestaurantListModel> RestaurantResult;


    public String getStatusmessage() {
        return statusmessage;
    }

    public void setStatusmessage(String statusmessage) {
        this.statusmessage = statusmessage;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    /*public int getResultcount() {
        return resultcount;
    }

    public void setResultcount(int resultcount) {
        this.resultcount = resultcount;
    }*/

    public ArrayList<RestaurantListModel> getRestaurantResult() {
        return RestaurantResult;
    }

    public void setRestaurantResult(ArrayList<RestaurantListModel> restaurantResult) {
        RestaurantResult = restaurantResult;
    }
}
