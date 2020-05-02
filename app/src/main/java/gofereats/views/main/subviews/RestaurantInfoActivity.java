package gofereats.views.main.subviews;

/**
 * @package com.gofereats
 * @subpackage views.main.subviews
 * @category RestaurantInfoActivity
 * @author Trioangle Product Team
 * @version 0.6
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.RunTimePermission;
import gofereats.configs.SessionManager;
import gofereats.datamodels.infowindow.InfoWindow;
import gofereats.datamodels.infowindow.RestaurantOpensInfo;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;

/*************************************************************
 Restaurants info details
 ************************************************************/

public class RestaurantInfoActivity extends AppCompatActivity implements OnMapReadyCallback, ServiceListener {


    public static ArrayList<RestaurantOpensInfo> restaurantOpensInfos = new ArrayList<>();
    /**
     * Variable Declarations
     */
    public GoogleMap mGoogleMap;
    public ArrayList mMarkerPoints = new ArrayList();
    public RunTimePermission runTimePermission;
    /**
     * Model Classes
     */
    public InfoWindow infoWindow;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @Inject
    ImageUtils imageUtils;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.ivBackArrow)
    ImageView ivBackArrow;
    public @InjectView(R.id.tvRestaurantName)
    CustomTextView tvRestaurantName;
    public @InjectView(R.id.tvAddress)
    CustomTextView tvAddress;
    public @InjectView(R.id.lltOpenTimes)
    LinearLayout lltOpenTimes;
    public @InjectView(R.id.rltWholeview)
    ScrollView rltWholeview;
    public @InjectView(R.id.mapview)
    MapView mMapView;
    private int mrestaurantId = 6;
    private AlertDialog dialog;

    /**
     * Click Listners For Views
     */
    @OnClick(R.id.ivBackArrow)
    public void back() {
        onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(this); //this is important

        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(ivBackArrow,this);
        mrestaurantId = sessionManager.getRestaurantId();
        rltWholeview.setVisibility(View.GONE);
        getData();
        initMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * Get Data From Api
     */

    private void getData() {

        commonMethods.showProgressDialog(this, customDialog);
        apiService.getStoreInfos(mrestaurantId, sessionManager.getToken()).enqueue(new RequestCallback(this));

    }

    /**
     * Update Views
     */
    private void updateView() {

        if (infoWindow.getRestaurantLatitude() != null || infoWindow.getRestaurantLongitude() != null) {
            LatLng restaurant = new LatLng(Double.valueOf(infoWindow.getRestaurantLatitude()), Double.valueOf(infoWindow.getRestaurantLongitude()));
            LatLng user = new LatLng(Double.valueOf(infoWindow.getUserLatitude()), Double.valueOf(infoWindow.getUserLongitude()));

            mMarkerPoints.add(0, restaurant);
            mMarkerPoints.add(1, user);

            getRestaurantOnMap();

            System.out.println("LAt LONG " + restaurant);
            tvRestaurantName.setText(infoWindow.getRestaurantName());
            tvAddress.setText(infoWindow.getRestaurantLocation());

            if (infoWindow.getRestaurantTime() != null && infoWindow.getRestaurantTime().size() > 0) {
                restaurantOpensInfos.clear();
                restaurantOpensInfos.addAll(infoWindow.getRestaurantTime());
            }

            if (restaurantOpensInfos.size() > 0) {
                System.out.println("status SIZE " + restaurantOpensInfos.size());
                lltOpenTimes.removeAllViews();
                for (int i = 0; i < restaurantOpensInfos.size(); i++) {
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.opening_times, null);
                    CustomTextView tvTimings = view.findViewById(R.id.tvTimings);
                    CustomTextView tvDays = view.findViewById(R.id.tvDays);
                    RelativeLayout rltTimings = view.findViewById(R.id.rltTimings);
                    if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                        rltTimings.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        tvDays.setGravity(Gravity.START);
                        tvTimings.setGravity(Gravity.START);
                    }
                    tvDays.setText(restaurantOpensInfos.get(i).getDayName());
                    tvTimings.setText(restaurantOpensInfos.get(i).getStartTime() + " - " + restaurantOpensInfos.get(i).getEndTime());
                    lltOpenTimes.addView(view);
                }
            } else {
                lltOpenTimes.setVisibility(View.GONE);
            }

        }
    }


    /**
     * To intialize map
     */
    private void initMap() {
        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, this, -1).show();
            finish();
        } else {
            if (mGoogleMap != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompaviewInfo.putExtra("id",)t#requestPermissions for more details.
                    return;
                }
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initMap();
    }

    /**
     * get Resturant On Map
     */
    private void getRestaurantOnMap() {
        mGoogleMap.clear();
        LatLng restaurantLatLng = (LatLng) mMarkerPoints.get(0);
        LatLng user = (LatLng) mMarkerPoints.get(1);

        // adding a marker on map with image from  drawable
        mGoogleMap.addMarker(new MarkerOptions().position(user).anchor(0, 1f).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.pickup_dot, 0))));
        //drawRouteRest(0);
        mGoogleMap.addMarker(new MarkerOptions().position(restaurantLatLng).anchor(0, 1f).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.dest_dot, 1))));
        //drawRouteRest(1);

        //mGoogleMap.addMarker(createMarker(R.drawable.pickup_dot, restaurantLatLng, 1));
        //drawRouteRest(1);
        //mGoogleMap.addMarker(createMarker(R.drawable.dest_dot, user, 0));
        //drawRouteRest(0);

        final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(user.latitude, user.longitude)).include(new LatLng(restaurantLatLng.latitude, restaurantLatLng.longitude)).build();
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        int height = getResources().getDisplayMetrics().heightPixels / 2;
        int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

        /*CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 25, 25, 5);
        mGoogleMap.animateCamera(cu);*/
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mGoogleMap.animateCamera(cu);


        if (mMapView.getViewTreeObserver().isAlive()) {
            mMapView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressWarnings("deprecation")
                        @SuppressLint("NewApi")
                        // We check which build version we are using.
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                mMapView.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            } else {
                                mMapView.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);
                            }
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                        }
                    });
        }

    }


    /**
     * Place Layout as a Marker as bitmap In map
     *
     * @param resId Drawable Image
     * @param type  Marker type
     * @return Marker
     */
    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId, int type) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_dialog, null);
        CustomTextView tv_address = customMarkerView.findViewById(R.id.tvLocation);
        CustomTextView tvOrderID = customMarkerView.findViewById(R.id.tvOrderID);
        final ImageView markerImageView = customMarkerView.findViewById(R.id.ivMarker);
        markerImageView.setImageResource(resId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (type == 1) {
            tv_address.setText(infoWindow.getRestaurantName());
            tvOrderID.setVisibility(View.GONE);
            params.gravity = Gravity.START;

        } else {
            tv_address.setText(sessionManager.getLocation());
            tvOrderID.setVisibility(View.GONE);
            params.gravity = Gravity.START;

        }
        markerImageView.setLayoutParams(params);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas;
        canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null) drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


    /**
     * Place Layout as a Marker In map
     *
     * @param resId Drawable Image
     * @param point Latlng
     * @param type  Marker Type
     * @return Marker
     */
    public MarkerOptions createMarker(@DrawableRes int resId, LatLng point, int type) {

        MarkerOptions marker = new MarkerOptions();
        marker.position(point);
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_dialog, null);
        CustomTextView tv_address = customMarkerView.findViewById(R.id.tvLocation);
        CustomTextView tvOrderID = customMarkerView.findViewById(R.id.tvOrderID);
        ImageView markerImageView = customMarkerView.findViewById(R.id.ivMarker);
        markerImageView.setImageResource(resId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (type == 1) {
            tv_address.setText(infoWindow.getRestaurantName());
            tvOrderID.setVisibility(View.GONE);
            params.gravity = Gravity.START;
        } else {
            tv_address.setText(sessionManager.getLocation());
            tvOrderID.setVisibility(View.GONE);
            params.gravity = Gravity.START;

        }
        markerImageView.setLayoutParams(params);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas;
        canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null) drawable.draw(canvas);
        customMarkerView.draw(canvas);
        marker.anchor(0.5f, 0.5f);
        marker.icon(BitmapDescriptorFactory.fromBitmap(returnedBitmap));
        return marker;

    }

    /**
     * Route Drawer
     */
    private void drawRouteRest() {

        LatLng restaurant = (LatLng) mMarkerPoints.get(0);
        LatLng user = (LatLng) mMarkerPoints.get(1);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        builder.include(user);

        builder.include(restaurant);


        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels / 2;
        int height = getResources().getDisplayMetrics().heightPixels / 2;
        int padding = (int) (width * 0.08); // offset from edges of the map 10% of screen


        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mGoogleMap.animateCamera(cu);

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }


        if (jsonResp.isSuccess()) {
            rltWholeview.setVisibility(View.VISIBLE);
            onSuccessGetRestaurantList(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {

    }

    private void onSuccessGetRestaurantList(JsonResponse jsonResp) {
        infoWindow = gson.fromJson(jsonResp.getStrResponse(), InfoWindow.class);
        if (infoWindow != null) {
            updateView();
        }
    }

}
