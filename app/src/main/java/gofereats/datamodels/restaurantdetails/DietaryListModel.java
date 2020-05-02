package gofereats.datamodels.restaurantdetails;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.restaurantDetails
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*****************************************************************
 Food list Model Class
 ****************************************************************/

public class DietaryListModel implements Serializable {


    @SerializedName("id")
    @Expose
    private Integer dietaryId;
    @SerializedName("name")
    @Expose
    private String dietaryName;
    @SerializedName("dietary_icon")
    @Expose
    private String dietaryImage;
    @SerializedName("dietary_selected")
    @Expose
    private boolean isDietarySelected;

    public Integer getDietaryId() {
        return dietaryId;
    }

    public void setDietaryId(Integer dietaryId) {
        this.dietaryId = dietaryId;
    }

    public String getDietaryName() {
        return dietaryName;
    }

    public void setDietaryName(String dietaryName) {
        this.dietaryName = dietaryName;
    }

    public String getDietaryImage() {
        return dietaryImage;
    }

    public void setDietaryImage(String dietaryImage) {
        this.dietaryImage = dietaryImage;
    }

    public boolean isDietarySelected() {
        return isDietarySelected;
    }

    public void setDietarySelected(boolean dietarySelected) {
        isDietarySelected = dietarySelected;
    }
}