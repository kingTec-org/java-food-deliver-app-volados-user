package gofereats.datamodels.ratings;


/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.ratings
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import gofereats.datamodels.main.home.BannerImageList;
import gofereats.datamodels.order.MenuListModel;
import gofereats.datamodels.ratings.issueslist.DriverIssueListModel;
import gofereats.datamodels.ratings.issueslist.FoodIssueModelList;

/**
 * Created by trioangle on 6/20/18.
 */

/*****************************************************************
 Rating Order Details Model Class
 ****************************************************************/


public class RatingOrderDetailsModel {

    @SerializedName("order_id")
    @Expose
    private int orderId;

    @SerializedName("name")
    @Expose
    private String resturantName;
    @SerializedName("store_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    /*@SerializedName("restaurant_banner")
    @Expose*/
    private String restauranBanner;

    @SerializedName("driver_image")
    @Expose
    private String driverImage;

    @SerializedName("driver_name")
    @Expose
    private String driverName;

    @SerializedName("issue_user_menu_item")
    @Expose
    private ArrayList<FoodIssueModelList> foodIssues;

    @SerializedName("issue_user_driver")
    @Expose
    private ArrayList<DriverIssueListModel> driverIssues;

    @SerializedName("menu")
    @Expose
    private ArrayList<MenuListModel> menuList;

    @SerializedName("store_banner")
    @Expose
    private BannerImageList bannerImageList;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getResturantName() {
        return resturantName;
    }

    public void setResturantName(String resturantName) {
        this.resturantName = resturantName;
    }

    public String getRestauranBanner() {
        return restauranBanner;
    }

    public void setRestauranBanner(String restauranBanner) {
        this.restauranBanner = restauranBanner;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public ArrayList<FoodIssueModelList> getFoodIssues() {
        return foodIssues;
    }

    public void setFoodIssues(ArrayList<FoodIssueModelList> foodIssues) {
        this.foodIssues = foodIssues;
    }

    public ArrayList<DriverIssueListModel> getDriverIssues() {
        return driverIssues;
    }

    public void setDriverIssues(ArrayList<DriverIssueListModel> driverIssues) {
        this.driverIssues = driverIssues;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public ArrayList<MenuListModel> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<MenuListModel> menuList) {
        this.menuList = menuList;
    }

    public BannerImageList getBannerImageList() {
        return bannerImageList;
    }

    public void setBannerImageList(BannerImageList bannerImageList) {
        this.bannerImageList = bannerImageList;
    }
}
