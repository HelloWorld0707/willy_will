package com.willy.will.setting;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.willy.will.R;
import com.willy.will.common.model.PopupActivity;
import com.willy.will.common.model.Task;

import java.util.ArrayList;

public class DeleteTasksPopupActivity extends PopupActivity {

    private Resources resources = null;

    private ArrayList<Task> selectedTasks = null;

    public DeleteTasksPopupActivity() {
        super(R.layout.activity_delete_popup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = getResources();

        selectedTasks = getIntent().getParcelableArrayListExtra(
                resources.getString(R.string.selected_tasks_key)
        );
    }

    @Override
    protected boolean setResults(Intent intent) {
        new TaskManagementController(resources).removeTasks(selectedTasks);
        return true;
    }
}
