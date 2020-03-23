package com.willy.will.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.main.view.MainFragment;

import java.util.ArrayList;

public class RecyclerViewSetter {

    /** For Setting RecyclerView **/
    private Activity activity;
    private MainFragment mainFragment;
    private ArrayList list;
    private SelectionTracker.SelectionPredicate predicate;
    private RecyclerViewItemType type;
    private int recyclerViewId;
    private int layoutId;
    private int selectionId;

    private SelectionTracker tracker = null;
    /* ~For Setting RecyclerView */

    /** For Group Search Setting **/
    private TextView selectingAllView = null;
    /* ~For Group Search Setting */

    private RecyclerViewSetter() {
        selectionId = R.string.no_selection_id;
    }

    // For Activity
    public RecyclerViewSetter(Activity activity,
                              int recyclerViewId,
                              RecyclerViewItemType type,
                              int layoutId,
                              ArrayList list) {
        this();
        this.activity = activity;
        mainFragment = null;
        this.recyclerViewId = recyclerViewId;
        this.type = type;
        this.layoutId = layoutId;
        this.list = list;
    }

    // For Fragment
    public RecyclerViewSetter(MainFragment mainFragment,
                              int recyclerViewId,
                              RecyclerViewItemType type,
                              int layoutId,
                              ArrayList list) {
        this();
        this.mainFragment = mainFragment;
        activity = mainFragment.getActivity();
        this.recyclerViewId = recyclerViewId;
        this.type = type;
        this.layoutId = layoutId;
        this.list = list;
    }

    // For Activity with selection
    public RecyclerViewSetter(Activity activity,
                              int recyclerViewId,
                              RecyclerViewItemType type,
                              int layoutId,
                              ArrayList list,
                              int selectionId,
                              boolean multipleSelection) {
        this.activity = activity;
        this.recyclerViewId = recyclerViewId;
        this.type = type;
        this.layoutId = layoutId;
        this.list = list;
        this.selectionId = selectionId;

        /** Set selection predicate for tracker **/
        if(selectionId == R.string.no_selection_id) {
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
    }

    // Set RecyclerView, LayoutManager, Adapter, and Tracker
    public RecyclerView setRecyclerView() {
        RecyclerView recyclerView;
        Context context;
        if(mainFragment != null) {
            recyclerView = mainFragment.getRootView().findViewById(recyclerViewId);
            context = mainFragment.getContext();
        }
        else {
            recyclerView = activity.findViewById(recyclerViewId);
            context = activity;
        }
        recyclerView.setHasFixedSize(true);

        /** Set LayoutManager **/
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        /* ~Set LayoutManager */

        /** Set Adapter **/
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(layoutId, type, list, activity);
        recyclerView.setAdapter(adapter);
        /* ~Set Adapter */

        /** Set Selected Position **/
        // Group Management
        if (type == RecyclerViewItemType.GROUP) {
            adapter.setSelectedPosition(activity.getResources().getInteger(R.integer.no_group_id));
        }
        /* ~Set Selected Position */
        /** Or Set Tracker **/
        else if(selectionId != R.string.no_selection_id) {
            tracker = new SelectionTracker.Builder(
                    activity.getResources().getString(selectionId),
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

                    // Group
                    if (type == RecyclerViewItemType.GROUP_SEARCH) {
                        changeGroupItem();
                    }
                    // Done or Loop
                    else if (type == RecyclerViewItemType.DONE_SEARCH ||
                            type == RecyclerViewItemType.LOOP_SEARCH) {
                        // Nothing
                    }
                    // ERROR: Wrong type
                    else {
                        Log.e("RecyclerViewSetter", "Selection: Wrong type");
                    }
                }

            });
        }
        // ERROR: Wrong selection ID
        else {
        }
        /* ~Set Tracker */

        /** Set Selecting All Text View of Group Search Setting **/
        if(type == RecyclerViewItemType.GROUP_SEARCH) {
            selectingAllView = activity.findViewById(R.id.selecting_all);
        }
        /* ~Set Selecting All Text View of Group Search Setting */

        return recyclerView;
    }

    private void changeGroupItem() {
        if(tracker.getSelection().size() > 1) {
            selectingAllView = activity.findViewById(R.id.selecting_all);
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
