package gofereats.datamodels.searchcategory;


/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.searchcategory
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by trioangle on 6/8/18.
 */

/*****************************************************************
 Category  Model  Class
 ****************************************************************/


public class CategoryModel {

    @SerializedName("status_message")
    @Expose
    private String statusmessage;

    @SerializedName("status_code")
    @Expose
    private String statuscode;


    @SerializedName("top_category")
    @Expose
    private ArrayList<CategoryListModel> topcategory;

    @SerializedName("category")
    @Expose
    private ArrayList<CategoryListModel> category;


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

    public ArrayList<CategoryListModel> getTopcategory() {
        return topcategory;
    }

    public void setTopcategory(ArrayList<CategoryListModel> topcategory) {
        this.topcategory = topcategory;
    }

    public ArrayList<CategoryListModel> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<CategoryListModel> category) {
        this.category = category;
    }
}
