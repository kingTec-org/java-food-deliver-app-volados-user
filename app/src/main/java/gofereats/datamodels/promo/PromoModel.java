package gofereats.datamodels.promo;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.promo
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by trioangle on 6/11/18.
 */

/*****************************************************************
 Promo Model Class
 ****************************************************************/


public class PromoModel {

    @SerializedName("status_message")
    @Expose
    private String status_message;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("promo_details")
    @Expose
    private ArrayList<PromoListModel> promo_details;

    public String getStatusMessage() {
        return status_message;
    }

    public void setStatusMessage(String status_message) {
        this.status_message = status_message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<PromoListModel> getPromoDetails() {
        return promo_details;
    }

    public void setPromoDetails(ArrayList<PromoListModel> promo_details) {
        this.promo_details = promo_details;
    }
}
