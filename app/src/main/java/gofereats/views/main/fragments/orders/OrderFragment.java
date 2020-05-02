package gofereats.views.main.fragments.orders;

/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.fragments.orders
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/


import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import gofereats.R;
import gofereats.adapters.main.OrderViewPagerAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.order.OrderModel;
import gofereats.datamodels.order.OrderResultModel;
import gofereats.interfaces.ActivityListener;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;

import static gofereats.utils.Enums.REQ_GET_ORDERSLIST;


/*****************************************************************
 Order Fragment used in Main Activity for viewing upcoming and history orders
 ****************************************************************/


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment implements TabLayout.OnTabSelectedListener, ServiceListener {

    public static ArrayList<OrderModel> pastOrder = new ArrayList<>();
    public static ArrayList<OrderModel> upcoming = new ArrayList<>();
    public static boolean isResumed = false;

    public SwipeRefreshLayout swipeContainer;

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

    public OrderResultModel orderResultModel;
    private FragmentManager childFragMang;
    private View view;
    private ActivityListener listener;
    private MainActivity mActivity = new MainActivity();
    private AlertDialog dialog;
    private ViewPager viewPager;

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Order must implement ActivityListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        init();
        ButterKnife.inject(mActivity);
        AppController.getAppComponent().inject(this);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_order, container, false);
            childFragMang = getChildFragmentManager();
            initView();
        }
        return view;
    }

    private void init() {
        if (listener == null) return;

        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (MainActivity) getActivity();
    }

    private void initView() {

        //View Pager
        viewPager = view.findViewById(R.id.order_viewpager);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getOrders(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        // Tab layout
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        dialog = commonMethods.getAlertDialog(mActivity);
        getOrders(true);

    }

    /**
     * Setup tab
     */

    /**
     * Setting up view pager
     *
     * @param viewPager need to passed for which fragments to be added
     */

    private void setupViewPager(ViewPager viewPager) {
        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(childFragMang);
        adapter.addFragment(new Upcoming(), getString(R.string.upcoming));
        adapter.addFragment(new PastOrders(), getString(R.string.history));
        viewPager.setAdapter(adapter);
        if (MainActivity.cancelled) {
            viewPager.setCurrentItem(1);
            MainActivity.cancelled = false;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        System.out.println("onTabUnselected");
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        System.out.println("onTabReselected");
    }


    @Override
    public void onResume() {
        super.onResume();
        MainActivity.isRefreshed = false;

        if (MainActivity.isOrder) {
            if (isResumed) getOrders(true);
            System.out.println("IS From Resume ");
        }
    }

    /**
     * Get Order History and Upcoming Orders
     */
    public void getOrders(boolean load) {

        if (load) {
            MainActivity.isOrder = false;
            commonMethods.showProgressDialog(mActivity, customDialog);
        }
        apiService.orderList(sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_ORDERSLIST, this));
    }


    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(mActivity, dialog, data);
            return;

        }
        switch (jsonResp.getRequestCode()) {
            case REQ_GET_ORDERSLIST:
                if (jsonResp.isSuccess()) {
                    onSuccessGetOrderList(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
            commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
        }
    }

    private void onSuccessGetOrderList(JsonResponse jsonResp) {
        swipeContainer.setRefreshing(false);

        orderResultModel = gson.fromJson(jsonResp.getStrResponse(), OrderResultModel.class);
        if (orderResultModel.getOrderHistroyModel() != null) {
            pastOrder.clear();
            pastOrder.addAll(orderResultModel.getOrderHistroyModel());
        }

        if (orderResultModel.getOrderUpcomingModel() != null) {
            upcoming.clear();
            upcoming.addAll(orderResultModel.getOrderUpcomingModel());
        }

        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            setupViewPager(viewPager);
        }
    }

    public List<OrderModel> getUpcomingOrderDetailsList() {
        return upcoming;
    }

    public List<OrderModel> getPastOrderDetailsList() {
        return pastOrder;
    }

}