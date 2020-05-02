package gofereats.datamodels.cart;

/**
 * @package com.trioangle.gofereats
 * @subpackage datamodels.cart
 * @category Model Class
 * @author Trioangle Product Team
 * @version 1.0
 **/


/*****************************************************************
 Address Model Class
 ****************************************************************/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressModel {
    @SerializedName("id")
    @Expose
    private Integer addressId;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("default")
    @Expose
    private String isDefault;
    @SerializedName("delivery_options")
    @Expose
    private Integer deliveryOptions;
    @SerializedName("apartment")
    @Expose
    private String apartment;
    @SerializedName("delivery_note")
    @Expose
    private String deliveryNote;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("static_map")
    @Expose
    private String staticMap;
    @SerializedName("first_address")
    @Expose
    private String firstAddress;
    @SerializedName("second_address")
    @Expose
    private String secondAddress;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Integer getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(Integer deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStaticMap() {
        return staticMap;
    }

    public void setStaticMap(String staticMap) {
        this.staticMap = staticMap;
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
