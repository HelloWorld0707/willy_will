package com.willy.will.search.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.main.model.ToDoItem;
import com.willy.will.search.controller.SearchController;
import com.willy.will.search.model.Distance;
import com.willy.will.search.model.DistanceSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

    private SearchController searchCtrl = null;

    private TextInputEditText textInputEditText = null;
    private RecyclerView recyclerView = null;

    private ArrayList<ToDoItem> toDoList = null;
    private ArrayList<Group> selectedGroups = null;
    private String selectedDone = null;
    private boolean includedRepeat;
    private String startOfStartDate = null;
    private String endOfStartDate = null;
    private String startOfEndDate = null;
    private String endOfEndDate = null;
    private boolean onlyDone;
    private String startOfDoneDate = null;
    private String endOfDoneDate = null;
    private Distance selectedDistance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        resources = getResources();
        extraNameCode = resources.getString(R.string.request_code);

        searchCtrl = new SearchController(getResources());

        /** Set data **/
        current = getIntent().getStringExtra(resources.getString(R.string.current_date_key));
        initSearchSetting(getWindow().getDecorView());
        setToDoList("");
        /* ~Set data */

        /** Set Views **/
        textInputEditText = findViewById(R.id.search_edit_text);
        if(textInputEditText.hasFocus()) {
            textInputEditText.clearFocus();
        }

        recyclerView = new RecyclerViewSetter(
                R.id.search_results_recycler_view, getWindow().getDecorView(),
                RecyclerViewItemType.TO_DO, toDoList,
                R.string.selection_id_search, false
        ).setRecyclerView();
        /* ~Set Views */

        /** Set extra names of Intent **/
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
        /* ~Set extra names of Intent */
    }

    private void setToDoList(String searchText) {
        toDoList = searchCtrl.getToDoItems(
                searchText,
                toDoList, selectedGroups,
                startOfDoneDate, endOfDoneDate,
                includedRepeat,
                startOfStartDate, endOfStartDate,
                startOfEndDate, endOfEndDate);
    }

    public void backToMain(View view) {
        /** Check focusing **/
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        /* ~Check focusing */
        this.finish();
    }

    public void search(View view) {
        String searchText = textInputEditText.getText().toString();
        setToDoList(searchText);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void bringUpGroupSearchSetting(View view) {
        Intent intent = new Intent(this, GroupSearchSettingActivity.class);
        intent.putParcelableArrayListExtra(selectedGroupsKey, selectedGroups);

        code = resources.getInteger(R.integer.group_search_setting_code);
        intent.putExtra(extraNameCode, code);
        startActivityForResult(intent, code);
    }

    public void bringUpDoneRepeatSearchSetting(View view) {
        Intent intent = new Intent(this, DoneRepeatSearchSettingActivity.class);
        intent.putExtra(selectedDoneKey, selectedDone);
        intent.putExtra(includedRepeatKey, includedRepeat);

        code = resources.getInteger(R.integer.done_repeat_search_setting_code);
        intent.putExtra(extraNameCode, code);
        startActivityForResult(intent, code);
    }

    public void bringUpPeriodSearchSetting(View view) {
        Intent intent = new Intent(this, PeriodSearchSettingActivity.class);
        intent.putExtra(startOfStartDateKey, startOfStartDate);
        intent.putExtra(endOfStartDateKey, endOfStartDate);
        intent.putExtra(startOfEndDateKey, startOfEndDate);
        intent.putExtra(endOfEndDateKey, endOfEndDate);
        intent.putExtra(resources.getString(R.string.only_done_key), onlyDone);
        intent.putExtra(startOfDoneDateKey, startOfDoneDate);
        intent.putExtra(endOfDoneDateKey, endOfDoneDate);

        code = resources.getInteger(R.integer.period_search_setting_code);
        intent.putExtra(extraNameCode, code);
        startActivityForResult(intent, code);
    }

    public void bringUpDistanceSearchSetting(View view) {
        Intent intent = new Intent(this, DistanceSearchSettingActivity.class);
        intent.putExtra(selectedDistanceKey, selectedDistance);

        code = resources.getInteger(R.integer.distance_search_setting_code);
        intent.putExtra(extraNameCode, code);
        startActivityForResult(intent, code);
    }

    public void initSearchSetting(View view) {
        selectedGroups = new ArrayList<>();
        selectedDone = resources.getString(R.string.all);
        includedRepeat = true;
        startOfStartDate = current;
        endOfStartDate = "";
        startOfEndDate = "";
        endOfEndDate = current;
        onlyDone = false;
        startOfDoneDate = "";
        endOfDoneDate = "";
        selectedDistance = DistanceSet.distances.get(0);
    }

    // Receive result data from SearchSettingActivity (Setting for Search)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Resources resources = getResources();

        /** Success to receive data **/
        if(resultCode == Activity.RESULT_FIRST_USER) {
            // Group Search Setting
            if (requestCode == resources.getInteger(R.integer.group_search_setting_code)) {
                selectedGroups = data.getParcelableArrayListExtra(selectedGroupsKey);
            }
            // Done and Repeat Search Setting
            else if (requestCode == getResources().getInteger(R.integer.done_repeat_search_setting_code)) {
                selectedDone = data.getStringExtra(selectedDoneKey);
                if(selectedDone.equals(resources.getString(R.string.done))) {
                    if(!onlyDone) {
                        onlyDone = true;
                    }
                }
                else {
                    if(onlyDone) {
                        onlyDone = false;
                        startOfDoneDate = "";
                        endOfDoneDate = "";
                    }
                }
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
        /* ~Success to receive data */
    }

}
