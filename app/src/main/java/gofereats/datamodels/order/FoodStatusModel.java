package gofereats.datamodels.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by trioangle on 7/10/18.
 */

public class FoodStatusModel {

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("status")
    @Expose
    private String status;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
