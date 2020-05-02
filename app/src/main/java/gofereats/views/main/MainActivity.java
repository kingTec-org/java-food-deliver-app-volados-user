package gofereats.views.main;


/**
 * @package com.trioangle.gofereats
 * @subpackage view.main
 * @category SplashActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.obs.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ActivityListener;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.pushnotification.Config;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.fragments.AccountFragment;
import gofereats.views.main.fragments.HomeFragment;
import gofereats.views.main.fragments.SearchFragment;
import gofereats.views.main.fragments.orders.OrderFragment;
import gofereats.views.main.subviews.PlaceOrderActivity;

import static gofereats.utils.Enums.REQ_UPDATE_DEVICE_ID;

/*****************************************************************
 Application Main Activity
 ****************************************************************/
public class MainActivity extends AppCompatActivity implements ActivityListener, ServiceListener {

    public static boolean cancelled = false;     // To Open History Fragment when User Cancels or Completed order
    public static boolean isRefreshed = false;   // To ReFresh Fragment when User Receives Push Notification
    public static boolean isOrder = false;

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
    public String type = "";
    public Fragment selectedFragment = null;
    public String selected = "home";
    public String backStateName;
    public @InjectView(R.id.tvCartAmount)
    CustomTextView tvCartAmount;
    public @InjectView(R.id.tvCartCount)
    CustomTextView tvCartCount;
    public @InjectView(R.id.rltCheckOut)
    RelativeLayout rltCheckOut;
    public BottomNavigationViewEx bottomNavigationView;
    public BroadcastReceiver mBroadcastReceiver;
    protected OnBackPressedListener onBackPressedListener;
    private AlertDialog dialog;
    private int backPressed = 0;    // used by onBackPressed()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);

        dialog = commonMethods.getAlertDialog(this);

        type = getIntent().getStringExtra("type");
        System.out.println("typeinMainOncreate" + type);
        if (type == null || type.equals("")) {
            type = "";
        }

        initBottomNavigation();
        updateDeviceId();
        receivePushNotification();
    }


    /**
     * Bottom navigation view to show and set fragment for Home page, Earning page, Rating page, Account page
     */


    public void initBottomNavigation() {

        bottomNavigationView = findViewById(R.id.navigation);


        if ("placeorder".equals(type)) {
            bottomNavigationView.setSelectedItemId(R.id.tab_orders);
        } else if ("search".equals(type)) {
            bottomNavigationView.setSelectedItemId(R.id.tab_search);
        } else if ("account".equals(type)) {
            bottomNavigationView.setSelectedItemId(R.id.tab_settings);
        }
        /*bottomNavigationView.setIconSize(30, 30);
        bottomNavigationView.enableAnimation(false);
        bottomNavigationView.enableShiftingMode(false);
        bottomNavigationView.enableItemShiftingMode(false);*/

        if (selectedFragment != null)
            getSupportFragmentManager().beginTransaction().remove(selectedFragment).commit();
        // BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (selectedFragment != null)
                    getSupportFragmentManager().beginTransaction().remove(selectedFragment).commit();

                switch (item.getItemId()) {
                    case R.id.tab_home:
                        selectedFragment = HomeFragment.newInstance();
                        selected = "home";
                        break;
                    case R.id.tab_search:
                        selectedFragment = SearchFragment.newInstance();
                        selected = "search";
                        break;
                    case R.id.tab_orders:
                        selectedFragment = OrderFragment.newInstance();
                        selected = "order";
                        break;
                    case R.id.tab_settings:
                        selectedFragment = AccountFragment.newInstance();
                        selected = "setting";
                        break;
                    default:
                        selectedFragment = HomeFragment.newInstance();
                        selected = "home";
                        break;
                }

                if (selectedFragment != null) {
                   // backStateName = selectedFragment.getClass().getName();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    transaction.commit();
                    return true;
                } else {
                    return false;
                }

            }
        });

        if ("placeorder".equals(type)) {
            //Redirects displaying the order fragment - one time only
            isRefreshed = false;
            selectedFragment = OrderFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, OrderFragment.newInstance());
            transaction.commit();
        } else if ("search".equals(type)) {
            //Redirects displaying the Search fragment - one time only
            isRefreshed = true;
            selectedFragment = SearchFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        } else if ("account".equals(type)) {
            //Redirects displaying the account fragment - one time only
            isRefreshed = true;
            selectedFragment = AccountFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        } else {
            //Manually displaying the first fragment - one time only
            isRefreshed = true;
            selectedFragment = HomeFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
        }
        rltCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Placeorder = new Intent(getApplicationContext(), PlaceOrderActivity.class);
                startActivity(Placeorder);
            }
        });
    }

    @Override
    public Resources getRes() {
        return MainActivity.this.getResources();
    }

    @Override
    public MainActivity getInstance() {
        return MainActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
        if (sessionManager.getCartCount() > 0) {
            rltCheckOut.setVisibility(View.VISIBLE);
            tvCartAmount.setText(sessionManager.getCurrencySymbol() + sessionManager.getCartAmount());
            tvCartCount.setText(String.valueOf(sessionManager.getCartCount()));
        } else {
            rltCheckOut.setVisibility(View.GONE);
        }
        type = getIntent().getStringExtra("type");
        if (MainActivity.isOrder) {
            type = "placeorder";
            bottomNavigationView.setSelectedItemId(R.id.tab_orders);
            selectedFragment = OrderFragment.newInstance();
        }
        isRefreshed = false;
    }

    /**
     * Call API to update restaurant device id
     */
    public void updateDeviceId() {
        /**
         *  Type 0 for Eater 1 for restaurant 2 for Driver
         *  Device type 2 for android and 1 for iOS
         */

        if (TextUtils.isEmpty(sessionManager.getDeviceId())) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            sessionManager.setDeviceId(refreshedToken);
        }
        apiService.updateDeviceId(sessionManager.getType(), sessionManager.getToken(), "2", sessionManager.getDeviceId()).enqueue(new RequestCallback(REQ_UPDATE_DEVICE_ID, this));
    }


    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
       // commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_UPDATE_DEVICE_ID:
                if (!jsonResp.isSuccess() && !TextUtils.isEmpty(jsonResp.getStatusMsg())) {
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
     * To intialize back pressed listener
     *
     * @param onBackPressedListener value for back press listner
     */
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if ("home".equals(selected) && HomeFragment.isSeeMore) {
            HomeFragment.showhome();//show home method call for home fragment
        } else {
            if (onBackPressedListener != null) {
                onBackPressedListener.doBack();
            } else {

                if (backPressed >= 1) {
                    finishAffinity();
                } else {
                    // clean up
                    backPressed = backPressed + 1;
                    Toast.makeText(this, getResources().getString(R.string.exit), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
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
                        if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_accepted") || jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delayed") || jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delivery_started")) {
                            cancelled = false;
                            isRefreshed = false;
                            isOrder = false;
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("type", "placeorder");
                            startActivity(i);
                        } else if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_cancelled") || jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delivery_completed")) {
                            cancelled = true;
                            isRefreshed = false;
                            isOrder = false;
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("type", "placeorder");
                            startActivity(i);
                        } else if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("Refund")) {
                            isRefreshed = true;
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("type", "home");
                            startActivity(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    public interface OnBackPressedListener {
        void doBack();
    }
}

