package gofereats.datamodels.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.cart
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/

/*****************************************************************
 Cart Restaurant Model Class
 ****************************************************************/


public class CartRestaurantModel {

    @SerializedName("id")
    @Expose
    private Integer restaurantId;
    @SerializedName("name")
    @Expose
    private String restaurantName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("convert_mintime")
    @Expose
    private String minTime;
    @SerializedName("convert_maxtime")
    @Expose
    private String maxTime;
    @SerializedName("type")
    @Expose
    private String type;

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinTime() {
        return minTime;
    }

    public void setMinTime(String minTime) {
        this.minTime = minTime;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}