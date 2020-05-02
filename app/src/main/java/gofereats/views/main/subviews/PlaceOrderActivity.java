package gofereats.views.main.subviews;

/**
 *
 * @package com.gofereats
 * @subpackage views.subviews
 * @category PlaceorderActivity
 * @author Trioangle Product Team
 * @version 0.6
 */


/*************************************************************
 Place the order
 ************************************************************/

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.cart.AddressModel;
import gofereats.datamodels.cart.CartDetailModel;
import gofereats.datamodels.cart.CartRestaurantModel;
import gofereats.datamodels.cart.ViewCartModel;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.main.home.RestaurantModel;
import gofereats.datamodels.placeorder.PlaceOrderModel;
import gofereats.datamodels.profile.UserProfileModel;
import gofereats.datamodels.restaurantdetails.FoodListModel;
import gofereats.datamodels.restaurantdetails.RestaurantMenuItemModel;
import gofereats.datamodels.unavailablemenus.UnavailableMenus;
import gofereats.datamodels.unavailablemenus.UnavailableModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.Enums;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;

public class PlaceOrderActivity extends AppCompatActivity implements ServiceListener {

    public static ArrayList<RestaurantModel> RestaurantModels = new ArrayList<>();
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
    public @InjectView(R.id.tvRestaurantName)
    CustomTextView tvRestaurantName;
    public @InjectView(R.id.tvDeliverType)
    CustomTextView tvDeliverType;
    public @InjectView(R.id.tvDeliverTime)
    CustomTextView tvDeliverTime;
    public @InjectView(R.id.ivMapImage)
    ImageView ivMapImage;
    public @InjectView(R.id.tvDeliveryAddress)
    CustomTextView tvDeliveryAddress;
    public @InjectView(R.id.tvDeliveryType)
    CustomTextView tvDeliveryType;
    public @InjectView(R.id.edtSpecialNote)
    EditText edtSpecialNote;
    public @InjectView(R.id.tvSubTotal)
    CustomTextView tvSubTotal;
    public @InjectView(R.id.tvTax)
    CustomTextView tvTax;
    public @InjectView(R.id.tvDeliveryPrice)
    CustomTextView tvDeliveryPrice;
    public @InjectView(R.id.tvTotal)
    CustomTextView tvTotal;
    public @InjectView(R.id.rltDeliverFee)
    RelativeLayout rltDeliverFee;
    public @InjectView(R.id.rltTax)
    RelativeLayout rltTax;
    public @InjectView(R.id.tvDeliveryNotes)
    CustomTextView tvDeliveryNotes;
    public @InjectView(R.id.tvPromoApplied)
    CustomTextView tvPromoApplied;
    public @InjectView(R.id.rltPromo)
    RelativeLayout rltPromo;
    public @InjectView(R.id.tvDiscountPrice)
    CustomTextView tvDiscountPrice;
    public @InjectView(R.id.place_order_text)
    CustomTextView place_order_text;
    public @InjectView(R.id.rltWallet)
    RelativeLayout rltWallet;
    public @InjectView(R.id.tvWalletAmount)
    CustomTextView tvWalletAmount;
    public @InjectView(R.id.rltServiceFee)
    RelativeLayout rltServiceFee;
    public @InjectView(R.id.tvServiceAmount)
    CustomTextView tvServiceAmount;
    public @InjectView(R.id.rltChangePaymentMethod)
    RelativeLayout rltChangePaymentMethod;
    public @InjectView(R.id.tvPaymentMethod)
    CustomTextView tvPaymentMethod;
    public @InjectView(R.id.ivPaymentImage)
    ImageView ivPaymentImage;
    public @InjectView(R.id.ivWalletImage)
    ImageView ivWalletImage;
    public @InjectView(R.id.rltChangeAddress)
    RelativeLayout rltChangeAddress;
    public @InjectView(R.id.rltcart)
    RelativeLayout rltcart;
    public @InjectView(R.id.rltnoitem)
    RelativeLayout rltnoitem;
    public @InjectView(R.id.rltDeliveryDetails)
    RelativeLayout rltDeliveryDetails;
    public @InjectView(R.id.rltOrderType)
    RelativeLayout rltOrderType;
    public @InjectView(R.id.ivOrderType)
    ImageView ivOrderType;
    public @InjectView(R.id.tvOrderType)
    CustomTextView tvOrderType;
    public @InjectView(R.id.rltDeliver)
    RelativeLayout rltDeliver;
    public @InjectView(R.id.rltPenalityFee)
    RelativeLayout rltPenalityFee;
    public @InjectView(R.id.tvPenalityPrice)
    CustomTextView tvPenalityPrice;

    public RecyclerView sugguestion_list;
    public RelativeLayout add_promo_code;
    public RelativeLayout place_order_layout;
    public LinearLayout order_list_layout;
    public ImageView arrow;
    public UserProfileModel userProfileModel;
    public PlaceOrderModel placeOrderModel;
    public String statusCode;
    private AlertDialog dialog;
    private ArrayList<RestaurantMenuItemModel> restaurantMenuItemModel;
    private CartDetailModel cartDetailModel;
    private ArrayList<UnavailableMenus> unavailableMenusArrayList = new ArrayList<>();

    @OnClick(R.id.rltChangePaymentMethod)
    public void changePaymentMethod() {
        Intent changepayment = new Intent(getApplicationContext(), PaymentActivity.class);
        changepayment.putExtra("changepayment", 2);
        startActivity(changepayment);
    }

    @OnClick(R.id.rltOrderType)
    public void orderType() {
        Intent orderType = new Intent(getApplicationContext(), LocActivity.class);
        orderType.putExtra("location", "change");
        orderType.putExtra("type", 2);
        startActivity(orderType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        order_list_layout = findViewById(R.id.order_list_layout);
        arrow = findViewById(R.id.arrow);
        add_promo_code = findViewById(R.id.add_promo_code);
        sugguestion_list = findViewById(R.id.sugguestion_list);
        place_order_layout = findViewById(R.id.place_order_layout);
        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(arrow,this);
        sugguestion_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        sugguestion_list.setLayoutManager(layoutManager);

        RestaurantModels.clear();


        /**
         * Place your Order
         */
        place_order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"2".equals(statusCode)) {
                    placeOrder();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                /*if (sessionManager.getCartCount() > 0) {
                    placeOrder();
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }*/

            }
        });


        /**
         * add promo code
         */
        add_promo_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addpromo = new Intent(getApplicationContext(), PromoActivity.class);
                startActivity(addpromo);
            }
        });

        /**
         * Intent to location change activity
         */
        tvDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeLocation = new Intent(getApplicationContext(), LocActivity.class);
                changeLocation.putExtra("location", "change");
                changeLocation.putExtra("type", 2);
                startActivity(changeLocation);
            }
        });


        /**
         * Intent to Change Notes
         */
        rltDeliveryDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeLocation = new Intent(getApplicationContext(), LocActivity.class);
                changeLocation.putExtra("location", "notes");
                changeLocation.putExtra("type", 2);
                startActivity(changeLocation);
            }
        });

        /**
         * Back press
         */
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * Call API to get Cart list
     */
    private void getViewCart() {
        commonMethods.showProgressDialog(this, customDialog);
        int isWallet = 0;
        if (sessionManager.getIsWallet()) {
            isWallet = 1;
        }
        apiService.getViewCart(sessionManager.getToken(), isWallet).enqueue(new RequestCallback(Enums.REQ_VIEW_CART, this));
    }

    /**
     * Call API to get restaurant list
     */
    private void placeOrder() {
        commonMethods.showProgressDialog(this, customDialog);
        int isWallet = 0;
        if (sessionManager.getIsWallet()) {
            isWallet = 1;
        }
        apiService.placeOrder(cartDetailModel.getOrderId(), sessionManager.getPaymentMethod(), isWallet, edtSpecialNote.getText().toString().trim(), sessionManager.getOrderType(), sessionManager.getDeliveredTime(), sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_PLACE_ORDER, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        statusCode = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "status_code", String.class);
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case Enums.REQ_VIEW_CART:
                if (jsonResp.isSuccess()) {
                    rltcart.setVisibility(View.VISIBLE);
                    rltnoitem.setVisibility(View.GONE);
                    onSuccessGetCartList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if ("2".equals(statusCode)) {
                        rltcart.setVisibility(View.GONE);
                        rltnoitem.setVisibility(View.VISIBLE);
                        place_order_layout.setVisibility(View.VISIBLE);
                        place_order_text.setText(R.string.keepbrowsing);
                        sessionManager.setCartCount(0);
                        sessionManager.setCartAmount("");
                        sessionManager.setOrderType(0);
                        sessionManager.setScheduledDay("");
                        sessionManager.setScheduleMin("");
                        sessionManager.setScheduleDate("");
                        sessionManager.setAddedTime("");

                    } else if ("3".equals(statusCode)) {
                        clearUnAvailableItem(jsonResp);
                        place_order_layout.setVisibility(View.GONE);
                    } else {
                        commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                        place_order_layout.setVisibility(View.GONE);
                    }
                }
                break;
            case Enums.REQ_PLACE_ORDER:
                if (jsonResp.isSuccess()) {
                    sessionManager.setCartCount(0);
                    sessionManager.setCartAmount("");
                    onSuccessGetUserOrder(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    place_order_layout.setVisibility(View.GONE);
                }
                break;
            case Enums.REQ_CLEAR_MENU:
                if (jsonResp.isSuccess()) {
                    setViewCart();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    place_order_layout.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private void clearUnAvailableItem(JsonResponse jsonResp) {

        UnavailableModel unavailableModel = gson.fromJson(jsonResp.getStrResponse(), UnavailableModel.class);

        if (unavailableModel.getUnavailable() != null && unavailableModel.getUnavailable().size() > 0) {
            unavailableMenusArrayList.clear();
            unavailableMenusArrayList.addAll(unavailableModel.getUnavailable());


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.unavailable_clear_menu, null, false);
            dialogBuilder.setView(dialogView);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
            TextView tvMsg = dialogView.findViewById(R.id.tvMsg);
            LinearLayout lltMenus = dialogView.findViewById(R.id.lltMenus);
            TextView btnClearMenu = dialogView.findViewById(R.id.btnClearMenu);
            TextView btnClearCart = dialogView.findViewById(R.id.btnClearCart);
            btnClearCart.setText(getResources().getString(R.string.clear_all_basket));
            boolean clearAll = false;

            System.out.println("ARRRAY SIZE " + unavailableMenusArrayList.size());
            if (unavailableMenusArrayList.size() > 1) {
                tvTitle.setText(getResources().getString(R.string.title1));
                tvMsg.setText(getResources().getString(R.string.msg1));
                lltMenus.setVisibility(View.VISIBLE);
                lltMenus.removeAllViews();
                for (int i = 0; i < unavailableMenusArrayList.size(); i++) {

                    View view = LayoutInflater.from(this).inflate(R.layout.unavailable_menus, null);
                    TextView tvMenus = view.findViewById(R.id.tvMenus);

                    tvMenus.setText("-" + unavailableMenusArrayList.get(i).getMenuName());

                    lltMenus.addView(view);
                    clearAll = true;
                    btnClearMenu.setText(getResources().getString(R.string.remove_menus));

                }
            } else {
                tvTitle.setText(getResources().getString(R.string.title2));
                tvMsg.setText(getResources().getString(R.string.msgfirsthalf) + unavailableMenusArrayList.get(0).getMenuName() + getResources().getString(R.string.msg2half));
                lltMenus.setVisibility(View.INVISIBLE);
                clearAll = false;
                btnClearMenu.setText(getResources().getString(R.string.remove_menu));
            }

            final boolean finalClearAll = clearAll;
            btnClearMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (finalClearAll) {
                        commonMethods.showProgressDialog(PlaceOrderActivity.this,customDialog);
                        apiService.clearAllCart(sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_CLEAR_MENU, PlaceOrderActivity.this));
                    } else {
                        commonMethods.showProgressDialog(PlaceOrderActivity.this,customDialog);
                        apiService.clearCart(unavailableMenusArrayList.get(0).getId(), unavailableMenusArrayList.get(0).getOrderId(), sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_CLEAR_MENU, PlaceOrderActivity.this));
                    }
                    alertDialog.dismiss();
                }
            });

            btnClearCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonMethods.showProgressDialog(PlaceOrderActivity.this,customDialog);
                    apiService.clearAllCart(sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_CLEAR_MENU, PlaceOrderActivity.this));
                    alertDialog.dismiss();
                }
            });
        }
    }


    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * To get values from a success response of place order api
     *
     * @param jsonResp
     */

    private void onSuccessGetUserOrder(JsonResponse jsonResp) {
        //userProfileModel = gson.fromJson(jsonResp.getStrResponse(), UserProfileModel.class);
        placeOrderModel = gson.fromJson(jsonResp.getStrResponse(), PlaceOrderModel.class);
        if (placeOrderModel.getPlaceOrderDetails() != null && sessionManager.getIsWallet()) {
            String rWallet = String.valueOf(Float.valueOf(sessionManager.getwalletAmount()) - Float.valueOf(placeOrderModel.getPlaceOrderDetails().getWalletAmount()));
            if (Float.valueOf(rWallet) < 0) {
                sessionManager.setWalletAmount("0");
            } else {
                sessionManager.setWalletAmount(rWallet);
            }
            System.out.println("ReMaining Wallet Amount " + sessionManager.getwalletAmount());
        }
        sessionManager.setOrderType(0);
        sessionManager.setScheduledDay("");
        sessionManager.setScheduleMin("");
        sessionManager.setScheduleDate("");
        sessionManager.setAddedTime("");

        //Toast.makeText(PlaceOrderActivity.this, "Your Order is Placed", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("type", "placeorder");
        startActivity(intent);
        finish();
    }


    /**
     * To fetch success response and getting values from view_cart api
     *
     * @param jsonResponse
     */


    public void onSuccessGetCartList(JsonResponse jsonResponse) {
        ViewCartModel viewCartModel = gson.fromJson(jsonResponse.getStrResponse(), ViewCartModel.class);
        if (viewCartModel != null) {
            cartDetailModel = viewCartModel.getCartDetails();
            System.out.println("cartDetailModel.get" + cartDetailModel.getMenuItemDetails().size());
            if (cartDetailModel != null) {

                CartRestaurantModel cartRestaurantModel = cartDetailModel.getRestaurantDetailsModel();
                AddressModel addressModel = cartDetailModel.getAddressModel();
                restaurantMenuItemModel = cartDetailModel.getMenuItemDetails();

                tvRestaurantName.setText(cartRestaurantModel.getRestaurantName());
                //tvDeliverTime.setText(" • "+cartRestaurantModel.getMinTime()+"-"+cartRestaurantModel.getMaxTime()+getResources().getString(R.string.mins));
                tvDeliverTime.setText(" • " /*+ getResources().getString(R.string.arrive_under) + " "*/ + cartRestaurantModel.getMinTime() + " - " + cartRestaurantModel.getMaxTime() + " " + getResources().getString(R.string.mins));

                System.out.println("PromoAmount  " + cartDetailModel.getPromoAmount());

                /**
                 *   order Type is 1 For Schedule Order
                 */
                if (sessionManager.getOrderType() == 1) {
                    ivOrderType.setImageDrawable(getResources().getDrawable(R.drawable.scheduled_calender));
                    if (sessionManager.getHomeScheduledDay().equalsIgnoreCase("Today") || sessionManager.getHomeScheduledDay().equalsIgnoreCase("Tomorrow")) {
                        tvOrderType.setText(sessionManager.getHomeScheduledDay() + " - " + sessionManager.getAddedTime());
                    } else {
                        tvOrderType.setText(sessionManager.getScheduleDate() + " - " + sessionManager.getAddedTime());
                    }
                    place_order_text.setText(getResources().getString(R.string.schedule_order));
                    rltDeliver.setVisibility(View.GONE);
                } else {
                    ivOrderType.setImageDrawable(getResources().getDrawable(R.drawable.timer));
                    tvOrderType.setText(getResources().getString(R.string.asap_as_soon_as_possible));
                    place_order_text.setText(getResources().getString(R.string.place_order));
                    rltDeliver.setVisibility(View.VISIBLE);
                }
                if (cartDetailModel.getPromoAmount() != null && Float.valueOf(cartDetailModel.getPromoAmount()) > 0) {
                    rltPromo.setVisibility(View.VISIBLE);
                    if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                        tvDiscountPrice.setText("(" + sessionManager.getCurrencySymbol() + cartDetailModel.getPromoAmount() + "-)");
                    }else {
                        tvDiscountPrice.setText("(-" + sessionManager.getCurrencySymbol() + cartDetailModel.getPromoAmount() + ")");
                    }
                    add_promo_code.setVisibility(View.GONE);
                } else {
                    rltPromo.setVisibility(View.GONE);
                    add_promo_code.setVisibility(View.VISIBLE);
                }
                if (cartDetailModel.getWalletAmount() != null && Float.valueOf(cartDetailModel.getWalletAmount()) > 0) {
                    ivWalletImage.setVisibility(View.VISIBLE);
                    rltWallet.setVisibility(View.VISIBLE);
                    if ("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
                        tvWalletAmount.setText(("(" + sessionManager.getCurrencySymbol() + cartDetailModel.getWalletAmount() + "-)"));
                    }else {
                        tvWalletAmount.setText(("(-" + sessionManager.getCurrencySymbol() + cartDetailModel.getWalletAmount() + ")"));
                    }

                } else {
                    ivWalletImage.setVisibility(View.GONE);
                    rltWallet.setVisibility(View.GONE);
                }
                if (cartDetailModel.getPenality() != null && Float.valueOf(cartDetailModel.getPenality()) > 0) {
                    rltPenalityFee.setVisibility(View.VISIBLE);
                    tvPenalityPrice.setText(sessionManager.getCurrencySymbol() + cartDetailModel.getPenality());
                } else {
                    rltPenalityFee.setVisibility(View.GONE);
                }
                if (addressModel.getDeliveryOptions()==null||addressModel.getDeliveryOptions() == 0) {
                    tvDeliverType.setText(getResources().getString(R.string.meet_at_vehicle));
                    tvDeliveryType.setText(getResources().getString(R.string.meet_at_vehicle));
                } else {
                    tvDeliverType.setText(getResources().getString(R.string.deliver_to_door));
                    if (!TextUtils.isEmpty(addressModel.getApartment()))
                        tvDeliveryType.setText(getResources().getString(R.string.deliver_to_door) + "," + addressModel.getApartment());
                    else tvDeliveryType.setText(getResources().getString(R.string.deliver_to_door));
                }
                if (!TextUtils.isEmpty(addressModel.getDeliveryNote()))
                    tvDeliveryNotes.setText(addressModel.getDeliveryNote());
                else tvDeliveryNotes.setText(getResources().getString(R.string.add_delivey_note));

                imageUtils.loadImage(this, ivMapImage, addressModel.getStaticMap());
                String address = "";
                String address1 = "";
                if (!TextUtils.isEmpty(addressModel.getApartment()))
                    address = address + " " + addressModel.getApartment();
                if (!TextUtils.isEmpty(addressModel.getStreet()))
                    address = address + " " + addressModel.getStreet();
                if (!TextUtils.isEmpty(addressModel.getArea()))
                    address = address + " " + addressModel.getArea();
                if (!TextUtils.isEmpty(addressModel.getCity()))
                    address = address + " " + addressModel.getCity();

                if (!TextUtils.isEmpty(addressModel.getState()))
                    address1 = address1 + " " + addressModel.getState();
                if (!TextUtils.isEmpty(addressModel.getCountry()))
                    address1 = address1 + " " + addressModel.getCountry();
                if (!TextUtils.isEmpty(addressModel.getPostalCode()))
                    address1 = address1 + " " + addressModel.getPostalCode();

                /*if(!TextUtils.isEmpty(address)&&!TextUtils.isEmpty(address1))
                    tvDeliveryAddress.setText(address+"\n"+address1);
                else if(!TextUtils.isEmpty(address))
                    tvDeliveryAddress.setText(address);
                else
                    tvDeliveryAddress.setText(address);*/

                if (!TextUtils.isEmpty(addressModel.getCity()) && !TextUtils.isEmpty(addressModel.getState()))
                    tvDeliveryAddress.setText(addressModel.getFirstAddress() + "\n" + addressModel.getSecondAddress());
                /*else if (!TextUtils.isEmpty(addressModel.getCity()))
                    tvDeliveryAddress.setText(addressModel.getCity());
                else
                    tvDeliveryAddress.setText(addressModel.getState());*/

                tvSubTotal.setText(sessionManager.getCurrencySymbol() + cartDetailModel.getSubTotal());
                if (!TextUtils.isEmpty(cartDetailModel.getTax()) && Float.valueOf(cartDetailModel.getTax()) > 0) {
                    rltTax.setVisibility(View.VISIBLE);
                    tvTax.setText(sessionManager.getCurrencySymbol() + cartDetailModel.getTax());
                } else {
                    rltTax.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(cartDetailModel.getDeliveryFee()) && Float.valueOf(cartDetailModel.getDeliveryFee()) > 0) {
                    rltDeliverFee.setVisibility(View.VISIBLE);
                    tvDeliveryPrice.setText(sessionManager.getCurrencySymbol() + cartDetailModel.getDeliveryFee());
                } else {
                    rltDeliverFee.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(cartDetailModel.getBookingFee()) && Float.valueOf(cartDetailModel.getBookingFee()) > 0) {
                    rltServiceFee.setVisibility(View.VISIBLE);
                    tvServiceAmount.setText(sessionManager.getCurrencySymbol() + cartDetailModel.getBookingFee());
                } else {
                    rltServiceFee.setVisibility(View.GONE);
                }

                tvTotal.setText(sessionManager.getCurrencySymbol() + cartDetailModel.getTotalAmount());


                if (restaurantMenuItemModel != null && restaurantMenuItemModel.size() > 0) {
                    order_list_layout.removeAllViews();
                    place_order_layout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < restaurantMenuItemModel.size(); i++) {

                        View view = LayoutInflater.from(this).inflate(R.layout.order_list_layout, null);
                        CustomTextView item_count = view.findViewById(R.id.item_count);
                        CustomTextView tvFoodName = view.findViewById(R.id.tvFoodName);
                        CustomTextView tvDeliveryNotes = view.findViewById(R.id.tvDeliveryNotes);
                        CustomTextView item_price = view.findViewById(R.id.item_price);

                        item_count.setText(String.valueOf(restaurantMenuItemModel.get(i).getQuantity()) /*+ "X"*/);
                        System.out.println("OUTPUT " + sessionManager.getCartCount());

                        tvFoodName.setText(restaurantMenuItemModel.get(i).getFoodName());
                        if (!TextUtils.isEmpty(restaurantMenuItemModel.get(i).getSpecialNotes())) {
                            tvDeliveryNotes.setText(restaurantMenuItemModel.get(i).getSpecialNotes());
                            tvDeliveryNotes.setVisibility(View.VISIBLE);
                        } else {
                            tvDeliveryNotes.setVisibility(View.GONE);
                        }

                        item_price.setText(sessionManager.getCurrencySymbol() + restaurantMenuItemModel.get(i).getTotalAmount());

                        final int index = i;
                        // Set click listener for button
                        view.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                RestaurantMenuItemModel restaurantMenuItemModels = restaurantMenuItemModel.get(index);
                                System.out.println("restaurantMenuItemModel.getOfferPrice" + restaurantMenuItemModel.get(index).getOfferPrice());
                                FoodListModel foodListModel = loadFoodList(restaurantMenuItemModels);
                                Intent updatecart = new Intent(getApplicationContext(), AddToCartActivity.class);
                                updatecart.putExtra("foodList", foodListModel);
                                updatecart.putExtra("type", 1);
                                updatecart.putExtra("status", 1);
                                startActivity(updatecart);
                            }
                        });
                        order_list_layout.setTag(i);
                        order_list_layout.addView(view);
                    }
                } else {
                    place_order_layout.setVisibility(View.GONE);
                }


            }
        }
    }

    public FoodListModel loadFoodList(RestaurantMenuItemModel restaurantMenuItemModels) {
        FoodListModel foodListModel = new FoodListModel();
        foodListModel.setType("Item");
        foodListModel.setMenuCategoryId(restaurantMenuItemModels.getMenuCategoryId());
        foodListModel.setMenuCategoryName("");
        foodListModel.setMenuId(restaurantMenuItemModels.getMenuId());
        foodListModel.setFoodId(restaurantMenuItemModels.getFoodId());
        foodListModel.setFoodName(restaurantMenuItemModels.getFoodName());
        foodListModel.setFoodDescription(restaurantMenuItemModels.getFoodDescription());
        foodListModel.setFoodPrice(restaurantMenuItemModels.getFoodPrice());
        foodListModel.setOfferPrice(restaurantMenuItemModels.getOfferPrice());
        foodListModel.setIsOffer(restaurantMenuItemModels.getIsOffer());
        //foodListModel.setFoodImage(restaurantMenuItemModels.getFoodImage());
        foodListModel.setMenuImage(restaurantMenuItemModels.getMenuImage());
        foodListModel.setIsAvailable(restaurantMenuItemModels.getIsAvailable());
        foodListModel.setIsVeg(restaurantMenuItemModels.getIsVeg());
        foodListModel.setTaxPercentage(restaurantMenuItemModels.getTaxPercentage());
        foodListModel.setStatus(restaurantMenuItemModels.getStatus());
        foodListModel.setOrderItemId(restaurantMenuItemModels.getOrderItemId());
        foodListModel.setOrderId(restaurantMenuItemModels.getOrderId());
        foodListModel.setQuantity(restaurantMenuItemModels.getQuantity());
        foodListModel.setModifierPrice(restaurantMenuItemModels.getModifierPrice());
        foodListModel.setTotalAmount(restaurantMenuItemModels.getTotalAmount());
        foodListModel.setTax(restaurantMenuItemModels.getTax());
        foodListModel.setSpecialNotes(restaurantMenuItemModels.getSpecialNotes());

        return foodListModel;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setViewCart();
        MainActivity.isRefreshed = true;
    }

    public void setViewCart() {
        rltcart.setVisibility(View.GONE);
        rltnoitem.setVisibility(View.GONE);
        if (sessionManager.getPaymentMethod() == 0) {
            ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.cash));
            tvPaymentMethod.setText(getResources().getString(R.string.cash));
        } else {
            //setCardImage(sessionManager.getCardBrand());
            ivPaymentImage.setImageDrawable(CommonMethods.getCardImage(sessionManager.getCardBrand(), getResources()));
            tvPaymentMethod.setText("•••• " + sessionManager.getCardValue());
        }

        getViewCart();
        System.out.println("Wallet amount" + sessionManager.getIsWallet());
        if (sessionManager.getIsWallet()) {
            ivWalletImage.setVisibility(View.VISIBLE);
            rltWallet.setVisibility(View.VISIBLE);
        } else {
            ivWalletImage.setVisibility(View.GONE);
            rltWallet.setVisibility(View.GONE);
        }
    }

    /**
     * Method to set Card image based on card type
     *
     * @param brand string to indicate card type
     */


    /*public void setCardImage(String brand) {
        if (brand.contains("Visa")) {
            ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_visa));
        } else if (brand.contains("MasterCard")) {
            ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_master));
        } else if (brand.contains("Discover")) {
            ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_discover));
        } else if (brand.contains("Amex")) {
            ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_amex));
        } else if (brand.contains("JCP")) {
            ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_jcp));
        } else if (brand.contains("Diner")) {
            ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_diner));
        } else if (brand.contains("Union")) {
            ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_unionpay));
        } else {
            ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_basic));
        }
    }*/
}
