package com.willy.will.add.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.Nullable;

import com.willy.will.R;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class activityItemAdd extends Activity{
    int y=0, m=0, d=0, h=0, mi=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemadd);
;

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
