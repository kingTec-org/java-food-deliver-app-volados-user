package gofereats.adapters.main;
/**
 * @package com.gofereats
 * @subpackage adapters.main
 * @category Favorites Activity
 * @author Trioangle Product Team
 * @version 0.6
 */


/* ************************************************************
                Fav list in the Screen
    *********************************************************** */

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.util.List;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.wishlist.WishListArrayModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.subviews.RestaurantDetailActivity;

public class FavouritesListAdapter extends RecyclerView.Adapter<FavouritesListAdapter.ViewHolder> implements ServiceListener {


    public @Inject
    ImageUtils imageUtils;

    public @Inject
    CommonMethods commonMethods;
    public @Inject
    ApiService apiService;
    public List<WishListArrayModel> wishListArrayModels;
    public @Inject
    SessionManager sessionManager;

    public @Inject
    Gson gson;

    public RelativeLayout rltEmptylayout;

    public @Inject
    CustomDialog customDialog;
    public Context context;

    /**
     * FavouritesListAdapter Constructor
     *
     * @param wishListArrayModels wishListArrayModels Array list
     * @param context             context of an Activity
     */


    public FavouritesListAdapter(List<WishListArrayModel> wishListArrayModels, Context context, RelativeLayout rltEmptylayout) {
        AppController.getAppComponent().inject(this);
        this.wishListArrayModels = wishListArrayModels;
        this.context = context;
        this.rltEmptylayout = rltEmptylayout;
        System.out.println("WishListAdapter checking ");
    }

    @Override
    public FavouritesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favourites_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouritesListAdapter.ViewHolder holder, final int position) {

        //System.out.println("getting list count "+getItemCount());
        String category = wishListArrayModels.get(position).getCategory();

        category = category.replaceAll(",", " • ");
        if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
            holder.prepartion_time.setText(wishListArrayModels.get(position).getMaxTime() + "-" + wishListArrayModels.get(position).getMinTime() + " " + context.getResources().getString(R.string.mins));
        }else {
            holder.prepartion_time.setText(wishListArrayModels.get(position).getMinTime() + "-" + wishListArrayModels.get(position).getMaxTime() + " " + context.getResources().getString(R.string.mins));
        }
        holder.Rest_name.setText(wishListArrayModels.get(position).getName());

        if (wishListArrayModels.get(position).getStatus() == 0) {

            holder.ivFoodImageBlur.setVisibility(View.VISIBLE);
            holder.Rest_name.setTextColor(context.getResources().getColor(R.color.dim_text_color));
            holder.prepartion_time.setTextColor(context.getResources().getColor(R.color.dim_text_color));
            holder.foodType.setTextColor(context.getResources().getColor(R.color.dim_text_color));
            holder.price_rating.setTextColor(context.getResources().getColor(R.color.dim_text_color));
            //holder.favourites_list_card.setForeground(context.getResources().getDrawable(R.drawable.background_dim));
        } else {
            holder.ivFoodImageBlur.setVisibility(View.GONE);
            holder.Rest_name.setTextColor(context.getResources().getColor(R.color.black));
            holder.prepartion_time.setTextColor(context.getResources().getColor(R.color.black));
            holder.foodType.setTextColor(context.getResources().getColor(R.color.category_hint));
            holder.price_rating.setTextColor(context.getResources().getColor(R.color.category_hint));
            // holder.favourites_list_card.setForeground(context.getResources().getDrawable(R.drawable.background_dark_dim));

        }


       /* holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                deleteWishListApi(position);

            }
        });*/


        holder.delete_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                deleteWishListApi(position);

            }
        });

        holder.deatilS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager.setRestaurantId(wishListArrayModels.get(position).getRestaurantId());
                Intent detail = new Intent(context, RestaurantDetailActivity.class);
                context.startActivity(detail);
            }
        });
        if (wishListArrayModels.get(position).getPriceRating() == 1) {
            holder.foodType.setText(sessionManager.getCurrencySymbol() + " • " + category);

            holder.price_rating.setText(sessionManager.getCurrencySymbol());
        } else if (wishListArrayModels.get(position).getPriceRating() == 2) {
            holder.foodType.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + " • " + category);

            holder.price_rating.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol());
        } else if (wishListArrayModels.get(position).getPriceRating() == 3) {
            holder.foodType.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + " • " + category);

            holder.price_rating.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol());
        } else if (wishListArrayModels.get(position).getPriceRating() == 4) {
            holder.foodType.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + " • " + category);

            holder.price_rating.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol());
        }

        imageUtils.loadImage(context, holder.food_image, wishListArrayModels.get(position).getBannerImageList().getSmallest());

    }

    /**
     * delete wishlist api Call
     *
     * @param position position of the list to be deleted
     */

    private void deleteWishListApi(int position) {
        commonMethods.showProgressDialog((AppCompatActivity) context, customDialog);

        apiService.wishList(wishListArrayModels.get(position).getRestaurantId(), sessionManager.getToken()).enqueue(new RequestCallback(FavouritesListAdapter.this));
        wishListArrayModels.remove(position);

        System.out.println("wishListArrayModels size check " + wishListArrayModels.size());
        if (wishListArrayModels.size() > 0) {
            rltEmptylayout.setVisibility(View.GONE);

        } else {
            rltEmptylayout.setVisibility(View.VISIBLE);
        }


        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return wishListArrayModels.size();
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {

        if (jsonResp.isSuccess()) {
            commonMethods.hideProgressDialog();
            // deleteSuccess(jsonResp);
        } else {
            commonMethods.hideProgressDialog();

        }
    }

    /*private void deleteSuccess(JsonResponse jsonResp) {


    }*/

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();

    }

    /**
     * Adapter View Holder Class
     */


    public class ViewHolder extends RecyclerView.ViewHolder {

        public CustomTextView Rest_name;
        public CustomTextView foodType;
        public CustomTextView prepartion_time;
        public CustomTextView price_rating;
        public ImageView food_image;
        public ImageView ivFoodImageBlur;
        public ImageView delete;
        public RelativeLayout delete_lay;
        public RelativeLayout deatilS;

        public ViewHolder(View itemView) {
            super(itemView);
            Rest_name = itemView.findViewById(R.id.Rest_name);
            foodType = itemView.findViewById(R.id.foodType);
            prepartion_time = itemView.findViewById(R.id.prepartion_time);
            price_rating = itemView.findViewById(R.id.price_rating);
            food_image = itemView.findViewById(R.id.food_image);
            ivFoodImageBlur = itemView.findViewById(R.id.ivFoodImageBlur);
            delete = itemView.findViewById(R.id.delete);
            deatilS = itemView.findViewById(R.id.deatilS);
            delete_lay = itemView.findViewById(R.id.delete_lay);

        }
    }
}