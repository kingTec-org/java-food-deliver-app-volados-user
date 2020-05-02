package gofereats.configs;
/**
 * @package com.trioangle.gofereats
 * @subpackage configs
 * @category AppController
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import gofereats.R;
import gofereats.dependencies.component.AppComponent;
import gofereats.dependencies.component.DaggerAppComponent;
import gofereats.dependencies.module.ApplicationModule;
import gofereats.dependencies.module.NetworkModule;

/*****************************************************************
 AppController
 ****************************************************************/
public class AppController extends Application {

    private static AppComponent appComponent;
    // private static InstagramHelper instagramHelper;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this); // Multiple dex initialize

        // Dagger%COMPONENT_NAME%
        appComponent = DaggerAppComponent.builder().applicationModule(new ApplicationModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .networkModule(new NetworkModule(getResources().getString(R.string.base_url))).build();

        /*instagramHelper = new InstagramHelper.Builder()
                .withClientId(getResources().getString(R.string.instagram_client_id))
                .withRedirectUrl(getResources().getString(R.string.redirect_url))
                .withScope("basic+public_content+relationships")
                .build();*/
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }


   /* public static InstagramHelper getInstagramHelper() {
        return instagramHelper;
    }*/
}
