package com.willy.will.search.controller;

import android.content.res.Resources;

import com.willy.will.R;
import com.willy.will.common.model.Group;
import com.willy.will.database.ToDoItemDBController;
import com.willy.will.main.model.ToDoItem;

import java.util.ArrayList;
import java.util.Iterator;

public class SearchController {

    private Resources resources;
    private ToDoItemDBController toDoItemDBController;

    public SearchController(Resources resources) {
        this.resources = resources;
        toDoItemDBController = new ToDoItemDBController(resources);
    }

    public ArrayList<ToDoItem> getToDoItems(String searchName,
                                            ArrayList<ToDoItem> toDoList,
                                            ArrayList<Group> groupList,
                                            String startOfDoneDate, String endOfDoneDate,
                                            boolean includedRepeat,
                                            String startOfStartDate, String endOfStartDate,
                                            String startOfEndDate, String endOfEndDate) {
        if(toDoList == null) {
            toDoList = new ArrayList<>();
        }
        else if(!toDoList.isEmpty()) {
            toDoList.clear();
        }

        String toDoIdColumn = resources.getString(R.string.to_do_id_column);

        // For dates
        String strftime = resources.getString(R.string.strftime_function);
        String comparisonDate = null;
        String criDate = null;

        /** Set criteria of a temporary table **/
        // Set item name criterion
        String itemNameQuery = "";
        if(!searchName.isEmpty()) {
            String itemNameColumn = resources.getString(R.string.item_name_column);
            itemNameQuery += itemNameColumn + "like" + "'%" + itemNameColumn + "%'";
        }
        // Set group criterion
        String groupsQuery = "";
        if(groupList.size() > 0) {
            String groupIdColumn = resources.getString(R.string.group_id_column);
            Iterator<Group> groupsIter = groupList.iterator();
            groupsQuery += (" ( " + groupIdColumn + "=" + groupsIter.next().getGroupId());
            while (groupsIter.hasNext()) {
                groupsQuery += (" OR" + groupIdColumn + "=" + groupsIter.next().getGroupId());
            }
            groupsQuery += " )";
        }
        // Set done date criterion
        String doneDateQuery = "";
        if(!startOfDoneDate.isEmpty() || !endOfDoneDate.isEmpty()) {
            comparisonDate = String.format(strftime, resources.getString(R.string.done_date_column));
            if(!startOfDoneDate.isEmpty()) {
                criDate = String.format(strftime, "'" + startOfDoneDate + "'");
                doneDateQuery += (" AND " + comparisonDate + ">=" + criDate);
            }
            if(!endOfDoneDate.isEmpty()) {
                criDate = String.format(strftime, "'" + endOfDoneDate + "'");
                doneDateQuery += (" AND " + comparisonDate + "<=" + criDate);
            }
        }
        // Set repeat criterion
        String repeatQuery = "";
        if(!includedRepeat) {
            repeatQuery += (" AND " +
                    toDoIdColumn + " NOT IN " +
                    "( SELECT " + toDoIdColumn + " FROM " + resources.getString(R.string.loop_info_table) + " )");
        }
        // Set start date criterion
        String startDateQuery = "";
        if(!startOfStartDate.isEmpty() || !endOfStartDate.isEmpty()) {
            comparisonDate = String.format(strftime, resources.getString(R.string.start_date_column));
            if(!startOfStartDate.isEmpty()) {
                criDate = String.format(strftime, "'" + startOfStartDate + "'");
                startDateQuery += (" AND " + comparisonDate + ">=" + criDate);
            }
            if(!endOfStartDate.isEmpty()) {
                criDate = String.format(strftime, "'" + endOfStartDate + "'");
                startDateQuery += (" AND " + comparisonDate + "<=" + criDate);
            }
        }
        // Set end date criterion
        String endDateQuery = "";
        if(!startOfEndDate.isEmpty() || !endOfEndDate.isEmpty()) {
            comparisonDate = String.format(strftime, resources.getString(R.string.end_date_column));
            if(!startOfEndDate.isEmpty()) {
                criDate = String.format(strftime, "'" + startOfEndDate + "'");
                endDateQuery += (" AND " + comparisonDate + ">=" + criDate);
            }
            if(!endOfEndDate.isEmpty()) {
                criDate = String.format(strftime, "'" + endOfEndDate + "'");
                endDateQuery += (" AND " + comparisonDate + "<=" + criDate);
            }
        }
        /* ~Set criteria of a temporary table */

        String tempTable = String.format(
                resources.getString(R.string.temporary_table_for_search_query),
                itemNameQuery + groupsQuery + doneDateQuery + repeatQuery + startDateQuery + endDateQuery,
                resources.getString(R.string.item_id_column)
        ).replace("WHERE  AND", "WHERE");

        toDoList = toDoItemDBController.searchToDoItems(toDoList, tempTable, toDoIdColumn);

        return toDoList;
    }

}
