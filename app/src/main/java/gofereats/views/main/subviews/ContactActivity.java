package gofereats.views.main.subviews;


/**
 * @package com.gofereats
 * @subpackage views.subviews
 * @category Contact Activity
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.obs.CustomTextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.utils.CommonMethods;
import gofereats.views.customize.CustomDialog;
/* ************************************************************
                Class to contact Driver activity
    *********************************************************** */


public class ContactActivity extends AppCompatActivity {

    public static final Integer CALL = 0x2;
    public static final Integer SMS = 300;
    public @InjectView(R.id.arrow)
    ImageView back;
    public @InjectView(R.id.tvDriverName)
    CustomTextView tvDriverName;
    public @InjectView(R.id.tvDriverNumber)
    CustomTextView tvDriverNumber;
    public @InjectView(R.id.lltDriverMessage)
    LinearLayout lltDriverMessage;
    public @InjectView(R.id.lltDriverCall)
    LinearLayout lltDriverCall;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    CommonMethods commonMethods;
    public String driverName;
    public String driverNumber;

    @OnClick(R.id.lltDriverMessage)
    public void message() {
        sendSms(driverNumber);

    }

    @OnClick(R.id.arrow)
    public void back() {
        onBackPressed();

    }

    @OnClick(R.id.lltDriverCall)
    public void call() {
        phoneCall(tvDriverNumber);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);

        commonMethods.rotateArrow(back,this);
        getDatas();
    }


    /**
     * Call permission
     */
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ContactActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ContactActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(ContactActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(ContactActivity.this, new String[]{permission}, requestCode);
            }
        }
    }


    /**
     * To call driver and to check phone access permission
     *
     * @param phonenumber of the driver
     */


    public void phoneCall(CustomTextView phonenumber) {
        String callnumber = phonenumber.getText().toString();
        askForPermission(Manifest.permission.CALL_PHONE, CALL);

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callnumber));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    /**
     * to Send sms to the driver
     *
     * @param numb of the driver
     */


    public void sendSms(String numb) {
        //askForPermission(Manifest.permission.SEND_SMS, SMS);
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", numb);
        smsIntent.putExtra("sms_body", "");
        /*if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        startActivity(smsIntent);
    }


    /**
     * To get values through the intent
     */

    public void getDatas() {
        driverName = getIntent().getStringExtra("drivername");
        driverNumber = getIntent().getStringExtra("drivernumber");
        tvDriverName.setText(driverName);
        tvDriverNumber.setText("+" + driverNumber);
    }
}
