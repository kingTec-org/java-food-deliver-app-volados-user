package gofereats.datamodels.profile;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.profile
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*****************************************************************
 User Profile Model Class
 ****************************************************************/

/**
 * Created by trioangle on 6/1/18.
 */

public class UserProfileModel {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("user_details")
    @Expose
    private UserProfileList userProfileList;


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

    public UserProfileList getUserProfileList() {
        return userProfileList;
    }

    public void setUserProfileList(UserProfileList userProfileList) {
        this.userProfileList = userProfileList;
    }
}
