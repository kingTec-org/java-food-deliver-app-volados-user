package gofereats.utils;

/**
 * @package com.trioangle.gofereats
 * @subpackage utils
 * @category SpacesItemDecoration
 * @author Trioangle Product Team
 * @version 1.0
 **/


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;


/*****************************************************************
 Terms and condition , privacy policy web url calling class
 ****************************************************************/


public class TermsWebView extends AppCompatActivity {
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public TextView webtitle;
    public String url, title;
    public ImageView web_title_back;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_web_view);
        AppController.getAppComponent().inject(this);


        Locale locale = new Locale(sessionManager.getAppLanguageCode()); // the locale to use for this activity
        fixupLocale(this, locale);
        webtitle = findViewById(R.id.webtitle);
        web_title_back = findViewById(R.id.web_title_back);
        commonMethods.rotateArrow(web_title_back,this);
        Intent x = getIntent();
        url = x.getStringExtra("WebUrl");
        title = x.getStringExtra("title");

        webtitle.setText(title);

        webView = findViewById(R.id.webView1);
        startWebView(url);

        web_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    static void fixupLocale(Context ctx, Locale newLocale) {
        final Resources res = ctx.getResources();
        final Configuration config = res.getConfiguration();
        final Locale curLocale = getLocale(config);
        if (!curLocale.equals(newLocale)) {
            Locale.setDefault(newLocale);
            final Configuration conf = new Configuration(config);
            conf.setLocale(newLocale);
            res.updateConfiguration(conf, res.getDisplayMetrics());
        }
    }

    private static Locale getLocale(Configuration config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0);
        } else {
            //noinspection deprecation
            return config.locale;
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();

    }

    /**
     * To set up Web view client and run the url
     *
     * @param url of privacy policy and terms and condition
     */


    private void startWebView(String url) {

        webView.setWebViewClient(new WebViewClient() {


            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("Webview url " + url);


                view.loadUrl(url);

                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            public void onPageFinished(WebView view, String url) {


                webView.loadUrl("javascript:android.showHTML(document.getElementById('json').innerHTML);");


            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                Log.d("", "progress" + progress);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage m) {

                return true;
            }
        });


        webView.loadUrl(url);


    }


}
