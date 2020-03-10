package com.willy.will.add.view;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.RecyclerViewItemType;

import java.util.ArrayList;

public class AddItem_important extends AppCompatActivity {
    private String selectedDoneKey = null;
    private RecyclerView recyclerView = null;
    private ArrayList<String> importantList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        importantList = new ArrayList<>();
        Resources resources = getResources();
        importantList.add(resources.getString(R.string.First));
        importantList.add(resources.getString(R.string.Second));
        importantList.add(resources.getString(R.string.Third));
        importantList.add(resources.getString(R.string.empty));

        recyclerView = new RecyclerViewSetter(
                R.id.important_recycler_view, getWindow().getDecorView(),
                RecyclerViewItemType.DONE_SEARCH, importantList,
                R.string.selection_id_done_search_setting, false
        ).setRecyclerView();

        /** Set a selected item **/
        selectedDoneKey = resources.getString(R.string.selected_done_key);

        String selectedDone = getIntent().getStringExtra(selectedDoneKey);
        for(int i = 0; i < importantList.size(); i++) {
            if(importantList.get(i).equals(selectedDone)) {
                long selectedPosition = Long.valueOf(i);
                ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().select(selectedPosition);
            }
        }
        /* ~Set a selected item */
    }
    /**@Override
    protected boolean setResults(Intent intent) {
        Iterator selectIter = ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().getSelection().iterator();
        int selectedIndex = Math.toIntExact((Long) selectIter.next());
        intent.putExtra(selectedDoneKey, importantList.get(selectedIndex));
        return true;
    }**/

}
