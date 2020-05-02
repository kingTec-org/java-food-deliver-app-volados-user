package gofereats.pushnotification;
/**
 * @package com.gofereats
 * @subpackage pushnotification
 * @category FirebaseMessagingService
 * @author Trioangle Product Team
 * @version 1.0
 */

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.utils.CommonMethods;
import gofereats.views.main.MainActivity;
import gofereats.views.main.ShowDialog;


/**************************************************************
 Firebase Messaging Service to base push notification message to activity
 ****************************************************************/
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public @Inject
    SessionManager sessionManager;
    public String message = "";
    public String status = "";
    public String title = "";
    public int orderId = 0;

    public @Inject
    CommonMethods commonMethods;

    @Override
    public void onCreate() {
        super.onCreate();
        AppController.getAppComponent().inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Message Notification remoteMessage: " + remoteMessage.toString());

        if (remoteMessage.getNotification() != null) {
            message = remoteMessage.getNotification().getBody();
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage == null) return;

        // Check if message contains AddCardActivity data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());


            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
                if (remoteMessage.getNotification() != null) {
                    Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {

            /*
            Sample Data From Push Notification
            {custom={"type":"order_cancelled","title":"Order has been cancelled.","order_id":545}}
            {custom={"type":"Refund","title":"Amount has been refund","order_id":155}}*/

            sessionManager.setPushNotification(json.toString());

            if (json.getJSONObject("custom").has("type")) {

                status = json.getJSONObject("custom").getString("type");
            }

            if (json.getJSONObject("custom").has("title")) {
                message="";
                title = json.getJSONObject("custom").getString("title");
            }
            if (json.getJSONObject("custom").has("order_id")) {
                orderId = json.getJSONObject("custom").getInt("order_id");
            }

            if ("".equals(message)) {
                message = title;
            }
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Log.e(TAG, "IF: " + json.toString());
                // app is in foreground, broadcast the push message

                if (json.getJSONObject("custom").has("type")) {

                    status = json.getJSONObject("custom").getString("type");
                }


                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                /**
                 * Order Types
                 * Type 1 --- Order Accepted
                 * Type 2 --- Order cancel
                 * Type 3 --- Order Delayed
                 * Type 4 --- Order Delivery Started
                 * Type 5 --- Order Refund
                 * Type 6 --- Order Delivery completed
                 */

                /**
                 *Shows Push Notification When App Not in background(On Open)
                 */

                if ("order_cancelled".equalsIgnoreCase(status)) {
                    if (MainActivity.isRefreshed) {
                        Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                        dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialogs.putExtra("message", title);
                        dialogs.putExtra("type", 2);
                        startActivity(dialogs);
                        MainActivity.isRefreshed = false;
                    }
                } else if ("order_delivery_completed".equalsIgnoreCase(status)) {
                    if (MainActivity.isRefreshed) {
                        Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                        dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dialogs.putExtra("message", title);
                        dialogs.putExtra("type", 6);
                        startActivity(dialogs);
                        MainActivity.isRefreshed = false;
                    }
                } else if ("Refund".equalsIgnoreCase(status) || "Amount Refund".equalsIgnoreCase(status)) {
                    MainActivity.isOrder = false;
                    Intent dialogs = new Intent(getApplicationContext(), ShowDialog.class);
                    dialogs.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogs.putExtra("message", title);
                    dialogs.putExtra("type", 5);
                    startActivity(dialogs);
                }
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                notificationUtils.generateNotification(getApplicationContext(), message, status, json.toString());
            } else {
                Log.e(TAG, "ELSE: " + json.toString());
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                if (!sessionManager.getToken().equals("")) {
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                }

                //if ("order_accepted".equalsIgnoreCase(status) || "order_cancelled".equalsIgnoreCase(status) || "order_delayed".equalsIgnoreCase(status) || "order_delivery_started".equalsIgnoreCase(status) || "Refund".equalsIgnoreCase(status) || "Amount Refund".equalsIgnoreCase(status) || "order_delivery_completed".equalsIgnoreCase(status)) {
                    /**
                     *Shows Push Notification When App in background(Closed)
                     */
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                    notificationUtils.generateNotification(getApplicationContext(), message, status, json.toString());
                //}
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "hereException: " + e.getMessage());
            e.printStackTrace();
        }

    }

}