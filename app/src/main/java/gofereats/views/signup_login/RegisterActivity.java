package gofereats.views.signup_login;
/**
 * @package com.gofereats
 * @subpackage views.main
 * @category register Activity
 * @author Trioangle Product Team
 * @version 0.6
 */


/* ************************************************************
                Registration Activity
    *********************************************************** */

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.obs.CustomButton;

import java.util.HashMap;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.Constants;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.RequestCallback;
import gofereats.utils.TermsWebView;
import gofereats.views.customize.CustomDialog;

public class RegisterActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    SessionManager sessionManager;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    Gson gson;
    public @Inject
    CustomDialog customDialog;
    public CountryCodePicker ccp;
    public CustomButton btn_continue;
    public TextView loginlink;
    public ImageView dochome_back;
    public RelativeLayout error_mob;
    public TextView signinterms;
    public String Termpolicy;
    public TextView tvMobileFocusText;
    private EditText input_first, input_last, emaitext, passwordtext, mobile_number;
    private TextInputLayout input_layout_first, input_layout_last, emailName, passwordName;
    private AlertDialog dialog;
    private String termsUrl, privacyUrl;

    /*
     *   Check email is valid or not
     */
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppController.getAppComponent().inject(this);


        btn_continue = findViewById(R.id.btn_continue);
        dochome_back = findViewById(R.id.dochome_back);
        loginlink = findViewById(R.id.loginlink);
        signinterms = findViewById(R.id.signinterms);

        input_layout_first = findViewById(R.id.input_layout_first);
        input_layout_last = findViewById(R.id.input_layout_last);
        emailName = findViewById(R.id.emailName);
        passwordName = findViewById(R.id.passwordName);
        termsUrl = getString(R.string.site_url) + getString(R.string.terms_url)+"?token="+sessionManager.getToken();
        privacyUrl = getString(R.string.site_url) + getString(R.string.privacy_url)+"?token="+sessionManager.getToken();
        customTextView(signinterms);
        error_mob = findViewById(R.id.error_mob);
        error_mob.setVisibility(View.GONE);

        input_first = findViewById(R.id.input_first);
        input_last = findViewById(R.id.input_last);
        emaitext = findViewById(R.id.emaitext);
        passwordtext = findViewById(R.id.passwordtext);
        mobile_number = findViewById(R.id.mobile_number);
        tvMobileFocusText = findViewById(R.id.tvMobileFocusText);
        ccp = findViewById(R.id.ccp);
        ccp.setAutoDetectedCountry(true);
        String laydir = getResources().getString(R.string.layout_direction);
        if ("1".equals(laydir)) {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.ARABIC);
            ccp.setCurrentTextGravity(CountryCodePicker.TextGravity.RIGHT);
            ccp.setGravity(Gravity.START);
        }

        input_first.requestFocus();
        input_first.addTextChangedListener(new NameTextWatcher(input_first));
        input_last.addTextChangedListener(new NameTextWatcher(input_last));
        emaitext.addTextChangedListener(new NameTextWatcher(emaitext));
        passwordtext.addTextChangedListener(new NameTextWatcher(passwordtext));
        if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            mobile_number.setGravity(Gravity.START);
            mobile_number.setTextDirection(View.LAYOUT_DIRECTION_LTR);

        }
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            emaitext.setGravity(Gravity.END);
            emaitext.setTextDirection(View.TEXT_DIRECTION_LTR);

        }
        mobile_number.addTextChangedListener(new NameTextWatcher(mobile_number));

        mobile_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvMobileFocusText.setTextColor(getResources().getColor(R.color.color_accent));
                } else {
                    tvMobileFocusText.setTextColor(getResources().getColor(R.color.mobile_focus_colur));
                }
            }
        });


        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(dochome_back,this);
        Termpolicy = getResources().getString(R.string.site_url);//Demo Url for Terms and policy

        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(signin);
            }
        });
        /**
         *   Validate registration fields
         */
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (!validateFirst()) {
                   // return;
                }
                if (!validateLast()) {
                    //return;
                }*/
                if (!validateEmail()) {
                    emailName.setError(getResources().getString(R.string.error_msg_email));
                    //return;
                } else {
                    emailName.setError(null);
                }
                if (!validatePhone()) {
                    error_mob.setVisibility(View.VISIBLE);
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.error_red));
                    ViewCompat.setBackgroundTintList(mobile_number, colorStateList);
                    // return;
                } else {
                    error_mob.setVisibility(View.GONE);
                    ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.app_green));
                    ViewCompat.setBackgroundTintList(mobile_number, colorStateList);
                }
                if (!validatePassword()) {
                    passwordName.setError(getResources().getString(R.string.error_msg_password));
                    //return;
                } else {
                    passwordName.setError(null);
                }

                if (!validateFirst() || !validateLast() || !validateEmail() || !validatePhone() || !validatePassword()) {
                    return;
                }
                sessionManager.setFirstName(input_first.getText().toString().trim());
                sessionManager.setLastName(input_last.getText().toString().trim());
                sessionManager.setEmail(emaitext.getText().toString().trim());
                sessionManager.setCountryCode(ccp.getSelectedCountryCode().trim());
                sessionManager.setPhoneNumber(mobile_number.getText().toString().trim());
                sessionManager.setPassword(passwordtext.getText().toString().trim());


                saveUserDetails();

            }
        });

        dochome_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /**
         * Picking Country Using Country Code
         */

        sessionManager.setCountryCode(ccp.getDefaultCountryCode());
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                ccp.getSelectedCountryName();

            }
        });
    }

    /**
     * Save Details
     */
    private void saveUserDetails() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.numberValidation(sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode(),sessionManager.getAppLanguageCode()).enqueue(new RequestCallback(this));
    }

    /**
     * Success Message
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
        if (jsonResp.isSuccess()) {
            onSuccessOTP(jsonResp);
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Get Failure response from API
     */
    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
    }

    private void onSuccessOTP(JsonResponse jsonResponse) {
        final int otp = (int) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.OTP, Integer.class);
        Intent getotp = new Intent(getApplicationContext(), ForgotPasswordOtpActivity.class);
        getotp.putExtra("otp", otp);
        getotp.putExtra("resetpassword", false);
        getotp.putExtra("isChange", false);
        getotp.putExtra("hashmap", getParams());
        startActivity(getotp);
    }

    /**
     * Getting User Details in Hash map
     *
     * @return
     */
    public HashMap<String, String> getParams() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("first_name", input_first.getText().toString().trim());
        hashMap.put("last_name", input_last.getText().toString().trim());
        hashMap.put("mobile_number", mobile_number.getText().toString().trim());
        hashMap.put("country_code", ccp.getSelectedCountryCode().trim());
        hashMap.put("email", emaitext.getText().toString().trim());
        hashMap.put("password", passwordtext.getText().toString().trim());
        hashMap.put("language", sessionManager.getAppLanguageCode());
        return hashMap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Validate first name
     */
    private boolean validateFirst() {
        if (input_first.getText().toString().trim().isEmpty()) {
            input_layout_first.setError(getResources().getString(R.string.error_msg_firstname));
            requestFocus(input_first);
            return false;
        } else {
            input_layout_first.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Validate last name
     */
    private boolean validateLast() {
        if (input_last.getText().toString().trim().isEmpty()) {
            input_layout_last.setError(getResources().getString(R.string.error_msg_lastname));
            requestFocus(input_last);
            return false;
        } else {
            input_layout_last.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * Validate email address
     */
    private boolean validateEmail() {
        String email = emaitext.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            //emailName.setError(getString(R.string.error_msg_email));
            requestFocus(emaitext);
            return false;
        } else {
            emailName.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Validate phone number
     */
    private boolean validatePhone() {
        if (mobile_number.getText().toString().trim().isEmpty() || mobile_number.getText().toString().length() < 6) {
            requestFocus(mobile_number);
            return false;
        } else {
            error_mob.setVisibility(View.GONE);
            ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.app_green));
            ViewCompat.setBackgroundTintList(mobile_number, colorStateList);
        }
        return true;
    }

    /**
     * Validate password
     */
    private boolean validatePassword() {
        if (passwordtext.getText().toString().trim().isEmpty() || passwordtext.getText().toString().length() < 6) {
            requestFocus(passwordtext);
            return false;
        } else {
            passwordName.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Set focus edit text
     *
     * @param view used for changing request focus
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    /**
     * To Set Text for custom text view
     *
     * @param view Textview for setting text for terms and conditions
     */


    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(getResources().getString(R.string.sigin_terms1));
        spanTxt.append(getResources().getString(R.string.sigin_terms2));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent account = new Intent(getApplicationContext(), TermsWebView.class);
                account.putExtra("WebUrl", termsUrl);
                account.putExtra("title", getResources().getString(R.string.terms_and_conditions));
                account.putExtra("titletype", 1);
                startActivity(account);
            }
        }, spanTxt.length() - getResources().getString(R.string.sigin_terms2).length(), spanTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_green)), spanTxt.length() - getResources().getString(R.string.sigin_terms2).length(), spanTxt.length(), 0);
        spanTxt.append(getResources().getString(R.string.sigin_terms3));
        spanTxt.append(getResources().getString(R.string.sigin_terms4));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent account = new Intent(getApplicationContext(), TermsWebView.class);
                account.putExtra("WebUrl", privacyUrl);
                account.putExtra("title", getResources().getString(R.string.privacy_policy));
                account.putExtra("titletype", 0);
                startActivity(account);
            }
        }, spanTxt.length() - getResources().getString(R.string.sigin_terms4).length(), spanTxt.length(), 0);
        spanTxt.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_green)), spanTxt.length() - getResources().getString(R.string.sigin_terms4).length(), spanTxt.length(), 0);
        spanTxt.append(".");
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }


    /**
     * Text Changer
     */
    private class NameTextWatcher implements TextWatcher {

        private View view;

        private NameTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (mobile_number.getText().toString().startsWith("0")) {
                mobile_number.setText("");
            }
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_first:
                    validateFirst();
                    break;
                case R.id.input_last:
                    validateLast();
                    break;
                case R.id.emaitext:
                    validateEmail();
                    break;
                case R.id.passwordtext:
                    validatePassword();
                    break;
                case R.id.mobile_number:
                    validatePhone();
                    break;
                default:
                    break;
            }
        }
    }
}
