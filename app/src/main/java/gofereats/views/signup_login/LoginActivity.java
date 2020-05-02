package gofereats.views.signup_login;
/**
 *
 * @package com.trioangle.gofereats
 * @subpackage views.main
 * @category LoginActivity
 * @author Trioangle Product Team
 * @version 1.0
 */


/*************************************************************
 Login Screen Activity
 ************************************************************/

import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.obs.CustomButton;
import com.obs.CustomEditText;

import java.util.HashMap;
import java.util.Locale;

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
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.subviews.EntryActivity;
import gofereats.views.main.subviews.LocActivity;


public class LoginActivity extends AppCompatActivity implements ServiceListener {

    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;


    public TextView forgotpassword;
    public CustomEditText mobilenumber;
    public CustomEditText password;
    public CustomButton login;
    public CustomButton Register;
    public CountryCodePicker ccp;
    public ImageView arrow;
    public String accesstoken;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppController.getAppComponent().inject(this);


        forgotpassword = findViewById(R.id.forgotpassword);
        mobilenumber = findViewById(R.id.mobilenumber);
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            mobilenumber.setGravity(Gravity.END);
            mobilenumber.setTextDirection(View.TEXT_DIRECTION_LTR);
        }
        password = findViewById(R.id.password);
        Register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        ccp = findViewById(R.id.ccp);
        arrow = findViewById(R.id.arrow);
        ccp.setAutoDetectedCountry(true);

        commonMethods.rotateArrow(arrow,this);
        String laydir = getResources().getString(R.string.layout_direction);
        if ("1".equals(laydir)||Locale.getDefault().getLanguage().equals("ar")) {
            ccp.changeDefaultLanguage(CountryCodePicker.Language.ARABIC);
            ccp.setCurrentTextGravity(CountryCodePicker.TextGravity.RIGHT);
            ccp.setGravity(Gravity.START);
            password.setGravity(Gravity.END);
        }

        sessionManager.setCountryCode(ccp.getDefaultCountryCode());

        dialog = commonMethods.getAlertDialog(this);

        /**
         *  Country code selection
         */
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                sessionManager.setCountryCode(ccp.getSelectedCountryCode());
            }
        });

        /**
         * back press
         */
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent entry = new Intent(getApplicationContext(), EntryActivity.class);
                startActivity(entry);
            }
        });
        /**
         * Login button
         **/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mobilenumber.getText().toString().length() == 0 || password.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_error1), Toast.LENGTH_SHORT).show();
                } else if (mobilenumber.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_error_2), Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_error3), Toast.LENGTH_SHORT).show();
                } else {
                    userLogin(); // calling user login function
                }
            }
        });


        /**
         * Go to Register page
         */

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(register);
            }
        });

        /**
         * Forgot passWord
         */

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotpass = new Intent(LoginActivity.this, MobileActivity.class);
                forgotpass.putExtra("isChange", false);
                startActivity(forgotpass);
                overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent entry = new Intent(LoginActivity.this, EntryActivity.class);
        startActivity(entry);
    }

    /**
     * User Login Api
     */
    public void userLogin() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.login(sessionManager.getType(), getDatas()).enqueue(new RequestCallback(this));
    }

    /**
     * Getting Login details
     *
     * @return
     */

    private HashMap<String, String> getDatas() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile_number", mobilenumber.getText().toString().trim());
        hashMap.put("country_code", ccp.getSelectedCountryCode().trim());
        hashMap.put("password", password.getText().toString().trim());
        hashMap.put("language", sessionManager.getAppLanguageCode());
        return hashMap;
    }

    /**
     * Result From API
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }

        if (jsonResp.isSuccess()) {
            onSuccessLogin(jsonResp); // onSuccessLogin call method
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }

    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        //commonMethods.showProgressDialog(this, customDialog);
    }

    /**
     * Response From API And get values From API
     */
    private void onSuccessLogin(JsonResponse jsonResponse) {
        accesstoken = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.ACCESS_TOKEN, String.class);
        sessionManager.setToken(accesstoken);
        Intent login = new Intent(getApplicationContext(), LocActivity.class);
        login.putExtra("location", "entry");
        login.putExtra("type", 2);
        startActivity(login);
        finish();
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }
}
