package com.willy.will.common.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.will.R;
import com.willy.will.adapter.ListViewAdapter;
import com.willy.will.common.controller.ListViewHolder;
import com.willy.will.common.model.Group;
import com.willy.will.database.GroupDBController;

import java.util.ArrayList;

public class GroupManagementActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 777;

    private ListViewAdapter<Group> adapter;

    private ImageButton submitBtn;
    Button btnAdd,btnDel;
    TextView Group_Text;
    TextView txt_color;

    private ArrayList<Group> groupList;

    private String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        /** Set submit button **/
        submitBtn = findViewById(R.id.submit_button);
        int requestCode = getIntent().getIntExtra(getResources().getString(R.string.request_code), getResources().getInteger(R.integer.group_setting_code));
        if(requestCode == getResources().getInteger(R.integer.group_management_code)) {
            submitBtn.setVisibility(View.GONE);
        }
        /* ~Set submit button */

        /** Set group list view **/
        groupList = new GroupDBController(getResources()).getAllGroups();

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

        btnAdd = (Button) findViewById(R.id.btn_color);
    }

    public void toadd(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        // ~Check focusing
        this.finish();
    }

    public void bringUpGroupColor(View view) {
        Intent intent = new Intent(getApplicationContext(), Group_Color.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            result = data.getStringExtra("result");
            txt_color.setText(result);

            // 색선택되면 화면에 뜨게해야함..
        }
    }

    public void submitSetting(View view) {
        Intent intent = new Intent();
        intent.putExtra(
                getResources().getString(R.string.group_setting_key),
                groupList.get(adapter.getSelectedPosition())
        );
        setResult(RESULT_FIRST_USER, intent);
        this.finish();
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
