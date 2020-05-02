package gofereats.views.main.alertdialog;


/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.alerdialog
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import gofereats.R;
import gofereats.configs.AppController;


/*****************************************************************
 Dialog Activity  used for filter
 ****************************************************************/


public class DialogActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        AppController.getAppComponent().inject(this);

        viewPager = findViewById(R.id.viewpager_sort);
        // setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.sorting_tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Setup tab
     */

   /* private void setupViewPager(ViewPager viewPager) {
       *//* SortingViewPagerAdapter adapter = new SortingViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SortFragment(),getString(R.string.sort));
        adapter.addFragment(new PriceFragment(),getString(R.string.price));
        adapter.addFragment(new DietaryFragment(),getString(R.string.dietary));
        viewPager.setAdapter(adapter);*//*
    }*/
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
}
