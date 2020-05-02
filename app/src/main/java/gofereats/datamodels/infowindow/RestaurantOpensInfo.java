package gofereats.datamodels.infowindow;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.infowindow
 * @category RestaurantOpensInfo
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*****************************************************************
 Restaurant  opening details Information
 ****************************************************************/

public class RestaurantOpensInfo {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("store_id")
    @Expose
    private int restaurant_id;

    @SerializedName("start_time")
    @Expose
    private String start_time;

    @SerializedName("end_time")
    @Expose
    private String end_time;

    @SerializedName("day")
    @Expose
    private int day;

    @SerializedName("day_name")
    @Expose
    private String day_name;

    @SerializedName("closed")
    @Expose
    private int closed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurant_id;
    }

    public void setRestaurantId(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getStartTime() {
        return start_time;
    }

    public void setStartTime(String start_time) {
        this.start_time = start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getDayName() {
        return day_name;
    }

    public void setDayName(String day_name) {
        this.day_name = day_name;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }
}
