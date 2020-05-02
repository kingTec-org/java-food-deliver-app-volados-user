package gofereats.views.main.subviews.wallet;


/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.subviews.wallet
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomEditText;
import com.obs.CustomTextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.Constants;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.subviews.PaymentActivity;


/*****************************************************************
 Add Wallet Activity used in Account Fragment
 ****************************************************************/


public class AddWalletActivity extends AppCompatActivity implements ServiceListener {

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
    public DialogInject dialogInject;
    public @InjectView(R.id.tvWalletAmount)
    CustomTextView tvWalletAmount;
    public @InjectView(R.id.btnAddAmount)
    CustomButton btnAddAmount;
    public @InjectView(R.id.ivBackArrow)
    ImageView ivBackArrow;
    private BottomSheetDialog dialog;
    private AlertDialog alertDialog;

    @OnClick(R.id.ivBackArrow)
    public void goBack() {
        onBackPressed();


    }


    @OnClick(R.id.btnAddAmount)
    public void addToWallet() {
        showAddWallet();
        //dialogInject.setMethod();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        alertDialog = commonMethods.getAlertDialog(this);

        commonMethods.rotateArrow(ivBackArrow,this);
        if (TextUtils.isEmpty(sessionManager.getCardBrand())) {
            sessionManager.setCardBrand("stripe");
        }
        //String walletamt = sessionManager.getCurrencySymbol() + sessionManager.getwalletAmount();

        tvWalletAmount.setText(sessionManager.getCurrencySymbol() + sessionManager.getwalletAmount());

    }

    /**
     * Dialog to add Amount in wallet
     */
    private void showAddWallet() {
        dialog = new BottomSheetDialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.add_wallet_amount);
        dialogInject = new DialogInject();

        // 5. We bind the elements of the included layouts.
        ButterKnife.inject(dialogInject, dialog);

        dialogInject.setMethod();
        dialog.show();

    }

    /**
     * Api calling method to add Wallet amount
     *
     * @param edtWalletAmount
     */

    public void addStripeToWallet(CustomEditText edtWalletAmount) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.addWallet(edtWalletAmount.getText().toString().trim(), sessionManager.getToken()).enqueue(new RequestCallback(this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, alertDialog, data);
            return;
        }
        if (jsonResp.isSuccess()) {
            onSuccesGetWallet(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, alertDialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, alertDialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Successfully getting json values after api  call
     *
     * @param jsonResp
     */


    private void onSuccesGetWallet(JsonResponse jsonResp) {
        String walletamount = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.WALLETAMOUNT, String.class);
        sessionManager.setWalletAmount(walletamount);
       // String walletamt = sessionManager.getCurrencySymbol() + sessionManager.getwalletAmount();
        tvWalletAmount.setText(sessionManager.getCurrencySymbol() + sessionManager.getwalletAmount());
    }

    /**
     * Annotation  using ButterKnife lib to Injection and OnClick for Accept or pause dialog
     **/
    public class DialogInject {

        public @InjectView(R.id.rltEnterAmount)
        RelativeLayout rltEnterAmount;
        public @InjectView(R.id.edtWalletAmount)
        CustomEditText edtWalletAmount;
        public @InjectView(R.id.tvChange)
        CustomTextView tvChange;
        public @InjectView(R.id.ivPaymentImage)
        ImageView ivPaymentImage;
        public @InjectView(R.id.tvPaymentMethod)
        CustomTextView tvPaymentMethod;
        public @InjectView(R.id.currency_symbol_wallet)
        CustomTextView currencySymbolWallet;
        public @InjectView(R.id.btnAddMoney)
        CustomButton btnAddMoney;

        @OnClick(R.id.tvChange)
        public void changePaymentMethod() {
            Intent change = new Intent(getApplicationContext(), PaymentActivity.class);
            change.putExtra("changepayment", 1);
            startActivity(change);
            dialog.dismiss();
        }

        @OnClick(R.id.btnAddMoney)
        public void addMoney() {
            dialog.dismiss();
            System.out.println("Wallet Currency"+sessionManager.getCurrencySymbol());
            edtWalletAmount.setSelection(edtWalletAmount.getText().length());
            if (edtWalletAmount.getText().toString().length() > 0) {

                if (sessionManager.getWalletCard() == 1) {
                    //Stripe
                    addStripeToWallet(edtWalletAmount);
                }
            } else {
                //snackBar(getString(R.string.enter_wallet_amount), "", false, 3);
                commonMethods.snackBar(getString(R.string.enter_wallet_amount), "", false, 3, rltEnterAmount, getResources(), AddWalletActivity.this);
            }

        }

        /**
         * To show Added card if exist or adding new card
         */

        public void setMethod() {
             if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            {
                edtWalletAmount.setGravity(Gravity.END);
                edtWalletAmount.setTextDirection(View.TEXT_DIRECTION_LTR);
            }
            currencySymbolWallet.setText(sessionManager.getCurrencySymbol());
            edtWalletAmount.setSelection(edtWalletAmount.getText().length());
            if (sessionManager.getWalletCard() == 0) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card));
                tvPaymentMethod.setText(getResources().getString(R.string.add_card));
                btnAddMoney.setEnabled(false);
                btnAddMoney.setBackgroundColor(getResources().getColor(R.color.payment_method));
            } else {
                ivPaymentImage.setImageDrawable(CommonMethods.getCardImage(sessionManager.getCardBrand(), getResources()));
                tvPaymentMethod.setText("•••• " + sessionManager.getCardValue());
                btnAddMoney.setEnabled(true);
                btnAddMoney.setBackgroundDrawable(getResources().getDrawable(R.drawable.ripple_effect_black));
            }
        }


        /**
         * To set Card Image based on brand
         * @param brand is a string based on which card image will be set
         */

       /* public void setCardImage(String brand) {
            if (brand.contains("Visa")) {
                ivPaymentImage.setImageDrawable(getResources().getDrawable(R.drawable.card_master));
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
}
