package gofereats.utils;
/**
 * @package com.trioangle.gofereats
 * @subpackage utils
 * @category RequestCallback
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONObject;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.Constants;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.views.main.subviews.EntryActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*****************************************************************
 RequestCallback
 ****************************************************************/
public class RequestCallback implements Callback<ResponseBody> {

    public @Inject
    JsonResponse jsonResp;
    public @Inject
    Context context;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    ApiService apiService;
    private ServiceListener listener;
    private int requestCode = 0;
    private String requestData = "";

    public RequestCallback() {
        AppController.getAppComponent().inject(this);
    }

    public RequestCallback(ServiceListener listener) {
        AppController.getAppComponent().inject(this);
        this.listener = listener;
    }

    public RequestCallback(int requestCode, ServiceListener listener) {
        AppController.getAppComponent().inject(this);
        this.listener = listener;
        this.requestCode = requestCode;
    }

    public RequestCallback(int requestCode, ServiceListener listener, String requestData) {
        AppController.getAppComponent().inject(this);
        this.listener = listener;
        this.requestCode = requestCode;
        this.requestData = requestData;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        this.listener.onSuccess(getSuccessResponse(call, response), requestData);
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        this.listener.onFailure(getFailureResponse(call, t), requestData);
    }

    private JsonResponse getFailureResponse(Call<ResponseBody> call, Throwable t) {
        try {
            jsonResp.clearAll();
            if (call != null && call.request() != null) {
                jsonResp.setMethod(call.request().method());
                jsonResp.setRequestCode(requestCode);
                jsonResp.setUrl(call.request().url().toString());
                LogManager.i(call.request().toString());
            }
            jsonResp.setOnline(isOnline());
            jsonResp.setErrorMsg(t.getMessage());
            jsonResp.setSuccess(false);
            jsonResp.setStatusMsg(context.getResources().getString(R.string.internal_server_error));
            requestData = (context != null && !isOnline()) ? context.getResources().getString(R.string.network_failure) : t.getMessage();
            LogManager.e(t.getMessage());
            LogManager.e(String.valueOf(requestCode));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResp;
    }


    private JsonResponse getSuccessResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            jsonResp.clearAll();
            if (call != null && call.request() != null) {
                jsonResp.setMethod(call.request().method());
                jsonResp.setRequestCode(requestCode);
                jsonResp.setUrl(call.request().url().toString());
                LogManager.i(call.request().toString());
            }
            if (response != null) {
                jsonResp.setResponseCode(response.code());
                jsonResp.setSuccess(false);
                jsonResp.setStatusMsg(context.getResources().getString(R.string.internal_server_error));
                if (response.isSuccessful() && response.body() != null) {
                    String strJson = response.body().string();
                    jsonResp.setStrResponse(strJson);
                    jsonResp.setStatusMsg(getStatusMsg(strJson));
                    if (jsonResp.getStatusMsg().equalsIgnoreCase("Token Expired")) {
                        jsonResp.setStatusMsg(context.getResources().getString(R.string.internal_server_error));
                       /* String urls=call.request().url().toString();
                        urls.replace(oldToken,sessionManager.getToken());*/

                    }
                    jsonResp.setSuccess(isSuccess(strJson));
                    LogManager.e(strJson);
                } else {
                    //jsonResp.setErrorMsg(response.errorBody().string());
                    if (response.code() == 401 || response.code() == 404) {
                        //jsonResp.setStatusMsg(context.getResources().getString(R.string.account_deactivated));
                        sessionManager.clearToken();
                        sessionManager.clearAll();
                        //((Activity)context).finishAffinity();
                        Intent intent = new Intent(context, EntryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                }
                jsonResp.setRequestData(requestData);
                jsonResp.setOnline(isOnline());
                requestData = (context != null && !isOnline()) ? context.getResources().getString(R.string.network_failure) : "im Asdmin tyoghc";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResp;
    }


    private boolean isOnline() {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    private boolean isSuccess(String jsonString) {
        boolean isSuccess = false;
        try {
            if (!TextUtils.isEmpty(jsonString)) {
                String statusCode = (String) getJsonValue(jsonString, Constants.STATUS_CODE, String.class);
                isSuccess = !TextUtils.isEmpty(statusCode) && "1".equals(statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    private String getStatusMsg(String jsonString) {
        String statusMsg = "";
        try {
            if (!TextUtils.isEmpty(jsonString)) {
                statusMsg = (String) getJsonValue(jsonString, Constants.STATUS_MSG, String.class);
                if (statusMsg.equalsIgnoreCase("Token Expired")) {
                    String token = (String) getJsonValue(jsonString, Constants.REFRESH_ACCESS_TOKEN, String.class);
                    sessionManager.setToken(token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusMsg;
    }

    private Object getJsonValue(String jsonString, String key, Object object) {
        Object objct = object;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has(key)) objct = jsonObject.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return objct;
    }


}
