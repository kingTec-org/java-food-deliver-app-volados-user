package gofereats.views.main.subviews;
/**
 * @package com.gofereats
 * @subpackage views.subviews
 * @category Payment Activity
 * @author Trioangle Product Team
 * @version 0.6
 */


/* ************************************************************
                Showing and setting the payment methods
    *********************************************************** */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.Enums;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;

public class PaymentActivity extends AppCompatActivity implements ServiceListener {


    private static int REQUEST_CODE_PAYMENT = 1;
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
    public @InjectView(R.id.ivCashTick)
    ImageView ivCashTick;
    public @InjectView(R.id.ivCardTick)
    ImageView ivCardTick;
    public @InjectView(R.id.ivWalletTick)
    ImageView ivWalletTick;
    public @InjectView(R.id.ivCard)
    ImageView ivCard;
    public @InjectView(R.id.lltWallet)
    LinearLayout lltWallet;
    public @InjectView(R.id.tvWalletAmount)
    CustomTextView tvWalletAmount;
    public @InjectView(R.id.tvCard)
    TextView tvCard;
    public @InjectView(R.id.rltCash)
    RelativeLayout rltCash;
    public @InjectView(R.id.rltCard)
    RelativeLayout rltCard;
    public @InjectView(R.id.rltWallet)
    RelativeLayout rltWallet;
    public @InjectView(R.id.rltAddCard)
    RelativeLayout rltAddCard;
    public ImageView arrow;
    public @InjectView(R.id.tvPayment)
    CustomTextView tvPayment;
    private String stripePublishKey;
    private AlertDialog dialog;

    @OnClick(R.id.rltCash)
    public void cashClick() {
       /* Intent details=new Intent(getApplicationContext(),CashDetailActivity.class);
        startActivity(details);*/
        sessionManager.setPaymentMethod(0);
        ivCardTick.setVisibility(View.GONE);
        ivCashTick.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.rltCard)
    public void cardClick() {
        sessionManager.setPaymentMethod(1);
        sessionManager.setWalletCard(1);
        ivCardTick.setVisibility(View.VISIBLE);
        ivCashTick.setVisibility(View.GONE);
    }

    @OnClick(R.id.rltWallet)
    public void walletClick() {
        //(sessionManager.getIsWallet())
        sessionManager.setIsWallet(!sessionManager.getIsWallet());
        if (sessionManager.getIsWallet()) ivWalletTick.setVisibility(View.VISIBLE);
        else ivWalletTick.setVisibility(View.GONE);
    }

    @OnClick(R.id.rltAddCard)
    public void addCardClick() {
        Intent stripe = new Intent(getApplicationContext(), AddCardActivity.class);
        stripe.putExtra("stripePublishKey", stripePublishKey);
        startActivityForResult(stripe, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);

        dialog = commonMethods.getAlertDialog(this);
        arrow = findViewById(R.id.arrow);
        commonMethods.rotateArrow(arrow,this);
        System.out.println("MY Wallet " + sessionManager.getwalletAmount());
        tvWalletAmount.setText(sessionManager.getCurrencySymbol() + sessionManager.getwalletAmount());
        MainActivity.isRefreshed = true;
        int changePayment = getIntent().getIntExtra("changepayment", 0);

        if (changePayment == 1) {
            rltCash.setVisibility(View.GONE);
            lltWallet.setVisibility(View.GONE);
        } else if (changePayment == 2) {
            arrow.setImageResource(R.drawable.ic_closer);
            tvPayment.setText(getResources().getString(R.string.payment_option));
        } else {
            rltCash.setVisibility(View.VISIBLE);
            lltWallet.setVisibility(View.VISIBLE);
        }


        /**
         * On back pressed
         */
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        commonMethods.showProgressDialog(this, customDialog);
        apiService.viewCard(sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_VIEW_PAYMENT, this));

        if (sessionManager.getPaymentMethod() == 0) {
            ivCashTick.setVisibility(View.VISIBLE);
            ivCardTick.setVisibility(View.GONE);
        } else {
            ivCardTick.setVisibility(View.VISIBLE);
            ivCashTick.setVisibility(View.GONE);
        }

        if (sessionManager.getIsWallet()) ivWalletTick.setVisibility(View.VISIBLE);
        else ivWalletTick.setVisibility(View.GONE);
    }

    /**
     * Result form Add card
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            paymentCompleted(data.getStringExtra("token"));
        }
    }


    /**
     * After Stripe payment
     *
     * @param payKey
     */
    public void paymentCompleted(String payKey) {
        //onBackPressed();
        //commonMethods.showProgressDialog(PaymentActivity.this, customDialog);
        if (!TextUtils.isEmpty(payKey))
            apiService.addCard(payKey, sessionManager.getToken()).enqueue(new RequestCallback(Enums.REQ_ADD_CARD, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case Enums.REQ_ADD_CARD:
                if (jsonResp.isSuccess()) {

                    String brand = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "brand", String.class);
                    String last4 = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "last4", String.class);

                    if (!TextUtils.isEmpty(last4)) {
                        rltCard.setVisibility(View.VISIBLE);
                        ivCard.setImageDrawable(CommonMethods.getCardImage(brand, getResources()));
                        //setCardImage(brand);
                        tvCard.setText("•••• " + last4);
                        sessionManager.setCardValue(last4);
                        sessionManager.setCardBrand(brand);
                        ivCardTick.setVisibility(View.VISIBLE);
                        ivCashTick.setVisibility(View.GONE);
                        sessionManager.setPaymentMethod(1);
                        sessionManager.setWalletCard(1);
                    } else {
                        sessionManager.setWalletCard(0);
                        rltCard.setVisibility(View.GONE);
                    }
                    // onBackPressed();
                }

                //stripePublishKey= (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "stripe_publish_key", String.class);
                break;
            case Enums.REQ_VIEW_PAYMENT:
                if (jsonResp.isSuccess()) {
                    String brand = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "brand", String.class);
                    String last4 = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "last4", String.class);

                    if (!TextUtils.isEmpty(last4)) {
                        rltCard.setVisibility(View.VISIBLE);
                        ivCard.setImageDrawable(CommonMethods.getCardImage(brand, getResources()));
                        //setCardImage(brand);
                        tvCard.setText("•••• " + last4);
                        sessionManager.setCardValue(last4);
                        sessionManager.setCardBrand(brand);
                    } else {
                        rltCard.setVisibility(View.GONE);
                    }
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg()) && !"Trying to get property of non-object".equals(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                stripePublishKey = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), "stripe_publish_key", String.class);
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
     * Method to set Card image based on card type
     * @param brand string to indicate card type
     */


    /*public void setCardImage(String brand) {
        if (brand.contains("Visa")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_visa));
        } else if (brand.contains("MasterCard")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_master));
        } else if (brand.contains("Discover")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_discover));
        } else if (brand.contains("Amex")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_amex));
        } else if (brand.contains("JCP")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_jcp));
        } else if (brand.contains("Diner")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_diner));
        } else if (brand.contains("Union")) {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_unionpay));
        } else {
            ivCard.setImageDrawable(getResources().getDrawable(R.drawable.card_basic));
        }
    }*/
}
