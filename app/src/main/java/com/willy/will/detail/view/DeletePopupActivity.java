package com.willy.will.detail.view;

import android.content.Intent;
import android.os.Bundle;

import com.willy.will.R;
import com.willy.will.common.view.PopupActivity;
import com.willy.will.detail.controller.DetailController;
import com.willy.will.main.view.MainActivity;

public class DeletePopupActivity extends PopupActivity {

    private DetailController detailCtrl;
    private int todoId;


    public DeletePopupActivity(){
        super(R.layout.activity_delete_popup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        todoId = intent.getIntExtra("todoId",-1);
    }

    public boolean setResults(Intent intent){
        detailCtrl = new DetailController();
        detailCtrl.deleteItemByTodoId(todoId);
        Intent intent1 = new Intent(DeletePopupActivity.this, MainActivity.class);
        startActivity(intent1);
        return true;
    }

}
