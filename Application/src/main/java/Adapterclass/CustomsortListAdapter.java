package Adapterclass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.map.saveeasysavedrivee.R;
import BeanClass.Placedata;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by abc on 7/16/2015. for showing sorted place and distance
 */
public class CustomsortListAdapter extends BaseAdapter {

    private ArrayList<Placedata> listData;
    private LayoutInflater layoutInflater;

    public CustomsortListAdapter(Context aContext, ArrayList<Placedata> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }


    public int getCount() {
        return listData.size();
    }


    public Object getItem(int position) {
        return listData.get(position);
    }


    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_rowsorted, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DecimalFormat df = new DecimalFormat("####0.000");
        holder.headlineView.setTextColor(Color.WHITE);
        holder.headlineView.setText(listData.get(position).getPlace());
        if(listData.get(position).getDistanceunit().equals("KiloMeter")) {
            holder.reporterNameView.setText(" " + df.format(listData.get(position).getDistance())+" Kilometer");

        } else {
            holder.reporterNameView.setText(" " + df.format(listData.get(position).getDistance() * 0.621371)+" Miles");
        }
          return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
    }
}
