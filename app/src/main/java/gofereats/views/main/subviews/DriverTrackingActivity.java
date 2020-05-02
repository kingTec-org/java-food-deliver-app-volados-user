package gofereats.views.main.subviews;


/**
 * @package com.gofereats
 * @subpackage views.subviews
 * @category Driver Tracking Activity
 * @author Trioangle Product Team
 * @version 1.0
 */


import android.Manifest;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.obs.CustomButton;
import com.obs.CustomTextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereats.R;
import gofereats.adapters.main.ReceiptOrderAdapter;
import gofereats.configs.AppController;
import gofereats.configs.Constants;
import gofereats.configs.RunTimePermission;
import gofereats.configs.SessionManager;
import gofereats.datamodels.order.FoodStatusModel;
import gofereats.datamodels.order.MenuListModel;
import gofereats.datamodels.order.OrderModel;
import gofereats.drawpolyline.DownloadTask;
import gofereats.drawpolyline.PolylineOptionsInterface;
import gofereats.interfaces.DriverLocation;
import gofereats.interfaces.LatLngInterpolator;
import gofereats.pushnotification.Config;
import gofereats.utils.CommonMethods;
import gofereats.utils.MyLocation;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;
import gofereats.views.main.fragments.orders.OrderFragment;



/* ************************************************************
                Class to track driver location
    *********************************************************** */


public class DriverTrackingActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 1000;
    private static String TAG = "MAP LOCATION";
    public final ArrayList movepoints = new ArrayList();
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    RunTimePermission runTimePermission;
    public GoogleMap mGoogleMap;
    public Polyline polyline;
    public ArrayList markerPoints = new ArrayList();
    public int status;
    public int driverDetailStatus;
    public int orderId = 0;
    public Marker carmarker;
    public float startbear = 0, endbear = 0;
    public Marker marker;
    public float speed = 13f;
    public boolean isFirst = true;
    public ValueAnimator valueAnimator;
    public DecimalFormat twoDForm;
    public Query query;
    public RelativeLayout orders_status;
    public RelativeLayout toolbar;
    public RelativeLayout help_layout;
    public RelativeLayout appbar_help_layout;
    public RelativeLayout rltCancel;
    public RelativeLayout rltDeliveryNotes;
    public ImageView appbararrow;
    public ImageView arrow;
    public ImageView ivDeliverNote_image;
    public LinearLayout order_lists;
    public ProgressBar progressBar;
    public CustomTextView recipt;
    public CustomTextView tvRestName;
    public CustomTextView tvOrderId;
    public CustomTextView tvTotal;
    public CustomTextView tvTotalamount;
    public CustomTextView tvEstimationTime;
    public CustomButton btnCancel;
    public int postion;
    public CustomTextView tvDriverName;
    public CustomTextView tvDriverVehcile;
    public CustomTextView tvDriverVechileNumber;
    public CustomTextView tvDriverRating;
    public CustomTextView tvDeliveryNOte;
    public CustomTextView tvPreparationText;
    public CustomTextView tvFoodStatus;
    public CustomTextView estimation_time1;
    public CircleImageView ivDriverProfile;
    public CustomButton btnContactDriver;
    public ImageView drop_down_icon;


    public List<OrderModel> trackingOrder = new ArrayList<OrderModel>();
    public ArrayList<MenuListModel> menuListModelArrayList = new ArrayList<MenuListModel>();
    public ArrayList<FoodStatusModel> foodStatusModels = new ArrayList<FoodStatusModel>();
    public ReceiptOrderAdapter receiptOrderAdapter;
    public RelativeLayout rltDriverDetails;
    public LinearLayout lltOrderDetailedStatus;
    public LinearLayout lltOrderDetails;
    public boolean isShown = true;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.mapview)
    MapView mMapView;
    /**
     * Get user current location
     */
    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            if (location == null) return;
        }
    };
    private LocationManager mLocationManager;
    private Location lastLocation = null;
    // Firebase database variables
    private DatabaseReference mFirebaseDatabase;
    private ValueEventListener mSearchedLocationReferenceListener;
    private RelativeLayout rltRatings;
    private BroadcastReceiver mBroadcastReceiver;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private boolean isFromOnCreate = true;
    private android.location.LocationListener mLocationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.d("", String.format("%f, %f", location.getLatitude(), location.getLongitude()));

            } else {
                Log.d("", "Location is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.d("", s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.d("", s);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.d("", s);
        }
    };

    /**
     * Rotate marker
     **/
    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }

    /**
     * Expand the View
     *
     * @param v relative layout to expand
     */
    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    /**
     * Collapse the View
     *
     * @param v relativelayout to collapse
     */
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_tracking);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        mMapView.onCreate(savedInstanceState);


        twoDForm = new DecimalFormat("#.##########");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);

        mMapView.getMapAsync(this); //this is important
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'Driver Location' node
        mFirebaseDatabase = mFirebaseInstance.getReference("live_tracking");

        orders_status = findViewById(R.id.orders_status);
        toolbar = findViewById(R.id.toolbar);
        help_layout = findViewById(R.id.help_layout);
        appbar_help_layout = findViewById(R.id.appbar_help_layout);
        order_lists = findViewById(R.id.order_lists);
        progressBar = findViewById(R.id.progress);
        recipt = findViewById(R.id.recipt);

        btnCancel = findViewById(R.id.btnCancel);
        tvRestName = findViewById(R.id.tvRestName);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvTotal = findViewById(R.id.tvTotal);
        tvTotalamount = findViewById(R.id.tvTotalamount);
        tvEstimationTime = findViewById(R.id.tvEstimationTime);
        rltCancel = findViewById(R.id.rltCancel);
        rltDriverDetails = findViewById(R.id.rltDriverDetails);
        tvDriverName = findViewById(R.id.tvDriverName);
        ivDriverProfile = findViewById(R.id.ivDriverProfile);
        tvDriverVehcile = findViewById(R.id.tvDriverVehcile);
        tvDriverVechileNumber = findViewById(R.id.tvDriverVechileNumber);
        tvDriverRating = findViewById(R.id.tvDriverRating);
        btnContactDriver = findViewById(R.id.btnContactDriver);
        rltDeliveryNotes = findViewById(R.id.rltDeliveryNotes);
        ivDeliverNote_image = findViewById(R.id.ivDeliverNote_image);
        tvDeliveryNOte = findViewById(R.id.tvDeliveryNOte);
        tvPreparationText = findViewById(R.id.tvPreparationText);
        tvFoodStatus = findViewById(R.id.tvFoodStatus);
        estimation_time1 = findViewById(R.id.estimation_time1);
        lltOrderDetails = findViewById(R.id.lltOrderDetails);
        lltOrderDetailedStatus = findViewById(R.id.lltOrderDetailedStatus);
        drop_down_icon = findViewById(R.id.drop_down_icon);
        rltRatings = findViewById(R.id.rltRatings);
        arrow=findViewById(R.id.arrow);
        appbararrow = findViewById(R.id.appbar_arrow);
        commonMethods.rotateArrow(arrow,this);
        commonMethods.rotateArrow(appbararrow,this);
        receivePushNotification();
        Intent intent = getIntent();
        postion = intent.getIntExtra("position", 0);
        orderId = intent.getIntExtra("orderId", 0);
        System.out.println("orderid" + orderId);
        OrderFragment orderFragment = new OrderFragment();
        trackingOrder = orderFragment.getUpcomingOrderDetailsList();
        System.out.println("trackingOrder_size" + trackingOrder.size());

        if (trackingOrder.size() == 0 || orderId == -1) {
            if (orderId == -1) MainActivity.cancelled = true;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("type", "placeorder");
            startActivity(i);
            finish();
        } else if (orderId > 0) {
            for (int i = 0; i < trackingOrder.size(); i++) {
                if (trackingOrder.get(i).getOrderid() == orderId) {
                    postion = i;
                    System.out.println("trackingOrder" + trackingOrder.get(postion).getOrderStatus());
                    if (trackingOrder.get(postion).getOrderStatus() == 1) {
                        trackingOrder.get(postion).setOrderStatus(3);
                    }
                }
            }
        }

        drop_down_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShown) {
                    drop_down_icon.animate().rotation(180).start();
                    isShown = false;
                    expand(lltOrderDetailedStatus);
                } else {
                    drop_down_icon.animate().rotation(0).start();
                    isShown = true;
                    collapse(lltOrderDetailedStatus);
                }
            }
        });
        System.out.println("ORDER POSITION " + postion);
        System.out.println("ORDER SIZE " + trackingOrder.size());

        if (trackingOrder.get(postion).getOrderStatus() == 3) {
            tvPreparationText.setVisibility(View.VISIBLE);
        } else {
            tvPreparationText.setVisibility(View.GONE);
        }

        if (trackingOrder.get(postion).getOrderStatus() == 1) {
            rltCancel.setVisibility(View.VISIBLE);
        } else {
            rltCancel.setVisibility(View.GONE);
        }

        if (!trackingOrder.get(postion).getDrivername().equals("") && trackingOrder.get(postion).getOrderDeliveryStatus() >= 3) {
            rltDriverDetails.setVisibility(View.VISIBLE);
            tvDriverName.setText(trackingOrder.get(postion).getDrivername());
            Glide.with(this).load(trackingOrder.get(postion).getDriverimage()).into(ivDriverProfile);
            if (Integer.valueOf(trackingOrder.get(postion).getDrivertating()) > 0) {
                rltRatings.setVisibility(View.VISIBLE);
                // Close Bracket Is in text view don't to clear This
                tvDriverRating.setText("( " + trackingOrder.get(postion).getDrivertating());
            } else {
                rltRatings.setVisibility(View.GONE);
            }

            tvDriverVehcile.setText(trackingOrder.get(postion).getDrivervechiletype());
            tvDriverVechileNumber.setText(trackingOrder.get(postion).getDrivervechilenumber());
            if (trackingOrder==null||trackingOrder.get(postion).getDeliveryoption().equals("") || trackingOrder.get(postion).getDeliveryoption() == null) {
                rltDeliveryNotes.setVisibility(View.GONE);
            } else {
                rltDeliveryNotes.setVisibility(View.VISIBLE);
                if (trackingOrder.get(postion).getDeliveryoption().equals("0")) {
                    ivDeliverNote_image.setImageDrawable(getResources().getDrawable(R.drawable.car_white));
                    tvDeliveryNOte.setText(getString(R.string.Ready_to_meet) + " " + trackingOrder.get(postion).getDrivername() + " " + getString(R.string.outside));
                } else {
                    ivDeliverNote_image.setImageDrawable(getResources().getDrawable(R.drawable.door_white));
                    tvDeliveryNOte.setText(R.string.Food_status_door);
                }
            }

        } else {
            rltDriverDetails.setVisibility(View.GONE);
            rltDeliveryNotes.setVisibility(View.GONE);
        }

        tvRestName.setText(trackingOrder.get(postion).getName());
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            tvOrderId.setText(getResources().getString(R.string.orderid)+ trackingOrder.get(postion).getOrderid()+"#");
        else
            tvOrderId.setText(getResources().getString(R.string.orderid) +"#"+ trackingOrder.get(postion).getOrderid());
        tvTotal.setText(getResources().getString(R.string.total_amount));
        tvTotalamount.setText(sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getTotalamount());
        String estimate[] = trackingOrder.get(postion).getEstcompletetime().split(" ", 2);
        System.out.println("Estimate time"+ estimate[0]);
        System.out.println("Estimate text"+ estimate[1]);
        tvEstimationTime.setText(estimate[0]);
        estimation_time1.setText(estimate[1]);
        /*if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
            tvEstimationTime.setText(estimate[1]);
            tvEstimationTime.setAllCaps(true);
            tvEstimationTime.setTextColor(getResources().getColor(R.color.payment_method));
            tvEstimationTime.setTextSize(12);
            estimation_time1.setText(estimate[0]);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvRestName.getLayoutParams();
            params.setMargins(0,0,0,35);
            estimation_time1.setTextColor(getResources().getColor(R.color.total_text_colur));
            estimation_time1.setTextSize(25);
        }else {
            tvEstimationTime.setText(estimate[0]);
            estimation_time1.setText(estimate[1]);
        }*/
        tvFoodStatus.setText(trackingOrder.get(postion).getUserstatustext());

        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.apptheme), android.graphics.PorterDuff.Mode.SRC_IN);
        final OrderModel orderModel = trackingOrder.get(postion);
        if (orderModel.getRemainingseconds() > 0) {

            int progress = orderModel.getRemainingseconds();
            if (progress > 0) {
                ProgressBar downloadProgress = progressBar;
                downloadProgress.setMax((orderModel.getTotalseconds()) / 60);
                if (downloadProgress.isIndeterminate()) {
                    downloadProgress.setIndeterminate(false);
                }
                setProgressLoader(progressBar, orderModel.getTotalseconds(), orderModel.getRemainingseconds());
            } else {
                progressBar.setProgress(0);
            }

        } else {
            progressBar.setProgress(0);
        }


        // arrow = findViewById(R.id.arrow);

        arrow.setOnClickListener(this);
        appbararrow.setOnClickListener(this);


        btnContactDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contact = new Intent(getApplicationContext(), ContactActivity.class);
                contact.putExtra("drivername", trackingOrder.get(postion).getDrivername());
                contact.putExtra("drivernumber", trackingOrder.get(postion).getDrivercontactnumber());
                startActivity(contact);
            }
        });
        /**
         * Cancel Order Page
         */

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(getApplicationContext(), CancellationActivity.class);
                cancel.putExtra("orderId", trackingOrder.get(postion).getOrderid());
                startActivity(cancel);
            }
        });

        /**
         * View Receipt
         */
        recipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(postion);
            }
        });

        foodStatusModels = trackingOrder.get(postion).getFoodStatus();

        if (foodStatusModels.size() > 0) {
            drop_down_icon.setVisibility(View.VISIBLE);
        } else {
            drop_down_icon.setVisibility(View.INVISIBLE);
        }

        foodStatusModels = trackingOrder.get(postion).getFoodStatus();
        if (foodStatusModels.size() > 0) {
            System.out.println("status SIZE " + foodStatusModels.size());
            lltOrderDetails.removeAllViews();
            for (int i = 0; i < foodStatusModels.size(); i++) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.food_status, null);
                CustomTextView tvStatusTime = view.findViewById(R.id.tvStatusTime);
                CustomTextView tvStatus = view.findViewById(R.id.tvStatus);
                RelativeLayout rltFoodProcess = view.findViewById(R.id.rlt_food_process);

                if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                    rltFoodProcess.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    tvStatusTime.setGravity(Gravity.START);
                    tvStatus.setGravity(Gravity.START);
                }
                tvStatusTime.setText(String.valueOf(foodStatusModels.get(i).getTime()));
                tvStatus.setText(foodStatusModels.get(i).getStatus());
                lltOrderDetails.addView(view);
            }
        } else {
            lltOrderDetailedStatus.setVisibility(View.GONE);
        }


        menuListModelArrayList = trackingOrder.get(postion).getMenu();
        System.out.println("Menu SIZE " + menuListModelArrayList.size());
        order_lists.removeAllViews();
        for (int i = 0; i < menuListModelArrayList.size(); i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_list_histroy, null);
            CustomTextView item_count = view.findViewById(R.id.item_count);
            RelativeLayout rlt_item = view.findViewById(R.id.rlt_item);
            CustomTextView item_name = view.findViewById(R.id.item_name);
            ImageView thumbs = view.findViewById(R.id.thumbs);

            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
                rlt_item.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                item_name.setGravity(Gravity.START);
                item_count.setGravity(Gravity.START);
            }

            thumbs.setVisibility(View.GONE);
            item_count.setText(String.valueOf(menuListModelArrayList.get(i).getQuantity()));
            item_name.setText(menuListModelArrayList.get(i).getMenuName());
            order_lists.addView(view);

        }
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.computeScroll();
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("TAG", "onPanelSlide, offset " + slideOffset);
                if (slideOffset > 0.9) {
                    toolbar.setVisibility(View.VISIBLE);
                } else {
                    toolbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("TAG", "onPanelStateChanged " + newState);


            }
        });


        getData();
        initMap();

    }

    /**
     * To show time taken by the driver to arrive
     *
     * @param progressBar   Progress bar for arrival of the driver (time)
     * @param totalTime     Total time that would be taken by driver
     * @param remainingTime Remaing time needed by the driver
     */


    public void setProgressLoader(final ProgressBar progressBar, final long totalTime, final int remainingTime) {
        new CountDownTimer(remainingTime * 1000, 1000 * 60) {
            @Override
            public void onTick(long millisUntilFinished) {
                //this will be done every 1000 milliseconds ( 1 seconds )
                int progress = (int) (((totalTime * 1000) - millisUntilFinished) / 1000);
                progressBar.setProgress(progress / 60);
                //System.out.println("Progress "+progress/60);
                //System.out.println("totalTime "+totalTime/60);
            }

            @Override
            public void onFinish() {
                //the progressBar will be invisible after 60 000 miliseconds ( 1 minute)
                //progressBar.dismiss();
                progressBar.setProgress((int) (totalTime / 60));
            }

        }.start();
    }

    /**
     * To show receipt calculation
     *
     * @param postion order based on the position
     */


    private void showAlert(int postion) {
        menuListModelArrayList = trackingOrder.get(postion).getMenu();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.receipt, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        RecyclerView orders_list = dialogView.findViewById(R.id.orders_list);
        RelativeLayout receipt_footer = dialogView.findViewById(R.id.receipt_footer);
        CustomTextView tvSubTotalPrice = dialogView.findViewById(R.id.tvSubTotalPrice);
        CustomTextView tvTaxPrice = dialogView.findViewById(R.id.tvTaxPrice);
        CustomTextView tvDeliveryFee = dialogView.findViewById(R.id.tvDeliveryFee);
        CustomTextView tvTotalPrice = dialogView.findViewById(R.id.tvTotalPrice);
        RelativeLayout rltWallet = dialogView.findViewById(R.id.rltWallet);
        RelativeLayout rltPromo = dialogView.findViewById(R.id.rltPromo);
        CustomTextView tvWalletAmount = dialogView.findViewById(R.id.tvWalletAmount);
        CustomTextView tvDiscountPrice = dialogView.findViewById(R.id.tvDiscountPrice);
        RelativeLayout delivery_fee_layout = dialogView.findViewById(R.id.delivery_fee_layout);
        RelativeLayout rltServiceFee = dialogView.findViewById(R.id.rltServiceFee);
        CustomTextView tvServiceAmount = dialogView.findViewById(R.id.tvServiceAmount);
        RelativeLayout tax_layout = dialogView.findViewById(R.id.tax_layout);
        RelativeLayout rltPenalityFee = dialogView.findViewById(R.id.rltPenalityFee);
        CustomTextView tvPenalityPrice = dialogView.findViewById(R.id.tvPenalityPrice);
        RelativeLayout rltAppliedPenality = dialogView.findViewById(R.id.rltAppliedPenality);
        CustomTextView tvAppliedPenalityPrice = dialogView.findViewById(R.id.tvAppliedPenalityPrice);


        tvSubTotalPrice.setText(sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getSubtotal());
        tvTaxPrice.setText(sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getTax());
        tvTotalPrice.setText(sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getTotalamount());

        if (!TextUtils.isEmpty(trackingOrder.get(postion).getDeliveryfee()) && Float.valueOf(trackingOrder.get(postion).getDeliveryfee()) > 0) {
            delivery_fee_layout.setVisibility(View.VISIBLE);
            tvDeliveryFee.setText(sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getDeliveryfee());
        } else {
            delivery_fee_layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(trackingOrder.get(postion).getWalletamount()) && Float.valueOf(trackingOrder.get(postion).getWalletamount()) > 0) {
            rltWallet.setVisibility(View.VISIBLE);
            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                tvWalletAmount.setText("(" + sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getWalletamount() + "-)");
            }else {
                tvWalletAmount.setText("(-" + sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getWalletamount() + ")");
            }

        } else {
            rltWallet.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(trackingOrder.get(postion).getPromoAmount()) && Float.valueOf(trackingOrder.get(postion).getPromoAmount()) > 0) {
            rltPromo.setVisibility(View.VISIBLE);
            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                tvDiscountPrice.setText("(" + sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getPromoAmount() + "-)");
            }else {
                tvDiscountPrice.setText("(-" + sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getPromoAmount() + ")");
            }
        } else {
            rltPromo.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(trackingOrder.get(postion).getBookingFee()) && Float.valueOf(trackingOrder.get(postion).getBookingFee()) > 0) {
            rltServiceFee.setVisibility(View.VISIBLE);
            tvServiceAmount.setText(sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getBookingFee());
        } else {
            rltServiceFee.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(trackingOrder.get(postion).getTax()) && Float.valueOf(trackingOrder.get(postion).getTax()) > 0) {
            tax_layout.setVisibility(View.VISIBLE);
            tvTaxPrice.setText(sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getTax());
        } else {
            tax_layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(trackingOrder.get(postion).getPenality()) && Float.valueOf(trackingOrder.get(postion).getPenality()) > 0) {
            rltPenalityFee.setVisibility(View.VISIBLE);
            tvPenalityPrice.setText(sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getPenality());
        } else {
            rltPenalityFee.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(trackingOrder.get(postion).getAppliedPenality()) && Float.valueOf(trackingOrder.get(postion).getAppliedPenality()) > 0) {
            rltAppliedPenality.setVisibility(View.VISIBLE);
            tvAppliedPenalityPrice.setText("-" + sessionManager.getCurrencySymbol() + trackingOrder.get(postion).getAppliedPenality());
        } else {
            rltAppliedPenality.setVisibility(View.GONE);
        }

        orders_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        orders_list.setLayoutManager(linearLayoutManager);

        receiptOrderAdapter = new ReceiptOrderAdapter(menuListModelArrayList);
        orders_list.setAdapter(receiptOrderAdapter);

        receipt_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appbar_arrow:
                onBackPressed();
                break;
            case R.id.arrow:
                onBackPressed();
                break;
            default:
                break;
        }
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

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());

        if (orderId > 0 && mSearchedLocationReferenceListener == null) {
            if (driverDetailStatus >= 3 && status >= 5) {
                addLatLngChangeListener();
            }

        }

        if (!isFromOnCreate) {
            getPushNotification(true);
        }
        isFromOnCreate = false;
        checkAllPermission(Constants.PERMISSIONS_LOCATION);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        if (mSearchedLocationReferenceListener != null) {
            query.removeEventListener(mSearchedLocationReferenceListener);
            mFirebaseDatabase.removeEventListener(mSearchedLocationReferenceListener);
            mSearchedLocationReferenceListener = null;
        } else {
            Log.e(TAG, "Driver Location data removed! Failed");
        }
    }


    /****************************************************************** */
    /**                  Animate Marker for Live Tracking               */
    /****************************************************************** */

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        //mLocationManager.removeUpdates(mLocationListener);
    }

    /**
     * To fetch status and other values of the driver
     */


    public void getData() {
        /**
         * @OrderStatus
         * 'pending' => 0, Driver accept the request
         * 'confirmed' => 1, Driver confirmed the  list and rating for restaurant
         * 'declined' => 2,  Driver declined the order list
         * 'started' => 3,  Driver start the trip (pickup the order)
         * 'delivered'  => 4, Driver drop off or delivered the order and rating for eater
         * 'completed' => 5, Driver complete the trip
         * 'cancelled' => 6, Driver or restaurant cancel the trip
         */

        /**
         * @OrderDeliveryStatus When Driver Starts the Trip
         * Order Delivery Status -1  & Status  1   Request to Restaurant
         * Order Delivery Status -1  & Status  3   Restaurant After Confirm the Order
         * Order Delivery Status  0  & Status  3   Driver accepts Request
         * Order Delivery Status  1  & Status  5   Driver Confirms Request
         * Order Delivery Status  3  & Status  5   Driver starts the Trip
         * Order Delivery Status  4  & Status  5   Driver Completes Drop Off
         * Order Delivery Status  5  & Status  6   Driver Completes Trip And Cash or Payment Successfully
         * Order Delivery Status  2  & Status  4   Cancel Order By Driver
         */

        if (!TextUtils.isEmpty(trackingOrder.get(postion).getPickupLatitude()) || !TextUtils.isEmpty(trackingOrder.get(postion).getPickupLatitude())) {
            LatLng pickupLatlng = new LatLng(Double.valueOf(trackingOrder.get(postion).getPickupLatitude()), Double.valueOf(trackingOrder.get(postion).getPickupLongitude()));
            LatLng dropLatlng = new LatLng(Double.valueOf(trackingOrder.get(postion).getDropLatitude()), Double.valueOf(trackingOrder.get(postion).getDropLongitude()));
            //
            markerPoints.add(0, pickupLatlng);
            markerPoints.add(1, dropLatlng);
            //
        }
        System.out.println("ORder Delivery statsuis " + trackingOrder.get(postion).getOrderDeliveryStatus());
        status = trackingOrder.get(postion).getOrderStatus();
        driverDetailStatus = trackingOrder.get(postion).getOrderDeliveryStatus();
        orderId = trackingOrder.get(postion).getOrderid();

        if (driverDetailStatus == -1) {
            driverDetailStatus = 0;
            System.out.println("driverDetailStatus " + trackingOrder.get(postion).getOrderDeliveryStatus());
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
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            }
        }
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            Snackbar.make(mMapView, "Unable to get GPS", Snackbar.LENGTH_SHORT).show();
        else {
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            Log.d("", String.format("getCurrentLocation(%f, %f)", location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initMap();
        if (markerPoints.size() > 0)
            //drawMarker();

            /*if (status == 1 || status == 3) {
                getRestaurantOnMap();
            } else if (status == 5) {
                LatLng driverLatlng = new LatLng(Double.valueOf(trackingOrder.get(postion).getDriverLatitude()), Double.valueOf(trackingOrder.get(postion).getDriverLongitude()));
                markerPoints.add(2, driverLatlng);
                drawMarker();
            }*/

            if (driverDetailStatus == 0 || driverDetailStatus == 1) {
                getRestaurantOnMap();
            } else if (driverDetailStatus >= 3 && status >= 5) {
                LatLng driverLatlng = new LatLng(Double.valueOf(trackingOrder.get(postion).getDriverLatitude()), Double.valueOf(trackingOrder.get(postion).getDriverLongitude()));
                markerPoints.add(2, driverLatlng);
                if (!sessionManager.getDriverUpdatedLat().equals("") && !sessionManager.getDriverUpdatedLng().equals("")) {
                    driverLatlng = new LatLng(Double.valueOf(sessionManager.getDriverUpdatedLat()), Double.valueOf(sessionManager.getDriverUpdatedLng()));
                    System.out.println("DRIVER LAT " + driverLatlng);
                    markerPoints.add(2, driverLatlng);
                }
                drawMarker();
            }

    }

    /**
     * Live tracking function
     */
    public void liveTrackingFirebase(Double driverlat, Double driverlong) {

        LatLng driverlatlng = new LatLng(driverlat, driverlong);
        System.out.println("Driver Lat Lng" + driverlatlng);
        sessionManager.setDriverUpdatedLat(String.valueOf(driverlatlng.latitude));
        sessionManager.setDriverUpdatedLng(String.valueOf(driverlatlng.longitude));

        markerPoints.set(2, driverlatlng);
        if (movepoints.size() < 1) {
            movepoints.add(0, driverlatlng);
            movepoints.add(1, driverlatlng);

        } else {
            movepoints.set(1, movepoints.get(0));
            movepoints.set(0, driverlatlng);
        }

        DecimalFormat twoDForm = new DecimalFormat("#.#######");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);
        String zerolat = twoDForm.format(((LatLng) movepoints.get(0)).latitude);
        String zerolng = twoDForm.format(((LatLng) movepoints.get(0)).longitude);

        String onelat = twoDForm.format(((LatLng) movepoints.get(1)).latitude);
        String onelng = twoDForm.format(((LatLng) movepoints.get(1)).longitude);

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(((LatLng) movepoints.get(1)).latitude);
        location.setLongitude(((LatLng) movepoints.get(1)).longitude);
        location.setTime(System.currentTimeMillis());


        float calculatedSpeed = 0;
        float distance = 0;
        if (lastLocation != null) {
            double elapsedTime = (location.getTime() - lastLocation.getTime()) / 1_000; // Convert milliseconds to seconds
            elapsedTime = Double.parseDouble(twoDForm.format(elapsedTime));

            if (elapsedTime > 0)
                calculatedSpeed = (float) (lastLocation.distanceTo(location) / elapsedTime);
            else calculatedSpeed = lastLocation.distanceTo(location) / 1;
            calculatedSpeed = Float.valueOf(twoDForm.format(calculatedSpeed));
            distance = location.distanceTo(lastLocation);

        }


        lastLocation = location;

        if (!Float.isNaN(calculatedSpeed) && !Float.isInfinite(calculatedSpeed)) {
            speed = calculatedSpeed;
        }
        if (speed <= 0) speed = 10;


        if ((!zerolat.equals(onelat) || !zerolng.equals(onelng)) && distance < 30) {
            adddefaultMarker((LatLng) movepoints.get(1), (LatLng) movepoints.get(0));
        }


    }

    /**
     * Driver Location change listener
     */
    private void addLatLngChangeListener() {

        // User data change listener
        query = mFirebaseDatabase.child(String.valueOf(orderId));

        mSearchedLocationReferenceListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (orderId > 0) {
                    DriverLocation driverLocation = dataSnapshot.getValue(DriverLocation.class);

                    // Check for null
                    if (driverLocation == null) {
                        Log.e("Map", "Driver Location data is null!");
                        return;
                    }
                    liveTrackingFirebase(Double.valueOf(driverLocation.lat), Double.valueOf(driverLocation.lng));
                } else {
                    query.removeEventListener(this);
                    mFirebaseDatabase.removeEventListener(this);
                    mFirebaseDatabase.onDisconnect();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Update route while trip (Car moving)
     */
    public void drawRoute(LatLng driverlatlng) {

        LatLng pickupLatlng = (LatLng) markerPoints.get(0);
        final LatLng dropLatlng = (LatLng) markerPoints.get(1);


        // Add new marker to the Google Map Android API V2

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //the include method will calculate the min and max bound.
        builder.include(driverlatlng);
        if (status >= 3) {
            builder.include(dropLatlng);
        } else {
            builder.include(pickupLatlng);
        }

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels / 2;
        int height = getResources().getDisplayMetrics().heightPixels / 2;
        int padding = (int) (width * 0.08); // offset from edges of the map 10% of screen

        if (isFirst) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mGoogleMap.moveCamera(cu);
            isFirst = false;
        }

        String url;
        if (status >= 3) {
            // Getting URL to the Google Directions API
            url = getDirectionsUrl(driverlatlng, dropLatlng);
        } else {
            url = getDirectionsUrl(driverlatlng, pickupLatlng);
        }
        DownloadTask downloadTask = new DownloadTask(new PolylineOptionsInterface() {
            @Override
            public void getPolylineOptions(PolylineOptions output, ArrayList points, String duration) {

                if (mGoogleMap != null && output != null) {
                    mGoogleMap.addMarker(new MarkerOptions().position(dropLatlng).anchor(0, 1f).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.dest_dot, 2, R.layout.estimation_time_layout, duration))));

                    //mGoogleMap.addMarker(createMarker(R.drawable.dest_dot, dropLatlng, 2, R.layout.estimation_time_layout, duration));
                    if (polyline != null) polyline.remove();
                    polyline = mGoogleMap.addPolyline(output);
                }
            }
        });
        Log.v("Downloadd", "task");
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    /**
     * Update route while trip (Car moving)
     */
    public void drawMarker() {
        if (mGoogleMap == null) return;
        mGoogleMap.clear();
        LatLng pickuplatlng = (LatLng) markerPoints.get(0);
        LatLng droplatlng = (LatLng) markerPoints.get(1);
        LatLng driverlatlng = (LatLng) markerPoints.get(2);


        // Creating MarkerOptions
        MarkerOptions pickupOptions = new MarkerOptions();

        // Setting the position of the marker

        if (status >= 3) {
            pickupOptions.position(droplatlng);
            //pickupOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dest_dot));
            //mGoogleMap.addMarker(new MarkerOptions().position(droplatlng).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.dest_dot, 2,R.layout.estimation_time_layout))));
        } else {
            pickupOptions.position(pickuplatlng);
            pickupOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ub__ic_pin_pickup));
            mGoogleMap.addMarker(pickupOptions);
        }
        // Add new marker to the Google Map Android API V2


        // Creating MarkerOptions
        MarkerOptions dropOptions = new MarkerOptions();


        // Setting the position of the marker
        dropOptions.position(driverlatlng);
        dropOptions.anchor(0.5f, 0.5f);
        dropOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gf_moto_bike));
        // Add new marker to the Google Map Android API V2
        carmarker = mGoogleMap.addMarker(dropOptions);

        drawRoute(driverlatlng);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(driverlatlng).zoom(16.5f).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * get direction for given origin, dest
     */
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=" + getResources().getString(R.string.google_maps_key);

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        System.out.println("GEt Url " + url);
        return url;
    }

    /**
     * Move marker for given locations
     **/
    public void adddefaultMarker(LatLng latlng, LatLng latlng1) {
        Location startbearlocation = new Location(LocationManager.GPS_PROVIDER);
        Location endbearlocation = new Location(LocationManager.GPS_PROVIDER);

        startbearlocation.setLatitude(latlng.latitude);
        startbearlocation.setLongitude(latlng.longitude);

        endbearlocation.setLatitude(latlng1.latitude);
        endbearlocation.setLongitude(latlng1.longitude);

        if (endbear != 0.0) {
            startbear = endbear;
        }

        carmarker.setFlat(true);
        carmarker.setAnchor(0.5f, 0.5f);
        marker = carmarker;
        // Move map while marker gone
        ensureMarkerOnBounds(latlng, "updated");

        endbear = (float) bearing(startbearlocation, endbearlocation);
        endbear = (float) (endbear * (180.0 / 3.14));

        double distance = Double.valueOf(twoDForm.format(startbearlocation.distanceTo(endbearlocation)));

        if (distance > 0 && distance < 30) animateMarker(latlng1, marker, speed, endbear);
        twoDForm = new DecimalFormat("#.####");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);
        String oldlat = twoDForm.format(latlng.latitude);
        double ola = Double.valueOf(oldlat);
        String oldlong = twoDForm.format(latlng.longitude);
        double olo = Double.valueOf(oldlong);
        String newlat = twoDForm.format(latlng1.latitude);
        double nla = Double.valueOf(newlat);
        String newlong = twoDForm.format(latlng1.longitude);
        double nlo = Double.valueOf(newlong);

        if (ola != nla && olo != nlo) {
            drawRoute((LatLng) movepoints.get(1));
        }
    }

    /**
     * Move marker
     **/
    public void animateMarker(final LatLng destination, final Marker marker, final float speed, final float endbear) {

        final LatLng[] newPosition = new LatLng[1];
        final LatLng[] oldPosition = new LatLng[1];
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);
            long duration;
            Location newLoc = new Location(LocationManager.GPS_PROVIDER);
            newLoc.setLatitude(startPosition.latitude);
            newLoc.setLongitude(startPosition.longitude);
            Location prevLoc = new Location(LocationManager.GPS_PROVIDER);
            prevLoc.setLatitude(endPosition.latitude);
            prevLoc.setLongitude(endPosition.longitude);

            final double distance = Double.valueOf(twoDForm.format(newLoc.distanceTo(prevLoc)));

            duration = (long) ((distance / speed) * 950);
            duration = 2000;
            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            if (valueAnimator != null) {
                valueAnimator.cancel();
                valueAnimator.end();
            }
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(duration);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        newPosition[0] = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition[0]); // Move Marker
                        marker.setRotation(computeRotation(v, startRotation, endbear)); // Rotate Marker
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();

        }
    }

    /**
     * Find GPS rotate position
     **/
    private double bearing(Location startPoint, Location endPoint) {
        double deltaLongitude = endPoint.getLongitude() - startPoint.getLongitude();
        double deltaLatitude = endPoint.getLatitude() - startPoint.getLatitude();
        double angle = (3.14 * .5f) - Math.atan(deltaLatitude / deltaLongitude);

        if (deltaLongitude > 0) return angle;
        else if (deltaLongitude < 0) return angle + 3.14;
        else if (deltaLatitude < 0) return 3.14;

        return 0.0f;
    }

    /**
     * move map to center position while marker hide
     **/
    private void ensureMarkerOnBounds(LatLng toPosition, String type) {
        if (marker != null) {
            float currentZoomLevel = mGoogleMap.getCameraPosition().zoom;
            float bearing = mGoogleMap.getCameraPosition().bearing;

            CameraPosition cameraPosition = new CameraPosition.Builder().target(toPosition).zoom(currentZoomLevel).bearing(bearing).build();

            if ("updated".equals(type)) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                if (!mGoogleMap.getProjection().getVisibleRegion().latLngBounds.contains(toPosition)) {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }
    }

    /**
     * Check user allow all permission and ask permission to allow
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(DriverTrackingActivity.this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(DriverTrackingActivity.this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(DriverTrackingActivity.this, permission, 150);
            }
        } else {
            checkGpsEnable();
        }
    }

    /**
     * Get permission result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);
        } else {
            checkGpsEnable();
        }
    }

    /**
     * If any one or more permission is deny or block show the enable permission dialog
     */
    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(message, getString(R.string.ok), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0) callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * Open permission dialog
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Check GPS enable or not
     */
    private void checkGpsEnable() {
        boolean isGpsEnabled = MyLocation.defaultHandler().isLocationAvailable(this);
        if (!isGpsEnabled) {

            showEnablePermissionDailog(1, getString(R.string.please_enable_location));
        } else {
            MyLocation.defaultHandler().getLocation(this, locationResult);
            getCurrentLocation();
        }
    }

    /**
     * Get Restaurant On Map
     */

    public void getRestaurantOnMap() {
        mGoogleMap.clear();
        LatLng restaurantLatLng = (LatLng) markerPoints.get(0);       //PickUp latlng
        LatLng drop = (LatLng) markerPoints.get(1);

        // adding a marker on map with image from  drawable
        mGoogleMap.addMarker(new MarkerOptions().position(restaurantLatLng).anchor(0, 1f).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.dest_dot, 1, R.layout.marker_dialog, ""))));

        //mGoogleMap.addMarker(createMarker(R.drawable.dest_dot, restaurantLatLng, 1, R.layout.marker_dialog, ""));
        // adding a marker on map with image from  drawable
        mGoogleMap.addMarker(new MarkerOptions().position(drop).anchor(0, 1f).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.pickup_dot, 0, R.layout.marker_dialog, ""))));

        //mGoogleMap.addMarker(createMarker(R.drawable.pickup_dot, drop, 0, R.layout.marker_dialog, ""));

        drawRouteRest();
    }

    /**
     * Place Layout as a Marker as bitmap In map
     *
     * @param resId  Drawable Image
     * @param type   Marker type
     * @param layout MarKer Layout
     * @param eta    Estimation Time When Driver accept the trip
     * @return Marker
     */
    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId, int type, int layout, String eta) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout, null);
        ImageView markerImageView = customMarkerView.findViewById(R.id.ivMarker);
        LinearLayout llt_estimation = customMarkerView.findViewById(R.id.llt_estimation);
        LinearLayout lltLocation = customMarkerView.findViewById(R.id.lltLocation);
        LinearLayout llt_marker = customMarkerView.findViewById(R.id.llt_marker);
        markerImageView.setImageResource(resId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (type == 2 && !"".equals(eta)) {
            CustomTextView tvTime = customMarkerView.findViewById(R.id.tvTime);
            CustomTextView tvEta = customMarkerView.findViewById(R.id.tvEta);
            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
                tvTime.setGravity(Gravity.START);
                tvEta.setGravity(Gravity.START);
                llt_estimation.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            String ETA[] = eta.split(" ", 2);
            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
                tvEta.setText(getResources().getString(R.string.mins)+"\n"+getResources().getString(R.string.min_neta));
                tvTime.setText(ETA[0]);
            }
            else {
                tvTime.setText(ETA[0]);
                tvEta.setText(getResources().getString(R.string.min_neta));
            }
            params.gravity = Gravity.START;
        } else {
            CustomTextView tv_address = customMarkerView.findViewById(R.id.tvLocation);
            CustomTextView tvOrderID = customMarkerView.findViewById(R.id.tvOrderID);
            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
                tv_address.setGravity(Gravity.START);
                tvOrderID.setGravity(Gravity.START);
                llt_marker.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            if (type == 1) {
                tv_address.setText(trackingOrder.get(postion).getName());
                tvOrderID.setVisibility(View.VISIBLE);
                tvOrderID.setText(getResources().getString(R.string.order_id) + " " + String.valueOf(trackingOrder.get(postion).getOrderid()));
                params.gravity = Gravity.START;
            } else if (type == 0) {
                tv_address.setText(sessionManager.getLocation());
                tvOrderID.setVisibility(View.GONE);
                params.gravity = Gravity.START;
            }
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
     * @param resId  Drawable Image
     * @param point  Latlng
     * @param type   Marker type
     * @param layout MarKer Layout
     * @param eta    Estimation Time When Driver accept the trip
     * @return Marker
     */
    public MarkerOptions createMarker(@DrawableRes int resId, LatLng point, int type, int layout, String eta) {

        MarkerOptions marker = new MarkerOptions();
        marker.position(point);
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout, null);
        ImageView markerImageView = customMarkerView.findViewById(R.id.ivMarker);
        LinearLayout lltLocation = customMarkerView.findViewById(R.id.lltLocation);
        markerImageView.setImageResource(resId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        System.out.println("GET ETA " + eta);
        if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
            //  llt_estimation.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            lltLocation.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        if (type == 2 && !"".equals(eta)) {
            CustomTextView tvTime = customMarkerView.findViewById(R.id.tvTime);
            CustomTextView tvEta = customMarkerView.findViewById(R.id.tvEta);
            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
                tvTime.setGravity(Gravity.START);
                tvEta.setGravity(Gravity.START);
            }
            String ETA[] = eta.split(" ", 2);
            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
                tvEta.setText(getResources().getString(R.string.mins)+"\n"+getResources().getString(R.string.min_neta));
                tvTime.setText(ETA[0]);
            }
            else {
                tvTime.setText(ETA[0]);
                tvEta.setText(getResources().getString(R.string.min_neta));
            }
        } else {
            CustomTextView tv_address = customMarkerView.findViewById(R.id.tvLocation);
            CustomTextView tvOrderID = customMarkerView.findViewById(R.id.tvOrderID);
            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
                tv_address.setGravity(Gravity.START);
                tvOrderID.setGravity(Gravity.START);
            }
            if (type == 1) {
                tv_address.setText(trackingOrder.get(postion).getName());
                tvOrderID.setVisibility(View.VISIBLE);
                tvOrderID.setText(getResources().getString(R.string.order_id) + " " + String.valueOf(trackingOrder.get(postion).getOrderid()));
                params.gravity = Gravity.START;
            } else if (type == 0) {
                tv_address.setText(sessionManager.getLocation());
                tvOrderID.setVisibility(View.GONE);
                params.gravity = Gravity.START;
            }
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

    public void drawRouteRest() {

        LatLng pickupLatlng = (LatLng) markerPoints.get(0);
        LatLng dropLatlng = (LatLng) markerPoints.get(1);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        builder.include(dropLatlng);

        builder.include(pickupLatlng);


        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels / 2;
        int height = getResources().getDisplayMetrics().heightPixels / 2;
        int padding = (int) (width * 0.08); // offset from edges of the map 10% of screen


        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mGoogleMap.moveCamera(cu);

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

                    getPushNotification(false);
                }
            }
        };
    }

    private void getPushNotification(boolean isResume) {
        String JSON_DATA = sessionManager.getPushNotification();

        try {
            JSONObject jsonObject = new JSONObject(JSON_DATA);
            System.out.println("jsonObjectPush Driver Tracking" + jsonObject);
            if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_accepted")) {
                rltCancel.setVisibility(View.GONE);
            } else if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_cancelled") || jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delivery_completed")
                                /*||jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("Refund")*/) {
                MainActivity.cancelled = true;
                Intent order = new Intent(getApplicationContext(), MainActivity.class);
                order.putExtra("type", "placeorder");
                startActivity(order);
            } else if (jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delayed") || jsonObject.getJSONObject("custom").has("type") && jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delivery_started")) {
                MainActivity.cancelled = false;
                Intent order = new Intent(getApplicationContext(), MainActivity.class);
                order.putExtra("type", "placeorder");
                startActivity(order);
            }

            if (isResume) sessionManager.setPushNotification("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}