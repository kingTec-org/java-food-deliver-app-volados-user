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

/*****************************************************************
 Model Class Restaurant offer Model
 ****************************************************************/


public class RestaurantOfferModel {
    @SerializedName("store_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("name")
    @Expose
    private String restaurantName;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("banner")
    @Expose
    private BannerImageList bannerList;


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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BannerImageList getBannerList() {
        return bannerList;
    }

    public void setBannerList(BannerImageList bannerList) {
        this.bannerList = bannerList;
    }
}
