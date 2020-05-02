package gofereats.views.main.subviews;


/**
 * @package com.gofereats
 * @subpackage views.subviews
 * @category Edit Profile Activity
 * @author Trioangle Product Team
 * @version 1.0
 */


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.obs.CustomEditText;
import com.obs.CustomTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereats.BuildConfig;
import gofereats.R;
import gofereats.backgroundtask.ImageCompressAsyncTask;
import gofereats.backgroundtask.ImageMinimumSizeCalculator;
import gofereats.configs.AppController;
import gofereats.configs.Constants;
import gofereats.configs.RunTimePermission;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.profile.UserProfileModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ImageListener;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;
import gofereats.views.signup_login.MobileActivity;
import okhttp3.RequestBody;

import static gofereats.utils.Enums.REQ_UPDATE_PROFILE;
import static gofereats.utils.Enums.REQ_UPLOAD_PROFILE_IMG;


/* ************************************************************
                Activity to edit profile
    ***********************************************************/

public class EditProfileActivity extends AppCompatActivity implements ImageListener, ServiceListener {

    public String userChoosenTask;
    public UserProfileModel userProfileModel;
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
    public @InjectView(R.id.ivProfileImage)
    CircleImageView ivProfileImage;
    public @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    public @InjectView(R.id.edtInputFirst)
    CustomEditText edtInputFirst;
    public @InjectView(R.id.edtInputlast)
    CustomEditText edtInputlast;
    public @InjectView(R.id.edtEmail)
    EditText edtEmail;
    public @InjectView(R.id.ccp)
    CountryCodePicker ccp;
    public @InjectView(R.id.edtMobile)
    EditText edtMobile;
    public @InjectView(R.id.ivBackArrow)
    ImageView ivBackArrow;
    public @InjectView(R.id.edtSave)
    CustomTextView edtSave;
    public @InjectView(R.id.rltMobileChangeLayout)
    RelativeLayout rltMobileChangeLayout;
    public String fullName;
    public String firstName;
    public String lastName;
    public String[] name;
    public String imageurl;
    public boolean isFromAccount;
    private AlertDialog dialog;
    private File imageFile = null;
    private String imagePath = "";

    @OnClick(R.id.ivProfileImage)
    public void changeProfile() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        checkAllPermission(permissions);
    }

    @OnClick(R.id.edtMobile)
    public void change() {
        Intent editPhone = new Intent(this, MobileActivity.class);
        editPhone.putExtra("isChange", true);
        startActivity(editPhone);
    }

    @OnClick(R.id.ivBackArrow)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.edtSave)
    public void save() {
        saveChanges();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        isFromAccount = getIntent().getBooleanExtra("isFromAccount", false);
        commonMethods.rotateArrow(ivBackArrow,this);
        dialog = commonMethods.getAlertDialog(this);

        getProfileDetails();

    }

    /**
     * Api calling method to save edited changes
     */

    private void saveChanges() {
        commonMethods.showProgressDialog(EditProfileActivity.this, customDialog);
        apiService.updateProfile(sessionManager.getToken(), editProfile()).enqueue(new RequestCallback(REQ_UPDATE_PROFILE, this));
        sessionManager.setName(edtInputFirst.getText().toString());
    }


    /**
     * Method to return profile details
     *
     * @return details  of hash map type
     */

    public HashMap<String, String> editProfile() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("first_name", edtInputFirst.getText().toString().trim());
        hashMap.put("last_name", edtInputlast.getText().toString().trim());
        hashMap.put("email", edtEmail.getText().toString());
        hashMap.put("country_code", ccp.getSelectedCountryCode());
        hashMap.put("mobile_number", edtMobile.getText().toString().trim());
        return hashMap;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("type", "account");
        startActivity(intent);
    }

    /**
     * Getting and setting The Profile details
     */
    public void getProfileDetails() {
        fullName = getIntent().getStringExtra("name");

        /* Spliting the First and last name */

        name = fullName.split(" ", 2);
        firstName = name[0];
        lastName = name[1];

        edtInputFirst.setText(firstName);
        edtInputlast.setText(lastName);
        edtEmail.setText(getIntent().getStringExtra("email"));

        edtMobile.setText(getIntent().getStringExtra("mobile"));
        ccp.setCountryForPhoneCode(Integer.valueOf(getIntent().getStringExtra("countrycode")));


        Glide.with(this).load(getIntent().getStringExtra("profile")).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(ivProfileImage);

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_UPLOAD_PROFILE_IMG:
                if (jsonResp.isSuccess()) {
                    onSuccessUploadImage(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_UPDATE_PROFILE:
                if (jsonResp.isSuccess()) {
                    onSuccessUpdateProfile(jsonResp);
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
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * Method to check permissions
     *
     * @param permission
     */

    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getResources().getString(R.string.enable_permission));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 300);
            }
        } else {
            pickImage();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);

        } else {
            pickImage();
        }
    }

    private void showEnablePermissionDailog(final int type, String message) {
   /*     AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        if (getResources().getString(R.string.layout_direction).equalsIgnoreCase("0")) {
            builder.create();
        }
        builder.setTitle(getResources().getString(R.string.alert));
        builder.setMessage(message);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (type == 0) {
                    callPermissionSettings();
                }
                else {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog =builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        builder.show();*/
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(getResources().getString(R.string.alert), message, getString(R.string.ok), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0) callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * To check call permissions
     */

    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Alert to select Camera or Gallery
     */
    public void pickImage() {

        final CharSequence[] items = {getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_gallery), getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(getResources().getString(R.string.take_photo))) {
                    userChoosenTask = "Take Photo";

                    cameraIntent();

                } else if (items[item].equals(getResources().getString(R.string.choose_gallery))){
                    userChoosenTask = "Choose from Library";

                    galleryIntent();

                } else if (items[item].equals(getResources().getString(R.string.cancel))){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Intent to camera
     */
    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = commonMethods.cameraFilePath();
        Uri imageUri = FileProvider.getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);

        try {
            List<ResolveInfo> resolvedIntentActivities = EditProfileActivity.this.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;
                grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            cameraIntent.putExtra("return-data", true);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, 1);
        commonMethods.refreshGallery(EditProfileActivity.this, imageFile);

    }

    /**
     * Intent to gallery page
     */
    private void galleryIntent() {
        imageFile = commonMethods.getDefaultFileName(EditProfileActivity.this);

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, Constants.REQUEST_CODE_GALLERY);
    }


    /**
     * OnActivity Result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    startCropImage();
                    break;
                case Constants.REQUEST_CODE_GALLERY:
                    try {
                        InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        if (inputStream != null) inputStream.close();
                        startCropImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    try {
                        imagePath = result.getUri().getPath();
                        if (!TextUtils.isEmpty(imagePath)) {
                            commonMethods.showProgressDialog(this, customDialog);
                            new ImageCompressAsyncTask(EditProfileActivity.this, imagePath, this).execute();
                        }
                    } catch (OutOfMemoryError | Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Input output Stream
     */
    private void copyStream(InputStream input, FileOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    /**
     * Crop Profile Image
     */
    private void startCropImage() {
        if (imageFile == null) return;
        int[] minimumSquareDimen = ImageMinimumSizeCalculator.getMinSquarDimension(Uri.fromFile(imageFile), this);
        CropImage.activity(Uri.fromFile(imageFile)).setAspectRatio(10, 10).setOutputCompressQuality(100).setMinCropResultSize(minimumSquareDimen[0], minimumSquareDimen[1]).start(this);
    }

    /**
     * Image To Compress and Update Using Post Method
     */
    @Override
    public void onImageCompress(String filePath, RequestBody requestBody) {
        commonMethods.hideProgressDialog();
        if (!TextUtils.isEmpty(filePath) && requestBody != null) {
            commonMethods.showProgressDialog(this, customDialog);
            apiService.uploadImage(requestBody, sessionManager.getToken(), sessionManager.getType()).enqueue(new RequestCallback(REQ_UPLOAD_PROFILE_IMG, this));
        }
    }

    /**
     * Upload Image using Post Method
     */
    private void onSuccessUploadImage(JsonResponse jsonResponse) {
        Toast.makeText(this, getResources().getString(R.string.image_upload), Toast.LENGTH_SHORT).show();
        imageurl = (String) commonMethods.getJsonValue(jsonResponse.getStrResponse(), Constants.PROFILEIMAGE, String.class);
        sessionManager.setImage(imageurl);
        loadImage(imageurl);
    }

    /* Update User profile
     * @param jsonResponse
     */
    private void onSuccessUpdateProfile(JsonResponse jsonResponse) {
        Toast.makeText(this, getResources().getString(R.string.profile_upload), Toast.LENGTH_SHORT).show();
        userProfileModel = gson.fromJson(jsonResponse.getStrResponse(), UserProfileModel.class);
    }

    /**
     * Load Image
     *
     * @param imageurl
     */
    private void loadImage(String imageurl) {
        commonMethods.hideProgressDialog();

        Glide.with(this).load(imageurl).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivProfileImage);
    }
}

