package com.willy.will.common.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.willy.will.R;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.PopupActivity;
import com.willy.will.database.GroupDBController;
import com.willy.will.setting.controller.TaskManagementController;

import java.util.Queue;

public class DeleteGroupPopupActivity extends PopupActivity {

    private TextView noticeView;

    private int groupId;

    public DeleteGroupPopupActivity() {
        super(R.layout.activity_delete_popup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Group group = getIntent().getParcelableExtra(getResources().getString(R.string.group_removal_key));
        groupId = group.getGroupId();
    }

    @Override
    protected boolean setResults(Intent intent) {
        GroupDBController groupDBCtrl = new GroupDBController(getResources());
        Queue<Integer> toDoIds = groupDBCtrl.getToDoIdsByGroupId(groupId);
        new TaskManagementController(getResources()).removeTasks(toDoIds);
        groupDBCtrl.deleteGroup(groupId);
        return true;
    }

}
