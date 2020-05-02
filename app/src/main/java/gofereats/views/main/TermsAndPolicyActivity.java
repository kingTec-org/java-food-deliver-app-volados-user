package gofereats.views.main;

import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import gofereats.R;

public class TermsAndPolicyActivity extends AppCompatActivity {

    public TextView webtitle;
    public String url, title;
    public ImageView web_title_back;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_policy);
        webtitle = findViewById(R.id.webtitle);
        web_title_back = findViewById(R.id.web_title_back);

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
