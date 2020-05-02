package gofereats.adapters.main;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.obs.CustomButton;
import com.obs.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.order.MenuListModel;
import gofereats.datamodels.order.OrderModel;
import gofereats.views.main.MainActivity;
import gofereats.views.main.subviews.CancellationActivity;
import gofereats.views.main.subviews.DriverTrackingActivity;
import gofereats.views.main.subviews.RestaurantDetailActivity;

/**
 * @package com.gofereats
 * @subpackage adapters.main
 * @category adapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/*************************************************************
 Upcoming Adapter of upcoming class
 ************************************************************/

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {

    public @Inject
    SessionManager sessionManager;

    private Context context;
    private List<OrderModel> UpcomingOrders;
    private ArrayList<MenuListModel> menuListModels;


    public UpcomingAdapter(Context context, List<OrderModel> UpcomingOrders) {
        AppController.getAppComponent().inject(this);
        this.context = context;
        this.UpcomingOrders = UpcomingOrders;

    }

    @NonNull
    @Override
    public UpcomingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_order_histroy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAdapter.ViewHolder holder, final int position) {
        final OrderModel orderModel = UpcomingOrders.get(position);


        Glide.with(context).load(UpcomingOrders.get(position).getRestaurantBanner().getOriginal()).into(holder.ivRestImag);

        holder.order_status.setText(UpcomingOrders.get(position).getUserstatustext());
        holder.tvRestName.setText(UpcomingOrders.get(position).getName());
        if("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
            holder.tvOrderId.setText(context.getResources().getString(R.string.orderid) + UpcomingOrders.get(position).getOrderid() + "#");
            holder.tvOrderDate.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.tvOrderDate.setText(UpcomingOrders.get(position).getDate());
            System.out.println("Upcoming order date"+UpcomingOrders.get(position).getDate());
        }
        else {
            holder.tvOrderId.setText(context.getResources().getString(R.string.orderid) + "#" + UpcomingOrders.get(position).getOrderid());
            holder.tvOrderDate.setText(UpcomingOrders.get(position).getDate());
        }
        holder.tvOrderArrival.setText(context.getResources().getString(R.string.estimate_arrival)+" "+ UpcomingOrders.get(position).getEstcompletetime());
        holder.tvTotal.setText(context.getResources().getString(R.string.total_dot));
        holder.tvTotalamount.setText(sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getTotalamount());

        if (UpcomingOrders.get(position).getOrderType() == 1 && (UpcomingOrders.get(position).getOrderStatus() == 1 || UpcomingOrders.get(position).getOrderStatus() == 3)) {
            holder.progress.setVisibility(View.GONE);
            holder.track.setVisibility(View.GONE);
            holder.tvOrderArrival.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.statusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.scheduled_calender));

        } else {
            holder.progress.setVisibility(View.VISIBLE);
            holder.track.setVisibility(View.VISIBLE);
            holder.tvOrderArrival.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.INVISIBLE);
            holder.progress.getProgressDrawable().setColorFilter(context.getResources().getColor(R.color.apptheme), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.statusIcon.setColorFilter(ContextCompat.getColor(context, R.color.apptheme), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.statusIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.green_dot));
            holder.statusIcon.setPadding(5, 5, 5, 5);
        }
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(context, CancellationActivity.class);
                cancel.putExtra("orderId", UpcomingOrders.get(position).getOrderid());
                context.startActivity(cancel);
            }
        });

        //holder.driver_layout.setVisibility(View.VISIBLE);
        /*holder.driver_name.setText(UpcomingOrders.get(position).getDrivername());
        if (UpcomingOrders.get(position).getDrivername().equals("")){
            holder.driver_layout.setVisibility(View.GONE);
        }else{
            holder.driver_layout.setVisibility(View.VISIBLE);
        }*/
        holder.driver_layout.setVisibility(View.GONE);
        System.out.println("ORDER STATUS " + UpcomingOrders.get(position).getOrderStatus());
        if (UpcomingOrders.get(position).getOrderType() == 0 && UpcomingOrders.get(position).getOrderStatus() == 3) {
            holder.preparing_text.setVisibility(View.VISIBLE);
        } else {
            holder.preparing_text.setVisibility(View.GONE);
        }
        //holder.track.setVisibility(View.VISIBLE);

        if (orderModel.getRemainingseconds() > 0) {

            int progress = orderModel.getRemainingseconds();
            //holder.tvTime.setText(String.valueOf(progress)+"\n Mins");
            if (progress > 0) {
                ProgressBar downloadProgress = holder.progress;
                downloadProgress.setMax((orderModel.getTotalseconds()) / 60);
                if (downloadProgress.isIndeterminate()) {
                    downloadProgress.setIndeterminate(false);
                }
                //downloadProgress.setProgress(progress);
                setProgressLoader(holder.progress, orderModel.getTotalseconds(), orderModel.getRemainingseconds());
            } else {
                holder.progress.setProgress(0);
            }

        } else {
            holder.progress.setProgress(0);
        }

        holder.track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.isRefreshed = true;
                Intent track = new Intent(context, DriverTrackingActivity.class);
                track.putExtra("position", position);
                context.startActivity(track);
            }
        });
        holder.rltImagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setRestaurantId(UpcomingOrders.get(position).getRestaurantid());
                Intent detail = new Intent(context, RestaurantDetailActivity.class);
                context.startActivity(detail);
            }
        });

        menuListModels = UpcomingOrders.get(position).getMenu();
        holder.food_layout.removeAllViews();
        for (int i = 0; i < UpcomingOrders.get(position).getMenu().size(); i++) {

            View view = LayoutInflater.from(context).inflate(R.layout.item_list_histroy, null);
            CustomTextView item_count = view.findViewById(R.id.item_count);
            CustomTextView item_name = view.findViewById(R.id.item_name);
            ImageView thumbs = view.findViewById(R.id.thumbs);


            thumbs.setVisibility(View.INVISIBLE);


            item_count.setText(String.valueOf(menuListModels.get(i).getQuantity()));
            item_name.setText(menuListModels.get(i).getMenuName());
            holder.food_layout.addView(view);
            System.out.println("MEnu LIst " + menuListModels.get(i).getMenuName());
        }

        holder.receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert(position);
            }
        });
    }


    public void notifys() {
        notifyDataSetChanged();
    }

    /**
     * To the  Receipt dialog
     *
     * @param position of the order in that particular order in  list
     */


    private void showAlert(int position) {
        menuListModels = UpcomingOrders.get(position).getMenu();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.receipt, null, false);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        RecyclerView orders_list = dialogView.findViewById(R.id.orders_list);
        RelativeLayout receipt_footer = dialogView.findViewById(R.id.receipt_footer);
        CustomTextView tvSubTotalPrice = dialogView.findViewById(R.id.tvSubTotalPrice);
        CustomTextView tvTaxPrice = dialogView.findViewById(R.id.tvTaxPrice);
        CustomTextView tvDeliveryFee = dialogView.findViewById(R.id.tvDeliveryFee);
        CustomTextView tvTotalPrice = dialogView.findViewById(R.id.tvTotalPrice);
        RelativeLayout rltWallet = dialogView.findViewById(R.id.rltWallet);
        RelativeLayout rltPromo = dialogView.findViewById(R.id.rltPromo);
        CustomTextView tvWalletAmount = dialogView.findViewById(R.id.tvWalletAmount);
        CustomTextView tvDiscountPrice = dialogView.findViewById(R.id.tvDiscountPrice);
        RelativeLayout delivery_fee_layout = dialogView.findViewById(R.id.delivery_fee_layout);
        RelativeLayout rltServiceFee = dialogView.findViewById(R.id.rltServiceFee);
        CustomTextView tvServiceAmount = dialogView.findViewById(R.id.tvServiceAmount);
        RelativeLayout tax_layout = dialogView.findViewById(R.id.tax_layout);
        RelativeLayout rltPenalityFee = dialogView.findViewById(R.id.rltPenalityFee);
        CustomTextView tvPenalityPrice = dialogView.findViewById(R.id.tvPenalityPrice);


        tvSubTotalPrice.setText(sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getSubtotal());
        tvTaxPrice.setText(sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getTax());
        tvTotalPrice.setText(sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getTotalamount());
        if (!TextUtils.isEmpty(UpcomingOrders.get(position).getDeliveryfee()) && Float.valueOf(UpcomingOrders.get(position).getDeliveryfee()) > 0) {
            delivery_fee_layout.setVisibility(View.VISIBLE);
            tvDeliveryFee.setText(sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getDeliveryfee());
        } else {
            delivery_fee_layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(UpcomingOrders.get(position).getWalletamount()) && Float.valueOf(UpcomingOrders.get(position).getWalletamount()) > 0) {
            rltWallet.setVisibility(View.VISIBLE);
            if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                tvWalletAmount.setText("(" + sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getWalletamount() + "-)");
            }else {
                tvWalletAmount.setText("(-" + sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getWalletamount() + ")");
            }
        } else {
            rltWallet.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(UpcomingOrders.get(position).getPromoAmount()) && Float.valueOf(UpcomingOrders.get(position).getPromoAmount()) > 0) {
            rltPromo.setVisibility(View.VISIBLE);
            if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                tvDiscountPrice.setText("(" + sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getPromoAmount() + "-)");
            }else {
                tvDiscountPrice.setText("(-" + sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getPromoAmount() + ")");
            }
        } else {
            rltPromo.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(UpcomingOrders.get(position).getBookingFee()) && Float.valueOf(UpcomingOrders.get(position).getBookingFee()) > 0) {
            rltServiceFee.setVisibility(View.VISIBLE);
            tvServiceAmount.setText(sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getBookingFee());
        } else {
            rltServiceFee.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(UpcomingOrders.get(position).getTax()) && Float.valueOf(UpcomingOrders.get(position).getTax()) > 0) {
            tax_layout.setVisibility(View.VISIBLE);
            tvTaxPrice.setText(sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getTax());
        } else {
            tax_layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(UpcomingOrders.get(position).getPenality()) && Float.valueOf(UpcomingOrders.get(position).getPenality()) > 0) {
            rltPenalityFee.setVisibility(View.VISIBLE);
            tvPenalityPrice.setText(sessionManager.getCurrencySymbol() + UpcomingOrders.get(position).getPenality());
        } else {
            rltPenalityFee.setVisibility(View.GONE);
        }

        orders_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        orders_list.setLayoutManager(linearLayoutManager);

        ReceiptOrderAdapter receiptOrderAdapter = new ReceiptOrderAdapter(menuListModels);
        orders_list.setAdapter(receiptOrderAdapter);

        receipt_footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return UpcomingOrders.size();
    }


    public void setProgressLoader(final ProgressBar progressBar, final long totalTime, final int remainingTime) {
        new CountDownTimer(remainingTime * 1000, 1000 * 60) {
            @Override
            public void onTick(long millisUntilFinished) {
                //this will be done every 1000 milliseconds ( 1 seconds )
                int progress = (int) (((totalTime * 1000) - millisUntilFinished) / 1000);
                progressBar.setProgress(progress / 60);
                //System.out.println("Progress "+progress/60);
                //System.out.println("totalTime "+totalTime/60);
            }

            @Override
            public void onFinish() {
                //the progressBar will be invisible after 60 000 miliseconds ( 1 minute)
                //progressBar.dismiss();
                progressBar.setProgress((int) (totalTime / 60));
            }

        }.start();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout food_layout;
        private CustomTextView order_status;
        private CustomTextView track;
        private CustomTextView tvOrderArrival;
        private CustomTextView tvOrderDate;
        private ProgressBar progress;
        private CustomTextView preparing_text;
        private RelativeLayout driver_layout;
        private RelativeLayout rltImagelayout;
        private CustomButton receipt;
        private CustomTextView driver_name;
        private ImageView ivRestImag;
        private CustomTextView tvRestName;
        private CustomTextView tvOrderId;
        private CustomTextView tvTotal;
        private CustomTextView tvTotalamount;
        private ImageView thumbs;
        private ImageView statusIcon;
        private CustomButton btnCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            food_layout = itemView.findViewById(R.id.food_layout);
            progress = itemView.findViewById(R.id.progress);
            order_status = itemView.findViewById(R.id.order_status);
            track = itemView.findViewById(R.id.track);
            tvOrderArrival = itemView.findViewById(R.id.tvOrderArrival);
            preparing_text = itemView.findViewById(R.id.preparing_text);
            driver_layout = itemView.findViewById(R.id.driver_layout);
            receipt = itemView.findViewById(R.id.receipt);
            driver_name = itemView.findViewById(R.id.driver_name);
            ivRestImag = itemView.findViewById(R.id.ivRestImag);
            tvRestName = itemView.findViewById(R.id.tvRestName);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvTotalamount = itemView.findViewById(R.id.tvTotalamount);
            rltImagelayout = itemView.findViewById(R.id.rltImagelayout);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            thumbs = itemView.findViewById(R.id.thumbs);
            statusIcon = itemView.findViewById(R.id.status_icon);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
