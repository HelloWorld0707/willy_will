package com.willy.will.calander.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.willy.will.R;
import com.willy.will.dataBase.DBAccess;

public class fragmentCalander extends Fragment {
    // example extra string
    private final static String EXTRA_INT = "someInt";
    private final static String EXTRA_STRING = "someTitle";

    public static final fragmentCalander newInstance(int page, String title) {
        // example of communication
        fragmentCalander fragment = new fragmentCalander();
        Bundle args = new Bundle();
        args.putInt(EXTRA_INT, page);
        args.putString(EXTRA_STRING, title);
        fragment.setArguments(args);
        return fragment;
        // ~example
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calander, container, false);
        return view;
    }
}
