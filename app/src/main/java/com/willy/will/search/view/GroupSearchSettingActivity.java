package com.willy.will.search.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.PopupActivity;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.database.GroupDBController;

import java.util.ArrayList;
import java.util.Iterator;

public class GroupSearchSettingActivity extends PopupActivity {

    private String selectedGroupsKey = null;

    private ArrayList<Group> groupList = null;

    private TextView selectingAllView = null;
    private RecyclerView recyclerView = null;

    // Initialization (including layout ID)
    public GroupSearchSettingActivity() {
        super(R.layout.activity_group_search_setting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Set data of items **/
        Resources resources = getResources();
        groupList = new ArrayList<>();
        groupList.add(new Group(resources.getInteger(R.integer.ghost_item_group_id), "", ""));
        new GroupDBController().getAllGroups(groupList);
        groupList.add(new Group(resources.getInteger(R.integer.no_group_id), resources.getString(R.string.no_group), ""));
        /* ~Set data of items */

        /** Set Views **/
        selectingAllView = findViewById(R.id.selecting_all);

        recyclerView = new RecyclerViewSetter(
                R.id.group_search_setting_recycler_view, getWindow().getDecorView(),
                RecyclerViewItemType.GROUP_SEARCH, groupList,
                R.string.selection_id_group_search_setting, true
        ).setRecyclerView();
        /* ~Set Views */

        /** Set selected items **/
        selectedGroupsKey = getResources().getString(R.string.selected_groups_key);
        ArrayList<Group> selectedGroups = getIntent().getParcelableArrayListExtra(selectedGroupsKey);

        SelectionTracker tracker = ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker();
        // Previous setting
        if(selectedGroups.size() > 0) {
            Iterator<Group> selectIter = selectedGroups.iterator();
            Group selectGroup = null;
            int i = 0;
            int groupListSize = groupList.size();
            while(selectIter.hasNext()) {
                selectGroup = selectIter.next();
                for(; i < groupListSize; i++) {
                    if(groupList.get(i).getGroupId() == selectGroup.getGroupId()) {
                        tracker.select(Long.valueOf(i));
                        break;
                    }
                }
            }
        }
        // No previous setting
        else {
            selectingAllView.setSelected(true);
            tracker.select(0L);
        }
        /* ~Set selected items */
    }

    // Called if the user selects text view (selecting all groups)
    public void onSelectAll(View view) {
        if(!selectingAllView.isSelected()) {
            selectingAllView.setSelected(true);
            ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().clearSelection();
            ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().select(0L);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected boolean setResults(Intent intent) {
        ArrayList<Group> selectedGroups = new ArrayList<>();
        Selection selection = ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().getSelection();
        if (selection.size() > 1) {
            Iterator selectIter = selection.iterator();
            /** Exclude ghost position **/
            selectIter.next();
            /* ~Exclude ghost position */
            int selectedIndex = -1;
            while (selectIter.hasNext()) {
                selectedIndex = Math.toIntExact((Long) selectIter.next());
                selectedGroups.add(groupList.get(selectedIndex));
            }
        }
        intent.putParcelableArrayListExtra(getResources().getString(R.string.selected_groups_key), selectedGroups);
        return true;
    }

}
