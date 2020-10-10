package com.willy.will.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.common.model.Group;
import com.willy.will.common.model.RecyclerViewItemType;

import java.util.ArrayList;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private SelectionTracker<Long> tracker = null;

    private Activity activity;

    private ArrayList<T> dataset;
    private RecyclerViewItemType type;
    private int layoutId;

    // For no tracker
    private int selectedPosition = -1;

    public RecyclerViewAdapter(int layoutId, RecyclerViewItemType type, ArrayList<T> dataset, Activity activity) {
        this.layoutId = layoutId;
        this.type = type;
        this.dataset = dataset;
        this.activity = activity;

        setHasStableIds(true);
    }

    // If Tracker is set inside the constructor, an error occurs
    public void setTracker(SelectionTracker tracker) {
        this.tracker = tracker;
    }
    public SelectionTracker getTracker() {
        return this.tracker;
    }

    // For starting new Activities
    public Activity getActivity() {
        return activity;
    }

    // Get data when changed check box of to-do item (at holder)
    public T getData(int position) {
        return dataset.get(position);
    }
    public ArrayList<T> getList() {
        return dataset;
    }

    // For no tracker
    public int getSelectedPosition() {
        return selectedPosition;
    }
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    // For Group Management
    public void setSelectedGroup(int selectedGroupId) {
        if(type == RecyclerViewItemType.GROUP) {
            int selectedIndex = 0;
            final int SIZE = dataset.size();
            for(int index = 0; index < SIZE; index++) {
                if(((Group) dataset.get(index)).getGroupId() == selectedGroupId) {
                    selectedIndex = index;
                    break;
                }
            }
            setSelectedPosition(selectedIndex);
        }
    }

    // Called for each item
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutId, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(this, type, view);
        return holder;
    }

    // Called for each item
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        T data = dataset.get(position);
        if (tracker != null) {
            holder.bind(type, data, tracker.isSelected(Long.valueOf(position)));
        }
        else {
            boolean selected = (position == selectedPosition) ? true : false;
            holder.bind(type, data, selected);
        }
    }

    // Get the position in Long type
    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
