package com.willy.will.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.detail.view.DetailActivity;

import java.util.ArrayList;

public class RecyclerViewSetter {

    /** For Setting RecyclerView **/
    private View parentView = null;
    private ArrayList list = null;
    private SelectionTracker.SelectionPredicate predicate = null;
    private RecyclerViewItemType type;
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
                              RecyclerViewItemType itemType,
                              ArrayList dataSet,
                              int selectionId,
                              boolean multipleSelection) {
        recyclerId = recyclerViewId;
        parentView = view;
        type = itemType;
        list = dataSet;
        selectId = selectionId;
        /** Set selection predicate for tracker **/
        if(multipleSelection) {
            predicate = SelectionPredicates.createSelectAnything();
        }
        else {
            predicate = SelectionPredicates.createSelectSingleAnything();
        }
        /* ~Set selection predicate for tracker */

        /** Set Selecting All Text View of Group Search Setting **/
        if(itemType == RecyclerViewItemType.GROUP_SEARCH) {
            selectingAllView = view.findViewById(R.id.selecting_all);
        }
        /* ~Set Selecting All Text View of Group Search Setting */
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
        adapter = new RecyclerViewAdapter(type, list);
        recyclerView.setAdapter(adapter);
        /* ~Set Adapter */

        /** Set Tracker **/
        tracker = new SelectionTracker.Builder(
                parentView.getResources().getString(selectId),
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
                if (type == RecyclerViewItemType.TO_DO) {
                    changeToDoItem();
                }
                // Group
                else if (type == RecyclerViewItemType.GROUP_SEARCH) {
                    changeGroupItem();
                }
                // Done or Distance
                else if (type == RecyclerViewItemType.DONE_SEARCH || type == RecyclerViewItemType.DISTANCE_SEARCH) { }
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
        if(tracker.hasSelection()) {
            Intent intent = new Intent(parentView.getContext(), DetailActivity.class);
            parentView.getContext().startActivity(intent);
            tracker.clearSelection();
        }
    }

    private void changeGroupItem() {
        if(tracker.getSelection().size() > 1) {
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

}
