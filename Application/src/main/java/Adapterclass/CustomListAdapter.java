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

import java.util.ArrayList;

/**
 * Created by abc on 7/15/2015.
 */
public class CustomListAdapter extends BaseAdapter {

    private ArrayList<Placedata> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<Placedata> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.headlineView.setText(listData.get(position).getPlace());
        holder.headlineView.setTextColor(Color.WHITE);
        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reportedDateView;
    }
}
