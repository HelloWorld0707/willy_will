package com.willy.will.calander.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.willy.will.R;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.database.DateDBController;
import com.willy.will.detail.view.DetailActivity;

import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends Activity {
    private String[] currentDate = null;
    private DateDBController dateDBController = null;
    private Resources resources = null;
    private TextView Textyear= null;
    private TextView TextMon = null;

    private DisplayMetrics windowDm = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);

        Resources resources = getResources();
        /** Set current Date **/
        currentDate = getIntent().getStringExtra(resources.getString(R.string.current_date_key)).split("-");
        Textyear = (TextView)findViewById(R.id.calenderYear);
        Textyear.setText(currentDate[0] + "년");

        TextMon = (TextView)findViewById(R.id.calenderMonthnDay);
        TextMon.setText(currentDate[1] + "월 " + currentDate[2] + "일");

        /** set Calendar Listenser */
        MaterialCalendarView calendar = findViewById(R.id.calendarView);
        OncalendarClickListener touchListener = new OncalendarClickListener();
        calendar.setOnDateChangedListener(touchListener);
        OnMonthChangedListener dragListener = new OnCalendarMonthOverListener();
        calendar.setOnMonthChangedListener(dragListener);

        /** highlight at holiday */
        calendar.setTopbarVisible(false);
        calendar.addDecorators( new SundayDecorator(),
                new SaturdayDecorator());

        /** device display size controller */
        windowDm = getApplicationContext().getResources().getDisplayMetrics();
    }

    /** Back to MainActivity **/
    public void backToMain(View view) {
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        this.finish();
    }

    /** calendar select listner */
    private class OncalendarClickListener implements OnDateSelectedListener {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            setDateAtCalendar(date.getYear(), (date.getMonth()+1), date.getDay());
            Textyear.setText(date.getYear() + "년");
            TextMon .setText((date.getMonth()+1) + "월 " + date.getDay() + "일");
        }
    }

    /** claendar month change listner */
    private class OnCalendarMonthOverListener implements OnMonthChangedListener {
        @Override
        public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
            int yyyy = date.getYear();
            int mm = date.getMonth()+1;
            int dd = date.getDay();

            Textyear.setText(yyyy + "년");
            TextMon.setText(mm + "월 " + dd + "일");

            setDateAtCalendar(yyyy,mm,dd);
        }
    }

    /** set Data at calendar */
    private void setDateAtCalendar(int yyyy, int mm,  int dd){
        /**init resource */
        resources = getResources();

        /** Create List Adapter*/
        final CalendarBaseAdapter calendarBaseAdapter = new CalendarBaseAdapter();

        /** setDBController*/
        dateDBController = new DateDBController(resources);
        List<DateDBController.calendarItem> itemIdList = dateDBController.getMonthItemByDate(yyyy,mm,dd);
        for (DateDBController.calendarItem item:itemIdList) {
            DateDBController.ItemNGroup itemNGroup = dateDBController.getItemNGroupByItemId(item.getItemId());
            calendarBaseAdapter.addItem(itemNGroup);
        }
        ListView calendarList = findViewById(R.id.calendarListView);
        calendarList.setAdapter(calendarBaseAdapter);


        MaterialCalendarView calendar = findViewById(R.id.calendarView);
        // set Visible at listView
        if(calendarBaseAdapter.getCount() > 0) {
            calendarList.setVisibility(View.VISIBLE);
            calendar.setTileHeight((int)windowDm.ydpi/4);
        }
        else {
            calendarList.setVisibility(View.INVISIBLE);
            calendar.setTileHeight((int)windowDm.ydpi);
        }
        // set ListView Click Listener
        calendarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = (int) calendarBaseAdapter.getItemId(position);
                ToDoItem toDoItem = new ToDoItem();
                toDoItem.setItemId(itemId);
                Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
                intent.putExtra(resources.getString(R.string.item_id), toDoItem);
                startActivity(intent);
            }
        });
        // ~set ListView Click Listener

        // setListView Height
        /*
        ViewGroup.LayoutParams params = calendarList.getLayoutParams();

        int height = 134;// findViewById(R.id.calendarListView).getMeasuredHeight();
        int listSize = calendarBaseAdapter.getCount();
        params.height = height * listSize;
        calendarList.setLayoutParams(params);
        calendarList.requestLayout();
        */
        // ~setListView Height
    }

    /** calendar custom class. set Red text on sunday */
    private class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    private class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }
}
