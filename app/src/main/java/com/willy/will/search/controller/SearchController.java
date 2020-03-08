package com.willy.will.search.controller;

import android.content.res.Resources;

import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.common.model.Group;
import com.willy.will.database.ToDoItemDBController;
import com.willy.will.common.model.ToDoItem;

import java.util.ArrayList;
import java.util.Iterator;

public class SearchController {

    private Resources resources;
    private RecyclerView recyclerView;
    private ToDoItemDBController toDoItemDBController;

    public SearchController(Resources resources, RecyclerView recyclerView) {
        this.resources = resources;
        this.recyclerView = recyclerView;
        toDoItemDBController = new ToDoItemDBController(resources);
    }

    public ArrayList<ToDoItem> getToDoItems(String searchName,
                                            ArrayList<ToDoItem> toDoList,
                                            ArrayList<Group> groupList,
                                            String selectedDone, String startOfDoneDate, String endOfDoneDate,
                                            String selectedLoop,
                                            String startOfStartDate, String endOfStartDate,
                                            String startOfEndDate, String endOfEndDate) {
        /** Initialize to-do item list **/
        if(toDoList == null) {
            toDoList = new ArrayList<>();
        }
        else if(!toDoList.isEmpty()) {
            toDoList.clear();
        }
        /* ~Initialize to-do item list */

        /** Set the temporary table **/
        // For dates
        String strftime = resources.getString(R.string.strftime_function);
        String comparisonDate = null;
        String criDate = null;

        // Set item name criterion
        String itemNameQuery = "";
        if(!searchName.isEmpty()) {
            String itemNameColumn = resources.getString(R.string.item_name_column);
            itemNameQuery += itemNameColumn + " like " + "'%" + itemNameColumn + "%'";
        }
        // Set group criterion
        String groupsQuery = "";
        if(groupList.size() > 0) {
            groupsQuery += "( ";
            String groupIdColumn = resources.getString(R.string.group_id_column);
            Iterator<Group> groupsIter = groupList.iterator();
            while (groupsIter.hasNext()) {
                groupsQuery += (" OR " + groupIdColumn + " = " + groupsIter.next().getGroupId());
            }
            groupsQuery += " )";
            groupsQuery = groupsQuery.replace("(  OR", "(");
        }
        // Set done criterion
        String doneQuery = "";
        if(!selectedDone.equals(resources.getString(R.string.all))) {
            String doneDateColumn = resources.getString(R.string.done_date_column);
            if (selectedDone.equals(resources.getString(R.string.not_done))) {
                doneQuery += (" AND " + doneDateColumn + " = NULL OR " + doneDateColumn + " = ''");
            }
            else if(selectedDone.equals(resources.getString(R.string.done))) {
                comparisonDate = String.format(strftime, doneDateColumn);
                if (!startOfDoneDate.isEmpty()) {
                    criDate = String.format(strftime, "'" + startOfDoneDate + "'");
                    doneQuery += (" AND " + comparisonDate + " >= " + criDate);
                }
                if (!endOfDoneDate.isEmpty()) {
                    criDate = String.format(strftime, "'" + endOfDoneDate + "'");
                    doneQuery += (" AND " + comparisonDate + " <= " + criDate);
                }
            }
        }
        // Set loop criterion
        String loopQuery = "";
        if(!selectedLoop.equals(resources.getString(R.string.all))) {
            String toDoIdColumn = resources.getString(R.string.to_do_id_column);
            if (selectedLoop.equals(resources.getString(R.string.not_loop))) {
                loopQuery += (" AND " +
                        toDoIdColumn + " NOT IN " +
                        "( SELECT " + toDoIdColumn + " FROM " + resources.getString(R.string.loop_info_table) + " )");
            } else if (selectedLoop.equals(resources.getString(R.string.loop))) {
                loopQuery += (" AND " +
                        toDoIdColumn + " IN " +
                        "( SELECT " + toDoIdColumn + " FROM " + resources.getString(R.string.loop_info_table) + " )");
            }
        }
        // Set start date criterion
        String startDateQuery = "";
        if(!startOfStartDate.isEmpty() || !endOfStartDate.isEmpty()) {
            comparisonDate = String.format(strftime, resources.getString(R.string.start_date_column));
            if(!startOfStartDate.isEmpty()) {
                criDate = String.format(strftime, "'" + startOfStartDate + "'");
                startDateQuery += (" AND " + comparisonDate + " >= " + criDate);
            }
            if(!endOfStartDate.isEmpty()) {
                criDate = String.format(strftime, "'" + endOfStartDate + "'");
                startDateQuery += (" AND " + comparisonDate + " <= " + criDate);
            }
        }
        // Set end date criterion
        String endDateQuery = "";
        if(!startOfEndDate.isEmpty() || !endOfEndDate.isEmpty()) {
            comparisonDate = String.format(strftime, resources.getString(R.string.end_date_column));
            if(!startOfEndDate.isEmpty()) {
                criDate = String.format(strftime, "'" + startOfEndDate + "'");
                endDateQuery += (" AND " + comparisonDate + " >= " + criDate);
            }
            if(!endOfEndDate.isEmpty()) {
                criDate = String.format(strftime, "'" + endOfEndDate + "'");
                endDateQuery += (" AND " + comparisonDate + " <= " + criDate);
            }
        }

        String tempTable = String.format(
                resources.getString(R.string.temporary_table_for_search_query),
                itemNameQuery + groupsQuery + doneQuery + loopQuery + startDateQuery + endDateQuery
        ).replace("WHERE  AND", "WHERE");
        /* ~Set the temporary table */

        toDoList = toDoItemDBController.searchToDoItems(toDoList, tempTable);

        return toDoList;
    }

}
