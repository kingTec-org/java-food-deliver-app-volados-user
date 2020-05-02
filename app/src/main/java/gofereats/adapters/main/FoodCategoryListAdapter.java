package gofereats.adapters.main;

/**
 *
 * @package com.gofereats
 * @subpackage adapters.main
 * @category Foodcategorylist adapter
 * @author Trioangle Product Team
 * @version 0.6
 */


/*************************************************************
 Food categories in the Restaurant
 ************************************************************/

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.obs.CustomTextView;

import java.util.ArrayList;

import gofereats.R;
import gofereats.datamodels.restaurantdetails.RestaurantMenuCategoriesModel;


public class FoodCategoryListAdapter extends RecyclerView.Adapter<FoodCategoryListAdapter.ViewHolder> {
    public static int index = 0;
    private Context context;
    private ArrayList<RestaurantMenuCategoriesModel> category;

    /**
     * FoodCategoryListAdapter Constructor
     *
     * @param category category Array list
     * @param context  context of an Activity
     */


    public FoodCategoryListAdapter(Context context, ArrayList<RestaurantMenuCategoriesModel> category) {
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public FoodCategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_category_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodCategoryListAdapter.ViewHolder holder, final int position) {

        holder.category.setText(category.get(position).getMenuCategoryName());
       /* holder.rltMenuCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=position;
                notifyDataSetChanged();
            }
        });*/
        if (index == position) {
            holder.category.setTextColor(context.getResources().getColor(R.color.black));
            if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                android.view.ViewGroup.LayoutParams layoutParams = holder.vBottomLine.getLayoutParams();
                holder.category.measure(0, 0);
                layoutParams.width = holder.category.getMeasuredWidth()+40;
                holder.vBottomLine.setLayoutParams(layoutParams);

            }
            holder.vBottomLine.setVisibility(View.VISIBLE);
        } else {
            holder.category.setTextColor(context.getResources().getColor(R.color.payment_method));
            holder.vBottomLine.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    /**
     * Adapter View Holder Class
     */


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView category;
        public RelativeLayout rltMenuCategory;
        public View vBottomLine;

        //View category_line;
        public ViewHolder(View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.category);
            rltMenuCategory = itemView.findViewById(R.id.rltMenuCategory);
            vBottomLine = itemView.findViewById(R.id.vBottomLine);
        }
    }
}
