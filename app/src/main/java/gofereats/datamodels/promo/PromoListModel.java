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

/*****************************************************************
 Promo List Model Class
 ****************************************************************/


/**
 * Created by trioangle on 6/11/18.
 */

public class PromoListModel {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("expire_date")
    @Expose
    private String expiredate;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("percentage")
    @Expose
    private int percentage;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
