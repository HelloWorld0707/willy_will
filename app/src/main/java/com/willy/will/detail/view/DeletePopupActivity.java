package com.willy.will.detail.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.willy.will.R;
import com.willy.will.common.model.PopupActivity;

public class DeletePopupActivity extends PopupActivity {


    public DeletePopupActivity(){
        super(R.layout.activity_delete_popup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int todoId = intent.getIntExtra("todoId",-1);
        Log.d("todo", "onMenuItemClick: " + todoId);

    }

    public boolean setResults(Intent intent){
        //삭제 sql
        return true;
    }

}
