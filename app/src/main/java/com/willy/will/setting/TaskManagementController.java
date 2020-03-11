package com.willy.will.setting;

import android.content.res.Resources;

import com.willy.will.R;
import com.willy.will.common.model.Task;
import com.willy.will.database.TaskDBController;

import java.util.ArrayList;

public class TaskManagementController {

    private Resources resources;

    private TaskDBController taskDBCtrl = null;

    public TaskManagementController(Resources resources) {
        this.resources = resources;
        taskDBCtrl = new TaskDBController(resources);
    }

    public void removeTasks(ArrayList<Task> selectedTasks) {
        /** Set to_do_id IN ( ... ) **/
        String whereToDoIds = setWhereToDoIds(selectedTasks);
        /* ~Set to_do_id IN ( ... ) */

        /** Set item_id IN ( ... ) for deleting items of calendar **/
        String itemIdColumn = resources.getString(R.string.item_id_column);
        String itemTable = resources.getString(R.string.item_table);
        String whereItemIds = itemIdColumn + " IN ( " +
                "SELECT " + itemIdColumn + " FROM " + itemTable + " WHERE " + whereToDoIds +
                " )";
        /* ~Set item_id IN ( ... ) for deleting items of calendar */

        taskDBCtrl.deleteTasks(whereToDoIds, whereItemIds);
    }

    private String setWhereToDoIds(ArrayList<Task> selectedTasks) {
        String whereToDoIds = resources.getString(R.string.to_do_id_column) + " IN ( ";

        for(int i = selectedTasks.size() - 1; !selectedTasks.isEmpty(); i--) {
            whereToDoIds += ( ((Task) selectedTasks.remove(i)).getToDoId() + ", " );
        }

        int index = whereToDoIds.lastIndexOf(", ");
        if(index > -1) {
            whereToDoIds = whereToDoIds.substring(0, index);
        }

        whereToDoIds += " )";

        return whereToDoIds;
    }

    public void moveTasks(int groupId, ArrayList<Task> selectedTasks) {
        /** Set to_do_id IN ( ... ) **/
        String whereToDoIds = setWhereToDoIds(selectedTasks);
        /* ~Set to_do_id IN ( ... ) */

        taskDBCtrl.moveTasks(groupId, whereToDoIds);
    }

}
