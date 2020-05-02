package gofereats.adapters.main.home;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.obs.CustomTextView;

import java.util.ArrayList;

import gofereats.R;
import gofereats.datamodels.searchcategory.CategoryListModel;

/**
 * @package com.trioangle.gofereats
 * @subpackage adapters.main.home
 * @category MoreRestaurantListAdapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/*************************************************************
 More Search list
 *********************************************************** */

public class MoreSearchListAdapter extends RecyclerView.Adapter<MoreSearchListAdapter.ViewHolder> {

    public ArrayList<CategoryListModel> categoryListModels;
    private Context context;
    private OnItemClickListener itemClickListener;

    public MoreSearchListAdapter(Context context, ArrayList<CategoryListModel> categoryListModels) {
        this.context = context;
        this.categoryListModels = categoryListModels;
    }

    public void setOnCategoryClick(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MoreSearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreSearchListAdapter.ViewHolder holder, final int position) {
        holder.title.setText(categoryListModels.get(position).getName());
        Glide.with(context).load(categoryListModels.get(position).getCategoryimage()).into(holder.category_image);

        holder.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener(categoryListModels.get(position).getId(), categoryListModels.get(position).getName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryListModels.size();
    }

    /**
     * Item click listner interface
     */
    public interface OnItemClickListener {
        void onItemClickListener(int id, String name);
    }

    /**
     * View Holder class of an Recycler view
     */


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView category_image;
        public CustomTextView title;
        public CardView cvCategory;

        public ViewHolder(View itemView) {
            super(itemView);

            category_image = itemView.findViewById(R.id.category_image);
            title = itemView.findViewById(R.id.title);
            cvCategory = itemView.findViewById(R.id.cvCategory);
        }
    }
}
