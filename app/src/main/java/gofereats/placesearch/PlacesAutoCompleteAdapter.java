package gofereats.placesearch;

/**
 * @package com.trioangle.gofereats
 * @subpackage placesearch
 * @category placeseach auto complete
 * @author Trioangle Product Team
 * @version 1.1
 */

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import gofereats.R;

/*****************************************************************
 Adapter Used for Places Auto Complete
 ****************************************************************/


/**
 * Note that this adapter requires AddCardActivity valid {@link GoogleApiClient}.
 * The API client must be maintained in the encapsulating Activity, including all lifecycle and
 * connection states. The API client must be connected with the {@link Places#GEO_DATA_API} API.
 **/
public class PlacesAutoCompleteAdapter extends RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder> implements Filterable {

    private ArrayList<PlaceAutocomplete> mResultList;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mBounds;
    private AutocompleteFilter mPlaceFilter;

    private Context mContext;
    private int layout;


    /**
     * PlacesAutoCompleteAdapter Constructor
     *
     * @param context         context of the activity that uses places auto complete adapter
     * @param resource        layout resource file
     * @param googleApiClient google api client
     * @param bounds          Latitude and longitude bounds
     * @param filter          Filer of AutoComplete
     */


    public PlacesAutoCompleteAdapter(Context context, int resource, GoogleApiClient googleApiClient, LatLngBounds bounds, AutocompleteFilter filter) {
        mContext = context;
        layout = resource;
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mPlaceFilter = filter;
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint);
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    private ArrayList getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {
            Log.i("", "Starting autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve AddCardActivity PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, constraint.toString(), mBounds, mPlaceFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for AddCardActivity result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results.await(500, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                // Toast.makeText(mContext, "Error contacting API: " + status.toString(),Toast.LENGTH_SHORT).show();
                Log.e("", "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i("", "Query completed. Received " + autocompletePredictions.getCount() + " predictions.");
            if (autocompletePredictions.getCount() == 0) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.no_location), Toast.LENGTH_SHORT).show();
                /*Snackbar snackbar = Snackbar
                        .make(, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);

                snackbar.show();*/
            }
            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into AddCardActivity new PlaceAutocomplete object.
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getFullText(null), prediction.getPrimaryText(null), prediction.getSecondaryText(null)));
            }

            // Release the buffer now that all data has been copied.
            autocompletePredictions.release();

            return resultList;
        }
        Log.e("", "Google API client is not connected for autocomplete query.");
        return null;
    }

    @Override
    public PredictionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, viewGroup, false);
        PredictionHolder mPredictionHolder = new PredictionHolder(convertView);
        return mPredictionHolder;
    }

    /**
     * Bind View holder
     */
    @Override
    public void onBindViewHolder(PredictionHolder mPredictionHolder, final int i) {

        String Address = (String) mResultList.get(i).description;
        String[] address = Address.split(",");
        String subaddress = "";
        System.out.println("Address " + Address);
        for (int j = 1; j < address.length; j++) {
            try {
                if (j == 1) {
                    subaddress = address[1];
                } else {
                    subaddress = subaddress + ", " + address[j];
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        }
        System.out.println("Address " + Address);
        System.out.println("Sub address " + subaddress);
        mPredictionHolder.mPrediction.setText(address[0]);
        mPredictionHolder.mPredictionSub.setText(subaddress);


    }

    @Override
    public int getItemCount() {
        if (mResultList != null) return mResultList.size();
        else return 0;
    }

    public void clear() {
        if (mResultList != null) mResultList.clear();
    }

    /**
     * Place auto complete get data
     */
    public PlaceAutocomplete getItem(int position) {
        try {
            if (mResultList.size() > 0) {
                return mResultList.get(position);
            } else {
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Holder for Places holder  API results.
     */
    public class PredictionHolder extends RecyclerView.ViewHolder {
        private TextView mPrediction, mPredictionSub;
        private RelativeLayout mRow;

        public PredictionHolder(View itemView) {

            super(itemView);
            mPrediction = itemView.findViewById(R.id.address);
            mPredictionSub = itemView.findViewById(R.id.address_secondry);
            mRow = itemView.findViewById(R.id.predictedRow);
        }

    }

    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;
        public CharSequence addresss;
        public CharSequence subaddress;

        PlaceAutocomplete(CharSequence placeId, CharSequence description, CharSequence addresss, CharSequence subaddress) {
            this.placeId = placeId;
            this.description = description;
            this.addresss = addresss;
            this.subaddress = subaddress;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }

}