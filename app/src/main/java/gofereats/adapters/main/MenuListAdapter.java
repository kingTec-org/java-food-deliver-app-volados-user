package gofereats.adapters.main;
/**
 *
 * @package com.gofereats
 * @subpackage adapters.main
 * @category NewListAdapter
 * @author Trioangle Product Team
 * @version 0.6
 */


/*************************************************************
 Menu to GoferEats list
 *********************************************************** */

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.restaurantdetails.RestaurantMenuModel;
import gofereats.interfaces.ApiService;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.views.customize.CustomDialog;


public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.Viewholder> {
    public ArrayList<RestaurantMenuModel> restaurantMenuModels;
    public Context context;

    public @Inject
    ImageUtils imageUtils;
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

    public int clickPosition = 0;

    /**
     * Menu List Adapter Constructor
     *
     * @param context              context of the activity used in
     * @param restaurantMenuModels restaurant menu model array List
     * @param clickPostion         clicked position
     */


    public MenuListAdapter(Context context, ArrayList<RestaurantMenuModel> restaurantMenuModels, int clickPostion) {
        this.restaurantMenuModels = restaurantMenuModels;
        this.context = context;
        this.clickPosition = clickPostion;
        AppController.getAppComponent().inject(this);
    }

    @Override
    public MenuListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuListAdapter.Viewholder holder, final int position) {
        holder.tvMenuName.setText(restaurantMenuModels.get(position).getMenuName());
        holder.tvMenuTime.setText(String.valueOf(restaurantMenuModels.get(position).getMenuTime()));

        if (position == clickPosition) holder.ivTick.setVisibility(View.VISIBLE);
        else holder.ivTick.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return restaurantMenuModels.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public ImageView ivTick;
        public CustomTextView tvMenuTime;
        public CustomTextView tvMenuName;

        public Viewholder(View itemView) {
            super(itemView);
            ivTick = itemView.findViewById(R.id.ivTick);
            tvMenuTime = itemView.findViewById(R.id.tvMenuTime);
            tvMenuName = itemView.findViewById(R.id.tvMenuName);
        }
    }
}
