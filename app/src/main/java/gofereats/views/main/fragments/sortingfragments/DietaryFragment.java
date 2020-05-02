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
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import gofereats.R;
import gofereats.adapters.main.home.DietaryListAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.home.RestaurantModel;
import gofereats.datamodels.restaurantdetails.DietaryListModel;
import gofereats.placesearch.RecyclerItemClickListener;
import gofereats.utils.SpacesItemDecoration;
import gofereats.views.customize.CustomLayoutManager;
import gofereats.views.customize.CustomRecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */

/*****************************************************************
 Dietary Fragment used in filter fragment for filters
 ****************************************************************/


public class DietaryFragment extends Fragment {

    public @Inject
    SessionManager sessionManager;

    public FilterFragment parentFrag;

    public @InjectView(R.id.rvDietaryList)
    CustomRecyclerView rvDietaryList;
    List<String> dietaryIdList = new ArrayList<String>();
    List<String> dietaryNameList = new ArrayList<String>();
    private ArrayList<DietaryListModel> dietaryListModels = new ArrayList<>();
    private DietaryListAdapter dietaryListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dietary_new, container, false);

        AppController.getAppComponent().inject(this);
        ButterKnife.inject(this, view);
        /**
         * Select Veg
         */
        parentFrag = ((FilterFragment) DietaryFragment.this.getParentFragment());
        initRecyclerView();
        loadDietary();
        return view;
    }

    /**
     * @Reference Initialize recycler view
     **/
    private void initRecyclerView() {

        CustomLayoutManager layoutManager = new CustomLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvDietaryList.setLayoutManager(layoutManager);
        rvDietaryList.addItemDecoration(new SpacesItemDecoration(2));

        dietaryListAdapter = new DietaryListAdapter(getContext());
        rvDietaryList.setAdapter(dietaryListAdapter);
        rvDietaryList.setHasFixedSize(true);

        rvDietaryList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String dietaryName = dietaryListModels.get(position).getDietaryName();
                int dietaryId = dietaryListModels.get(position).getDietaryId();
                System.out.println("Name " + dietaryName);
                System.out.println("Position " + position);
                setDietary(dietaryId, dietaryName);
            }
        }));

    }

    /**
     * To Add newly added dietary filter for api passing
     */


    public void setDietary(int id, String name) {
        System.out.println("Dietary Id Sort" + sessionManager.getDietarySort());
        System.out.println("Dietary Name Sort" + sessionManager.getDietaryName());
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

        System.out.println("Dietary Id Sort" + sessionManager.getDietarySort());
        System.out.println("Dietary Name Sort" + sessionManager.getDietaryName());
        for (int i = 0; i < dietaryListModels.size(); i++) {
            System.out.println("Dietary Id " + dietaryListModels.get(i).getDietaryId());
            if (dietaryIdList.contains(String.valueOf(dietaryListModels.get(i).getDietaryId()))) {
                dietaryListModels.get(i).setDietarySelected(true);
            } else {
                dietaryListModels.get(i).setDietarySelected(false);
            }
            System.out.println("Dietary isSelect " + dietaryListModels.get(i).isDietarySelected());
        }
        dietaryListAdapter.updateList(dietaryListModels);

        if (!sessionManager.getDietarySort().equals("")) {
            parentFrag.dot3.setVisibility(View.VISIBLE);
        } else {
            parentFrag.dot3.setVisibility(View.GONE);
        }

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

    public void loadDietary() {

        if (!sessionManager.getDietaryList().equals("")) {
            dietaryListModels.clear();
            Gson gson = new Gson();
            RestaurantModel restaurantModel = gson.fromJson(sessionManager.getDietaryList(), RestaurantModel.class);
            if (restaurantModel != null && restaurantModel.getDietaryListModel() != null && restaurantModel.getDietaryListModel().size() > 0) {
                dietaryListModels = restaurantModel.getDietaryListModel();
                dietaryListAdapter = new DietaryListAdapter(getContext(), dietaryListModels);
                rvDietaryList.setAdapter(dietaryListAdapter);
                setDietary(-1, "");
            }
        }
    }

}
