package gofereats.adapters.main;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.obs.CustomButton;
import com.obs.CustomTextView;

import java.util.List;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.order.MenuListModel;
import gofereats.datamodels.order.OrderModel;
import gofereats.views.main.subviews.RatingActivity;
import gofereats.views.main.subviews.RestaurantDetailActivity;

/**
 * @package com.gofereats
 * @subpackage adapters.main
 * @category Adapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/*************************************************************
 Adapter Class for Past Order Recycler view
 *********************************************************** */

public class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.ViewHolder> {

    public @Inject
    SessionManager sessionManager;

    private Context context;
    private List<OrderModel> pastOrders;
    private List<MenuListModel> menuListModels;


    /**
     * Past Order adapter constructor
     *
     * @param context    context of a past order Activity
     * @param pastOrders past order array list
     */


    public PastOrderAdapter(Context context, List<OrderModel> pastOrders) {
        AppController.getAppComponent().inject(this);
        this.context = context;
        this.pastOrders = pastOrders;
    }

    @NonNull
    @Override
    public PastOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PastOrderAdapter.ViewHolder holder, final int position) {

        if (pastOrders.get(position).getRestaurantClosed() == 1) {
            holder.tvOpeningTime.setVisibility(View.GONE);
            if (pastOrders.get(position).getRestaurantStatus() == 0) {
                holder.rltUnavailble.setVisibility(View.VISIBLE);
                holder.tvCurrently_Unavaiable.setText(context.getResources().getString(R.string.currently_unavailable));
                holder.menu_layout.setVisibility(View.GONE);
            } else {
                holder.rltUnavailble.setVisibility(View.GONE);
                holder.menu_layout.setVisibility(View.VISIBLE);
                holder.tvRestName.setText(pastOrders.get(position).getName());
            }
        } else {
            holder.rltUnavailble.setVisibility(View.VISIBLE);
            holder.tvCurrently_Unavaiable.setText(context.getResources().getString(R.string.closed));
            holder.menu_layout.setVisibility(View.GONE);
            holder.tvOpeningTime.setText(pastOrders.get(position).getRestaurantNextTime());
        }
        Glide.with(context).load(pastOrders.get(position).getRestaurantBanner().getOriginal()).into(holder.ivRestImag);
        holder.order_status.setText(pastOrders.get(position).getUserstatustext());
        if (pastOrders.get(position).getDrivername().equals("")) {
            holder.driver_layout.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        } else {
            //holder.driver_name.setText(context.getString(R.string.your_delivery) + " " + pastOrders.get(position).getDrivername());
            holder.driver_name.setText(pastOrders.get(position).getDrivername());
            holder.driver_layout.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);

        }

        if (pastOrders.get(position).getStatus() == 4 || pastOrders.get(position).getStatus() == 2) {
            holder.status_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.round_cancel));
        } else {
            holder.status_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.round_checked_tick));
            holder.status_icon.setColorFilter(ContextCompat.getColor(context, R.color.apptheme), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        holder.tvOrderTime.setText(pastOrders.get(position).getDate());
        if("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction)))
            holder.tvOrderId.setText(context.getResources().getString(R.string.orderid) + pastOrders.get(position).getOrderid() + "#");
        else
            holder.tvOrderId.setText(context.getResources().getString(R.string.orderid)+ "#" + pastOrders.get(position).getOrderid());
        holder.tvTotal.setText(context.getResources().getString(R.string.total_dot));
        holder.tvTotalamount.setText(sessionManager.getCurrencySymbol() + pastOrders.get(position).getTotalamount());


        if (pastOrders.get(position).getIsRating() == 1) {
            holder.btnRateorder.setVisibility(View.GONE);
            if (Float.valueOf(pastOrders.get(position).getStarRating()) > 0) {
                holder.restaurantRating.setVisibility(View.VISIBLE);
                holder.restaurantRating.setRating(Float.valueOf(pastOrders.get(position).getStarRating()));
            } else {
                holder.restaurantRating.setVisibility(View.GONE);
            }
        } else {
            if (pastOrders.get(position).getStatus() == 4 || pastOrders.get(position).getStatus() == 2) {
                holder.btnRateorder.setVisibility(View.GONE);
                holder.restaurantRating.setVisibility(View.GONE);
            } else if (pastOrders.get(position).getStatus() == 6) {
                holder.btnRateorder.setVisibility(View.VISIBLE);
                holder.restaurantRating.setVisibility(View.GONE);
            }
        }

        if (pastOrders.get(position).getAppliedPenality() != null && !TextUtils.isEmpty(pastOrders.get(position).getAppliedPenality()) && Float.valueOf(pastOrders.get(position).getAppliedPenality()) > 0) {
            holder.line5.setVisibility(View.VISIBLE);
            if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))){
                holder.tvAppliedPenality.setText(context.getResources().getString(R.string.applied_penalty) + " " + pastOrders.get(position).getAppliedPenality()+sessionManager.getCurrencySymbol() );
            }else {
                holder.tvAppliedPenality.setText(context.getResources().getString(R.string.applied_penalty) + " " + sessionManager.getCurrencySymbol() + pastOrders.get(position).getAppliedPenality());
            }
            holder.tvAppliedPenality.setVisibility(View.VISIBLE);
        } else {
            holder.tvAppliedPenality.setVisibility(View.GONE);
        }

        if (pastOrders.get(position).getNotes() != null && !TextUtils.isEmpty(pastOrders.get(position).getNotes())) {
            //holder.tvNotesPenality.setText(pastOrders.get(position).getNotes());
            SpannableStringBuilder spanTxt = new SpannableStringBuilder(context.getResources().getString(R.string.notes));
            spanTxt.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.error_red)), spanTxt.length() - context.getResources().getString(R.string.notes).length(), spanTxt.length(), 0);
            spanTxt.append(" ");
            spanTxt.append(pastOrders.get(position).getNotes());
            holder.tvNotesPenality.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tvNotesPenality.setText(spanTxt, TextView.BufferType.SPANNABLE);
            holder.tvNotesPenality.setVisibility(View.VISIBLE);
        } else {
            holder.tvNotesPenality.setVisibility(View.GONE);
        }
        if (pastOrders.get(position).getAppliedPenality() != null && !TextUtils.isEmpty(pastOrders.get(position).getAppliedPenality()) && Float.valueOf(pastOrders.get(position).getAppliedPenality()) > 0
                || pastOrders.get(position).getNotes() != null && !TextUtils.isEmpty(pastOrders.get(position).getNotes())) {
            holder.line5.setVisibility(View.VISIBLE);
        } else {
            holder.line5.setVisibility(View.GONE);
        }

        holder.btnRateorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rating = new Intent(context, RatingActivity.class);
                rating.putExtra("orderId", pastOrders.get(position).getOrderid());
                context.startActivity(rating);
            }
        });
        holder.rltImagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setRestaurantId(pastOrders.get(position).getRestaurantid());
                Intent detail = new Intent(context, RestaurantDetailActivity.class);
                context.startActivity(detail);
            }
        });
        menuListModels = pastOrders.get(position).getMenu();
        if (pastOrders.get(position).getMenu().size() > 0) {
            holder.food_layout.setVisibility(View.VISIBLE);
            holder.line1.setVisibility(View.VISIBLE);
            holder.line2.setVisibility(View.VISIBLE);
        } else {
            holder.food_layout.setVisibility(View.GONE);
            holder.line1.setVisibility(View.GONE);
            holder.line2.setVisibility(View.GONE);
        }
        holder.food_layout.removeAllViews();
        for (int i = 0; i < pastOrders.get(position).getMenu().size(); i++) {

            //holder.food_layout.removeAllViews();
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_histroy, null);
            CustomTextView item_count = view.findViewById(R.id.item_count);
            CustomTextView item_name = view.findViewById(R.id.item_name);
            ImageView thumbs = view.findViewById(R.id.thumbs);

            item_count.setText(String.valueOf(menuListModels.get(i).getQuantity()));
            item_name.setText(menuListModels.get(i).getMenuName());

            if (menuListModels.get(i).getReview() == 0) {
                thumbs.setVisibility(View.VISIBLE);
                thumbs.setRotation(180);
            } else if (menuListModels.get(i).getReview() == 1) {
                thumbs.setVisibility(View.VISIBLE);
                thumbs.setRotation(0);
            } else {
                thumbs.setVisibility(View.INVISIBLE);
            }

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


    /**
     * Receipt dialog of that particular past order
     *
     * @param position of the order in a list view
     */

    private void showAlert(int position) {

        menuListModels = pastOrders.get(position).getMenu();
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
        RelativeLayout rltPenalityFee = dialogView.findViewById(R.id.rltPenalityFee);
        CustomTextView tvPenalityPrice = dialogView.findViewById(R.id.tvPenalityPrice);
        RelativeLayout tax_layout = dialogView.findViewById(R.id.tax_layout);
        RelativeLayout rltAppliedPenality = dialogView.findViewById(R.id.rltAppliedPenality);
        CustomTextView tvAppliedPenalityPrice = dialogView.findViewById(R.id.tvAppliedPenalityPrice);


        tvSubTotalPrice.setText(sessionManager.getCurrencySymbol() + pastOrders.get(position).getSubtotal());
        tvTaxPrice.setText(sessionManager.getCurrencySymbol() + pastOrders.get(position).getTax());
        tvTotalPrice.setText(sessionManager.getCurrencySymbol() + pastOrders.get(position).getTotalamount());

        if (!TextUtils.isEmpty(pastOrders.get(position).getDeliveryfee()) && Float.valueOf(pastOrders.get(position).getDeliveryfee()) > 0) {
            delivery_fee_layout.setVisibility(View.VISIBLE);
            tvDeliveryFee.setText(sessionManager.getCurrencySymbol() + pastOrders.get(position).getDeliveryfee());
        } else {
            delivery_fee_layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(pastOrders.get(position).getWalletamount()) && Float.valueOf(pastOrders.get(position).getWalletamount()) > 0) {
            rltWallet.setVisibility(View.VISIBLE);
            if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                tvWalletAmount.setText("(" + sessionManager.getCurrencySymbol() + pastOrders.get(position).getWalletamount() + "-)");
            }else {
                tvWalletAmount.setText("(-" + sessionManager.getCurrencySymbol() + pastOrders.get(position).getWalletamount() + ")");
            }
        } else {
            rltWallet.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(pastOrders.get(position).getPromoAmount()) && Float.valueOf(pastOrders.get(position).getPromoAmount()) > 0) {
            rltPromo.setVisibility(View.VISIBLE);
            if ("1".equalsIgnoreCase(context.getResources().getString(R.string.layout_direction))) {
                tvDiscountPrice.setText("(" + sessionManager.getCurrencySymbol() + pastOrders.get(position).getPromoAmount() + "-)");
            }else {
                tvDiscountPrice.setText("(-" + sessionManager.getCurrencySymbol() + pastOrders.get(position).getPromoAmount() + ")");
            }

        } else {
            rltPromo.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(pastOrders.get(position).getBookingFee()) && Float.valueOf(pastOrders.get(position).getBookingFee()) > 0) {
            rltServiceFee.setVisibility(View.VISIBLE);
            tvServiceAmount.setText(sessionManager.getCurrencySymbol() + pastOrders.get(position).getBookingFee());
        } else {
            rltServiceFee.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(pastOrders.get(position).getTax()) && Float.valueOf(pastOrders.get(position).getTax()) > 0) {
            tax_layout.setVisibility(View.VISIBLE);
            tvTaxPrice.setText(sessionManager.getCurrencySymbol() + pastOrders.get(position).getTax());
        } else {
            tax_layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(pastOrders.get(position).getPenality()) && Float.valueOf(pastOrders.get(position).getPenality()) > 0) {
            rltPenalityFee.setVisibility(View.VISIBLE);
            tvPenalityPrice.setText(sessionManager.getCurrencySymbol() + pastOrders.get(position).getPenality());
        } else {
            rltPenalityFee.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(pastOrders.get(position).getAppliedPenality()) && Float.valueOf(pastOrders.get(position).getAppliedPenality()) > 0) {
            //rltAppliedPenality.setVisibility(View.VISIBLE);  // if the applied Penalty amount to show in receipt uncomment the visible
            tvAppliedPenalityPrice.setText("-" + sessionManager.getCurrencySymbol() + pastOrders.get(position).getAppliedPenality());
        } else {
            rltAppliedPenality.setVisibility(View.GONE);
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
        return pastOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout food_layout;
        private CustomTextView order_status;
        private CustomButton receipt;
        private CustomButton btnRateorder;
        private RelativeLayout driver_layout;
        private View view;
        private View line1;
        private View line2;
        private RelativeLayout rltImagelayout;
        private CustomTextView driver_name;
        private CustomTextView tvOrderId;
        private CustomTextView tvTotal;
        private CustomTextView tvTotalamount;
        private CustomTextView tvRestName;
        private CustomTextView tvOpeningTime;
        private CustomTextView tvOrderTime;
        private ImageView ivRestImag;
        private ImageView status_icon;
        private RatingBar restaurantRating;
        private RelativeLayout rltUnavailble;
        private RelativeLayout menu_layout;
        private CustomTextView tvCurrently_Unavaiable;
        private CustomTextView tvAppliedPenality;
        private View line5;
        private CustomTextView tvNotesPenality;


        public ViewHolder(View itemView) {
            super(itemView);
            food_layout = itemView.findViewById(R.id.food_layout);
            order_status = itemView.findViewById(R.id.tvOrderstatus);
            //preparing_text=(CustomTextView)itemView.findViewById(R.id.preparing_text);
            driver_layout = itemView.findViewById(R.id.driver_layout);
            receipt = itemView.findViewById(R.id.receipt);
            view = itemView.findViewById(R.id.line4);
            driver_name = itemView.findViewById(R.id.driver_name);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvTotalamount = itemView.findViewById(R.id.tvTotalamount);
            ivRestImag = itemView.findViewById(R.id.ivRestImag);
            tvRestName = itemView.findViewById(R.id.tvRestName);
            tvOpeningTime = itemView.findViewById(R.id.tvOpeningTime);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            rltImagelayout = itemView.findViewById(R.id.rltImagelayout);
            btnRateorder = itemView.findViewById(R.id.btnRateorder);
            status_icon = itemView.findViewById(R.id.status_icon);
            restaurantRating = itemView.findViewById(R.id.RestaurantRating);
            rltUnavailble = itemView.findViewById(R.id.rltUnavailble);
            menu_layout = itemView.findViewById(R.id.menu_layout);
            tvCurrently_Unavaiable = itemView.findViewById(R.id.tvCurrently_Unavaiable);
            line1 = itemView.findViewById(R.id.line1);
            line2 = itemView.findViewById(R.id.line2);
            line5 = itemView.findViewById(R.id.line5);
            tvAppliedPenality = itemView.findViewById(R.id.tvAppliedPenality);
            tvNotesPenality = itemView.findViewById(R.id.tvNotesPenality);
        }
    }
}
