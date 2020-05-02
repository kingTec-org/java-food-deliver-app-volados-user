package gofereats.adapters.main;
/**
 * @package com.gofereats
 * @subpackage adapter.main
 * @category Adapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/* ************************************************************
                Promotions list adapter
    *********************************************************** */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.promo.PromoListModel;

public class PromoDetailAdapter extends RecyclerView.Adapter<PromoDetailAdapter.ViewHolder> {
    public @Inject
    SessionManager sessionManager;

    public Context context;
    public ArrayList<PromoListModel> promoListModelArrayList;

    /**
     * PromoDetailAdapter
     *
     * @param context                 context of that Activity it is used in
     * @param promoListModelArrayList promoListModelArrayList array list
     */


    public PromoDetailAdapter(Context context, ArrayList<PromoListModel> promoListModelArrayList) {
        AppController.getAppComponent().inject(this);
        this.context = context;
        this.promoListModelArrayList = promoListModelArrayList;
    }

    @Override
    public PromoDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_promotion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PromoDetailAdapter.ViewHolder holder, int position) {
        if (promoListModelArrayList.get(position).getType() == 0) {
            holder.tvPromoamount.setText(sessionManager.getCurrencySymbol() + promoListModelArrayList.get(position).getPrice() + " " + context.getResources().getString(R.string.off));
            holder.tvPromoamount.setTextColor(context.getResources().getColor(R.color.apptheme));
            if("0".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction)))
                holder.tvPromodesc.setText(context.getResources().getString(R.string.free_upto)+" "+ sessionManager.getCurrencySymbol() + promoListModelArrayList.get(position).getPrice() + " "+context.getResources().getString(R.string.on)+" "+context.getResources().getString(R.string.app_name));
            else
                holder.tvPromodesc.setText(context.getResources().getString(R.string.free_upto)+" " + promoListModelArrayList.get(position).getPrice()+ sessionManager.getCurrencySymbol() + " "+context.getResources().getString(R.string.on)+" "+context.getResources().getString(R.string.app_name));
            holder.tvExpirydate.setText(context.getResources().getString(R.string.expired_on)+" "+ promoListModelArrayList.get(position).getExpiredate());
        } else {
            holder.tvPromoamount.setText(promoListModelArrayList.get(position).getPercentage() + context.getResources().getString(R.string.off_per));
            holder.tvPromoamount.setTextColor(context.getResources().getColor(R.color.apptheme));
            if("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                holder.tvPromodesc.setText(context.getResources().getString(R.string.free_upto) + " %" + promoListModelArrayList.get(position).getPercentage() +" "+ context.getResources().getString(R.string.off) + " " + context.getResources().getString(R.string.app_name));
            }
            else {
                holder.tvPromodesc.setText(context.getResources().getString(R.string.free_upto) + " " + promoListModelArrayList.get(position).getPercentage() + context.getResources().getString(R.string.off_on) + " " + context.getResources().getString(R.string.app_name));
            }
            holder.tvExpirydate.setText(context.getResources().getString(R.string.expired_on)+" " + promoListModelArrayList.get(position).getExpiredate());
        }
    }

    @Override
    public int getItemCount() {
        return promoListModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CustomTextView tvPromoamount;
        public CustomTextView tvPromodesc;
        public CustomTextView tvExpirydate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPromoamount = itemView.findViewById(R.id.tvPromoamount);
            tvPromodesc = itemView.findViewById(R.id.tvPromodesc);
            tvExpirydate = itemView.findViewById(R.id.tvExpirydate);
        }
    }
}
