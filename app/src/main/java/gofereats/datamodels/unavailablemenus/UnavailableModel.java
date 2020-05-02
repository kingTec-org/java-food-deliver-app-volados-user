package gofereats.datamodels.unavailablemenus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by trioangle on 8/14/18.
 */

public class UnavailableModel {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("unavailable")
    @Expose
    private ArrayList<UnavailableMenus> unavailable = new ArrayList();

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

    public ArrayList<UnavailableMenus> getUnavailable() {
        return unavailable;
    }

    public void setUnavailable(ArrayList<UnavailableMenus> unavailable) {
        this.unavailable = unavailable;
    }
}
