package gofereats.views.splash;
/**
 * @package com.trioangle.gofereats
 * @subpackage view.main
 * @category SplashActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.google.android.libraries.places.api.Places;
import com.obs.CustomTextView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.views.main.MainActivity;
import gofereats.views.main.subviews.EntryActivity;
import gofereats.views.main.subviews.LocActivity;

/*****************************************************************
 Application splash screen
 ****************************************************************/
public class SplashActivity extends AppCompatActivity {

    public @Inject
    SessionManager sessionManager;

    public @InjectView(R.id.rltDeliveryAddress)
    RelativeLayout rltDeliveryAddress;
    public @InjectView(R.id.tvDeliveryAddress)
    CustomTextView tvDeliveryAddress;
    public @InjectView(R.id.tvDeliveryText)
    CustomTextView tvDeliveryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        initMapPlaceAPI();
        getIntentValues();

        sessionManager.setDeliveredTime("");
        sessionManager.setHomeScheduledDay("");
        sessionManager.setType("0");
        sessionManager.setOrderType(0);
        sessionManager.setScheduledDay("");
        sessionManager.setScheduleMin("");
        sessionManager.setScheduleDate("");
        sessionManager.setAddedTime("");
        sessionManager.setIsFirst(true);
        sessionManager.setCartCount(0);
        sessionManager.setCartAmount("");
        setLocale();
        checkForUpdates();

        getDeliveryAddress();
    }

    private void setLocale(){
        Locale locale = new Locale(sessionManager.getAppLanguageCode());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        SplashActivity.this.getResources().updateConfiguration(config, SplashActivity.this.getResources().getDisplayMetrics());
    }

    private void getDeliveryAddress() {
        if (TextUtils.isEmpty(sessionManager.getLocation()) || sessionManager.getLocation().equals("")) {
            rltDeliveryAddress.setVisibility(View.GONE);
        } else {
            rltDeliveryAddress.setVisibility(View.VISIBLE);
            System.out.println("get Location "+getResources().getString(R.string.delivering_to));
            tvDeliveryText.setText(getResources().getString(R.string.delivering_to));
            tvDeliveryAddress.setText(sessionManager.getLocation());
        }
    }


    /**
     * Method to check to enter Login page or location page or main page
     */


    private void getIntentValues() {
       /* sessionManager.setCurrencyCode("$");
        sessionManager.setCurrencySymbol("$");*/

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(sessionManager.getToken())) {
                    Intent intent = new Intent(SplashActivity.this, EntryActivity.class);
                    startActivity(intent);
                    finish();
                } else if (TextUtils.isEmpty(sessionManager.getLocation())) {
                    Intent intent = new Intent(SplashActivity.this, LocActivity.class);
                    intent.putExtra("location", "entry");
                    intent.putExtra("type", 2);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("type", "home");
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... your own onResume implementation
        checkForCrashes();  //Calling crash checking method
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    /**
     * Method to check for crashes
     */
    private void checkForCrashes() {
        CrashManager.register(this);
    }

    /**
     * Method to register
     */

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    /**
     * Method to unregister
     */


    private void unregisterManagers() {
        UpdateManager.unregister();
    }

    private void initMapPlaceAPI() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
