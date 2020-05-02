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

import java.io.Serializable;


/*****************************************************************
 Restaurant Menu Item Model Class
 ****************************************************************/

public class RestaurantMenuItemModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer foodId;
    @SerializedName("menu_id")
    @Expose
    private Integer menuId;
    @SerializedName("menu_category_id")
    @Expose
    private Integer menuCategoryId;
    @SerializedName("name")
    @Expose
    private String foodName;
    @SerializedName("price")
    @Expose
    private String foodPrice;
    @SerializedName("type")
    @Expose
    private Integer isVeg;
    @SerializedName("is_visible")
    @Expose
    private Integer isAvailable;
    /*@SerializedName("menu_item_image")
    @Expose*/
    private String foodImage;
    @SerializedName("description")
    @Expose
    private String foodDescription;
    @SerializedName("tax_percentage")
    @Expose
    private String taxPercentage;

    @SerializedName("offer_price")
    @Expose
    private String offerPrice;

    @SerializedName("order_item_id")
    @Expose
    private Integer orderItemId;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("modifier_price")
    @Expose
    private String modifierPrice;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("tax")
    @Expose
    private String Tax;
    @SerializedName("notes")
    @Expose
    private String specialNotes;

    @SerializedName("menu_item_image")
    @Expose
    private MenuImageList menuImage;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("is_offer")
    @Expose
    private int isOffer;

//    menu_item_main_addon:

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(Integer menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public Integer getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(Integer isVeg) {
        this.isVeg = isVeg;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }


    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getModifierPrice() {
        return modifierPrice;
    }

    public void setModifierPrice(String modifierPrice) {
        this.modifierPrice = modifierPrice;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public MenuImageList getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(MenuImageList menuImage) {
        this.menuImage = menuImage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsOffer() {
        return isOffer;
    }

    public void setIsOffer(int isOffer) {
        this.isOffer = isOffer;
    }
}