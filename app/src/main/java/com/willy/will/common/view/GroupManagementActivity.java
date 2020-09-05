package com.willy.will.common.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.controller.AdMobController;
import com.willy.will.common.controller.AscendingGroupByName;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.database.GroupDBController;

import java.util.ArrayList;
import java.util.Collections;

public class GroupManagementActivity extends AppCompatActivity {

    private Resources resources;
    private Toast noColorToast;
    private Toast noNameToast;
    private int noGroupId;

    private GroupDBController groupDBCtrl;
    private AscendingGroupByName ascendingGroupByName;
    private InputMethodManager inputMethodManager;

    private ImageButton submitBtn;
    private ImageButton groupColorBtn;
    private TextInputEditText textInputEditText;
    private RecyclerView recyclerView;

    private ArrayList<Group> groupList;
    private Group newGroup;
    private static boolean removing;

    private int requestCode;
    private boolean groupListChanged;
    private AdMobController adMobController = new AdMobController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        resources = getResources();
        noColorToast = Toast.makeText(this, resources.getString(R.string.no_group_color_text), Toast.LENGTH_SHORT);
        noNameToast = Toast.makeText(this, resources.getString(R.string.group_name_hint), Toast.LENGTH_SHORT);
        noGroupId = resources.getInteger(R.integer.no_group_id);

        groupDBCtrl = new GroupDBController(resources);
        ascendingGroupByName = new AscendingGroupByName();

        /** Set start to remove button and submit button **/
        submitBtn = findViewById(R.id.submit_button);
        requestCode = getIntent().getIntExtra(
                resources.getString(R.string.request_code),
                resources.getInteger(R.integer.group_setting_code)
        );
        if(requestCode == resources.getInteger(R.integer.group_setting_code)) {
            removing = false;
        }
        else if(requestCode == resources.getInteger(R.integer.group_management_code)) {
            submitBtn.setVisibility(View.GONE);
            removing = true;
        }
        /* ~Set start to remove button and submit button */

        /** Set Text Input Edit **/
        textInputEditText = findViewById(R.id.group_name_edit_text);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        /* ~Set Text Input Edit */

        /** Set group list view **/
        setGroupList();

        recyclerView = new RecyclerViewSetter(
                this, R.id.group_recycler_view,
                RecyclerViewItemType.GROUP, R.layout.item_group,
                groupList
        ).setRecyclerView();
        /* ~Set group list view */

        groupColorBtn = findViewById(R.id.group_color_button);
        groupColorBtn.setActivated(true);

        groupListChanged = false;

        /** loading Ad*/
        adMobController.callingAdmob();
    }

    private void setGroupList() {
        groupList = groupDBCtrl.getAllGroups(groupList);
        Group noGroup = groupList.remove(noGroupId);
        Collections.sort(groupList, ascendingGroupByName);
        groupList.add(noGroupId, noGroup);
    }

    public void backToMain(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            onSoftKeyboardDown(view);
        }
        // ~Check focusing

        this.finish();
    }

    @Override
    public void finish() {
        if(requestCode == resources.getInteger(R.integer.group_management_code)) {
            if(groupListChanged) {
                setResult(resources.getInteger(R.integer.group_change_return_code));
            }
        }
        super.finish();
    }

    public void submit(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            onSoftKeyboardDown(view);
        }
        // ~Check focusing

        Intent intent = new Intent();
        int p = ((RecyclerViewAdapter) recyclerView.getAdapter()).getSelectedPosition();
        intent.putExtra(
                resources.getString(R.string.group_setting_key),
                groupList.get(p)
        );
        setResult(RESULT_FIRST_USER, intent);
        this.finish();
    }

    public void bringUpGroupColorSetting(View view) {
        Intent intent = new Intent(this, GroupColorSettingActivity.class);
        int code = resources.getInteger(R.integer.group_color_setting_code);
        startActivityForResult(intent, code);
    }

    public void addGroup(View view) {
        if(newGroup == null) {
            noColorToast.show();
        }
        else {
            String newGroupName = textInputEditText.getText().toString();
            if(newGroupName == null || newGroupName.isEmpty()) {
                noNameToast.show();
            }
            else {
                newGroup.setGroupName(newGroupName);
                groupDBCtrl.insertGroup(newGroup);

                newGroup = null;
                groupColorBtn.getDrawable().mutate().setTint(resources.getColor(R.color.light_gray, null));
                textInputEditText.setText("");
                setGroupList();
                ((RecyclerViewAdapter) recyclerView.getAdapter()).setSelectedPosition(noGroupId);
                recyclerView.getAdapter().notifyDataSetChanged();
                onSoftKeyboardDown(view);
                groupListChanged = true;
            }
        }
    }

    public void onSoftKeyboardDown(View view) {
        inputMethodManager.hideSoftInputFromWindow(textInputEditText.getWindowToken(), 0);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** Success to receive data **/
        if(resultCode == Activity.RESULT_FIRST_USER) {
            // Set group color (to add new group)
            if (requestCode == resources.getInteger(R.integer.group_color_setting_code)) {
                int colorInt = data.getIntExtra(
                        resources.getString(R.string.group_color_setting_key),
                        resources.getColor(R.color.light_gray, null)
                );

                if(newGroup == null) {
                    newGroup = new Group();
                }
                String selectedColor = String.format("#%08X", (0xFFFFFFFF & colorInt));
                newGroup.setGroupColor(selectedColor);

                groupColorBtn.getDrawable().mutate().setTint(colorInt);
            }
            // Remove group
            else if(requestCode == resources.getInteger(R.integer.remove_group_code)) {
                setGroupList();
                ((RecyclerViewAdapter) recyclerView.getAdapter()).setSelectedPosition(noGroupId);
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(this, resources.getString(R.string.successful_delete), Toast.LENGTH_SHORT).show();
                groupListChanged = true;
            }
        }
        /* ~Success to receive data */
    }

    public static boolean isRemoving() {
        return removing;
    }

}
