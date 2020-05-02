package gofereats.datamodels.placeorder;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.placeorder
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*****************************************************************
 Place Order Model Class
 ****************************************************************/

/**
 * Created by trioangle on 6/21/18.
 */

public class PlaceOrderModel {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("order_details")
    @Expose
    private PlaceOrderDetails placeOrderDetails;

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

    public PlaceOrderDetails getPlaceOrderDetails() {
        return placeOrderDetails;
    }

    public void setPlaceOrderDetails(PlaceOrderDetails placeOrderDetails) {
        this.placeOrderDetails = placeOrderDetails;
    }
}
