package gofereats.views.main.fragments;


/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.fragments.sortingfragments
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.obs.CustomTextView;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.profile.UserProfileModel;
import gofereats.interfaces.ActivityListener;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.utils.TermsWebView;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;
import gofereats.views.main.subviews.EditProfileActivity;
import gofereats.views.main.subviews.FavouritesActivity;
import gofereats.views.main.subviews.PaymentActivity;
import gofereats.views.main.subviews.PromoActivity;
import gofereats.views.main.subviews.SettingActivity;
import gofereats.views.main.subviews.wallet.AddWalletActivity;

/*****************************************************************
 Account Fragment used in Main Activity
 ****************************************************************/


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements ServiceListener {

    public String termsUrl, privacyUrl;
    public UserProfileModel userProfileModel;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    Gson gson;
    public @Inject
    ImageUtils imageUtils;
    public CircleImageView user_image;
    public ProgressBar progressBar;
    public LinearLayout settinglayout;
    public LinearLayout favorites_lay;
    public LinearLayout payment_layout;
    public LinearLayout promotion_layout;
    public LinearLayout lltWallet;
    public String name;
    public String email;
    public String mobile;
    public String countrycode;
    public String profileimage;
    public RelativeLayout rltProfile;
    protected CustomTextView user_name;
    protected CustomTextView tvWalletAmount;
    protected CustomTextView tvTerms;
    protected CustomTextView tvPrivacy;
    private View view;
    private ActivityListener listener;
    private MainActivity mActivity;
    private AlertDialog dialog;


    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Profile must implement ActivityListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        init();
        AppController.getAppComponent().inject(this);

        termsUrl = getString(R.string.site_url) + getString(R.string.terms_url)+"?token="+sessionManager.getToken();
        privacyUrl = getString(R.string.site_url) + getString(R.string.privacy_url)+"?token="+sessionManager.getToken();


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_account, container, false);
            intView();

        }

        rltProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isRefreshed = true;
                Intent account = new Intent(getActivity(), EditProfileActivity.class);
                account.putExtra("name", name);
                account.putExtra("email", email);
                account.putExtra("mobile", mobile);
                account.putExtra("countrycode", countrycode);
                account.putExtra("profile", profileimage);
                account.putExtra("isFromAccount", true);
                startActivity(account);
            }
        });


        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadUrl(termsUrl);
                MainActivity.isRefreshed = true;
                Intent account = new Intent(getActivity(), TermsWebView.class);
                account.putExtra("WebUrl", termsUrl);
                account.putExtra("title", mActivity.getResources().getString(R.string.terms_and_conditions));
                startActivity(account);

            }
        });


        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadUrl(privacyUrl);
                MainActivity.isRefreshed = true;
                Intent account = new Intent(getActivity(), TermsWebView.class);
                account.putExtra("WebUrl", privacyUrl);
                account.putExtra("title", mActivity.getResources().getString(R.string.privacy_policy));
                startActivity(account);

            }
        });

        settinglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isRefreshed = true;
                Intent setting = new Intent(getActivity(), SettingActivity.class);
                startActivity(setting);
            }
        });

        favorites_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isRefreshed = true;
                Intent favorites = new Intent(getActivity(), FavouritesActivity.class);
                startActivity(favorites);
            }
        });

        payment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.isRefreshed = true;
                Intent payment = new Intent(getActivity(), PaymentActivity.class);
                startActivity(payment);
            }
        });

        promotion_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.isRefreshed = true;
                Intent payment = new Intent(getActivity(), PromoActivity.class);
                startActivity(payment);
            }
        });

        lltWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isRefreshed = true;
                Intent wallet = new Intent(getActivity(), AddWalletActivity.class);
                startActivity(wallet);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MainActivity.isOrder) {
            getUserProfile();
        }
        MainActivity.isRefreshed = false;
    }

    /**
     * Activity Listener
     */
    private void init() {
        if (listener == null) return;

        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (MainActivity) getActivity();
    }


    /**
     * To Intialize views
     */


    private void intView() {
        settinglayout = view.findViewById(R.id.setting_layout);
        favorites_lay = view.findViewById(R.id.favorites_lay);
        payment_layout = view.findViewById(R.id.payment_layout);
        promotion_layout = view.findViewById(R.id.promotion_layout);
        user_name = view.findViewById(R.id.user_name);
        user_image = view.findViewById(R.id.user_image);
        progressBar = view.findViewById(R.id.progressBar);
        lltWallet = view.findViewById(R.id.lltWallet);
        tvWalletAmount = view.findViewById(R.id.tvWalletAmount);
        tvTerms = view.findViewById(R.id.tvTerms);
        tvPrivacy = view.findViewById(R.id.tvPrivacy);
        rltProfile = view.findViewById(R.id.rltProfile);
        rltProfile.setEnabled(false);

        dialog = commonMethods.getAlertDialog(mActivity);
        tvWalletAmount.setText(sessionManager.getCurrencySymbol() + sessionManager.getwalletAmount());

    }

    /**
     * Call API to get Details
     */
    private void getUserProfile() {
        commonMethods.showProgressDialog(mActivity, customDialog);
        apiService.getProfile(sessionManager.getToken()).enqueue(new RequestCallback(this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(mActivity, dialog, data);
            return;
        }
        if (jsonResp.isSuccess()) {
            onSuccessGetProfile(jsonResp);
            commonMethods.hideProgressDialog();
        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
        }
    }

    /**
     * To Fetch Response for Successful api call
     *
     * @param jsonResponse JsonResponse of successfull api execution
     */


    private void onSuccessGetProfile(JsonResponse jsonResponse) {
        userProfileModel = gson.fromJson(jsonResponse.getStrResponse(), UserProfileModel.class);
        if (userProfileModel != null) {
            profileView();
        }
    }

    /**
     * For updating profile page
     */

    private void profileView() {
        if (userProfileModel.getUserProfileList() != null) {
            user_name.setText(userProfileModel.getUserProfileList().getName());
            name = userProfileModel.getUserProfileList().getName();
            email = userProfileModel.getUserProfileList().getEmail();
            mobile = userProfileModel.getUserProfileList().getMobileNumber();
            countrycode = userProfileModel.getUserProfileList().getCountryCode();
            profileimage = userProfileModel.getUserProfileList().getProfileimage();
            sessionManager.setWalletAmount(userProfileModel.getUserProfileList().getWalletAmount());
            tvWalletAmount.setText(sessionManager.getCurrencySymbol() + sessionManager.getwalletAmount());

            //if(this.isAdded()&&!this.isDetached())
            Glide.with(mActivity).load(profileimage).listener(new RequestListener<String, GlideDrawable>() {
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
            }).into(user_image);
            if (user_name.getText().toString().equals("") || user_name.getText().toString().isEmpty()) {
                rltProfile.setEnabled(false);
            } else {
                rltProfile.setEnabled(true);
            }

        }
    }
}
