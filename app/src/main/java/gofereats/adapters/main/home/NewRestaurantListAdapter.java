package gofereats.adapters.main.home;
/**
 * @package com.gofereats
 * @subpackage adapters.main
 * @category NewListAdapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/* ************************************************************
                New to GoferEats list
    *********************************************************** */

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.main.home.RestaurantListModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.subviews.RestaurantDetailActivity;


public class NewRestaurantListAdapter extends RecyclerView.Adapter<SeeMoreListAdapter.Viewholder> implements ServiceListener {

    public ArrayList<RestaurantListModel> RestaurantListModels;
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
    private AlertDialog dialog;

    private OnItemClickListener ClickListener;

    public NewRestaurantListAdapter(Context context, ArrayList<RestaurantListModel> RestaurantListModels) {
        this.RestaurantListModels = RestaurantListModels;
        this.context = context;
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(context);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.ClickListener = mItemClickListener;
    }

    @Override
    public SeeMoreListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (getItemCount() == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_restaurant_list, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_layout, parent, false);
        }
        return new SeeMoreListAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SeeMoreListAdapter.Viewholder holder, final int position) {

        if (RestaurantListModels.size() == 7 && position == RestaurantListModels.size() - 1) {
            System.out.println("RestaurantListModels.size()" + RestaurantListModels.size());
            holder.rest_name_location.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.category.setVisibility(View.GONE);
            holder.ivWishList.setVisibility(View.GONE);
            holder.rltRating.setVisibility(View.GONE);
            holder.rltSeeMore.setVisibility(View.VISIBLE);
            //imageUtils.loadImage(context, holder.rest_image, RestaurantListModels.get(position).getBanner());
            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("RestaurantListModels.sizeposition" + position);
                    if (ClickListener != null) {
                        ClickListener.onItemClickListener(RestaurantListModels.get(position).getRestaurantId(), position);
                    }
                }
            });
        } else {
            holder.rest_name_location.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            holder.category.setVisibility(View.VISIBLE);
            holder.ivWishList.setVisibility(View.VISIBLE);
            holder.rltRating.setVisibility(View.VISIBLE);
            holder.rltSeeMore.setVisibility(View.GONE);
            holder.rltOfferlay.setVisibility(View.GONE);
            holder.rest_name_location.setText(RestaurantListModels.get(position).getRestaurantName());
            // holder.time.setText(RestaurantListModels.get(position).getMinTime()+"-"+RestaurantListModels.get(position).getMaxTime()+" "+"Mins");
            String category = RestaurantListModels.get(position).getCategory();
            if (RestaurantListModels.get(position).getRestaurantOffer().size() > 0 && RestaurantListModels.get(position).getRestaurantOffer().get(0).getPercentage() > 0) {
                holder.rltOfferlay.setVisibility(View.VISIBLE);
                holder.tvOffertitle.setText(RestaurantListModels.get(position).getRestaurantOffer().get(0).getTitle());
                holder.tvOfferdesc.setText(RestaurantListModels.get(position).getRestaurantOffer().get(0).getDescription());
                String per = String.valueOf(RestaurantListModels.get(position).getRestaurantOffer().get(0).getPercentage()) + "% OFF";
                holder.tvOfferpercent.setText(per);
            }
            category = category.replaceAll(",", " • ");
            if (RestaurantListModels.get(position).getPriceRating() == 1) {
                holder.category.setText(sessionManager.getCurrencySymbol() + " • " + category);
            } else if (RestaurantListModels.get(position).getPriceRating() == 2) {
                holder.category.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + " • " + category);
            } else if (RestaurantListModels.get(position).getPriceRating() == 3) {
                holder.category.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + " • " + category);
            } else if (RestaurantListModels.get(position).getPriceRating() == 4) {
                holder.category.setText(sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + " • " + category);
            } else {
                holder.category.setText(category);
            }
            //holder.category.setText(category+" • "+RestaurantListModels.get(position).getPriceRating());
            if (RestaurantListModels.get(position).getRestaurantRating() > 0) {
                if (RestaurantListModels.get(position).getRestaurantRating() >= 4.5) {
                    holder.star.setImageDrawable(context.getResources().getDrawable(R.drawable.star_two_gold));
                } else {
                    holder.star.setImageDrawable(context.getResources().getDrawable(R.drawable.star_two));
                }
                holder.rltRating.setVisibility(View.VISIBLE);
                holder.tvRating.setText(String.valueOf(RestaurantListModels.get(position).getRestaurantRating()));
                holder.tvOverallRating.setText("(" + String.valueOf(RestaurantListModels.get(position).getAverageRating()) + ")");
            } else {
                holder.rltRating.setVisibility(View.INVISIBLE);
            }
            //holder.tvRating.setText(String.valueOf(RestaurantListModels.get(position).getRestaurantRating()) + " " + "(" + String.valueOf(RestaurantListModels.get(position).getAverageRating()) + ")");
            //holder.tvOverallRating.setText("(" + String.valueOf(RestaurantListModels.get(position).getAverageRating()) + ")");

            // imageUtils.loadImage(context, holder.rest_image, RestaurantListModels.get(position).getBanner());

            if (RestaurantListModels.get(position).getRestaurantClosed() == 1) {

                if (RestaurantListModels.get(position).getStatus() == 1) {
                    holder.rltUnavailble.setVisibility(View.GONE);
                    if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                        holder.time.setText(RestaurantListModels.get(position).getMaxTime() + "-" + RestaurantListModels.get(position).getMinTime() + " " + context.getResources().getString(R.string.mins_s));
                    }else {
                        holder.time.setText(RestaurantListModels.get(position).getMinTime() + "-" + RestaurantListModels.get(position).getMaxTime() + " " + context.getResources().getString(R.string.mins_s));
                    }
                } else {
                    holder.rltUnavailble.setVisibility(View.VISIBLE);
                    holder.tvCurrently_Unavaiable.setText(context.getResources().getString(R.string.currently_unavailable));
                    holder.time.setText(context.getResources().getString(R.string.currently_unavailable));
                }
            } else {
                holder.rltUnavailble.setVisibility(View.VISIBLE);
                holder.tvCurrently_Unavaiable.setText(context.getString(R.string.closed));
                //holder.time.setText(context.getResources().getString(R.string.closed));
                holder.time.setText(RestaurantListModels.get(position).getRestaurantNextTime());
            }
            if (RestaurantListModels.get(position).getWished() == 1) {
                holder.ivWishList.setImageDrawable(context.getResources().getDrawable(R.drawable.like_white_fill));
            } else {
                holder.ivWishList.setImageDrawable(context.getResources().getDrawable(R.drawable.like_white));
            }

            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sessionManager.setRestaurantId(RestaurantListModels.get(position).getRestaurantId());
                    Intent detail = new Intent(context, RestaurantDetailActivity.class);
                    context.startActivity(detail);
                }
            });
            holder.ivWishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getRestaurantList(RestaurantListModels.get(position).getRestaurantId());
                    if (RestaurantListModels.get(position).getWished() == 1) {
                        RestaurantListModels.get(position).setWished(0);
                        holder.ivWishList.setImageDrawable(context.getResources().getDrawable(R.drawable.like_white));
                    } else {
                        RestaurantListModels.get(position).setWished(1);
                        holder.ivWishList.setImageDrawable(context.getResources().getDrawable(R.drawable.like_white_fill));
                    }
                }
            });
            if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))){
                holder.category.setTextDirection(View.TEXT_DIRECTION_RTL);
                holder.tvOffertitle.setTextDirection(View.TEXT_DIRECTION_RTL);
                holder.tvOfferdesc.setTextDirection(View.TEXT_DIRECTION_RTL);
                holder.tvOfferpercent.setTextDirection(View.TEXT_DIRECTION_RTL);
                //holder.time.setTextDirection(View.TEXT_DIRECTION_RTL);
            }
        }
        if (RestaurantListModels.size() > 1) {
            imageUtils.loadImage(context, holder.rest_image, RestaurantListModels.get(position).getBannerList().getMediumX());
        } else {
            imageUtils.loadImage(context, holder.rest_image, RestaurantListModels.get(position).getBannerList().getMediumImage());
        }
    }

    @Override
    public int getItemCount() {
        return RestaurantListModels.size();
    }

    /**
     * Call API to get restaurant list
     */
    private void getRestaurantList(int restaurantId) {
        apiService.wishList(restaurantId, sessionManager.getToken()).enqueue(new RequestCallback(this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        System.out.println(jsonResp.isSuccess());
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(context, dialog, jsonResp.getStatusMsg());
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int id, int positionz);
    }

    /**
     * view Holder class of an Recycler view
     */


    /*public class Viewholder extends RecyclerView.ViewHolder {
        public ImageView rest_image;
        public ImageView ivWishList;
        public CustomTextView rest_name_location;
        public CustomTextView time;
        public CustomTextView category;
        public CustomTextView tvRating;
        public RelativeLayout details;
        public RelativeLayout rltRating;
        public RelativeLayout rltUnavailble;
        public RelativeLayout rltSeeMore;
        public ImageView ivSeemore;
        public CustomTextView tvseemore;
        public CustomTextView tvOverallRating;

        public Viewholder(View itemView) {
            super(itemView);
            rest_image = (ImageView) itemView.findViewById(R.id.rest_image);
            ivWishList = (ImageView) itemView.findViewById(R.id.ivWishList);
            category = (CustomTextView) itemView.findViewById(R.id.category);
            tvRating = (CustomTextView) itemView.findViewById(R.id.tvRating);
            rest_name_location = (CustomTextView) itemView.findViewById(R.id.rest_name_location);
            time = (CustomTextView) itemView.findViewById(R.id.time);

            details = (RelativeLayout) itemView.findViewById(R.id.details);
            rltRating = (RelativeLayout) itemView.findViewById(R.id.rltRating);
            ivSeemore = (ImageView) itemView.findViewById(R.id.ivSeemore);
            tvseemore = (CustomTextView) itemView.findViewById(R.id.tvseemore);
            rltUnavailble = (RelativeLayout) itemView.findViewById(R.id.rltUnavailble);
            rltSeeMore = (RelativeLayout) itemView.findViewById(R.id.rltSeeMore);
            tvOverallRating=(CustomTextView)itemView.findViewById(R.id.tvOverallRating);
        }
    }*/
}

