package gofereats.adapters.main.home;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.main.home.RestaurantListModel;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.OnLoadMoreListener;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.subviews.RestaurantDetailActivity;

/**
 * @author Trioangle Product Team
 * @version 1.0
 * @package com.gofereats
 * @subpackage adapters.main
 * @category NewListAdapter
 */


/* ************************************************************
                see more list Adapter
    *********************************************************** */

public class SeeMoreListAdapter extends RecyclerView.Adapter<SeeMoreListAdapter.Viewholder> implements ServiceListener {
    @NonNull

    public final int TYPE_Explore = 0;
    public final int TYPE_LOAD = 1;
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
    private ArrayList<RestaurantListModel> RestaurantListModels;
    private Context context;
    private AlertDialog dialog;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    /**
     * See More List Adapter constructor
     *
     * @param context              context of activity it is used in
     * @param RestaurantListModels RestaurantListModels Array List
     */


    public SeeMoreListAdapter(Context context, ArrayList<RestaurantListModel> RestaurantListModels, RecyclerView recyclerView) {
        this.RestaurantListModels = RestaurantListModels;
        this.context = context;
        System.out.println("Checking in  force stop crash ");
        AppController.getAppComponent().inject(this);
        dialog = commonMethods.getAlertDialog(context);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

    }

    @Override
    public SeeMoreListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_Explore) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_restaurant_list, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_load, parent, false);
        }
        return new Viewholder(view);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_restaurant_list, parent, false);
        //return  new SeeMoreListAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SeeMoreListAdapter.Viewholder holder, final int position) {
//        loadMoreListener.onLoadMore();
        /*if(position>=getItemCount()-1&& isMoreDataAvailable &&!isLoading && loadMoreListener!=null){
            loadMoreListener.onLoadMore();
            isMoreDataAvailable=false;
           // isLoading = true;
        }*/


        if (getItemViewType(position) == TYPE_Explore) {
            try {
                holder.rest_name_location.setText(RestaurantListModels.get(position).getRestaurantName());
                if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                    holder.time.setText(RestaurantListModels.get(position).getMaxTime() + "-" + RestaurantListModels.get(position).getMinTime() + " " + context.getResources().getString(R.string.mins));
                }else {
                    holder.time.setText(RestaurantListModels.get(position).getMinTime() + "-" + RestaurantListModels.get(position).getMaxTime() + " " + context.getResources().getString(R.string.mins));
                }
                String category = RestaurantListModels.get(position).getCategory();
                category = category.replaceAll(",", " • ");
                holder.rltOfferlay.setVisibility(View.GONE);
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
                System.out.println("Rating Filter " + RestaurantListModels.get(position).getRestaurantRating());
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


                if (RestaurantListModels.get(position).getRestaurantOffer().size() > 0 && RestaurantListModels.get(position).getRestaurantOffer().get(0).getPercentage() > 0) {
                    holder.rltOfferlay.setVisibility(View.VISIBLE);
                    holder.tvOffertitle.setText(RestaurantListModels.get(position).getRestaurantOffer().get(0).getTitle());
                    holder.tvOfferdesc.setText(RestaurantListModels.get(position).getRestaurantOffer().get(0).getDescription());
                    String per = String.valueOf(RestaurantListModels.get(position).getRestaurantOffer().get(0).getPercentage()) + "% OFF";
                    holder.tvOfferpercent.setText(per);
                }


                if (RestaurantListModels.get(position).getStatus() == 1) {
                    holder.rltUnavailble.setVisibility(View.GONE);
                    if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                        holder.time.setText(RestaurantListModels.get(position).getMaxTime() + "-" + RestaurantListModels.get(position).getMinTime() + " " + context.getResources().getString(R.string.mins));
                    }else {
                        holder.time.setText(RestaurantListModels.get(position).getMinTime() + "-" + RestaurantListModels.get(position).getMaxTime() + " " + context.getResources().getString(R.string.mins));
                    }
                } else {
                    holder.rltUnavailble.setVisibility(View.VISIBLE);
                    holder.time.setText(context.getString(R.string.currently_unavailable));
                }

                //imageUtils.loadImage(context, holder.rest_image, RestaurantListModels.get(position).getBanner());
                imageUtils.loadImage(context, holder.rest_image, RestaurantListModels.get(position).getBannerList().getSmallImage());
                if (RestaurantListModels.get(position).getRestaurantClosed() == 1) {

                    if (RestaurantListModels.get(position).getStatus() == 1) {
                        holder.rltUnavailble.setVisibility(View.GONE);
                        if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                            holder.time.setText(RestaurantListModels.get(position).getMaxTime() + "-" + RestaurantListModels.get(position).getMinTime() + " " + context.getResources().getString(R.string.mins));
                        }else {
                            holder.time.setText(RestaurantListModels.get(position).getMinTime() + "-" + RestaurantListModels.get(position).getMaxTime() + " " + context.getResources().getString(R.string.mins));
                        }

                    } else {
                        holder.rltUnavailble.setVisibility(View.VISIBLE);
                        holder.tvCurrently_Unavaiable.setText(context.getString(R.string.currently_unavailable));
                        holder.time.setText(context.getString(R.string.currently_unavailable));
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
                if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                    holder.tvOffertitle.setTextDirection(View.TEXT_DIRECTION_RTL);
                    holder.tvOfferdesc.setTextDirection(View.TEXT_DIRECTION_RTL);
                    holder.tvOfferpercent.setTextDirection(View.TEXT_DIRECTION_RTL);
                }
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

            } catch (Exception e) {
                Log.v("type", "adapter" + RestaurantListModels.get(position).getType());
                e.printStackTrace();
            }

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

    public OnLoadMoreListener getOnLoadMoreListener() {
        return this.onLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemViewType(int position) {
        if (RestaurantListModels.get(position).getType().equals("load")) {
            return TYPE_LOAD;
        } else {
            return TYPE_Explore;
        }
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged() {
        notifyDataSetChanged();
        //loading = false;
        //isMoreDataAvailable=true;
    }

    public void clear() {
        RestaurantListModels.clear();
        notifyDataSetChanged();
    }

   /* public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }*/

    /**
     * View holder class
     */


    public static class Viewholder extends RecyclerView.ViewHolder {
        public ImageView rest_image;
        public ImageView ivWishList;
        public CustomTextView rest_name_location;
        public CustomTextView time;
        public CustomTextView category;
        public CustomTextView tvRating;
        public CustomTextView tvOffertitle;
        public CustomTextView tvOfferdesc;
        public CustomTextView tvOfferpercent;
        public RelativeLayout details;
        public RelativeLayout rltRating;
        public RelativeLayout rltUnavailble;
        public CustomTextView tvOverallRating;
        public RelativeLayout rltSeeMore;
        public RelativeLayout rltOfferlay;
        public CustomTextView tvCurrently_Unavaiable;
        public ImageView star;

        public Viewholder(View itemView) {
            super(itemView);
            rest_image = itemView.findViewById(R.id.rest_image);
            ivWishList = itemView.findViewById(R.id.ivWishList);
            category = itemView.findViewById(R.id.category);
            tvRating = itemView.findViewById(R.id.tvRating);
            rest_name_location = itemView.findViewById(R.id.rest_name_location);
            time = itemView.findViewById(R.id.time);
            tvOffertitle = itemView.findViewById(R.id.tvOffertitle);
            tvOfferdesc = itemView.findViewById(R.id.tvOfferdesc);
            tvOfferpercent = itemView.findViewById(R.id.tvOfferpercent);

            details = itemView.findViewById(R.id.details);
            rltRating = itemView.findViewById(R.id.rltRating);
            rltUnavailble = itemView.findViewById(R.id.rltUnavailble);
            tvOverallRating = itemView.findViewById(R.id.tvOverallRating);
            rltSeeMore = itemView.findViewById(R.id.rltSeeMore);
            rltOfferlay = itemView.findViewById(R.id.rltOfferlay);
            tvCurrently_Unavaiable = itemView.findViewById(R.id.tvCurrently_Unavaiable);
            star = itemView.findViewById(R.id.star);
        }
    }


}
