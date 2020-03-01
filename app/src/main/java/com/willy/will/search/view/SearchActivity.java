package com.willy.will.search.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.Group;
import com.willy.will.main.model.mainListItem;
import com.willy.will.search.model.Distance;
import com.willy.will.search.model.DistanceSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class SearchActivity extends AppCompatActivity {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String selectedGroupsKey = null;
    private String selectedDoneKey = null;
    private String includedRepeatKey = null;
    private String startOfStartDateKey = null;
    private String endOfStartDateKey = null;
    private String startOfEndDateKey = null;
    private String endOfEndDateKey = null;
    private String startOfDoneDateKey = null;
    private String endOfDoneDateKey = null;
    private String selectedDistanceKey = null;

    private String extraNameCode = null;
    private Resources resources = null;
    private int code = 0;
    private String current = null;

    private TextInputEditText textInputEditText = null;
    private RecyclerView recyclerView = null;

    private ArrayList<mainListItem> toDoList = null;
    private ArrayList<Group> selectedGroups = null;
    private String selectedDone = null;
    private boolean includedRepeat;
    private String startOfStartDate = null;
    private String endOfStartDate = null;
    private String startOfEndDate = null;
    private String endOfEndDate = null;
    private String startOfDoneDate = null;
    private String endOfDoneDate = null;
    private Distance selectedDistance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        resources = getResources();
        extraNameCode = resources.getString(R.string.request_code);

        // Set data
        current = getIntent().getStringExtra(resources.getString(R.string.current_date_key));

        toDoList = new ArrayList<>();
        initSearchSetting(getWindow().getDecorView());
        //search(getWindow().getDecorView());
        mainListItem sample = new mainListItem();
        sample.setName("임시아이템");
        sample.setRank(ResourcesCompat.getDrawable(resources, R.drawable.important2, null));
        sample.setRoutine("Routine?");
        sample.setTime(current);
        toDoList.add(sample);
        // ~Set data

        // Set Views
        textInputEditText = findViewById(R.id.search_edit_text);
        if(textInputEditText.hasFocus()) {
            textInputEditText.clearFocus();
        }

        recyclerView = new RecyclerViewSetter(
                R.id.search_results_recycler_view, getWindow().getDecorView(),
                R.integer.to_do_recycler_item_type, toDoList,
                R.string.selection_id_search, false
        ).setRecyclerView();
        // ~Set Views

        // Set extra names of Intent
        selectedGroupsKey = resources.getString(R.string.selected_groups_key);
        selectedDoneKey = resources.getString(R.string.selected_done_key);
        includedRepeatKey = resources.getString(R.string.included_repeat_key);
        startOfStartDateKey = resources.getString(R.string.start_of_start_date_key);
        endOfStartDateKey = resources.getString(R.string.end_of_start_date_key);
        startOfEndDateKey = resources.getString(R.string.start_of_end_date_key);
        endOfEndDateKey = resources.getString(R.string.end_of_end_date_key);
        startOfDoneDateKey = resources.getString(R.string.start_of_done_date_key);
        endOfDoneDateKey = resources.getString(R.string.end_of_done_date_key);
        selectedDistanceKey = resources.getString(R.string.selected_distance_key);
        // ~Set extra names of Intent
    }

    /**
     * Last Modified: 2020-02-20
     * Last Modified By: Shin Minyong
     * Created: 2020-02-08
     * Created By: Shin Minyong
     * Function: Back to BaseActivity (Main View)
     * Called when the user taps the back_button
     * Check focusing
     * @param view
     */
    public void backToMain(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        // ~Check focusing
        this.finish();
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-21
     * Created By: Shin Minyong
     * Function: Prepare to search
     * Called when the user taps the search_button
     * @param view
     */
    public void search(View view) {
        // Preprocess
        String searchText = textInputEditText.getText().toString();

        ArrayList<Integer> groupIds = new ArrayList<>();
        if(selectedGroups.size() > 0) {
            Iterator<Group> selectedGroupsIter = selectedGroups.iterator();
            while (selectedGroupsIter.hasNext()) {
                groupIds.add(selectedGroupsIter.next().getGroupId());
            }
        }

        int maxDistance = selectedDistance.getLength();
        // ~Preprocess

        /*ArrayList<Integer> findedIds = findToDoItemIds(searchText, groupIds,
                selectedDone, includedRepeat,
                startOfStartDate, endOfStartDate, startOfEndDate, endOfEndDate, startOfDoneDate, endOfDoneDate
                length);

        setToDoItems(toDoList, findedIds);*/
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-08
     * Created By: Shin Minyong
     * Function: Bring up SearchSettingActivity (Group Setting for Search)
     * Called when the user taps the group_search_setting_button
     * @param view
     */
    public void bringUpGroupSearchSetting(View view) {
        Intent intent = new Intent(this, GroupSearchSettingActivity.class);
        intent.putParcelableArrayListExtra(selectedGroupsKey, selectedGroups);

        code = resources.getInteger(R.integer.group_search_setting_code);
        intent.putExtra(extraNameCode, code);
        startActivityForResult(intent, code);
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-15
     * Created By: Shin Minyong
     * Function: Bring up SearchSettingActivity (Done and Repeat Setting for Search)
     * Called when the user taps the done_repeat_search_setting_button
     * @param view
     */
    public void bringUpDoneRepeatSearchSetting(View view) {
        Intent intent = new Intent(this, DoneRepeatSearchSettingActivity.class);
        intent.putExtra(selectedDoneKey, selectedDone);
        intent.putExtra(includedRepeatKey, includedRepeat);

        code = resources.getInteger(R.integer.done_repeat_search_setting_code);
        intent.putExtra(extraNameCode, code);
        startActivityForResult(intent, code);
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-15
     * Created By: Shin Minyong
     * Function: Bring up SearchSettingActivity (Period Setting for Search)
     * Called when the user taps the period_search_setting_button
     * @param view
     */
    public void bringUpPeriodSearchSetting(View view) {
        Intent intent = new Intent(this, PeriodSearchSettingActivity.class);
        intent.putExtra(startOfStartDateKey, startOfStartDate);
        intent.putExtra(endOfStartDateKey, endOfStartDate);
        intent.putExtra(startOfEndDateKey, startOfEndDate);
        intent.putExtra(endOfEndDateKey, endOfEndDate);
        if(selectedDone.equals(resources.getString(R.string.done))) {
            intent.putExtra(resources.getString(R.string.only_done_key), true);
        }
        else {
            intent.putExtra(resources.getString(R.string.only_done_key), false);
            startOfDoneDate = "";
            endOfDoneDate = "";
        }
        intent.putExtra(startOfDoneDateKey, startOfDoneDate);
        intent.putExtra(endOfDoneDateKey, endOfDoneDate);

        code = resources.getInteger(R.integer.period_search_setting_code);
        intent.putExtra(extraNameCode, code);
        startActivityForResult(intent, code);
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-15
     * Created By: Shin Minyong
     * Function: Bring up SearchSettingActivity (Distance Setting for Search)
     * Called when the user taps the distance_search_setting_button
     * @param view
     */
    public void bringUpDistanceSearchSetting(View view) {
        Intent intent = new Intent(this, DistanceSearchSettingActivity.class);
        intent.putExtra(selectedDistanceKey, selectedDistance);

        code = resources.getInteger(R.integer.distance_search_setting_code);
        intent.putExtra(extraNameCode, code);
        startActivityForResult(intent, code);
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-21
     * Created By: Shin Minyong
     * Function: Initialize Search Setting
     * Called on Create and
     * called when the user taps the revert_search_setting_button
     * @param view
     */
    public void initSearchSetting(View view) {
        selectedGroups = new ArrayList<>();
        selectedDone = resources.getString(R.string.all);
        includedRepeat = true;
        startOfStartDate = current;
        endOfStartDate = "";
        startOfEndDate = "";
        endOfEndDate = current;
        startOfDoneDate = "";
        endOfDoneDate = "";
        selectedDistance = DistanceSet.distances.get(0);
    }

    /**
     * Last Modified: 2020-02-21
     * Last Modified By: Shin Minyong
     * Created: 2020-02-08
     * Created By: Shin Minyong
     * Function: Receive result data from SearchSettingActivity (Setting for Search)
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Resources resources = getResources();

        // Success to receive data
        if(resultCode == Activity.RESULT_FIRST_USER) {
            // Group Search Setting
            if (requestCode == resources.getInteger(R.integer.group_search_setting_code)) {
                selectedGroups = data.getParcelableArrayListExtra(selectedGroupsKey);
            }
            // Done and Repeat Search Setting
            else if (requestCode == getResources().getInteger(R.integer.done_repeat_search_setting_code)) {
                selectedDone = data.getStringExtra(selectedDoneKey);
                includedRepeat = data.getBooleanExtra(includedRepeatKey, true);
            }
            // Period Search Setting
            else if (requestCode == getResources().getInteger(R.integer.period_search_setting_code)) {
                startOfStartDate = data.getStringExtra(startOfStartDateKey);
                endOfStartDate = data.getStringExtra(endOfStartDateKey);
                startOfEndDate = data.getStringExtra(startOfEndDateKey);
                endOfEndDate = data.getStringExtra(endOfEndDateKey);
                startOfDoneDate = data.getStringExtra(startOfDoneDateKey);
                endOfDoneDate = data.getStringExtra(endOfDoneDateKey);
            }
            // Distance Search Setting
            else if (requestCode == getResources().getInteger(R.integer.distance_search_setting_code)) {
                selectedDistance = data.getParcelableExtra(selectedDistanceKey);
            }
        }
    }

}
