package com.willy.will.setting;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.will.R;

public class TaskManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);
    }

    public void backToMain(View view) {
        /*if(recyclerView != null) {
            if(!((RecyclerViewAdapter) recyclerView.getAdapter()).getTracker().hasSelection()) {
                *//** Check focusing **//*
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                *//* ~Check focusing *//*
                this.finish();
            }
        }*/
        this.finish();
    }

}
