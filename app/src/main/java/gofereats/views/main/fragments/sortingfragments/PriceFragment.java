package gofereats.views.main.fragments.sortingfragments;


/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.fragments.sortingfragments
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/


/*****************************************************************
 Price Fragment used in home fragment for filters
 ****************************************************************/


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class PriceFragment extends Fragment {

    public @Inject
    SessionManager sessionManager;
    public FilterFragment parentFrag;
    public TextView dollar1;
    public TextView dollar2;
    public TextView dollar3;
    public TextView dollar4;
    public int click1 = 0;
    public int click2 = 0;
    public int click3 = 0;
    public int click4 = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppController.getAppComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_price, container, false);

        dollar1 = view.findViewById(R.id.dollar1);
        dollar2 = view.findViewById(R.id.dollar2);
        dollar3 = view.findViewById(R.id.dollar3);
        dollar4 = view.findViewById(R.id.dollar4);
        dollar1.setText(sessionManager.getCurrencySymbol());
        dollar2.setText(sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol());
        dollar3.setText(sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol());
        dollar4.setText(sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol()+sessionManager.getCurrencySymbol());
        changeButton(dollar1, click1);
        changeButton(dollar2, click2);
        changeButton(dollar3, click3);
        changeButton(dollar4, click4);
        if (!TextUtils.isEmpty(sessionManager.getPriceSort())) {
            if (sessionManager.getPriceSort().contains("1")) {
                click1 = 1;
                changeButton(dollar1, click1);
            }

            if (sessionManager.getPriceSort().contains("2")) {
                click2 = 1;
                changeButton(dollar2, click2);
            }

            if (sessionManager.getPriceSort().contains("3")) {
                click3 = 1;
                changeButton(dollar3, click3);
            }

            if (sessionManager.getPriceSort().contains("4")) {
                click4 = 1;
                changeButton(dollar4, click4);
            }
        }

        /**
         * Click Action For type - 1
         */

        parentFrag = ((FilterFragment) PriceFragment.this.getParentFragment());


        dollar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (click1 == 1) {
                    click1 = 0;
                } else if (click1 == 0) {
                    click1 = 1;
                }
                changeButton(dollar1, click1);
                setPrice();


            }
        });

        /**
         * Click Action For type - 2
         */

        dollar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (click2 == 1) {
                    click2 = 0;
                } else if (click2 == 0) {
                    click2 = 1;
                }
                changeButton(dollar2, click2);
                setPrice();


            }
        });

        /**
         * Click Action For type - 3
         */
        dollar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (click3 == 1) {
                    click3 = 0;
                } else if (click3 == 0) {
                    click3 = 1;
                }
                changeButton(dollar3, click3);
                setPrice();


            }
        });

        /**
         * Click Action For type - 4
         */
        dollar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (click4 == 1) {
                    click4 = 0;
                } else if (click4 == 0) {
                    click4 = 1;
                }
                changeButton(dollar4, click4);
                setPrice();
                /*FilterFragment ff = FilterFragment.newInstance();
                ImageView vp=(ImageView) getActivity().findViewById(R.id.dot2);
                vp.setVisibility(View.VISIBLE);
                ff.highLight();*/


            }
        });
        return view;
    }

    /**
     * To Add Price filter method for passing in the api for filter search
     */


    public void setPrice() {
        String price = "";
        if (click1 == 1) {
            if (TextUtils.isEmpty(price)) price = "1";
            else price = price + "," + "1";
        }

        if (click2 == 1) {
            if (TextUtils.isEmpty(price)) price = "2";
            else price = price + "," + "2";
        }

        if (click3 == 1) {
            if (TextUtils.isEmpty(price)) price = "3";
            else price = price + "," + "3";
        }
        if (click4 == 1) {
            if (TextUtils.isEmpty(price)) price = "4";
            else price = price + "," + "4";
        }

        sessionManager.setPriceSort(price);

        if (!sessionManager.getPriceSort().equals("")) {
            parentFrag.dot2.setVisibility(View.VISIBLE);
        } else {
            parentFrag.dot2.setVisibility(View.GONE);
        }

    }

    /**
     * To Changing Text view background color selection
     *
     * @param view highlighting views based on selection
     * @param type if already selected type will be 1 else type will be 0
     */


    public void changeButton(TextView view, int type) {
        if (type == 1) {
            view.setTextColor(getResources().getColor(R.color.white));
            view.setBackground(getResources().getDrawable(R.drawable.round_boarder_black));
        } else {
            view.setTextColor(getResources().getColor(R.color.black));
            view.setBackground(getResources().getDrawable(R.drawable.round_boarder_white));
        }
    }

}
