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
import com.willy.will.common.model.Group;
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
    private TaskDBController taskDBCtrl = null;
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
        taskDBCtrl = new TaskDBController(resources);
        noCheckedTask = Toast.makeText(this, resources.getString(R.string.no_checked_task), Toast.LENGTH_SHORT);

        /** Set data of item **/
        taskList = taskDBCtrl.getAllTasks(taskList);
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

            code = resources.getInteger(R.integer.group_setting_code);
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
            Intent intent = new Intent(this, ManageTasksPopupActivity.class);
            intent.putParcelableArrayListExtra(selectedTasksKey, selectedTasks);

            code = resources.getInteger(R.integer.remove_tasks_code);
            intent.putExtra(extraNameCode, code);
            startActivityForResult(intent, code);
        }
    }

    // Receive result data from other Activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Success to receive data **/
        if(resultCode == Activity.RESULT_FIRST_USER) {
            // To-do Item Detail
            if(requestCode == resources.getInteger(R.integer.detail_request_code)) {
                ((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().clearSelection();
            }
            // Group setting (to move tasks to this group)
            else if(requestCode == resources.getInteger(R.integer.group_setting_code)) {
                String groupSettingKey = resources.getString(R.string.group_setting_key);
                Group selectedGroup = data.getParcelableExtra(groupSettingKey);

                Intent intent = new Intent(this, ManageTasksPopupActivity.class);
                intent.putParcelableArrayListExtra(selectedTasksKey, selectedTasks);
                intent.putExtra(groupSettingKey, selectedGroup);

                code = resources.getInteger(R.integer.move_tasks_code);
                intent.putExtra(extraNameCode, code);
                startActivityForResult(intent, code);
            }
            // Move tasks (to other group)
            else if(requestCode == resources.getInteger(R.integer.move_tasks_code)) {
                taskList = taskDBCtrl.getAllTasks(taskList);
                selectedTasks.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(this, resources.getString(R.string.successful_movement), Toast.LENGTH_SHORT).show();
            }
            // Remove tasks
            else if(requestCode == resources.getInteger(R.integer.remove_tasks_code)) {
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
