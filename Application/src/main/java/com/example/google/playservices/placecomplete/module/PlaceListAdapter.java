package com.example.google.playservices.placecomplete.module;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.google.playservices.placecomplete.R;
import com.example.google.playservices.placecomplete.module.Placedata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Devendra on 06-05-2015.
 */
public class PlaceListAdapter extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;

    ArrayList<String> data = new ArrayList<String>();

    public PlaceListAdapter(Context context, int layoutResourceId,
                              ArrayList<String> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

            String place = data.get(position);
            txtTitle.setText(place);

        }

        return row;

    }

}
