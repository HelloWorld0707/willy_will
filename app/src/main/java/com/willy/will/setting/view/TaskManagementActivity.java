package com.willy.will.setting.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.Task;
import com.willy.will.common.view.GroupManagementActivity;
import com.willy.will.database.TaskDBController;

import java.text.ParseException;
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

    private boolean itemListChanged;

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
        try {
            taskList = taskDBCtrl.getAllTasks(taskList);
        } catch (ParseException e) {
            Log.e("TaskManagementActivity", e.toString());
            return;
        }
        selectedTasks = new ArrayList<>();
        /* ~Set data of item */

        /** Set Views **/
        recyclerView = new RecyclerViewSetter(
                this, R.id.task_recycler_view,
                RecyclerViewItemType.TASK, R.layout.item_task,
                taskList
        ).setRecyclerView();
        /* ~Set Views */

        itemListChanged = false;
    }

    public void backToMain(View view) {
        this.finish();
    }

    @Override
    public void finish() {
        if(itemListChanged) {
            setResult(resources.getInteger(R.integer.item_change_return_code));
        }
        else {
            setResult(RESULT_CANCELED);
        }
        super.finish();
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

            int code = resources.getInteger(R.integer.group_setting_code);
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

            int code = resources.getInteger(R.integer.remove_tasks_code);
            intent.putExtra(extraNameCode, code);
            startActivityForResult(intent, code);
        }
    }

    // Receive result data from other Activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // To-do Item Detail
        if(resultCode == resources.getInteger(R.integer.item_change_return_code)) {
            try {
                taskList = taskDBCtrl.getAllTasks(taskList);
            } catch (ParseException e) {
                Log.e("TaskManagementActivity", e.toString());
            }
            selectedTasks.clear();
            recyclerView.getAdapter().notifyDataSetChanged();
            itemListChanged = true;
        }
        /** Success to receive data **/
        else if(resultCode == Activity.RESULT_FIRST_USER) {
            // Group setting (to move tasks to this group)
            if(requestCode == resources.getInteger(R.integer.group_setting_code)) {
                String groupSettingKey = resources.getString(R.string.group_setting_key);
                Group selectedGroup = data.getParcelableExtra(groupSettingKey);

                Intent intent = new Intent(this, ManageTasksPopupActivity.class);
                intent.putParcelableArrayListExtra(selectedTasksKey, selectedTasks);
                intent.putExtra(groupSettingKey, selectedGroup);

                int code = resources.getInteger(R.integer.move_tasks_code);
                intent.putExtra(extraNameCode, code);
                startActivityForResult(intent, code);
            }
            // Move tasks (to other group)
            else if(requestCode == resources.getInteger(R.integer.move_tasks_code)) {
                try {
                    taskList = taskDBCtrl.getAllTasks(taskList);
                } catch (ParseException e) {
                    Log.e("TaskManagementActivity", e.toString());
                }
                selectedTasks.clear();
                recyclerView.getAdapter().notifyDataSetChanged();
                Toast.makeText(this, resources.getString(R.string.successful_movement), Toast.LENGTH_SHORT).show();
                itemListChanged = true;
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
                itemListChanged = true;
            }
        }
        /* ~Success to receive data */
    }

}
