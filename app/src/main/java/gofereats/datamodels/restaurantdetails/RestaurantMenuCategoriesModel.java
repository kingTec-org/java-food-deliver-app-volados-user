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
 Restaurant Menu Categories Model Class
 ****************************************************************/

public class RestaurantMenuCategoriesModel {

    @SerializedName("id")
    @Expose
    private Integer menuCategoryId;

    @SerializedName("menu_id")
    @Expose
    private Integer menuId;

    @SerializedName("category_position")
    @Expose
    private Integer categoryPosition;

    @SerializedName("name")
    @Expose
    private String menuCategoryName;

    @SerializedName("menu_item")
    @Expose
    private ArrayList<RestaurantMenuItemModel> menuItemDetails = new ArrayList<>();

    public Integer getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(Integer menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuCategoryName() {
        return menuCategoryName;
    }

    public void setMenuCategoryName(String menuCategoryName) {
        this.menuCategoryName = menuCategoryName;
    }

    public ArrayList<RestaurantMenuItemModel> getMenuItemDetails() {
        return menuItemDetails;
    }

    public void setMenuItemDetails(ArrayList<RestaurantMenuItemModel> menuItemDetails) {
        this.menuItemDetails = menuItemDetails;
    }


    public Integer getCategoryPosition() {
        return categoryPosition;
    }

    public void setCategoryPosition(Integer categoryPosition) {
        this.categoryPosition = categoryPosition;
    }
}