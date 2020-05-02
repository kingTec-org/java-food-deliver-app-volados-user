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

/**
 * Created by trioangle on 6/12/18.
 */

/*****************************************************************
 Cancel Reason Model Class
 ****************************************************************/


public class CancelReasonModel {

    @SerializedName("id")
    @Expose
    private Integer cancelId;
    @SerializedName("reason")
    @Expose
    private String reason;

    public Integer getCancelId() {
        return cancelId;
    }

    public void setCancelId(Integer cancelId) {
        this.cancelId = cancelId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
