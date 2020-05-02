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


/*****************************************************************
 Restaurant Menu  Model Class
 ****************************************************************/

public class RestaurantMenuModel {
    @SerializedName("id")
    @Expose
    private Integer menuId;

    @SerializedName("store_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("name")
    @Expose
    private String menuName;// Diffen, Lunch , Dinner , Brunch like...
    @SerializedName("menu_time")
    @Expose
    private String menuTime;
    @SerializedName("menu_start_time")
    @Expose
    private String menuStartTime;
    @SerializedName("menu_end_time")
    @Expose
    private String menuEndTime;
    @SerializedName("menu_closed")
    @Expose
    private int menuClosed;
    @SerializedName("menu_category")
    @Expose
    private ArrayList<RestaurantMenuCategoriesModel> restaurantMenuCategoriesDetails = new ArrayList<>();


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuTime() {
        return menuTime;
    }

    public void setMenuTime(String menuTime) {
        this.menuTime = menuTime;
    }

    public ArrayList<RestaurantMenuCategoriesModel> getRestaurantMenuCategoriesDetails() {
        return restaurantMenuCategoriesDetails;
    }

    public void setRestaurantMenuCategoriesDetails(ArrayList<RestaurantMenuCategoriesModel> restaurantMenuCategoriesDetails) {
        this.restaurantMenuCategoriesDetails = restaurantMenuCategoriesDetails;
    }

    public String getMenuStartTime() {
        return menuStartTime;
    }

    public void setMenuStartTime(String menuStartTime) {
        this.menuStartTime = menuStartTime;
    }

    public String getMenuEndTime() {
        return menuEndTime;
    }

    public void setMenuEndTime(String menuEndTime) {
        this.menuEndTime = menuEndTime;
    }

    public int getMenuClosed() {
        return menuClosed;
    }

    public void setMenuClosed(int menuClosed) {
        this.menuClosed = menuClosed;
    }
}