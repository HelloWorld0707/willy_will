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

public class DoneSearchSettingActivity extends PopupActivity {

    private String selectedDoneKey = null;

    private RecyclerView recyclerView = null;

    private ArrayList<String> doneList = null;

    // Initialization (including layout ID)
    public DoneSearchSettingActivity() {
        super(R.layout.activity_done_search_setting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Set data of item **/
        doneList = new ArrayList<>();
        Resources resources = getResources();
        doneList.add(resources.getString(R.string.all));
        doneList.add(resources.getString(R.string.not_done));
        doneList.add(resources.getString(R.string.done));
        /* ~Set data of item */

        /** Set Views **/
        recyclerView = new RecyclerViewSetter(
                this, R.id.done_search_setting_recycler_view,
                RecyclerViewItemType.DONE_SEARCH, R.layout.item_text_only,
                doneList,
                R.string.selection_id_done_search_setting, false
        ).setRecyclerView();
        /* ~Set Views */

        /** Set a selected item **/
        selectedDoneKey = resources.getString(R.string.selected_done_key);
        String selectedDone = getIntent().getStringExtra(selectedDoneKey);
        for(int i = 0; i < doneList.size(); i++) {
            if(doneList.get(i).equals(selectedDone)) {
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
        intent.putExtra(selectedDoneKey, doneList.get(selectedIndex));
        return true;
    }

}
