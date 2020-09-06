package com.willy.will.add.controller;

import android.content.res.Resources;
import android.util.Log;

import com.willy.will.R;
import com.willy.will.database.DateRange;
import com.willy.will.database.ToDoItemDBController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AddItemController {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Resources resources;
    private ToDoItemDBController toDoItemDBController;

    public AddItemController(Resources resources) {
        this.resources = resources;
        toDoItemDBController = new ToDoItemDBController(resources);
    }

    public void addItem(int groupId, String itemName, int important,
                        String startDate, String endDate, String latitude, String longitude,
                        String loopWeek, ArrayList<String> checkedDays, String itemMemo) {
        final int toDoId = toDoItemDBController.getToDoId(resources.getInteger(R.integer.max_to_do_id_request)) + 1;

        /** Add one item **/
        if(loopWeek == null || loopWeek.equals("0000000")) {
            try {
                toDoItemDBController.insertOneItem(
                        toDoId, groupId, itemName, important, latitude, longitude,
                        startDate, endDate, itemMemo
                );
            } catch (ParseException e) {
                Log.e("AddItemController", e.toString());
                // 롤백(앞에서 추가한 데이터 삭제) 코드 추가
            }
        }
        /* ~Add one item */
        /** Add Repeated Items **/
        else {
            try {
                toDoItemDBController.insertItems(
                        toDoId, groupId, itemName, important, latitude, longitude,
                        startDate, endDate, loopWeek, checkedDays, itemMemo);
            } catch (ParseException e) {
                Log.e("AddItemController", e.toString());
                // 롤백(앞에서 추가한 데이터 삭제) 코드 추가
            }
        }
        /* ~Add Repeated Items */
    }

    public void modifyItem(int itemId, int groupId, String itemName, int important,
                           String latitude, String longitude, String startDate, String endDate,
                           String loopWeek, ArrayList<String> checkedDays, String itemMemo) {
        /** Update  **/
        final int toDoId = toDoItemDBController.getToDoId(itemId);
        toDoItemDBController.updateItems(
                toDoId, groupId, itemName, important,
                latitude, longitude, startDate, endDate,
                itemMemo
        );

        try {
            DateRange dateRange = toDoItemDBController.getDateRange(toDoId);
            long startValue = simpleDateFormat.parse(startDate).getTime();
            long endValue = simpleDateFormat.parse(endDate).getTime();

            boolean delete = false;
            boolean addBefore = false;
            boolean addAfter = false;

            if(dateRange.getMinValue() < startValue) {
                delete = true;
            }
            else if(dateRange.getMinValue() > startValue) {
                addBefore = true;
            }

            if(dateRange.getMaxValue() > endValue) {
                delete = true;
            }
            else if(dateRange.getMaxValue() < endValue) {
                addAfter = true;
            }

            /** Update one item **/
            if(loopWeek == null || loopWeek.equals("0000000")) {
                if(delete) {
                    toDoItemDBController.deleteDatesConditionally(
                            toDoItemDBController.getItemIdsStr(toDoId, startDate, endDate),
                            startDate,
                            endDate
                    );
                }

                if(addBefore) {
                    toDoItemDBController.insertDatesIntoCalendar(
                            itemId, startDate, dateRange.getBeforeMin()
                    );
                }

                if(addAfter) {
                    toDoItemDBController.insertDatesIntoCalendar(
                            itemId, dateRange.getAfterMax(), endDate
                    );
                }
            }
            /* ~Update one item */
            /** Update Repeated Items **/
            else {
                if(delete) {
                    toDoItemDBController.deleteItemsConditionally(toDoId, startDate, endDate);
                }

                if(addBefore) {
                    toDoItemDBController.insertItemsIntoItemAndCalendar(
                            toDoId, groupId, itemName, important, latitude, longitude,
                            startDate, endDate, startDate, dateRange.getBeforeMin(), checkedDays,
                            itemMemo
                    );
                }

                if(addAfter) {
                    toDoItemDBController.insertItemsIntoItemAndCalendar(
                            toDoId, groupId, itemName, important, latitude, longitude,
                            startDate, endDate, dateRange.getAfterMax(), endDate, checkedDays,
                            itemMemo
                    );
                }
            }
            /* ~Update Repeated Items */
        } catch (ParseException e) {
            Log.e("AddItemController", e.toString());
            // 롤백(앞에서 변경한 데이터 삭제) 코드 추가
            return;
        }
    }

}
