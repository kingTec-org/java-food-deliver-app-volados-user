package gofereats.adapters.main;

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


public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private ArrayList<CategoryListModel> categoryListModels;
    private Context context;
    private OnItemClickListener itemClickListener;

    public SearchListAdapter(Context context, ArrayList<CategoryListModel> categoryListModels) {
        this.context = context;
        this.categoryListModels = categoryListModels;
    }

    public void setOnCategoryClick(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, final int position) {


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


    public interface OnItemClickListener {
        void onItemClickListener(int id, String name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView category_image;
        private CustomTextView title;
        private CardView cvCategory;

        public ViewHolder(View itemView) {
            super(itemView);

            category_image = itemView.findViewById(R.id.category_image);
            title = itemView.findViewById(R.id.title);
            cvCategory = itemView.findViewById(R.id.cvCategory);


        }
    }
}
