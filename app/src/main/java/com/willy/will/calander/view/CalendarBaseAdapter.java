package com.willy.will.calander.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.willy.will.R;
import com.willy.will.database.DateDBController;

import java.util.ArrayList;

public class CalendarBaseAdapter extends BaseAdapter {
    private ArrayList<DateDBController.ItemNGroup> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public DateDBController.ItemNGroup getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getItem_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.calendar_listview, parent, false);
        }

        TextView colorTmp = convertView.findViewById(R.id.calendarGroupColorTemp);
        TextView grpName = convertView.findViewById(R.id.calendarGroupText);
        TextView itemName = convertView.findViewById(R.id.calendarItemNameText);

        DateDBController.ItemNGroup item = getItem(position);
        colorTmp.setBackgroundColor(Color.parseColor(item.getGroup_color()));
        grpName.setText(item.getGroup_name());
        itemName.setText(item.getItem_name());

        return convertView;
    }

    public void addItem(DateDBController.ItemNGroup item){
        items.add(item);
    }
}
