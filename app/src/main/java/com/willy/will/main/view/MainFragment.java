package com.willy.will.main.view;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.database.ToDoItemDBController;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    //Recycler View
    private RecyclerView recyclerView = null;

    //fragment var
    private static final String ARG_DATE = "ARG_DATE";
    private static final String ARG_GROUP_NO = "ARG_GROUP_NO";
    private Resources resources;
    private ToDoItemDBController dbController;

    private ArrayList<ToDoItem> list;
    private String currentDate;
    private int groupId;

    private ViewGroup rootView;
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

        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main,container,false);


        /** Set TodoItem */
        resources = getActivity().getResources();
        dbController = new ToDoItemDBController(resources);

        nullList = (TextView) rootView.findViewById(R.id.tv_default);

        list = dbController.mainToDoItems(list,currentDate,groupId);

        setNullListLocation();
        setRecyclerView();

        if(list.isEmpty()) {
            recyclerView.setVisibility(rootView.GONE);
            nullList.setVisibility(rootView.VISIBLE);
        }

        return rootView;

    }
    /* ~Inflate the view for the fragment based on layout XML*/

    public ViewGroup getRootView() {
        return rootView;
    }

    public void refreshListDomain() {

        list = dbController.mainToDoItems(list, currentDate, groupId);
        recyclerView.getAdapter().notifyDataSetChanged();

        if (list.isEmpty()) {
            recyclerView.setVisibility(rootView.GONE);
            nullList.setVisibility(rootView.VISIBLE);
        }
        else {
            nullList.setVisibility(rootView.GONE);
            recyclerView.setVisibility(rootView.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /** Initialization (including Item View)*/
    private void setRecyclerView() {

        recyclerView = new RecyclerViewSetter(
                this, R.id.mainItemList,
                RecyclerViewItemType.TO_DO_MAIN, R.layout.item_main,
                list
        ).setRecyclerView();
    }
    /* ~Initialization (including Item View) */

    /** Setting Position of TextView*/
    public void setNullListLocation(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        display.getSize(size);
        int hei = size.y;
        params.setMargins(0,(int)hei/4,0,0);
        nullList.setGravity(Gravity.CENTER_HORIZONTAL);

        //set nullList size
        nullList.setLayoutParams(params);
    }
    /* ~Setting Position of TxtView*/


}
