package gofereats.interfaces;
/**
 * @package com.trioangle.gofereats
 * @subpackage interfaces
 * @category ApiService
 * @author Trioangle Product Team
 * @version 1.0
 **/

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/*****************************************************************
 ApiService
 ****************************************************************/


public interface ApiService {


    // Home page store list
    @GET("home")
    Call<ResponseBody> home(@Query("token") String token, @Query("order_type") Integer orderType);

    // Get Store details and menu items list
    @GET("get_store_details")
    Call<ResponseBody> getStoreDetails(@Query("store_id") Integer storeId, @Query("token") String token, @Query("order_type") Integer orderType);

    // Add and remove store from wish lists  / favorite
    @GET("add_wish_list")
    Call<ResponseBody> wishList(@Query("store_id") Integer storeId, @Query("token") String token);

    // Update Device ID for Push notification
    @GET("update_device_id")
    Call<ResponseBody> updateDeviceId(@Query("type") String userType, @Query("token") String token, @Query("device_type") String device_type, @Query("device_id") String device_id);

    // Add to cart
    @GET("add_to_cart")
    Call<ResponseBody> addToCart(@Query("store_id") Integer storeId, @Query("menu_item_id") Integer menuItemId, @Query("quantity") Integer quantity, @Query("notes") String specialNotes, @Query("token") String token);

    // Add to cart
    @GET("add_to_cart")
    Call<ResponseBody> addToCart(@Query("store_id") Integer storeId, @Query("menu_item_id") Integer menuItemId, @Query("order_item_id") Integer orderItemId, @Query("quantity") Integer quantity, @Query("notes") String specialNotes, @Query("token") String token);

    // Clear selected cart
    @GET("clear_cart")
    Call<ResponseBody> clearCart(@Query("order_item_id") Integer orderItemId, @Query("order_id") Integer orderId, @Query("token") String token);

    // Clear all cart
    @GET("clear_all_cart")
    Call<ResponseBody> clearAllCart(@Query("token") String token);

    // Add to cart
    @GET("add_card_details")
    Call<ResponseBody> addCard(@Query("stripe_id") String stripeId, @Query("token") String token);

    // Add to cart
    @GET("get_card_details")
    Call<ResponseBody> viewCard(@Query("token") String token);

    // Check user Mobile Number
    @GET("number_validation")
    Call<ResponseBody> numberValidation(@Query("type") String userType, @Query("mobile_number") String mobilenumber, @Query("country_code") String countrycode,@Query("language") String language);

    // Register
    @GET("register")
    Call<ResponseBody> register(@Query("type") String userType, @QueryMap HashMap<String, String> hashMap);

    //Login
    @GET("login")
    Call<ResponseBody> login(@Query("type") String userType, @QueryMap HashMap<String, String> hashMap);

    //forgot Password
    @GET("forgot_password")
    Call<ResponseBody> forgotPassword(@Query("type") String userType, @Query("mobile_number") String mobilenumber, @Query("country_code") String countrycode,@Query("language") String language);

    //Reset Password
    @GET("reset_password")
    Call<ResponseBody> resetPassword(@Query("type") String userType, @Query("mobile_number") String mobilenumber, @Query("country_code") String countrycode, @Query("password") String password,@Query("language") String language);

    //Get User Profile Details
    @GET("get_profile")
    Call<ResponseBody> getProfile(@Query("token") String token);

    //Upload Profile Image
    @POST("upload_image")
    Call<ResponseBody> uploadImage(@Body RequestBody RequestBody, @Query("token") String token, @Query("type") String type);

    //Update user Profile
    @GET("update_profile")
    Call<ResponseBody> updateProfile(@Query("token") String token, @QueryMap HashMap<String, String> hashMap);


    //Update user Profile
    @GET("get_location")
    Call<ResponseBody> getSavedLocation(@Query("token") String token);


    //Update Delivery,Home,Work
    @GET("save_location")
    Call<ResponseBody> saveLocation(@Query("token") String token, @QueryMap HashMap<String, String> hashMap);

    //Get View cart list
    @GET("view_cart")
    Call<ResponseBody> getViewCart(@Query("token") String token, @Query("isWallet") int iswallet);

    //Get place order
    @GET("place_order")
    Call<ResponseBody> placeOrder(@Query("order_id") Integer orderId, @Query("payment_method") Integer paymentMethod, @Query("isWallet") Integer isWallet, @Query("notes") String notes, @Query("order_type") Integer orderType, @Query("delivery_time") String deliveryTime, @Query("token") String token);


    //Get categories
    @GET("categories")
    Call<ResponseBody> categories(@Query("token") String token);

    //Get Results
    @GET("search")
    Call<ResponseBody> search(@Query("keyword") String keyword, @Query("token") String token);

    //Get Order List
    @GET("order_list")
    Call<ResponseBody> orderList(@Query("token") String token);

    //Set default_location
    @GET("default_location")
    Call<ResponseBody> defaultLocation(@Query("token") String token, @Query("default") Integer defaultLocation, @Query("order_type") Integer orderType, @Query("delivery_time") String deliveryTime);

    //remove_location
    @GET("remove_location")
    Call<ResponseBody> removeLocation(@Query("token") String token, @Query("type") Integer type);

    //Add PROMO CODE
    @GET("add_promo_code")
    Call<ResponseBody> addPromo(@Query("token") String token, @Query("code") String code);

    //Get Promo List
    @GET("get_promo_details")
    Call<ResponseBody> getPromoDetails(@Query("token") String token);


    // Cancel reason
    @GET("get_cancel_reason")
    Call<ResponseBody> cancelOrdersReason(@Query("type") String userType, @Query("token") String token);

    // Cancel selected order
    @GET("cancel_order")
    Call<ResponseBody> cancelOrders(@Query("cancel_reason") Integer cancelReason, @Query("cancel_message") String cancelMessage, @Query("order_id") Integer orderId, @Query("token") String token);

    // get Rating
    @GET("user_review")
    Call<ResponseBody> userRatings(@Query("order_id") Integer orderId, @Query("token") String token);

    // update Rating
    @FormUrlEncoded
    @POST("add_user_review")
    Call<ResponseBody> updateRating(@Field("order_id") Integer orderId, @Field("rating") String rating, @Query("token") String token);

    // Add money to wallet
    @GET("add_wallet_amount")
    Call<ResponseBody> addWallet(@Query("amount") String amount, @Query("token") String token);

    // Add money to wallet
    @GET("wishlist")
    Call<ResponseBody> wishlist(@Query("token") String token);

    // Change Mobile Number
    @GET("change_mobile")
    Call<ResponseBody> changeMobile(@Query("token") String token, @Query("type") String userType, @Query("mobile_number") String mobileNumber, @Query("country_code") String countrtyCode);


    // Log out User
    @GET("logout")
    Call<ResponseBody> logOut(@Query("token") String token);

    // Get filter data
    @GET("filter")
    Call<ResponseBody> filter(@Query("type") Integer type, @Query("page") Integer page, @Query("min_time") String time, @Query("token") String token);

    // Get filter sort data
    @GET("filter")
    Call<ResponseBody> filter(@Query("type") Integer type, @Query("page") Integer page, @Query("sort") Integer sort, @Query("price") String price, @Query("dietary") String dietary, @Query("token") String token);

    // Get Store info
    @GET("info_window")
    Call<ResponseBody> getStoreInfos(@Query("id") Integer storeId, @Query("token") String token);

    // Update Language
    @GET("language")
    Call<ResponseBody> updateLanguage(@Query("token") String token, @Query("language") String language,@Query("type") String type);
}
