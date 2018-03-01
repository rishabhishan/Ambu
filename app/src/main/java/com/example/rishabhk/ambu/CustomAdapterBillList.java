package com.example.rishabhk.ambu;

import android.content.Context;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import android.app.Activity;
/**
 * Created by rishabhishan on 01/03/18.
 */

public class CustomAdapterBillList extends BaseAdapter {



    Context context;
    List<RowItemForBill> rowItems;

    CustomAdapterBillList(Context context, List<RowItemForBill> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    /* private view holder class */
    private class ViewHolder {
        TextView startletter;
        TextView room_name;
        TextView date_time;
        TextView litres;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.bill_list_item, null);
            holder = new ViewHolder();

            holder.startletter = (TextView) convertView
                    .findViewById(R.id.textstart);
            holder.room_name = (TextView) convertView
                    .findViewById(R.id.room_name);
            holder.date_time = (TextView) convertView
                    .findViewById(R.id.update_date_time);
            holder.litres = (TextView) convertView
                    .findViewById(R.id.litres_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RowItemForBill row_pos = rowItems.get(position);
        holder.startletter.setText(row_pos.getStartletter());
        holder.room_name.setText(row_pos.getRoom_name());
        holder.date_time.setText(row_pos.getLastUpdated());
        holder.litres.setText(row_pos.getLitres_count());

        return convertView;
    }


}