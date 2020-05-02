package gofereats.adapters.main;
/**
 * @package com.gofereats
 * @subpackage adapters.main
 * @category Adapter
 * @author Trioangle Product Team
 * @version 0.6
 */


/* ************************************************************
                Location list Adapter class
    *********************************************************** */

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.obs.CustomTextView;

import java.util.ArrayList;

import gofereats.R;
import gofereats.datamodels.location.LocationList;

/**
 * Created by trioangle on 5/23/18.
 */

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LocationList> locationLists;
    private int clickedPos = -1;

    private OnItemClickListener mItemClickListener;

    public LocationListAdapter(Context context, ArrayList<LocationList> locationLists) {
        this.context = context;
        this.locationLists = locationLists;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public LocationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (locationLists.get(position).getType() == 0) {
            holder.card_loction_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.location_home));
            System.out.println("check Home "+context.getResources().getString(R.string.home));
            holder.main_address.setText(context.getResources().getString(R.string.home));
            holder.sub_address.setText(locationLists.get(position).getFirstAddress());
        } else if (locationLists.get(position).getType() == 1) {
            holder.card_loction_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.location_work));
            holder.main_address.setText(context.getResources().getString(R.string.work));
            holder.sub_address.setText(locationLists.get(position).getFirstAddress());
        } else if (locationLists.get(position).getType() == 2) {
            holder.main_address.setText(locationLists.get(position).getFirstAddress());
            holder.sub_address.setText(locationLists.get(position).getSecondAddress());
            holder.card_loction_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.location_recent));
        }


        if (locationLists.get(position).getDefaultaddress() == 1) {
            clickedPos = position;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (locationLists.get(position).getDefaultaddress()==1) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClickListener(position, locationLists);
                }
                // }
                if (clickedPos >= 0) locationLists.get(clickedPos).setDefaultaddress(0);
                clickedPos = position;
                locationLists.get(clickedPos).setDefaultaddress(1);
                notifyDataSetChanged();
            }
        });


        if (clickedPos == position) {
            locationLists.get(position).setDefaultaddress(1);
            holder.pickup_mode.setVisibility(View.VISIBLE);
            holder.delivery_note.setVisibility(View.VISIBLE);
            holder.edit_delivery_note.setVisibility(View.VISIBLE);
            holder.check.setVisibility(View.VISIBLE);
        } else {
            locationLists.get(position).setDefaultaddress(0);
            holder.pickup_mode.setVisibility(View.GONE);
            holder.delivery_note.setVisibility(View.GONE);
            holder.edit_delivery_note.setVisibility(View.GONE);
            holder.check.setVisibility(View.GONE);
        }

        if (locationLists.get(position).getDeliveryoptions() == 0) {
            holder.pickup_mode.setText(context.getResources().getString(R.string.meet_at_vehicle));
        } else {
            if (locationLists.get(position).getApartment() != null) {
                holder.pickup_mode.setText(context.getResources().getString(R.string.deliver_to_door) + " " + locationLists.get(position).getApartment());
            } else {
                holder.pickup_mode.setText(context.getResources().getString(R.string.deliver_to_door));
            }
        }

        if (locationLists.get(position).getDeliverynote() == null || locationLists.get(position).getDeliverynote().equals("")) {
            holder.delivery_note.setVisibility(View.GONE);
        } else {
            holder.delivery_note.setVisibility(View.VISIBLE);
            holder.delivery_note.setText(locationLists.get(position).getDeliverynote());
        }

    }

    @Override
    public int getItemCount() {
        return locationLists.size();
    }

    /**
     * item click listener interface
     */
    public interface OnItemClickListener {
        void onItemClickListener(int position, ArrayList<LocationList> locationLists);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView card_loction_icon;
        private ImageView check;
        private CustomTextView main_address;
        private CustomTextView sub_address;
        private CustomTextView pickup_mode;
        private CustomTextView delivery_note;
        private CustomTextView edit_delivery_note;
        private CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);

            card_loction_icon = itemView.findViewById(R.id.card_loction_icon);
            check = itemView.findViewById(R.id.check);
            main_address = itemView.findViewById(R.id.main_address);
            sub_address = itemView.findViewById(R.id.sub_address);
            pickup_mode = itemView.findViewById(R.id.pickup_mode);
            delivery_note = itemView.findViewById(R.id.delivery_note);
            edit_delivery_note = itemView.findViewById(R.id.edit_delivery_note);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }
}
