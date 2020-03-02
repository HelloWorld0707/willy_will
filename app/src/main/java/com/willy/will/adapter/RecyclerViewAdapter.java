package com.willy.will.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private SelectionTracker<Long> trckr = null;

    private RecyclerViewSetter setter = null;
    private ArrayList<T> dset = null;
    private int t = 0;

    public RecyclerViewAdapter(int type, ArrayList<T> dataset, RecyclerViewSetter recyclerViewSetter) {
        t = type;
        dset = dataset;
        setter = recyclerViewSetter;

        setHasStableIds(true);
    }

    // If Tracker is set inside the constructor, an error occurs
    public void setTracker(SelectionTracker tracker) {
        trckr = tracker;
    }
    public SelectionTracker getTracker() {
        return this.trckr;
    }

    // Get data when changed check box of to-do item (at holder)
    public T getData(Long itemId) {
        int position = Math.toIntExact((Long) itemId);
        return dset.get(position);
    }

    // Called for each item
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        int layoutId = setter.getLayoutId(t);
        View view = layoutInflater.inflate(layoutId, parent, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(this, t, view);
        return holder;
    }

    // Called for each item
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        T data = dset.get(position);
        if (trckr != null) {
            holder.bind(t, data, trckr.isSelected(Long.valueOf(position)));
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
