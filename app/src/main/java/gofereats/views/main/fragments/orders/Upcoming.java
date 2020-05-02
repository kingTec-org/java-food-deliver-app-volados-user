package gofereats.views.main.fragments.orders;


/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.fragments.orders
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import gofereats.R;
import gofereats.adapters.main.UpcomingAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.order.OrderModel;
import gofereats.interfaces.ActivityListener;
import gofereats.interfaces.ApiService;
import gofereats.utils.CommonMethods;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;


/*****************************************************************
 Fragement set up for upcoming tab of orders
 ****************************************************************/


/**
 * A simple {@link Fragment} subclass.
 */
public class Upcoming extends Fragment {

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
    public RelativeLayout rltEmptylayout;
    public UpcomingAdapter upcomingAdapter;
    public RecyclerView upcoming_order_list;
    private ActivityListener listener;
    private MainActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Order must implement ActivityListener");
        }
    }

    /**
     * intialize view method
     */


    private void init() {
        if (listener == null) return;
        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (MainActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        ButterKnife.inject(mActivity);
        AppController.getAppComponent().inject(this);

        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        upcoming_order_list = view.findViewById(R.id.upcoming_order_list);
        rltEmptylayout = view.findViewById(R.id.rltEmptylayout);

        upcoming_order_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        upcoming_order_list.setLayoutManager(layoutManager);


        List<OrderModel> upcomingOrdersList = new ArrayList<OrderModel>();
        OrderFragment orderFragment = new OrderFragment();

        upcomingOrdersList = orderFragment.getUpcomingOrderDetailsList();

        if (upcomingOrdersList.size() == 0) {
            upcoming_order_list.setVisibility(View.GONE);
            rltEmptylayout.setVisibility(View.VISIBLE);
        } else {
            upcoming_order_list.setVisibility(View.VISIBLE);
            rltEmptylayout.setVisibility(View.GONE);
            upcomingAdapter = new UpcomingAdapter(getContext(), upcomingOrdersList);
            upcoming_order_list.setAdapter(upcomingAdapter);
        }


        return view;
    }


}