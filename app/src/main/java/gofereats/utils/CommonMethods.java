package gofereats.utils;
/**
 * @package com.trioangle.gofereats
 * @subpackage utils
 * @category CommonMethods
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.Constants;
import gofereats.configs.SessionManager;
import gofereats.views.customize.CustomDialog;

/*****************************************************************
 CommonMethods
 ****************************************************************/
public class CommonMethods {

    public static PopupWindow popupWindow = null;
    public static CustomDialog progressDialog;

    public @Inject
    SessionManager sessionManager;

    public CommonMethods() {
        AppController.getAppComponent().inject(this);

    }


    public static Drawable getCardImage(String brand, Resources getRes) {
        Drawable card;

        if (brand.contains("Visa")) {
            card = getRes.getDrawable(R.drawable.card_visa);
        } else if (brand.contains("MasterCard")) {
            card = getRes.getDrawable(R.drawable.card_master);
        } else if (brand.contains("Discover")) {
            card = getRes.getDrawable(R.drawable.card_discover);
        } else if (brand.contains("Amex") || brand.contains("American Express")) {
            card = getRes.getDrawable(R.drawable.card_amex);
        } else if (brand.contains("JCB") || brand.contains("JCP")) {
            card = getRes.getDrawable(R.drawable.card_jcp);
        } else if (brand.contains("Diner") || brand.contains("Diners")) {
            card = getRes.getDrawable(R.drawable.card_diner);
        } else if ("Union".contains(brand) || "UnionPay".contains(brand)) {
            card = getRes.getDrawable(R.drawable.card_unionpay);
        } else {
            card = getRes.getDrawable(R.drawable.card_basic);
        }
        // return getRes.getDrawable(R.drawable.card_basic);
        return card;
    }

    public void showProgressDialog(AppCompatActivity mActivity, CustomDialog customDialog) {
        if (mActivity == null || customDialog == null || (customDialog.getDialog() != null && customDialog.getDialog().isShowing()))
            return;
        progressDialog = new CustomDialog(true);
        progressDialog.show(mActivity.getSupportFragmentManager(), "");
    }

    public void hideProgressDialog() {
        if (progressDialog == null || progressDialog.getDialog() == null || !progressDialog.getDialog().isShowing())
            return;
        progressDialog.dismissAllowingStateLoss();
        progressDialog = null;
    }

    //Show Dialog with message
    public void showMessage(Context context, AlertDialog dialog, String msg) {
        if (context != null && dialog != null && !((Activity) context).isFinishing()) {
            dialog.setMessage(msg);
            dialog.show();
        }
    }


    /**
     * To Show dialog Message Without cancel
     */
    public void showMessage(final Context context, final AlertDialog dialog, String msg, Boolean cancellable) {
        if (context != null && dialog != null && !((Activity) context).isFinishing()) {
            dialog.setMessage(msg);
            dialog.setCancelable(cancellable);
            dialog.show();
        }
    }

    //Show Dialog with message
    public void showDialog(Context context, AlertDialog dialog, String msg) {
        if (context != null && dialog != null && !((Activity) context).isFinishing()) {
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    public boolean isLoading(AppCompatActivity mActivity, CustomDialog customDialog) {
        if (mActivity == null || customDialog == null || (customDialog.getDialog() != null && customDialog.getDialog().isShowing()))
            return false;
        return customDialog.isVisible();
    }

    //Create and Get Dialog
    public AlertDialog getAlertDialog(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        return dialog;
    }

    public boolean isNotEmpty(Object s) {
        if (s == null) {
            return false;
        }
        if ((s instanceof String) && (((String) s).trim().length() > 0)) {
            return true;
        }
        if (s instanceof ArrayList) {
            return !((ArrayList<?>) s).isEmpty();
        }
        if (s instanceof Map) {
            return !((Map<?, ?>) s).isEmpty();
        }
        if (s instanceof List) {
            return !((List<?>) s).isEmpty();
        }

        if (s instanceof Object[]) {
            return ((Object[]) s).length != 0;
        }
        return false;
    }

    public boolean isTaped() {
        if (SystemClock.elapsedRealtime() - Constants.mLastClickTime < 1000) return true;
        Constants.mLastClickTime = SystemClock.elapsedRealtime();
        return false;
    }

    public File cameraFilePath() {
        return new File(getDefaultCameraPath(), "GoferEats_" + System.currentTimeMillis() + ".png");
    }

    public String getDefaultCameraPath() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (path.exists()) {
            File test1 = new File(path, "Camera/");
            if (test1.exists()) {
                path = test1;
            } else {
                File test2 = new File(path, "100MEDIA/");
                if (test2.exists()) {
                    path = test2;
                } else {
                    File test3 = new File(path, "100ANDRO/");
                    if (test3.exists()) {
                        path = test3;
                    } else {
                        test1.mkdirs();
                        path = test1;
                    }
                }
            }
        } else {
            path = new File(path, "Camera/");
            path.mkdirs();
        }
        return path.getPath();
    }

    public void refreshGallery(Context context, File file) {
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file); //out is your file you saved/deleted/moved/copied
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////// CAMERA ///////////////////////////////////
    public File getDefaultFileName(Context context) {
        File imageFile;
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) { // External storage path
            imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + Constants.FILE_NAME + System.currentTimeMillis() + ".png");
        } else {  // Internal storage path
            imageFile = new File(context.getFilesDir() + File.separator + Constants.FILE_NAME + System.currentTimeMillis() + ".png");
        }
        return imageFile;
    }

    /*public void dropDownMenu(AppCompatActivity mActivity, View v, int[] images, final String[] listItems, final DropDownClickListener listener) {
        LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        *//*View popupView = layoutInflater.inflate(R.layout.drop_down_menu_list, null);
        ListView listView = (ListView) popupView.findViewById(R.id.drop_down_list);

        DropDownMenuAdapter adapter = new DropDownMenuAdapter(mActivity, R.layout.drop_down_menu_row, images, listItems);
        listView.setAdapter(adapter);
        listView.setSelected(false);*//*

        Rect displayFrame = new Rect();
        v.getWindowVisibleDisplayFrame(displayFrame);

        int displayFrameWidth = displayFrame.right - displayFrame.left;
        int[] loc = new int[2];
        v.getLocationInWindow(loc);

        if (popupWindow == null) {
            int width = (int) (120 * Resources.getSystem().getDisplayMetrics().density);
            popupWindow = new PopupWindow(popupView, width, WindowManager.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        if (!popupWindow.isShowing()) {
            popupView.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.popup_anim_in));
            int margin = (displayFrameWidth - (loc[0] + v.getWidth()));
            int xOff = (displayFrameWidth - margin - popupWindow.getWidth()) - loc[0];
            popupWindow.showAsDropDown(v, (int) (xOff / 0.8), 0);
            popupWindow.setAnimationStyle(R.anim.popup_anim_in);

        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (popupWindow != null) popupWindow = null;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (popupWindow != null && popupWindow.isShowing()) popupWindow.dismiss();
                listener.onDropDrownClick(listItems[position]);
            }
        });
    }

    public boolean isSupportTransition() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }*/

    public void clearFileCache(String path) {
        try {
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNetWorkAvailable(Context context, AlertDialog dialog, String msg) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo == null || (netInfo != null && !netInfo.isConnected())) {
                showMessage(context, dialog, msg);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean isOnline(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public Object getJsonValue(String jsonString, String key, Object object) {
        Object objct = object;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has(key)) objct = jsonObject.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return objct;
    }

    /**
     * Snack  bar to messages to the user
     *
     * @param message       message that is to be shown
     * @param buttonmessage to set Text on button
     * @param buttonvisible button visibility boolean value
     * @param duration      duration of the snackbar
     */

    public void snackBar(String message, String buttonmessage, boolean buttonvisible, int duration, RelativeLayout rel, Resources getRes, Activity ctx) {
        // Create the Snackbar
        Snackbar snackbar;
        RelativeLayout snackbar_background;
        TextView snack_button;
        TextView snack_message;
        // Snack bar visible duration
        if (duration == 1) snackbar = Snackbar.make(rel, "", Snackbar.LENGTH_INDEFINITE);
        else if (duration == 2) snackbar = Snackbar.make(rel, "", Snackbar.LENGTH_LONG);
        else snackbar = Snackbar.make(rel, "", Snackbar.LENGTH_SHORT);

        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = ctx.getLayoutInflater().inflate(R.layout.snackbar, null);
        // Configure the view

        snackbar_background = snackView.findViewById(R.id.snackbar);
        snack_button = snackView.findViewById(R.id.snack_button);
        snack_message = snackView.findViewById(R.id.snack_message);

        snackbar_background.setBackgroundColor(getRes.getColor(R.color.black)); // Background Color

        if (buttonvisible) // set Right side button visible or gone
            snack_button.setVisibility(View.VISIBLE);
        else snack_button.setVisibility(View.GONE);

        snack_button.setTextColor(getRes.getColor(R.color.bluelight)); // set right side button text color
        snack_button.setText(buttonmessage); // set right side button text


        snack_message.setTextColor(getRes.getColor(R.color.white)); // set left side main message text color
        snack_message.setText(message);  // set left side main message text

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getRes.getColor(R.color.black));
        snackbar.show();
        System.out.println("Snack bar ended");

    }
    public void rotateArrow(ImageView ivArrow, Context context) {
        if (context.getResources().getString(R.string.layout_direction).equals("1")) {
            ivArrow.setRotation(180);
        } else {
            ivArrow.setRotation(0);
        }
    }


}

