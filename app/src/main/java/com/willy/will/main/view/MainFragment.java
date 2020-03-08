package com.willy.will.main.view;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.database.DBAccess;
import com.willy.will.database.ToDoItemDBController;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    //Recycler View
    private RecyclerView recyclerView = null;

    //fragment var
    private static final String ARG_DATE = "ARG_DATE";
    private static final String ARG_GROUP_NO = "ARG_GROUP_NO";
    private Resources resources;

    private ArrayList<ToDoItem> list;
    private String currentDate;
    private int groupId;

    private TextView nullList;


    /**setting fragment*/
    public static MainFragment getInstance(String dateString, int groupId){
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATE,dateString);
        args.putInt(ARG_GROUP_NO,groupId);
        fragment.setArguments(args);
        return fragment;
    }
    /*~setting fragment*/

    /** Store instance variables based on arguments passed */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getActivity().getResources();
    }
    /* ~Store instance variables based on arguments passed */

    /** Inflate the view for the fragment based on layout XML*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentDate = getArguments().getString(ARG_DATE);
        groupId = getArguments().getInt(ARG_GROUP_NO);

        ViewGroup rootView =
                (ViewGroup)inflater.inflate(R.layout.fragment_main,container,false);


        /** Set TodoItem */
        list = mainToDoItems(list,currentDate,groupId);
        if(list.size() == 0){
            nullList = (TextView) rootView.findViewById(R.id.tv_default);
            nullList.setVisibility(rootView.VISIBLE);
        }
        /* ~Set TodoItem */
        else {
            /** Initialization (including Item View)*/
            RecyclerViewSetter recyclerViewSetter = new RecyclerViewSetter(
                    R.id.mainItemList, rootView,
                    RecyclerViewItemType.TO_DO, list,
                    R.string.selection_id_main, false
            );
            recyclerView = recyclerViewSetter.setRecyclerView();
            // WARNING: Only one must be assigned
            recyclerViewSetter.setFragmentAndActivities(this, null, null);
            /* ~Initialization (including Item View) */
        }
        return rootView;

    }
    /* ~Inflate the view for the fragment based on layout XML*/

    @Override
    public void onResume() {
        super.onResume();
    }

    /** ToDoItem from DB */
    public ArrayList<ToDoItem> mainToDoItems(ArrayList<ToDoItem> toDoItemList,
                                             String currentDate, int selectedGroup) {
        SQLiteDatabase readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        String selectQuery;
        // Initialization of ArrayList

        if(toDoItemList == null) {
            toDoItemList = new ArrayList<>();
        }


        //Read DB All Item
        if(selectedGroup == -1){
            selectQuery = "SELECT i.item_name, i.done_date, " +
                    "i.start_date, i.end_date, g.group_id, g.group_color," +
                    " l.loop_week, i.item_id, i.to_do_id,i.item_important\n" +
                    "FROM _ITEM i, _GROUP g, _LOOP_INFO l \n" +
                    "WHERE i.group_id = g.group_id \n" +
                    "AND i.to_do_id = l.to_do_id \n" +
                    "AND date(i.end_date) >= \""+currentDate+"\" \n"+
                    "AND date(i.start_date) <= \""+currentDate+"\"\n"+
                    "ORDER BY i.done_date,i.item_important,i.item_name;";
        }

        //Read DB by selected group
        else {
            selectedGroup+=1; // temp

            selectQuery = "SELECT i.item_name, i.done_date, " +
                    "i.start_date, i.end_date, g.group_id, g.group_color," +
                    " l.loop_week, i.item_id, i.to_do_id, i.item_important\n" +
                    "FROM _ITEM i, _GROUP g, _LOOP_INFO l \n" +
                    "WHERE i.group_id = g.group_id \n" +
                    "AND i.to_do_id = l.to_do_id \n" +
                    "AND date(i.end_date) >= \""+currentDate+"\"\n"+
                    "AND date(i.start_date) >= \""+currentDate+"\"\n"+
                    "AND g.group_id = "+selectedGroup+"\n"+
                    "ORDER BY i.done_date,i.item_important,i.item_name;";
        }
        Cursor cursor = readDatabase.rawQuery(selectQuery, null);
        Log.d("checkQuery",selectQuery);

        /** Put data in ArrayList **/
        ToDoItem curToDoItem = null;
        int itemId = -1;
        int groupId = -1;
        String doneDate = null;
        boolean done = false;
        String endDate = null;
        int toDoId = -1;
        int rank = -1;
//        int loop = -1;
//       String loopday = null;
        String name = null;

        while(cursor.moveToNext()) {
            itemId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_id_column)));
            groupId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_id_column)));
            doneDate = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.done_date_column)));
            if(doneDate != null) {
                done = true;
            }
            else {
                done = false;
            }
            endDate = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.end_date_column)));
            toDoId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.to_do_id_column)));
            rank = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_important_column)));

            name = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_name_column)));
//            loopday = cursor.getString(cursor.getColumnIndex(resources.getString((R.string.loop))));

            curToDoItem = new ToDoItem(itemId, groupId, doneDate, done, endDate, toDoId, rank, name);
            toDoItemList.add(curToDoItem);
        }
        /* ~Put data in ArrayList */

        return toDoItemList;
    }
    /* ~ToDoItem from DB */
}
