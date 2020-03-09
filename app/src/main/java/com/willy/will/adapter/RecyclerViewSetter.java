package com.willy.will.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.detail.view.DetailActivity;
import com.willy.will.main.view.MainFragment;
import com.willy.will.search.view.SearchActivity;

import java.io.Serializable;
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

    /** For To-do Item **/
    private MainFragment mainFragment = null;
    private SearchActivity searchActivity = null;
    //private ToDoItemManagementActivity toDoItemManagementActivity = null;
    /* ~For To-do Item */

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

                // To-do item (of Main or Search)
                if(type == RecyclerViewItemType.TO_DO_MAIN ||
                   type == RecyclerViewItemType.TO_DO_SEARCH) {
                    changeToDoItem();
                }
                // Group
                else if(type == RecyclerViewItemType.GROUP_SEARCH) {
                    changeGroupItem();
                }
                // Done or Loop
                else if(type == RecyclerViewItemType.DONE_SEARCH ||
                        type == RecyclerViewItemType.LOOP_SEARCH) { }
                // ERROR: Wrong type
                else {
                    Log.e("RecyclerViewSetter", "Setting: Wrong type");
                }
            }

        });
        /* ~Set Tracker */

        return recyclerView;
    }

    // WARNING: Only one must be assigned
    public void setFragmentAndActivities(MainFragment main,
                                         SearchActivity search,
                                         AppCompatActivity toDoManagement) {
        mainFragment = main;
        searchActivity = search;
        //toDoItemManagementActivity = toDoManagement;
    }

    private void changeToDoItem() {
        if(tracker.hasSelection()) {
            Context context = parentView.getContext();
            String extraName = context.getResources().getString(R.string.request_code);
            int code = context.getResources().getInteger(R.integer.detail_request_code);

            // To-do item of Main
            if((type == RecyclerViewItemType.TO_DO_MAIN) && (mainFragment != null)) {
                int p = tracker.getSelection().hashCode();
                Intent intent = new Intent(mainFragment.getContext(), DetailActivity.class);
                intent.putExtra(extraName, code);
                intent.putExtra(parentView.getResources().getString(R.string.item_id)
                , (Serializable) list.get(p));
                mainFragment.startActivityForResult(intent, code);
            }
            // To-do item of Search
            else if((type == RecyclerViewItemType.TO_DO_SEARCH) && (searchActivity != null)) {
                Intent intent = new Intent(searchActivity, DetailActivity.class);
                intent.putExtra(extraName, code);
                searchActivity.startActivityForResult(intent, code);
            }
            // Task item of Task management
            /*else if(toDoItemManagementActivity != null) {
                Intent intent = new Intent(toDoItemManagementActivity, DetailActivity.class);
                intent.putExtra(extraName, code);
                toDoItemManagementActivity.startActivityForResult(intent, code);
            }*/
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
