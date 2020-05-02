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

import java.util.ArrayList;

import gofereats.datamodels.restaurantdetails.DietaryListModel;

/*****************************************************************
 Model Class Restaurant Model
 ****************************************************************/


public class RestaurantModel {
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("under_time")
    @Expose
    private String underTime;
    @SerializedName("under_count")
    @Expose
    private Integer underTotalPage;
    @SerializedName("new_count")
    @Expose
    private Integer newTotalPage;
    @SerializedName("fav_count")
    @Expose
    private Integer favouriteTotalPage;
    @SerializedName("popular_count")
    @Expose
    private Integer popularTotalPage;
    @SerializedName("more_count")
    @Expose
    private Integer moreTotalPage;
    @SerializedName("More Store")
    @Expose
    private ArrayList<RestaurantListModel> moreRestaurant = new ArrayList<>();
    @SerializedName("Favourite Store")
    @Expose
    private ArrayList<RestaurantListModel> favouriteRestaurant = new ArrayList<>();
    @SerializedName("Popular Store")
    @Expose
    private ArrayList<RestaurantListModel> popularRestaurant = new ArrayList<>();
    @SerializedName("New Store")
    @Expose
    private ArrayList<RestaurantListModel> newRestaurant = new ArrayList<>();
    @SerializedName("Under Store")
    @Expose
    private ArrayList<RestaurantListModel> underRestaurant = new ArrayList<>();
    @SerializedName("Store Offer")
    @Expose
    private ArrayList<RestaurantOfferModel> restaurantOffer = new ArrayList<>();
    @SerializedName("store")
    @Expose
    private ArrayList<RestaurantListModel> filter = new ArrayList<>();

    @SerializedName("category")
    @Expose
    private ArrayList<DietaryListModel> dietaryListModel = new ArrayList<>();

    @SerializedName("wallet_amount")
    @Expose
    private String walletamount;

    @SerializedName("wallet_currency")
    @Expose
    private String walletcurrency;
    @SerializedName("page_count")
    @Expose
    private Integer pageCount;

    @SerializedName("cart_details")
    @Expose
    private CartDetails cartDetails;

    @SerializedName("default_currency_symbol")
    @Expose
    private String defaultCurrencySymbol;


/*@SerializedName("cart_details")
    @Expose
    private ArrayList<CartDetails> cartDetails=new ArrayList<>();*/

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getUnderTime() {
        return underTime;
    }

    public void setUnderTime(String underTime) {
        this.underTime = underTime;
    }

    public ArrayList<RestaurantListModel> getMoreRestaurant() {
        return moreRestaurant;
    }

    public void setMoreRestaurant(ArrayList<RestaurantListModel> moreRestaurant) {
        this.moreRestaurant = moreRestaurant;
    }

    public ArrayList<RestaurantListModel> getFavouriteRestaurant() {
        return favouriteRestaurant;
    }

    public void setFavouriteRestaurant(ArrayList<RestaurantListModel> favouriteRestaurant) {
        this.favouriteRestaurant = favouriteRestaurant;
    }

    public ArrayList<RestaurantListModel> getPopularRestaurant() {
        return popularRestaurant;
    }

    public void setPopularRestaurant(ArrayList<RestaurantListModel> popularRestaurant) {
        this.popularRestaurant = popularRestaurant;
    }

    public ArrayList<RestaurantListModel> getNewRestaurant() {
        return newRestaurant;
    }

    public void setNewRestaurant(ArrayList<RestaurantListModel> newRestaurant) {
        this.newRestaurant = newRestaurant;
    }

    public ArrayList<RestaurantListModel> getUnderRestaurant() {
        return underRestaurant;
    }

    public void setUnderRestaurant(ArrayList<RestaurantListModel> underRestaurant) {
        this.underRestaurant = underRestaurant;
    }

    public ArrayList<RestaurantOfferModel> getRestaurantOffer() {
        return restaurantOffer;
    }

    public void setRestaurantOffer(ArrayList<RestaurantOfferModel> restaurantOffer) {
        this.restaurantOffer = restaurantOffer;
    }

    public ArrayList<DietaryListModel> getDietaryListModel() {
        return dietaryListModel;
    }

    public void setDietaryListModel(ArrayList<DietaryListModel> dietaryListModel) {
        this.dietaryListModel = dietaryListModel;
    }

    public String getWalletamount() {
        return walletamount;
    }

    public void setWalletamount(String walletamount) {
        this.walletamount = walletamount;
    }

    public String getWalletcurrency() {
        return walletcurrency;
    }

    public void setWalletcurrency(String walletcurrency) {
        this.walletcurrency = walletcurrency;
    }

    public ArrayList<RestaurantListModel> getFilter() {
        return filter;
    }

    public void setFilter(ArrayList<RestaurantListModel> filter) {
        this.filter = filter;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getUnderTotalPage() {
        return underTotalPage;
    }

    public void setUnderTotalPage(Integer underTotalPage) {
        this.underTotalPage = underTotalPage;
    }

    public Integer getNewTotalPage() {
        return newTotalPage;
    }

    public void setNewTotalPage(Integer newTotalPage) {
        this.newTotalPage = newTotalPage;
    }

    public Integer getFavouriteTotalPage() {
        return favouriteTotalPage;
    }

    public void setFavouriteTotalPage(Integer favouriteTotalPage) {
        this.favouriteTotalPage = favouriteTotalPage;
    }

    public Integer getPopularTotalPage() {
        return popularTotalPage;
    }

    public void setPopularTotalPage(Integer popularTotalPage) {
        this.popularTotalPage = popularTotalPage;
    }

    public Integer getMoreTotalPage() {
        return moreTotalPage;
    }

    public void setMoreTotalPage(Integer moreTotalPage) {
        this.moreTotalPage = moreTotalPage;
    }

    public CartDetails getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(CartDetails cartDetails) {
        this.cartDetails = cartDetails;
    }

    /*public ArrayList<CartDetails> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(ArrayList<CartDetails> cartDetails) {
        this.cartDetails = cartDetails;
    }*/

    public String getDefaultCurrencySymbol() {
        return defaultCurrencySymbol;
    }

    public void setDefaultCurrencySymbol(String defaultCurrencySymbol) {
        this.defaultCurrencySymbol = defaultCurrencySymbol;
    }
}
