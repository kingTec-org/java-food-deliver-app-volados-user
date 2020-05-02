package gofereats.adapters.main;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obs.CustomTextView;

import java.util.List;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.order.MenuListModel;

/**
 * @author Trioangle Product Team
 * @version 1.0
 * @package com.gofereats
 * @subpackage adapter.main
 * @category Adapter
 */


/* ************************************************************
                Receipt Order  adapter of an upcoming order
    *********************************************************** */

public class ReceiptOrderAdapter extends RecyclerView.Adapter<ReceiptOrderAdapter.ViewHolder> {

    public @Inject
    SessionManager sessionManager;

    private List<MenuListModel> menuListModels;


    /**
     * ReceiptOrderAdapter Constructor
     *
     * @param menuListModels menuListModels list
     */


    public ReceiptOrderAdapter(List<MenuListModel> menuListModels) {
        AppController.getAppComponent().inject(this);
        this.menuListModels = menuListModels;
    }

    @NonNull
    @Override
    public ReceiptOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptOrderAdapter.ViewHolder holder, int position) {

        holder.item_count.setText(String.valueOf(menuListModels.get(position).getQuantity()));
        holder.item_name.setText(menuListModels.get(position).getMenuName());
        //holder.addtional_charge.setText(menuListModels.get(position).getQuantity());
        int count = menuListModels.get(position).getQuantity();
        double eachPrice = Double.valueOf(menuListModels.get(position).getPrice());
        holder.item_price.setText(sessionManager.getCurrencySymbol() + String.valueOf(eachPrice));
    }

    @Override
    public int getItemCount() {
        return menuListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView item_count;
        private CustomTextView item_name;
        private CustomTextView item_price;

        public ViewHolder(View itemView) {
            super(itemView);
            item_count = itemView.findViewById(R.id.item_count);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
        }
    }
}
