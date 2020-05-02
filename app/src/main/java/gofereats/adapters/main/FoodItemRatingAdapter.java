package gofereats.adapters.main;

/**
 * @package com.gofereats
 * @subpackage adapters.main
 * @category Adapter
 * @author Trioangle Product Team
 * @version 0.6
 */


/* ************************************************************
                Food Item Rating List View Adapter class
    *********************************************************** */


import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.obs.CustomEditText;
import com.obs.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gofereats.R;
import gofereats.datamodels.order.MenuListModel;
import gofereats.datamodels.ratings.issueslist.FoodIssueModelList;
import gofereats.views.main.subviews.RatingActivity;

/**
 * Created by trioangle on 5/25/18.
 */

public class FoodItemRatingAdapter extends RecyclerView.Adapter<FoodItemRatingAdapter.ViewHolder> {

    public static ArrayList<FoodIssueModelList> foodIssueList = new ArrayList<>();
    private Context context;
    private ArrayList<MenuListModel> menuList;
    private RatingActivity ratingActivity = new RatingActivity();
    private String foodReason;

    private JSONArray foodArray;

    /**
     * FoodItemRatingAdapter Constructor
     *
     * @param menuList MenuListModel Array list
     * @param context  context of an Activity
     */


    public FoodItemRatingAdapter(Context context, ArrayList<MenuListModel> menuList) {
        this.context = context;
        this.menuList = menuList;
        foodReason = "";
        foodArray = new JSONArray();
    }

    @NonNull
    @Override
    public FoodItemRatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_rating, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodItemRatingAdapter.ViewHolder holder, final int position) {

        holder.tvMenuname.setText(menuList.get(position).getMenuName());
        //Glide.with(context).load(menuList.get(position).getItemimage()).into(holder.ivMenuImage);
        Glide.with(context).load(menuList.get(position).getMenuImageList().getLargeImage()).into(holder.ivMenuImage);

        /**
         *  issue Reasoons For Food
         */

        holder.thumbs_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodReason = "";
                holder.like_status_icon.setVisibility(View.VISIBLE);
                holder.like_status_icon.setImageResource(R.drawable.thumbs_up_selected);
                holder.thumbs_up.setImageResource(R.drawable.thumbs_up_selected);
                holder.thumbs_down.setImageResource(R.drawable.thumbs_down_normal);
                holder.thumbs_up.setActivated(true);
                holder.thumbs_down.setActivated(false);
                holder.delivery_status.setText(R.string.oh_great);
                holder.add_commet_layout.setVisibility(View.VISIBLE);
                holder.feedback_comments.setVisibility(View.GONE);
                addJSON(holder, position);
            }
        });

        holder.thumbs_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodReason = "";
                holder.like_status_icon.setVisibility(View.VISIBLE);
                holder.like_status_icon.setImageResource(R.drawable.thumbs_down_selected);
                holder.thumbs_down.setImageResource(R.drawable.thumbs_down_selected);
                holder.thumbs_up.setImageResource(R.drawable.thumbs_up_normal);
                holder.thumbs_down.setActivated(true);
                holder.thumbs_up.setActivated(false);
                holder.delivery_status.setText(R.string.what_went_wrong);
                holder.add_commet_layout.setVisibility(View.VISIBLE);
                holder.feedback_comments.setVisibility(View.VISIBLE);
                System.out.println("THUMBs Down " + ratingActivity.getFoodIssueList().size());
                addJSON(holder, position);
                foodIssueList = ratingActivity.getFoodIssueList();

                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                //gridLayoutManager.setAutoMeasureEnabled(false);
                gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                holder.rvMenuIssuesList.setLayoutManager(gridLayoutManager);

               /* holder.rvMenuIssuesList.setHasFixedSize(true);
                  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                  holder.rvMenuIssuesList.setLayoutManager(linearLayoutManager);*/
                MenuFeedBackAdapter menuFeedBackAdapter = new MenuFeedBackAdapter(context, ratingActivity.getFoodIssueList());
                menuFeedBackAdapter.setOnItemClickListener(new MenuFeedBackAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int id, int positionz) {
                        foodReason = "";
                        for (int i = 0; i < foodIssueList.size(); i++) {
                            if (!foodIssueList.get(i).isSelected()) {
                                foodIssueList.get(i).getId();
                                System.out.println("SIZE" + foodIssueList.get(i).getId());

                                if (TextUtils.isEmpty(foodReason)) {
                                    foodReason = foodReason + foodIssueList.get(i).getId();
                                } else {
                                    foodReason = foodReason + "," + foodIssueList.get(i).getId();
                                }
                            }
                        }
                        System.out.println("foodReason " + foodReason);
                        addJSON(holder, position);
                    }
                });

                holder.rvMenuIssuesList.setAdapter(menuFeedBackAdapter);
                menuFeedBackAdapter.notifyDataSetChanged();
            }
        });

        holder.add_comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) holder.add_comment.setHint("");
                else
                    holder.add_comment.setHint(context.getResources().getString(R.string.add_a_comment));
            }
        });
        holder.add_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                System.out.print(i);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.print(i);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addJSON(holder, position);
            }
        });
    }


    /**
     * Sending Food Array to the Rating Activity
     *
     * @param holder   holder for that particular view in a list
     * @param position position of a view in a list
     */

    public void addJSON(ViewHolder holder, int position) {
        JSONObject items = new JSONObject();

        try {
            if (holder.thumbs_down.isActivated() || holder.thumbs_up.isActivated()) {
                holder.add_comment.setVisibility(View.VISIBLE);
                if (foodArray.length() > 0) {
                    JSONArray jsonArray = foodArray;
                    foodArray = new JSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (menuList.get(position).getOrderItemId() != jsonArray.getJSONObject(i).getInt("order_item_id")) {
                            foodArray.put(jsonArray.get(i));
                        }
                    }
                    int thumbs = 0;
                    if (holder.thumbs_up.isActivated()) thumbs = 1;
                    items.put("id", menuList.get(position).getMenuitemid());
                    items.put("order_item_id", menuList.get(position).getOrderItemId());
                    items.put("thumbs", thumbs);
                    items.put("reason", foodReason);
                    items.put("comment", holder.add_comment.getText().toString());
                    foodArray.put(items);
                    System.out.println("Food Array " + foodArray.toString());
                } else {

                    int thumbs = 0;
                    if (holder.thumbs_up.isActivated()) thumbs = 1;
                    items.put("id", menuList.get(position).getMenuitemid());
                    items.put("order_item_id", menuList.get(position).getOrderItemId());
                    items.put("thumbs", thumbs);
                    items.put("reason", foodReason);
                    items.put("comment", holder.add_comment.getText().toString());
                    foodArray.put(items);
                    System.out.println("Food Array " + foodArray.toString());
                }

                Intent intent = new Intent("foodArray");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("foodArray", foodArray.toString());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            } else {
                holder.add_comment.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView tvMenuname;
        private ImageView ivMenuImage;
        private CircleImageView thumbs_up;
        private CircleImageView thumbs_down;
        private CircleImageView like_status_icon;
        private CustomTextView delivery_status;
        private RelativeLayout add_commet_layout;
        private RelativeLayout feedback_comments;
        private CustomEditText add_comment;
        private RecyclerView rvMenuIssuesList;

        public ViewHolder(View itemView) {
            super(itemView);
            ivMenuImage = itemView.findViewById(R.id.ivMenuImage);
            tvMenuname = itemView.findViewById(R.id.tvMenuname);
            thumbs_up = itemView.findViewById(R.id.thumbs_up);
            thumbs_down = itemView.findViewById(R.id.thumbs_down);
            delivery_status = itemView.findViewById(R.id.delivery_status);
            add_commet_layout = itemView.findViewById(R.id.add_commet_layout);
            feedback_comments = itemView.findViewById(R.id.feedback_comments);
            add_comment = itemView.findViewById(R.id.add_comment);
            //issues=(LinearLayout)itemView.findViewById(R.id.issues);
            like_status_icon = itemView.findViewById(R.id.like_status_icon);
            rvMenuIssuesList = itemView.findViewById(R.id.rvMenuIssuesList);
        }
    }
}
