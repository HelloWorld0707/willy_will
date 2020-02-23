package com.willy.will.adapter;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.detail.view.activityDetail;

import java.util.ArrayList;

public class RecyclerViewSetter {

    // Common (also written in RecyclerViewHolder)
    private int TO_DO_CODE = 0;
    private int GROUP_CODE = 0;
    private int DONE_CODE = 0;
    private int DISTANCE_CODE = 0;
    // ~Common (also written in RecyclerViewHolder)

    // For Setting RecyclerView
    private View parentView = null;
    private Resources resources = null;
    private ArrayList list = null;
    private SelectionTracker.SelectionPredicate predicate = null;
    private int tId = 0;
    private int recyclerId = 0;
    private int selectId = 0;

    private RecyclerViewAdapter adapter = null;
    private SelectionTracker tracker = null;
    // ~For Setting RecyclerView

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-17
     * Created By: Shin Minyong
     * Function: Initialization
     * @param recyclerViewId
     * @param view
     * @param typeId
     * @param dataSet
     * @param selectionId
     * @param multipleSelection
     */
    public RecyclerViewSetter(int recyclerViewId,
                              View view,
                              int typeId,
                              ArrayList dataSet,
                              int selectionId,
                              boolean multipleSelection) {
        recyclerId = recyclerViewId;
        parentView = view;
        tId = typeId;
        list = dataSet;
        selectId = selectionId;

        // Set selection predicate for tracker
        if(multipleSelection) {
            predicate = SelectionPredicates.<Long>createSelectAnything();
        }
        else {
            predicate = SelectionPredicates.createSelectSingleAnything();
        }
        // ~Set selection predicate for tracker

        // Set resources
        resources = parentView.getResources();
        // Set codes by type (also written in RecyclerViewHolder:RecyclerViewHolder)
        TO_DO_CODE = resources.getInteger(R.integer.to_do_recycler_item_type);
        GROUP_CODE = resources.getInteger(R.integer.group_search_setting_recycler_item_type);
        DONE_CODE = resources.getInteger(R.integer.done_search_setting_recycler_item_type);
        DISTANCE_CODE = resources.getInteger(R.integer.distance_search_setting_recycler_item_type);
        // ~Set codes by type (also written in RecyclerViewHolder:RecyclerViewHolder)
    }

    /**
     * Last Modified: 2020-02-23
     * Last Modified By: Shin Minyong
     * Created: 2020-02-17
     * Created By: Shin Minyong
     * Function: Set RecyclerView
     * Set RecyclerView, LayoutManager, Adapter, and Tracker
     * @return recyclerView
     */
    public RecyclerView setRecyclerView() {
        RecyclerView recyclerView = parentView.findViewById(recyclerId);
        recyclerView.setHasFixedSize(true);

        // set LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(parentView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // set the type
        final int TYPE = resources.getInteger(tId);
        // set Adapter
        adapter = new RecyclerViewAdapter(TYPE, list, this);
        recyclerView.setAdapter(adapter);



        //swipe for delete
        if(TYPE == TO_DO_CODE) {
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT /*| ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP*/) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    Toast.makeText(parentView.getContext(), "on Move", Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    Toast.makeText(parentView.getContext(), "on Swiped ", Toast.LENGTH_SHORT).show();
                    //Remove swiped item from list and notify the RecyclerView
                    int position = viewHolder.getAdapterPosition();
                    list.remove(position);
                    //adapter.notifyDataSetChanged();
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, list.size());
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
            //~swipe for delete
        }

        // set Tracker
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
                    //changeToDoItem();
                }
                // Group
                else if (TYPE == GROUP_CODE) {
                    changeGroupItem();
                }
                // Done
                else if (TYPE == DONE_CODE) {
                    changeDoneItem();
                }
                // Distance
                else if (TYPE == DISTANCE_CODE) {
                    changeDistanceItem();
                }
                // ERROR: Wrong type
                else {
                    Log.e("RecyclerViewSetter", "Setting: Wrong type");
                }
            }

        });

        return recyclerView;
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-17
     * Created By: Shin Minyong
     * Function: Change To-do Item
     * Change To-do Item of Main View or Search View on selection changed
     */
    private void changeToDoItem(View view) {
        Intent intent = new Intent(parentView.getContext(), activityDetail.class);
        parentView.getContext().startActivity(intent);
    }

    /**
     * Last Modified: 2020-02-19
     * Last Modified By: Shin Minyong
     * Created: 2020-02-17
     * Created By: Shin Minyong
     * Function: Change Group Item
     * Change Group Item of Group Search Setting View on selection changed
     */
    private void changeGroupItem() {
        if(tracker.hasSelection()) {
            TextView selectingAllView = parentView.findViewById(R.id.selecting_all);
            if(selectingAllView.isSelected()) {
                selectingAllView.setSelected(false);
            }
        }
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-19
     * Created By: Shin Minyong
     * Function: Change Done Item
     * Change Done Item of done and Repeat Search Setting View on selection changed
     */
    private void changeDoneItem() {
        //
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-19
     * Created By: Shin Minyong
     * Function: Change Distance Item
     * Change Distance Item of Distance Search Setting View on selection changed
     */
    private void changeDistanceItem() {
        //
    }

    /**
     * Last Modified: 2020-02-19
     * Last Modified By: Shin Minyong
     * Created: 2020-02-17
     * Created By: Shin Minyong
     * Function: Get Layout ID for onCreateViewHolder of RecyclerViewAdapter
     * Layout ID is layout filename without extension(.xml)
     * @param type
     * @return
     */
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
