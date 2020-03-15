package com.willy.will.search.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.view.PopupActivity;
import com.willy.will.common.model.RecyclerViewItemType;

import java.util.ArrayList;
import java.util.Iterator;

public class LoopSearchSettingActivity extends PopupActivity {

    private String selectedLoopKey = null;

    private RecyclerView recyclerView = null;

    private ArrayList<String> loopList = null;

    // Initialization (including layout ID)
    public LoopSearchSettingActivity() {
        super(R.layout.activity_loop_search_setting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Set data of item **/
        loopList = new ArrayList<>();
        Resources resources = getResources();
        loopList.add(resources.getString(R.string.all));
        loopList.add(resources.getString(R.string.not_loop));
        loopList.add(resources.getString(R.string.loop));
        /* ~Set data of item */

        /** Set RecyclerView **/
        recyclerView = new RecyclerViewSetter(
                R.id.loop_search_setting_recycler_view, getWindow().getDecorView(),
                RecyclerViewItemType.LOOP_SEARCH, loopList,
                R.string.selection_id_loop_search_setting, false
        ).setRecyclerView();
        /* ~Set RecyclerView */

        /** Set a selected item **/
        selectedLoopKey = resources.getString(R.string.selected_loop_key);

        String selectedLoop = getIntent().getStringExtra(selectedLoopKey);
        for(int i = 0; i < loopList.size(); i++) {
            if(loopList.get(i).equals(selectedLoop)) {
                long selectedPosition = Long.valueOf(i);
                ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().select(selectedPosition);
            }
        }
        /* ~Set a selected item */
    }

    @Override
    protected boolean setResults(Intent intent) {
        Iterator selectIter = ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().getSelection().iterator();
        int selectedIndex = Math.toIntExact((Long) selectIter.next());
        intent.putExtra(selectedLoopKey, loopList.get(selectedIndex));
        return true;
    }

}
