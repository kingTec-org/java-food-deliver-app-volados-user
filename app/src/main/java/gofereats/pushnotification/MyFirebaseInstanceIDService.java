package gofereats.pushnotification;
/**
 * @package com.gofereats
 * @subpackage MyFirebaseInstanceIDService
 * @category Firebase instance service
 * @author Trioangle Product Team
 * @version 1.5
 */

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import javax.inject.Inject;

import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;

import static gofereats.utils.Enums.REQ_UPDATE_DEVICE_ID;

/* ************************************************************
   Firebase instance service to get device ID
   *************************************************************** */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements ServiceListener {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;

    //private AlertDialog dialog;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        AppController.getAppComponent().inject(this);
        //dialog = commonMethods.getAlertDialog(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);


        /*// Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/
    }

    private void sendRegistrationToServer(final String token) {
        // sending FCM token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        sessionManager.setDeviceId(token);

        if (!sessionManager.getToken().equals("")) {
            /************************************************
             *  Type 0 for Eater 1 for restaurant 2 for Driver
             *  Device type 2 for android and 1 for iOS
             ************************************************/
            apiService.updateDeviceId(sessionManager.getType(), sessionManager.getToken(), "2", sessionManager.getDeviceId()).enqueue(new RequestCallback(REQ_UPDATE_DEVICE_ID, this));
        }
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        //commonMethods.hideProgressDialog();
       /* if (!jsonResp.isOnline()) {
            if(!TextUtils.isEmpty(data))
            // commonMethods.showMessage(this, dialog, data);
            return;
        }*/
        switch (jsonResp.getRequestCode()) {
            case REQ_UPDATE_DEVICE_ID:
                /*if (jsonResp.isSuccess()) {
                    // onSuccessGetMyHome(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    // commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }*/
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        System.out.println(data);
        //commonMethods.hideProgressDialog();
        //if (!jsonResp.isOnline()) commonMethods.showMessage(this, dialog, data);
    }
}


