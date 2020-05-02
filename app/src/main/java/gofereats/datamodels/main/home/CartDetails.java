package gofereats.datamodels.main.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 7/27/18.
 */

public class CartDetails implements Serializable {

    @SerializedName("image")
    @Expose
    private BannerImageList bannerList;

    @SerializedName("name")
    @Expose
    private String name;

    public BannerImageList getBannerList() {
        return bannerList;
    }

    public void setBannerList(BannerImageList bannerList) {
        this.bannerList = bannerList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
