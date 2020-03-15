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
import com.willy.will.add.view.AddItemActivity;
import com.willy.will.add.view.LocationSearchActivity;
import com.willy.will.common.model.Location;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.Task;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.common.view.GroupManagementActivity;
import com.willy.will.detail.view.DetailActivity;
import com.willy.will.main.view.MainFragment;
import com.willy.will.search.view.SearchActivity;
import com.willy.will.setting.view.TaskManagementActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class RecyclerViewSetter {

    /** For Setting RecyclerView **/
    private View parentView;
    private ArrayList list;
    private SelectionTracker.SelectionPredicate predicate;
    private RecyclerViewItemType type;
    private int recyclerId;
    private int selectId;

    private RecyclerViewAdapter adapter = null;
    private SelectionTracker tracker = null;
    /* ~For Setting RecyclerView */

    /** For starting Activity **/
    private MainFragment mainFragment = null;
    private SearchActivity searchActivity = null;
    private TaskManagementActivity taskManagementActivity = null;
    private static GroupManagementActivity groupManagementActivity = null;
    private LocationSearchActivity locationSearchActivity = null;
    /* ~For starting Activity */

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
        if(selectionId == 0) {
            predicate = null;
        }
        else {
            if (multipleSelection) {
                predicate = SelectionPredicates.createSelectAnything();
            } else {
                predicate = SelectionPredicates.createSelectSingleAnything();
            }
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

        /** Set Selected Position **/
        // Group Management
        if (type == RecyclerViewItemType.GROUP) {
            adapter.setSelectedPosition(parentView.getResources().getInteger(R.integer.no_group_id));
        }
        /* ~Set Selected Position */
        /** Or Set Tracker **/
        else if(selectId != 0) {
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
                    if (type == RecyclerViewItemType.TO_DO_MAIN ||
                            type == RecyclerViewItemType.TO_DO_SEARCH) {
                        if (tracker.hasSelection()) {
                            selectToDoItem();
                        }
                    }
                    // Group
                    else if (type == RecyclerViewItemType.GROUP_SEARCH) {
                        changeGroupItem();
                    }
                    // Done or Loop
                    else if (type == RecyclerViewItemType.DONE_SEARCH ||
                            type == RecyclerViewItemType.LOOP_SEARCH) {
                    }
                    // Task
                    else if (type == RecyclerViewItemType.TASK) {
                        if (tracker.hasSelection()) {
                            selectTask();
                        }
                    }
                    // Location search
                    else if (type == RecyclerViewItemType.LOCATION_SEARCH) {
                        if (tracker.hasSelection()) {
                            selectLocation();
                        }
                    }
                    // ERROR: Wrong type
                    else {
                    }
                }

            });
        }
        // ERROR: Wrong selection ID
        else {
        }
        /* ~Set Tracker */

        return recyclerView;
    }

    // WARNING: Only one must be assigned
    public void setFragment(MainFragment main) {
        mainFragment = main;
        searchActivity = null;
        taskManagementActivity = null;
        groupManagementActivity = null;
        locationSearchActivity = null;
    }
    public void setActivity(SearchActivity search) {
        mainFragment = null;
        searchActivity = search;
        taskManagementActivity = null;
        groupManagementActivity = null;
        locationSearchActivity = null;
    }
    public void setActivity(TaskManagementActivity taskManagement) {
        mainFragment = null;
        searchActivity = null;
        taskManagementActivity = taskManagement;
        groupManagementActivity = null;
        locationSearchActivity = null;
    }
    public void setActivity(GroupManagementActivity groupManagement) {
        mainFragment = null;
        searchActivity = null;
        taskManagementActivity = null;
        groupManagementActivity = groupManagement;
        locationSearchActivity = null;
    }
    public void setActivity(LocationSearchActivity locationSearch) {
        mainFragment = null;
        searchActivity = null;
        taskManagementActivity = null;
        groupManagementActivity = null;
        locationSearchActivity = locationSearch;
    }
    public static GroupManagementActivity getGroupManagementActivity() {
        return groupManagementActivity;
    }

    private void selectToDoItem() {
        Context context = parentView.getContext();
        String extraName = context.getResources().getString(R.string.request_code);
        int code = context.getResources().getInteger(R.integer.detail_request_code);

        int p = tracker.getSelection().hashCode();

        // To-do item of Main
        if((type == RecyclerViewItemType.TO_DO_MAIN) && (mainFragment != null)) {
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

            String extraName = context.getResources().getString(R.string.request_code);
            int code = context.getResources().getInteger(R.integer.detail_request_code);

            int p = tracker.getSelection().hashCode();
            Task selectedTask = (Task) list.get(p);
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.setItemId(selectedTask.getItemId());

            Intent intent = new Intent(taskManagementActivity, DetailActivity.class);
            intent.putExtra(extraName, code);
            intent.putExtra(parentView.getResources().getString(R.string.item_id)
                    , (Serializable) toDoItem);

            taskManagementActivity.startActivityForResult(intent, code);
        }
        else {
            Log.e("RecyclerViewSetter", "Bring up Detail: Don't set up Task Management Activity");
        }
    }

    private void selectLocation(){
        if(locationSearchActivity != null) {
            Context context = parentView.getContext();

            String extraName = context.getResources().getString(R.string.location_search_code);
            int code = context.getResources().getInteger(R.integer.location_search_code);

            int p = tracker.getSelection().hashCode();
            Location selectedLocation = (Location) list.get(p);

            Intent intent = new Intent(locationSearchActivity, AddItemActivity.class);
            Location location = new Location();
            location.setLocationId(selectedLocation.getLocationId());
            location.setAddressName(selectedLocation.getAddressName());
            location.setPlaceName(selectedLocation.getPlaceName());
            location.setLatitude(selectedLocation.getLongitude());
            location.setLongitude(selectedLocation.getLongitude());
            intent.putExtra(extraName, code);
            intent.putExtra(parentView.getResources().getString(R.string.item_id), location);
            locationSearchActivity.startActivityForResult(intent, code);
        }
        else {
            Log.e("RecyclerViewSetter", "Bring up Detail: Don't set up Location Search Activity");
        }
    }

}
