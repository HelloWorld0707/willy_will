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
import com.willy.will.common.controller.ListViewHolder;
import com.willy.will.common.model.Group;
import com.willy.will.database.GroupDBController;

import java.util.ArrayList;

public class GroupManagementActivity extends AppCompatActivity {

    private Resources resources;
    private Toast noColorToast;
    private Toast noNameToast;

    private GroupDBController groupDBCtrl;
    private ListViewAdapter<Group> adapter;

    private ImageButton submitBtn;
    private ImageButton groupColorBtn;
    private TextInputEditText textInputEditText;

    private ArrayList<Group> groupList;
    private Group newGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        resources = getResources();
        noColorToast = Toast.makeText(this, resources.getString(R.string.no_group_color_text), Toast.LENGTH_SHORT);
        noNameToast = Toast.makeText(this, resources.getString(R.string.group_name_hint), Toast.LENGTH_SHORT);

        groupDBCtrl = new GroupDBController(resources);

        /** Set submit button **/
        submitBtn = findViewById(R.id.submit_button);
        int requestCode = getIntent().getIntExtra(
                resources.getString(R.string.request_code),
                getResources().getInteger(R.integer.group_setting_code)
        );
        if(requestCode == resources.getInteger(R.integer.group_management_code)) {
            submitBtn.setVisibility(View.GONE);
        }
        /* ~Set submit button */

        /****/
        textInputEditText = findViewById(R.id.group_name_edit_text);

        /** Set group list view **/
        groupList = groupDBCtrl.getAllGroups(groupList);

        adapter = new ListViewAdapter<>(
                groupList,
                R.layout.item_group,
                new ListViewHolder<Group>() {
                    private ImageView groupColorView;
                    private TextView groupName;
                    private RadioButton radioButton;

                    @Override
                    public void setView(int position, View convertView) {
                        groupColorView = convertView.findViewById(R.id.group_color);
                        groupName = convertView.findViewById(R.id.group_name);
                        radioButton = convertView.findViewById(R.id.radio_button);
                        if(!radioButton.hasOnClickListeners()) {
                            radioButton.setOnClickListener(new RadioButtonListener(position));
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
                        radioButton.setChecked(selected);
                        /* ~Set the radio button */
                    }
                }
        );

        ListView groupListView = (ListView) findViewById(R.id.group_list_view);
        groupListView.setAdapter(adapter);
        /* ~Set group list view */

        groupColorBtn = findViewById(R.id.group_color_button);
        groupColorBtn.setActivated(true);
    }

    public void submit(View view) {
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

    public void backToMain(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        // ~Check focusing
        this.finish();
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

                newGroupName = null;
                groupColorBtn.getDrawable().mutate().setTint(resources.getColor(R.color.light_gray, null));
                textInputEditText.setText("");
                groupList = groupDBCtrl.getAllGroups(groupList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Success to receive data **/
        if(resultCode == Activity.RESULT_FIRST_USER) {
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
        }
        /* ~Success to receive data */
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

}
