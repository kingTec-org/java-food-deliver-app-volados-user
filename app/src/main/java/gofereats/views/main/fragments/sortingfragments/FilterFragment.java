package gofereats.views.main.fragments.sortingfragments;


/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.fragments.sortingfragments
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/


/*****************************************************************
 Filter Fragment used in home fragment for filters
 ****************************************************************/


import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.obs.CustomButton;
import com.obs.CustomTextView;

import javax.inject.Inject;

import gofereats.R;
import gofereats.adapters.main.SortingViewPagerAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.views.main.fragments.HomeFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends DialogFragment {

    public @Inject
    SessionManager sessionManager;
    public LinearLayout linearLayoutOne;
    public LinearLayout linearLayout2;
    public LinearLayout linearLayout3;
    public ImageView dot1;
    public ImageView dot2;
    public ImageView dot3;
    public TextView tvSort;
    public TextView tvPrice;
    public TextView tvDiet;
    public Dialog d;
    public TabLayout tabLayout;
    private View view;

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }


    /*@Override public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            Window window = d.getWindow();
            window.setGravity(Gravity.TOP);

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            //d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppController.getAppComponent().inject(this);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.sorting_filter_layout, container, false);
            final ViewPager viewPager = view.findViewById(R.id.viewpager_filter);

            ImageView arrow = view.findViewById(R.id.arrow);

            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    d.dismiss();
                }
            });

            CustomTextView tvReset = view.findViewById(R.id.tvReset);

            tvReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sessionManager.setPriceSort("");
                    sessionManager.setDietarySort("");
                    sessionManager.setDietaryName("");
                    sessionManager.setSort(0);
                    d.dismiss();
                }
            });

            CustomButton btnDone = view.findViewById(R.id.btnDone);

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sessionManager.getPriceSort().equals("") && sessionManager.getDietarySort().equals("") && sessionManager.getSort() == 0) {
                        HomeFragment.showhome();
                    }
                    d.dismiss();
                }
            });

            /* Use childFragmentManager here provided from the PagerDialog */
            SortingViewPagerAdapter mAdapter = new SortingViewPagerAdapter(getChildFragmentManager());
            viewPager.setAdapter(mAdapter);

            tabLayout = view.findViewById(R.id.sorting_tabLayout);
            tabLayout.setupWithViewPager(viewPager);

            setupViewPager(viewPager);
            View headerView = inflater.inflate(R.layout.custom_tab, container, false);

            linearLayoutOne = headerView.findViewById(R.id.ll);
            linearLayout2 = headerView.findViewById(R.id.ll2);
            linearLayout3 = headerView.findViewById(R.id.ll3);

            dot1 = headerView.findViewById(R.id.dot1);
            dot2 = headerView.findViewById(R.id.dot2);
            dot3 = headerView.findViewById(R.id.dot3);

            tvSort = (CustomTextView) headerView.findViewById(R.id.tvtab1);
            tvPrice = (CustomTextView) headerView.findViewById(R.id.tvtab2);
            tvDiet = (CustomTextView) headerView.findViewById(R.id.tvtab3);
            if("1".equalsIgnoreCase(getResources().getString(R.string.layout_direction)))
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,0);
                tvDiet.setLayoutParams(params);
            }


            tabLayout.getTabAt(0).setCustomView(linearLayoutOne);
            tabLayout.getTabAt(1).setCustomView(linearLayout2);
            //tabLayout.getTabAt(2).setCustomView(linearLayout3);


            if (!sessionManager.getPriceSort().equals("")) {
                dot2.setVisibility(View.VISIBLE);
            } else {
                dot2.setVisibility(View.GONE);
            }
            if (!sessionManager.getDietarySort().equals("")) {

                dot3.setVisibility(View.VISIBLE);
            } else {
                dot3.setVisibility(View.GONE);
            }
            if (sessionManager.getSort() == 0) {

                dot1.setVisibility(View.GONE);
            } else {
                dot1.setVisibility(View.VISIBLE);
            }

            tvSort.setTextColor(getResources().getColor(R.color.total_text_colur));
            tvPrice.setTextColor(getResources().getColor(R.color.text_light_gray));
            tvDiet.setTextColor(getResources().getColor(R.color.text_light_gray));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        tvSort.setTextColor(getResources().getColor(R.color.total_text_colur));
                        tvPrice.setTextColor(getResources().getColor(R.color.text_light_gray));
                        tvDiet.setTextColor(getResources().getColor(R.color.text_light_gray));
                    } else if (tab.getPosition() == 1) {
                        tvSort.setTextColor(getResources().getColor(R.color.text_light_gray));
                        tvPrice.setTextColor(getResources().getColor(R.color.total_text_colur));
                        tvDiet.setTextColor(getResources().getColor(R.color.text_light_gray));
                    } else if (tab.getPosition() == 2) {
                        tvSort.setTextColor(getResources().getColor(R.color.text_light_gray));
                        tvPrice.setTextColor(getResources().getColor(R.color.text_light_gray));
                        tvDiet.setTextColor(getResources().getColor(R.color.total_text_colur));
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    System.out.println("onTabUnselected");
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    System.out.println("onTabReselected");
                }
            });

        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public void onStart() {
        super.onStart();
        d = getDialog();
        if (d != null) {

            Window window = d.getWindow();
            window.setGravity(Gravity.TOP);
            window.setBackgroundDrawableResource(R.color.white);

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * set up filter fragments to a view page
     *
     * @param viewPager view has to be passed
     */


    private void setupViewPager(ViewPager viewPager) {
        SortingViewPagerAdapter adapter = new SortingViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new SortFragment(), getResources().getString(R.string.sort));
        adapter.addFragment(new PriceFragment(), getResources().getString(R.string.price));
        //adapter.addFragment(new DietaryFragment(), getResources().getString(R.string.dietary));
        viewPager.setAdapter(adapter);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
