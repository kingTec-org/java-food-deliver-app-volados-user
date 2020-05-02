package gofereats.views.main.subviews;
/**
 * @package com.trioangle.gofereats
 * @subpackage views.main.subviews
 * @category EntryActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.obs.CustomButton;

import java.util.Locale;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.utils.LanguageConverter;
import gofereats.views.signup_login.LoginActivity;
import gofereats.views.signup_login.RegisterActivity;


public class EntryActivity extends AppCompatActivity implements LanguageConverter.onSuccessLanguageChangelistner {

    public @Inject
    SessionManager sessionManager;

    LanguageConverter languageConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Setting Layout after Removing Title
        setContentView(R.layout.activity_entry);
        AppController.getAppComponent().inject(this);



        sessionManager.setDeliveredTime("");
        sessionManager.setHomeScheduledDay("");
        sessionManager.setType("0");
        sessionManager.setOrderType(0);
        sessionManager.setOrderType(0);
        sessionManager.setScheduledDay("");
        sessionManager.setScheduleMin("");
        sessionManager.setScheduleDate("");
        sessionManager.setAddedTime("");
        /*sessionManager.setCurrencyCode("$");
        sessionManager.setCurrencySymbol("$");
*/
        CustomButton login_button = findViewById(R.id.login_button);
        CustomButton register_button = findViewById(R.id.register_button);
        RelativeLayout rltlanguage = findViewById(R.id.rltlanguage);
        TextView language = findViewById(R.id.language);
        ImageView applogo=findViewById(R.id.applogo);

        language.setText(sessionManager.getAppLanguage());


        /**
         * Redirects to login activity
         */
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(Login);
                finish();
            }
        });
        /**
         * Redirects to Register activity
         */
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(register);
            }
        });

        rltlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] languages = getResources().getStringArray(R.array.languages);
                String[] langCode = getResources().getStringArray(R.array.languageCode);
                languageConverter=new LanguageConverter(EntryActivity.this,true,languages,langCode,EntryActivity.this);
            }
        });
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)applogo.getLayoutParams();
        if(getResources().getString(R.string.layout_direction).equals("1"))
        {
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
        }
        else
        {
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onSuccess(String language, String languageCode) {
        System.out.println("getLanguage "+language);
        setLocale(languageCode);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
