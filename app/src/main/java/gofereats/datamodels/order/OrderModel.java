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

import java.util.ArrayList;

import gofereats.datamodels.main.home.BannerImageList;

/*****************************************************************
 Order  Model Class
 ****************************************************************/


/**
 * Created by trioangle on 6/8/18.
 */

public class OrderModel {

    @SerializedName("order_id")
    @Expose
    private int orderid;
    @SerializedName("total_amount")
    @Expose
    private String totalamount;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("name")
    @Expose
    private String name;
    /*@SerializedName("restaurant_banner")
    @Expose*/
    private String restaurantbanner;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("delivery_fee")
    @Expose
    private String deliveryfee;
    @SerializedName("store_open_time")
    @Expose
    private String restaurantopentime;
    @SerializedName("driver_name")
    @Expose
    private String drivername;
    @SerializedName("store_id")
    @Expose
    private int restaurantid;
    @SerializedName("menu")
    @Expose
    private ArrayList<MenuListModel> menu;
    @SerializedName("total_seconds")
    @Expose
    private int totalseconds;
    @SerializedName("remaining_seconds")
    @Expose
    private int remainingseconds;
    @SerializedName("user_status_text")
    @Expose
    private String userstatustext;
    @SerializedName("est_complete_time")
    @Expose
    private String estcompletetime;
    @SerializedName("pickup_latitude")
    @Expose
    private String pickupLatitude;
    @SerializedName("pickup_longitude")
    @Expose
    private String pickupLongitude;
    @SerializedName("drop_longitude")
    @Expose
    private String dropLongitude;
    @SerializedName("drop_latitude")
    @Expose
    private String dropLatitude;
    @SerializedName("driver_latitude")
    @Expose
    private String driverLatitude;
    @SerializedName("driver_longitude")
    @Expose
    private String driverLongitude;
    @SerializedName("wallet_amount")
    @Expose
    private String walletamount;
    @SerializedName("promo_amount")
    @Expose
    private String promoAmount;
    @SerializedName("driver_image")
    @Expose
    private String driverimage;
    @SerializedName("vehicle_number")
    @Expose
    private String drivervechilenumber;
    @SerializedName("vehicle_type")
    @Expose
    private String drivervechiletype;
    @SerializedName("driver_contact")
    @Expose
    private String drivercontactnumber;
    @SerializedName("driver_rating")
    @Expose
    private String drivertating;
    @SerializedName("delivery_options")
    @Expose
    private String deliveryoption;
    @SerializedName("delivery_note")
    @Expose
    private String deliverynotes;
    @SerializedName("apartment")
    @Expose
    private String apartment;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("store_longitude")
    @Expose
    private String restaurantLongitude;
    @SerializedName("store_latitude")
    @Expose
    private String restaurantLatitude;
    @SerializedName("booking_fee")
    @Expose
    private String bookingFee;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;
    /*@SerializedName("order_delivery_status")
    @Expose
    private int order_delivery_status;*/
    @SerializedName("is_rating")
    @Expose
    private int isRating;
    @SerializedName("star_rating")
    @Expose
    private String starRating;
    @SerializedName("food_status")
    @Expose
    private ArrayList<FoodStatusModel> foodStatus;
    @SerializedName("store_status")
    @Expose
    private int restaurant_status;
    @SerializedName("store_closed")
    @Expose
    private int restaurant_closed;

    @SerializedName("store_banner")
    @Expose
    private BannerImageList restaurantBanner;

    @SerializedName("store_next_time")
    @Expose
    private String restaurantNextTime;

    @SerializedName("order_type")
    @Expose
    private int orderType;

    @SerializedName("order_delivery_status")
    @Expose
    private int orderDeliveryStatus;

    @SerializedName("penality")
    @Expose
    private String penality;

    @SerializedName("applied_penality")
    @Expose
    private String appliedPenality;

    @SerializedName("notes")
    @Expose
    private String notes;

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestaurantbanner() {
        return restaurantbanner;
    }

    public void setRestaurantbanner(String restaurantbanner) {
        this.restaurantbanner = restaurantbanner;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeliveryfee() {
        return deliveryfee;
    }

    public void setDeliveryfee(String deliveryfee) {
        this.deliveryfee = deliveryfee;
    }

    public String getRestaurantopentime() {
        return restaurantopentime;
    }

    public void setRestaurantopentime(String restaurantopentime) {
        this.restaurantopentime = restaurantopentime;
    }

    public int getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(int restaurantid) {
        this.restaurantid = restaurantid;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public ArrayList<MenuListModel> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<MenuListModel> menu) {
        this.menu = menu;
    }

    public int getTotalseconds() {
        return totalseconds;
    }

    public void setTotalseconds(int totalseconds) {
        this.totalseconds = totalseconds;
    }

    public int getRemainingseconds() {
        return remainingseconds;
    }

    public void setRemainingseconds(int remainingseconds) {
        this.remainingseconds = remainingseconds;
    }

    public String getUserstatustext() {
        return userstatustext;
    }

    public void setUserstatustext(String userstatustext) {
        this.userstatustext = userstatustext;
    }

    public String getEstcompletetime() {
        return estcompletetime;
    }

    public void setEstcompletetime(String estcompletetime) {
        this.estcompletetime = estcompletetime;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getDropLongitude() {
        return dropLongitude;
    }

    public void setDropLongitude(String dropLongitude) {
        this.dropLongitude = dropLongitude;
    }

    public String getDropLatitude() {
        return dropLatitude;
    }

    public void setDropLatitude(String dropLatitude) {
        this.dropLatitude = dropLatitude;
    }

    public String getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(String driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public String getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(String driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    public String getWalletamount() {
        return walletamount;
    }

    public void setWalletamount(String walletamount) {
        this.walletamount = walletamount;
    }

    public String getDriverimage() {
        return driverimage;
    }

    public void setDriverimage(String driverimage) {
        this.driverimage = driverimage;
    }

    public String getDrivervechilenumber() {
        return drivervechilenumber;
    }

    public void setDrivervechilenumber(String drivervechilenumber) {
        this.drivervechilenumber = drivervechilenumber;
    }

    public String getDrivervechiletype() {
        return drivervechiletype;
    }

    public void setDrivervechiletype(String drivervechiletype) {
        this.drivervechiletype = drivervechiletype;
    }

    public String getDrivercontactnumber() {
        return drivercontactnumber;
    }

    public void setDrivercontactnumber(String drivercontactnumber) {
        this.drivercontactnumber = drivercontactnumber;
    }

    public String getDrivertating() {
        return drivertating;
    }

    public void setDrivertating(String drivertating) {
        this.drivertating = drivertating;
    }

    public String getDeliveryoption() {
        return deliveryoption;
    }

    public void setDeliveryoption(String deliveryoption) {
        this.deliveryoption = deliveryoption;
    }

    public String getDeliverynotes() {
        return deliverynotes;
    }

    public void setDeliverynotes(String deliverynotes) {
        this.deliverynotes = deliverynotes;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }


    public String getRestaurantLongitude() {
        return restaurantLongitude;
    }

    public void setRestaurantLongitude(String restaurantLongitude) {
        this.restaurantLongitude = restaurantLongitude;
    }

    public String getRestaurantLatitude() {
        return restaurantLatitude;
    }

    public void setRestaurantLatitude(String restaurantLatitude) {
        this.restaurantLatitude = restaurantLatitude;
    }

    public String getPromoAmount() {
        return promoAmount;
    }

    public void setPromoAmount(String promoAmount) {
        this.promoAmount = promoAmount;
    }

    public String getBookingFee() {
        return bookingFee;
    }

    public void setBookingFee(String bookingFee) {
        this.bookingFee = bookingFee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getIsRating() {
        return isRating;
    }

    public void setIsRating(int isRating) {
        this.isRating = isRating;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public ArrayList<FoodStatusModel> getFoodStatus() {
        return foodStatus;
    }

    public void setFoodStatus(ArrayList<FoodStatusModel> foodStatus) {
        this.foodStatus = foodStatus;
    }

    public int getRestaurantStatus() {
        return restaurant_status;
    }

    public void setRestaurantStatus(int restaurant_status) {
        this.restaurant_status = restaurant_status;
    }

    public int getRestaurantClosed() {
        return restaurant_closed;
    }

    public void setRestaurantClosed(int restaurant_closed) {
        this.restaurant_closed = restaurant_closed;
    }

    public BannerImageList getRestaurantBanner() {
        return restaurantBanner;
    }

    public void setRestaurantBanner(BannerImageList restaurantBanner) {
        this.restaurantBanner = restaurantBanner;
    }

    public String getRestaurantNextTime() {
        return restaurantNextTime;
    }

    public void setRestaurantNextTime(String restaurantNextTime) {
        this.restaurantNextTime = restaurantNextTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderDeliveryStatus() {
        return orderDeliveryStatus;
    }

    public void setOrderDeliveryStatus(int orderDeliveryStatus) {
        this.orderDeliveryStatus = orderDeliveryStatus;
    }

    public String getPenality() {
        return penality;
    }

    public void setPenality(String penality) {
        this.penality = penality;
    }

    public String getAppliedPenality() {
        return appliedPenality;
    }

    public void setAppliedPenality(String appliedPenality) {
        this.appliedPenality = appliedPenality;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
