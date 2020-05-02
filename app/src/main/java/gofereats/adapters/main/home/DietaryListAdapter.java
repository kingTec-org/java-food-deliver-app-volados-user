package gofereats.adapters.main.home;

/**
 *
 * @package com.gofereats
 * @subpackage adapters.main
 * @category Search list adapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/*************************************************************
 Search for whole Category in Gofereats
 ************************************************************/

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.obs.CustomTextView;

import java.util.ArrayList;

import gofereats.R;
import gofereats.datamodels.restaurantdetails.DietaryListModel;


public class DietaryListAdapter extends RecyclerView.Adapter<DietaryListAdapter.ViewHolder> {

    private ArrayList<DietaryListModel> dietaryListModels;
    private Context context;
    private OnItemClickListener itemClickListener;

    public DietaryListAdapter(Context context) {
        this.context = context;
    }

    public DietaryListAdapter(Context context, ArrayList<DietaryListModel> dietaryListModels) {
        this.context = context;
        this.dietaryListModels = dietaryListModels;
    }

    public void setOnCategoryClick(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public DietaryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_dietary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DietaryListAdapter.ViewHolder holder, final int position) {


        holder.tvDietaryName.setText(dietaryListModels.get(position).getDietaryName());
        Glide.with(context).load(dietaryListModels.get(position).getDietaryImage()).into(holder.ivDietaryImage);

        if (dietaryListModels.get(position).isDietarySelected())
            holder.ivDietaryTick.setVisibility(View.VISIBLE);
        else holder.ivDietaryTick.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener(dietaryListModels.get(position).getDietaryId(), dietaryListModels.get(position).getDietaryName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dietaryListModels.size();
    }

    public void updateList(ArrayList<DietaryListModel> dietaryListModels) {
        this.dietaryListModels = dietaryListModels;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int id, String name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivDietaryImage, ivDietaryTick;
        private CustomTextView tvDietaryName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivDietaryImage = itemView.findViewById(R.id.ivDietaryImage);
            tvDietaryName = itemView.findViewById(R.id.tvDietaryName);
            ivDietaryTick = itemView.findViewById(R.id.ivDietaryTick);
        }
    }
}
