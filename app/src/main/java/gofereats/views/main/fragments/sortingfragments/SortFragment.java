package gofereats.views.main.fragments.sortingfragments;


/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.fragments.sortingfragments
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import gofereats.R;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.interfaces.ActivityListener;

/*****************************************************************
 Sort Fragment used in home fragment for filters
 ****************************************************************/


/**
 * A simple {@link Fragment} subclass.
 */
public class SortFragment extends Fragment {
    public @Inject
    SessionManager sessionManager;
    public FilterFragment parentFrag;
    public RelativeLayout recommended_layout;
    public RelativeLayout mostpopular_layout;
    public RelativeLayout rating_layout;
    public RelativeLayout delivery_layout;
    public ImageView check;
    public ImageView check_mostpopular;
    public ImageView check_rating;
    public ImageView check_delivery;
    private View view;
    private ActivityListener listener;


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        init();
        AppController.getAppComponent().inject(this);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_sort, container, false);
            initViews();
            resetingViews();
        }

        return view;
    }

    private void init() {
        if (listener == null) return;

    }


    /**
     * To intialize view
     */
    private void initViews() {
        recommended_layout = view.findViewById(R.id.recommended_layout);
        mostpopular_layout = view.findViewById(R.id.mostpopular_layout);
        rating_layout = view.findViewById(R.id.rating_layout);
        delivery_layout = view.findViewById(R.id.delivery_layout);

        check = view.findViewById(R.id.check);
        check_mostpopular = view.findViewById(R.id.check_mostpopular);
        check_rating = view.findViewById(R.id.check_rating);
        check_delivery = view.findViewById(R.id.check_delivery);
    }


    /**
     * Reseting view based on selection
     */

    private void resetingViews() {

        check.setVisibility(View.GONE);
        check_mostpopular.setVisibility(View.GONE);
        check_rating.setVisibility(View.GONE);
        check_delivery.setVisibility(View.GONE);

        if (sessionManager.getSort() == 0) {
            check.setVisibility(View.VISIBLE);
        } else if (sessionManager.getSort() == 1) {
            check_mostpopular.setVisibility(View.VISIBLE);
        } else if (sessionManager.getSort() == 2) {
            check_rating.setVisibility(View.VISIBLE);
        } else if (sessionManager.getSort() == 3) {
            check_delivery.setVisibility(View.VISIBLE);
        }


        parentFrag = ((FilterFragment) SortFragment.this.getParentFragment());

        mostpopular_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.setVisibility(View.GONE);
                check_mostpopular.setVisibility(View.VISIBLE);
                check_rating.setVisibility(View.GONE);
                check_delivery.setVisibility(View.GONE);
                sessionManager.setSort(1);
                parentFrag.dot1.setVisibility(View.VISIBLE);


            }
        });

        rating_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.setVisibility(View.GONE);
                check_mostpopular.setVisibility(View.GONE);
                check_rating.setVisibility(View.VISIBLE);
                check_delivery.setVisibility(View.GONE);
                sessionManager.setSort(2);
                parentFrag.dot1.setVisibility(View.VISIBLE);


            }
        });

        delivery_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.setVisibility(View.GONE);
                check_mostpopular.setVisibility(View.GONE);
                check_rating.setVisibility(View.GONE);
                check_delivery.setVisibility(View.VISIBLE);
                sessionManager.setSort(3);
                parentFrag.dot1.setVisibility(View.VISIBLE);

            }
        });


        recommended_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.setVisibility(View.VISIBLE);
                check_mostpopular.setVisibility(View.GONE);
                check_rating.setVisibility(View.GONE);
                check_delivery.setVisibility(View.GONE);
                sessionManager.setSort(0);
                parentFrag.dot1.setVisibility(View.GONE);

            }
        });
    }

}
