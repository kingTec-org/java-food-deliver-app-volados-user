package gofereats.datamodels.placeorder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by trioangle on 8/17/18.
 */

public class PlaceOrderDetails {

    @SerializedName("wallet_amount")
    @Expose
    private String walletAmount;


    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }
}
