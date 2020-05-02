package gofereats.views.signup_login;
/**
 * @package com.gofereats
 * @subpackage views.main
 * @category resetPassword
 * @author Trioangle Product Team
 * @version 0.6
 */


/* ************************************************************
                Reset the Password Activity
    *********************************************************** */

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;

public class ResetPassword extends AppCompatActivity implements ServiceListener {

    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;


    public RelativeLayout back;
    public RelativeLayout next;
    public EditText input_password;
    public EditText input_confirmpassword;
    public ProgressBar progressBar;
    public ImageView backArrow,backarrow;
    private TextInputLayout input_layout_password;
    private TextInputLayout input_layout_confirmpassword;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        AppController.getAppComponent().inject(this);

        progressBar = findViewById(R.id.progressBar);
        backArrow = findViewById(R.id.backArrow);


        dialog = commonMethods.getAlertDialog(this);
        input_layout_password = findViewById(R.id.input_layout_password);
        input_password = findViewById(R.id.input_password);

        input_layout_confirmpassword = findViewById(R.id.input_layout_confirmpassword);
        input_confirmpassword = findViewById(R.id.input_confirmpassword);


        back = findViewById(R.id.resetpasswordback);
        next = findViewById(R.id.next);
        backarrow=findViewById(R.id.backarrow);
        commonMethods.rotateArrow(backarrow,this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFirst()) {
                    return;
                } else if (!validateconfrom()) {
                    return;
                } else {
                    String input_password_str = input_password.getText().toString();
                    String input_password_confirmstr = input_confirmpassword.getText().toString();
                    if (input_password_str.length() > 5 && input_password_confirmstr.length() > 5 && input_password_confirmstr.equals(input_password_str)) {

                        resetPassword(input_password_str);

                    } else {
                        if (!input_password_confirmstr.equals(input_password_str)) {
                            // snackBar(getString(R.string.Password_Mismatch), "", false, 2);
                            commonMethods.snackBar(getResources().getString(R.string.Password_Mismatch), "", false, 2, next, getResources(), ResetPassword.this);
                        } else {
                            // snackBar(getString(R.string.InValid_Password), "", false, 2);
                            commonMethods.snackBar(getResources().getString(R.string.InValid_Password), "", false, 2, next, getResources(), ResetPassword.this);
                        }
                    }
                }
            }
        });
    }


    /**
     * Reset password Api
     */
    public void resetPassword(String password) {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.resetPassword(sessionManager.getType(), sessionManager.getPhoneNumber(), sessionManager.getCountryCode(), password,sessionManager.getAppLanguageCode()).enqueue(new RequestCallback(this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }

        if (jsonResp.isSuccess()) {
            onSuccessResetLogin();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Reset to Login
     */
    private void onSuccessResetLogin() {
        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(login);
        finishAffinity();
        overridePendingTransition(R.anim.ub__slide_in_right, R.anim.ub__slide_out_left);
    }

    /*
 *   Validate password field
 */
    private boolean validateFirst() {
        if (input_password.getText().toString().trim().isEmpty()) {
            input_layout_password.setError(getResources().getString(R.string.enter_your_password));
            requestFocus(input_password);
            return false;
        } else {
            input_layout_password.setErrorEnabled(false);
        }

        return true;
    }

    /*
     *   Validate Confirm password field
     */
    private boolean validateconfrom() {
        if (input_confirmpassword.getText().toString().trim().isEmpty()) {
            input_layout_confirmpassword.setError(getResources().getString(R.string.confirm_your_password));
            requestFocus(input_confirmpassword);
            return false;
        } else {
            input_layout_confirmpassword.setErrorEnabled(false);
        }

        return true;
    }

    /*
     *   Focus edit text field
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ub__slide_in_left, R.anim.ub__slide_out_right);
    }


}
