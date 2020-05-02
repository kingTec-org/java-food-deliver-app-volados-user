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
 User Profile List Model Class
 ****************************************************************/


/**
 * Created by trioangle on 6/1/18.
 */

public class UserProfileList {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("mobile_number")
    @Expose
    private String mobile_number;

    @SerializedName("country_code")
    @Expose
    private String country_code;

    @SerializedName("home")
    @Expose
    private String home;

    @SerializedName("work")
    @Expose
    private String work;

    @SerializedName("eater_image")
    @Expose
    private String profileimage;

    @SerializedName("wallet_amount")
    @Expose
    private String wallet_amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobile_number;
    }

    public void setMobileNumber(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getCountryCode() {
        return country_code;
    }

    public void setCountryCode(String country_code) {
        this.country_code = country_code;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getWalletAmount() {
        return wallet_amount;
    }

    public void setWalletAmount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }
}
