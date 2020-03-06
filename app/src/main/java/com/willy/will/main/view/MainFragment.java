package com.willy.will.main.view;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.database.DBAccess;
import com.willy.will.database.ToDoItemDBController;
import com.willy.will.main.model.ToDoItem;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    //Recycler View
    private RecyclerView recyclerView = null;

    //fragment var
    private static final String ARG_DATE = "ARG_DATE";
    private static final String ARG_GROUP_NO = "ARG_GROUP_NO";
    private static DBAccess dbHelper = DBAccess.getDbHelper();
    private Resources resources;
    private ToDoItemDBController dbController;

    private ArrayList<ToDoItem> list;


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
        String checkString = getArguments().getString(ARG_DATE,"Today");
        resources = getActivity().getResources();

        String text = checkString + "날의 프래그먼트";
        Log.d("MyFragment", "onCreate " + text+"***************************************");
    }
    /* ~Store instance variables based on arguments passed */

    /** Inflate the view for the fragment based on layout XML*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView =
                (ViewGroup)inflater.inflate(R.layout.fragment_main,container,false);


        /** Set TodoItem */
        dbController = new ToDoItemDBController(resources);
        list = dbController.searchToDoItems(list, resources.getString(R.string.item_table),
                resources.getString(R.string.item_important_column));

        /* ~Set TodoItem */

        /** Initialization (including Item View)*/
        // ↓↓↓↓↓↓↓↓↓↓ RecyclerViewAdapter 매개변수 고치는 바람에 부득이하게 수정함
        recyclerView = new RecyclerViewSetter(
                R.id.mainItemList, rootView,
                RecyclerViewItemType.TO_DO, list,
                R.string.selection_id_main, false
        ).setRecyclerView();
        /* ~Initialization (including Item View) */

        return rootView;

    }
    /* ~Inflate the view for the fragment based on layout XML*/

    @Override
    public void onResume() {
        super.onResume();
    }

}
