package gofereats.views.main.subviews;
/**
 * @package com.gofereats
 * @subpackage views.subviews
 * @category cancel activity
 * @author Trioangle Product Team
 * @version 1.0
 */

/* ************************************************************
                Class to cancel orders
    *********************************************************** */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.obs.CustomButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.cancel.CancelReasonModel;
import gofereats.datamodels.cancel.CancelResultModel;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.pushnotification.Config;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;

import static gofereats.utils.Enums.REQ_CANCEL_ORDER;
import static gofereats.utils.Enums.REQ_GET_CANCEL_REASONS;

public class CancellationActivity extends AppCompatActivity implements ServiceListener {

    public ArrayList<CancelReasonModel> cancelReasonModels = new ArrayList<>();
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CustomDialog customDialog1;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @Inject
    ImageUtils imageUtils;
    public int orderId = 0;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.edtCancelReason)
    EditText edtCancelReason;
    public @InjectView(R.id.spinner)
    Spinner spinner;
    public @InjectView(R.id.btnCancelReason)
    CustomButton btnCancelReason;
    public @InjectView(R.id.ivBackArrow)
    ImageView ivBackArrow;
    private BroadcastReceiver mBroadcastReceiver;
    private AlertDialog dialog;

    @OnClick(R.id.ivBackArrow)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btnCancelReason)
    public void onCancel() {
        cancelOrders();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation);

        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(this);
        receivePushNotification();
        ivBackArrow=(ImageView)findViewById(R.id.ivBackArrow) ;
        commonMethods.rotateArrow(ivBackArrow,this);
        /**
         * Getting Order Id From Order details
         */
        orderId = getIntent().getIntExtra("orderId", 0);

        getCancelReason();
    }


    /**
     * Call Api to get Reasons
     */
    private void getCancelReason() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.cancelOrdersReason(sessionManager.getType(), sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_CANCEL_REASONS, this));
    }

    /**
     * Call API to cancel order
     */
    public void cancelOrders() {

        String cancelMessage = edtCancelReason.getText().toString();

        int position = spinner.getSelectedItemPosition();

        if (position > 0) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.cancelOrders(cancelReasonModels.get(position).getCancelId(), cancelMessage, orderId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_CANCEL_ORDER, this));
        } else {
            commonMethods.showMessage(this, dialog, getResources().getString(R.string.select_reason));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {

            case REQ_GET_CANCEL_REASONS:
                //swipeContainer.setRefreshing(false);
                if (jsonResp.isSuccess()) {
                    onSuccessCancelReason(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_CANCEL_ORDER:
                if (jsonResp.isSuccess()) {
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(main);
                    finish();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * To set Values from api response
     *
     * @param jsonResponse successfull Json response
     */
    public void onSuccessCancelReason(JsonResponse jsonResponse) {
        CancelResultModel cancelResultModel = gson.fromJson(jsonResponse.getStrResponse(), CancelResultModel.class);
        if (cancelResultModel != null) {
            CancelReasonModel cancelReasonModel = new CancelReasonModel();
            cancelReasonModel.setCancelId(0);
            cancelReasonModel.setReason(getResources().getString(R.string.select_reason));
            cancelReasonModels.add(cancelReasonModel);
            cancelReasonModels.addAll(cancelResultModel.getCancelReasonModels());

            System.out.println("Cancel reason" + cancelReasonModels.size());
            String[] cancelReason = new String[cancelReasonModels.size()];

            for (int i = 0; i < cancelReasonModels.size(); i++) {
                cancelReason[i] = cancelReasonModels.get(i).getReason();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancel_reason_layout, cancelReason);

            spinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            /**
             *  Cancel trip reasons in spinner
             */
            /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });*/
        }
    }

    public void receivePushNotification() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("onrecieve");
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // FCM successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String JSON_DATA = sessionManager.getPushNotification();

                    try {
                        JSONObject jsonObject = new JSONObject(JSON_DATA);
                        System.out.println("jsonObjectPush" + jsonObject);
                        if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_accepted")) {
                            MainActivity.cancelled = false;
                            Intent order = new Intent(getApplicationContext(), MainActivity.class);
                            order.putExtra("type", "placeorder");
                            startActivity(order);
                        } else if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_cancelled")
                                /*||jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("Amount Refund")
                                ||jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("Refund")*/) {
                            MainActivity.cancelled = true;
                            Intent order = new Intent(getApplicationContext(), MainActivity.class);
                            order.putExtra("type", "placeorder");
                            startActivity(order);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
