package com.willy.will.calander.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
            convertView = inflater.inflate(R.layout.item_calendar, parent, false);
        }

        ImageView colorTmp = convertView.findViewById(R.id.calendar_group_color);
        TextView itemName = convertView.findViewById(R.id.calendar_task_name);
        TextView Dday = convertView.findViewById(R.id.calendar_d_day_or_achievement);

        DateDBController.ItemNGroup item = getItem(position);

        if(item.getGroup_id() != 0) {
            colorTmp.setActivated(true);
            colorTmp.getDrawable().mutate().setTint(Color.parseColor(item.getGroup_color()));
        }
        else{
            colorTmp.setActivated(false);
        }
        itemName.setText(item.getItem_name());
        Dday.setText(item.getdDayOrAchievement());

        return convertView;
    }

    public void addItem(DateDBController.ItemNGroup item){
        items.add(item);
    }

}
