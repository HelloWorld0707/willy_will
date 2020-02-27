package com.willy.will.search.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.controller.App;
import com.willy.will.search.model.PopupActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class DoneRepeatSearchSettingActivity extends PopupActivity {

    private String selectedDoneKey = null;
    private String includedRepeatKey = null;

    private RecyclerView recyclerView = null;
    private CheckBox checkBox = null;

    private ArrayList<String> doneList = null;

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-18
     * Created By: Shin Minyong
     * Function: Initialization (including layout ID)
     */
    public DoneRepeatSearchSettingActivity() {
        super(R.layout.activity_done_repeat_search_setting);
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

        // Set data of item
        doneList = new ArrayList<>();
        Resources resources = App.getContext().getResources();
        doneList.add(resources.getString(R.string.all));
        doneList.add(resources.getString(R.string.undone));
        doneList.add(resources.getString(R.string.done));
        // ~Set data of item

        // Set Views
        recyclerView = new RecyclerViewSetter(
                R.id.done_search_setting_recycler_view, getWindow().getDecorView(),
                R.integer.done_search_setting_recycler_item_type, doneList,
                R.string.selection_id_done_search_setting, false
        ).setRecyclerView();
        checkBox = findViewById(R.id.repeat_check_box);
        // ~Set Views

        // Set a selected item and checkbox checking
        selectedDoneKey = resources.getString(R.string.selected_done_key);
        includedRepeatKey = resources.getString(R.string.included_repeat_key);

        String selectedDone = getIntent().getStringExtra(selectedDoneKey);
        for(int i = 0; i < doneList.size(); i++) {
            if(doneList.get(i).equals(selectedDone)) {
                long selectedPosition = Long.valueOf(i);
                ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().select(selectedPosition);
            }
        }

        boolean includedRepeat = getIntent().getBooleanExtra(includedRepeatKey, true);
        checkBox.setChecked(includedRepeat);
        // ~Set a selected item and checking the checkbox
    }

    /**
     * Last Modified: 2020-02-23
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * @param intent
     * @return true
     */
    @Override
    protected boolean setResults(Intent intent) {
        Iterator selectIter = ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().getSelection().iterator();
        int selectedIndex = Math.toIntExact((Long) selectIter.next());
        intent.putExtra(selectedDoneKey, doneList.get(selectedIndex));
        intent.putExtra(includedRepeatKey, checkBox.isChecked());
        return true;
    }

}
