package gofereats.views.main.subviews;
/**
 * @package com.gofereats
 * @subpackage views.subviews
 * @category Stripe Activity
 * @author Trioangle Product Team
 * @version 0.6
 */


/* ************************************************************
                Showing and Adding the payment methods
    *********************************************************** */

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.utils.CommonMethods;
import gofereats.views.customize.CustomDialog;

public class AddCardActivity extends AppCompatActivity {

    private static int REQUEST_CODE_PAYMENT = 1;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public ImageView arrow;
    public Button addpayment;
    public CardMultilineWidget stripe;
    public CountryCodePicker countryCodePicker;
    private String stripePublishKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        AppController.getAppComponent().inject(this);


        addpayment = findViewById(R.id.addpayment);
        stripe = findViewById(R.id.stripe);
        arrow = findViewById(R.id.arrow);
        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.setAutoDetectedCountry(true);
        //commonMethods.rotateArrow(arrow,this);
        String laydir = getResources().getString(R.string.layout_direction);
        if ("1".equals(laydir)) {
            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ARABIC);
            countryCodePicker.setCurrentTextGravity(CountryCodePicker.TextGravity.RIGHT);
            countryCodePicker.setGravity(Gravity.START);
        }
        stripePublishKey = getIntent().getStringExtra("stripePublishKey");
        System.out.println("stripePublishKey " + stripePublishKey);

        /**
         * On back pressed
         */
        commonMethods.rotateArrow(arrow,this);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        /**
         * Pay through  Stripe Payment
         */
        addpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Card cardToSave = stripe.getCard();
                if (cardToSave != null) {

                    commonMethods.showProgressDialog(AddCardActivity.this, customDialog);
                    final Stripe stripe = new Stripe(getApplicationContext(), stripePublishKey);
                    stripe.createToken(cardToSave, new TokenCallback() {
                        public void onSuccess(Token token) {
                            commonMethods.hideProgressDialog();
                            // Send token to your server
                            //Toast.makeText(getApplicationContext(),"Token "+token.getId().toString(),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.putExtra("token", token.getId());
                            setResult(REQUEST_CODE_PAYMENT, intent);
                            finish();//finishing activity
                        }

                        public void onError(Exception error) {
                            // Show localized error message
                            commonMethods.hideProgressDialog();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("token", "");
        setResult(REQUEST_CODE_PAYMENT, intent);
        finish();
    }
}
