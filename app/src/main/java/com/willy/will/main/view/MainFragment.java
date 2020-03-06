package com.willy.will.main.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.ToDoItem;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    // dont fix it
    private final static String EXTRA_ADAPTER = "BaseAdpater";

    //Recycler View
    private RecyclerView recyclerView = null;

    //fragment var
    private static final String ARG_NO = "ARG_NO";


    /**setting fragment*/
    public static MainFragment getInstance(String dateString){
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NO,dateString);
        fragment.setArguments(args);
        return fragment;
    }
    /*~setting fragment*/


    /** Store instance variables based on arguments passed */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String checkString = getArguments().getString(ARG_NO,"Today");

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
        ArrayList<ToDoItem> list = new ArrayList<>();
        ToDoItem sample = null;
        for(int i=0; i<20;i++){
            sample = new ToDoItem();
            sample.setName("sample"+i);
            sample.setEndDate(getArguments().getString(ARG_NO,"Today"));
            sample.setDone(false);
            sample.setRank(i % 3 + 1);
            list.add(sample);
        }
        /* ~Set TodoItem */

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

        return rootView;

    }
    /* ~Inflate the view for the fragment based on layout XML*/

    @Override
    public void onResume() {
        super.onResume();
    }


}
