package gofereats.datamodels.main.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 7/20/18.
 */

public class BannerImageList implements Serializable {
    @SerializedName("small")
    @Expose
    private String smallImage;

    @SerializedName("medium")
    @Expose
    private String mediumImage;

    @SerializedName("large")
    @Expose
    private String largeImage;

    @SerializedName("medium_x")
    @Expose
    private String mediumX;

    @SerializedName("original")
    @Expose
    private String original;

    @SerializedName("smallest")
    @Expose
    private String smallest;

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getMediumImage() {
        return mediumImage;
    }

    public void setMediumImage(String mediumImage) {
        this.mediumImage = mediumImage;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getMediumX() {
        return mediumX;
    }

    public void setMediumX(String mediumX) {
        this.mediumX = mediumX;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getSmallest() {
        return smallest;
    }

    public void setSmallest(String smallest) {
        this.smallest = smallest;
    }
}
