package gofereats.datamodels.main.home;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.main.home
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import gofereats.datamodels.restaurantdetails.RestOfferModel;

/*****************************************************************
 Model Class Restaurant List Model
 ****************************************************************/


public class RestaurantListModel {
    @SerializedName("store_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("name")
    @Expose
    private String restaurantName;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("min_time")
    @Expose
    private String minTime;
    @SerializedName("max_time")
    @Expose
    private String maxTime;
    @SerializedName("store_rating")
    @Expose
    private Float restaurantRating;
    @SerializedName("price_rating")
    @Expose
    private Float priceRating;
    @SerializedName("average_rating")
    @Expose
    private Integer averageRating;
    @SerializedName("wished")
    @Expose
    private Integer wished;

    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("store_offer")
    @Expose
    private ArrayList<RestOfferModel> restaurantOffer = new ArrayList<>();

    @SerializedName("store_closed")
    @Expose
    private int restaurant_closed;
    @SerializedName("banner")
    @Expose
    private BannerImageList bannerList;

    @SerializedName("store_next_time")
    @Expose
    private String restaurantNextTime;


    public RestaurantListModel(String type) {
        this.type = type;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Float getRestaurantRating() {
        return restaurantRating;
    }

    public void setRestaurantRating(Float restaurantRating) {
        this.restaurantRating = restaurantRating;
    }

    public Float getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(Float priceRating) {
        this.priceRating = priceRating;
    }

    public Integer getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Integer averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getWished() {
        return wished;
    }

    public void setWished(Integer wished) {
        this.wished = wished;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<RestOfferModel> getRestaurantOffer() {
        return restaurantOffer;
    }

    public void setRestaurantOffer(ArrayList<RestOfferModel> restaurantOffer) {
        this.restaurantOffer = restaurantOffer;
    }

    public int getRestaurantClosed() {
        return restaurant_closed;
    }

    public void setRestaurantClosed(int restaurant_closed) {
        this.restaurant_closed = restaurant_closed;
    }

    public BannerImageList getBannerList() {
        return bannerList;
    }

    public void setBannerList(BannerImageList bannerList) {
        this.bannerList = bannerList;
    }

    public String getRestaurantNextTime() {
        return restaurantNextTime;
    }

    public void setRestaurantNextTime(String restaurantNextTime) {
        this.restaurantNextTime = restaurantNextTime;
    }
}
