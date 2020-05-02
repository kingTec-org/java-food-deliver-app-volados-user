package gofereats.views.main.subviews;

/**
 *
 * @package com.gofereats
 * @subpackage views.main
 * @category RestaurantDetail Activity
 * @author Trioangle Product Team
 * @version 0.6
 */


/*************************************************************
 Restaurants Food details
 ************************************************************/

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereats.R;
import gofereats.adapters.main.FoodCategoryListAdapter;
import gofereats.adapters.main.FoodListAdapter;
import gofereats.adapters.main.MenuListAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.restaurantdetails.FoodListModel;
import gofereats.datamodels.restaurantdetails.RestOfferModel;
import gofereats.datamodels.restaurantdetails.RestaurantDetailModel;
import gofereats.datamodels.restaurantdetails.RestaurantDetailResultModel;
import gofereats.datamodels.restaurantdetails.RestaurantMenuCategoriesModel;
import gofereats.datamodels.restaurantdetails.RestaurantMenuItemModel;
import gofereats.datamodels.restaurantdetails.RestaurantMenuModel;
import gofereats.datamodels.restaurantdetails.TooFarDetailModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.placesearch.RecyclerItemClickListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.Enums;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.customize.CustomRecyclerView;
import gofereats.views.main.MainActivity;

public class RestaurantDetailActivity extends AppCompatActivity implements ServiceListener {

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

    public MenuInject menuInject;
    public BottomSheetDialog menuDialog;
    public ArrayList<RestaurantMenuModel> restaurantMenuModel = new ArrayList<>();
    public RecyclerView food_list;
    public RecyclerView category_list;
    public ImageView rest_details_back;
    public ImageView rest_details_tool_back;
    public int scrollHeight = 0;
    public int categoryPosition = 0;
    public AppBarLayout appBarLayout;
    public CollapsingToolbarLayout collapsingToolbar;
    public Toolbar toolbar;
    public RelativeLayout toolbar_layout;
    public FrameLayout frame;
    public Animation slide_down;
    public Animation slide_up;
    public LinearLayoutManager layoutManager1;
    public LinearLayoutManager layoutManager2;
    public int menuPosition = 0;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.tvRestaurantName)
    CustomTextView tvRestaurantName;
    public @InjectView(R.id.tvRestaurantCategory)
    CustomTextView tvRestaurantCategory;
    public @InjectView(R.id.tvRestaurantTime)
    CustomTextView tvRestaurantTime;
    public @InjectView(R.id.tvRating)
    CustomTextView tvRating;
    public @InjectView(R.id.tvRatingUser)
    CustomTextView tvRatingUser;
    public @InjectView(R.id.tvRestaurantNameToolbar)
    CustomTextView tvRestaurantNameToolbar;
    public @InjectView(R.id.ivBanner)
    ImageView ivBanner;
    public @InjectView(R.id.tvCartAmount)
    CustomTextView tvCartAmount;
    public @InjectView(R.id.tvCartCount)
    CustomTextView tvCartCount;
    public @InjectView(R.id.rltCheckOut)
    RelativeLayout rltCheckOut;
    public @InjectView(R.id.rltAvialable)
    RelativeLayout rltAvialable;
    public @InjectView(R.id.rltUnavailble)
    RelativeLayout rltUnavailble;
    public @InjectView(R.id.rltRating)
    RelativeLayout rltRating;
    public @InjectView(R.id.tvMenu)
    CustomTextView tvMenu;
    public @InjectView(R.id.nsvScrollView)
    NestedScrollView nsvScrollView;
    public @InjectView(R.id.cdlList)
    CoordinatorLayout cdlList;
    public @InjectView(R.id.menu_layout)
    RelativeLayout menu_layout;

    public @InjectView(R.id.ivWishList)
    ImageView ivWishList;
    public @InjectView(R.id.ivWishListHidden)
    ImageView ivWishListHidden;
    public @InjectView(R.id.rltOfferlay)
    RelativeLayout rltOfferlay;
    public @InjectView(R.id.tvOffertitle)
    CustomTextView tvOffertitle;
    public @InjectView(R.id.tvOfferdesc)
    CustomTextView tvOfferdesc;
    public @InjectView(R.id.tvOfferpercent)
    CustomTextView tvOfferpercent;
    public @InjectView(R.id.tvViewInfo)
    CustomTextView tvViewInfo;
    public @InjectView(R.id.ivUnavailable)
    ImageView ivUnavailable;
    public @InjectView(R.id.tvCurrently_Unavaiable)
    CustomTextView tvCurrently_Unavaiable;
    public @InjectView(R.id.rating_star)
    ImageView rating_star;

    private AlertDialog dialog;
    private Integer restaurantId = 0;
    private RestaurantDetailResultModel restaurantDetailResultModel;
    private RestaurantDetailModel restaurantDetailModel;
    private ArrayList<FoodListModel> foodListModels = new ArrayList<>();
    private ArrayList<RestaurantMenuCategoriesModel> menuCategoriesModels = new ArrayList<>();
    private FoodCategoryListAdapter foodCategoryListAdapter;


    @OnClick(R.id.rltCheckOut)
    public void checkOut() {
        Intent Placeorder = new Intent(getApplicationContext(), PlaceOrderActivity.class);
        startActivity(Placeorder);
    }

    @OnClick(R.id.tvViewInfo)
    public void viewInfo() {
        Intent viewInfo = new Intent(getApplicationContext(), RestaurantInfoActivity.class);
        startActivity(viewInfo);
    }

    @OnClick(R.id.menu_layout)
    public void menuDialogShow() {
        menuList();
    }


    @OnClick(R.id.ivWishList)
    public void ivWishList() {
        if (restaurantDetailModel != null) {
            wishList();
            if (restaurantDetailModel.getWished() == 1) {
                restaurantDetailModel.setWished(0);
                ivWishList.setImageDrawable(getResources().getDrawable(R.drawable.like_white));
                ivWishListHidden.setImageDrawable(getResources().getDrawable(R.drawable.like_white));
            } else {
                restaurantDetailModel.setWished(1);
                ivWishList.setImageDrawable(getResources().getDrawable(R.drawable.like_white_fill));
                ivWishListHidden.setImageDrawable(getResources().getDrawable(R.drawable.like_white_fill));
            }
        }

    }

    @OnClick(R.id.ivWishListHidden)
    public void ivWishListHidden() {
        ivWishList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        FoodCategoryListAdapter.index=0;
        getIntentData();
        if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            Drawable img = getResources().getDrawable(R.drawable.info_next);
            img.setBounds(0, 0, 60, 60);
            tvViewInfo.setCompoundDrawables(null, null, img, null);

            //tvViewInfo.setText(sessionManager.getCurrencySymbol() + "" + total_week_amount);
        }
        else
        {
            Drawable img = getResources().getDrawable(R.drawable.info_next_rtl);
            img.setBounds(0, 0, 60, 60);
            tvViewInfo.setCompoundDrawables(img, null, null, null);

           // tvWeeklyfare.setText(sessionManager.getCurrencySymbol() + "" + total_week_amount);
        }
        initView();

        animateToolBar();

        initRecyclerView();

        getRestaurantDetails();

        recyclerItemClick();

    }


    /**
     * Method to fetch data through intent
     */


    public void getIntentData() {
        restaurantId = sessionManager.getRestaurantId();
    }


    /**
     * Method to intialize view
     */


    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        frame = findViewById(R.id.exploreframe);
        toolbar_layout = findViewById(R.id.toolbar_layout);
        rest_details_back = findViewById(R.id.rest_details_back);
        rest_details_tool_back = findViewById(R.id.rest_details_tool_back);

        cdlList.setVisibility(View.INVISIBLE);
        toolbar_layout.setVisibility(View.GONE);
        //Load animation
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ub__slide_up);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ub__slide_down);

        rest_details_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rest_details_tool_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        appBarLayout = findViewById(R.id.appbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        food_list = findViewById(R.id.food_list);
        category_list = findViewById(R.id.category_list);
        commonMethods.rotateArrow(rest_details_tool_back,this);
        commonMethods.rotateArrow(rest_details_back,this);
        dialog = commonMethods.getAlertDialog(this);
    }


    /**
     * Method to intialize recycler view
     */

    public void initRecyclerView() {
        food_list.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // food_list.setLayoutManager(layoutManager1);
        //food_list.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(this));
        food_list.setLayoutManager(layoutManager1);
        //food_list.setNestedScrollingEnabled(false);

        category_list.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        category_list.setLayoutManager(layoutManager2);

    }

    /**
     * Method to animate tool bar
     */


    private void animateToolBar() {
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        final int height = metrics.heightPixels;

        System.out.println("WIDTH " + width + " Height " + height);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {

                    System.out.println("Collapse verticalOffset" + verticalOffset);
                } else {
                    System.out.println("Expand verticalOffset" + verticalOffset);
                    if (verticalOffset <= -350) {

                        toolbar_layout.setVisibility(View.VISIBLE);
                        toolbar_layout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

                    } else if (verticalOffset >= -349) {
                        toolbar_layout.animate().translationY(-toolbar_layout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sessionManager.getCartCount() > 0) {
            rltCheckOut.setVisibility(View.VISIBLE);
            tvCartAmount.setText(sessionManager.getCurrencySymbol() + sessionManager.getCartAmount());
            tvCartCount.setText(String.valueOf(sessionManager.getCartCount()));
        } else {
            rltCheckOut.setVisibility(View.GONE);
        }
        MainActivity.isRefreshed = true;
    }

    /**
     * Call API to get restaurant list
     */
    private void getRestaurantDetails() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.getStoreDetails(restaurantId, sessionManager.getToken(), sessionManager.getOrderType()).enqueue(new RequestCallback(Enums.REQ_RESTAURANT_DETAILS, this));
    }

    private void wishList() {
        apiService.wishList(restaurantId, sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_WISH_LIST, this));
    }

    /**
     * Get Success response from API
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case Enums.REQ_RESTAURANT_DETAILS:
                if (jsonResp.isSuccess()) {
                    cdlList.setVisibility(View.VISIBLE);
                    onSuccessGetRestaurantList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    //commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    onErrorMessageDisplay(jsonResp);
                }
                break;
            case Enums.REQ_WISH_LIST:
                /*if (jsonResp.isSuccess()) {
                    //onSuccessGetRestaurantList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    //commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }*/
                break;
            default:
                break;

        }
    }

    /**
     * Get Failure response from API
     */
    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    private void onErrorMessageDisplay(JsonResponse jsonResponse) {
        TooFarDetailModel tooFarDetailModel = gson.fromJson(jsonResponse.getStrResponse(), TooFarDetailModel.class);
        System.out.println("tooFarDetailModel.getStatusCode " + tooFarDetailModel.getStatusCode());
        if (Integer.valueOf(tooFarDetailModel.getStatusCode()) == 2 || Integer.valueOf(tooFarDetailModel.getStatusCode()) == 3) {
            showAlert(jsonResponse);
        } else {
            commonMethods.showMessage(this, dialog, jsonResponse.getStatusMsg());

        }
    }

    private void onSuccessGetRestaurantList(JsonResponse jsonResponse) {
        restaurantDetailResultModel = gson.fromJson(jsonResponse.getStrResponse(), RestaurantDetailResultModel.class);
        if (restaurantDetailResultModel != null) {
            updateView();
        }

    }


    /**
     * update views based on api response
     */

    private void updateView() {

        restaurantDetailModel = restaurantDetailResultModel.getRestaurantDetails();
        tvRestaurantName.setText(restaurantDetailModel.getRestaurantName());
        tvRestaurantNameToolbar.setText(restaurantDetailModel.getRestaurantName());
        String category = restaurantDetailModel.getCategory();
        category = category.replaceAll(",", " • ");
        if (restaurantDetailModel.getPriceRating() == 1) {
            tvRestaurantCategory.setText(sessionManager.getCurrencySymbol() + " • " + category);
        } else if (restaurantDetailModel.getPriceRating() == 2) {
            tvRestaurantCategory.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + " • " + category);
        } else if (restaurantDetailModel.getPriceRating() == 3) {
            tvRestaurantCategory.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + " • " + category);
        } else if (restaurantDetailModel.getPriceRating() == 4) {
            tvRestaurantCategory.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + " • " + category);
        } else {
            tvRestaurantCategory.setText(category);
        }
        /*if (restaurantDetailModel.getPriceRating() == 1) {
            tvRestaurantCategory.setText(category + " • " + sessionManager.getCurrencySymbol());
        } else if (restaurantDetailModel.getPriceRating() == 2) {
            tvRestaurantCategory.setText(category + " • " + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol());
        } else if (restaurantDetailModel.getPriceRating() == 3) {
            tvRestaurantCategory.setText(category + " • " + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol());
        } else if (restaurantDetailModel.getPriceRating() == 4) {
            tvRestaurantCategory.setText(category + " • " + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol());
        } else {
            tvRestaurantCategory.setText(category);
        }*/
        //tvRestaurantCategory.setText(category);
        if (restaurantDetailModel.getRestaurantClosed() == 1) {
            if (restaurantDetailModel.getStatus() == 1) {
                rltAvialable.setVisibility(View.VISIBLE);
                rltUnavailble.setVisibility(View.GONE);
                if (sessionManager.getOrderType() == 1) {
                    if (sessionManager.getHomeScheduledDay().equalsIgnoreCase("Today") || sessionManager.getHomeScheduledDay().equalsIgnoreCase("Tomorrow")) {
                        tvRestaurantTime.setText(sessionManager.getHomeScheduledDay() + " - " + sessionManager.getAddedTime());
                    } else {
                        tvRestaurantTime.setText(sessionManager.getScheduleDate() + " - " + sessionManager.getAddedTime());
                    }
                } else {
                    if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                        tvRestaurantTime.setText(restaurantDetailModel.getMaxTime() + "-" + restaurantDetailModel.getMinTime() + " " + getResources().getString(R.string.mins_s));
                    }else {
                        tvRestaurantTime.setText(restaurantDetailModel.getMinTime() + "-" + restaurantDetailModel.getMaxTime() + " " + getResources().getString(R.string.mins_s));
                    }

                }
            } else {
                restaurantDetailModel.setStatus(0);
                rltUnavailble.setVisibility(View.VISIBLE);
                rltAvialable.setVisibility(View.GONE);
            }
        } else {
            ivUnavailable.setVisibility(View.GONE);
            rltUnavailble.setVisibility(View.VISIBLE);
            tvCurrently_Unavaiable.setAllCaps(false);
            tvCurrently_Unavaiable.setText(restaurantDetailModel.getRestaurantNextTime());
            rltAvialable.setVisibility(View.GONE);
            restaurantDetailModel.setStatus(0);
        }

        //restaurantDetailModel.setStatus(restaurantDetailModel.getRestaurantClosed());

        if (restaurantDetailModel.getAverageRating() > 0) {
            rltRating.setVisibility(View.VISIBLE);
            tvRating.setText(String.valueOf(restaurantDetailModel.getRestaurantRating()));
            tvRatingUser.setText("(" + String.valueOf(restaurantDetailModel.getAverageRating()) + ")");
            if (restaurantDetailModel.getRestaurantRating() >= 4.5) {
                rating_star.setImageDrawable(getResources().getDrawable(R.drawable.star_two_gold));
            } else {
                rating_star.setImageDrawable(getResources().getDrawable(R.drawable.star_two));
            }
        } else {
            rltRating.setVisibility(View.GONE);
        }
        updateWishList();
        //tvRating.setText(restaurantDetailModel.ger);
        //tvRatingUser;
        imageUtils.loadImage(this, ivBanner, restaurantDetailModel.getBannerList().getOriginal());


        // Under Restaurant List update
        if (restaurantDetailModel.getRestaurantMenuDetails() != null && (restaurantDetailModel.getRestaurantMenuDetails().size() > 0)) {
            foodListModels.clear();
            menuCategoriesModels.clear();

            for (int i = 0; i < restaurantDetailModel.getRestaurantMenuDetails().size(); i++) {
                if (restaurantDetailModel.getRestaurantMenuDetails().get(i).getMenuClosed() == 1) {
                    menuPosition = i;
                    break;
                }
            }

            restaurantMenuModel = restaurantDetailModel.getRestaurantMenuDetails();
            tvMenu.setText(restaurantMenuModel.get(menuPosition).getMenuName());
            restaurantMenuModel.get(menuPosition).getMenuEndTime();

            if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))){
                tvRestaurantCategory.setTextDirection(View.TEXT_DIRECTION_RTL);
                //holder.time.setTextDirection(View.TEXT_DIRECTION_RTL);
            }

            loadItem(menuPosition);
        }
    }

    public boolean checktime(String endtime) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        //SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm") ;
        dateFormat.format(date);

        try {
            if (dateFormat.parse(dateFormat.format(date)).after(dateFormat.parse(endtime))) {
                System.out.println("Current time is greater than " + endtime);
                return true;
            } else {
                System.out.println("Current time is less than " + endtime);
                return false;
            }
        } catch (ParseException p) {
            p.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(JsonResponse jsonResponse) {
        String statusCode = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "status_code", String.class);
        String message = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "messages", String.class);
        final String cusine = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), "category", String.class);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.no_restuarant_alert, null, false);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);

        CustomTextView tvTooFar = dialogView.findViewById(R.id.tvTooFar);
        CustomTextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        CustomButton btnClose = dialogView.findViewById(R.id.btnClose);
        CustomButton btnSimliar = dialogView.findViewById(R.id.btnSimliar);

        if (Integer.valueOf(statusCode) == 3) {
            tvTooFar.setText(getResources().getString(R.string.currently_unavailable));
        }
        tvMessage.setText(message);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                onBackPressed();
            }
        });

        btnSimliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setSearchCuisine("1");
                sessionManager.setCuisineName(cusine);
                Intent search = new Intent(getApplicationContext(), MainActivity.class);
                search.putExtra("type", "search");
                startActivity(search);
                alertDialog.dismiss();
            }
        });

    }

    /**
     * To Load items based on dropn down select
     *
     * @param posotion
     */


    public void loadItem(int posotion) {
        foodListModels.clear();
        menuCategoriesModels.clear();
        //for(int k=0;k<restaurantMenuModel.size();k++) {
        if (restaurantMenuModel.get(posotion).getRestaurantMenuCategoriesDetails() != null && (restaurantMenuModel.get(posotion).getRestaurantMenuCategoriesDetails().size() > 0)) {
            ArrayList<RestaurantMenuCategoriesModel> restaurantMenuCategoriesModel = restaurantMenuModel.get(posotion).getRestaurantMenuCategoriesDetails();
            for (int c = 0; c < restaurantMenuCategoriesModel.size(); c++) {
                if (restaurantMenuCategoriesModel.get(c).getMenuItemDetails().size() > 0)
                    menuCategoriesModels.add(restaurantMenuCategoriesModel.get(c));
            }

            ArrayList<RestOfferModel> restaurantOffer = restaurantDetailModel.getRestaurantOfferDetails();
            for (int i = 0; i < restaurantMenuCategoriesModel.size(); i++) {
                if (restaurantMenuCategoriesModel.get(i).getMenuItemDetails() != null && (restaurantMenuCategoriesModel.get(0).getMenuItemDetails().size() > 0)) {
                    ArrayList<RestaurantMenuItemModel> restaurantMenuItemModels = restaurantMenuCategoriesModel.get(i).getMenuItemDetails();
                    if (restaurantMenuItemModels != null && restaurantMenuItemModels.size() > 0) {
                        System.out.println("FSizes  " + restaurantMenuItemModels.size());
                        for (int j = 0; j < restaurantMenuItemModels.size(); j++) {
                            // Add menu name like Diffen,Lunch,Dinner, Brunch
                                    /*if (i == 0&&j==0) {
                                        FoodListModel foodListModel = new FoodListModel();
                                        foodListModel.setType("Menu");
                                        foodListModel.setMenuCategoryId(0);
                                        foodListModel.setMenuCategoryName(restaurantMenuModel.get(i).getMenuName());
                                        foodListModel.setMenuId(restaurantMenuCategoriesModel.get(i).getMenuId());
                                        foodListModels.add(foodListModel);
                                    }*/
                            // Add category name like ICE Cream, Juice, Burger, smoothies
                            if (j == 0) {
                                FoodListModel foodListModel = new FoodListModel();
                                foodListModel.setType("Category");
                                foodListModel.setCategoryPosition(i);
                                foodListModel.setMenuCategoryId(restaurantMenuCategoriesModel.get(i).getMenuCategoryId());
                                foodListModel.setMenuCategoryName(restaurantMenuCategoriesModel.get(i).getMenuCategoryName());
                                foodListModel.setMenuId(restaurantMenuCategoriesModel.get(i).getMenuId());
                                foodListModel.setMenuClosed(restaurantMenuModel.get(posotion).getMenuClosed());
                                foodListModels.add(foodListModel);
                            }

                            //System.out.println("REST IMAGE LiST " + restaurantMenuItemModels.get(j).getFoodImage());
                            // Add Food details
                            FoodListModel foodListModel = new FoodListModel();
                            foodListModel.setType("Item");
                            foodListModel.setMenuCategoryId(restaurantMenuItemModels.get(j).getMenuCategoryId());
                            foodListModel.setMenuCategoryName("");
                            foodListModel.setMenuId(restaurantMenuItemModels.get(j).getMenuId());
                            foodListModel.setFoodId(restaurantMenuItemModels.get(j).getFoodId());
                            foodListModel.setFoodName(restaurantMenuItemModels.get(j).getFoodName());
                            foodListModel.setFoodDescription(restaurantMenuItemModels.get(j).getFoodDescription());
                            foodListModel.setFoodPrice(restaurantMenuItemModels.get(j).getFoodPrice());
                            foodListModel.setOfferPrice(restaurantMenuItemModels.get(j).getOfferPrice());
                            foodListModel.setIsOffer(restaurantMenuItemModels.get(j).getIsOffer());
                            //foodListModel.setFoodImage(restaurantMenuItemModels.get(j).getFoodImage());
                            foodListModel.setMenuImage(restaurantMenuItemModels.get(j).getMenuImage());
                            foodListModel.setIsAvailable(restaurantMenuItemModels.get(j).getIsAvailable());
                            foodListModel.setIsVeg(restaurantMenuItemModels.get(j).getIsVeg());
                            foodListModel.setStatus(restaurantMenuItemModels.get(j).getStatus());
                            //foodListModel.setIsVeg(0);
                            foodListModel.setTaxPercentage(restaurantMenuItemModels.get(j).getTaxPercentage());
                            foodListModel.setOrderItemId(0);
                            foodListModels.add(foodListModel);
                        }
                        if (foodListModels.size() > 0) {
                            System.out.println("FSize  " + restaurantMenuItemModels.size());
                            int status = 1;
                            if (restaurantDetailModel.getStatus() == 0 || restaurantMenuModel.get(posotion).getMenuClosed() == 0) {
                                status = 0;
                            }
                            FoodListAdapter foodListAdapter = new FoodListAdapter(this, food_list, foodListModels, restaurantMenuItemModels, status);
                            food_list.setAdapter(foodListAdapter);
                        }

                        if (menuCategoriesModels.size() > 0) {
                            int m = 0;
                            for (int c = 0; c < foodListModels.size(); c++) {
                                if (foodListModels.get(c).getType().equalsIgnoreCase("Category")) {
                                    menuCategoriesModels.get(m).setCategoryPosition(c);
                                    m++;
                                }
                            }
                            foodCategoryListAdapter = new FoodCategoryListAdapter(this, menuCategoriesModels);
                            category_list.setAdapter(foodCategoryListAdapter);
                        }
                        if (restaurantOffer.size() > 0 && restaurantOffer.get(0).getPercentage() > 0) {
                            rltOfferlay.setVisibility(View.VISIBLE);
                            tvOffertitle.setText(restaurantOffer.get(0).getTitle());
                            tvOfferdesc.setText(restaurantOffer.get(0).getDescription());
                            tvOfferpercent.setText(restaurantOffer.get(0).getPercentage() + " " + "%");
                        }
                    }
                }
            }
        }
        //}
    }

    /**
     * To update wishlist
     */


    public void updateWishList() {
        if (restaurantDetailModel.getWished() == 0) {
            restaurantDetailModel.setWished(0);
            ivWishListHidden.setImageDrawable(getResources().getDrawable(R.drawable.like_white));
        } else {
            restaurantDetailModel.setWished(1);
            ivWishListHidden.setImageDrawable(getResources().getDrawable(R.drawable.like_white_fill));
        }

        if (restaurantDetailModel.getWished() == 0) {
            restaurantDetailModel.setWished(0);
            ivWishList.setImageDrawable(getResources().getDrawable(R.drawable.like_white));
        } else {
            restaurantDetailModel.setWished(1);
            ivWishList.setImageDrawable(getResources().getDrawable(R.drawable.like_white_fill));
        }
    }

    /*
    *   Food and category list click listener search from google API
    */
    public void recyclerItemClick() {

       /* food_list.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(),"Scrolled Food",Toast.LENGTH_SHORT).show();
                        food_list.scrollToPosition(8);
                        category_list.scrollToPosition(menuCategoriesModels.size()-1);
                    }
                })
        );*/


        /*nsvScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                if (v.getChildAt(v.getChildCount() - 1) != null) {

                    LinearLayoutManager layoutManager = (LinearLayoutManager) food_list.getLayoutManager();

                    int firstVisible = layoutManager.findFirstVisibleItemPosition();
                    System.out.println(" firstVisible " + firstVisible);

                    int visibleItemCounts = layoutManager1.getChildCount();
                    int totalItemCounts = layoutManager1.getItemCount();
                    int firstVisibleItems = layoutManager1.findFirstVisibleItemPosition();

                    System.out.println(" visibleItemCounts " + visibleItemCounts);
                    System.out.println(" totalItemCounts " + totalItemCounts);
                    System.out.println(" firstVisibleItems " + firstVisibleItems);

                   *//* if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {*//*

                        int visibleItemCount = layoutManager1.getChildCount();
                        int totalItemCount = layoutManager1.getItemCount();
                        int firstVisibleItem = layoutManager1.findLastVisibleItemPosition();

                        System.out.println(" visibleItemCount " + visibleItemCount);
                        System.out.println(" totalItemCount " + totalItemCount);
                        System.out.println(" firstVisibleItem " + firstVisibleItem);

                        int firstVisibleItem1 = food_list.getChildCount();
                        int adaptercount = foodListAdapter.getItemCount();

                        System.out.println(" firstVisibleItems " + firstVisibleItems);
                        System.out.println(" adaptercount " + adaptercount);

                            if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {

                            }

                        System.out.println(" First visible item" + firstVisibleItem);
                        if (firstVisibleItem >= 0) {
                            float y = 0;
                            int catPosition;

                            catPosition = foodListModels.get(firstVisibleItem).getCategoryPosition();
                            System.out.println(" Cat position " + catPosition + " First visible item" + firstVisibleItem);
                            if (catPosition == firstVisibleItem) {
                                foodCategoryListAdapter.index = catPosition;
                                foodCategoryListAdapter.notifyDataSetChanged();
                                category_list.scrollToPosition(catPosition);
                                System.out.println(" 1Cat position " + catPosition + " First visible item" + firstVisibleItem);
                            } else {
                                foodCategoryListAdapter.index = catPosition;
                                foodCategoryListAdapter.notifyDataSetChanged();
                                category_list.scrollToPosition(catPosition);
                                System.out.println(" 2Cat position " + catPosition + " First visible item" + firstVisibleItem);
                            }
                        }
                        //}
                    }
                //}
            }
        });*/


        category_list.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        FoodCategoryListAdapter.index = position;
                        foodCategoryListAdapter.notifyDataSetChanged();

                        float y = 0;
                        int catPosition = 0;
                        catPosition = menuCategoriesModels.get(position).getCategoryPosition();
                        if (catPosition != categoryPosition) {
                            if (catPosition > categoryPosition) {
                                categoryPosition = catPosition;
                                y = food_list.getChildAt(categoryPosition).getY();

                                int move = (int) y - scrollHeight;
                                System.out.println(">MOVE " + move);
                                scrollHeight = (int) y;

                                nsvScrollView.smoothScrollTo(0, move);
                            } else {
                                categoryPosition = catPosition;
                                nsvScrollView.smoothScrollTo(0, 0);
                                scrollHeight = 0;
                                y = food_list.getChildAt(categoryPosition).getY();
                                scrollHeight = (int) y;
                                int move = (int) y;
                                System.out.println(">MOVE " + move);
                                nsvScrollView.smoothScrollTo(0, move);
                            }
                        }

                                /*int height=(position-1)*food_list.getLayoutManager().getChildAt(0).getMeasuredHeight();
                                int height1=(categoryPosition-position)*food_list.getLayoutManager().getChildAt(1).getMeasuredHeight();
                                System.out.println("food List height "+height+" "+height1);
                                nsvScrollView.smoothScrollTo(0, height+height1);*/
                    }
                }, 200);


            }
        }));
    }


    /**
     * To Show list of menu items available using bottom sheet
     */

    public void menuList() {

        menuDialog = new BottomSheetDialog(this);
        menuDialog.setContentView(R.layout.menu_dialog);
        menuDialog.setTitle(getResources().getString(R.string.menu));
        menuInject = new MenuInject();


        // 5. We bind the elements of the included layouts.
        ButterKnife.inject(menuInject, menuDialog);
        menuInject.setData();

        menuDialog.show();
    }

    /**
     * Annotation  using ButterKnife lib to Injection and OnClick for Accept or pause dialog
     **/
    public class MenuInject {

        public @InjectView(R.id.rvMenuList)
        CustomRecyclerView rvMenuList;


        public void setData() {
            rvMenuList.setHasFixedSize(false);
            RecyclerView.LayoutManager layoutManager0 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            rvMenuList.setLayoutManager(layoutManager0);

            if (restaurantMenuModel.size() > 0) {
                MenuListAdapter menuListAdapter = new MenuListAdapter(getApplicationContext(), restaurantMenuModel, menuPosition);
                rvMenuList.setAdapter(menuListAdapter);
            }

            rvMenuList.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    menuPosition = position;
                    menuDialog.dismiss();
                    loadItem(menuPosition);
                    tvMenu.setText(restaurantMenuModel.get(menuPosition).getMenuName());

                }
            }));
        }
    }

}
