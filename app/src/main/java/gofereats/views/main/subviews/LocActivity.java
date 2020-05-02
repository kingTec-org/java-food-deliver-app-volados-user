package gofereats.views.main.subviews;
/**
 *
 * @package com.trioangle.gofereats
 * @subpackage views.subviews
 * @category LocActivity
 * @author Trioangle Product Team
 * @version 1.0
 */


/*************************************************************
 Location search
 ************************************************************/

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomEditText;
import com.obs.CustomTextView;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import gofereats.R;
import gofereats.adapters.main.LocationListAdapter;
import gofereats.configs.AppController;
import gofereats.configs.Constants;
import gofereats.configs.RunTimePermission;
import gofereats.configs.SessionManager;
import gofereats.datamodels.location.GetLocationModel;
import gofereats.datamodels.location.LocationList;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.placesearch.PlacesAutoCompleteAdapter;
import gofereats.utils.CommonMethods;
import gofereats.utils.GoogleMapPlaceSearchAutoCompleteRecyclerView;
import gofereats.utils.MyLocation;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;

import static gofereats.utils.Enums.REQ_CLEAR_AND_CHANGE_LOCATION;
import static gofereats.utils.Enums.REQ_GET_SAVED_LOCATION;
import static gofereats.utils.Enums.REQ_REMOVE_LOCATION;
import static gofereats.utils.Enums.REQ_SET_DEFAULT;
import static gofereats.utils.Enums.REQ_UPDATE_LOCATION;


public class LocActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ServiceListener, GoogleMapPlaceSearchAutoCompleteRecyclerView.AutoCompleteAddressTouchListener {

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));
    public static ArrayList<LocationList> locationModels = new ArrayList<>();
    public final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(LocActivity.class.getName());
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    public @Inject
    RunTimePermission runTimePermission;
    public RelativeLayout asap_lay;
    public RelativeLayout schedule_lay;
    public RelativeLayout meet_at_vehicle_lay;
    public RelativeLayout delivery_to_you_layout;
    public RelativeLayout deliverynote_lay;
    public RelativeLayout edits;
    public RelativeLayout delivery_notes;
    public RelativeLayout order_time;
    public ImageView checked_asap;
    public ImageView checked_sch;
    public ImageView delivery_check_tick;
    public ImageView meet_at_vehicle_check_tick;
    public ImageView ivClosedeliveryNote1;
    public ImageView ivCloseapartment;
    public ImageView ivClosedeliveryNote2;
    public CustomEditText deliveryaddress;
    public CustomEditText etdAdddeliverynote1;
    public CustomEditText edtAddapartment;
    public CustomEditText add_business;
    public CustomEditText edtAdddoordelivery;
    public RecyclerView location_placesearch;
    public RecyclerView rcLocation_list;
    public ImageView close;
    public CustomTextView when_text;
    public CustomTextView tvMain_address;
    public CustomTextView tvSub_address;
    public GoogleApiClient googleApiClient;
    public String oldstring = "";
    public String searchlocation;
    public String clickedLocation = "";
    public int isDefault = 0;
    public String lat;
    public String log;
    public String deliverycountry;
    public String fullAddress;
    public CustomButton save_button;
    public LinearLayout set_sch_layout;
    public CustomTextView dates;
    public CustomTextView times;
    public String add;
    public ImageView arrow;
    public String savedeliverylocation;
    public LatLng latLngdelivery;
    public LatLng current_latlng;
    public boolean isFirstClick = false;
    public int getPostion = 0;
    public LocationListAdapter adapter;
    public String newTime;
    public String newTimer;
    public String day;
    public String minutea;
    public boolean Checksave = false;
    public List<Address> addressList = null; // Addresslist
    public String address = null; // Address
    public String countrys = null; // Country name
    public LatLng getLocations = null;
    public String getAddress = null;
    public String getcity;
    public String getstate;
    public String getcountry;
    public String getpostal;
    public String getstreet;
    public String getcountryCode;
    public String currentlocation1;
    public String curremtlocation2;
    public LinearLayout lltWhen;
    public LinearLayout lltlocationlist;
    public LinearLayout lltCurrentLocation;
    public int deliverynoteType = 0;
    public String deliverynotes;
    public GetLocationModel getLocationModel;
    public RelativeLayout rltWholeview;
    public RelativeLayout currentlocation;
    public String todayDate;
    public String compareDate;
    public int counti;
    // Text watch for ApartmentAddress
    protected TextWatcher apartmentTextWatcher = new TextWatcher() {

        int count = 0;

        public void onTextChanged(final CharSequence s, int start, int before, int count) {


            if (s.toString().equals("")) {
                ivCloseapartment.setVisibility(View.GONE);
            } else {
                ivCloseapartment.setVisibility(View.VISIBLE);
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.toString().equals("")) {
                ivCloseapartment.setVisibility(View.GONE);
            } else {
                ivCloseapartment.setVisibility(View.VISIBLE);
            }

        }

        public void afterTextChanged(final Editable s) {

            if (edtAddapartment.hasFocus()) {
                if (s.toString().equals("")) {
                    ivCloseapartment.setVisibility(View.GONE);
                } else {
                    ivCloseapartment.setVisibility(View.VISIBLE);
                }
            }

        }
    };
    // Text watch for Delivery Address
    protected TextWatcher DeliveryNoteTextWatcher = new TextWatcher() {

        int count = 0;

        public void onTextChanged(final CharSequence s, int start, int before, int count) {


            if (s.toString().equals("")) {
                ivClosedeliveryNote1.setVisibility(View.GONE);
            } else {
                ivClosedeliveryNote1.setVisibility(View.VISIBLE);
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.toString().equals("")) {
                ivClosedeliveryNote1.setVisibility(View.GONE);
            } else {
                ivClosedeliveryNote1.setVisibility(View.VISIBLE);
            }

        }

        public void afterTextChanged(final Editable s) {

            if (etdAdddeliverynote1.hasFocus()) {
                if (s.toString().equals("")) {
                    ivClosedeliveryNote1.setVisibility(View.GONE);
                } else {
                    ivClosedeliveryNote1.setVisibility(View.VISIBLE);
                }
            }

        }
    };
    // Text watch for DoorDeliveryAddress
    protected TextWatcher AdddoordeliveryTextWatcher = new TextWatcher() {

        int count = 0;

        public void onTextChanged(final CharSequence s, int start, int before, int count) {


            if (s.toString().equals("")) {
                ivClosedeliveryNote2.setVisibility(View.GONE);
            } else {
                ivClosedeliveryNote2.setVisibility(View.VISIBLE);

            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.toString().equals("")) {
                ivClosedeliveryNote2.setVisibility(View.GONE);
            } else {
                ivClosedeliveryNote2.setVisibility(View.VISIBLE);

            }

        }

        public void afterTextChanged(final Editable s) {

            if (edtAdddoordelivery.hasFocus()) {

                if (s.toString().equals("")) {
                    ivClosedeliveryNote2.setVisibility(View.GONE);
                } else {
                    ivClosedeliveryNote2.setVisibility(View.VISIBLE);

                }
            }

        }
    };
    private PlacesAutoCompleteAdapter AutoCompleteAdapter;
    private boolean isPermissionGranted = false;
    private int type;
    private boolean datevalid = true;
    private int delivery_options = 0;
    private AlertDialog dialog;
    private CustomButton btnRemoveLocation;
    private String setAddress;
    private String area;
    private String second_address;
    private String mCancel;
    private int mNewLocation = 0;
    private int orderType = 0;
    private String deliveryTime;
    private String add30;
    private String scheduledDay;
    private boolean currentLocation = false;

    PlacesClient placesClient;
    AutocompleteSessionToken googleAutoCompleteToken;
    GoogleMapPlaceSearchAutoCompleteRecyclerView googleMapPlaceSearchAutoCompleteRecyclerView;

    private List<AutocompletePrediction> addressAutoCompletePredictions = new ArrayList<>();
    // Text watch for destination address
    protected TextWatcher deliveryTextwatcher = new TextWatcher() {

        int count = 0;

        public void onTextChanged(final CharSequence s, int start, int before, int count) {

            if (s.toString().equals("")) {
                viewChanger(savedeliverylocation);
            } else {
                close.setVisibility(View.VISIBLE);
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            if (s.toString().equals("")) {
                viewChanger(savedeliverylocation);
            } else {
                close.setVisibility(View.VISIBLE);
            }
        }

        public void afterTextChanged(final Editable s) {

            if (deliveryaddress.hasFocus()) {
                save_button.setEnabled(false);
                if (!s.toString().equals("") && googleApiClient.isConnected()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (!oldstring.equals(s.toString())) {
                                oldstring = s.toString();
                                count++;

                                if (!s.toString().equals("")) {
                                    //AutoCompleteAdapter.getFilter().filter(s.toString());  // Place search
                                    oldstring = s.toString();
                                    //mAutoCompleteAdapter.getFilter().filter(s.toString()); // Place search
                                    getFullAddressUsingEdittextStringFromGooglePlaceSearchAPI(s.toString());
                                    location_placesearch.setVisibility(View.VISIBLE);

                                    lltCurrentLocation.setVisibility(View.GONE);
                                    rcLocation_list.setVisibility(View.GONE);
                                    when_text.setVisibility(View.GONE);
                                    order_time.setVisibility(View.GONE);
                                    delivery_notes.setVisibility(View.GONE);
                                    btnRemoveLocation.setVisibility(View.GONE);
                                }
                            }
                        }
                    }, 1000);

                } else if (!googleApiClient.isConnected()) {
                    Log.e("API CLIENT", "NOTCONNECTED");
                }

                if (s.toString().equals("")) {
                    viewChanger(savedeliverylocation);
                } else {
                    close.setVisibility(View.VISIBLE);
                }
            } else {
                clearAddressAndHideRecyclerView();
            }

        }
    };
    /**
     * Get user current location
     */
    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            if (location == null) return;
            current_latlng = new LatLng(location.getLatitude(), location.getLongitude());
            fetchAddress(current_latlng, "currentloation");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();  // Connect Google API client
        setContentView(R.layout.activity_location);
        AppController.getAppComponent().inject(this);


        intViews();
        getCurrentDate();

        savedeliverylocation = getIntent().getStringExtra("location");
        type = getIntent().getIntExtra("type", 0);

        getUserSavedLocation();// Api Calling method to fetch user saved location

        onClickViews();

        //enableViews();


        checked_asap.setVisibility(View.VISIBLE);
        checked_sch.setVisibility(View.INVISIBLE);


        deliveryaddress.addTextChangedListener(deliveryTextwatcher);
        if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            deliveryaddress.setGravity(Gravity.END);
            deliveryaddress.setTextDirection(View.TEXT_DIRECTION_LTR);
            etdAdddeliverynote1.setGravity(Gravity.END);
            etdAdddeliverynote1.setTextDirection(View.TEXT_DIRECTION_LTR);
            edtAddapartment.setGravity(Gravity.END);
            edtAddapartment.setTextDirection(View.TEXT_DIRECTION_LTR);
            edtAdddoordelivery.setGravity(Gravity.END);
            edtAdddoordelivery.setTextDirection(View.TEXT_DIRECTION_LTR);
        }
        edtAddapartment.addTextChangedListener(apartmentTextWatcher);
        etdAdddeliverynote1.addTextChangedListener(DeliveryNoteTextWatcher);
        edtAdddoordelivery.addTextChangedListener(AdddoordeliveryTextWatcher);


        /**
         * Auto complete Place search adapter Starts
         */
        AutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.location_search, googleApiClient, BOUNDS_INDIA, null);


        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        googleAutoCompleteToken = AutocompleteSessionToken.newInstance();
        googleMapPlaceSearchAutoCompleteRecyclerView = new GoogleMapPlaceSearchAutoCompleteRecyclerView(addressAutoCompletePredictions, this, this);
        // Place search list
        location_placesearch = findViewById(R.id.location_placesearch);
        location_placesearch.setVisibility(View.GONE);
        location_placesearch.setHasFixedSize(true);
        location_placesearch.setNestedScrollingEnabled(false);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        location_placesearch.setLayoutManager(mLinearLayoutManager);
        location_placesearch.setAdapter(googleMapPlaceSearchAutoCompleteRecyclerView);


    }

    private void onClickViews() {

        meet_at_vehicle_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliverynoteType = 0;
                delivery_options = 0;
                deliverynote_lay.setVisibility(View.VISIBLE);
                edits.setVisibility(View.GONE);
                delivery_check_tick.setVisibility(View.GONE);
                meet_at_vehicle_check_tick.setVisibility(View.VISIBLE);
            }
        });

        delivery_to_you_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliverynoteType = 1;
                delivery_options = 1;
                edits.setVisibility(View.VISIBLE);
                deliverynote_lay.setVisibility(View.GONE);
                delivery_check_tick.setVisibility(View.VISIBLE);
                meet_at_vehicle_check_tick.setVisibility(View.GONE);
            }
        });

        commonMethods.rotateArrow(arrow, this);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /**
         * set Current Address
         */
        lltCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area = tvMain_address.getText().toString().trim();
                second_address = tvSub_address.getText().toString().trim();
                deliveryaddress.setText(area);
                deliveryaddress.setSelection(deliveryaddress.getText().length());
                latLngdelivery = current_latlng;
                lltlocationlist.setVisibility(View.GONE);
                lltWhen.setVisibility(View.GONE);
                delivery_notes.setVisibility(View.VISIBLE);
                btnRemoveLocation.setVisibility(View.GONE);
                save_button.setVisibility(View.VISIBLE);
                save_button.setEnabled(true);
            }
        });


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (orderType == 0) {
                    sessionManager.setDeliveredTime("");
                    sessionManager.setHomeScheduledDay("");
                    sessionManager.setScheduleMin("");
                    sessionManager.setScheduleDate("");
                    sessionManager.setScheduledDay("");
                    sessionManager.setAddedTime("");
                    deliveryTime = "";
                } else {
                    deliveryTime = sessionManager.getDeliveredTime();
                }

                if (deliverynoteType == 0) {
                    deliverynotes = etdAdddeliverynote1.getText().toString().trim();
                    etdAdddeliverynote1.setSelection(etdAdddeliverynote1.getText().length());
                } else {
                    deliverynotes = edtAdddoordelivery.getText().toString().trim();
                }
                if (deliveryaddress.getText().toString().isEmpty()) {
                    if (locationModels != null && locationModels.size() > 0) {
                        sessionManager.setOrderType(orderType);
                        setDefault(isDefault);
                    } else {
                        Toast.makeText(LocActivity.this, getResources().getString(R.string.enter_location), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sessionManager.setOrderType(orderType);
                    updateSavedLocation();
                }

            }
        });

        set_sch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime(); // showtime method call
            }
        });

        btnRemoveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeLocation(type);
            }
        });

        asap_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asapView();
            }
        });

        schedule_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduledViews();

            }
        });


        /**
         * Clear text
         */
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close.setVisibility(View.GONE);
                deliveryaddress.getText().clear();
                enableViews();
            }
        });
        ivClosedeliveryNote1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etdAdddeliverynote1.getText().clear();
            }
        });
        ivCloseapartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAddapartment.getText().clear();
            }
        });
        ivClosedeliveryNote2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAdddoordelivery.getText().clear();
            }
        });


        // Place search list click
        /*location_placesearch.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PlacesAutoCompleteAdapter.PlaceAutocomplete item = AutoCompleteAdapter.getItem(position);
                *//*if (counti > 0) {
                    item = null;
                }*//*
         *//*if (item != null) {
                    counti++;
                }*//*
                AutoCompleteAdapter.clear();
                Log.i("TAG", "Autocomplete item selected: " + item.description);


                searchlocation = (String) item.description;

                deliveryaddress.setText(item.addresss);
                deliveryaddress.setSelection(deliveryaddress.getText().length());
                oldstring = item.addresss.toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(deliveryaddress.getWindowToken(), 0);
                save_button.setEnabled(false);
                fetchLocation(searchlocation, "");
                clickedLocation = (String) item.description;

                location_placesearch.setVisibility(View.GONE);
                lltlocationlist.setVisibility(View.GONE);
                delivery_notes.setVisibility(View.VISIBLE);
                lltWhen.setVisibility(View.GONE);

                area = (String) item.addresss;
                second_address = (String) item.subaddress;
                save_button.setVisibility(View.VISIBLE);
            }
        }));*/


    }

    public void asapView() {
        orderType = 0;
        Checksave = false;
        checked_asap.setVisibility(View.VISIBLE);
        checked_sch.setVisibility(View.INVISIBLE);
        set_sch_layout.setVisibility(View.GONE);
        save_button.setEnabled(true);
        save_button.setBackgroundColor(getResources().getColor(R.color.apptheme));
    }

    public void scheduledViews() {
        checked_asap.setVisibility(View.INVISIBLE);
        checked_sch.setVisibility(View.VISIBLE);
        set_sch_layout.setVisibility(View.VISIBLE);
        if (!Checksave) {
            dates.setText(getResources().getString(R.string.schedule_date));
            times.setText(getResources().getString(R.string.set_time));
            dates.setTextColor(getResources().getColor(R.color.dim_black));
            times.setTextColor(getResources().getColor(R.color.dim_black));
            save_button.setEnabled(false);
            save_button.setBackgroundColor(getResources().getColor(R.color.payment_method));
        }
    }

    /**
     * Enable Views
     */

    public void enableViews() {
        if (savedeliverylocation == null || savedeliverylocation.equals("")) {
            savedeliverylocation = "";
        }

        if ((("edit").equals(savedeliverylocation) || ("change").equals(savedeliverylocation)) && sessionManager.getOrderType() == 1) {
            orderType = 1;
            checked_asap.setVisibility(View.INVISIBLE);
            checked_sch.setVisibility(View.VISIBLE);
            set_sch_layout.setVisibility(View.VISIBLE);
            dates.setText(sessionManager.getScheduleDate());
            times.setText(sessionManager.getAddedTime());
            dates.setTextColor(getResources().getColor(R.color.apptheme));
            times.setTextColor(getResources().getColor(R.color.apptheme));
        }

        if (("edit").equals(savedeliverylocation) || "entry".equals(savedeliverylocation) || "change".equals(savedeliverylocation)) {
            lltlocationlist.setVisibility(View.VISIBLE);
            //lltCurrentLocation.setVisibility(View.VISIBLE);
            lltWhen.setVisibility(View.VISIBLE);
            delivery_notes.setVisibility(View.GONE);
            btnRemoveLocation.setVisibility(View.GONE);
            getCurrentLocation();
        }

        if ("home".equals(savedeliverylocation) || "work".equals(savedeliverylocation)) {

            if (type == 0) {
                if (locationModels.size() < 0) {
                    System.out.println("List Siz<0 " + locationModels.size());
                } else {
                    for (int i = 0; i < locationModels.size(); i++) {
                        if (locationModels.get(i).getType() == 0) {
                            deliveryaddress.setClickable(false);
                            deliveryaddress.setFocusable(false);
                            deliveryaddress.setText(locationModels.get(i).getFirstAddress());
                            deliveryaddress.setSelection(deliveryaddress.getText().length());
                            area = locationModels.get(i).getFirstAddress();
                            second_address = locationModels.get(i).getSecondAddress();
                            lltlocationlist.setVisibility(View.GONE);
                            lltCurrentLocation.setVisibility(View.GONE);
                            lltWhen.setVisibility(View.GONE);
                            btnRemoveLocation.setVisibility(View.VISIBLE);

                            delivery_notes.setVisibility(View.VISIBLE);

                            latLngdelivery = new LatLng(locationModels.get(i).getLatitude(), locationModels.get(i).getLongitude());
                            fetchAddress(latLngdelivery, "");
                            deilveryzero(i);
                            break;
                        } else {
                            save_button.setVisibility(View.GONE);
                            delivery_notes.setVisibility(View.GONE);
                            lltlocationlist.setVisibility(View.VISIBLE);
                            lltCurrentLocation.setVisibility(View.VISIBLE);
                            rcLocation_list.setVisibility(View.GONE);
                            lltWhen.setVisibility(View.GONE);
                            btnRemoveLocation.setVisibility(View.GONE);
                        }

                    }

                }

            }

            if (type == 1 && locationModels.size() > 0) {
                for (int i = 0; i < locationModels.size(); i++) {
                    if (locationModels.get(i).getType() == 1) {
                        deliveryaddress.setClickable(false);
                        deliveryaddress.setFocusable(false);
                        deliveryaddress.setText(locationModels.get(i).getFirstAddress());
                        deliveryaddress.setSelection(deliveryaddress.getText().length());
                        area = locationModels.get(i).getFirstAddress();
                        second_address = locationModels.get(i).getSecondAddress();
                        delivery_notes.setVisibility(View.VISIBLE);
                        lltlocationlist.setVisibility(View.GONE);
                        lltWhen.setVisibility(View.GONE);
                        btnRemoveLocation.setVisibility(View.VISIBLE);
                        save_button.setVisibility(View.VISIBLE);

                        latLngdelivery = new LatLng(locationModels.get(i).getLatitude(), locationModels.get(i).getLongitude());
                        fetchAddress(latLngdelivery, "");

                        deilveryzero(i);
                        break;
                    } else {
                        save_button.setVisibility(View.GONE);
                        delivery_notes.setVisibility(View.GONE);
                        lltlocationlist.setVisibility(View.VISIBLE);
                        lltCurrentLocation.setVisibility(View.VISIBLE);
                        rcLocation_list.setVisibility(View.GONE);
                        lltWhen.setVisibility(View.GONE);
                        btnRemoveLocation.setVisibility(View.GONE);
                    }

                }
            }

        }
        if ("notes".equals(savedeliverylocation)) {
            for (int i = 0; i < locationModels.size(); i++) {
                if (locationModels.get(i).getDefaultaddress() == 1) {
                    deliveryaddress.setClickable(false);
                    deliveryaddress.setFocusable(false);
                    deliveryaddress.setText(locationModels.get(i).getFirstAddress());
                    deliveryaddress.setSelection(deliveryaddress.getText().length());
                    area = locationModels.get(i).getFirstAddress();
                    second_address = locationModels.get(i).getSecondAddress();
                    lltlocationlist.setVisibility(View.GONE);
                    lltCurrentLocation.setVisibility(View.GONE);
                    lltWhen.setVisibility(View.GONE);
                    delivery_notes.setVisibility(View.VISIBLE);
                    btnRemoveLocation.setVisibility(View.GONE);
                    save_button.setVisibility(View.VISIBLE);
                    latLngdelivery = new LatLng(locationModels.get(i).getLatitude(), locationModels.get(i).getLongitude());
                    fetchAddress(latLngdelivery, "");
                    deilveryzero(i);
                    break;
                } /*else {
                    delivery_notes.setVisibility(View.GONE);
                    lltlocationlist.setVisibility(View.VISIBLE);
                    lltCurrentLocation.setVisibility(View.VISIBLE);
                    rcLocation_list.setVisibility(View.GONE);
                    lltWhen.setVisibility(View.GONE);
                    btnRemoveLocation.setVisibility(View.GONE);
                }*/

            }
        }
    }

    private void deilveryzero(int i) {
        if (locationModels.get(i).getDeliveryoptions() == 0) {
            deliverynoteType = 0;
            delivery_options = locationModels.get(i).getDeliveryoptions();
            deliverynote_lay.setVisibility(View.VISIBLE);
            edits.setVisibility(View.GONE);
            delivery_check_tick.setVisibility(View.GONE);
            meet_at_vehicle_check_tick.setVisibility(View.VISIBLE);
            if (locationModels.get(i).getDeliverynote() == null) {
                etdAdddeliverynote1.setText("");
                etdAdddeliverynote1.setSelection(etdAdddeliverynote1.getText().length());
            } else {
                etdAdddeliverynote1.setText(locationModels.get(i).getDeliverynote());
                etdAdddeliverynote1.setSelection(etdAdddeliverynote1.getText().length());
            }

        } else {
            deliverynoteType = 1;
            delivery_options = locationModels.get(i).getDeliveryoptions();
            edits.setVisibility(View.VISIBLE);
            deliverynote_lay.setVisibility(View.GONE);
            delivery_check_tick.setVisibility(View.VISIBLE);
            meet_at_vehicle_check_tick.setVisibility(View.GONE);

            if (locationModels.get(i).getApartment() == null) {
                edtAddapartment.setText("");
                edtAddapartment.setSelection(edtAddapartment.getText().length());
            } else {
                edtAddapartment.setText(locationModels.get(i).getApartment());
                edtAddapartment.setSelection(edtAddapartment.getText().length());
            }
            if (locationModels.get(i).getDeliverynote() == null) {
                edtAdddoordelivery.setText("");
                edtAdddoordelivery.setSelection(edtAdddoordelivery.getText().length());
            } else {
                edtAdddoordelivery.setText(locationModels.get(i).getDeliverynote());
                edtAdddoordelivery.setSelection(edtAdddoordelivery.getText().length());
            }
        }
    }

    /**
     * On Backpressed
     */

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(sessionManager.getLocation())) {
            finishAffinity();
        }
        if ("cancel".equals(mCancel)) {
            mCancel = "";
            super.onBackPressed();
        }
        if ("notes".equals(savedeliverylocation)) {
            super.onBackPressed();
        }
        if (!TextUtils.isEmpty(sessionManager.getLocation()) && deliveryaddress.getText().toString().isEmpty() || deliveryaddress.getText().toString().equals("")) {
            super.onBackPressed();
        } else {
            if (type == 0 || type == 1) {
                super.onBackPressed();
            } else {
                deliveryaddress.getText().clear();
                enableViews();

            }
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(deliveryaddress.getWindowToken(), 0);

    }

    /**
     * To intialize views
     */

    public void intViews() {

        rcLocation_list = findViewById(R.id.rcLocation_list);
        deliveryaddress = findViewById(R.id.et_location);
        location_placesearch = findViewById(R.id.location_placesearch);
        close = findViewById(R.id.close);
        save_button = findViewById(R.id.save_button);
        asap_lay = findViewById(R.id.asap_lay);
        schedule_lay = findViewById(R.id.schedule_lay);
        meet_at_vehicle_lay = findViewById(R.id.meet_at_vehicle_lay);
        delivery_to_you_layout = findViewById(R.id.delivery_to_you_layout);
        deliverynote_lay = findViewById(R.id.deliverynote_lay);
        edits = findViewById(R.id.edits);
        delivery_notes = findViewById(R.id.delivery_notes);
        set_sch_layout = findViewById(R.id.set_sch_layout);
        dates = findViewById(R.id.date);
        times = findViewById(R.id.time);
        checked_asap = findViewById(R.id.checked_asap);
        checked_sch = findViewById(R.id.checked_sch);
        delivery_check_tick = findViewById(R.id.delivery_check_tick);
        meet_at_vehicle_check_tick = findViewById(R.id.meet_at_vehicle_check_tick);
        when_text = findViewById(R.id.when_text);
        order_time = findViewById(R.id.order_time);
        arrow = findViewById(R.id.arrow);
        etdAdddeliverynote1 = findViewById(R.id.etdAdddeliverynote1);
        edtAddapartment = findViewById(R.id.edtAddapartment);
        add_business = findViewById(R.id.add_business);
        edtAdddoordelivery = findViewById(R.id.edtAdddoordelivery);
        lltWhen = findViewById(R.id.lltWhen_layout);
        lltlocationlist = findViewById(R.id.lltlocationList);
        lltCurrentLocation = findViewById(R.id.lltCurrentLocation);
        tvSub_address = findViewById(R.id.tvSub_address);
        tvMain_address = findViewById(R.id.tvMain_address);
        ivClosedeliveryNote1 = findViewById(R.id.ivClosedeliveryNote1);
        ivCloseapartment = findViewById(R.id.ivCloseapartment);
        ivClosedeliveryNote2 = findViewById(R.id.ivClosedeliveryNote2);
        btnRemoveLocation = findViewById(R.id.btnRemoveLocation);
        rltWholeview = findViewById(R.id.rltWholeview);
        currentlocation = findViewById(R.id.currentlocation);
        rltWholeview.setVisibility(View.GONE);


        dialog = commonMethods.getAlertDialog(this);


        rcLocation_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rcLocation_list.setLayoutManager(layoutManager1);
    }

    /**
     * To Fetch User Saved Location
     */

    public void getUserSavedLocation() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.getSavedLocation(sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_SAVED_LOCATION, this));

    }

    /**
     * Show time for schedule
     */
    public void showTime() {


        Locale locale;// Set start time from current time  Setting Maximum Duration upto 15.
        if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            locale = new Locale("ar");
        } else {
            if (sessionManager.getAppLanguageCode().equals("en"))
                locale = new Locale("en");
            else
                locale = new Locale("pt");
        }
        Locale.setDefault(locale);
        System.out.println("Locale Language" + locale);


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LocActivity.this);
        LayoutInflater inflater = LocActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.date_time_picker, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

        final Date date = Calendar.getInstance().getTime();
        final Calendar c = Calendar.getInstance();
        final int startMin = 60;

        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        c.add(Calendar.DAY_OF_MONTH, 7);
        datePicker.setMaxDate(c.getTimeInMillis());
        final Calendar calendarMax = Calendar.getInstance();
        Calendar CurrentCal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        try {
            Date d = df.parse(date.toString());

            calendarMax.setTime(d);
            calendarMax.add(Calendar.MINUTE, startMin);       //Setting Maximum Duration upto 15.
            newTime = df.format(calendarMax.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date max = calendarMax.getTime();
        String new_date = newTime;
        System.out.println("New Date and time " + new_date);
        Date dt1 = null;
        try {
            dt1 = df.parse(new_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        datePicker.init(CurrentCal.get(Calendar.YEAR), CurrentCal.get(Calendar.MONTH), CurrentCal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                int mon = c.get(Calendar.MONTH);
                int mony = month + 1;
                datevalid = !((dayOfMonth != date.getDate()) || mony != mon);

                if (datevalid) {
                    timePicker.setCurrentMinute(max.getMinutes());
                    timePicker.setCurrentHour(max.getHours());
                } else {
                    Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    if (c.getTime().getDate() == calendar.getTime().getDate()) {
                        timePicker.setCurrentMinute(max.getMinutes() - startMin);
                        timePicker.setCurrentHour(max.getHours());
                    } else if (calendarMax.getTime().getDate() == calendar.getTime().getDate()) {
                        timePicker.setCurrentMinute(max.getMinutes() - startMin);
                        timePicker.setCurrentHour(max.getHours());
                    }
                }
            }
        });


        DateFormat format2 = new SimpleDateFormat("hh");
        DateFormat format3 = new SimpleDateFormat("mm");
        String hour = format2.format(dt1);
        String minute = format3.format(dt1);

        timePicker.setCurrentMinute(max.getMinutes());
        timePicker.setCurrentHour(max.getHours());

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                boolean validTime;
                Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                if (c.getTime().getDate() == calendar.getTime().getDate()) {
                    int maxHour = calendarMax.getTime().getHours();
                    int maxMinute = calendarMax.getTime().getMinutes();
                    validTime = !(hourOfDay > maxHour || (hourOfDay == maxHour && minute + startMin > maxMinute));
                } else {
                    validTime = true;
                    if ((hourOfDay > max.getHours() || (hourOfDay == max.getHours()) && (minute > max.getMinutes()))) {
                        validTime = true;
                    } else {
                        if (datevalid) {
                            timePicker.setCurrentMinute(max.getMinutes());
                            timePicker.setCurrentHour(max.getHours());
                        }
                    }
                }
                datevalid = datePicker.getDayOfMonth() == date.getDate();

                if (validTime) {
                    System.out.println("isValidTime OK");
                } else if (datevalid) {
                    timePicker.setCurrentMinute(max.getMinutes() - startMin);
                    timePicker.setCurrentHour(max.getHours());
                }

            }
        });

        dialogView.findViewById(R.id.cancel_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Checksave = false;
                save_button.setEnabled(false);
                save_button.setBackgroundColor(getResources().getColor(R.color.payment_method));
            }
        });
        dialogView.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Checksave = true;
                Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());


                Date add_time = calendar.getTime();
                final Calendar add_30 = Calendar.getInstance();
                SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);  //Normal date Format
                try {
                    Date d = df1.parse(add_time.toString());

                    add_30.setTime(d);
                    add_30.add(Calendar.MINUTE, 30);
                    add = df1.format(add_30.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String new_date1 = add;
                Date dtadd = null;
                try {
                    dtadd = df1.parse(new_date1);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat formatadd;
                if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                    Locale locale = new Locale("ar");
                    formatadd = new SimpleDateFormat("hh:mm a", locale);
                } else if (sessionManager.getAppLanguageCode().equals("en")) {
                    formatadd = new SimpleDateFormat("hh:mm a", Locale.US);
                } else {
                    Locale locale = new Locale("pt");
                    formatadd = new SimpleDateFormat("hh:mm a", locale);
                }


                add30 = formatadd.format(dtadd);

                Date time = calendar.getTime();
                final Calendar calendarMaxz = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);  //Normal date Format
                try {
                    Date d = df.parse(time.toString());

                    calendarMaxz.setTime(d);
                    newTimer = df.format(calendarMaxz.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String new_date = newTimer;
                Date dt1 = null;
                try {
                    dt1 = df.parse(new_date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat format2;
                DateFormat format3;
                DateFormat format4;
                DateFormat format5;
                DateFormat format6;
                DateFormat format7;
                Locale locale;
                locale = new Locale("pt");
                System.out.println("Before Condition: " + locale);
                if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                    locale = new Locale("ar");
                    System.out.println("IF statement" + locale);
                    format2 = new SimpleDateFormat("EEE, dd MMM", locale);
                    format3 = new SimpleDateFormat("hh:mm a", locale);
                    format4 = new SimpleDateFormat("EEE", locale);
                    //format5 = new SimpleDateFormat("yyyy-MM-dd HH:mm",locale);
                    format6 = new SimpleDateFormat("EEE dd", locale);
                    format7 = new SimpleDateFormat("dd-MM-yyyy", locale);
                } else if (sessionManager.getAppLanguageCode().equals("pt")) {
                    locale = new Locale("pt");
                    System.out.println("ELSE IF statement" + locale);
                    format2 = new SimpleDateFormat("EEE, dd MMM", locale);
                    format3 = new SimpleDateFormat("hh:mm a", locale);
                    format4 = new SimpleDateFormat("EEE", locale);
                    //format5 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    format6 = new SimpleDateFormat("EEE dd", locale);
                    format7 = new SimpleDateFormat("dd-MM-yyyy", locale);
                } else {
                    System.out.println("ELSE statement" + Locale.US);
                    format2 = new SimpleDateFormat("EEE, dd MMM", Locale.US);
                    format3 = new SimpleDateFormat("hh:mm a", Locale.US);
                    format4 = new SimpleDateFormat("EEE", Locale.US);
                    //format5 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    format6 = new SimpleDateFormat("EEE dd", Locale.US);
                    format7 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                }
                format5 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

                day = format2.format(dt1);
                minutea = format3.format(dt1);
                scheduledDay = format4.format(dt1);
                compareDate = format7.format(dt1);

                alertDialog.dismiss();
                sessionManager.setDeliveredTime(format5.format(dt1));
                sessionManager.setHomeScheduledDay(format6.format(dt1));
                sessionManager.setScheduleMin(minutea);
                sessionManager.setScheduleDate(day);
                sessionManager.setScheduledDay(scheduledDay);

                if (sessionManager.getScheduleDate().equals("") && sessionManager.getscheduleMin().equals("")) {
                    dates.setText(getResources().getString(R.string.schedule_date));
                    times.setText(getResources().getString(R.string.set_time));
                } else {
                    dates.setText(day);
                    times.setText(minutea + "-" + add30);
                    if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                        dates.setTextDirection(View.TEXT_DIRECTION_RTL);
                        times.setTextDirection(View.TEXT_DIRECTION_RTL);
                    }
                    dates.setTextColor(getResources().getColor(R.color.apptheme));
                    times.setTextColor(getResources().getColor(R.color.apptheme));
                    sessionManager.setAddedTime(times.getText().toString());
                    orderType = 1;
                }
                checkDates(todayDate, compareDate);
                save_button.setEnabled(true);
                save_button.setBackgroundColor(getResources().getColor(R.color.apptheme));
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            googleApiClient.connect();
        }

        if (!isPermissionGranted)
            checkAllPermission(Constants.PERMISSIONS_LOCATION); //check permission for location

        getCurrentLocation();

    }

    private void getCurrentLocation() {
        if (!currentLocation) {
            lltCurrentLocation.setVisibility(View.GONE);
        } else {
            lltCurrentLocation.setVisibility(View.VISIBLE);
            tvMain_address.setText(currentlocation1);
            tvSub_address.setText(curremtlocation2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            googleApiClient.disconnect();
        }
    }

    public void viewChanger(String getLocationType) {

        if ("home".equals(getLocationType) || "work".equals(getLocationType)) {
            getCurrentLocation();
            if ("".equals(tvMain_address.getText().toString()) && "".equals(tvSub_address.getText().toString())) {
                lltCurrentLocation.setVisibility(View.GONE);
            } else {
                lltCurrentLocation.setVisibility(View.VISIBLE);
            }
            location_placesearch.setVisibility(View.GONE);
            rcLocation_list.setVisibility(View.GONE);
            when_text.setVisibility(View.GONE);
            order_time.setVisibility(View.GONE);
            delivery_notes.setVisibility(View.GONE);
            btnRemoveLocation.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
        } else {
            location_placesearch.setVisibility(View.GONE);
            rcLocation_list.setVisibility(View.VISIBLE);
            when_text.setVisibility(View.VISIBLE);
            order_time.setVisibility(View.VISIBLE);
            delivery_notes.setVisibility(View.GONE);
            btnRemoveLocation.setVisibility(View.GONE);
            close.setVisibility(View.GONE);
        }
    }

    /**
     * Connect Google APi Client
     */
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).addApi(Places.GEO_DATA_API).build();
    }

    /**
     * Google APi on Connected
     */
    @Override
    public void onConnected(Bundle bundle) {
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
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (mLastLocation != null) {
            current_latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Log.d("Is connected", "ON connected");
            Log.d("Latitude", String.valueOf(current_latlng.latitude));
            Log.d("Longi", String.valueOf(current_latlng.longitude));
            // fetchAddress(current_latlng, "currentloation");
        } else try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Google API suspended
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("is suspended", "Connection suspended");
        googleApiClient.connect();
    }

    /**
     * Google API connection failed
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * Fetch Location from address if Geocode available get from geocode otherwise get location from google
     */
    public void fetchLocation(String addresss, final String type) {
        getAddress = addresss;
        new AsyncTask<Void, Void, String>() {
            String locations = null;

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {

                if (Geocoder.isPresent()) // Check geo code available or not
                {
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> address;

                        // May throw an IOException
                        address = geocoder.getFromLocationName(getAddress, 5);
                        if (address == null) {
                            return null;
                        }
                        Address location = address.get(0);

                        countrys = address.get(0).getCountryName();

                        if (!"".equals(countrys)) deliverycountry = address.get(0).getCountryName();

                        location.getLatitude();
                        location.getLongitude();

                        lat = String.valueOf(location.getLatitude());
                        log = String.valueOf(location.getLongitude());
                        locations = lat + "," + log;
                    } catch (Exception ignored) {
                        // after a while, Geocoder start to throw "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }

                if (locations != null) // i.e., Geocoder succeed
                {
                    return locations;
                } else // i.e., Geocoder failed
                {
                    return fetchLocationUsingGoogleMap(); // If geocode not available or location null call google API
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private String fetchLocationUsingGoogleMap() {
                getAddress = getAddress.replaceAll(" ", "%20");
                String googleMapUrl = "http://maps.google.com/maps/api/geocode/json?address=" + getAddress + "&sensor=false";
                try {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl), new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results

                    if (googleMapResponse.length() > 0) {
                        JSONArray result = googleMapResponse.getJSONArray("results");
                        int array_size = result.length();
                        if (array_size > 0) {
                            String longitute = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");

                            String latitude = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");


                            int len = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").length();
                            for (int i = 0; i < len; i++) {
                                if (((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getJSONArray("types").getString(0).equals("country")) {
                                    countrys = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getString("long_name");
                                }
                            }

                            deliverycountry = countrys;

                            return latitude + "," + longitute;
                        } else {
                            String longitute = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");

                            String latitude = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");

                            return latitude + "," + longitute;
                        }

                    } else {
                        return null;
                    }

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String location) {
                if (location != null) {
                    String[] parts = location.split(",");
                    Double lat = Double.valueOf(parts[0]);
                    Double lng = Double.valueOf(parts[1]);
                    if (String.valueOf(lat) == null || String.valueOf(lng) == null) {
                        save_button.setEnabled(false);
                    } else {
                        latLngdelivery = new LatLng(lat, lng);

                        System.out.println("lat " + latLngdelivery.latitude + "long " + latLngdelivery.longitude);

                        if ("searchitemclick".equals(type)) {
                            if ("".equals(countrys) || "".equals(countrys))
                                countrys = deliverycountry;
                        } else if ("".equals(type)) {
                            fetchAddress(latLngdelivery, "");
                        }
                        save_button.setEnabled(true);
                    }
                } else {
                    save_button.setEnabled(false);
                    //snackBar("Unable to get location please try again...", "hi", false, 2);
                    if (("home".equals(savedeliverylocation) || "work".equals(savedeliverylocation))) {
                        commonMethods.snackBar("Unable to get location please try again...", "hi", false, 2, edits, getResources(), LocActivity.this);
                    }

                }

            }

        }.execute();
    }

    /**
     * Fetch address from location if Geocode available get from geocode otherwise get location from google
     */
    public void fetchAddress(LatLng location, final String type) {
        getLocations = location;
        address = null;
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {

                if (Geocoder.isPresent()) // Check Geo code available or not
                {
                    try {

                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(getLocations.latitude, getLocations.longitude, 1);
                        if (addresses != null) {
                            countrys = addresses.get(0).getCountryName();

                            String adress0 = addresses.get(0).getAddressLine(0);
                            String adress1 = addresses.get(0).getAddressLine(1);

                            address = adress0 + " " + adress1; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                            String country = addresses.get(0).getCountryName();
                            String Street = addresses.get(0).getFeatureName();
                            String ad = addresses.get(0).getSubLocality();
                            String countryCode = addresses.get(0).getCountryCode();

                            if (ad == null) {
                                ad = "";
                            }
                            getcountryCode = countryCode;
                            getcity = city;
                            getstate = state;
                            getpostal = postalCode;
                            getcountry = country;
                            getstreet = Street + " " + ad;

                            if (getpostal == null) {
                                getpostal = " ";
                            } else {
                                setAddress = getpostal;
                            }
                            if (getcountry == null) {
                                getcountry = " ";
                            } else {
                                setAddress = getcountry;
                            }
                            if (getstate == null) {
                                getstate = " ";
                            } else {
                                setAddress = getstate;
                            }
                            if (getcity == null) {
                                getcity = " ";
                            } else {
                                setAddress = getcity;
                            }
                            if (getstreet == null) {
                                getstreet = " ";
                            }

                            if ("currentloation".equals(type)) {
                                currentlocation1 = getstreet;
                                curremtlocation2 = getcity + "," + getstate + "," + getcountry;
                            }
                        }
                    } catch (Exception ignored) {
                        // after a while, Geocoder start to throw "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }
                if (address != null) // i.e., Geocoder succeed
                {
                    return address;
                } else // i.e., Geocoder failed
                {
                    return fetchAddressUsingGoogleMap();
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private String fetchAddressUsingGoogleMap() {

                addressList = new ArrayList<Address>();
                String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + getLocations.latitude + "," + getLocations.longitude + "&sensor=false";
                try {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl), new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results

                    JSONArray results = (JSONArray) googleMapResponse.get("results");
                    for (int i = 0; i < results.length(); i++) {


                        JSONObject result = results.getJSONObject(i);


                        String indiStr = result.getString("formatted_address");


                        Address addr = new Address(Locale.getDefault());


                        addr.setAddressLine(0, indiStr);
                        //  country=addr.getCountryName();

                        addressList.add(addr);


                    }

                    int len = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").length();
                    for (int i = 0; i < len; i++) {
                        if (((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getJSONArray("types").getString(0).equals("country")) {
                            countrys = ((JSONArray) googleMapResponse.get("results")).getJSONObject(0).getJSONArray("address_components").getJSONObject(i).getString("long_name");
                            System.out.println("countrys " + countrys);
                        }
                    }


                    // if (addressList != null&&countrys.equalsIgnoreCase("India")) {

                    String adress0 = addressList.get(0).getAddressLine(0);
                    address = adress0;//+" "+adress1;
                    //address = adress0+" "+adress1; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    address.replaceAll("null", "");

                    if (address != null) {
                        setAddress = address;
                        return address;
                    }
                    //}

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String address) {
                if (address != null) {
                    if ("settext".equals(type)) {
                        System.out.println("settext " + address);
                        deliverycountry = address;
                    } else if ("currentloation".equals(type)) {
                        currentLocation = true;
                        getCurrentLocation();
                        fullAddress = address.replaceAll("null", "");
                    } else if ("".equals(type)) {
                        fullAddress = address.replaceAll("null", "");
                        System.out.println("alladdress " + fullAddress);
                    }
                    //fullAddress=address;

                    //sessionManager.setLocation(address);
                } else {

                    //snackBar("Unable to get location please try again...", "hi", false, 2);
                    if (("home".equals(savedeliverylocation) || "work".equals(savedeliverylocation))) {
                        commonMethods.snackBar("Unable to get location please try again...", "hi", false, 2, edits, getResources(), LocActivity.this);
                    }
                }


            }

        }.execute();
    }

    /**
     * ADD HOME AND WORK AND Delivery Address
     */
    private void updateSavedLocation() {

        commonMethods.showProgressDialog(this, customDialog);
        apiService.saveLocation(sessionManager.getToken(), getSave()).enqueue(new RequestCallback(REQ_UPDATE_LOCATION, this));
    }

    /**
     * getting ALL Params
     */

    public HashMap<String, String> getSave() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("latitude", Double.toString(latLngdelivery.latitude));
        hashMap.put("longitude", Double.toString(latLngdelivery.longitude));
        hashMap.put("city", getcity);
        hashMap.put("state", getstate);
        hashMap.put("postal_code", getpostal);
        hashMap.put("street", getstreet);
        hashMap.put("country", getcountry);
        hashMap.put("country_code", getcountryCode);
        hashMap.put("delivery_note", deliverynotes);
        hashMap.put("type", String.valueOf(type));
        hashMap.put("address", fullAddress);
        hashMap.put("apartment", edtAddapartment.getText().toString().trim());
        hashMap.put("delivery_options", String.valueOf(delivery_options));
        hashMap.put("first_address", area);
        hashMap.put("second_address", second_address);
        hashMap.put("order_type", String.valueOf(orderType));
        hashMap.put("delivery_time", deliveryTime);
        return hashMap;
    }

    /**
     * on success method   json resp
     *
     * @param jsonResp json response
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        String status_code = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "status_code", String.class);
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }

        switch (jsonResp.getRequestCode()) {
            case REQ_UPDATE_LOCATION:
                if (jsonResp.isSuccess()) {
                    onSuccessLocation(jsonResp);
                    //sessionManager.setLocation(area);
                } else if ("2".equals(status_code)) {
                    showClearAlert();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_GET_SAVED_LOCATION:
                if (jsonResp.isSuccess()) {
                    onSuccessGetSavedLocation(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;

            case REQ_REMOVE_LOCATION:
                if (jsonResp.isSuccess()) {
                    onSuccessRemoveLocation(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;

            case REQ_SET_DEFAULT:
                if (jsonResp.isSuccess()) {
                    /*if (savedeliverylocation.equals("edit")||savedeliverylocation.equals("entry")||savedeliverylocation.equals("change")){

                        for (int i=0;i<locationModels.size();i++){
                            if (locationModels.get(i).getDefaultaddress()==1) {
                                sessionManager.setLocation(locationModels.get(i).getAddress());
                            }
                        }
                    }*/
                    for (int i = 0; i < locationModels.size(); i++) {
                        if (locationModels.get(i).getDefaultaddress() == 1) {
                            //sessionManager.setLocation(locationModels.get(i).getStreet()+""+locationModels.get(i).getCity());
                            sessionManager.setLocation(locationModels.get(i).getFirstAddress());
                            //break;
                        }
                    }
                    if (type == 2) {
                        if ("edit".equals(savedeliverylocation) || "entry".equals(savedeliverylocation)) {
                            Intent saved = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(saved);
                        } else if ("change".equals(savedeliverylocation) && mNewLocation == 1) {
                            sessionManager.setCartCount(0);
                            sessionManager.setCartAmount("");
                            sessionManager.setOrderType(0);
                            sessionManager.setScheduledDay("");
                            sessionManager.setScheduleMin("");
                            sessionManager.setScheduleDate("");
                            sessionManager.setAddedTime("");
                            Intent saved = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(saved);
                        }
                        finish();
                    }
                } else if ("2".equals(status_code)) {
                    showClearAlert();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                //finish();
                break;
            case REQ_CLEAR_AND_CHANGE_LOCATION:
                if (jsonResp.isSuccess()) {
                    onSuccessClearLocation(jsonResp);
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
        commonMethods.showProgressDialog(this, customDialog);
    }


    /**
     * Getting values method for get Location api
     *
     * @param jsonResponse response from get Location api
     */
    private void onSuccessGetSavedLocation(JsonResponse jsonResponse) {
        getLocationModel = gson.fromJson(jsonResponse.getStrResponse(), GetLocationModel.class);
        if (getLocationModel != null) {
            locationModels.clear();
            locationModels.addAll(getLocationModel.getLocationList());
            for (int i = 0; i < locationModels.size(); i++) {
                System.out.println("SETTED DEFAULT " + locationModels.get(i).getDefaultaddress());
                if (locationModels.get(i).getDefaultaddress() == 1) {
                    isDefault = locationModels.get(i).getType();
                    System.out.println(" DEFAULT " + isDefault);
                    break;
                }
            }
            sessionManager.setLocationList(jsonResponse.getStrResponse());
            enableViews();
            adapter = new LocationListAdapter(this, locationModels);
            adapter.setOnItemClickListener(new LocationListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int position, ArrayList<LocationList> locationLists) {
                    getPostion = position;
                    if (locationLists.get(position).getDefaultaddress() == 1) {//&&isFirstClick) {
                        type = locationLists.get(position).getType();
                        area = locationLists.get(position).getFirstAddress();
                        second_address = locationLists.get(position).getSecondAddress();
                        isDefault = type;
                        isFirstClick = false;
                        deliveryaddress.setText(locationLists.get(position).getFirstAddress());
                        deliveryaddress.setSelection(deliveryaddress.getText().length());
                        latLngdelivery = new LatLng(locationLists.get(position).getLatitude(), locationLists.get(position).getLongitude());
                        fetchAddress(latLngdelivery, "");
                        lltlocationlist.setVisibility(View.GONE);
                        delivery_notes.setVisibility(View.VISIBLE);
                        lltWhen.setVisibility(View.GONE);
                        btnRemoveLocation.setVisibility(View.GONE);
                    } else {
                        isFirstClick = true;
                        isDefault = locationLists.get(position).getType();
                        save_button.setEnabled(true);
                    }

                    if (locationLists.get(position).getDeliveryoptions() == 0) {
                        deliverynoteType = 0;
                        delivery_options = 0;
                        deliverynote_lay.setVisibility(View.VISIBLE);
                        edits.setVisibility(View.GONE);
                        delivery_check_tick.setVisibility(View.GONE);
                        meet_at_vehicle_check_tick.setVisibility(View.VISIBLE);
                        if (locationLists.get(position).getDeliverynote() == null) {
                            etdAdddeliverynote1.setText("");
                            etdAdddeliverynote1.setSelection(etdAdddeliverynote1.getText().length());
                        } else {
                            etdAdddeliverynote1.setText(locationLists.get(position).getDeliverynote());
                            etdAdddeliverynote1.setSelection(etdAdddeliverynote1.getText().length());
                        }

                    } else {
                        deliverynoteType = 1;
                        delivery_options = 1;
                        edits.setVisibility(View.VISIBLE);
                        deliverynote_lay.setVisibility(View.GONE);
                        delivery_check_tick.setVisibility(View.VISIBLE);
                        meet_at_vehicle_check_tick.setVisibility(View.GONE);

                        if (locationLists.get(position).getApartment() == null) {
                            edtAddapartment.setText("");
                            edtAddapartment.setSelection(edtAddapartment.getText().length());
                        } else {
                            edtAddapartment.setText(locationLists.get(position).getApartment());
                            edtAddapartment.setSelection(edtAddapartment.getText().length());
                        }
                        if (locationLists.get(position).getDeliverynote() == null) {
                            edtAdddoordelivery.setText("");
                            edtAdddoordelivery.setSelection(edtAdddoordelivery.getText().length());
                        } else {
                            edtAdddoordelivery.setText(locationLists.get(position).getDeliverynote());
                            edtAdddoordelivery.setSelection(edtAdddoordelivery.getText().length());
                        }
                    }
                }
            });
            rcLocation_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            rltWholeview.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Getting values method for save Location api
     *
     * @param jsonResponse response from save Location api
     */

    private void onSuccessLocation(JsonResponse jsonResponse) {
        getLocationModel = gson.fromJson(jsonResponse.getStrResponse(), GetLocationModel.class);
        if (getLocationModel != null) {
            locationModels.clear();
            locationModels.addAll(getLocationModel.getLocationList());
            sessionManager.setLocationList(jsonResponse.getStrResponse());
        }
        if (("home".equals(savedeliverylocation) || "work".equals(savedeliverylocation)) && (type == 0 || type == 1)) {
            onBackPressed();
        }
        //if (savedeliverylocation.equals("edit")||savedeliverylocation.equals("entry")||savedeliverylocation.equals("change")){

        for (int i = 0; i < locationModels.size(); i++) {
            if (locationModels.get(i).getDefaultaddress() == 1) {
                //sessionManager.setLocation(locationModels.get(i).getStreet()+" "+locationModels.get(i).getCity());
                sessionManager.setLocation(locationModels.get(i).getFirstAddress());
                //break;
            }
        }
        // }


        if ("edit".equals(savedeliverylocation) || "entry".equals(savedeliverylocation)) {
            Intent saved = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(saved);
        } else if ("change".equals(savedeliverylocation)) {
            finish();
        } else if ("change".equals(savedeliverylocation) && mNewLocation == 1) {
            Intent saved = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(saved);
        } else if ("notes".equals(savedeliverylocation)) {
            finish();
        }


    }


    /**
     * Getting values method for remove Location api
     *
     * @param jsonResp response from remove Location api
     */

    private void onSuccessRemoveLocation(JsonResponse jsonResp) {
        getLocationModel = gson.fromJson(jsonResp.getStrResponse(), GetLocationModel.class);
        if (getLocationModel != null) {
            locationModels.clear();
            locationModels.addAll(getLocationModel.getLocationList());
            sessionManager.setLocationList(jsonResp.getStrResponse());
            Toast.makeText(this, getResources().getString(R.string.location_removed), Toast.LENGTH_SHORT).show();
        }

        if (type == 0 || type == 1) {
            onBackPressed();
        }

        for (int i = 0; i < locationModels.size(); i++) {
            if (locationModels.get(i).getDefaultaddress() == 1) {
                //sessionManager.setLocation(locationModels.get(i).getStreet()+" "+locationModels.get(i).getCity());
                sessionManager.setLocation(locationModels.get(i).getFirstAddress());
                //break;
            }
        }

    }

    /**
     * api call to update default location
     *
     * @param type 0 for home, 1 for work , 2 Delivery address
     */


    private void setDefault(int type) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.defaultLocation(sessionManager.getToken(), type, orderType, deliveryTime).enqueue(new RequestCallback(REQ_SET_DEFAULT, this));
    }


    /**
     * remove Location api call method
     *
     * @param type 0 for home, 1 for work , 2 Delivery address
     */


    private void removeLocation(int type) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.removeLocation(sessionManager.getToken(), type).enqueue(new RequestCallback(REQ_REMOVE_LOCATION, this));
    }

    /**
     * Check user allow all permission and ask permission to allow
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 150);
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
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Check GPS enable or not
     */
    private void checkGpsEnable() {
        boolean isGpsEnabled = MyLocation.defaultHandler().isLocationAvailable(this);
        if (!isGpsEnabled) {
            //startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
            showEnablePermissionDailog(1, getString(R.string.please_enable_location));
        } else {
            isPermissionGranted = true;
            MyLocation.defaultHandler().getLocation(this, locationResult);
        }
    }

    /**
     * clear Cart When Location is changed
     */
    public void showClearAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_clear_cart_dialog, null, false);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        CustomButton btnClearCart = dialogView.findViewById(R.id.btnClearCart);
        CustomTextView tvcancel = dialogView.findViewById(R.id.tvcancel);

        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAndSaveLocation();
            }
        });

        tvcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                mCancel = "cancel";
                onBackPressed();
            }
        });
    }

    /**
     * Clear Cart
     */
    private void clearAndSaveLocation() {
        commonMethods.showProgressDialog(LocActivity.this, customDialog);
        apiService.clearAllCart(sessionManager.getToken()).enqueue(new RequestCallback(REQ_CLEAR_AND_CHANGE_LOCATION, LocActivity.this));
    }

    /**
     * Change Location and clear Cart
     *
     * @param jsonResp
     */
    private void onSuccessClearLocation(JsonResponse jsonResp) {
        String statusCode = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "status_code", String.class);
        if ("1".equals(statusCode)) {
            mNewLocation = 1;
            sessionManager.setCartCount(0);
            sessionManager.setCartAmount("");
            if (deliveryaddress.getText().toString().isEmpty()) {
                if (locationModels != null && locationModels.size() > 0) {
                    System.out.println(" Is Default " + isDefault);
                    setDefault(isDefault);
                } else {
                    Toast.makeText(LocActivity.this, getResources().getString(R.string.enter_location), Toast.LENGTH_SHORT).show();
                }
            } else {

                updateSavedLocation();
            }
            //updateSavedLocation();
        }
    }

    public void getCurrentDate() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        todayDate = df.format(today);
        System.out.println("Current time => " + todayDate);
    }

    /**
     * Compare 2 dates are Equal to set Today and Tomorrow
     *
     * @param startDate Today Date
     * @param endDate   selected Date in Date Picker
     */
    public void checkDates(String startDate, String endDate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                // If start date is before end date. Tomorrow
                Date d = null;
                Date d1 = null;
                Calendar cal = Calendar.getInstance();
                try {
                    d = dfDate.parse(endDate);
                    d1 = dfDate.parse(dfDate.format(cal.getTime()));//Returns Today date
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int diffInDays = (int) ((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
                System.out.println("My DiffDays" + diffInDays);

                if (diffInDays == 1) {
                    sessionManager.setHomeScheduledDay(getResources().getString(R.string.tommorow));
                }

            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                // If two dates are equal. Today
                sessionManager.setHomeScheduledDay(getResources().getString(R.string.today));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void selectedAddress(AutocompletePrediction autocompletePrediction) {

        if (autocompletePrediction != null) {
            counti++;
            searchlocation = autocompletePrediction.getFullText(null).toString();
            deliveryaddress.setText(searchlocation);
            oldstring = searchlocation;
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(deliveryaddress.getWindowToken(), 0);
            save_button.setEnabled(false);
            fetchLocation(searchlocation, "");
            clickedLocation = searchlocation;
            location_placesearch.setVisibility(View.GONE);
            lltlocationlist.setVisibility(View.GONE);
            delivery_notes.setVisibility(View.VISIBLE);
            lltWhen.setVisibility(View.GONE);
            area = autocompletePrediction.getPrimaryText(null).toString();
            ;
            second_address = autocompletePrediction.getSecondaryText(null).toString();
            save_button.setVisibility(View.VISIBLE);
        }
    }

    public void getFullAddressUsingEdittextStringFromGooglePlaceSearchAPI(String queryAddress) {
        //RectangularBounds bounds = RectangularBounds.newInstance(first, sec);
        FindAutocompletePredictionsRequest request =
                FindAutocompletePredictionsRequest.builder()
                        .setSessionToken(googleAutoCompleteToken)
                        .setQuery(queryAddress)
                        .build();
        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(
                        (response) -> {
                        /*Toast toast = Toast.makeText(this, "address auto comp succ" + response.getAutocompletePredictions().size(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
                        toast.show();*/
                            if (response.getAutocompletePredictions().size() > 0) {
                                googleMapPlaceSearchAutoCompleteRecyclerView.updateList(response.getAutocompletePredictions());
                            } else {
                                clearAddressAndHideRecyclerView();
                                //showUserMessage(getResources().getString(R.string.no_address_found));
                            }
                        })
                .addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        //CommonMethods.showUserMessage("place not found "+ apiException.getStatusCode());
                     /*Toast toast = Toast.makeText(this, "address error "+apiException.getStatusCode(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
                        toast.show();*/
                    }
                    exception.printStackTrace();
                /*Toast toast = Toast.makeText(this, "address error ", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
                toast.show();*/
                });
    }

    public void clearAddressAndHideRecyclerView() {
        googleMapPlaceSearchAutoCompleteRecyclerView.clearAddresses();
        location_placesearch.setVisibility(View.GONE);
    }
}