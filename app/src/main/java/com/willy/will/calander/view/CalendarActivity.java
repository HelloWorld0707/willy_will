package com.willy.will.calander.view;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.willy.will.R;

public class CalendarActivity extends Activity {
    private String[] currentDate = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);

        Resources resources = getResources();
        /** Set current Date **/
        currentDate = getIntent().getStringExtra(resources.getString(R.string.current_date_key)).split("-");
        TextView textView = (TextView)findViewById(R.id.calenderYear);
        textView.setText(currentDate[0] + "년");

        textView = (TextView)findViewById(R.id.calenderMonthnDay);
        textView.setText(currentDate[1] + "월 " + currentDate[2] + "일");
    }

    // set Data at calendar
    private void setDateAtCalendar(){

    }
}
