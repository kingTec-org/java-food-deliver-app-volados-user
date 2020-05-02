package gofereats.datamodels.cart;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.cart
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


/*****************************************************************
 Cart Details Model Class
 ****************************************************************/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import gofereats.datamodels.restaurantdetails.RestaurantMenuItemModel;

/**
 * Created by trioangle on 6/5/18.
 */

public class CartDetailModel {
    @SerializedName("id")
    @Expose
    private Integer orderId;
    @SerializedName("store_id")
    @Expose
    private Integer restaurantId;
    @SerializedName("subtotal")
    @Expose
    private String subTotal;
    @SerializedName("offer_amount")
    @Expose
    private String offerAmount;
    @SerializedName("promo_amount")
    @Expose
    private String promoAmount;
    @SerializedName("delivery_fee")
    @Expose
    private String deliveryFee;
    @SerializedName("booking_fee")
    @Expose
    private String bookingFee;
    @SerializedName("store_commision_fee")
    @Expose
    private String restaurantCommisionFee;
    @SerializedName("driver_commision_fee")
    @Expose
    private String driverCommisionFee;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("wallet_amount")
    @Expose
    private String walletAmount;
    @SerializedName("menu_item")
    @Expose
    private ArrayList<RestaurantMenuItemModel> menuItemDetails = new ArrayList<>();
    @SerializedName("store")
    @Expose
    private CartRestaurantModel restaurantDetailsModel;
    @SerializedName("address")
    @Expose
    private AddressModel addressModel;
    @SerializedName("order_type")
    @Expose
    private int orderType;
    @SerializedName("")
    @Expose
    private String deliveryTime;
    @SerializedName("penality")
    @Expose
    private String penality;


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(String offerAmount) {
        this.offerAmount = offerAmount;
    }

    public String getPromoAmount() {
        return promoAmount;
    }

    public void setPromoAmount(String promoAmount) {
        this.promoAmount = promoAmount;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getBookingFee() {
        return bookingFee;
    }

    public void setBookingFee(String bookingFee) {
        this.bookingFee = bookingFee;
    }

    public String getRestaurantCommisionFee() {
        return restaurantCommisionFee;
    }

    public void setRestaurantCommisionFee(String restaurantCommisionFee) {
        this.restaurantCommisionFee = restaurantCommisionFee;
    }

    public String getDriverCommisionFee() {
        return driverCommisionFee;
    }

    public void setDriverCommisionFee(String driverCommisionFee) {
        this.driverCommisionFee = driverCommisionFee;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }

    public ArrayList<RestaurantMenuItemModel> getMenuItemDetails() {
        return menuItemDetails;
    }

    public void setMenuItemDetails(ArrayList<RestaurantMenuItemModel> menuItemDetails) {
        this.menuItemDetails = menuItemDetails;
    }

    public CartRestaurantModel getRestaurantDetailsModel() {
        return restaurantDetailsModel;
    }

    public void setRestaurantDetailsModel(CartRestaurantModel restaurantDetailsModel) {
        this.restaurantDetailsModel = restaurantDetailsModel;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getPenality() {
        return penality;
    }

    public void setPenality(String penality) {
        this.penality = penality;
    }
}
