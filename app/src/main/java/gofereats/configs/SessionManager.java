package gofereats.configs;
/**
 * @package com.trioangle.gofereats
 * @subpackage configs
 * @category SessionManager
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.SharedPreferences;
import android.text.Html;

import javax.inject.Inject;

/*****************************************************************
 Session manager to set and get glopal values
 ***************************************************************/
public class SessionManager {
    public @Inject
    SharedPreferences sharedPreferences;

    public SessionManager() {
        AppController.getAppComponent().inject(this);
    }

    public String getType() {
        return sharedPreferences.getString("type", "");
    }

    public void setType(String type) {
        sharedPreferences.edit().putString("type", type).apply();
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void setToken(String token) {
        sharedPreferences.edit().putString("token", token).apply();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString("phoneNumber", "");
    }

    public void setPhoneNumber(String phoneNumber) {
        sharedPreferences.edit().putString("phoneNumber", phoneNumber).apply();
    }

    public String getFirstName() {
        return sharedPreferences.getString("firstname", "");
    }

    public void setFirstName(String firstName) {
        sharedPreferences.edit().putString("firstname", firstName).apply();
    }

    public String getLastName() {
        return sharedPreferences.getString("lastname", "");
    }

    public void setLastName(String lastName) {
        sharedPreferences.edit().putString("lastname", lastName).apply();
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString("email", email).apply();
    }

    public String getemail() {
        return sharedPreferences.getString("email", "");
    }

    public String getPassword() {
        return sharedPreferences.getString("Password", "");
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString("password", password).apply();
    }

    public String getCountryCode() {
        return sharedPreferences.getString("countryCode", "");
    }

    public void setCountryCode(String countryCode) {
        sharedPreferences.edit().putString("countryCode", countryCode).apply();
    }

    public String getScheduleDate() {
        return sharedPreferences.getString("scheduleDate", "");
    }

    public void setScheduleDate(String scheduleDate) {
        sharedPreferences.edit().putString("scheduleDate", scheduleDate).apply();
    }

    public void setScheduleMin(String scheduleMin) {
        sharedPreferences.edit().putString("scheduleMin", scheduleMin).apply();
    }

    public String getscheduleMin() {
        return sharedPreferences.getString("scheduleMin", "");
    }

    public String getDeviceId() {
        return sharedPreferences.getString("deviceId", "");
    }

    public void setDeviceId(String deviceId) {
        sharedPreferences.edit().putString("deviceId", deviceId).apply();
    }

    public String getPushNotification() {
        return sharedPreferences.getString("pushNotification", "");
    }

    public void setPushNotification(String pushNotification) {
        sharedPreferences.edit().putString("pushNotification", pushNotification).apply();
    }

    public String getImage() {
        return sharedPreferences.getString("image", "");
    }

    public void setImage(String image) {
        sharedPreferences.edit().putString("image", image).apply();
    }

    public String getName() {
        return sharedPreferences.getString("name", "");
    }

    public void setName(String name) {
        sharedPreferences.edit().putString("name", name).apply();
    }

    public String getLocation() {
        return sharedPreferences.getString("location", "");
    }

    public void setLocation(String location) {
        sharedPreferences.edit().putString("location", location).apply();
    }

    public boolean getCheckPrice() {
        return sharedPreferences.getBoolean("checkprice", true);
    }

    public void setCheckPrice(Boolean checkPrice) {
        sharedPreferences.edit().putBoolean("checkprice", checkPrice).apply();
    }

    public boolean getCheckDiet() {
        return sharedPreferences.getBoolean("checkdiet", true);
    }

    public void setCheckDiet(Boolean checkDiet) {
        sharedPreferences.edit().putBoolean("checkdiet", checkDiet).apply();
    }

    public void clearToken() {
        sharedPreferences.edit().putString("token", "").apply();
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
        setType("0");
    }

    public int getUserId() {
        return sharedPreferences.getInt("userId", 0);
    }

    public void setUserId(int userId) {
        sharedPreferences.edit().putInt("userId", userId).apply();
    }

    public String getCurrencyCode() {
        return sharedPreferences.getString("currencyCode", "");
    }

    public void setCurrencyCode(String currencyCode) {
        sharedPreferences.edit().putString("currencyCode", currencyCode).apply();
    }

    public String getCurrencySymbol() {
        return sharedPreferences.getString("currencysymbol", "");
    }

    public void setCurrencySymbol(String currencySymbol) {
        sharedPreferences.edit().putString("currencysymbol", currencySymbol).apply();
    }

    public Integer getRestaurantId() {
        return sharedPreferences.getInt("restaurantId", 0);
    }

    public void setRestaurantId(Integer restaurantId) {
        sharedPreferences.edit().putInt("restaurantId", restaurantId).apply();
    }

    public Integer getCartCount() {
        return sharedPreferences.getInt("cartCount", 0);
    }

    public void setCartCount(Integer cartCount) {
        sharedPreferences.edit().putInt("cartCount", cartCount).apply();
    }

    public String getCartAmount() {
        return sharedPreferences.getString("cartAmount", "");
    }

    public void setCartAmount(String cartAmount) {
        sharedPreferences.edit().putString("cartAmount", cartAmount).apply();
    }


    public String getLocationList() {
        return sharedPreferences.getString("locationlist", "");
    }

    public void setLocationList(String LocationList) {
        sharedPreferences.edit().putString("locationlist", LocationList).apply();
    }

    // 0 for cash  1 for stripe
    public Integer getPaymentMethod() {
        return sharedPreferences.getInt("paymentMethod", 0);
    }

    public void setPaymentMethod(Integer paymentMethod) {
        sharedPreferences.edit().putInt("paymentMethod", paymentMethod).apply();
    }

    public Boolean getIsWallet() {
        return sharedPreferences.getBoolean("isWallet", false);
    }

    public void setIsWallet(boolean isWallet) {
        sharedPreferences.edit().putBoolean("isWallet", isWallet).apply();
    }

    public String getCardValue() {
        return sharedPreferences.getString("cardValue", "");
    }

    public void setCardValue(String cardValue) {
        sharedPreferences.edit().putString("cardValue", cardValue).apply();
    }

    public String getCardBrand() {
        return sharedPreferences.getString("cardBrand", "");
    }

    public void setCardBrand(String cardBrand) {
        sharedPreferences.edit().putString("cardBrand", cardBrand).apply();
    }

    public int getSort() {
        return sharedPreferences.getInt("sort", 0);
    }

    public void setSort(int sort) {
        sharedPreferences.edit().putInt("sort", sort).apply();
    }

    public String getPriceSort() {
        return sharedPreferences.getString("priceSort", "");
    }

    public void setPriceSort(String priceSort) {
        sharedPreferences.edit().putString("priceSort", priceSort).apply();
    }

    public String getDietarySort() {
        return sharedPreferences.getString("dietarySort", "");
    }

    public void setDietarySort(String dietarySort) {
        sharedPreferences.edit().putString("dietarySort", dietarySort).apply();
    }

    public String getDietaryName() {
        return sharedPreferences.getString("dietaryName", "");
    }

    public void setDietaryName(String dietaryName) {
        sharedPreferences.edit().putString("dietaryName", dietaryName).apply();
    }

    public String getDietaryList() {
        return sharedPreferences.getString("dietaryList", "");
    }

    public void setDietaryList(String dietaryList) {
        sharedPreferences.edit().putString("dietaryList", dietaryList).apply();
    }

    public void setWalletAmount(String walletAmount) {
        sharedPreferences.edit().putString("walletAmount", walletAmount).apply();
    }

    public String getwalletAmount() {
        return sharedPreferences.getString("walletAmount", "");
    }

    public String getSearchCuisine() {
        return sharedPreferences.getString("searchCuisine", "");
    }

    public void setSearchCuisine(String searchCuisine) {
        sharedPreferences.edit().putString("searchCuisine", searchCuisine).apply();
    }

    public String getCuisineName() {
        return sharedPreferences.getString("cuisineName", "");
    }

    public void setCuisineName(String cuisineName) {
        sharedPreferences.edit().putString("cuisineName", cuisineName).apply();
    }

    // Money Add to wallet by Card
    public Integer getWalletCard() {
        return sharedPreferences.getInt("walletCard", 0);
    }

    public void setWalletCard(Integer walletCard) {
        sharedPreferences.edit().putInt("walletCard", walletCard).apply();
    }

    public String getAddedTime() {
        return sharedPreferences.getString("addedTime", "");
    }

    public void setAddedTime(String addedTime) {
        sharedPreferences.edit().putString("addedTime", addedTime).apply();
    }

    //Order Type
    // 0 -Normal order
    // 1 - Schedule order
    public Integer getOrderType() {
        return sharedPreferences.getInt("orderType", 0);
    }

    public void setOrderType(Integer orderType) {
        sharedPreferences.edit().putInt("orderType", orderType).apply();
    }

    public String getScheduledDay() {
        return sharedPreferences.getString("scheduledDay", "");
    }

    public void setScheduledDay(String scheduledDay) {
        sharedPreferences.edit().putString("scheduledDay", scheduledDay).apply();
    }

    public boolean getIsFirst() {
        return sharedPreferences.getBoolean("isFirst", true);
    }

    public void setIsFirst(Boolean isFirst) {
        sharedPreferences.edit().putBoolean("isFirst", isFirst).apply();
    }

    public String getDeliveredTime() {
        return sharedPreferences.getString("deliveredTime", "");
    }

    public void setDeliveredTime(String deliveredTime) {
        sharedPreferences.edit().putString("deliveredTime", deliveredTime).apply();
    }

    public String getHomeScheduledDay() {
        return sharedPreferences.getString("homeScheduledDay", "");
    }

    public void setHomeScheduledDay(String homeScheduledDay) {
        sharedPreferences.edit().putString("homeScheduledDay", homeScheduledDay).apply();
    }


    public String getDriverUpdatedLat() {
        return sharedPreferences.getString("driverUpdatedLat", "");
    }

    public void setDriverUpdatedLat(String driverUpdatedLat) {
        sharedPreferences.edit().putString("driverUpdatedLat", driverUpdatedLat).apply();
    }

    public String getDriverUpdatedLng() {
        return sharedPreferences.getString("driverUpdatedLng", "");
    }

    public void setDriverUpdatedLng(String driverUpdatedLng) {
        sharedPreferences.edit().putString("driverUpdatedLng", driverUpdatedLng).apply();
    }

    public String getAppLanguage() {
        return sharedPreferences.getString("AppLanguage", "English");
    }

    public void setAppLanguage(String AppLanguage) {
        sharedPreferences.edit().putString("AppLanguage", AppLanguage).apply();
    }

    public String getAppLanguageCode() {
        return sharedPreferences.getString("AppLanguageCode", "en");
    }

    public void setAppLanguageCode(String AppLanguageCode) {
        sharedPreferences.edit().putString("AppLanguageCode", AppLanguageCode).apply();
    }
}



