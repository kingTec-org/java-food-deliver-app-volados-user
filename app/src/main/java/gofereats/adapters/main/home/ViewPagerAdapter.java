package gofereats.adapters.main.home;

/**
 *
 * @package com.gofereats
 * @subpackage adapters.main
 * @category Viewpager adapter
 * @author Trioangle Product Team
 * @version 1.0
 */


/*************************************************************
 Promo Ads For GoferEats
 ************************************************************/

import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.home.RestaurantOfferModel;
import gofereats.utils.ImageUtils;
import gofereats.views.main.subviews.RestaurantDetailActivity;


public class ViewPagerAdapter extends PagerAdapter {

    public ArrayList<RestaurantOfferModel> restaurantOfferModels;
    public @Inject
    ImageUtils imageUtils;
    public @Inject
    SessionManager sessionManager;
    private LayoutInflater inflater;
    private Context context;

    /**
     * Popular List Adapter constructor
     *
     * @param context               context of activity it is used in
     * @param restaurantOfferModels restaurantOfferModels Array List
     */
    public ViewPagerAdapter(Context context, ArrayList<RestaurantOfferModel> restaurantOfferModels) {
        this.context = context;
        this.restaurantOfferModels = restaurantOfferModels;
        AppController.getAppComponent().inject(this);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return restaurantOfferModels.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.custom_promo_slider, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.promo_image);
        final CustomTextView title = imageLayout.findViewById(R.id.title);
        final CustomTextView description = imageLayout.findViewById(R.id.description);


        /*imageView.setImageResource(IMAGES.get(position));*/
        imageUtils.loadImage(context, imageView, restaurantOfferModels.get(position).getBannerList().getOriginal());
        title.setText(restaurantOfferModels.get(position).getTitle());
        description.setText(restaurantOfferModels.get(position).getDescription());

        imageLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //this will log the page number that was click
                sessionManager.setRestaurantId(restaurantOfferModels.get(position).getRestaurantId());
                Intent detail = new Intent(context, RestaurantDetailActivity.class);
                context.startActivity(detail);
            }
        });


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
