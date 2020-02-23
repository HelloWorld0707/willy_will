package com.willy.will.search.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.search.model.Distance;
import com.willy.will.search.model.DistanceSet;
import com.willy.will.search.model.PopupActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class DistanceSearchSettingActivity extends PopupActivity {

    private String selectedDistanceKey = null;

    private RecyclerView recyclerView = null;

    private ArrayList<Distance> distancelist = null;

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-18
     * Created By: Shin Minyong
     * Function: Initialization (including layout ID)
     */
    public DistanceSearchSettingActivity() {
        super(R.layout.activity_distance_search_setting);
    }

    /**
     * Last Modified: 2020-02-23
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set a selected item
        selectedDistanceKey = getResources().getString(R.string.selectedDistance);
        Distance selectedDistance = getIntent().getParcelableExtra(selectedDistanceKey);
        long selectedPosition = 0L;
        // ~Set a selected item

        // Set data of item
        distancelist = new ArrayList<>();
        Iterator<Distance> iterator = DistanceSet.distances.iterator();
        Distance distance = null;
        while(iterator.hasNext()) {
            distance = iterator.next();
            // Set a selected item
            if(distance.getLength() == selectedDistance.getLength()) {
                selectedPosition = Long.valueOf(distancelist.size());
            }
            // ~Set a selected item
            if(distance.isUse()) {
                distancelist.add(distance);
            }
        }
        // ~Set data of item

        // Set RecyclerView
        recyclerView = new RecyclerViewSetter(
                R.id.distance_search_setting_recycler_view, getWindow().getDecorView(),
                R.integer.distance_search_setting_recycler_item_type, distancelist,
                R.string.selection_id_distance_search_setting, false
        ).setRecyclerView();
        // ~Set RecyclerView

        // Set a selected item
        ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().select(selectedPosition);
        // ~Set a selected item
    }

    /**
     * Last Modified: 2020-02-23
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * @param intent
     * @return success
     */
    @Override
    protected boolean setResults(Intent intent) {
        boolean success = true;
        Distance selectedDistance = null;
        try {
            Iterator selectIter = ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().getSelection().iterator();
            int selectedIndex = Math.toIntExact((Long) selectIter.next());
            selectedDistance = distancelist.get(selectedIndex);
        }
        catch (Exception e) {
            selectedDistance = distancelist.get(0);
            success = false;
            Log.e("GroupSearchSettingActivity", "Results: "+e.getMessage());
            e.printStackTrace();
        }
        finally {
            intent.putExtra(selectedDistanceKey, selectedDistance);
            return success;
        }
    }

}
