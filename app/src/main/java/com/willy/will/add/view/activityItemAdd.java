package com.willy.will.add.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import com.willy.will.R;

public class activityItemAdd extends Activity {
    int y=0, m=0, d=0, h=0, mi=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemadd);
        /******* Group buuton -> moving ********************/
        Button bnt_group = findViewById(R.id.bnt_group);
        bnt_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityItemAdd.this, activityItemAdd_group.class);
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

}
