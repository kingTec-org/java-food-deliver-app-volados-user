package gofereats.views.main.fragments;


/**
 * @package com.trioangle.gofereats
 * @subpackage view.main.fragments.SearchFragments
 * @category CustomDialog
 * @author Trioangle Product Team
 * @version 1.0
 **/


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.obs.CustomEditText;
import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import gofereats.R;
import gofereats.adapters.main.SearchListAdapter;
import gofereats.adapters.main.home.MoreRestaurantListAdapter;
import gofereats.adapters.main.home.MoreSearchListAdapter;
import gofereats.configs.AppController;
import gofereats.configs.SessionManager;
import gofereats.datamodels.main.JsonResponse;
import gofereats.datamodels.main.home.RestaurantListModel;
import gofereats.datamodels.searchcategory.CategoryListModel;
import gofereats.datamodels.searchcategory.CategoryModel;
import gofereats.datamodels.searchcategory.ResultListModel;
import gofereats.interfaces.ActivityListener;
import gofereats.interfaces.ApiService;
import gofereats.interfaces.ServiceListener;
import gofereats.utils.CommonMethods;
import gofereats.utils.RequestCallback;
import gofereats.views.customize.CustomDialog;
import gofereats.views.main.MainActivity;

import static gofereats.utils.Enums.REQ_GET_CATEGORY;
import static gofereats.utils.Enums.REQ_GET_CATEGORY_RESULT;

/*****************************************************************
 Search Fragment used in Main Activity
 ****************************************************************/


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements ServiceListener, MainActivity.OnBackPressedListener {

    private static ArrayList<CategoryListModel> categoriesList = new ArrayList<>();
    private static ArrayList<CategoryListModel> morecategoryList = new ArrayList<>();
    private static ArrayList<RestaurantListModel> restaurantList = new ArrayList<>();
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
    public RecyclerView rcvTopcategories;
    public RecyclerView more_search_list_view;
    public RecyclerView rcResults;
    public ImageView ivClose;
    public CustomEditText edtSearch;
    public CustomTextView tvResultCount;
    public SearchListAdapter searchlistadapter;
    public MoreSearchListAdapter moreSearchListAdapter;
    public MoreRestaurantListAdapter resultadapter;
    public RelativeLayout rltEmptylist;
    public RelativeLayout rltSearchCategories;
    public RelativeLayout rltResultList;
    private View view;
    private ActivityListener listener;
    private MainActivity mActivity;
    private AlertDialog dialog;
    private int backPressed = 0;
    private int status = 0;


    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        ButterKnife.inject(mActivity);
        AppController.getAppComponent().inject(this);
        mActivity.setOnBackPressedListener(this);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_search, container, false);

            intiViews();
            intRecycleViews();
            if (sessionManager.getSearchCuisine().equals("1")) {
                status = 1;
                edtSearch.setText(sessionManager.getCuisineName());
                ivClose.setVisibility(View.VISIBLE);
                getResults(edtSearch.getText().toString().trim());
                sessionManager.setCuisineName("");
                sessionManager.setSearchCuisine("");
            } else {
                getCategories();
            }

        }

        /**
         * TextWatcher For Edit Text
         */
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edtSearch.getText().toString().equals("")) {
                    rltSearchCategories.setVisibility(View.VISIBLE);
                    rltResultList.setVisibility(View.GONE);
                    ivClose.setVisibility(View.GONE);
                    rltEmptylist.setVisibility(View.GONE);
                } else {
                    rltSearchCategories.setVisibility(View.GONE);
                    rltResultList.setVisibility(View.GONE);
                    ivClose.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("afterTextChanged ");
            }
        });

        /**
         *  KeyBoard Search listerner
         */

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getResults(edtSearch.getText().toString().trim());
                    //rltResultList.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
        /**
         * Clear Text
         */
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSearch.getText().clear();
                if (status == 1) {
                    getCategories();
                    status = 0;
                }
            }
        });

        return view;

    }


    /**
     * Ints Activitty
     */
    private void init() {
        if (listener == null) return;

        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (MainActivity) getActivity();
    }

    /**
     * Views
     */
    private void intiViews() {

        rcvTopcategories = view.findViewById(R.id.rcvTopcategories);
        more_search_list_view = view.findViewById(R.id.more_search_list_view);
        edtSearch = view.findViewById(R.id.edtSearch);
        rltEmptylist = view.findViewById(R.id.rltEmptylist);
        rltSearchCategories = view.findViewById(R.id.rltSearchCategories);
        rltResultList = view.findViewById(R.id.rltResultList);
        tvResultCount = view.findViewById(R.id.tvResultCount);
        rcResults = view.findViewById(R.id.rcResults);
        ivClose = view.findViewById(R.id.ivClose);

        ivClose.setVisibility(View.GONE);
        dialog = commonMethods.getAlertDialog(mActivity);

        rltSearchCategories.setVisibility(View.GONE);
    }

    /**
     * Layout Manager For Rc Views
     */
    public void intRecycleViews() {

        rcvTopcategories.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setAutoMeasureEnabled(false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvTopcategories.setLayoutManager(gridLayoutManager);

        more_search_list_view.setHasFixedSize(true);
        GridLayoutManager gridLayoutManagers = new GridLayoutManager(getActivity(), 2);
        gridLayoutManagers.setAutoMeasureEnabled(false);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        more_search_list_view.setLayoutManager(gridLayoutManagers);

        rcResults.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setAutoMeasureEnabled(false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcResults.setLayoutManager(linearLayoutManager);
    }

    /**
     * Call API For Get the Category lists
     */
    public void getCategories() {
        commonMethods.showProgressDialog(mActivity, customDialog);
        apiService.categories(sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_CATEGORY, this));
    }

    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) {
            if (!TextUtils.isEmpty(data)) commonMethods.showMessage(mActivity, dialog, data);
            return;

        }
        switch (jsonResp.getRequestCode()) {
            case REQ_GET_CATEGORY:
                if (jsonResp.isSuccess()) {
                    onSuccessGetCategories(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }
                break;

            case REQ_GET_CATEGORY_RESULT:
                if (jsonResp.isSuccess()) {
                    onSuccessGetCategoryList(jsonResp);
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

    /**
     * Get ALL Category List
     */

    private void onSuccessGetCategories(JsonResponse jsonResponse) {
        CategoryModel categoryModel = gson.fromJson(jsonResponse.getStrResponse(), CategoryModel.class);
        if (categoryModel.getTopcategory() != null) {
            categoriesList.clear();
            categoriesList.addAll(categoryModel.getTopcategory());
            searchlistadapter = new SearchListAdapter(getContext(), categoriesList);
            rcvTopcategories.setAdapter(searchlistadapter);

            morecategoryList.clear();
            morecategoryList.addAll(categoryModel.getCategory());
            moreSearchListAdapter = new MoreSearchListAdapter(getContext(), morecategoryList);
            more_search_list_view.setAdapter(moreSearchListAdapter);


            searchlistadapter.setOnCategoryClick(new SearchListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int id, String name) {
                    edtSearch.setText(name);
                    rltSearchCategories.setVisibility(View.GONE);
                    getResults(name);
                }
            });

            moreSearchListAdapter.setOnCategoryClick(new MoreSearchListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(int id, String name) {
                    edtSearch.setText(name);
                    rltSearchCategories.setVisibility(View.GONE);
                    getResults(name);
                }
            });
            rltSearchCategories.setVisibility(View.VISIBLE);
        } else {
            rltSearchCategories.setVisibility(View.GONE);
        }
    }

    /**
     * OnSuccess of Getting Category List
     *
     * @param jsonResponse Json Response of api call
     */


    private void onSuccessGetCategoryList(JsonResponse jsonResponse) {
        ResultListModel resultListModel = gson.fromJson(jsonResponse.getStrResponse(), ResultListModel.class);
        if (resultListModel.getRestaurantResult() != null && resultListModel.getRestaurantResult().size() > 0) {
            restaurantList.clear();
            restaurantList.addAll(resultListModel.getRestaurantResult());
            tvResultCount.setText(resultListModel.getRestaurantResult().size() + " " + getString(R.string.restaurant));
            resultadapter = new MoreRestaurantListAdapter(getContext(), restaurantList);
            rcResults.setAdapter(resultadapter);
            rltResultList.setVisibility(View.VISIBLE);
            rltEmptylist.setVisibility(View.GONE);
        } else {
            rltResultList.setVisibility(View.GONE);
            rltEmptylist.setVisibility(View.VISIBLE);
        }

    }

    private void getResults(String name) {
        commonMethods.showProgressDialog(mActivity, customDialog);
        apiService.search(name, sessionManager.getToken()).enqueue(new RequestCallback(REQ_GET_CATEGORY_RESULT, this));
    }

    @Override
    public void doBack() {
        if (status == 1) {
            getCategories();
            status = 0;
        }
        if (TextUtils.isEmpty(edtSearch.getText().toString())) {
            if (backPressed >= 1) {
                mActivity.finishAffinity();
            } else {
                // clean up
                backPressed = backPressed + 1;
                Toast.makeText(mActivity, "Press back again to exit.", Toast.LENGTH_SHORT).show();
            }
        } else {
            edtSearch.getText().clear();
            backPressed = 0;
        }
    }
}
