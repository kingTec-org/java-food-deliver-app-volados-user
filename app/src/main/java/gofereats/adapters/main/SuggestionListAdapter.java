package gofereats.adapters.main;


/**
 *
 * @package com.gofereats
 * @subpackage adapters.main
 * @category Suggesstion list adapter
 * @author Trioangle Product Team
 * @version 0.6
 */


/*************************************************************
 suggest items to user to order
 ************************************************************/

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.obs.CustomTextView;

import java.util.ArrayList;

import gofereats.R;
import gofereats.datamodels.main.home.RestaurantModel;


public class SuggestionListAdapter extends RecyclerView.Adapter<SuggestionListAdapter.Viewholder> {

    private ArrayList<RestaurantModel> RestaurantModels;

   /* public SuggestionListAdapter(ArrayList<RestaurantModel> RestaurantModels, Context context) {
        this.RestaurantModels = RestaurantModels;
    }*/

    @NonNull
    @Override
    public SuggestionListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_cart_suggestion, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionListAdapter.Viewholder holder, int position) {

        /*holder.suggest_price.setText(RestaurantModels.get(position).getPopular());
        holder.suggest_rest_name.setText(RestaurantModels.get(position).getPopular_name());
        holder.other_suggest_name.setText(RestaurantModels.get(position).getPopular_min());*/
        // Glide.with(context).load(R.drawable.testfood).into(holder.rest_image);
    }

    @Override
    public int getItemCount() {
        return RestaurantModels.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private CustomTextView suggest_price;
        private ImageView rest_image;

        public Viewholder(View itemView) {
            super(itemView);

            suggest_price = itemView.findViewById(R.id.suggest_price);
            rest_image = itemView.findViewById(R.id.rest_image);
        }
    }
}
