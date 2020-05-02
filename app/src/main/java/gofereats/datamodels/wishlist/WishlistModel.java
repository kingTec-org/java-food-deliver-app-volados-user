package gofereats.datamodels.wishlist;


/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.wishlist
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by trioangle on 6/26/18.
 */


/*****************************************************************
 Wish List  Model  Class
 ****************************************************************/


public class WishlistModel {


    @SerializedName("status_message")
    @Expose
    private String statusmessage;

    @SerializedName("status_code")
    @Expose
    private String statuscode;


    @SerializedName("wishlist")
    @Expose
    private ArrayList<WishListArrayModel> wishlist;


    public String getStatusmessage() {
        return statusmessage;
    }

    public void setStatusmessage(String statusmessage) {
        this.statusmessage = statusmessage;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public ArrayList<WishListArrayModel> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<WishListArrayModel> wishlist) {
        this.wishlist = wishlist;
    }
}
