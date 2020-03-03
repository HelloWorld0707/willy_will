package com.willy.will.add.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import com.willy.will.R;
import com.willy.will.common.view.GroupManagementActivity;

public class AddItemActivity extends Activity{
    int y=0, m=0, d=0;
    Switch repeat_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemadd);
        repeat_switch = (Switch) findViewById(R.id.repeat_switch);
;

        /******* Group buuton -> moving ********************/
        Button bnt_group = findViewById(R.id.bnt_group);
        bnt_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItemActivity.this, GroupManagementActivity.class);
                startActivity(intent);
            }
        });

        /******* Start_celander ********************/
        Button button = findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Start_Date_Show();
            }
        });

        /******* End_celander ********************/
        Button button1 = findViewById(R.id.btn_end);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                End_Date_Show();
            }
        });


        repeat_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // checked -> add_item_repeat
                if (repeat_switch.isChecked() == true) {
                    Intent intent = new Intent(AddItemActivity.this, AddItemRepeatActivity.class);
                    startActivity(intent);

                    // switch off
                    repeat_switch.setChecked(false);
                }
            }
        });

    }


    void Start_Date_Show() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;
            }
        },2019, 1, 11);

        datePickerDialog.setMessage("시작 날짜");
        datePickerDialog.show();
    }

    void End_Date_Show(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

            }
        },2019, 1, 11);

        datePickerDialog.setMessage("종료 날짜");
        datePickerDialog.show();
    }

    public void Tomain(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        // ~Check focusing
        this.finish();
    }


}
