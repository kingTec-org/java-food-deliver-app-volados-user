package gofereats.views.main.subviews;


/**
 * @package com.gofereats
 * @subpackage views.main
 * @category Settings Activity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.RunTimePermission;
import gofereats.configs.SessionManager;
import gofereats.datamodels.location.GetLocationModel;
import gofereats.datamodels.location.LocationList;
import gofereats.datamodels.main.JsonResponse;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.LanguageConverter;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;

import static gofereats.utils.Enums.REQ_LOGOUT;
import static gofereats.utils.Enums.REQ_UPDATE_LANGUAGE;


/*************************************************************
 Settings Activity
 ************************************************************/


public class SettingActivity extends AppCompatActivity implements ServiceListener,LanguageConverter.onSuccessLanguageChangelistner {

    public static ArrayList<LocationList> locationLists = new ArrayList<>();
    public GetLocationModel getLocationModel = new GetLocationModel();
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
    public @Inject
    RunTimePermission runTimePermission;
    public ImageView back;
    public RelativeLayout signoutlayout, flaglayout;
    public CustomTextView edit;
    public CustomTextView hometext;
    public CustomTextView tvHomeaddress;
    public CustomTextView tvlanguage;
    public CustomTextView tvWorkaddress;
    public CustomTextView tvWorkDeliverystatus;
    public CustomTextView tvWorkDeliverynotes;
    public CustomTextView tvDeliverynotes;
    public CustomTextView tvDeliverystatus;
    public RelativeLayout rltHomelayout;
    public RelativeLayout rltWorklayout;
    private AlertDialog dialog;
    private RelativeLayout rltlanguage;

    LanguageConverter languageConverter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        AppController.getAppComponent().inject(this);

        intViews();

        /**
         * Sign out button
         */
        signoutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert();
            }
        });

        /**
         * Add Home
         */
        rltHomelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addhome = new Intent(getApplicationContext(), LocActivity.class);
                addhome.putExtra("location", "home");
                addhome.putExtra("type", 0);
                startActivity(addhome);
            }
        });


        /**
         *  Add Work
         */
        rltWorklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addwork = new Intent(getApplicationContext(), LocActivity.class);
                addwork.putExtra("location", "work");
                addwork.putExtra("type", 1);
                startActivity(addwork);
            }
        });
        commonMethods.rotateArrow(back,this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvlanguage.setText(sessionManager.getAppLanguage());

        /**
         * Change Language
         */
        rltlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] languages = getResources().getStringArray(R.array.languages);
                String[] langCode = getResources().getStringArray(R.array.languageCode);
                languageConverter=new LanguageConverter(SettingActivity.this,true,languages,langCode,SettingActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onResume() {
        super.onResume();
        locationList();
    }

    /**
     * To set location of home and work
     */


    public void locationList() {
        System.out.println("GETLIST Settings " + sessionManager.getLocationList());
        getLocationModel = gson.fromJson(sessionManager.getLocationList(), GetLocationModel.class);
        if (getLocationModel != null) {
            locationLists.clear();
            locationLists.addAll(getLocationModel.getLocationList());
        } else {

            tvHomeaddress.setText(getResources().getString(R.string.add_home));
            tvWorkaddress.setText(getResources().getString(R.string.add_work));
            tvWorkDeliverystatus.setVisibility(View.GONE);
            tvWorkDeliverynotes.setVisibility(View.GONE);
            tvDeliverynotes.setVisibility(View.GONE);
            tvDeliverystatus.setVisibility(View.GONE);
        }
        System.out.println("List Siz " + locationLists.size());
        if (locationLists.size() < 0) {
            System.out.println("List Siz<0 " + locationLists.size());
        } else {

            for (int i = 0; i < locationLists.size(); i++) {
                if (locationLists.get(i).getType() == 0) {
                    //home
                    tvDeliverystatus.setVisibility(View.VISIBLE);

                    tvHomeaddress.setText(locationLists.get(i).getFirstAddress());
                    if (locationLists.get(i).getDeliveryoptions() == 0) {
                        tvDeliverystatus.setText(getString(R.string.meet_at_vehicle));
                    } else {
                        if (locationLists.get(i).getApartment() == null || locationLists.get(i).getApartment().equals("")) {
                            tvDeliverystatus.setText(getString(R.string.deliver_to_door));
                        } else {
                            tvDeliverystatus.setText(getString(R.string.deliver_to_door) + " " + locationLists.get(i).getApartment());
                        }
                    }

                    if (locationLists.get(i).getDeliverynote() == null || locationLists.get(i).getDeliverynote().equals("")) {
                        tvDeliverynotes.setVisibility(View.GONE);
                    } else {
                        tvDeliverynotes.setVisibility(View.VISIBLE);
                        tvDeliverynotes.setText(locationLists.get(i).getDeliverynote());
                    }
                    break;
                } else {
                    tvHomeaddress.setText(getResources().getString(R.string.add_home));
                    tvDeliverynotes.setVisibility(View.GONE);
                    tvDeliverystatus.setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < locationLists.size(); i++) {
                if (locationLists.get(i).getType() == 1) {
                    //work
                    tvWorkDeliverystatus.setVisibility(View.VISIBLE);

                    tvWorkaddress.setText(locationLists.get(i).getFirstAddress());
                    if (locationLists.get(i).getDeliveryoptions() == 1) {
                        if (locationLists.get(i).getApartment() == null || locationLists.get(i).getApartment().equals("")) {
                            tvWorkDeliverystatus.setText(getString(R.string.deliver_to_door));
                        } else {
                            tvWorkDeliverystatus.setText(getString(R.string.deliver_to_door) + " " + locationLists.get(i).getApartment());
                        }
                    } else {
                        tvWorkDeliverystatus.setText(getString(R.string.meet_at_vehicle));
                    }

                    if (locationLists.get(i).getDeliverynote() == null || locationLists.get(i).getDeliverynote().equals("")) {
                        tvWorkDeliverynotes.setVisibility(View.GONE);
                    } else {
                        tvWorkDeliverynotes.setVisibility(View.VISIBLE);
                        tvWorkDeliverynotes.setText(locationLists.get(i).getDeliverynote());
                    }
                    break;
                } else {
                    tvWorkaddress.setText(getResources().getString(R.string.add_work));
                    tvWorkDeliverystatus.setVisibility(View.GONE);
                    tvWorkDeliverynotes.setVisibility(View.GONE);
                }
            }
        }

    }


    /**
     * To intialize views
     */


    public void intViews() {
        back = findViewById(R.id.arrow);
        signoutlayout = findViewById(R.id.signoutlayout);
        edit = findViewById(R.id.edit);
        hometext = findViewById(R.id.hometext);
        flaglayout = findViewById(R.id.flaglayout);
        rltHomelayout = findViewById(R.id.rltHomelayout);
        rltWorklayout = findViewById(R.id.rltWorklayout);
        rltlanguage = findViewById(R.id.rltlanguage);
        tvHomeaddress = findViewById(R.id.tvHomeaddress);
        tvlanguage = findViewById(R.id.tvlanguage);
        tvWorkaddress = findViewById(R.id.tvWorkaddress);
        tvWorkDeliverystatus = findViewById(R.id.tvWorkDeliverystatus);
        tvWorkDeliverynotes = findViewById(R.id.tvWorkDeliverynotes);
        tvDeliverynotes = findViewById(R.id.tvDeliverynotes);
        tvDeliverystatus = findViewById(R.id.tvDeliverystatus);


        dialog = commonMethods.getAlertDialog(this);

    }

    /**
     * Dialog for sign out confirmation
     */


    public void alert() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
       // if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction))) {
            builder.setMessage(R.string.signout_msg).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton(getResources().getString(R.string.signout), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    logOut();
                }
            }).show();
        //}
        /*else
        {
            builder.setMessage(R.string.signout_msg).setPositiveButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setNegativeButton(getResources().getString(R.string.signout), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    logOut();
                }
            }).show();

        }*/

    }

    /**
     * Api calling method logout
     */


    public void logOut() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.logOut(sessionManager.getToken()).enqueue(new RequestCallback(REQ_LOGOUT, this));
    }

    /**
     * API on Success Result
     */

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_LOGOUT:
                if (jsonResp.isSuccess()) {
                    onSuccessLogOut();
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_UPDATE_LANGUAGE:
                if (jsonResp.isSuccess()){
                    changeLanguage();
                }else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;

        }

    }

    private void changeLanguage() {
        setLocale(sessionManager.getAppLanguageCode());
        recreate();
        sessionManager.setDeliveredTime("");
        sessionManager.setHomeScheduledDay("");
        sessionManager.setType("0");
        sessionManager.setOrderType(0);
        sessionManager.setScheduledDay("");
        sessionManager.setScheduleMin("");
        sessionManager.setScheduleDate("");
        sessionManager.setAddedTime("");
        sessionManager.setIsFirst(true);
        sessionManager.setCartCount(0);
        sessionManager.setCartAmount("");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("type", "home");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Success Fully Log out
     */
    private void onSuccessLogOut() {

        String lang = sessionManager.getAppLanguage();
        String langCode = sessionManager.getAppLanguageCode();

        sessionManager.clearToken();
        sessionManager.clearAll();
        // Clear local saved data
        clearApplicationData(); // Clear cache data

        sessionManager.setAppLanguage(lang);
        sessionManager.setAppLanguageCode(langCode);

        finishAffinity();
        Intent intent = new Intent(getApplicationContext(), EntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Clear application data while logout
     */
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!"lib".equals(s)) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    /**
     * delete cache data while delete
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }

        return dir.delete();
    }

    @Override
    public void onSuccess(String language, String languageCode) {
        updatelanguage(language, languageCode);
    }

    private void updatelanguage(String language, String languageCode){
        commonMethods.showProgressDialog(this, customDialog);
        apiService.updateLanguage(sessionManager.getToken(),languageCode,sessionManager.getType()).enqueue(new RequestCallback(REQ_UPDATE_LANGUAGE, this));
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
