package com.willy.will.setting;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.Task;
import com.willy.will.database.TaskDBController;

import java.util.ArrayList;

public class TaskManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView = null;

    private ArrayList<Task> taskList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);

        /** Set data of item **/
        taskList = new TaskDBController(getResources()).getAllTasks();
        /* ~Set data of item */

        /** Set Views **/
        RecyclerViewSetter recyclerViewSetter = new RecyclerViewSetter(
                R.id.task_recycler_view, getWindow().getDecorView(),
                RecyclerViewItemType.TASK, taskList,
                R.string.selection_id_task_management, true
        );
        recyclerView = recyclerViewSetter.setRecyclerView();
        // WARNING: Only one must be assigned
        recyclerViewSetter.setFragmentAndActivities(null, null, this);
        /* ~Set Views */
    }

    public void backToMain(View view) {
        /*if(recyclerView != null) {
            if(!((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().hasSelection()) {
                *//** Check focusing **//*
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                *//* ~Check focusing *//*
                this.finish();
            }
        }*/
        this.finish();
    }

    // Move tasks to other group
    public void setGroup(View view) {
        //
    }

    public void removeTasks(View view) {
        //
    }

}
