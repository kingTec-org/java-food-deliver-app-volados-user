package gofereats.views.signup_login;
/**
 *
 * @package com.gofereats
 * @subpackage views.main
 * @category mobileActivity
 * @author Trioangle Product Team
 * @version 0.6
 */


/*************************************************************
 Mobile Number for ForgotPassword Activity
 *********************************************************** */

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.Constants;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.Enums;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;

public class MobileActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;

    public CountryCodePicker ccp;
    public RelativeLayout next;
    public RelativeLayout back;
    public LinearLayout mobilelayout;
    public TextView entermobileno;
    public EditText phone;

    public ProgressBar progressBar;
    public ImageView backArrow,backarrow;
    public boolean editMob;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        AppController.getAppComponent().inject(this);

        progressBar = findViewById(R.id.progressBar);
        backArrow = findViewById(R.id.backArrow);

        ccp = findViewById(R.id.ccp);
        mobilelayout = findViewById(R.id.mobilelayout);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        entermobileno = findViewById(R.id.entermobileno);
        phone = findViewById(R.id.phone);
        backarrow=findViewById(R.id.backarrow);
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            phone.setGravity(Gravity.END);
            phone.setTextDirection(View.TEXT_DIRECTION_LTR);
        }
        ccp.setAutoDetectedCountry(true);

        String laydir = getResources().getString(R.string.layout_direction);
        if ("1".equals(laydir)) {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.ARABIC);
            ccp.setCurrentTextGravity(CountryCodePicker.TextGravity.RIGHT);
            ccp.setGravity(Gravity.START);
        }

        editMob = getIntent().getBooleanExtra("isChange", false);

        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(backarrow,this
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().getSharedElementEnterTransition().setDuration(600);
            getWindow().getSharedElementReturnTransition().setDuration(600).setInterpolator(new DecelerateInterpolator());
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            entermobileno.setTransitionName("mobilenumber");
            mobilelayout.setTransitionName("mobilelayout");
        }


        //Text listener
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.print(start);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (phone.getText().toString().startsWith("0")) {
                    phone.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.print(s.toString());

            }
        });


        /*
         *   Get country code picker
         */
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                ccp.getSelectedCountryCode();
            }
        });
        sessionManager.setCountryCode(ccp.getSelectedCountryCodeWithPlus().replaceAll("\\+", ""));

        /*
         *   After get phone number check the number validation is already exits or not
         */
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.getText().toString().equals("") || phone.toString().isEmpty()) {
                    //  snackBar(getString(R.string.enter_your_mobile_number), "", false, 2);
                    commonMethods.snackBar(getResources().getString(R.string.enter_your_mobile_number), "", false, 2, next, getResources(), MobileActivity.this);
                } else {
                    if (editMob) {
                        //Change  Mobile Number
                        String editMob = phone.getText().toString();
                        sessionManager.setPhoneNumber(editMob);
                        sessionManager.setCountryCode(ccp.getSelectedCountryCode().trim());
                        System.out.println("MOBE " + editMob + " CCP " + ccp.getSelectedCountryCode().trim());
                        editMobNumber(editMob);
                    } else {
                        // Forgot Password
                        String mobile = phone.getText().toString();
                        sessionManager.setPhoneNumber(mobile);
                        sessionManager.setCountryCode(ccp.getSelectedCountryCode().trim());
                        System.out.println("MOBE " + mobile + " CCP " + ccp.getSelectedCountryCode().trim());
                        forgotPassword(mobile);
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Check Number Availabe or Not
     */
    public void editMobNumber(String mobile) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.numberValidation(sessionManager.getType(), mobile, ccp.getSelectedCountryCode().trim(), sessionManager.getAppLanguageCode()).enqueue(new RequestCallback(Enums.REQ_EDIT_MOBILE, this));
    }

    /**
     * Number validator for Forgot Password
     */
    public void forgotPassword(String mobile) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.forgotPassword(sessionManager.getType(), mobile, ccp.getSelectedCountryCode().trim(), sessionManager.getAppLanguageCode()).enqueue(new RequestCallback(Enums.REQ_FORGOT_PASSWORD, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case Enums.REQ_FORGOT_PASSWORD:
                if (jsonResp.isSuccess()) {
                    onSuccessForgotPassword(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case Enums.REQ_EDIT_MOBILE:
                if (jsonResp.isSuccess()) {
                    onSuccessChangeMobile(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (editMob) {
                        commonMethods.showMessage(this, dialog, getResources().getString(R.string.mobile_already_entered));
                    } else {
                        commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
    }

    /**
     * Get Otp From Forgot Password
     */
    private void onSuccessForgotPassword(JsonResponse jsonResponse) {
        final int otp = (int) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.OTP, Integer.class);
        Intent resetotp = new Intent(getApplicationContext(), ForgotPasswordOtpActivity.class);
        resetotp.putExtra("otp", otp);
        resetotp.putExtra("resetpassword", true);
        resetotp.putExtra("isChange", false);
        startActivity(resetotp);
    }

    /**
     * Get Otp From Forgot Password
     */
    private void onSuccessChangeMobile(JsonResponse jsonResponse) {
        final int otp = (int) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.OTP, Integer.class);
        Intent changeMob = new Intent(getApplicationContext(), ForgotPasswordOtpActivity.class);
        changeMob.putExtra("otp", otp);
        changeMob.putExtra("resetpassword", false);
        changeMob.putExtra("isChange", true);
        startActivity(changeMob);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }

    /*  *//*
     *   Show error message or information
     *//*
    public void snackBar(String message, String buttonmessage, boolean buttonvisible, int duration) {
        // Create the Snackbar
        Snackbar snackbar;
        RelativeLayout snackbar_background;
        TextView snack_button;
        TextView snack_message;

        // Snack bar visible duration
        if (duration == 1)
            snackbar = Snackbar.make(next, "", Snackbar.LENGTH_INDEFINITE);
        else if (duration == 2)
            snackbar = Snackbar.make(next, "", Snackbar.LENGTH_LONG);
        else
            snackbar = Snackbar.make(next, "", Snackbar.LENGTH_SHORT);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        snackbar_background = (RelativeLayout) snackView.findViewById(R.id.snackbar);
        snack_button = (TextView) snackView.findViewById(R.id.snack_button);
        snack_message = (TextView) snackView.findViewById(R.id.snack_message);

        snackbar_background.setBackgroundColor(getResources().getColor(R.color.black)); // Background Color

        if (buttonvisible) // set Right side button visible or gone
            snack_button.setVisibility(View.VISIBLE);
        else
            snack_button.setVisibility(View.GONE);

        snack_button.setTextColor(getResources().getColor(R.color.bluelight)); // set right side button text color
        snack_button.setText(buttonmessage); // set right side button text
        snack_message.setTextColor(getResources().getColor(R.color.white)); // set left side main message text color
        snack_message.setText(message);  // set left side main message text

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.black));
        snackbar.show();
        System.out.println("Snack bar ended");

    }*/


}

