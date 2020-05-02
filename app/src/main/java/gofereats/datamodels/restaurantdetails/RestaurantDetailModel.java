package gofereats.datamodels.restaurantdetails;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.restaurantDetails
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import gofereats.datamodels.main.home.BannerImageList;

/*****************************************************************
 Restaurant Details Model Class
 ****************************************************************/


public class RestaurantDetailModel {

    @SerializedName("store_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("name")
    @Expose
    private String restaurantName;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("wished")
    @Expose
    private Integer wished;
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("min_time")
    @Expose
    private String minTime;
    @SerializedName("max_time")
    @Expose
    private String maxTime;
    @SerializedName("preparation_day")
    @Expose
    private String preparationDay;
    @SerializedName("today_preparation_min")
    @Expose
    private String todayPreparationMin;
    @SerializedName("today_preparation_max")
    @Expose
    private String todayPreparationMax;
    @SerializedName("store_rating")
    @Expose
    private Float restaurantRating;
    @SerializedName("price_rating")
    @Expose
    private Integer priceRating;
    @SerializedName("average_rating")
    @Expose
    private Integer averageRating;
    @SerializedName("store_menu")
    @Expose
    private ArrayList<RestaurantMenuModel> restaurantMenuDetails = new ArrayList<>();
    @SerializedName("store_offer")
    @Expose
    private ArrayList<RestOfferModel> RestaurantOfferDetails = new ArrayList<>();
    @SerializedName("store_closed")
    @Expose
    private int restaurant_closed;
    @SerializedName("store_open_time")
    @Expose
    private String restaurant_open_time;
    @SerializedName("banner")
    @Expose
    private BannerImageList bannerList;
    @SerializedName("store_next_time")
    @Expose
    private String restaurantNextTime;

    public String getTodayPreparationMin() {
        return todayPreparationMin;
    }

    public void setTodayPreparationMin(String todayPreparationMin) {
        this.todayPreparationMin = todayPreparationMin;
    }

    public String getTodayPreparationMax() {
        return todayPreparationMax;
    }

    public void setTodayPreparationMax(String todayPreparationMax) {
        this.todayPreparationMax = todayPreparationMax;
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

    public Integer getWished() {
        return wished;
    }

    public void setWished(Integer wished) {
        this.wished = wished;
    }

    public String getPreparationDay() {
        return preparationDay;
    }

    public void setPreparationDay(String preparationDay) {
        this.preparationDay = preparationDay;
    }


    public ArrayList<RestaurantMenuModel> getRestaurantMenuDetails() {
        return restaurantMenuDetails;
    }

    public void setRestaurantMenuDetails(ArrayList<RestaurantMenuModel> restaurantMenuDetails) {
        this.restaurantMenuDetails = restaurantMenuDetails;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getRestaurantRating() {
        return restaurantRating;
    }

    public void setRestaurantRating(Float restaurantRating) {
        this.restaurantRating = restaurantRating;
    }

    public Integer getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(Integer priceRating) {
        this.priceRating = priceRating;
    }

    public Integer getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Integer averageRating) {
        this.averageRating = averageRating;
    }

    public ArrayList<RestOfferModel> getRestaurantOfferDetails() {
        return RestaurantOfferDetails;
    }

    public void setRestaurantOfferDetails(ArrayList<RestOfferModel> restaurantOfferDetails) {
        RestaurantOfferDetails = restaurantOfferDetails;
    }

    public int getRestaurantClosed() {
        return restaurant_closed;
    }

    public void setRestaurantClosed(int restaurant_closed) {
        this.restaurant_closed = restaurant_closed;
    }

    public String getRestaurantOpenTime() {
        return restaurant_open_time;
    }

    public void setRestaurantOpenTime(String restaurant_open_time) {
        this.restaurant_open_time = restaurant_open_time;
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