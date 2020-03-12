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
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.search.controller.SearchController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String selectedGroupsKey = null;
    private String selectedDoneKey = null;
    private String startOfStartDateKey = null;
    private String endOfStartDateKey = null;
    private String startOfEndDateKey = null;
    private String endOfEndDateKey = null;
    private String startOfDoneDateKey = null;
    private String endOfDoneDateKey = null;
    private String selectedLoopKey = null;

    private String extraNameCode = null;
    private Resources resources = null;
    private int code = 0;
    private Group currentGroup = null;
    private String current = null;

    private SearchController searchCtrl = null;
    private InputMethodManager inputMethodManager = null;

    private TextInputEditText textInputEditText = null;
    private RecyclerView recyclerView = null;

    private ArrayList<ToDoItem> toDoList = null;
    private ArrayList<Group> selectedGroups = null;
    private String selectedDone = null;
    private String selectedLoop = null;
    private String startOfStartDate = null;
    private String endOfStartDate = null;
    private String startOfEndDate = null;
    private String endOfEndDate = null;
    private boolean onlyDone;
    private String startOfDoneDate = null;
    private String endOfDoneDate = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        resources = getResources();
        extraNameCode = resources.getString(R.string.request_code);

        searchCtrl = new SearchController(getResources(), recyclerView);

        /** Set data **/
        currentGroup = getIntent().getParcelableExtra(resources.getString(R.string.current_group_key));
        current = getIntent().getStringExtra(resources.getString(R.string.current_date_key));
        initSearchSetting(getWindow().getDecorView());
        setToDoList("");
        /* ~Set data */

        /** Set Views **/
        textInputEditText = findViewById(R.id.search_edit_text);
        if(textInputEditText.hasFocus()) {
            textInputEditText.clearFocus();
        }
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        RecyclerViewSetter recyclerViewSetter = new RecyclerViewSetter(
                R.id.search_results_recycler_view, getWindow().getDecorView(),
                RecyclerViewItemType.TO_DO_SEARCH, toDoList,
                R.string.selection_id_search, false
        );
        recyclerView = recyclerViewSetter.setRecyclerView();
        // WARNING: Only one must be assigned
        recyclerViewSetter.setFragmentAndActivities(null, this, null);
        /* ~Set Views */

        /** Set extra names of Intent **/
        selectedGroupsKey = resources.getString(R.string.selected_groups_key);
        selectedDoneKey = resources.getString(R.string.selected_done_key);
        selectedLoopKey = resources.getString(R.string.selected_loop_key);
        startOfStartDateKey = resources.getString(R.string.start_of_start_date_key);
        endOfStartDateKey = resources.getString(R.string.end_of_start_date_key);
        startOfEndDateKey = resources.getString(R.string.start_of_end_date_key);
        endOfEndDateKey = resources.getString(R.string.end_of_end_date_key);
        startOfDoneDateKey = resources.getString(R.string.start_of_done_date_key);
        endOfDoneDateKey = resources.getString(R.string.end_of_done_date_key);
        /* ~Set extra names of Intent */
    }

    private void setToDoList(String searchText) {
        toDoList = searchCtrl.getToDoItems(
                searchText,
                toDoList,
                selectedGroups,
                selectedDone, startOfDoneDate, endOfDoneDate,
                selectedLoop,
                startOfStartDate, endOfStartDate,
                startOfEndDate, endOfEndDate);
    }

    public void backToMain(View view) {
        if(recyclerView != null) {
            if(!((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().hasSelection()) {
                /** Check focusing **/
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    onDownSoftKeyboard(view);
                }
                /* ~Check focusing */
                this.finish();
            }
        }
    }

    public void search(View view) {
        onDownSoftKeyboard(view);

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

    public void bringUpDoneSearchSetting(View view) {
        Intent intent = new Intent(this, DoneSearchSettingActivity.class);
        intent.putExtra(selectedDoneKey, selectedDone);

        code = resources.getInteger(R.integer.done_search_setting_code);
        intent.putExtra(extraNameCode, code);
        startActivityForResult(intent, code);
    }

    public void bringUpLoopSearchSetting(View view) {
        Intent intent = new Intent(this, LoopSearchSettingActivity.class);
        intent.putExtra(selectedLoopKey, selectedLoop);

        code = resources.getInteger(R.integer.loop_search_setting_code);
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

    public void initSearchSetting(View view) {
        if(selectedGroups == null) {
            selectedGroups = new ArrayList<>();
        }
        else {
            selectedGroups.clear();
        }
        if(currentGroup != null) {
            selectedGroups.add(currentGroup);
        }
        selectedDone = resources.getString(R.string.all);
        selectedLoop = resources.getString(R.string.all);
        startOfStartDate = "";
        endOfStartDate = current;
        startOfEndDate = current;
        endOfEndDate = "";
        onlyDone = false;
        startOfDoneDate = "";
        endOfDoneDate = "";
    }

    public void onDownSoftKeyboard(View view) {
        inputMethodManager.hideSoftInputFromWindow(textInputEditText.getWindowToken(), 0);
    }

    // Receive result data from Detail Activity or Search Setting Activity (Setting for Search)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Resources resources = getResources();

        /** Success to receive data **/
        if(resultCode == Activity.RESULT_FIRST_USER) {
            // To-do Item Detail
            if(requestCode == resources.getInteger(R.integer.detail_request_code)) {
                ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().clearSelection();
            }
            // Group Search Setting
            if (requestCode == resources.getInteger(R.integer.group_search_setting_code)) {
                selectedGroups = data.getParcelableArrayListExtra(selectedGroupsKey);
            }
            // Done Search Setting
            else if (requestCode == getResources().getInteger(R.integer.done_search_setting_code)) {
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
            }
            // Loop Search Setting
            else if (requestCode == getResources().getInteger(R.integer.loop_search_setting_code)) {
                selectedLoop = data.getStringExtra(selectedLoopKey);
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
        }
        /* ~Success to receive data */
    }

}
