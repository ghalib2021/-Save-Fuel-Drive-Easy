package Adapterclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.map.saveeasysavedrivee.R;


import BeanClass.stepdata;

import java.util.ArrayList;

/**
 * Created by abc on 7/16/2015.
 */
public class CustomdirectionstepListAdapter extends BaseAdapter {

    private ArrayList<stepdata> listData;
    private LayoutInflater layoutInflater;

    public CustomdirectionstepListAdapter(Context aContext, ArrayList<stepdata> listData) {
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
            convertView = layoutInflater.inflate(R.layout.list_rowdirectionstep, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.title);
            holder.reporterNameView = (TextView) convertView.findViewById(R.id.distance);
            holder.reporteddistancetimeView=(TextView) convertView.findViewById(R.id.distancetime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//showing direction arrow
        if(listData.get(position).getStep().contains("My Location"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.mylocation);
        }

        else if(listData.get(position).getStep().contains("Turn right"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.turnright);
        }

        else if(listData.get(position).getStep().contains("Slight right")||listData.get(position).getStep().contains("Keep right"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.slightright);
        }
        else if(listData.get(position).getStep().contains("Slight left")||listData.get(position).getStep().contains("Keep left"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.slightleft);
        }
        else if(listData.get(position).getStep().contains("Turn left"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.turnleft);

        }
        else if(listData.get(position).getStep().contains("Head"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.head);
        }
        else if(listData.get(position).getStep().contains("continue")||listData.get(position).getStep().contains("Continue")|| listData.get(position).getStep().contains("straight")
                ||listData.get(position).getStep().contains("ramp"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.cointinue);
        }
        else if(listData.get(position).getStep().contains("Destination"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.destination);
            //holder.headlineView.setBackgroundResource(R.drawable.);
        }
        else if(listData.get(position).getStep().contains("round")||listData.get(position).getStep().contains("Chowk"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.circleicon);
            //holder.headlineView.setBackgroundResource(R.drawable.);
        }
        else if(listData.get(position).getStep().contains("U-turn"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.uturn);
            //holder.headlineView.setBackgroundResource(R.drawable.);
        }
        else if(listData.get(position).getStep().contains("Sharp right"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.sharpright);
            //holder.headlineView.setBackgroundResource(R.drawable.);
        }
        else  if(listData.get(position).getStep().contains("Merge"))
        {
            holder.headlineView.setBackgroundResource(R.drawable.merge);
            //holder.headlineView.setBackgroundResource(R.drawable.);
        }

        String stepsh=listData.get(position).getStep();
       /* holder.reporterNameView.setText(stepsh);
        holder.reporteddistancetimeView.setText("");*/
        int p = stepsh.indexOf('*');
        if (p >= 0) {
            String left = stepsh.substring(0, p);
            String right =stepsh.substring(p + 1);
            holder.reporterNameView.setText(left);
            holder.reporteddistancetimeView.setText(right);
        } else {
            holder.reporterNameView.setText(stepsh);
            holder.reporteddistancetimeView.setText("");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView reporterNameView;
        TextView reporteddistancetimeView;
    }
}
