package gofereats.datamodels.location;


/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.location
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/*****************************************************************
 Model Class Location List
 ****************************************************************/


/**
 * Created by trioangle on 6/6/18.
 */

public class LocationList {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("delivery_options")
    @Expose
    private int deliveryoptions;

    @SerializedName("apartment")
    @Expose
    private String apartment;


    @SerializedName("default")
    @Expose
    private int defaultaddress;

    @SerializedName("delivery_note")
    @Expose
    private String deliverynote;

    @SerializedName("latitude")
    @Expose
    private Double latitude;

    @SerializedName("longitude")
    @Expose
    private Double longitude;

    @SerializedName("street")
    @Expose
    private String street;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("first_address")
    @Expose
    private String firstAddress;

    @SerializedName("second_address")
    @Expose
    private String secondAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDeliveryoptions() {
        return deliveryoptions;
    }

    public void setDeliveryoptions(int deliveryoptions) {
        this.deliveryoptions = deliveryoptions;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getDeliverynote() {
        return deliverynote;
    }

    public void setDeliverynote(String deliverynote) {
        this.deliverynote = deliverynote;
    }

    public int getDefaultaddress() {
        return defaultaddress;
    }

    public void setDefaultaddress(int defaultaddress) {
        this.defaultaddress = defaultaddress;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstAddress() {
        return firstAddress;
    }

    public void setFirstAddress(String firstAddress) {
        this.firstAddress = firstAddress;
    }

    public String getSecondAddress() {
        return secondAddress;
    }

    public void setSecondAddress(String secondAddress) {
        this.secondAddress = secondAddress;
    }
}
