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

import java.util.ArrayList;
import com.willy.will.main.model.mainListItem;

public class fragmentMain extends Fragment {
    // dont fix it
    private final static String EXTRA_ADAPTER = "BaseAdpater";

    //fragment var
    private static final String ARG_NO = "ARG_NO";

    // example extra string
    /*
    private final static String EXTRA_INT = "someInt";
    private final static String EXTRA_STRING = "someTitle";
    */

    //Recycler View
    private RecyclerView recyclerView = null;


    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-19
     * Created By: Lee Jaeeun
     * Function: setting fragment
     */
    public static fragmentMain getInstance(String dateString){
        fragmentMain fragment = new fragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_NO,dateString);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    // omission database object
    public static final fragmentMain newInstance(int page, String title, BaseAdapter adapter) {
        // example of communication
        fragmentMain fragment = new fragmentMain();
        Bundle args = new Bundle();
        args.putInt(EXTRA_INT, page);
        args.putString(EXTRA_STRING, title);

        //
        args.putSerializable(EXTRA_ADAPTER, (mainListAdapter)adapter);
        //

        fragment.setArguments(args);
        return fragment;
        // ~example
    }
     */

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String checkString = getArguments().getString(ARG_NO,"Today");

        String text = checkString + "날의 프래그먼트";
        Log.d("MyFragment", "onCreate " + text+"***************************************");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView =
                (ViewGroup)inflater.inflate(R.layout.fragment_main,container,false);

        ArrayList<mainListItem> list = new ArrayList<>();
        for(int i=0; i<20;i++){
            list.add(new mainListItem());
        }

        // Set RecyclerView
        // ↓↓↓↓↓↓↓↓↓↓ RecyclerViewAdapter 매개변수 고치는 바람에 부득이하게 수정함
        recyclerView = new RecyclerViewSetter(
                R.id.mainItemList, rootView,
                R.integer.to_do_recycler_item_type, list,
                R.string.selection_id_main, false
        ).setRecyclerView();
        /*recyclerView = view.findViewById(R.id.mainItemList);
        recyclerView.setHasFixedSize(true); //안정적...고정사이즈
        // set LayoutManager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // set Adapter
        final int TYPE = getResources().getInteger(R.integer.to_do_recycler_item_type);
        adapter = new RecyclerViewAdapter(TYPE, list);//itemlist
        recyclerView.setAdapter(adapter);
        // set Tracker
        tracker = new SelectionTracker.Builder<>(
                "example",
                recyclerView,
                new StableIdKeyProvider(recyclerView),
                new RecyclerItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                // Select multiple items
                SelectionPredicates.<Long>createSelectSingleAnything()
        ).build(); // 하나만 선택
        adapter.setTracker(tracker);
        tracker.addObserver(new SelectionTracker.SelectionObserver() {
            */
        /**
             * Last Modified: 2020-02-12
             * Last Modified By: Shin Minyong
             * Created: -
             * Created By: -
             * Function: Initialization (including Item View)
             * */

            /*
            @Override //선택에 변화가 있을 떄 사용함
            public void onSelectionChanged() {
                super.onSelectionChanged();
                Intent intent = new Intent(getContext(), activityDetail.class);
                startActivity(intent);
            }
        });*/
        // ↑↑↑↑↑↑↑↑↑↑ RecyclerViewAdapter 매개변수 고치는 바람에 부득이하게 수정함

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
