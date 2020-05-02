package gofereats.views.main.subviews;
/**
 * @package com.gofereats
 * @subpackage views.main
 * @category Favorites Activity
 * @author Trioangle Product Team
 * @version 1.0
 */


/* ************************************************************
                View our Fav list in the Screen
    *********************************************************** */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import gofereats.R;
import gofereats.adapters.main.FavouritesListAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.wishlist.WishListArrayModel;
import gofereats.datamodels.wishlist.WishlistModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;

public class FavouritesActivity extends AppCompatActivity implements ServiceListener {
    public @Inject
    SessionManager sessionManager;
    public ImageView arrow;
    public RecyclerView favourites_list;
    public RelativeLayout rltEmptylayout;

    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    ApiService apiService;
    public @Inject
    Gson gson;
    public RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        AppController.getAppComponent().inject(this);

        arrow = findViewById(R.id.arrow);
        commonMethods.rotateArrow(arrow,this);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        favourites_list = findViewById(R.id.favourites_list);
        rltEmptylayout = findViewById(R.id.rltEmptylayout);

        favouritesApi();

        favourites_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        favourites_list.setLayoutManager(layoutManager);


    }


    /**
     * Api Calling method for favourites
     */
    private void favouritesApi() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.wishlist(sessionManager.getToken()).enqueue(new RequestCallback(this));

    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (jsonResp.isSuccess()) {
            commonMethods.hideProgressDialog();
            onGetFavourites(jsonResp);

        } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            System.out.println("Json Response check " + jsonResp.getRequestData());
            commonMethods.hideProgressDialog();
        }


    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        System.out.println("Json Response check " + jsonResp.getRequestData());
        commonMethods.hideProgressDialog();

    }

    private void onGetFavourites(JsonResponse jsonResponse) {
        WishlistModel wishlistmodel = gson.fromJson(jsonResponse.getStrResponse(), WishlistModel.class);
        if (wishlistmodel.getWishlist() != null && wishlistmodel.getWishlist().size() > 0) {
            rltEmptylayout.setVisibility(View.GONE);
            favourites_list.setVisibility(View.VISIBLE);
            List<WishListArrayModel> wishListArrayModels = wishlistmodel.getWishlist();
            adapter = new FavouritesListAdapter(wishListArrayModels, this, rltEmptylayout);
            favourites_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            favourites_list.setVisibility(View.GONE);
            rltEmptylayout.setVisibility(View.VISIBLE);
        }
    }
}
