package com.willy.will.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.detail.view.DetailActivity;

import java.util.ArrayList;

public class RecyclerViewSetter {

    /** Common (also written in RecyclerViewHolder) **/
    private int TO_DO_CODE = 0;
    private int GROUP_CODE = 0;
    private int DONE_CODE = 0;
    private int DISTANCE_CODE = 0;
    /* ~Common (also written in RecyclerViewHolder) */

    /** For Setting RecyclerView **/
    private View parentView = null;
    private Resources resources = null;
    private ArrayList list = null;
    private SelectionTracker.SelectionPredicate predicate = null;
    private int tId = 0;
    private int recyclerId = 0;
    private int selectId = 0;

    private RecyclerViewAdapter adapter = null;
    private SelectionTracker tracker = null;
    /* ~For Setting RecyclerView */

    /** For Group Search Setting **/
    private TextView selectingAllView = null;
    /* ~For Group Search Setting */

    public RecyclerViewSetter(int recyclerViewId,
                              View view,
                              int typeId,
                              ArrayList dataSet,
                              int selectionId,
                              boolean multipleSelection) {
        // Set Resources
        resources = view.getResources();
        /** Set codes by type (also written in RecyclerViewHolder:RecyclerViewHolder) **/
        TO_DO_CODE = resources.getInteger(R.integer.to_do_recycler_item_type);
        GROUP_CODE = resources.getInteger(R.integer.group_search_setting_recycler_item_type);
        DONE_CODE = resources.getInteger(R.integer.done_search_setting_recycler_item_type);
        DISTANCE_CODE = resources.getInteger(R.integer.distance_search_setting_recycler_item_type);
        /* ~Set codes by type (also written in RecyclerViewHolder:RecyclerViewHolder) */

        if(resources.getInteger(typeId) == GROUP_CODE) {
            selectingAllView = view.findViewById(R.id.selecting_all);
        }

        recyclerId = recyclerViewId;
        parentView = view;
        tId = typeId;
        list = dataSet;
        selectId = selectionId;
        /** Set selection predicate for tracker **/
        if(multipleSelection) {
            predicate = SelectionPredicates.<Long>createSelectAnything();
        }
        else {
            predicate = SelectionPredicates.createSelectSingleAnything();
        }
        /* ~Set selection predicate for tracker */
    }

    // Set RecyclerView, LayoutManager, Adapter, and Tracker
    public RecyclerView setRecyclerView() {
        RecyclerView recyclerView = parentView.findViewById(recyclerId);
        recyclerView.setHasFixedSize(true);

        /** Set LayoutManager **/
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(parentView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        /* ~Set LayoutManager */

        /** Set Adapter **/
        // Set the type
        final int TYPE = resources.getInteger(tId);
        adapter = new RecyclerViewAdapter(TYPE, list, this);
        recyclerView.setAdapter(adapter);
        /* ~Set Adapter */

        /** Set Tracker **/
        tracker = new SelectionTracker.Builder(
                resources.getString(selectId),
                recyclerView,
                new RecyclerItemKeyProvider(recyclerView),
                new RecyclerItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                predicate
        ).build();
        adapter.setTracker(tracker);
        tracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();

                // To-do
                if (TYPE == TO_DO_CODE) {
                    changeToDoItem();
                }
                // Group
                else if (TYPE == GROUP_CODE) {
                    changeGroupItem();
                }
                // ERROR: Wrong type
                else {
                    Log.e("RecyclerViewSetter", "Setting: Wrong type");
                }
            }

        });
        /* ~Set Tracker */

        return recyclerView;
    }

    private void changeToDoItem() {
        Intent intent = new Intent(parentView.getContext(), DetailActivity.class);
        parentView.getContext().startActivity(intent);
    }

    private void changeGroupItem() {
        if(tracker.hasSelection()) {
            if(selectingAllView.isSelected()) {
                selectingAllView.setSelected(false);
            }
        }
        else {
            if(!selectingAllView.isSelected()) {
                selectingAllView.setSelected(true);
            }
        }
    }

    // Get Layout ID for onCreateViewHolder of RecyclerViewAdapter. Layout ID is layout filename without extension(.xml)
    public int getLayoutId(int type) {
        int id = 0;

        // To-do
        if(type == TO_DO_CODE) {
            id = R.layout.listitem;
        }
        // Text-only (Group, Done, Distance)
        else if(type == GROUP_CODE || type == DONE_CODE || type == DISTANCE_CODE) {
            id = R.layout.recycleritem_text_only;
        }
        // ERROR: Wrong type
        else {
            Log.e("RecyclerViewHolder", "Getting Layout ID: Wrong type");
        }

        return id;
    }

}
