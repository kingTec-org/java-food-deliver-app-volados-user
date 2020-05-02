package gofereats.adapters.main;
/**
 * @package com.gofereats
 * @subpackage adapters.main
 * @category Adapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/* ************************************************************
                Menu Feedback adapter class
    *********************************************************** */

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
import gofereats.datamodels.ratings.issueslist.FoodIssueModelList;


/**
 * Created by trioangle on 6/19/18.
 */

public class MenuFeedBackAdapter extends RecyclerView.Adapter<MenuFeedBackAdapter.ViewHolder> {

    public Context context;
    public ArrayList<FoodIssueModelList> FoodIssueList = new ArrayList<>();


    private OnItemClickListener mItemClickListener;

    /**
     * Constructor for MenuFeedBackAdapter
     *
     * @param context       context of an  activity attached
     * @param foodIssueList FoodIssueModelList Array list
     */


    public MenuFeedBackAdapter(Context context, ArrayList<FoodIssueModelList> foodIssueList) {
        this.context = context;
        FoodIssueList = foodIssueList;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public MenuFeedBackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuFeedBackAdapter.ViewHolder holder, final int position) {
        holder.tvFeedback.setText(FoodIssueList.get(position).getIssue());
        FoodIssueList.get(position).setSelected(true);

        holder.rltFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FoodIssueList.get(position).isSelected()) {
                    holder.rltFeedback.setBackground(context.getResources().getDrawable(R.drawable.oval_border_black));
                    FoodIssueList.get(position).setSelected(false);
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickListener(FoodIssueList.get(position).getId(), position);
                    }
                } else {
                    holder.rltFeedback.setBackground(context.getResources().getDrawable(R.drawable.oval_border_grey));
                    FoodIssueList.get(position).setSelected(true);
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClickListener(FoodIssueList.get(position).getId(), position);
                    }
                }
            }
        });

    }

    //
    @Override
    public int getItemCount() {
        return FoodIssueList.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int id, int positionz);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rltFeedback;
        public CustomTextView tvFeedback;

        public ViewHolder(View itemView) {
            super(itemView);

            rltFeedback = itemView.findViewById(R.id.rltFeedback);
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
        }
    }
}
