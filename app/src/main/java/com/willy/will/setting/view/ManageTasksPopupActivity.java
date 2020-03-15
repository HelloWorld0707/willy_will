package com.willy.will.setting.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.willy.will.R;
import com.willy.will.common.model.Group;
import com.willy.will.common.view.PopupActivity;
import com.willy.will.common.model.Task;
import com.willy.will.setting.controller.TaskManagementController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ManageTasksPopupActivity extends PopupActivity {

    private int MOVE_CODE;
    private int REMOVE_CODE;

    private Resources resources;
    private int requestCode;

    private TextView noticeView;

    private ArrayList<Task> selectedTasks;
    private int selectedGroupId;

    public ManageTasksPopupActivity() {
        super(R.layout.activity_delete_popup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = getResources();
        MOVE_CODE = resources.getInteger(R.integer.move_tasks_code);
        REMOVE_CODE = getResources().getInteger(R.integer.remove_tasks_code);
        requestCode = getIntent().getIntExtra(resources.getString(R.string.request_code), REMOVE_CODE);

        /** Set the list of tasks **/
        selectedTasks = getIntent().getParcelableArrayListExtra(
                resources.getString(R.string.selected_tasks_key)
        );
        /* Set the list of tasks */

        /** Set the selected group id and the notice text **/
        noticeView = findViewById(R.id.notice_text_view);
        if(requestCode == MOVE_CODE) {
            selectedGroupId = ((Group) getIntent().getParcelableExtra(resources.getString(R.string.group_setting_key))).getGroupId();
            noticeView.setText(resources.getString(R.string.move_msg));
        }
        /* ~Set the selected group id and the notice text */
    }

    @Override
    protected boolean setResults(Intent intent) {
        Queue<Integer> selectedToDoIds = new LinkedList<>();
        while(!selectedTasks.isEmpty()) {
            selectedToDoIds.offer(selectedTasks.remove(0).getToDoId());
        }

        TaskManagementController taskMngmntCtrl = new TaskManagementController(resources);
        if(requestCode == MOVE_CODE) {
            taskMngmntCtrl.moveTasks(selectedGroupId, selectedToDoIds);
        }
        else if(requestCode == REMOVE_CODE) {
            taskMngmntCtrl.removeTasks(selectedToDoIds);
        }
        return true;
    }
}
