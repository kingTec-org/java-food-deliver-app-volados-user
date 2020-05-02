package gofereats.datamodels.wishlist;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.wishlist
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import gofereats.datamodels.main.home.BannerImageList;

/**
 * Created by trioangle on 6/26/18.
 */

/*****************************************************************
 WishListArray  Model  Class
 ****************************************************************/


public class WishListArrayModel {


    @SerializedName("store_id")
    @Expose
    private int restaurant_id;

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("category")
    @Expose
    private String category;


    @SerializedName("min_time")
    @Expose
    private int min_time;


    @SerializedName("max_time")
    @Expose
    private int max_time;


    @SerializedName("price_rating")
    @Expose
    private int price_rating;


    @SerializedName("average_rating")
    @Expose
    private int average_rating;

    @SerializedName("wished")
    @Expose
    private int wished;


    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("banner")
    @Expose
    private BannerImageList bannerImageList;


    public int getRestaurantId() {
        return restaurant_id;
    }

    public void setRestaurantId(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public int getMinTime() {
        return min_time;
    }

    public void setMinTime(int min_time) {
        this.min_time = min_time;
    }

    public int getMaxTime() {
        return max_time;
    }

    public void setMaxTime(int max_time) {
        this.max_time = max_time;
    }

    public int getPriceRating() {
        return price_rating;
    }

    public void setPriceRating(int price_rating) {
        this.price_rating = price_rating;
    }

    public int getAverageRating() {
        return average_rating;
    }

    public void setAverageRating(int average_rating) {
        this.average_rating = average_rating;
    }

    public int getWished() {
        return wished;
    }

    public void setWished(int wished) {
        this.wished = wished;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BannerImageList getBannerImageList() {
        return bannerImageList;
    }

    public void setBannerImageList(BannerImageList bannerImageList) {
        this.bannerImageList = bannerImageList;
    }
}
