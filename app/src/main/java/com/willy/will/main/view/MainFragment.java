package com.willy.will.main.view;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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
        resources = getActivity().getResources();
        dbController = new ToDoItemDBController(resources);

        list = dbController.mainToDoItems(list,currentDate,groupId);
        if(list.size() == 0){
            nullList = (TextView) rootView.findViewById(R.id.tv_default);

            //get user display size
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            display.getSize(size);
            int hei = size.y;
            int wid = size.x;
            params.setMargins((int)(2.8*wid)/10,(int)hei/4,0,0);
            Log.d("check size","****************hei"+(int) hei/2+". size: "+size+"****************");
            //set nullList size
            nullList.setLayoutParams(params);

            nullList.setVisibility(rootView.VISIBLE);
        }
        /* ~Set TodoItem */
        else {
            /** Initialization (including Item View)*/
            RecyclerViewSetter recyclerViewSetter = new RecyclerViewSetter(
                    R.id.mainItemList, rootView,
                    RecyclerViewItemType.TO_DO_MAIN, list,
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


}
