package gofereats.datamodels.ratings;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.ratings
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by trioangle on 6/20/18.
 */


/*****************************************************************
 Rating Details Model Class
 ****************************************************************/


public class RatingDetailsModel {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;


    @SerializedName("user_review_data")
    @Expose
    private RatingOrderDetailsModel ratingOrderDetailsModel;

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

    public RatingOrderDetailsModel getRatingOrderDetailsModel() {
        return ratingOrderDetailsModel;
    }

    public void setRatingOrderDetailsModel(RatingOrderDetailsModel ratingOrderDetailsModel) {
        this.ratingOrderDetailsModel = ratingOrderDetailsModel;
    }
}
