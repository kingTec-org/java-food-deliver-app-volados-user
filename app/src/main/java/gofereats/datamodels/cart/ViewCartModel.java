package gofereats.datamodels.cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import gofereats.datamodels.promo.PromoListModel;


/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.cart
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/

/*****************************************************************
 View Cart Model Class
 ****************************************************************/


public class ViewCartModel {
    @SerializedName("status_message")
    @Expose
    private String statusmessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("cart_details")
    @Expose
    private CartDetailModel cartDetails;// = new ArrayList<>();promo_details

    @SerializedName("promo_details")
    @Expose
    private ArrayList<PromoListModel> promo_details = new ArrayList<>();

    public String getStatusMessage() {
        return statusmessage;
    }

    public void setStatusMessage(String statusmessage) {
        this.statusmessage = statusmessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public CartDetailModel getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(CartDetailModel cartDetails) {
        this.cartDetails = cartDetails;
    }

    public ArrayList<PromoListModel> getPromoDetails() {
        return promo_details;
    }

    public void setPromoDetails(ArrayList<PromoListModel> promo_details) {
        this.promo_details = promo_details;
    }
}
