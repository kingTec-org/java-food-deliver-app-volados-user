package gofereats.datamodels.order;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.order
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*****************************************************************
 Order  Result Model Class
 ****************************************************************/


public class OrderResultModel {

    @SerializedName("status_message")
    @Expose
    private String status_message;

    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("order_history")
    @Expose
    private ArrayList<OrderModel> orderHistroyModel;

    @SerializedName("upcoming")
    @Expose
    private ArrayList<OrderModel> orderUpcomingModel;

    public String getStatusMessage() {
        return status_message;
    }

    public void setStatusMessage(String status_message) {
        this.status_message = status_message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


    public ArrayList<OrderModel> getOrderHistroyModel() {
        return orderHistroyModel;
    }

    public void setOrderHistroyModel(ArrayList<OrderModel> orderHistroyModel) {
        this.orderHistroyModel = orderHistroyModel;
    }

    public ArrayList<OrderModel> getOrderUpcomingModel() {
        return orderUpcomingModel;
    }

    public void setOrderUpcomingModel(ArrayList<OrderModel> orderUpcomingModel) {
        this.orderUpcomingModel = orderUpcomingModel;
    }
}