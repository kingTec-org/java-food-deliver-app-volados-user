package gofereats.datamodels.infowindow;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.infowindow
 * @category InfoWindow
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*****************************************************************
 Restaurant Information
 ****************************************************************/
public class InfoWindow {

    @SerializedName("status_message")
    @Expose
    private String status_message;

    @SerializedName("status_code")
    @Expose
    private String status_code;

    @SerializedName("user_latitude")
    @Expose
    private String user_latitude;

    @SerializedName("user_longitude")
    @Expose
    private String user_longitude;

    @SerializedName("user_location")
    @Expose
    private String user_location;

    @SerializedName("store_latitude")
    @Expose
    private String restaurant_latitude;

    @SerializedName("store_longitude")
    @Expose
    private String restaurant_longitude;

    @SerializedName("store_location")
    @Expose
    private String restaurant_location;

    @SerializedName("store_name")
    @Expose
    private String restaurant_name;

    @SerializedName("store_time")
    @Expose
    private ArrayList<RestaurantOpensInfo> restaurant_time;

    public String getStatusMessage() {
        return status_message;
    }

    public void setStatusMessage(String status_message) {
        this.status_message = status_message;
    }

    public String getStatusCode() {
        return status_code;
    }

    public void setStatusCode(String status_code) {
        this.status_code = status_code;
    }

    public String getUserLatitude() {
        return user_latitude;
    }

    public void setUserLatitude(String user_latitude) {
        this.user_latitude = user_latitude;
    }

    public String getUserLongitude() {
        return user_longitude;
    }

    public void setUserLongitude(String user_longitude) {
        this.user_longitude = user_longitude;
    }

    public String getUserLocation() {
        return user_location;
    }

    public void setUserLocation(String user_location) {
        this.user_location = user_location;
    }

    public String getRestaurantLatitude() {
        return restaurant_latitude;
    }

    public void setRestaurantLatitude(String restaurant_latitude) {
        this.restaurant_latitude = restaurant_latitude;
    }

    public String getRestaurantLongitude() {
        return restaurant_longitude;
    }

    public void setRestaurantLongitude(String restaurant_longitude) {
        this.restaurant_longitude = restaurant_longitude;
    }

    public String getRestaurantLocation() {
        return restaurant_location;
    }

    public void setRestaurantLocation(String restaurant_location) {
        this.restaurant_location = restaurant_location;
    }

    public ArrayList<RestaurantOpensInfo> getRestaurantTime() {
        return restaurant_time;
    }

    public void setRestaurantTime(ArrayList<RestaurantOpensInfo> restaurant_time) {
        this.restaurant_time = restaurant_time;
    }

    public String getRestaurantName() {
        return restaurant_name;
    }

    public void setRestaurantName(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }
}
