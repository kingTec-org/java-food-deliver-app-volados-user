package gofereats.views.main.fragments;
/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.fragments.sortingfragments
 * @category Main Fragment
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.obs.CustomButton;
import com.obs.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gofereats.R;
import gofereats.adapters.main.home.FavoriteListAdapter;
import gofereats.adapters.main.home.MoreRestaurantListAdapter;
import gofereats.adapters.main.home.NewRestaurantListAdapter;
import gofereats.adapters.main.home.PopularListAdapter;
import gofereats.adapters.main.home.SeeMoreListAdapter;
import gofereats.adapters.main.home.UnderListAdapter;
import gofereats.adapters.main.home.ViewPagerAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.main.home.RestaurantListModel;
import gofereats.datamodels.main.home.RestaurantModel;
import gofereats.datamodels.main.home.RestaurantOfferModel;
import gofereats.interfaces.ActivityListener;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.OnLoadMoreListener;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CirclePageIndicator;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;
import gofereats.views.main.fragments.sortingfragments.FilterFragment;
import gofereats.views.main.subviews.FavouritesActivity;
import gofereats.views.main.subviews.LocActivity;
import gofereats.views.main.subviews.PlaceOrderActivity;

import static gofereats.utils.Enums.REQ_FILTER;
import static gofereats.utils.Enums.REQ_GET_HOME;

/*****************************************************************
 Home Fragment used in Main Activity for searching restaurants
 ****************************************************************/
public class HomeFragment extends Fragment implements ServiceListener {

    public static boolean isSeeMore = false;
    public static RecyclerView seemore_list;
    public static ArrayList<RestaurantListModel> moreRestaurantList = new ArrayList<>();
    public static ArrayList<RestaurantListModel> favoriteRestaurantList = new ArrayList<>();
    public static ArrayList<RestaurantListModel> popularRestaurantList = new ArrayList<>();
    public static ArrayList<RestaurantListModel> newRestaurantList = new ArrayList<>();
    public static ArrayList<RestaurantListModel> underRestaurantList = new ArrayList<>();
    public static ArrayList<RestaurantOfferModel> restaurantOfferList = new ArrayList<>();
    public static SwipeRefreshLayout swipeContainer;
    public static LinearLayout rltLocationText;
    public static RelativeLayout rltLoadMoreTitle;
    public static RelativeLayout rltFilter;
    public static int type = 0;
    private static boolean isMoreRestaurant = false;
    public ArrayList<RestaurantListModel> loadmoreList = new ArrayList<>();
    public int totalpages = 1;
    public int pageNo = 1;
    public RecyclerView list_twenty;
    public RecyclerView popular_list;
    public RecyclerView newtogofer_list;
    public RecyclerView more_rest_list;
    public RecyclerView favorite_list;
    public CustomTextView location_placesearch;
    public CustomTextView tvUnder;
    public RelativeLayout rltEmptylayout;
    public RelativeLayout rltWholeview;
    public ImageView ivSorting;
    public ViewPager viewpager;
    public FilterFragment newFragment;
    public @Inject
    ApiService apiService;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    Gson gson;
    public @InjectView(R.id.tvSearchType)
    CustomTextView tvSearchType;
    public @InjectView(R.id.tvSeeAll)
    CustomTextView tvSeeAll;
    public @InjectView(R.id.btnBrowse)
    CustomTextView btnBrowse;
    public @InjectView(R.id.ivHomeBack)
    ImageView ivHomeBack;
    public @InjectView(R.id.tvMostpopular)
    CustomTextView tvMostpopular;
    public @InjectView(R.id.tvSearchRating)
    CustomTextView tvSearchRating;
    public @InjectView(R.id.tvDeliveryTime)
    CustomTextView tvDeliveryTime;
    public @InjectView(R.id.tvPrice1)
    CustomTextView tvPrice1;
    public @InjectView(R.id.tvPrice2)
    CustomTextView tvPrice2;
    public @InjectView(R.id.tvPrice3)
    CustomTextView tvPrice3;
    public @InjectView(R.id.tvPrice4)
    CustomTextView tvPrice4;

    public @InjectView(R.id.lltFilterRow)
    LinearLayout lltFilterRow;
    public @InjectView(R.id.tvGettingOrderDetail)
    CustomTextView tvGettingOrderDetail;
    public @InjectView(R.id.scrollView)
    ScrollView scrollView;
    public DialogInject dialogInject;
    protected Handler handler;
    List<String> dietaryIdList = new ArrayList<String>();
    List<String> dietaryNameList = new ArrayList<String>();
    private View view;
    private ActivityListener listener;
    private MainActivity mActivity;
    private AlertDialog dialog;
    private RelativeLayout favorite_lay;
    private RelativeLayout under_lay;
    private RelativeLayout popular_lay;
    private RelativeLayout new_restaurant_lay;
    private RelativeLayout more_rest_lay;
    private RelativeLayout rltRestaurantOffer;
    private MoreRestaurantListAdapter moreRestaurantListAdapter;
    private SeeMoreListAdapter seemoreRestaurantListAdapter;
    private FavoriteListAdapter favoriteListAdapter;
    private PopularListAdapter popularListAdapter;
    private NewRestaurantListAdapter newRestaurantListAdapter;
    private UnderListAdapter underRestaurantListAdapter;
    private RestaurantModel RestaurantModel;
    private RestaurantModel RestaurantModelMore;
    private CustomTextView tvOrderType;
    private BottomSheetDialog bottomSheetDialog;
    private android.app.FragmentManager supportFragmentManager;

    boolean isStop=false;


    //private ArrayList<RestaurantOfferModel> restaurantOffer;  // Offer banner

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public static void showhome() {
        type = 0;
        isSeeMore = false;
        isMoreRestaurant = false;
        swipeContainer.setVisibility(View.VISIBLE);
        seemore_list.setVisibility(View.GONE);
        rltLoadMoreTitle.setVisibility(View.GONE);
        rltLocationText.setVisibility(View.VISIBLE);
        rltFilter.setVisibility(View.GONE);
    }


    @OnClick(R.id.tvMostpopular)
    public void tvMostpopular() {
        moveFilter();
        tvMostpopular.setVisibility(View.GONE);
        sessionManager.setSort(0);
        //showFilterResult();
        getFilter(true);

    }

    @OnClick(R.id.tvSearchRating)
    public void tvSearchRating() {
        moveFilter();
        tvSearchRating.setVisibility(View.GONE);
        sessionManager.setSort(0);
        //showFilterResult();
        getFilter(true);
    }

    @OnClick(R.id.tvDeliveryTime)
    public void tvDeliveryTime() {
        moveFilter();
        tvDeliveryTime.setVisibility(View.GONE);
        sessionManager.setSort(0);
        //showFilterResult();
        getFilter(true);
    }

    @OnClick(R.id.tvPrice1)
    public void tvPrice1() {
        moveFilter();
        tvPrice1.setVisibility(View.GONE);
        String price1 = sessionManager.getPriceSort();
        if (sessionManager.getPriceSort().contains("1,")) {
            price1 = price1.replace("1,", "");
            sessionManager.setPriceSort(price1);
        } else if (sessionManager.getPriceSort().contains("1")) {
            price1 = price1.replace("1", "");
            sessionManager.setPriceSort(price1);
        }
        if (sessionManager.getPriceSort().endsWith(",")) {
            price1 = sessionManager.getPriceSort().substring(0, sessionManager.getPriceSort().length() - 1);
            sessionManager.setPriceSort(price1);
        }
        //showFilterResult();
        getFilter(true);
    }

    @OnClick(R.id.tvPrice2)
    public void tvPrice2() {
        moveFilter();
        tvPrice2.setVisibility(View.GONE);
        String price2 = sessionManager.getPriceSort();
        if (sessionManager.getPriceSort().contains("2,")) {
            price2 = price2.replace("2,", "");
            sessionManager.setPriceSort(price2);
        } else if (sessionManager.getPriceSort().contains("2")) {
            price2 = price2.replace("2", "");
            sessionManager.setPriceSort(price2);
        }
        if (sessionManager.getPriceSort().endsWith(",")) {
            price2 = sessionManager.getPriceSort().substring(0, sessionManager.getPriceSort().length() - 1);
            sessionManager.setPriceSort(price2);
        }
        //showFilterResult();
        getFilter(true);
    }

    @OnClick(R.id.tvPrice3)
    public void tvPrice3() {
        moveFilter();
        tvPrice3.setVisibility(View.GONE);
        String price3 = sessionManager.getPriceSort();
        if (sessionManager.getPriceSort().contains("3,")) {
            price3 = price3.replace("3,", "");
            sessionManager.setPriceSort(price3);
        } else if (sessionManager.getPriceSort().contains("3")) {
            price3 = price3.replace("3", "");
            sessionManager.setPriceSort(price3);
        }
        if (sessionManager.getPriceSort().endsWith(",")) {
            price3 = sessionManager.getPriceSort().substring(0, sessionManager.getPriceSort().length() - 1);
            sessionManager.setPriceSort(price3);
        }
        getFilter(true);
    }

    @OnClick(R.id.tvPrice4)
    public void tvPrice4() {
        moveFilter();
        tvPrice4.setVisibility(View.GONE);
        String price4 = sessionManager.getPriceSort();
        if (sessionManager.getPriceSort().contains(",4")) {
            price4 = price4.replace(",4", "");
            sessionManager.setPriceSort(price4);
        } else if (sessionManager.getPriceSort().contains("4")) {
            price4 = price4.replace("4", "");
            sessionManager.setPriceSort(price4);
        }
        if (sessionManager.getPriceSort().endsWith(",")) {
            price4 = sessionManager.getPriceSort().substring(0, sessionManager.getPriceSort().length() - 1);
            sessionManager.setPriceSort(price4);
        }
        //showFilterResult();
        getFilter(true);
    }

    @OnClick(R.id.btnBrowse)
    public void browse() {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.putExtra("type", "search");
        startActivity(intent);
    }

    @OnClick(R.id.ivHomeBack)
    public void onBack() {
        showhome();
        // System.exit(0);
        //mActivity.finishAffinity();
        //MainActivity.onBackPressed();
        //supportFragmentManager.beginTransaction().remove(fragment);
    }

    @OnClick(R.id.tvSeeAll)
    public void seeAllResturant() {
        isMoreRestaurant = true;
        totalpages = RestaurantModel.getMoreTotalPage();
        type = 5;
        rltFilter.setVisibility(View.GONE);
        clearFilter();
        loadmoreList.clear();
        loadmoreList.addAll(RestaurantModel.getMoreRestaurant());
        seemoreRestaurantListAdapter.setLoaded();
        tvSearchType.setText(mActivity.getResources().getString(R.string.all_restaurant));
        loadData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Profile must implement ActivityListener");
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.mActivity = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        init();
        AppController.getAppComponent().inject(this);

        if (view != null) {
            isStop=false;
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.inject(this, view);
            handler = new Handler();
            getRestaurantList(false);
            initView();
            //initBanner();
            initRecyclerView();
            clearFilter();

            seemoreRestaurantListAdapter = new SeeMoreListAdapter(mActivity, loadmoreList, seemore_list);
            seemore_list.setAdapter(seemoreRestaurantListAdapter);
            loadMore();
        }

        return view;

    }

    private void init() {
        if (listener == null) return;

        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (MainActivity) getActivity();
    }


    private void initView() {

        tvUnder = view.findViewById(R.id.tvUnder);
        list_twenty = view.findViewById(R.id.list_twenty);
        popular_list = view.findViewById(R.id.popular_list);
        newtogofer_list = view.findViewById(R.id.newtogofer_list);
        more_rest_list = view.findViewById(R.id.more_rest_list);
        favorite_list = view.findViewById(R.id.favorite_list);

        seemore_list = view.findViewById(R.id.seemorelist);
        rltLocationText = view.findViewById(R.id.rltLocationText);
        rltLoadMoreTitle = view.findViewById(R.id.rltLoadMoreTitle);
        rltFilter = view.findViewById(R.id.rltFilter);

        rltWholeview = view.findViewById(R.id.rltWholeview);
        rltRestaurantOffer = view.findViewById(R.id.rltRestaurantOffer);
        favorite_lay = view.findViewById(R.id.favorite_lay);
        under_lay = view.findViewById(R.id.under_lay);
        popular_lay = view.findViewById(R.id.popular_lay);
        new_restaurant_lay = view.findViewById(R.id.new_restaurant_lay);
        more_rest_lay = view.findViewById(R.id.more_rest_lay);
        rltEmptylayout = view.findViewById(R.id.rltEmptylayout);
        tvOrderType = view.findViewById(R.id.tvOrderType);
        location_placesearch = view.findViewById(R.id.location_placesearch);
        ivSorting = view.findViewById(R.id.ivSorting);

        rltRestaurantOffer.setVisibility(View.GONE);
        favorite_lay.setVisibility(View.GONE);
        under_lay.setVisibility(View.GONE);
        popular_lay.setVisibility(View.GONE);
        new_restaurant_lay.setVisibility(View.GONE);
        more_rest_lay.setVisibility(View.GONE);
        rltEmptylayout.setVisibility(View.GONE);

        dialog = commonMethods.getAlertDialog(mActivity);


        if (sessionManager.getOrderType() == 1) {
            tvOrderType.setText(sessionManager.getHomeScheduledDay());
        } else {
            sessionManager.setScheduleDate("");
            sessionManager.setScheduledDay("");
            sessionManager.setScheduleMin("");
            sessionManager.setAddedTime("");
            sessionManager.setDeliveredTime("");
            sessionManager.setHomeScheduledDay("");
        }
        location_placesearch.setText(sessionManager.getLocation());
        ellipsizeText(location_placesearch);
        /**
         * Change Location Search
         */
        rltLocationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.isRefreshed = true;
                Intent loc = new Intent(getActivity(), LocActivity.class);
                loc.putExtra("location", "edit");
                loc.putExtra("type", 2);
                startActivity(loc);
            }
        });

        ivSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });

    }

    private void showFilter() {
        FragmentManager fm = getFragmentManager();
        newFragment = FilterFragment.newInstance();

        newFragment.setCancelable(false);
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        newFragment.show(fm, "dialog");
        fm.executePendingTransactions();
        newFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //do whatever you want when dialog is dismissed
                newFragment.dismiss();

                int sort = sessionManager.getSort();
                String price = sessionManager.getPriceSort();
                String diet = sessionManager.getDietarySort();
                if (sort == 0 && TextUtils.isEmpty(price) && TextUtils.isEmpty(diet)) {
                    swipeContainer.setVisibility(View.VISIBLE);
                    seemore_list.setVisibility(View.GONE);
                    isMoreRestaurant = false;
                    rltEmptylayout.setVisibility(View.GONE);
                    rltFilter.setVisibility(View.GONE);
                } else {
                    dietaryIdList.clear();
                    dietaryNameList.clear();
                    lltFilterRow.removeAllViews();
                    moveFilter();
                    getFilter(true);
                    setDietary(-1, "");
                    addFilterView();

                }
            }
        });
    }

    /**
     * Set Filter to get Result From api
     */
    public void moveFilter() {
        type = 0;
        pageNo = 1;
        loadmoreList.clear();
        swipeContainer.setVisibility(View.GONE);
        seemore_list.setVisibility(View.VISIBLE);
        isMoreRestaurant=false;
        isSeeMore = true;
        loadFilterTitle();
    }

    private void initRecyclerView() {

        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getRestaurantList(true);
            }
        });

        /**
         * Colour Changes For SwipeRefresh
         */
        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        // for favorite list
        favorite_list.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager0 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        favorite_list.setLayoutManager(layoutManager0);
        favorite_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState > 0) {
                    swipeContainer.setEnabled(false);
                } else {
                    swipeContainer.setEnabled(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // for under 20 min
        list_twenty.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        list_twenty.setLayoutManager(layoutManager1);
        list_twenty.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState > 0) {
                    swipeContainer.setEnabled(false);
                } else {
                    swipeContainer.setEnabled(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // for popular
        popular_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        popular_list.setLayoutManager(layoutManager2);
        popular_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState > 0) {
                    swipeContainer.setEnabled(false);
                } else {
                    swipeContainer.setEnabled(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //for new to gofereats
        newtogofer_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        newtogofer_list.setLayoutManager(layoutManager3);
        newtogofer_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState > 0) {
                    swipeContainer.setEnabled(false);
                } else {
                    swipeContainer.setEnabled(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //for more Restaurant
        //more_rest_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        more_rest_list.setLayoutManager(layoutManager4);

        seemore_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        // seemore_list.setLayoutManager(layoutManager5);

        seemore_list.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    private void initBanner() {
        viewpager = view.findViewById(R.id.viewpager);

        if (getContext() != null)
            viewpager.setAdapter(new ViewPagerAdapter(getContext(), restaurantOfferList));

        CirclePageIndicator indicator = view.findViewById(R.id.circle_image);
        indicator.setViewPager(viewpager);

        final float density = mActivity.getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);
        indicator.setStrokeColor(ContextCompat.getColor(getActivity(), R.color.white));
        indicator.setFillColor(ContextCompat.getColor(getActivity(), R.color.white));
        indicator.setPageColor(ContextCompat.getColor(getActivity(), R.color.transparent));
        indicator.setStrokeWidth((float) 1.5);
    }

    public void ellipsizeText(CustomTextView tv) {
        int maxLength=20;
        /*if(getResources().getString(R.string.layout_direction).equalsIgnoreCase("1")){
        maxLength = 20;
        }*/
        if (tv.getText().length() > maxLength) {
            String currentText = tv.getText().toString();
            String ellipsizedText = currentText.substring(0, maxLength - 3) + "...";
            tv.setText(ellipsizedText);
        }
    }

    /**
     * Call API to get restaurant list
     */
    private void getRestaurantList(boolean isSwipe) {
        if (!isSwipe) {
            commonMethods.showProgressDialog(mActivity, customDialog);
        }
        apiService.home(sessionManager.getToken(), sessionManager.getOrderType()).enqueue(new RequestCallback(REQ_GET_HOME, this));
    }

    /**
     * Call API to get restaurant list
     */
    private void getFilter(boolean isLoad) {
        /**
         * For Favourites No Type Just Move to Favourites Page
         *
         * Type 5 --- ALL Restaurant
         * Type 4 --- Under x mins (where x is the Min time in Under Mins eg- Under 15 Minutes)
         * Type 3 --- Popular Near You
         * Type 2 --- New To App
         *
         * Type 0 --- For Sort Price And Dietary Filter
         */
        String time = "";
        if (type == 4) {
            time = RestaurantModel.getUnderTime();
        }

        int sort = sessionManager.getSort();
        String price = sessionManager.getPriceSort();
        String diet = sessionManager.getDietarySort();

        //loadmoreList.clear(); //The list for update recycle view

        if (!isMoreRestaurant && sort == 0 && TextUtils.isEmpty(price) && TextUtils.isEmpty(diet)) {
            filterClear();
        } else {
            if (type == 0) {
                if (isLoad) {
                    commonMethods.showProgressDialog(mActivity, customDialog);
                }
                apiService.filter(type, pageNo, sort, price, diet, sessionManager.getToken()).enqueue(new RequestCallback(REQ_FILTER, this));
            } else {
                apiService.filter(type, pageNo, time, sessionManager.getToken()).enqueue(new RequestCallback(REQ_FILTER, this));
            }

        }
    }

    /**
     * Get Success response from API
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        if (!jsonResp.isOnline()) {
            commonMethods.hideProgressDialog();
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(mActivity, dialog, data);
            return;
        }
        switch (jsonResp.getRequestCode()) {
            case REQ_GET_HOME:
                if (jsonResp.isSuccess()) {
                    onSuccessGetRestaurantList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.hideProgressDialog();
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }

                break;
            case REQ_FILTER:
                if (jsonResp.isSuccess()) {
                    onSuccessGetLoadmore(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    seemoreRestaurantListAdapter.setLoaded();
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }
                commonMethods.hideProgressDialog();
                break;
            default:
                commonMethods.hideProgressDialog();
                break;
        }
    }

    /**
     * Get Failure response from API
     */
    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
        }
    }

    private void onSuccessGetRestaurantList(JsonResponse jsonResponse) {
        RestaurantModel = gson.fromJson(jsonResponse.getStrResponse(), RestaurantModel.class);
        if (RestaurantModel != null) {
            updateView();
        }else{
            commonMethods.hideProgressDialog();
        }
    }

    private void onSuccessGetLoadmore(JsonResponse jsonResponse) {
        RestaurantModelMore = gson.fromJson(jsonResponse.getStrResponse(), RestaurantModel.class);
        if (RestaurantModelMore != null) {
            updateViewLoad();
        }
    }

    private void loadData() {
        pageNo = 1;
        isSeeMore = true;
        rltLocationText.setVisibility(View.INVISIBLE);
        rltLoadMoreTitle.setVisibility(View.VISIBLE);
        swipeContainer.setVisibility(View.GONE);
        seemore_list.setVisibility(View.VISIBLE);
        isMoreRestaurant = true;
        for (int i = 0; i < loadmoreList.size(); i++) {
            loadmoreList.get(i).setType("item");
        }

        seemoreRestaurantListAdapter.notifyDataChanged();
    }

    public void loadMore() {
        seemoreRestaurantListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (pageNo == 1 && type > 0) {
                    seemore_list.scrollToPosition(loadmoreList.size());
                }
                if (pageNo < totalpages) {
                    //add load view , so the adapter will check view_type and show progress bar at bottom
                    loadmoreList.add(new RestaurantListModel("load"));
                    //seemoreRestaurantListAdapter.notifyItemInserted(loadmoreList.size()-1);
                    seemore_list.post(new Runnable() {
                        public void run() {
                            seemoreRestaurantListAdapter.notifyItemInserted(loadmoreList.size() - 1);
                        }
                    });
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageNo++;
                            getFilter(false);
                        }
                    }, 2000);
                } else {
                    seemoreRestaurantListAdapter.setLoaded();
                }
            }
        });
    }

    private void updateViewLoad() {
        if (RestaurantModelMore.getFilter() != null && (RestaurantModelMore.getFilter().size() > 0)) {
            int last = 0;
            rltEmptylayout.setVisibility(View.GONE);
            seemore_list.setVisibility(View.VISIBLE);
            isMoreRestaurant = true;
            if (loadmoreList.size() > 0) {
                loadmoreList.remove(loadmoreList.size() - 1);
                //seemoreRestaurantListAdapter.notifyDataChanged();
                seemoreRestaurantListAdapter.notifyItemRemoved(loadmoreList.size());
                if (loadmoreList.size() > 0) {
                    last = loadmoreList.size() - 1;
                } else {
                    last = 0;
                }
            }
            totalpages = RestaurantModelMore.getPageCount();


            loadmoreList.addAll(RestaurantModelMore.getFilter());

            for (int i = last; i < loadmoreList.size(); i++) {
                loadmoreList.get(i).setType("item");
            }
            seemoreRestaurantListAdapter.setLoaded();
            seemoreRestaurantListAdapter.notifyDataChanged();
        } else {

            if (loadmoreList.size() > 0) {
                loadmoreList.remove(loadmoreList.size() - 1);
                //seemoreRestaurantListAdapter.notifyDataChanged();
                seemoreRestaurantListAdapter.notifyItemRemoved(loadmoreList.size());
                seemoreRestaurantListAdapter.setLoaded();
            } else {
                rltEmptylayout.setVisibility(View.VISIBLE);
                seemore_list.setVisibility(View.GONE);
                isMoreRestaurant = false;
            }
        }
    }

    private void updateView() {
        swipeContainer.setRefreshing(false);
        System.out.println("Currency Symbol "+RestaurantModel.getDefaultCurrencySymbol());

        if(RestaurantModel.getDefaultCurrencySymbol()!=null )
        {
            sessionManager.setCurrencySymbol(RestaurantModel.getDefaultCurrencySymbol());
        }
        if (RestaurantModel.getDietaryListModel() != null && RestaurantModel.getDietaryListModel().size() > 0) {
            Gson gson = new Gson();
            String json = gson.toJson(RestaurantModel);
            sessionManager.setDietaryList(json);
        }

        // More Restaurant List update
        if (RestaurantModel.getMoreRestaurant() != null && (RestaurantModel.getMoreRestaurant().size() > 0)) {
            moreRestaurantList.clear();
            more_rest_lay.setVisibility(View.VISIBLE);
            moreRestaurantList.addAll(RestaurantModel.getMoreRestaurant());
            moreRestaurantListAdapter = new MoreRestaurantListAdapter(mActivity, moreRestaurantList);
            more_rest_list.setAdapter(moreRestaurantListAdapter);
        } else {
            more_rest_lay.setVisibility(View.GONE);
        }

        // Favorite Restaurant List update
        if (RestaurantModel.getFavouriteRestaurant() != null && (RestaurantModel.getFavouriteRestaurant().size() > 0)) {
            favorite_lay.setVisibility(View.VISIBLE);
            favoriteRestaurantList.clear();
            favoriteRestaurantList.addAll(RestaurantModel.getFavouriteRestaurant());
            favoriteListAdapter = new FavoriteListAdapter(mActivity, favoriteRestaurantList);
            favoriteListAdapter.setOnItemClickListener(new FavoriteListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int id, int positionz) {
                    totalpages = RestaurantModel.getFavouriteTotalPage();
                    MainActivity.isRefreshed = true;
                    Intent myFavourites = new Intent(mActivity, FavouritesActivity.class);
                    startActivity(myFavourites);

                }
            });
            favorite_list.setAdapter(favoriteListAdapter);
        } else {
            favorite_lay.setVisibility(View.GONE);
        }

        // Popular Restaurant List update
        if (RestaurantModel.getPopularRestaurant() != null && (RestaurantModel.getPopularRestaurant().size() > 0)) {
            popularRestaurantList.clear();
            popular_lay.setVisibility(View.VISIBLE);
            popularRestaurantList.addAll(RestaurantModel.getPopularRestaurant());
            popularListAdapter = new PopularListAdapter(mActivity, popularRestaurantList);
            popularListAdapter.setOnItemClickListener(new PopularListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int id, int positionz) {
                    totalpages = RestaurantModel.getPopularTotalPage();
                    type = 3;
                    clearFilter();
                    loadmoreList.clear();
                    loadmoreList.addAll(RestaurantModel.getPopularRestaurant());
                    seemoreRestaurantListAdapter.setLoaded();
                    tvSearchType.setText(mActivity.getResources().getString(R.string.popular_near_you));
                    loadData();

                }
            });
            popular_list.setAdapter(popularListAdapter);
        } else {
            popular_lay.setVisibility(View.GONE);
        }

        // New Restaurant List update
        if (RestaurantModel.getNewRestaurant() != null && (RestaurantModel.getNewRestaurant().size() > 0)) {
            newRestaurantList.clear();
            new_restaurant_lay.setVisibility(View.VISIBLE);
            newRestaurantList.addAll(RestaurantModel.getNewRestaurant());
            newRestaurantListAdapter = new NewRestaurantListAdapter(mActivity, newRestaurantList);
            newRestaurantListAdapter.setOnItemClickListener(new NewRestaurantListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int id, int positionz) {
                    totalpages = RestaurantModel.getNewTotalPage();
                    type = 2;
                    clearFilter();
                    loadmoreList.clear();
                    loadmoreList.addAll(RestaurantModel.getNewRestaurant());
                    seemoreRestaurantListAdapter.setLoaded();
                    tvSearchType.setText(mActivity.getResources().getString(R.string.new_to_gofer_eats));
                    loadData();
                }
            });
            newtogofer_list.setAdapter(newRestaurantListAdapter);
        } else {
            new_restaurant_lay.setVisibility(View.GONE);
        }

        // Under Restaurant List update
        if (RestaurantModel.getUnderRestaurant() != null && (RestaurantModel.getUnderRestaurant().size() > 0)) {
            underRestaurantList.clear();
            under_lay.setVisibility(View.VISIBLE);
            tvUnder.setText(mActivity.getResources().getString(R.string.under) + " " + RestaurantModel.getUnderTime() + " " + mActivity.getResources().getString(R.string.minutes));
            underRestaurantList.addAll(RestaurantModel.getUnderRestaurant());
            underRestaurantListAdapter = new UnderListAdapter(mActivity, underRestaurantList);
            underRestaurantListAdapter.setOnItemClickListener(new UnderListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int id, int positionz) {
                    totalpages = RestaurantModel.getUnderTotalPage();
                    type = 4;
                    clearFilter();
                    loadmoreList.clear();
                    loadmoreList.addAll(RestaurantModel.getUnderRestaurant());
                    seemoreRestaurantListAdapter.setLoaded();
                    tvSearchType.setText(mActivity.getResources().getString(R.string.under) + " " + RestaurantModel.getUnderTime() + " " + mActivity.getResources().getString(R.string.minutes));
                    loadData();
                }
            });

            list_twenty.setAdapter(underRestaurantListAdapter);
        } else {
            under_lay.setVisibility(View.GONE);
        }

        // Restaurant Offer (Banner)List update
        if (RestaurantModel.getRestaurantOffer() != null && (RestaurantModel.getRestaurantOffer().size() > 0)) {
            restaurantOfferList.clear();
            restaurantOfferList.addAll(RestaurantModel.getRestaurantOffer());
        } else {
            rltRestaurantOffer.setVisibility(View.GONE);
        }

        //Empty If there is no restaurant
        if (RestaurantModel.getUnderRestaurant().isEmpty() && RestaurantModel.getNewRestaurant().isEmpty() && RestaurantModel.getPopularRestaurant().isEmpty() && RestaurantModel.getFavouriteRestaurant().isEmpty() && RestaurantModel.getMoreRestaurant().isEmpty() && RestaurantModel.getRestaurantOffer().isEmpty()) {
            rltEmptylayout.setVisibility(View.VISIBLE);
        } else {
            rltEmptylayout.setVisibility(View.GONE);
        }
        sessionManager.setWalletAmount(RestaurantModel.getWalletamount());

        if (sessionManager.getIsFirst() && RestaurantModel.getCartDetails().getName() != null) {
            showClearDialog(RestaurantModel);
        }else{
            commonMethods.hideProgressDialog();
        }
        tvGettingOrderDetail.setVisibility(View.GONE);


    }

    public void clearFilter() {
        sessionManager.setSort(0);
        sessionManager.setPriceSort("");
        sessionManager.setDietarySort("");
        sessionManager.setDietaryName("");
        //sessionManager.setCurrencySymbol("");
    }

    public void loadFilterTitle() {
        int sort = sessionManager.getSort();
        String price = sessionManager.getPriceSort();
        String diet = sessionManager.getDietarySort();
        String filter = "";

        rltFilter.setVisibility(View.VISIBLE);
        if (sort == 1) {
            filter =mActivity.getResources().getString(R.string.most_popular);
            if (lltFilterRow.findViewById(R.id.tvMostpopular) == null)
                lltFilterRow.addView(tvMostpopular);
            tvMostpopular.setVisibility(View.VISIBLE);
            tvSearchRating.setVisibility(View.GONE);
            tvDeliveryTime.setVisibility(View.GONE);
        } else if (sort == 2) {
            filter = mActivity.getResources().getString(R.string.rating);
            if (lltFilterRow.findViewById(R.id.tvSearchRating) == null)
                lltFilterRow.addView(tvSearchRating);
            tvMostpopular.setVisibility(View.GONE);
            tvSearchRating.setVisibility(View.VISIBLE);
            tvDeliveryTime.setVisibility(View.GONE);


        } else if (sort == 3) {
            filter = mActivity.getResources().getString(R.string.delivery_time);
            if (lltFilterRow.findViewById(R.id.tvDeliveryTime) == null)
                lltFilterRow.addView(tvDeliveryTime);
            tvMostpopular.setVisibility(View.GONE);
            tvSearchRating.setVisibility(View.GONE);
            tvDeliveryTime.setVisibility(View.VISIBLE);
        } else if (sort == 0) {
            tvMostpopular.setVisibility(View.GONE);
            tvSearchRating.setVisibility(View.GONE);
            tvDeliveryTime.setVisibility(View.GONE);
        }

        if (price.contains("1")) {
            if (lltFilterRow.findViewById(R.id.tvPrice1) == null) lltFilterRow.addView(tvPrice1);
            tvPrice1.setVisibility(View.VISIBLE);
            tvPrice1.setText(sessionManager.getCurrencySymbol());
            if (TextUtils.isEmpty(filter)) filter = sessionManager.getCurrencySymbol();
            else filter = filter + "," + sessionManager.getCurrencySymbol();
        } else {
            tvPrice1.setVisibility(View.GONE);
        }

        if (price.contains("2")) {
            if (lltFilterRow.findViewById(R.id.tvPrice2) == null) lltFilterRow.addView(tvPrice2);
            tvPrice2.setVisibility(View.VISIBLE);
            tvPrice2.setText(sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol());
            if (TextUtils.isEmpty(filter))
                filter = sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol();
            else
                filter = filter + "," + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol();
        } else {
            tvPrice2.setVisibility(View.GONE);
        }

        if (price.contains("3")) {
            if (lltFilterRow.findViewById(R.id.tvPrice3) == null) lltFilterRow.addView(tvPrice3);
            tvPrice3.setVisibility(View.VISIBLE);
            tvPrice3.setText(sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol());
            if (TextUtils.isEmpty(filter))
                filter = sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol();
            else
                filter = filter + "," + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol();
        } else {
            tvPrice3.setVisibility(View.GONE);
        }

        if (price.contains("4")) {
            if (lltFilterRow.findViewById(R.id.tvPrice4) == null) lltFilterRow.addView(tvPrice4);
            tvPrice4.setVisibility(View.VISIBLE);
            tvPrice4.setText(sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol());
            if (TextUtils.isEmpty(filter))
                filter = sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol();
            else
                filter = filter + "," + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol() + sessionManager.getCurrencySymbol();
        } else {
            tvPrice4.setVisibility(View.GONE);
        }
    }

    /**
     * Dialog to Clear or Continue
     */
    private void showClearDialog(RestaurantModel restaurantModel) {
        /*FragmentManager fragment=getFragmentManager();
        System.out.println("Fragment Manager "+ fragment);
        System.out.println("Fragment Activity Manager" + getActivity().getFragmentManager());*/
        bottomSheetDialog = new BottomSheetDialog(mActivity);
        bottomSheetDialog.setContentView(R.layout.clear_cart_bottomsheet);
        dialogInject = new DialogInject();

        // 5. We bind the elements of the included layouts.

        ButterKnife.inject(dialogInject, bottomSheetDialog);
        //if(fragment.equals(getActivity().getSupportFragmentManager()))
        dialogInject.clickActions(restaurantModel.getCartDetails().getName(), restaurantModel.getCartDetails().getBannerList().getSmallest());
        if(!isStop)
        bottomSheetDialog.show();
        sessionManager.setIsFirst(false);
        commonMethods.hideProgressDialog();
    }

    public void filterClear() {
        commonMethods.hideProgressDialog();
        type = 0;
        pageNo = 1;
        isSeeMore = false;
        swipeContainer.setVisibility(View.VISIBLE);
        seemore_list.setVisibility(View.GONE);
        isMoreRestaurant = false;
        rltEmptylayout.setVisibility(View.GONE);
        rltFilter.setVisibility(View.GONE);
    }

    public void addFilterView() {

        // Layout inflater
        LayoutInflater layoutInflater = getLayoutInflater();
        View view;

        for (int i = 0; i < dietaryIdList.size(); i++) {
            // Add the text layout to the parent layout
            view = layoutInflater.inflate(R.layout.row_filter_name, lltFilterRow, false);

            final CustomTextView tvFilterName = view.findViewById(R.id.tvFilterName);
            // In order to get the view we have to use the new view with text_layout in it
            tvFilterName.setText(dietaryNameList.get(i));
            tvFilterName.setId(Integer.valueOf(dietaryIdList.get(i)));

            if (lltFilterRow != null) {
                tvFilterName.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        //Log.i("TAG", "The index is" + index);
                        setDietary(tvFilterName.getId(), tvFilterName.getText().toString());
                        lltFilterRow.removeView(tvFilterName);
                        moveFilter();
                        getFilter(true);
                    }
                });

                if (lltFilterRow.findViewById(Integer.valueOf(dietaryIdList.get(i))) == null) {
                    // Add the text view to the parent layout
                    lltFilterRow.addView(tvFilterName);
                }
            }
        }
    }

    public void setDietary(int id, String name) {
        dietaryIdList.clear();
        dietaryNameList.clear();

        if (sessionManager.getDietarySort() != null && !TextUtils.isEmpty(sessionManager.getDietarySort())) {
            String[] dietaryId = sessionManager.getDietarySort().split(",");
            String[] dietaryName = sessionManager.getDietaryName().split(",");
            for (int i = 0; i < dietaryId.length; i++) {
                if (!TextUtils.isEmpty(dietaryId[i])) addRemove(dietaryId[i], dietaryName[i], true);
            }
        }

        if (id >= 0) {
            addRemove(String.valueOf(id), name, false);
        }

        String dietId = null;
        for (String s : dietaryIdList) {
            if (dietId != null) {
                dietId = dietId + "," + s;
            } else {
                dietId = s;
            }
        }

        String dietName = null;
        for (String s : dietaryNameList) {
            if (dietName != null) {
                dietName = dietName + "," + s;
            } else {
                dietName = s;
            }
        }


        sessionManager.setDietarySort(dietId);
        sessionManager.setDietaryName(dietName);

    }

    private void addRemove(String id, String name, boolean isAddOnly) {
        if (!dietaryIdList.contains(id)) {
            dietaryIdList.add(id);
        } else {
            if (!isAddOnly) dietaryIdList.remove(id);
        }

        if (!dietaryNameList.contains(name)) {
            dietaryNameList.add(name);
        } else {
            if (!isAddOnly) dietaryNameList.remove(name);
        }
    }

    /**
     * Annotation  using ButterKnife lib to Injection and OnClick for Clear and Continue the Cart
     **/
    public class DialogInject implements ServiceListener {

        public @InjectView(R.id.civRestImage)
        CircleImageView civRestImage;
        public @InjectView(R.id.tvNameOfTheRestaurant)
        CustomTextView tvNameOfTheRestaurant;
        public @InjectView(R.id.btnContinue)
        CustomButton btnContinue;
        public @InjectView(R.id.btnClearCart)
        CustomButton btnClearCart;


        @OnClick(R.id.btnClearCart)
        public void clearCart() {
            bottomSheetDialog.dismiss();
            if(!isStop)
            apiService.clearAllCart(sessionManager.getToken()).enqueue(new RequestCallback(this));

        }

        @OnClick(R.id.btnContinue)
        public void goToViewCart() {
            MainActivity.isRefreshed = true;
            bottomSheetDialog.dismiss();
            System.out.println("MActivity Set"+ mActivity);
            Activity activity = mActivity;
            System.out.println("GetActivity received"+activity);
            System.out.println("ADDED value "+isAdded());
            if (!isStop&&mActivity != null) {
                Intent view = new Intent(mActivity, PlaceOrderActivity.class);
                startActivity(view);
            }

        }

        public void clickActions(String name, String image) {
            Glide.with(mActivity).load(image).into(civRestImage);
            tvNameOfTheRestaurant.setText(mActivity.getResources().getString(R.string.you_still_have_items) + "\n" + name);
        }

        @Override
        public void onSuccess(JsonResponse jsonResp, String data) {
            if (!jsonResp.isOnline()) {
                if (!TextUtils.isEmpty(data)) commonMethods.showMessage(mActivity, dialog, data);
                return;
            }

            if (jsonResp.isSuccess()) {
                sessionManager.setIsFirst(false);
                sessionManager.setCartCount(0);
                sessionManager.setCartAmount("");
            } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
            }
        }

        @Override
        public void onFailure(JsonResponse jsonResp, String data) {
            commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
        }

    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {


        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isStop=true;
    }
}