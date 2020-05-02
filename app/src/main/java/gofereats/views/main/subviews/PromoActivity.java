package gofereats.views.main.subviews;
/**
 * @package com.gofereats
 * @subpackage views.main
 * @category PromoCode
 * @author Trioangle Product Team
 * @version 0.6
 */


/* ************************************************************
                Add Promo code
    *********************************************************** */

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomEditText;

import java.util.ArrayList;

import javax.inject.Inject;

import gofereats.R;
import gofereats.adapters.main.PromoDetailAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.promo.PromoListModel;
import gofereats.datamodels.promo.PromoModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;

import static gofereats.utils.Enums.REQ_ADD_PROMO;
import static gofereats.utils.Enums.REQ_GET_PROMO;

public class PromoActivity extends AppCompatActivity implements ServiceListener {

    private static ArrayList<PromoListModel> promoListModelArrayList = new ArrayList<>();
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
    public RecyclerView rvPromolist;
    public ImageView arrow;
    public CustomEditText edtEnterpromo;
    public PromoDetailAdapter promoDetailAdapter;
    public CustomButton btnApply;
    public PromoModel promoModel;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        AppController.getAppComponent().inject(this);


        arrow = findViewById(R.id.arrow);
        rvPromolist = findViewById(R.id.rvPromolist);
        edtEnterpromo = findViewById(R.id.edtEnterpromo);
        btnApply = findViewById(R.id.btnApply);

        dialog = commonMethods.getAlertDialog(this);
        commonMethods.rotateArrow(arrow,this);
        if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
        {
            edtEnterpromo.setGravity(Gravity.END);
            edtEnterpromo.setTextDirection(View.TEXT_DIRECTION_LTR);
        }
        getPromoList();

        rvPromolist.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvPromolist.setLayoutManager(layoutManager);


        /**
         * On back pressed
         */
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtEnterpromo.getText().toString().isEmpty()) {
                    Toast.makeText(PromoActivity.this, getResources().getString(R.string.enter_promo), Toast.LENGTH_SHORT).show();
                } else {
                    addPromoCode();
                }
            }
        });
    }

    /**
     * Adding Promo Code
     */
    private void addPromoCode() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.addPromo(sessionManager.getToken(), edtEnterpromo.getText().toString()).enqueue(new RequestCallback(REQ_ADD_PROMO, this));
    }

    /**
     * Getting Promocode List
     */
    private void getPromoList() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.getPromoDetails(sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_PROMO, this));
    }


    /**
     * Get Response From Api
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_ADD_PROMO:
                if (jsonResp.isSuccess()) {
                    onSuccessAddPromo(jsonResp);
                    edtEnterpromo.getText().clear();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                    edtEnterpromo.getText().clear();
                }
                break;
            case REQ_GET_PROMO:
                if (jsonResp.isSuccess()) {
                    onSuccessGetPromo(jsonResp);
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
        commonMethods.hideProgressDialog();
        commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
    }

    /**
     * Success Response After Adding a Promo
     */

    private void onSuccessAddPromo(JsonResponse jsonResp) {
        promoModel = gson.fromJson(jsonResp.getStrResponse(), PromoModel.class);
        if (promoModel != null) {
            promoListModelArrayList.clear();
            promoListModelArrayList.addAll(promoModel.getPromoDetails());
        }
        promoDetailAdapter = new PromoDetailAdapter(this, promoListModelArrayList);
        rvPromolist.setAdapter(promoDetailAdapter);

    }


    /**
     * Success Response After Getting a Promo list
     */
    private void onSuccessGetPromo(JsonResponse jsonResp) {
        onSuccessAddPromo(jsonResp);
    }


}
