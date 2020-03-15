package com.willy.will.search.controller;

import android.content.res.Resources;

import com.willy.will.R;
import com.willy.will.common.controller.ToDoItemComparator;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.database.ToDoItemDBController;
import com.willy.will.search.model.SearchType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;

public class SearchController {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Resources resources;
    private ToDoItemDBController toDoItemDBController;
    private ToDoItemComparator toDoItemComparator;

    public SearchController(Resources resources) {
        this.resources = resources;
        toDoItemDBController = new ToDoItemDBController(resources);
        toDoItemComparator = new ToDoItemComparator();
    }

    public ArrayList<ToDoItem> getToDoItems(String searchName,
                                            ArrayList<ToDoItem> toDoItemList,
                                            ArrayList<Group> groupList,
                                            String selectedDone,
                                            String selectedLoop,
                                            String startOfPeriod, String endOfPeriod) {
        /** Initialize to-do item list **/
        if(toDoItemList == null) {
            toDoItemList = new ArrayList<>();
        }
        else if(!toDoItemList.isEmpty()) {
            toDoItemList.clear();
        }
        /* ~Initialize to-do item list */

        /** Set columns **/
        String[] columns = {
                resources.getString(R.string.to_do_id_column),
                resources.getString(R.string.item_id_column),
                resources.getString(R.string.done_column),
                resources.getString(R.string.item_important_column),
                resources.getString(R.string.item_name_column),
                resources.getString(R.string.group_color_column),
                resources.getString(R.string.loop_column),
                resources.getString(R.string.end_date_column),
                resources.getString(R.string.calendar_date_column)
        };
        /* ~Set columns */

        /** Set the temporary table **/
        int bool = -1;

        // Set item name criterion
        String itemNameQuery = "";
        if(!searchName.isEmpty()) {
            String itemNameColumn = resources.getString(R.string.item_name_column);
            itemNameQuery += itemNameColumn + " like " + "'%" + itemNameColumn + "%' AND ";
        }
        // Set group criterion
        String groupsQuery = "";
        if(groupList.size() > 0) {
            groupsQuery += (resources.getString(R.string.group_id_column) + " IN ( " );
            Iterator<Group> groupsIter = groupList.iterator();
            while (groupsIter.hasNext()) {
                groupsQuery += (groupsIter.next().getGroupId() + ", ");
            }
            int index = groupsQuery.lastIndexOf(", ");
            if(index > -1) {
                groupsQuery = groupsQuery.substring(0, index);
            }
            groupsQuery += " ) AND ";
        }
        // Set done criterion
        String doneQuery = "";
        if(!selectedDone.equals(resources.getString(R.string.all))) {
            String doneColumn = resources.getString(R.string.done_column);
            if (selectedDone.equals(resources.getString(R.string.not_done))) {
                bool = 0;
            }
            else if(selectedDone.equals(resources.getString(R.string.done))) {
                bool = 1;
            }
            doneQuery += (doneColumn + " IS " + bool + " AND ");
        }
        // Set loop criterion
        String loopQuery = "";
        if(!selectedLoop.equals(resources.getString(R.string.all))) {
            String loopColumn = resources.getString(R.string.loop_column);
            if (selectedLoop.equals(resources.getString(R.string.not_loop))) {
                bool = 0;
            } else if (selectedLoop.equals(resources.getString(R.string.loop))) {
                bool = 1;
            }
            loopQuery += (loopColumn + " IS " + bool + " AND ");
        }
        // Set period criterion
        String periodQuery = resources.getString(R.string.calendar_date_column) +
                " BETWEEN '" + startOfPeriod + "' AND '" + endOfPeriod + "'";

        String tempTable = String.format(
                resources.getString(R.string.temporary_table_for_search_query),
                resources.getString(R.string.done_column_query),
                resources.getString(R.string.loop_column_query),
                itemNameQuery + groupsQuery + doneQuery + loopQuery + periodQuery
        ).replace("WHERE  AND", "WHERE");
        /* ~Set the temporary table */

        /** Call reading DB **/
        // Set today string
        String today = simpleDateFormat.format(Calendar.getInstance().getTime());
        // Read to today
        toDoItemList = toDoItemDBController.searchToDoItems(
                toDoItemList, SearchType.TO_CURRENT_DATE,
                columns, tempTable, today
        );
        // Read to today
        ArrayList<ToDoItem> afterCurDate = toDoItemDBController.searchToDoItems(
                toDoItemList, SearchType.AFTER_CURRENT_DATE,
                columns, tempTable, today
        );
        /* ~Call reading DB */

        /** Set the list excluding duplicates in the list until today and the list after today **/
        Iterator<ToDoItem> afterIter = afterCurDate.iterator();
        Iterator<ToDoItem> toIter;
        ToDoItem curItem;
        boolean none;
        while(afterIter.hasNext()) {
            none = true;
            toIter = toDoItemList.iterator();

            curItem = afterIter.next();
            while(toIter.hasNext()) {
                if (toIter.next().getToDoId() == curItem.getToDoId()) {
                    none = false;
                    break;
                }
            }

            if(none) {
                toDoItemList.add(curItem);
            }
        }
        /* ~Set the list excluding duplicates in the list until today and the list after today */

        /** Sort by done, importance, and name **/
        Collections.sort(toDoItemList, toDoItemComparator);
        /* ~Sort by done, importance, and name */

        return toDoItemList;
    }

}
