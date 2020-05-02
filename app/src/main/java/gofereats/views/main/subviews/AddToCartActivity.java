package gofereats.views.main.subviews;


/**
 * @package com.gofereats
 * @subpackage views.subviews
 * @category Stripe Activity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.restaurantdetails.FoodListModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.Enums;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;


/* ************************************************************
                Add To Card for Activity
    *********************************************************** */


public class AddToCartActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CustomDialog customDialog1;
    public @Inject
    CustomDialog customDialogClear;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @Inject
    ImageUtils imageUtils;
    public @InjectView(R.id.tvMinus)
    TextView tvMinus;
    public @InjectView(R.id.tvPlus)
    TextView tvPlus;
    public @InjectView(R.id.tvCount)
    TextView tvCount;
    public @InjectView(R.id.tvAddToCart)
    TextView tvAddToCart;
    public @InjectView(R.id.tvCartAmount)
    TextView tvCartAmount;
    public @InjectView(R.id.edtSpecialNote)
    EditText edtSpecialNote;
    public @InjectView(R.id.rltAddCart)
    RelativeLayout rltAddCart;
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.ivFoodImage)
    ImageView ivFoodImage;
    public @InjectView(R.id.tvFoodName)
    CustomTextView tvFoodName;
    public @InjectView(R.id.rltVeg)
    RelativeLayout rltVeg;
    public @InjectView(R.id.tvFoodDesc)
    CustomTextView tvFoodDesc;
    public @InjectView(R.id.tvSoldOut)
    CustomTextView tvSoldOut;
    public @InjectView(R.id.tvremove)
    CustomTextView tvRemove;
    public @InjectView(R.id.roomsdetailsnestedscrool)
    ScrollView scrollView;
    public @InjectView(R.id.vwShadeToolbar)
    View vwShadeToolbar;

    public int foodCount = 1;
    public Double foodTotalAmount = 0.0;
    public int type = 0;
    public int status = 0;
    private AlertDialog dialog;
    private FoodListModel foodListModel;

    @OnClick(R.id.tvMinus)
    public void setMinus() {
        if (foodCount == 1) {
            tvCount.setText("1");
           /* if (!"0".equals(foodListModel.getOfferPrice()))
                foodTotalAmount = Double.valueOf(foodListModel.getOfferPrice());
            else foodTotalAmount = Double.valueOf(foodListModel.getFoodPrice());
            tvCartAmount.setText(sessionManager.getCurrencySymbol() + String.valueOf(foodTotalAmount));*/
        } else {
            foodCount = foodCount - 1;
            tvCount.setText(String.valueOf(foodCount));
            //if (!"0".equals(foodListModel.getOfferPrice()))
            if (foodListModel.getIsOffer() == 1) {
                foodTotalAmount = foodTotalAmount - Double.valueOf(foodListModel.getOfferPrice());
            } else {
                foodTotalAmount = foodTotalAmount - Double.valueOf(foodListModel.getFoodPrice());
            }

            DecimalFormat df = new DecimalFormat("####0.00");
            DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
            dfs.setDecimalSeparator('.');
            df.setDecimalFormatSymbols(dfs);
            tvCartAmount.setText(sessionManager.getCurrencySymbol() + String.valueOf(df.format(foodTotalAmount)));
        }
        if (foodListModel.getOrderItemId() == 0) {
            tvAddToCart.setText(getResources().getString(R.string.add_to_basket,String.valueOf(foodCount)));
        } else {
            tvAddToCart.setText(getResources().getString(R.string.update_to_basket,String.valueOf(foodCount)));
        }
    }

    @OnClick(R.id.tvPlus)
    public void setPlus() {
        foodCount = foodCount + 1;
        tvCount.setText(String.valueOf(foodCount));
        //if (!"0".equals(foodListModel.getOfferPrice()))
        if (foodListModel.getIsOffer() == 1) {
            foodTotalAmount = foodTotalAmount + Double.valueOf(foodListModel.getOfferPrice());
        } else {
            foodTotalAmount = foodTotalAmount + Double.valueOf(foodListModel.getFoodPrice());
        }
        DecimalFormat df = new DecimalFormat("####0.00");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        tvCartAmount.setText(sessionManager.getCurrencySymbol() + String.valueOf(df.format((foodTotalAmount))));

        if (foodListModel.getOrderItemId() == 0) {
            tvAddToCart.setText(getResources().getString(R.string.add_to_basket,String.valueOf(foodCount)));
        } else {
            tvAddToCart.setText(getResources().getString(R.string.update_to_basket,String.valueOf(foodCount)));
        }
    }

    @OnClick(R.id.rltAddCart)
    public void addCart() {
        addToCart();
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.tvremove)
    public void clear() {
        clearCart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);
        MainActivity.isRefreshed = true;
        System.out.println("Food Count: "+String.valueOf(foodCount));
        System.out.println("Add to Basket :"+getResources().getString(R.string.add_to_basket));
        tvAddToCart.setText(getResources().getString(R.string.add_to_basket,String.valueOf(foodCount)));
        commonMethods.rotateArrow(ivBack,this);
        scroll();
        loadView();

    }

    public void scroll() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                int scrollY = scrollView.getScrollY();
                Log.d("scrollView", "scrollY: " + scrollY);
                if (scrollY > 340) {
                    ivBack.setImageDrawable(getResources().getDrawable(R.drawable.backarrow_black));
                } else {
                    ivBack.setImageDrawable(getResources().getDrawable(R.drawable.backarrow_white));
                }
            }
        });
    }

    /**
     * To Load view while opening an Activity
     */


    public void loadView() {

        dialog = commonMethods.getAlertDialog(this);

        foodListModel = (FoodListModel) getIntent().getSerializableExtra("foodList");
        type = getIntent().getIntExtra("type", 0);
        status = getIntent().getIntExtra("status", 0);
        if (type > 0) {
            tvRemove.setVisibility(View.VISIBLE);
        } else {
            tvRemove.setVisibility(View.GONE);
        }
        if (status == 0) {
            tvMinus.setTextColor(getResources().getColor(R.color.gray_color));
            tvCount.setTextColor(getResources().getColor(R.color.gray_color));
            tvPlus.setTextColor(getResources().getColor(R.color.gray_color));
            tvMinus.setEnabled(false);
            tvPlus.setEnabled(false);
            tvCount.setEnabled(false);
            rltAddCart.setVisibility(View.GONE);

        }
        if (foodListModel.getOrderItemId() == 0) {
            if (foodListModel.getIsOffer() == 1) {
                foodTotalAmount = Double.valueOf(foodListModel.getOfferPrice());
            } else {
                foodTotalAmount = Double.valueOf(foodListModel.getFoodPrice());
            }
            tvCartAmount.setText(sessionManager.getCurrencySymbol() + String.valueOf(foodTotalAmount));
        } else {
            foodTotalAmount = Double.valueOf(foodListModel.getTotalAmount());
            tvCartAmount.setText(sessionManager.getCurrencySymbol() + String.valueOf(foodTotalAmount));
            foodCount = foodListModel.getQuantity();
            tvCount.setText(String.valueOf(foodCount));
            tvAddToCart.setText(getResources().getString(R.string.update_to_basket,String.valueOf(foodCount)));
            edtSpecialNote.setText(foodListModel.getSpecialNotes());
        }


        if (TextUtils.isEmpty(foodListModel.getMenuImage().getLargeImage())) {
            ivBack.setImageDrawable(getResources().getDrawable(R.drawable.backarrow_black));
            ivFoodImage.getLayoutParams().height = 150;
            ivFoodImage.requestLayout();
            ivFoodImage.setBackgroundColor(getResources().getColor(R.color.white));
        }

        Glide.with(this).load(foodListModel.getMenuImage().getLargeImage()).into(ivFoodImage);
        tvFoodName.setText(foodListModel.getFoodName());
        if (!TextUtils.isEmpty(foodListModel.getFoodDescription())) {
            tvFoodDesc.setText(foodListModel.getFoodDescription());
        }
        /*if (foodListModel.getIsVeg() == 0) {
            rltVeg.setVisibility(View.GONE);
        } else {
            rltVeg.setVisibility(View.GONE);
        }*/

        if (foodListModel.getIsAvailable() == 1 && foodListModel.getStatus() == 1) {
            tvSoldOut.setVisibility(View.GONE);
        } else {
            tvSoldOut.setVisibility(View.VISIBLE);
            /*holder1.ivFoodImageBlur.setVisibility(View.VISIBLE);
            holder1.food_name.setTextColor(context.getResources().getColor(R.color.category_hint));
            holder1.food_amount.setTextColor(context.getResources().getColor(R.color.category_hint));
            holder1.food_description.setTextColor(context.getResources().getColor(R.color.category_hint));*/
        }
    }

    /**
     * Call API to get restaurant list
     */
    private void addToCart() {
        commonMethods.showProgressDialog(this, customDialog);
        if (foodListModel.getOrderItemId() == null || foodListModel.getOrderItemId() == 0)
            apiService.addToCart(sessionManager.getRestaurantId(), foodListModel.getFoodId(), foodCount, edtSpecialNote.getText().toString(), sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_ADD_CART, this));
        else
            apiService.addToCart(sessionManager.getRestaurantId(), foodListModel.getFoodId(), foodListModel.getOrderItemId(), foodCount, edtSpecialNote.getText().toString(), sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_ADD_CART, this));
    }

    /**
     * Call API to Clear all cart
     */
    private void clearAllCart() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.clearAllCart(sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_CLEAR_ALL_CART, this));
    }

    /**
     * Call API to Remove selected one car
     */
    private void clearCart() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.clearCart(foodListModel.getOrderItemId(), foodListModel.getOrderId(), sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_CLEAR_CART, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        System.out.println("Response "+jsonResp.getStrResponse());
        String statusCode = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "status_code", String.class);
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case Enums.REQ_ADD_CART:
                if (jsonResp.isSuccess()) {
                    int cartCount = (int) commonMethods.getJsonValue(jsonResp.getStrResponse(), "quantity", Integer.class);
                    String cartAmount = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "subtotal", String.class);
                    sessionManager.setCartCount(cartCount);
                    sessionManager.setCartAmount(cartAmount);
                    onBackPressed();
                } else if (Integer.valueOf(statusCode) == 2) {
                    showDialogToClear(jsonResp.getStatusMsg(), getResources().getString(R.string.ok));
                } else if (Integer.valueOf(statusCode) == 0) {
                    showDialog(getResources().getString(R.string.clear_cart_title), getResources().getString(R.string.clear_cart_msg), getResources().getString(R.string.clear_cart));
                }
                break;
            case Enums.REQ_CLEAR_ALL_CART:
                if (jsonResp.isSuccess()) {
                    addToCart();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case Enums.REQ_CLEAR_CART:
                if (jsonResp.isSuccess()) {
                    onBackPressed();
                    sessionManager.setCartCount(sessionManager.getCartCount() - foodCount);
                    double remaining = Double.valueOf(sessionManager.getCartAmount()) - foodTotalAmount;
                    sessionManager.setCartAmount(String.valueOf(remaining));
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
     * Show dialog while first swipe or click the bottom images
     */
    private void showDialog(String title, String msg, String buttonText) {
        customDialog1 = new CustomDialog(title, msg, buttonText, getResources().getString(R.string.cancel), new CustomDialog.btnAllowClick() {
            @Override
            public void clicked() {
                clearAllCart();
            }
        }, new CustomDialog.btnDenyClick() {
            @Override
            public void clicked() {
                //if(customDialog.isVisible())
                customDialog1.dismiss();
            }
        });
        customDialog1.show(getSupportFragmentManager(), "");
    }


    /**
     * Show dialog while Menu Not Available in Add to Card Page
     */
    private void showDialogToClear(String title, String buttonText) {
        customDialogClear = new CustomDialog(title, buttonText, new CustomDialog.btnAllowClick() {
            @Override
            public void clicked() {
                onBackPressed();
            }
        });
        customDialogClear.show(getSupportFragmentManager(), "");
    }
}
