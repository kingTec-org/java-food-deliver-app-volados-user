package gofereats.utils;
/**
 * @package com.trioangle.gofereats
 * @subpackage utils
 * @category Enums
 * @author Trioangle Product Team
 * @version 1.0
 **/

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*****************************************************************
 Enums
 ****************************************************************/

public class Enums {

    public static final int REQ_RESEND_OTP = 100;
    public static final int REQ_SIGNUP = 101;
    public static final int REQ_RESET_PASSWORD = 102;
    public static final int REQ_NUMBER_SIGNUP = 103;
    public static final int REQ_GET_EDIT_PROFILE = 104;
    public static final int REQ_UPLOAD_PROFILE_IMG = 105;
    public static final int REQ_UPDATE_PROFILE = 106;
    public static final int REQ_GET_SAVED_LOCATION = 107;
    public static final int REQ_UPDATE_LOCATION = 108;
    public static final int REQ_GET_CATEGORY = 109;
    public static final int REQ_GET_HOME = 110;
    public static final int REQ_WISH_LIST = 111;
    public static final int REQ_RESTAURANT_DETAILS = 112;
    public static final int REQ_RATING = 113;
    public static final int REQ_RATING_UPDATE = 114;
    public static final int REQ_UPDATE_DEVICE_ID = 120;
    public static final int REQ_VIEW_CART = 121;
    public static final int REQ_PLACE_ORDER = 122;
    public static final int REQ_CLEAR_CART = 123;
    public static final int REQ_CLEAR_ALL_CART = 124;
    public static final int REQ_ADD_CART = 125;
    public static final int REQ_ADD_CARD = 126;
    public static final int REQ_VIEW_PAYMENT = 127;
    public static final int REQ_GET_CATEGORY_RESULT = 128;
    public static final int REQ_GET_ORDERSLIST = 129;
    public static final int REQ_SET_DEFAULT = 130;
    public static final int REQ_REMOVE_LOCATION = 131;
    public static final int REQ_ADD_PROMO = 132;
    public static final int REQ_GET_PROMO = 133;
    public static final int REQ_GET_CANCEL_REASONS = 134;
    public static final int REQ_CANCEL_ORDER = 135;
    public static final int REQ_LOGOUT = 136;
    public static final int REQ_FILTER = 137;
    public static final int REQ_EDIT_MOBILE = 138;
    public static final int REQ_FORGOT_PASSWORD = 139;
    public static final int REQ_CHANGE_NUMBER = 140;
    public static final int REQ_CLEAR_AND_CHANGE_LOCATION = 141;
    public static final int REQ_CLEAR_MENU = 142;
    public static final int REQ_UPDATE_LANGUAGE = 143;


    public static final String MATCH_LIKE = "like";
    public static final String MATCH_SUPER_LIKE = "super like";
    public static final String MATCH_NOPE = "nope";
    public static final String MATCH_RELOAD = "reload";

    @IntDef({REQ_RESEND_OTP, REQ_SIGNUP, REQ_RESET_PASSWORD, REQ_NUMBER_SIGNUP, REQ_GET_EDIT_PROFILE, REQ_UPLOAD_PROFILE_IMG, REQ_UPDATE_PROFILE, REQ_LOGOUT, REQ_GET_HOME, REQ_WISH_LIST, REQ_RESTAURANT_DETAILS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RequestType {
    }

    @StringDef({MATCH_LIKE, MATCH_SUPER_LIKE, MATCH_NOPE, MATCH_RELOAD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MatchType {
    }
}
