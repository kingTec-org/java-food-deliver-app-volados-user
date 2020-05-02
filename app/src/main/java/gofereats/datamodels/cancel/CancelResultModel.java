package gofereats.datamodels.cancel;
/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.cart
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by trioangle on 6/12/18.
 */

/*****************************************************************
 Cancel result Model Class
 ****************************************************************/


public class CancelResultModel {
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("cancel_reason")
    @Expose
    private ArrayList<CancelReasonModel> cancelReasonModels;

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

    public ArrayList<CancelReasonModel> getCancelReasonModels() {
        return cancelReasonModels;
    }

    public void setCancelReasonModels(ArrayList<CancelReasonModel> cancelReasonModels) {
        this.cancelReasonModels = cancelReasonModels;
    }
}
