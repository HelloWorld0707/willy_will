package com.willy.will.common.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.willy.will.R;
import com.willy.will.adapter.ListViewAdapter;
import com.willy.will.adapter.ListViewHolder;
import com.willy.will.common.model.Group;
import com.willy.will.database.GroupDBController;

import java.util.ArrayList;

public class GroupManagementActivity extends AppCompatActivity {

    private Resources resources;
    private Toast noColorToast;
    private Toast noNameToast;
    private int noGroupId;

    private GroupDBController groupDBCtrl;
    private InputMethodManager inputMethodManager;
    private ListViewAdapter<Group> adapter;

    private ImageButton startToRemoveBtn;
    private ImageButton submitBtn;
    private ImageButton groupColorBtn;
    private TextInputEditText textInputEditText;

    private ArrayList<Group> groupList;
    private Group newGroup;
    private boolean removing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        resources = getResources();
        noColorToast = Toast.makeText(this, resources.getString(R.string.no_group_color_text), Toast.LENGTH_SHORT);
        noNameToast = Toast.makeText(this, resources.getString(R.string.group_name_hint), Toast.LENGTH_SHORT);
        noGroupId = resources.getInteger(R.integer.no_group_id);

        groupDBCtrl = new GroupDBController(resources);

        /** Set start to remove button and submit button **/
        startToRemoveBtn = findViewById(R.id.start_to_remove_button);
        submitBtn = findViewById(R.id.submit_button);
        int requestCode = getIntent().getIntExtra(
                resources.getString(R.string.request_code),
                getResources().getInteger(R.integer.group_setting_code)
        );
        if(requestCode == getResources().getInteger(R.integer.group_setting_code)) {
            startToRemoveBtn.setVisibility(View.GONE);
        }
        if(requestCode == resources.getInteger(R.integer.group_management_code)) {
            submitBtn.setVisibility(View.GONE);
        }
        /* ~Set start to remove button and submit button */

        /** Set Text Input Edit **/
        textInputEditText = findViewById(R.id.group_name_edit_text);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        /* ~Set Text Input Edit */

        /** Set group list view **/
        groupList = groupDBCtrl.getAllGroups(groupList);

        adapter = new ListViewAdapter<>(
                groupList,
                R.layout.item_group,
                new GroupListViewHolder(this),
                noGroupId
        );

        ListView groupListView = (ListView) findViewById(R.id.group_list_view);
        groupListView.setAdapter(adapter);
        /* ~Set group list view */

        groupColorBtn = findViewById(R.id.group_color_button);
        groupColorBtn.setActivated(true);

        removing = false;
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

    public void startToRemoveGroup(View view) {
        onSoftKeyboardDown(view);
        if(removing) {
            removing = false;
            adapter.setSelectedPosition(noGroupId);
            adapter.notifyDataSetChanged();
        }
        else {
            removing = true;
            adapter.notifyDataSetChanged();
        }
    }

    public void submit(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            onSoftKeyboardDown(view);
        }
        // ~Check focusing

        Intent intent = new Intent();
        intent.putExtra(
                resources.getString(R.string.group_setting_key),
                groupList.get(adapter.getSelectedPosition())
        );
        setResult(RESULT_FIRST_USER, intent);
        this.finish();
    }

    public void bringUpGroupColorSetting(View view) {
        Intent intent = new Intent(this, GroupColorSetting.class);
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
                groupList = groupDBCtrl.getAllGroups(groupList);
                adapter.notifyDataSetChanged();
                onSoftKeyboardDown(view);
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
                groupList = groupDBCtrl.getAllGroups(groupList);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, resources.getString(R.string.successful_delete), Toast.LENGTH_SHORT).show();
            }
        }
        /* ~Success to receive data */
    }

    class GroupListViewHolder implements ListViewHolder<Group> {
        private GroupManagementActivity activity;

        private ImageView groupColorView;
        private TextView groupName;
        private RadioButton radioButton;
        private ImageButton removeButton;

        public GroupListViewHolder(GroupManagementActivity activity) {
            this.activity = activity;
        }

        @Override
        public void setView(int position, View convertView) {
            groupColorView = convertView.findViewById(R.id.group_color);
            groupName = convertView.findViewById(R.id.group_name);
            radioButton = convertView.findViewById(R.id.radio_button);
            if(!radioButton.hasOnClickListeners()) {
                radioButton.setOnClickListener(new RadioButtonListener(position));
            }
            removeButton = convertView.findViewById(R.id.remove_button);
            if(!removeButton.hasOnClickListeners()) {
                removeButton.setOnClickListener(new RemoveButtonListner(position, activity));
            }
            if(removing) {
                radioButton.setVisibility(View.GONE);
                if(position != noGroupId) {
                    removeButton.setVisibility(View.VISIBLE);
                }
            }
            else {
                radioButton.setVisibility(View.VISIBLE);
                removeButton.setVisibility(View.GONE);
            }
        }

        @Override
        public void bindData(Group data, boolean selected) {
            /** Set the group color circle **/
            if(data.getGroupId() == 0) {
                groupColorView.setActivated(false);
            }
            else {
                groupColorView.setActivated(true);
                groupColorView.getDrawable().mutate().setTint(Color.parseColor(data.getGroupColor()));
            }
            /* ~Set the group color circle */

            /** Set the group name **/
            groupName.setText(data.getGroupName());
            /* ~Set the group name */

            /** Set the radio button **/
            if(!removing) {
                radioButton.setChecked(selected);
            }
            /* ~Set the radio button */
        }
    }

    class RadioButtonListener implements View.OnClickListener {
        private int itemId;

        public RadioButtonListener(int itemId) {
            this.itemId = itemId;
        }

        @Override
        public void onClick(View v) {
            adapter.setSelectedPosition(itemId);
            adapter.notifyDataSetChanged();
        }
    }

    class RemoveButtonListner implements View.OnClickListener {
        private GroupManagementActivity activity;
        private int itemId;

        public RemoveButtonListner(int itemId, GroupManagementActivity activity) {
            this.itemId = itemId;
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, DeleteGroupPopupActivity.class);
            intent.putExtra(resources.getString(R.string.group_removal_key), groupList.get(itemId));

            int code = resources.getInteger(R.integer.remove_group_code);
            startActivityForResult(intent, code);
        }
    }

}
