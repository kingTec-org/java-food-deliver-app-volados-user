package gofereats.adapters.main;
/**
 *
 * @package com.gofereats
 * @subpackage adapters.main
 * @category adapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/*************************************************************
 Driver FeedBack Adapter
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
import gofereats.datamodels.ratings.issueslist.DriverIssueListModel;

/**
 * Created by trioangle on 6/20/18.
 */

public class DriverFeedBacKAdapter extends RecyclerView.Adapter<DriverFeedBacKAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DriverIssueListModel> driverIssueList = new ArrayList<>();


    private OnItemClickListener ClickListener;


    public DriverFeedBacKAdapter(Context context, ArrayList<DriverIssueListModel> driverIssueList) {
        this.context = context;
        this.driverIssueList = driverIssueList;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.ClickListener = mItemClickListener;
    }

    @NonNull
    @Override

    public DriverFeedBacKAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DriverFeedBacKAdapter.ViewHolder holder, final int position) {
        holder.tvFeedback.setText(driverIssueList.get(position).getIssue());
        driverIssueList.get(position).setSelected(true);

        holder.rltFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (driverIssueList.get(position).isSelected()) {
                    holder.rltFeedback.setBackground(context.getResources().getDrawable(R.drawable.oval_border_black));
                    driverIssueList.get(position).setSelected(false);
                    if (ClickListener != null) {
                        ClickListener.onItemClickListener(driverIssueList.get(position).getId(), position);
                    }
                } else {
                    holder.rltFeedback.setBackground(context.getResources().getDrawable(R.drawable.oval_border_grey));
                    driverIssueList.get(position).setSelected(true);
                    if (ClickListener != null) {
                        ClickListener.onItemClickListener(driverIssueList.get(position).getId(), position);
                    }
                }
            }
        });

    }

    //
    @Override
    public int getItemCount() {
        return driverIssueList.size();
    }

    /**
     * Interface for on Item click listner
     */
    public interface OnItemClickListener {
        void onItemClickListener(int id, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rltFeedback;
        private CustomTextView tvFeedback;

        public ViewHolder(View itemView) {
            super(itemView);

            rltFeedback = itemView.findViewById(R.id.rltFeedback);
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
        }
    }
}
