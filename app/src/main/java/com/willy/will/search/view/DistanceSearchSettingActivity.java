package com.willy.will.search.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.search.model.Distance;
import com.willy.will.search.model.DistanceSet;
import com.willy.will.common.model.PopupActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class DistanceSearchSettingActivity extends PopupActivity {

    private String selectedDistanceKey = null;

    private RecyclerView recyclerView = null;

    private ArrayList<Distance> distanceList = null;

    // Initialization (including layout ID)
    public DistanceSearchSettingActivity() {
        super(R.layout.activity_distance_search_setting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Set a selected item **/
        selectedDistanceKey = getResources().getString(R.string.selected_distance_key);
        Distance selectedDistance = getIntent().getParcelableExtra(selectedDistanceKey);
        long selectedPosition = 0L;
        /* ~Set a selected item */

        /** Set data of item **/
        distanceList = new ArrayList<>();
        Iterator<Distance> iterator = DistanceSet.distances.iterator();
        Distance distance = null;
        while(iterator.hasNext()) {
            distance = iterator.next();
            // Set a selected item
            if(distance.getLength() == selectedDistance.getLength()) {
                selectedPosition = Long.valueOf(distanceList.size());
            }
            // Set the distanceList
            if(distance.isUse()) {
                distanceList.add(distance);
            }
        }
        /* ~Set data of item */

        /** Set RecyclerView **/
        recyclerView = new RecyclerViewSetter(
                R.id.distance_search_setting_recycler_view, getWindow().getDecorView(),
                RecyclerViewItemType.DISTANCE_SEARCH, distanceList,
                R.string.selection_id_distance_search_setting, false
        ).setRecyclerView();
        /* ~Set RecyclerView */

        /** Set a selected item **/
        ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().select(selectedPosition);
        /* ~Set a selected item */
    }

    @Override
    protected boolean setResults(Intent intent) {
        Iterator selectIter = ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().getSelection().iterator();
        int selectedIndex = Math.toIntExact((Long) selectIter.next());
        intent.putExtra(selectedDistanceKey, distanceList.get(selectedIndex));
        return true;
    }

}
