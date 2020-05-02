package gofereats.views.signup_login;
/**
 * @package com.gofereats
 * @subpackage views.main
 * @category ForgotPasswordOtpActivity
 * @author Trioangle Product Team
 * @version 0.6
 */


/* ************************************************************
                ForgotPasswordOtpActivity
    *********************************************************** */

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.Constants;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.profile.UserProfileModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.subviews.EditProfileActivity;
import gofereats.views.main.subviews.LocActivity;

import static gofereats.utils.Enums.REQ_CHANGE_NUMBER;
import static gofereats.utils.Enums.REQ_RESEND_OTP;
import static gofereats.utils.Enums.REQ_SIGNUP;

public class ForgotPasswordOtpActivity extends AppCompatActivity implements ServiceListener {

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

    public TextView resend;
    public TextView resend_timer;
    public CustomTextView codetext;
    public RelativeLayout back;
    public RelativeLayout next;
    public CountDownTimer countDownTimer;


    public ProgressBar progressBar;
    public ImageView backArrow,backarrow;

    public EditText one;
    public EditText two;
    public EditText three;
    public EditText four;
    public int otp;
    public boolean isResetpassword;
    public boolean isChange;
    public String accesstoken;
    public HashMap<String, String> hashMap;
    public String[] otpNumbers;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp);
        AppController.getAppComponent().inject(this);


        back = findViewById(R.id.back);
        resend = findViewById(R.id.resend);
        resend_timer = findViewById(R.id.resend_timer);
        next = findViewById(R.id.next);

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);

        //TO set edit text select all
        one.setSelectAllOnFocus(true);
        two.setSelectAllOnFocus(true);
        three.setSelectAllOnFocus(true);
        four.setSelectAllOnFocus(true);

        //Setting the edit text cursor at Start
        int position = one.getSelectionStart();
        int position2 = two.getSelectionStart();
        int position3 = three.getSelectionStart();
        int position4 = four.getSelectionStart();

        one.setSelection(position);
        two.setSelection(position2);
        three.setSelection(position3);
        four.setSelection(position4);

        Intent intent = getIntent();
        //hashMap For Register Flow and its Data
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("hashmap");
        //isResetpassword is for Reset password For otp
        isResetpassword = intent.getBooleanExtra("resetpassword", false);
        //isChange For Change the Mobile Number
        isChange = intent.getBooleanExtra("isChange", false);

        dialog = commonMethods.getAlertDialog(this);
        otp = getIntent().getIntExtra("otp", 0);


        progressBar = findViewById(R.id.progressBar);
        backArrow = findViewById(R.id.backArrow);
        backarrow = findViewById(R.id.backarrow);
        codetext = findViewById(R.id.codetext);

        commonMethods.rotateArrow(backarrow,this);
        String str1 = getResources().getString(R.string.enter_the_4_digit) + " " + sessionManager.getPhoneNumber();
        int str = str1.length();


        //Action for Backspace on Keypad
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            one.setGravity(Gravity.END);
            one.setTextDirection(View.TEXT_DIRECTION_LTR);
            two.setGravity(Gravity.END);
            two.setTextDirection(View.TEXT_DIRECTION_LTR);
            three.setGravity(Gravity.END);
            three.setTextDirection(View.TEXT_DIRECTION_LTR);
            four.setGravity(Gravity.END);
            four.setTextDirection(View.TEXT_DIRECTION_LTR);
        }
        one.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL)) {
                    one.requestFocus();
                }
                return false;
            }
        });

        two.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL)) {

                    two.requestFocus();
                }
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL) && two.getText().toString().length() == 0) {

                    one.requestFocus();
                    one.getText().clear();
                }
                return false;
            }
        });

        three.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL)) {

                    three.requestFocus();
                }
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL) && three.getText().toString().length() == 0) {

                    two.requestFocus();
                    two.getText().clear();
                }
                return false;
            }
        });
        four.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL)) {
                    four.requestFocus();
                }
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL) && four.getText().toString().length() == 0) {
                    three.requestFocus();
                    three.getText().clear();
                }
                return false;
            }
        });


        /*
         *   Text watcher for OTP fields
         */
        one.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (one.getText().toString().length() == 1)     //size as per your requirement
                {
                    two.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
              /*  if (one.getText().toString().length()==1){
                    two.requestFocus();
                }*/

            }

        });

        two.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (two.getText().toString().length() == 1)     //size as per your requirement
                {
                    three.requestFocus();

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

               /* if (two.getText().toString().length() == 0)     //size as per your requirement
                {
                    one.requestFocus();
                }*/
            }

        });

        three.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (three.getText().toString().length() == 1)     //size as per your requirement
                {
                    four.requestFocus();
                    //three.setBackgroundResource(R.drawable.d_buttomboardermobilenumber);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

               /* if (three.getText().toString().length() == 0)     //size as per your requirement
                {
                    two.requestFocus();
                }*/
            }

        });


        four.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (three.getText().toString().length() == 1)     //size as per your requirement
                {
                    four.requestFocus();
                    //four.setBackgroundResource(R.drawable.d_buttomboardermobilenumber);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

               /* if (four.getText().toString().length() == 0)     //size as per your requirement
                {
                    three.requestFocus();
                }*/
            }

        });

        /**
         * Splitting Opt And Set on its Edit text
         */
        otpNumbers = String.valueOf(otp).split("(?!^)");

       /* one.setText(otpNumbers[0]);
        two.setText(otpNumbers[1]);
        three.setText(otpNumbers[2]);
        four.setText(otpNumbers[3]);
        four.setSelection(four.getText().length());
*/
         /*
         *  Countdown for resent OTP button enable
         */
        countdown();

        final SpannableStringBuilder str3 = new SpannableStringBuilder(str1);
        str3.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), Integer.parseInt("37"), str, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        codetext.setText(str3);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
            }
        });

        /**
         * Resend code
         */

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checkresend=true;
                one.getText().clear();
                two.getText().clear();
                three.getText().clear();
                four.getText().clear();

                resendOtp();
                countdown();
            }
        });

        /**
         * next button
         */
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emtytextone = one.getText().toString().trim();
                String emtytexttwo = two.getText().toString().trim();
                String emtytextthree = three.getText().toString().trim();
                String emtytextfour = four.getText().toString().trim();

                if (emtytextone.isEmpty() || emtytexttwo.isEmpty() || emtytextthree.isEmpty() || emtytextfour.isEmpty()) {
                    commonMethods.snackBar(getResources().getString(R.string.otpEmpty), "", false, 2, next, getResources(), ForgotPasswordOtpActivity.this);
                } else {
                    String otpcode = one.getText().toString() + "" + two.getText().toString() + "" + three.getText().toString() + "" + four.getText().toString() + "";

                    if (otpcode.equals(String.valueOf(otp))) {
                        countDownTimer.cancel();
                        if (isResetpassword) {
                            resetPassword();
                        } else if (isChange) {
                            changeNumber();
                        } else {
                            register();
                        }

                    } else {
                        commonMethods.snackBar(getResources().getString(R.string.otp_mismatch), "", false, 2, next, getResources(), ForgotPasswordOtpActivity.this);
                    }
                }
            }
        });
    }

    /**
     * Register
     */
    public void register() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.register(sessionManager.getType(), hashMap).enqueue(new RequestCallback(REQ_SIGNUP, this));
    }

    /**
     * Reset your Password
     */
    public void resetPassword() {
        Intent reset = new Intent(getApplicationContext(), ResetPassword.class);
        startActivity(reset);
        finish();
    }

    /**
     * Reset your Mobile Number
     */
    public void changeNumber() {

        commonMethods.showProgressDialog(this, customDialog);
        apiService.changeMobile(sessionManager.getToken(), sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode()).enqueue(new RequestCallback(REQ_CHANGE_NUMBER, this));

    }

    /**
     * Resend OTP API
     */
    private void resendOtp() {
        if (isResetpassword) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.forgotPassword(sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode(),sessionManager.getAppLanguageCode()).enqueue(new RequestCallback(REQ_RESEND_OTP, this));
        } else {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.numberValidation(sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode(),sessionManager.getAppLanguageCode()).enqueue(new RequestCallback(REQ_RESEND_OTP, this));
        }
    }

    /**
     * Call API For Resend Otp
     *
     * @param jsonResp
     * @param data
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }

        switch (jsonResp.getRequestCode()) {

            // Resend OTP
            case REQ_RESEND_OTP:
                if (jsonResp.isSuccess()) {
                    onSuccessResendOTP(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            // Register Response and Access Token
            case REQ_SIGNUP:
                if (jsonResp.isSuccess()) {
                    onSuccessRegister(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_CHANGE_NUMBER:
                if (jsonResp.isSuccess()) {
                    onSuccessChangeNumber(jsonResp);
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
        commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());

    }


    /**
     * success response functionality while changing number api call
     *
     * @param jsonResp of a successful change number api  call
     */


    private void onSuccessChangeNumber(JsonResponse jsonResp) {
        UserProfileModel userProfileModel = gson.fromJson(jsonResp.getStrResponse(), UserProfileModel.class);

        Intent changeNumber = new Intent(getApplicationContext(), EditProfileActivity.class);
        changeNumber.putExtra("name", userProfileModel.getUserProfileList().getName());
        changeNumber.putExtra("email", userProfileModel.getUserProfileList().getEmail());
        changeNumber.putExtra("mobile", userProfileModel.getUserProfileList().getMobileNumber());
        changeNumber.putExtra("countrycode", userProfileModel.getUserProfileList().getCountryCode());
        changeNumber.putExtra("profile", userProfileModel.getUserProfileList().getProfileimage());
        startActivity(changeNumber);
        finish();
        Toast.makeText(this, getResources().getString(R.string.success_mobile_number), Toast.LENGTH_SHORT).show();
    }

    /**
     * On Register
     */
    public void onSuccessRegister(JsonResponse jsonResponse) {
        accesstoken = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.ACCESS_TOKEN, String.class);
        sessionManager.setToken(accesstoken);
        Intent register = new Intent(getApplicationContext(), LocActivity.class);
        register.putExtra("location", "entry");
        register.putExtra("type", 2);
        startActivity(register);
        finishAffinity();
    }

    /**
     * Resend OTP Result From API
     */
    private void onSuccessResendOTP(JsonResponse jsonResponse) {
        otp = (int) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.OTP, Integer.class);
        /**
         * Splitting OTP and Set into its Edit text
         */
        otpNumbers = String.valueOf(otp).split("(?!^)");


       /* one.setText(otpNumbers[0]);
        two.setText(otpNumbers[1]);
        three.setText(otpNumbers[2]);
        four.setText(otpNumbers[3]);
        four.setSelection(four.getText().length());*/
        //Toast.makeText(this, "Your OTP is " + otp, Toast.LENGTH_LONG).show();
    }

    /**
     * Timer  method
     */
    public void countdown() {
        resend.setEnabled(false);
        resend_timer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                NumberFormat f = new DecimalFormat("00");
                resend_timer.setText("00:" + f.format(millisUntilFinished / 1000));
            }

            public void onFinish() {
                resend.setEnabled(true);
                resend_timer.setText("00:00");
                resend_timer.setVisibility(View.INVISIBLE);
                //resend_timer.setText("done!");
            }
        }.start();
    }


}
