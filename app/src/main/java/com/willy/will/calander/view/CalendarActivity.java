package com.willy.will.calander.view;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.willy.will.R;
import com.willy.will.database.DateDBController;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends Activity {
    private String[] currentDate = null;
    private DateDBController dateDBController = null;
    private Resources resources = null;
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

        /** set Calendar Listenser */
        OncalendarClickListener calanderListener = new OncalendarClickListener();
        ((MaterialCalendarView)findViewById(R.id.calendarView)).setOnDateChangedListener(calanderListener);
    }


    private class OncalendarClickListener implements OnDateSelectedListener {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            // Toast.makeText(getBaseContext(), date.getYear()+"/"+ (date.getMonth()+1)+"/"+ date.getDay(), Toast.LENGTH_LONG).show();
            setDateAtCalendar(date.getYear(), (date.getMonth()+1), date.getDay());
        }
    }

    // set Data at calendar
    private void setDateAtCalendar(int yy, int mm,  int dd){
        /**init resource */
        resources = getResources();

        /** Create List Adapter*/
        CalendarBaseAdapter calendarBaseAdapter = new CalendarBaseAdapter();

        /** setDBController*/
        dateDBController = new DateDBController(resources);
        List<DateDBController.calendarItem> itemIdList = dateDBController.getMonthItemByDate(yy,mm,dd);
        // ArrayList<DateDBController.ItemNGroup> iandgList = new ArrayList<>();
        for (DateDBController.calendarItem item:itemIdList) {
            DateDBController.ItemNGroup itemNGroup = dateDBController.getItemNGroupByItemId(item.getItemId());
            // iandgList.add(itemNGroup);
            calendarBaseAdapter.addItem(itemNGroup);
        }

        ListView calendarList = findViewById(R.id.calendarListView);
        calendarList.setAdapter(calendarBaseAdapter);
    }
}
