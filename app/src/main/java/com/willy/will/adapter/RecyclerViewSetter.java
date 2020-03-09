package com.willy.will.adapter;

import android.content.Context;
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
import com.willy.will.common.model.Task;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.detail.view.DetailActivity;
import com.willy.will.main.view.MainFragment;
import com.willy.will.search.view.SearchActivity;
import com.willy.will.setting.TaskManagementActivity;

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
    private TaskManagementActivity taskManagementActivity = null;
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
                    if(tracker.hasSelection()) {
                        selectToDoItem();
                    }
                }
                // Group
                else if(type == RecyclerViewItemType.GROUP_SEARCH) {
                    changeGroupItem();
                }
                // Done or Loop
                else if(type == RecyclerViewItemType.DONE_SEARCH ||
                        type == RecyclerViewItemType.LOOP_SEARCH) { }
                // Task
                else if(type == RecyclerViewItemType.TASK) {
                    if(tracker.hasSelection()) {
                        selectTask();
                    }
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

    // WARNING: Only one must be assigned
    public void setFragmentAndActivities(MainFragment main,
                                         SearchActivity search,
                                         TaskManagementActivity taskManagement) {
        mainFragment = main;
        searchActivity = search;
        taskManagementActivity = taskManagement;
    }

    private void selectToDoItem() {
        Context context = parentView.getContext();

        Intent intent = new Intent(mainFragment.getContext(), DetailActivity.class);
        String extraName = context.getResources().getString(R.string.request_code);
        int code = context.getResources().getInteger(R.integer.detail_request_code);
        intent.putExtra(extraName, code);

        int p = tracker.getSelection().hashCode();

        // To-do item of Main
        if((type == RecyclerViewItemType.TO_DO_MAIN) && (mainFragment != null)) {
            intent.putExtra(parentView.getResources().getString(R.string.item_id)
                    , (Serializable) list.get(p));

            mainFragment.startActivityForResult(intent, code);
        }
        // To-do item of Search
        else if((type == RecyclerViewItemType.TO_DO_SEARCH) && (searchActivity != null)) {
            intent.putExtra(parentView.getResources().getString(R.string.item_id)
                    , (Serializable) list.get(p));

            searchActivity.startActivityForResult(intent, code);
        }
        else {
            Log.e("RecyclerViewSetter", "Bring up Detail: Don't set up Main Fragment or Search Activity");
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

    private void selectTask() {
        if(taskManagementActivity != null) {
            Context context = parentView.getContext();

            Intent intent = new Intent(mainFragment.getContext(), DetailActivity.class);
            String extraName = context.getResources().getString(R.string.request_code);
            int code = context.getResources().getInteger(R.integer.detail_request_code);
            intent.putExtra(extraName, code);

            int p = tracker.getSelection().hashCode();
            Task selectedTask = (Task) list.get(p);
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.setItemId(selectedTask.getItemId());
            intent.putExtra(parentView.getResources().getString(R.string.item_id)
                    , (Serializable) toDoItem);

            taskManagementActivity.startActivityForResult(intent, code);
        }
        else {
            Log.e("RecyclerViewSetter", "Bring up Detail: Don't set up Task Management Activity");
        }
    }

}
