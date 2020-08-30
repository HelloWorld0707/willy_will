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

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.controller.AdMobController;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.search.controller.SearchController;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private String selectedGroupsKey = null;
    private String selectedDoneKey = null;
    private String startOfPeriodKey = null;
    private String endOfPeriodKey = null;
    private String selectedLoopKey = null;

    private String extraNameCode = null;
    private Resources resources = null;
    private int code = 0;
    private Group currentGroup = null;
    private String current = null;
    private String searchText = null;

    private SearchController searchCtrl = null;
    private InputMethodManager inputMethodManager = null;

    private TextInputEditText textInputEditText = null;
    private RecyclerView recyclerView = null;

    private ArrayList<ToDoItem> toDoList = null;
    private ArrayList<Group> selectedGroups = null;
    private String selectedDone = null;
    private String selectedLoop = null;
    private String startOfPeriod = null;
    private String endOfPeriod = null;

    private boolean itemChanged;
    private AdMobController adMobController = new AdMobController(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        resources = getResources();
        extraNameCode = resources.getString(R.string.request_code);

        searchCtrl = new SearchController(getResources());

        /** Initialize data **/
        currentGroup = getIntent().getParcelableExtra(resources.getString(R.string.current_group_key));
        current = getIntent().getStringExtra(resources.getString(R.string.current_date_key));
        initSearchSetting(getWindow().getDecorView());
        /* ~Initialize data */

        /** Set Views **/
        textInputEditText = findViewById(R.id.search_edit_text);
        if(textInputEditText.hasFocus()) {
            textInputEditText.clearFocus();
        }
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        searchText = textInputEditText.getText().toString();
        setToDoList(searchText);

        recyclerView = new RecyclerViewSetter(
                this, R.id.search_results_recycler_view,
                RecyclerViewItemType.TO_DO_SEARCH, R.layout.item_main,
                toDoList
        ).setRecyclerView();
        /* ~Set Views */

        /** Set extra names of Intent **/
        selectedGroupsKey = resources.getString(R.string.selected_groups_key);
        selectedDoneKey = resources.getString(R.string.selected_done_key);
        selectedLoopKey = resources.getString(R.string.selected_loop_key);
        startOfPeriodKey = resources.getString(R.string.start_of_period_key);
        endOfPeriodKey = resources.getString(R.string.end_of_period_key);
        /* ~Set extra names of Intent */

        itemChanged = false;

        /**load ad**/
        adMobController.callingAdmob();
        /*~load ad*/
    }

    private void setToDoList(String searchText) {
        toDoList = searchCtrl.getToDoItems(
                searchText,
                toDoList,
                selectedGroups,
                selectedDone,
                selectedLoop,
                startOfPeriod, endOfPeriod);
    }

    public void backToMain(View view) {
        /** Check focusing **/
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            onSoftKeyboardDown(view);
        }
        /* ~Check focusing */

        this.finish();
    }

    @Override
    public void finish() {
        if(itemChanged) {
            setResult(resources.getInteger(R.integer.item_change_return_code));
        }
        else {
            setResult(RESULT_CANCELED);
        }

        super.finish();
    }

    public void search(View view) {
        onSoftKeyboardDown(view);

        searchText = textInputEditText.getText().toString();
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
        intent.putExtra(startOfPeriodKey, startOfPeriod);
        intent.putExtra(endOfPeriodKey, endOfPeriod);

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
        startOfPeriod = current;
        endOfPeriod = current;
    }

    public void onSoftKeyboardDown(View view) {
        inputMethodManager.hideSoftInputFromWindow(textInputEditText.getWindowToken(), 0);
    }

    // Receive result data from Detail Activity or Search Setting Activity (Setting for Search)
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
            // Done Search Setting
            else if (requestCode == getResources().getInteger(R.integer.done_search_setting_code)) {
                selectedDone = data.getStringExtra(selectedDoneKey);
            }
            // Loop Search Setting
            else if (requestCode == getResources().getInteger(R.integer.loop_search_setting_code)) {
                selectedLoop = data.getStringExtra(selectedLoopKey);
            }
            // Period Search Setting
            else if (requestCode == getResources().getInteger(R.integer.period_search_setting_code)) {
                startOfPeriod = data.getStringExtra(startOfPeriodKey);
                endOfPeriod = data.getStringExtra(endOfPeriodKey);
            }
        }
        // Return from DetailActivity
        else if(resultCode == resources.getInteger(R.integer.item_change_return_code)) {
            setToDoList(searchText);
            recyclerView.getAdapter().notifyDataSetChanged();
            itemChanged = true;
        }
        /* ~Success to receive data */
    }

}
