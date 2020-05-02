package gofereats.adapters.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * @package com.gofereats
 * @subpackage adapters.main
 * @category spinner Adapter
 * @author Trioangle Product Team
 * @version 1.0
 * <p>
 * Created by trioangle on 5/30/18.
 * <p>
 * Created by trioangle on 5/30/18.
 * <p>
 * Created by trioangle on 5/30/18.
 * <p>
 * Created by trioangle on 5/30/18.
 * <p>
 * Created by trioangle on 5/30/18.
 * <p>
 * Created by trioangle on 5/30/18.
 * <p>
 * Created by trioangle on 5/30/18.
 * <p>
 * Created by trioangle on 5/30/18.
 * <p>
 * Created by trioangle on 5/30/18.
 */


/**
 * Created by trioangle on 5/30/18.
 */

/*************************************************************
 custom Spinner Adapter class
 ************************************************************/


public class CustomSpinnerArrayAdapter extends ArrayAdapter<String> {

    public Context context;
    public String[] objects;
    public String firstElement;
    public boolean isFirstTime;


    public CustomSpinnerArrayAdapter(Context context, int textViewResourceId, String[] objects, String defaultText) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.isFirstTime = true;
        setDefaultText(defaultText);
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (isFirstTime) {
            objects[0] = firstElement;
            isFirstTime = false;
        }
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);
    }

    public void setDefaultText(String defaultText) {
        this.firstElement = objects[0];
        objects[0] = defaultText;
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        TextView label = row.findViewById(android.R.id.text1);
        label.setText(objects[position]);

        return row;
    }
}
