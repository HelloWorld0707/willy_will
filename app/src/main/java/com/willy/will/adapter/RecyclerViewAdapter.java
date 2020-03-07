package com.willy.will.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.common.model.RecyclerViewItemType;

import java.util.ArrayList;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private SelectionTracker<Long> tracker = null;

    private ArrayList<T> dset = null;
    private RecyclerViewItemType type;

    public RecyclerViewAdapter(RecyclerViewItemType itemType, ArrayList<T> dataset) {
        type = itemType;
        dset = dataset;

        setHasStableIds(true);
    }

    // If Tracker is set inside the constructor, an error occurs
    public void setTracker(SelectionTracker tracker) {
        this.tracker = tracker;
    }
    public SelectionTracker getTracker() {
        return this.tracker;
    }

    // Get data when changed check box of to-do item (at holder)
    public T getData(int position) {
        return dset.get(position);
    }

    // Called for each item
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        int layoutId = 0;
        // To-do
        if(type == RecyclerViewItemType.TO_DO) {
            layoutId = R.layout.listitem;
        }
        // Text-only (Group, Done, or Loop)
        else if(type == RecyclerViewItemType.GROUP_SEARCH ||
                type == RecyclerViewItemType.DONE_SEARCH ||
                type == RecyclerViewItemType.LOOP_SEARCH) {
            layoutId = R.layout.recycleritem_text_only;
        }
        // ERROR: Wrong type
        else {
            Log.e("RecyclerViewAdapter", "Setting Layout ID: Wrong type");
        }
        View view = layoutInflater.inflate(layoutId, parent, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(this, type, view);
        return holder;
    }

    // Called for each item
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        T data = dset.get(position);
        if (tracker != null) {
            holder.bind(type, data, tracker.isSelected(Long.valueOf(position)));
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
        return dset.size();
    }

}
