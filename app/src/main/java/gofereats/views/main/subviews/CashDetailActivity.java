package gofereats.views.main.subviews;

/**
 * @package com.gofereats
 * @subpackage views.subviews
 * @category Cash Details Activity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import javax.inject.Inject;

import gofereats.R;
import gofereats.utils.CommonMethods;


/* ************************************************************
                class to show Cash Details
    *********************************************************** */
public class CashDetailActivity extends AppCompatActivity {

    public ImageView arrow;
    public @Inject
    CommonMethods commonMethods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_detail);


        arrow = findViewById(R.id.arrow);

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
    }
}
