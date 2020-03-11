package com.willy.will.common.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.will.R;
import com.willy.will.adapter.ListViewAdapter;
import com.willy.will.common.controller.ListViewHolder;
import com.willy.will.common.model.Group;
import com.willy.will.database.GroupDBController;

import java.util.ArrayList;

public class GroupManagementActivity extends AppCompatActivity {

    Button btnAdd,btnDel;
    TextView Group_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        /** Set group list view **/
        ArrayList<Group> groupList = new ArrayList<>();
        groupList = new GroupDBController(getResources()).getAllGroups();

        ListViewAdapter<Group> adapter = new ListViewAdapter<>(
                groupList,
                R.layout.item_group,
                new ListViewHolder<Group>() {
                    private ImageView groupColorView;
                    private TextView groupName;

                    @Override
                    public void setView(View convertView) {
                        groupColorView = convertView.findViewById(R.id.group_color);
                        groupName = convertView.findViewById(R.id.group_name);
                    }

                    @Override
                    public void bindData(Group data) {
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
                    }
                }
        );

        ListView groupListView = (ListView) findViewById(R.id.group_list_view);
        groupListView.setAdapter(adapter);
        /* ~Set group list view */

        btnAdd = (Button) findViewById(R.id.btnAdd);
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

    }

    /**private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnAdd:
                    String text = editText.getText().toString();
                    if(text.length() != 0){
                        Items.add(text);
                        editText.setText("");
                        Adapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.btnDel:
                    int pos;
                    pos = listView.getCheckedItemPosition();
                    if (pos != ListView.INVALID_POSITION){
                        Items.remove(pos);
                        listView.clearChoices();
                        Adapter.notifyDataSetChanged();
                    }
                    break;
            }

        }
    };**/





