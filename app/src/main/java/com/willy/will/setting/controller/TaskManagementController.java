package com.willy.will.setting.controller;

import android.content.res.Resources;

import com.willy.will.R;
import com.willy.will.database.TaskDBController;

import java.util.Queue;

public class TaskManagementController {

    private Resources resources;

    private TaskDBController taskDBCtrl = null;

    public TaskManagementController(Resources resources) {
        this.resources = resources;
        taskDBCtrl = new TaskDBController(resources);
    }

    public void removeTasks(Queue<Integer> selectedToDoIds) {
        /** Set to_do_id IN ( ... ) **/
        String whereToDoIds = setWhereToDoIds(selectedToDoIds);
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

    private String setWhereToDoIds(Queue<Integer> selectedToDoIds) {
        String whereToDoIds = resources.getString(R.string.to_do_id_column) + " IN ( ";

        while(!selectedToDoIds.isEmpty()) {
            whereToDoIds += ( selectedToDoIds.poll() + ", " );
        }

        int index = whereToDoIds.lastIndexOf(", ");
        if(index > -1) {
            whereToDoIds = whereToDoIds.substring(0, index);
        }

        whereToDoIds += " )";

        return whereToDoIds;
    }

    public void moveTasks(int groupId, Queue<Integer> selectedToDoIds) {
        /** Set to_do_id IN ( ... ) **/
        String whereToDoIds = setWhereToDoIds(selectedToDoIds);
        /* ~Set to_do_id IN ( ... ) */

        taskDBCtrl.moveTasks(groupId, whereToDoIds);
    }

}
