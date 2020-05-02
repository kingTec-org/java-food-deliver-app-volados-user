package gofereats.adapters.main;

/**
 *
 * @package com.gofereats
 * @subpackage adapters.main
 * @category FoodListAdapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/*************************************************************
 Food List for the Restaurant
 ************************************************************/

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.restaurantdetails.FoodListModel;
import gofereats.datamodels.restaurantdetails.RestaurantMenuItemModel;
import gofereats.views.main.subviews.AddToCartActivity;


public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_item = 0;
    public final int TYPE_category = 1;
    public @Inject
    SessionManager sessionManager;
    public Context context;
    public ArrayList<FoodListModel> foodListModels;
    public ArrayList<RestaurantMenuItemModel> restaurantMenuItemModels;
    public RecyclerView rvFoodList;
    /*  private int visibleThreshold = 4;
      private int firstVisibleItem, totalItemCount;
      private boolean loading;
      private OnScrollCategoryListener onScrollCategoryListener;*/
    private int status = 1;

    /**
     * FoodItemRatingAdapter Constructor
     *
     * @param foodListModels           FoodListModel Array list
     * @param restaurantMenuItemModels RestaurantMenuItemModel Array list
     * @param context                  context of an Activity
     */

    public FoodListAdapter(Context context, RecyclerView rvFoodList, ArrayList<FoodListModel> foodListModels, final ArrayList<RestaurantMenuItemModel> restaurantMenuItemModels, int status) {
        this.context = context;
        this.foodListModels = foodListModels;
        this.restaurantMenuItemModels = restaurantMenuItemModels;
        this.status = status;
        this.rvFoodList = rvFoodList;
        AppController.getAppComponent().inject(this);


       /* if (rvFoodList.getLayoutManager() instanceof LinearLayoutManager) {



            *//*rvFoodList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            firstVisibleItem = linearLayoutManager
                                    .findFirstVisibleItemPosition();

                            System.out.println("Total Item Count "+totalItemCount);
                            System.out.println("First visible item "+firstVisibleItem);
                            System.out.println("Last visible item "+linearLayoutManager
                                    .findLastVisibleItemPosition());
                            System.out.println("Food Id "+restaurantMenuItemModels.get(firstVisibleItem).getFoodId());

                            System.out.println("loading "+loading);
                            if (totalItemCount <= (firstVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onScrollCategoryListener != null) {
                                    onScrollCategoryListener.onScrollCategory();
                                }
                                //loading = true;
                            }else{
                                System.out.println("loading2 "+loading);
                            }
                        }
                    });*//*
        }*/
    }


    @NonNull
    @Override
    public FoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_details_layout, parent, false);
        return new ViewHolder(view);*/
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_item) {
            return new ViewHolder(inflater.inflate(R.layout.food_details_layout, parent, false));
        } else {
            return new CategoryViewHolder(inflater.inflate(R.layout.header_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_item) {
            ViewHolder holder1 = (ViewHolder) holder;
            holder1.food_name.setText(foodListModels.get(position).getFoodName());
            String foodamt = sessionManager.getCurrencySymbol() + foodListModels.get(position).getFoodPrice();
            String offeramt = sessionManager.getCurrencySymbol() + foodListModels.get(position).getOfferPrice();
            holder1.food_amount.setText(foodamt);
            if (foodListModels.get(position).getIsOffer() == 1) {
                strikeThroughText(holder1.food_amount);
                holder1.offer_amount.setText(offeramt);
                holder1.food_amount.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                holder1.offer_amount.setVisibility(View.GONE);
                holder1.food_amount.setTextColor(context.getResources().getColor(R.color.category_hint));
            }
            System.out.println("ImageList " + foodListModels.get(position).getFoodName());
            //Glide.with(context).load(foodListModels.get(position).getFoodImage()).into(holder1.food_image);

            Glide.with(context).load(foodListModels.get(position).getMenuImage().getSmallImage()).into(holder1.food_image);

            if (!TextUtils.isEmpty(foodListModels.get(position).getFoodDescription())) {
                holder1.food_description.setVisibility(View.VISIBLE);
                holder1.food_description.setText(foodListModels.get(position).getFoodDescription());
            } else {
                holder1.food_description.setVisibility(View.GONE);
            }
           /* if (foodListModels.get(position).getIsVeg() == 0) {
                holder1.veg_lay.setVisibility(View.GONE);
            } else {
                holder1.veg_lay.setVisibility(View.GONE);
            }*/
            if (foodListModels.get(position).getIsAvailable() == 1 && foodListModels.get(position).getStatus() == 1) {
                holder1.tvSoldOut.setVisibility(View.GONE);
            } else {
                holder1.tvSoldOut.setVisibility(View.VISIBLE);
                if (foodListModels.get(position).getMenuImage().getSmallImage() != null) {
                    holder1.ivFoodImageBlur.setVisibility(View.VISIBLE);
                } else {
                    holder1.ivFoodImageBlur.setVisibility(View.GONE);
                }
                holder1.food_name.setTextColor(context.getResources().getColor(R.color.category_hint));
                holder1.food_amount.setTextColor(context.getResources().getColor(R.color.category_hint));
                holder1.food_description.setTextColor(context.getResources().getColor(R.color.category_hint));
            }

            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FoodListModel foodListModel = foodListModels.get(position);


                    if (foodListModels.get(position).getIsAvailable() == 1 && foodListModels.get(position).getStatus() == 1) {
                        Intent addtocart = new Intent(context, AddToCartActivity.class);
                        addtocart.putExtra("foodList", foodListModel);
                        addtocart.putExtra("type", 0);
                        addtocart.putExtra("status", status);
                        context.startActivity(addtocart);
                       /* addtocart.putExtra("foodId",foodListModels.get(position).getFoodId());
                        addtocart.putExtra("foodName",foodListModels.get(position).getFoodName());
                        addtocart.putExtra("foodPrice",foodListModels.get(position).getFoodPrice());
                        addtocart.putExtra("foodImage",foodListModels.get(position).getFootImage());
                        addtocart.putExtra("foodIsVeg",foodListModels.get(position).getIsVeg());
                        addtocart.putExtra("foodIsAvailable",foodListModels.get(position).getIsAvailable());
                        addtocart.putExtra("foodDescription",foodListModels.get(position).getFoodDescription());
                        addtocart.putExtra("foodCount",0);*/
                    }
                }
            });
        } else {
            CategoryViewHolder holder1 = (CategoryViewHolder) holder;
            if (foodListModels.get(position).getMenuCategoryId() == 0) {
                holder1.category_title.setTextColor(context.getResources().getColor(R.color.apptheme));
            }
            holder1.category_title.setText(foodListModels.get(position).getMenuCategoryName());
        }
    }

    private void strikeThroughText(TextView price) {
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemViewType(int position) {
        if (foodListModels.get(position).getType().equals("Item")) {
            return TYPE_item;
        } else {
            return TYPE_category;
        }
    }

    @Override
    public int getItemCount() {
        return foodListModels.size();
    }


    /**
     * View Holder class for the recycler view android
     */


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView food_name;
        private CustomTextView food_description;
        private CustomTextView food_amount;
        private CustomTextView offer_amount;
        private CustomTextView tvSoldOut;
        private ImageView food_image;
        private ImageView ivFoodImageBlur;
        private RelativeLayout veg_lay;

        public ViewHolder(View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_name);
            food_description = itemView.findViewById(R.id.food_description);
            food_amount = itemView.findViewById(R.id.food_amount);
            offer_amount = itemView.findViewById(R.id.offer_amount);
            tvSoldOut = itemView.findViewById(R.id.tvSoldOut);
            food_image = itemView.findViewById(R.id.food_image);
            ivFoodImageBlur = itemView.findViewById(R.id.ivFoodImageBlur);
            veg_lay = itemView.findViewById(R.id.veg_lay);


        }
    }

    private class CategoryViewHolder extends FoodListAdapter.ViewHolder {
        private CustomTextView category_title;

        public CategoryViewHolder(View view) {
            super(view);
            category_title = view.findViewById(R.id.category_title);
        }
    }

}
