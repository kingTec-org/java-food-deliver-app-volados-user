package gofereats.views.main.fragments.sortingfragments;

/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.fragments.sortingfragments
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 */

/*****************************************************************
 Dietary Fragment used in filter fragment for filters
 ****************************************************************/


public class DietaryFragmentold extends Fragment {

    public @Inject
    SessionManager sessionManager;

    public FilterFragment parentFrag;

    public RelativeLayout veg_layout;
    public RelativeLayout vegan_layout;
    public RelativeLayout gluten_layout;
    public ImageView check_veg;
    public ImageView check_vegan;
    public ImageView check_gluten;

    public int click1 = 0;
    public int click2 = 0;
    public int click3 = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppController.getAppComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_dietary, container, false);

        veg_layout = view.findViewById(R.id.veg_layout);
        vegan_layout = view.findViewById(R.id.vegan_layout);
        gluten_layout = view.findViewById(R.id.gluten_layout);

        check_veg = view.findViewById(R.id.check_veg);
        check_vegan = view.findViewById(R.id.check_vegan);
        check_gluten = view.findViewById(R.id.check_gluten);

        check_veg.setVisibility(View.GONE);
        check_vegan.setVisibility(View.GONE);
        check_gluten.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(sessionManager.getDietarySort())) {

            System.out.println("Diet get " + sessionManager.getDietarySort());
            if (sessionManager.getDietarySort().contains("0")) {
                click1 = 1;
                check_veg.setVisibility(View.VISIBLE);
                System.out.println("Diet get contains 0" + sessionManager.getDietarySort());
            }
            if (sessionManager.getDietarySort().contains("1")) {
                click2 = 1;
                check_vegan.setVisibility(View.VISIBLE);
                System.out.println("Diet get contains 1" + sessionManager.getDietarySort());
            }
            if (sessionManager.getDietarySort().contains("2")) {
                click3 = 1;
                check_gluten.setVisibility(View.VISIBLE);
                System.out.println("Diet get contains 2" + sessionManager.getDietarySort());
            }
        }

        /**
         * Select Veg
         */
        parentFrag = ((FilterFragment) DietaryFragmentold.this.getParentFragment());


        veg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (click1 == 1) {
                    check_veg.setVisibility(View.GONE);
                    click1 = 0;
                } else if (click1 == 0) {
                    click1 = 1;
                    check_veg.setVisibility(View.VISIBLE);
                }
                setDietary();
            }
        });

        /**
         * Select Vegan
         */
        vegan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click2 == 1) {
                    check_vegan.setVisibility(View.GONE);
                    click2 = 0;
                } else if (click2 == 0) {
                    click2 = 1;
                    check_vegan.setVisibility(View.VISIBLE);
                }
                setDietary();
            }
        });

        /**
         * Select gluten
         */
        gluten_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (click3 == 1) {
                    check_gluten.setVisibility(View.GONE);
                    click3 = 0;
                } else if (click3 == 0) {
                    click3 = 1;
                    check_gluten.setVisibility(View.VISIBLE);
                }
                setDietary();

            }
        });
        return view;
    }

    /**
     * To Add newly added dietary filter for api passing
     */


    public void setDietary() {
        String diet = "";
        if (click1 == 1) {
            if (TextUtils.isEmpty(diet)) diet = "0";
            else diet = diet + "," + "0";
        }

        if (click2 == 1) {
            if (TextUtils.isEmpty(diet)) diet = "1";
            else diet = diet + "," + "1";
        }

        if (click3 == 1) {
            if (TextUtils.isEmpty(diet)) diet = "2";
            else diet = diet + "," + "2";
        }

        sessionManager.setDietarySort(diet);

        if (!sessionManager.getDietarySort().equals("")) {
            parentFrag.dot3.setVisibility(View.VISIBLE);
        } else {
            parentFrag.dot3.setVisibility(View.GONE);
        }

        System.out.println("Diet set " + sessionManager.getDietarySort());
    }

}
