package gofereats.adapters.infowindow;

/**
 * @package com.trioangle.gofereats
 * @subpackage adapters.infowindow
 * @category InfoWindowModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.obs.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.SessionManager;
import gofereats.datamodels.order.OrderModel;
import gofereats.views.main.fragments.orders.OrderFragment;

/**************************************************************
 Info Window for add location adapter
 ****************************************************************/

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    public @Inject
    SessionManager sessionManager;
    public List<OrderModel> trackingOrder = new ArrayList<OrderModel>();
    private Context context;
    private int postion = 0;

    public CustomInfoWindowGoogleMap(Context ctx, int position) {
        context = ctx;
        postion = position;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.marker_dialog, null);

        CustomTextView tv_address = view.findViewById(R.id.tvLocation);
        CustomTextView tvOrderID = view.findViewById(R.id.tvOrderID);

        OrderFragment orderFragment = new OrderFragment();

        trackingOrder = orderFragment.getUpcomingOrderDetailsList();
        System.out.println("LIST SIZE " + trackingOrder.size());

        InfoWindowModel infoWindowData = (InfoWindowModel) marker.getTag();

        if (infoWindowData.getType() == 1) {
            tv_address.setText(trackingOrder.get(postion).getName());
            tvOrderID.setText(String.valueOf(trackingOrder.get(postion).getOrderid()));
        } else {
            tv_address.setText("Your Location");
            tvOrderID.setVisibility(View.GONE);
        }

        return view;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
