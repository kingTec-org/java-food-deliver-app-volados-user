package gofereats.datamodels.order;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.order
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import gofereats.datamodels.restaurantdetails.MenuImageList;


/*****************************************************************
 Menu List Model Class
 ****************************************************************/


/**
 * Created by trioangle on 6/8/18.
 */

public class MenuListModel {

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("order_item_id")
    @Expose
    private int orderItemId;

    @SerializedName("menu_item_id")
    @Expose
    private int menuitemid;

    /*@SerializedName("item_image")
    @Expose*/
    private String itemimage;


    @SerializedName("menu_name")
    @Expose
    private String menu_name;


    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("review")
    @Expose
    private int review;

    @SerializedName("item_image")
    @Expose
    private MenuImageList menuImageList;


    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMenuitemid() {
        return menuitemid;
    }

    public void setMenuitemid(int menuitemid) {
        this.menuitemid = menuitemid;
    }

    public String getItemimage() {
        return itemimage;
    }

    public void setItemimage(String itemimage) {
        this.itemimage = itemimage;
    }

    public String getMenuName() {
        return menu_name;
    }

    public void setMenuName(String menu_name) {
        this.menu_name = menu_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public MenuImageList getMenuImageList() {
        return menuImageList;
    }

    public void setMenuImageList(MenuImageList menuImageList) {
        this.menuImageList = menuImageList;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }
}
