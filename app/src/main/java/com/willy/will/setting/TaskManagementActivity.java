package com.willy.will.setting;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.Task;
import com.willy.will.common.view.GroupManagementActivity;
import com.willy.will.database.TaskDBController;

import java.util.ArrayList;
import java.util.Iterator;

public class TaskManagementActivity extends AppCompatActivity {

    private String extraNameCode = null;
    private String selectedTasksKey = null;
    private Resources resources = null;
    private Toast noCheckedTask = null;

    private RecyclerView recyclerView = null;

    private ArrayList<Task> taskList = null;
    private ArrayList<Task> selectedTasks = null;

    private int code = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);

        resources = getResources();
        extraNameCode = resources.getString(R.string.request_code);
        selectedTasksKey = resources.getString(R.string.selected_tasks_key);
        noCheckedTask = Toast.makeText(this, resources.getString(R.string.no_checked_task), Toast.LENGTH_SHORT);

        /** Set data of item **/
        taskList = new TaskDBController(resources).getAllTasks(taskList);
        selectedTasks = new ArrayList<>();
        /* ~Set data of item */

        /** Set Views **/
        RecyclerViewSetter recyclerViewSetter = new RecyclerViewSetter(
                R.id.task_recycler_view, getWindow().getDecorView(),
                RecyclerViewItemType.TASK, taskList,
                R.string.selection_id_task_management, false
        );
        recyclerView = recyclerViewSetter.setRecyclerView();
        // WARNING: Only one must be assigned
        recyclerViewSetter.setFragmentAndActivities(null, null, this);
        /* ~Set Views */
    }

    public void backToMain(View view) {
        if(recyclerView != null) {
            if(!((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().hasSelection()) {
                this.finish();
            }
        }
    }

    // Move tasks to other group
    public void setGroup(View view) {
        Iterator<Task> iter = taskList.iterator();
        Task curTask = null;
        selectedTasks.clear();
        while(iter.hasNext()) {
            curTask = iter.next();
            if(curTask.isChecked()) {
                selectedTasks.add(curTask);
            }
        }

        if(selectedTasks.isEmpty()) {
            noCheckedTask.show();
        }
        else {
            Intent intent = new Intent(this, GroupManagementActivity.class);
            intent.putParcelableArrayListExtra(selectedTasksKey, selectedTasks);

            code = resources.getInteger(R.integer.move_tasks_code);
            intent.putExtra(extraNameCode, code);
            startActivityForResult(intent, code);
        }
    }

    public void removeTasks(View view) {
        Iterator<Task> iter = taskList.iterator();
        Task curTask = null;
        selectedTasks.clear();
        while(iter.hasNext()) {
            curTask = iter.next();
            if(curTask.isChecked()) {
                selectedTasks.add(curTask);
            }
        }

        if(selectedTasks.isEmpty()) {
            noCheckedTask.show();
        }
        else {
            Intent intent = new Intent(this, DeleteTasksPopupActivity.class);
            intent.putParcelableArrayListExtra(selectedTasksKey, selectedTasks);

            code = resources.getInteger(R.integer.remove_tasks_code);
            intent.putExtra(extraNameCode, code);
            startActivityForResult(intent, code);
        }
    }

    // Receive result data from Detail Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Success to receive data **/
        if(resultCode == Activity.RESULT_FIRST_USER) {
            // To-do Item Detail
            if(requestCode == getResources().getInteger(R.integer.detail_request_code)) {
                ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().clearSelection();
            }
            // Move tasks (to other group)
            else if(requestCode == resources.getInteger(R.integer.move_tasks_code)) {
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(this, resources.getString(R.string.successful_movement), Toast.LENGTH_SHORT).show();
            }
            // Remove tasks
            else if(requestCode == getResources().getInteger(R.integer.remove_tasks_code)) {
                Iterator<Task> selectIter = selectedTasks.iterator();
                while(selectIter.hasNext()) {
                    taskList.remove(selectIter.next());
                }
                selectedTasks.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(this, resources.getString(R.string.successful_delete), Toast.LENGTH_SHORT).show();
            }
        }
        /* ~Success to receive data */
    }

}
