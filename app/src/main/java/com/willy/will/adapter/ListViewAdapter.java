package com.willy.will.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.willy.will.common.controller.ListViewHolder;

import java.util.ArrayList;

public class ListViewAdapter<T> extends BaseAdapter {

    private ArrayList<T> list = null;
    private int layoutId = -1;

    private ListViewHolder holder = null;

    public ListViewAdapter(ArrayList<T> list, int layoutId, ListViewHolder holder) {
        this.list = list;
        this.layoutId = layoutId;
        this.holder = holder;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutId, parent, false);
        }

        holder.setView(convertView);
        holder.bindData(list.get(position));

        return convertView;
    }
}
