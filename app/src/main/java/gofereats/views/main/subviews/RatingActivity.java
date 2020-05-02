package gofereats.views.main.subviews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereats.R;
import gofereats.adapters.main.DriverFeedBacKAdapter;
import gofereats.adapters.main.FoodItemRatingAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.order.MenuListModel;
import gofereats.datamodels.ratings.RatingDetailsModel;
import gofereats.datamodels.ratings.RatingOrderDetailsModel;
import gofereats.datamodels.ratings.issueslist.DriverIssueListModel;
import gofereats.datamodels.ratings.issueslist.FoodIssueModelList;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.ImageUtils;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;

import static gofereats.utils.Enums.REQ_RATING;
import static gofereats.utils.Enums.REQ_RATING_UPDATE;

public class RatingActivity extends AppCompatActivity implements ServiceListener, View.OnFocusChangeListener {

    public static ArrayList<DriverIssueListModel> driverIssueList = new ArrayList<>();
    public static ArrayList<FoodIssueModelList> foodIssueList = new ArrayList<>();
    public static ArrayList<MenuListModel> ratingMenuList = new ArrayList<>();
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    ImageUtils imageUtils;
    public @Inject
    Gson gson;
    public CircleImageView thumbs_up;
    public CircleImageView thumbs_down;
    public CircleImageView like_status_icon;
    public CircleImageView ivDriverImage;
    public ImageView ivResturantImage;
    public CustomTextView delivery_status;
    public CustomTextView tvResturantName;
    public CustomTextView tvResturantOrderId;
    public RelativeLayout add_commet_layout;
    public RelativeLayout feedback_comments;
    public RatingBar ratingBar;
    public int orderId = 0;
    public String driverReason = "";
    public boolean submitEnable = false;
    public float resturantRating;
    // JSON conversion
    public String jsonString = "";
    public JSONObject json = new JSONObject();
    public JSONObject driverObj = new JSONObject();
    public JSONObject restaurantObj = new JSONObject();
    public JSONArray foodArray = new JSONArray();
    public CustomButton submit;
    public RecyclerView rvIssues;
    public RecyclerView rvMenus;
    public RatingDetailsModel ratingDetailsModel;
    public RatingOrderDetailsModel ratingOrderDetailsModel;
    public FoodItemRatingAdapter foodItemRatingAdapter;
    public DriverFeedBacKAdapter driverFeedBacKAdapter;
    public @InjectView(R.id.edtDriverComment)
    EditText edtDriverComment;
    public @InjectView(R.id.edtRestaurantComment)
    EditText edtRestaurantComment;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String foodArrays = intent.getStringExtra("foodArray");
            //Toast.makeText(RatingActivity.this,"Food Array "+foodArrays ,Toast.LENGTH_SHORT).show();
            try {
                foodArray = new JSONArray();
                foodArray = new JSONArray(foodArrays);
                submitEnable = true;
                enableSubmit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this);

        initViews();
        recView();
        orderId = getIntent().getIntExtra("orderId", 0);
        getMenuForRating();

        edtDriverComment.setOnFocusChangeListener(this);
        edtRestaurantComment.setOnFocusChangeListener(this);

        /*String laydir = getResources().getString(R.string.layout_direction);
        if ("1".equals(laydir)) {
            ratingBar.setRotation(180);
            ratingBar.setScaleY(-1);
        }else {
            ratingBar.setRotation(0);
            ratingBar.setScaleY(0);
        }
*/
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                resturantRating = rating;
                System.out.println("Rating " + resturantRating);
                if (resturantRating > 0) edtRestaurantComment.setVisibility(View.VISIBLE);
                else edtRestaurantComment.setVisibility(View.GONE);
                submitEnable = true;
                enableSubmit();
            }
        });
        /**
         * Submit your Ratings
         */

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enableSubmit();
                System.out.println("message " + jsonString);
                //Toast.makeText(RatingActivity.this, "Thanks for the Ratings and comments", Toast.LENGTH_SHORT).show();
                updateRating(jsonString);
            }
        });
        /**
         * Like and Dislike Functionality
         */

        thumbs_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thumbsUp();
            }
        });

        thumbs_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thumbsDown();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("foodArray"));
    }

    /**
     * Ini Views
     */
    private void initViews() {
        rvMenus = findViewById(R.id.rvMenus);
        thumbs_up = findViewById(R.id.thumbs_up);
        thumbs_down = findViewById(R.id.thumbs_down);
        delivery_status = findViewById(R.id.delivery_status);
        add_commet_layout = findViewById(R.id.add_commet_layout);
        //issues=(LinearLayout)findViewById(R.id.issues);
        feedback_comments = findViewById(R.id.feedback_comments);
        like_status_icon = findViewById(R.id.like_status_icon);
        submit = findViewById(R.id.submit);
        rvIssues = findViewById(R.id.rvIssues);
        ivDriverImage = findViewById(R.id.ivDriverImage);
        tvResturantName = findViewById(R.id.tvResturantName);
        ivResturantImage = findViewById(R.id.ivResturantImage);
        tvResturantOrderId = findViewById(R.id.tvResturantOrderId);
        ratingBar = findViewById(R.id.ratingBar);


        dialog = commonMethods.getAlertDialog(this);

    }

    /**
     * Thumbs Up Functionality
     */
    private void thumbsUp() {
        like_status_icon.setVisibility(View.VISIBLE);
        like_status_icon.setImageResource(R.drawable.thumbs_up_selected);
        thumbs_up.setActivated(true);
        thumbs_down.setActivated(false);
        thumbs_up.setTag(1);
        thumbs_down.setTag(0);
        thumbs_up.setImageResource(R.drawable.thumbs_up_selected);
        thumbs_down.setImageResource(R.drawable.thumbs_down_normal);
        delivery_status.setText(getResources().getString(R.string.oh_great));
        add_commet_layout.setVisibility(View.VISIBLE);
        feedback_comments.setVisibility(View.GONE);
        submitEnable = true;
        enableSubmit();
    }

    /**
     * Thumbs Down Functionality
     */
    private void thumbsDown() {
        like_status_icon.setVisibility(View.VISIBLE);
        like_status_icon.setImageResource(R.drawable.thumbs_down_selected);
        thumbs_up.setActivated(false);
        thumbs_down.setActivated(true);
        thumbs_up.setTag(0);
        thumbs_down.setTag(1);
        thumbs_up.setImageResource(R.drawable.thumbs_up_normal);
        thumbs_down.setImageResource(R.drawable.thumbs_down_selected);
        delivery_status.setText(getResources().getString(R.string.what_could_better));
        add_commet_layout.setVisibility(View.VISIBLE);
        feedback_comments.setVisibility(View.VISIBLE);
        submitEnable = true;
        enableSubmit();
        driverFeedBacKAdapter = new DriverFeedBacKAdapter(getApplicationContext(), driverIssueList);
        driverFeedBacKAdapter.setOnItemClickListener(new DriverFeedBacKAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int id, int positionz) {
                driverReason = "";
                for (int i = 0; i < driverIssueList.size(); i++) {
                    if (!driverIssueList.get(i).isSelected()) {
                        driverIssueList.get(i).getId();
                        System.out.println("SIZE" + driverIssueList.get(i).getId());

                        if (TextUtils.isEmpty(driverReason)) {
                            driverReason = driverReason + driverIssueList.get(i).getId();
                        } else {
                            driverReason = driverReason + "," + driverIssueList.get(i).getId();
                        }

                    }
                }
                        /*
                         *  For Must Give the Reason For Thumbs Down
                         */

                       /* if (TextUtils.isEmpty(driverReason)) {
                            //submitEnable=false;
                        } else {
                            //submitEnable=true;
                        }*/
                System.out.println("Driver Issues " + driverReason);
                enableSubmit();
            }

        });

        rvIssues.setAdapter(driverFeedBacKAdapter);
        driverFeedBacKAdapter.notifyDataSetChanged();
    }

    /**
     * To enable submit button
     */

    public void enableSubmit() {

        //submit.setEnabled(true);
        //submit.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
        if (submitEnable) {//&&resturantRating>0.0){
            submit.setEnabled(true);
            submit.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
        } else {
            submit.setEnabled(false);
            submit.setBackgroundColor(getResources().getColor(R.color.appbuttondisable));
        }
        try {

            if (thumbs_down.isActivated() || thumbs_up.isActivated()) {
                int thumbs = 0;
                if (thumbs_up.isActivated()) thumbs = 1;

                driverObj.put("id", ratingOrderDetailsModel.getDriverId());
                driverObj.put("thumbs", thumbs);
                driverObj.put("reason", driverReason);
                driverObj.put("comment", edtDriverComment.getText().toString());
            }

            if (resturantRating > 0) {
                restaurantObj.put("id", ratingOrderDetailsModel.getRestaurantId());
                restaurantObj.put("thumbs", Float.valueOf(resturantRating));
                //restaurantObj.put("reason", "");
                restaurantObj.put("comment", edtRestaurantComment.getText().toString());
            }

            if (foodArray != null) json.put("food", foodArray);

            if (driverObj != null) json.put("driver", driverObj);
            if (restaurantObj != null) json.put("store", restaurantObj);

            jsonString = json.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setting RecyclerViews
     */
    private void recView() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        //gridLayoutManager.setAutoMeasureEnabled(false);
        //layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        rvIssues.setLayoutManager(layoutManager);
        rvIssues.setHasFixedSize(true);
        /*RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvIssues.setLayoutManager(layoutManager);*/

        rvMenus.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvMenus.setLayoutManager(linearLayoutManager);
    }

    /**
     * Call API to Get The Get Rating details for menu, driver and restaurant
     */
    private void getMenuForRating() {
        commonMethods.showProgressDialog(this, customDialog);
        apiService.userRatings(orderId, sessionManager.getToken()).enqueue(new RequestCallback(REQ_RATING, this));
    }

    /**
     * Call API to update rating for menu, driver and restaurant
     */
    private void updateRating(String rating) {
        commonMethods.showProgressDialog(this, customDialog);
        System.out.println("OrderId and Rating "+orderId+ ""+rating);
        apiService.updateRating(orderId, rating, sessionManager.getToken()).enqueue(new RequestCallback(REQ_RATING_UPDATE, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(this, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_RATING:
                if (jsonResp.isSuccess()) {
                    onSuccessRatingItemList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_RATING_UPDATE:
                if (jsonResp.isSuccess()) {
                    MainActivity.cancelled = true;
                    Intent restartMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    restartMainActivity.putExtra("type", "placeorder");
                    startActivity(restartMainActivity);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(this, dialog, jsonResp.getStatusMsg());
        }
    }


    /**
     * On Success Get List For rating
     *
     * @param jsonResp
     */
    private void onSuccessRatingItemList(JsonResponse jsonResp) {
        ratingDetailsModel = gson.fromJson(jsonResp.getStrResponse(), RatingDetailsModel.class);
        if (ratingDetailsModel != null) {
            updateView();
        }
    }

    private void updateView() {
        ratingOrderDetailsModel = ratingDetailsModel.getRatingOrderDetailsModel();
        Glide.with(this).load(ratingOrderDetailsModel.getDriverImage()).into(ivDriverImage);
        //Glide.with(this).load(ratingOrderDetailsModel.getRestauranBanner()).into(ivResturantImage);
        Glide.with(this).load(ratingOrderDetailsModel.getBannerImageList().getSmallImage()).into(ivResturantImage);
        tvResturantName.setText(ratingOrderDetailsModel.getResturantName());
        if("0".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            tvResturantOrderId.setText(ratingOrderDetailsModel.getResturantName() + "\n" +getResources().getString(R.string.orderid) + " "+ "#" + ratingOrderDetailsModel.getOrderId());
        else
            tvResturantOrderId.setText(ratingOrderDetailsModel.getResturantName() + "\n"+getResources().getString(R.string.orderid)  + " "+ ratingOrderDetailsModel.getOrderId()+  "#" );
        if (ratingOrderDetailsModel.getDriverIssues() != null && (ratingOrderDetailsModel.getDriverIssues().size() > 0)) {
            driverIssueList.clear();
            driverIssueList.addAll(ratingOrderDetailsModel.getDriverIssues());
        }

        if (ratingOrderDetailsModel.getFoodIssues() != null && ratingOrderDetailsModel.getFoodIssues().size() > 0) {
            foodIssueList.clear();
            foodIssueList.addAll(ratingOrderDetailsModel.getFoodIssues());
        }

        if (ratingOrderDetailsModel.getMenuList() != null && ratingOrderDetailsModel.getMenuList().size() > 0) {
            ratingMenuList.clear();
            ratingMenuList.addAll(ratingOrderDetailsModel.getMenuList());
        }

        foodItemRatingAdapter = new FoodItemRatingAdapter(getApplicationContext(), ratingMenuList);
        rvMenus.setAdapter(foodItemRatingAdapter);


    }

    public ArrayList<FoodIssueModelList> getFoodIssueList() {
        return foodIssueList;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.edtDriverComment:
                if (hasFocus) {
                    edtDriverComment.setHint("");
                } else {
                    edtDriverComment.setHint(getResources().getString(R.string.add_a_comment));
                }
                break;
            case R.id.edtRestaurantComment:
                if (hasFocus) {
                    edtRestaurantComment.setHint("");
                } else {
                    edtRestaurantComment.setHint(getResources().getString(R.string.add_a_comment));
                }
                break;
            default:
                break;
        }
    }
}
