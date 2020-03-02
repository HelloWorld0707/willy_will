package com.willy.will.search.view;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Iterator;

public class GroupSearchSettingActivity extends PopupActivity {

    private String selectedGroupsKey = null;

    private ArrayList<Group> groupList = null;

    private TextView selectingAllView = null;
    private RecyclerView recyclerView = null;

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-17
     * Created By: Shin Minyong
     * Function: Initialization (including layout ID)
     */
    public GroupSearchSettingActivity() {
        super(R.layout.activity_group_search_setting);
    }

    /**
     * Last Modified: 2020-02-21
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set data of items
        groupList = new ArrayList<>();
        groupList.add(new Group(1, "첫 번째 그룹", "80FF3C3C"));
        groupList.add(new Group(2, "두 번째 그룹", "80FF3C3C"));
        groupList.add(new Group(3, "세 번째 그룹", "80FF3C3C"));
        groupList.add(new Group(4, "네 번째 그룹", "80FF3C3C"));
        groupList.add(new Group(5, "다섯 번째 그룹", "80FF3C3C"));
        // ~Set data of items

        // Set Views
        selectingAllView = findViewById(R.id.selecting_all);

        recyclerView = new RecyclerViewSetter(
                R.id.group_search_setting_recycler_view, getWindow().getDecorView(),
                R.integer.group_search_setting_recycler_item_type, groupList,
                R.string.selection_id_group_search_setting, true
        ).setRecyclerView();
        // ~Set Views

        // Set selected items
        selectedGroupsKey = getResources().getString(R.string.selected_groups_key);
        ArrayList<Group> selectedGroups = getIntent().getParcelableArrayListExtra(selectedGroupsKey);

        SelectionTracker tracker = ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker();
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
        else {
            selectingAllView.setSelected(true);
        }
        // ~Set selected items
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-17
     * Created By: Shin Minyong
     * Function: Select text view (selecting all groups)
     * Called if the user selects text view (selecting all groups)
     * @param view
     */
    public void onSelectAll(View view) {
        if(!selectingAllView.isSelected()) {
            selectingAllView.setSelected(true);
            ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().clearSelection();
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * Last Modified: 2020-02-21
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * @param intent
     * @return success
     */
    @Override
    protected boolean setResults(Intent intent) {
        ArrayList<Group> selectedGroups = new ArrayList<>();
        Selection selection = ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().getSelection();
        if (selection.size() > 0) {
            Iterator selectIter = selection.iterator();
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
