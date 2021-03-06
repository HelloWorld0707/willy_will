package com.willy.will.calander.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
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
import com.willy.will.common.controller.AdMobController;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.database.DateDBController;
import com.willy.will.detail.view.DetailActivity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends Activity {
    private String[] currentDate = null;
    private DateDBController dateDBController = null;
    private Resources resources = null;
    private TextView Textyear= null;
    private TextView TextMon = null;
    private TextView TextDay = null;
    private DatePickerDialog datepickerdialog = null;
    private MaterialCalendarView calendar = null;
    private AdMobController adMobController = new AdMobController(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);

        calendar = findViewById(R.id.calendarView);
        resources = getResources();
        
        /** Set current Date **/
        currentDate = getIntent().getStringExtra(resources.getString(R.string.current_date_key)).split("-");
        Textyear = (TextView)findViewById(R.id.calenderYear);
        TextMon = (TextView)findViewById(R.id.calenderMonth);
        TextDay = (TextView)findViewById(R.id.calenderDay);

        /** init() */
        setDateAtCalendar(
                Integer.parseInt(currentDate[0]),
                Integer.parseInt(currentDate[1]),
                Integer.parseInt(currentDate[2])
                );

        /** set Calendar Listenser */
        OncalendarClickListener touchListener = new OncalendarClickListener();
        calendar.setOnDateChangedListener(touchListener);
        OnMonthChangedListener dragListener = new OnCalendarMonthOverListener();
        calendar.setOnMonthChangedListener(dragListener);

        /** set Calendar Height */
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenHeight = (int)metrics.ydpi;
        calendar.setTileHeightDp(screenHeight/8);

        /** highlight at holiday */
        calendar.setTopbarVisible(false);
        calendar.addDecorators( new SundayDecorator(),
                new SaturdayDecorator());

        /**load ad**/
        adMobController.callingAdmob();
        /*~load ad*/
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

    @Override
    public void finish() {
        String yyyy = convertStrtoStrOnlyNum(Textyear.getText().toString());
        String mm = convertStrtoStrOnlyNum(TextMon.getText().toString());
        mm = mm.length()<2? ("0"+mm) : mm;
        String dd = convertStrtoStrOnlyNum(TextDay.getText().toString());
        dd = dd.length()<2? ("0"+dd) : dd;
        String thisDate = (yyyy) + "-" + (mm) + "-" + (dd);

        Intent intent = new Intent();
        intent.putExtra(resources.getString(R.string.request_code)
                ,resources.getInteger(R.integer.calender_item_request_code));
        intent.putExtra(resources.getString(R.string.current_date_key),thisDate);
        setResult(Activity.RESULT_OK,intent);
        super.finish();
    }

    /** calendar select listner */
    private class OncalendarClickListener implements OnDateSelectedListener {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            setDateAtCalendar(date.getYear(), (date.getMonth()+1), date.getDay());
        }
    }

    /** claendar month change listner */
    private class OnCalendarMonthOverListener implements OnMonthChangedListener {
        @Override
        public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
            int yyyy = date.getYear();
            int mm = date.getMonth()+1;
            int dd = date.getDay();

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
        try {
            for(DateDBController.calendarItem item : itemIdList) {
                DateDBController.ItemNGroup itemNGroup = null;
                itemNGroup = dateDBController.getItemNGroupByItemId(item.getItemId());
                calendarBaseAdapter.addItem(itemNGroup);
            }
        } catch (ParseException e) {
            Log.e("CalendarActivity", e.toString());
        }
        ListView calendarList = findViewById(R.id.calendarListView);
        calendarList.setAdapter(calendarBaseAdapter);

        /** set ListView Click Listener */
        calendarList.setOnItemClickListener(new listViewOnClick(calendarBaseAdapter));

        /** setListView Height */
        ViewGroup.LayoutParams params = calendarList.getLayoutParams();

        int heightPixel = resources.getDimensionPixelSize(R.dimen.text_recycler_item_height) + calendarList.getDividerHeight();
        int listSize = calendarBaseAdapter.getCount();
        params.height = calendarList.getPaddingTop() + heightPixel * listSize + calendarList.getPaddingBottom();

        calendarList.setLayoutParams(params);
        calendarList.requestLayout();
        /* ~setListView Height */

        /** set Visible at listView */
        TextView tv = findViewById(R.id.calendarIfItemNull);
        if(calendarBaseAdapter.getCount() > 0) {
            calendarList.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }
        else {
            calendarList.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        }

        /** renew date */
        setDate(yyyy,mm,dd);
        calendar.setSelectedDate(CalendarDay.from(yyyy, mm-1, dd));
    }

    private class listViewOnClick implements AdapterView.OnItemClickListener {
        private CalendarBaseAdapter calendarBaseAdapter = null;
        public listViewOnClick(CalendarBaseAdapter calendarBaseAdapter){
            this.calendarBaseAdapter = calendarBaseAdapter;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int itemId = (int) calendarBaseAdapter.getItemId(position);
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.setItemId(itemId);
            Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
            intent.putExtra(resources.getString(R.string.item_id), toDoItem);
            startActivityForResult(intent, resources.getInteger(R.integer.detail_request_code));
        }
    }


    public void showPicker(View v){
        CalendarDay date = calendar.getSelectedDate();
        int yyyy = date.getYear();
        int mm = date.getMonth();
        int dd = date.getDay();

        /** Set Picker */
        new DatePickerDialog(CalendarActivity.this,
                AlertDialog.THEME_HOLO_LIGHT,datepicker,yyyy,mm,dd).show();
    }

    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int yyyy, int mm, int dd) {
            calendar.setCurrentDate(CalendarDay.from(yyyy, mm, dd));
            setDateAtCalendar(yyyy,mm+1,dd);
        }
    };

    /** set CalendarDate on top */
    private void setDate(int yyyy, int mm, int dd){

        String month = (mm < 10)? "0"+mm : ""+mm;
        String day = (dd < 10)? "0"+dd : ""+dd;

        Textyear.setText(yyyy + "년");
        TextMon.setText(month + "월 ");
        TextDay.setText(day+"일");
    }

    /** only number string convert to String */
    private String convertStrtoStrOnlyNum(String str){
        String value = "";
        for(int i = 0 ; i < str.length(); i ++)
        {
            if(48 <= str.charAt(i) && str.charAt(i) <= 57)
                value += str.charAt(i);
        }
        return value;
    }

    /** calendar custom class. set Red text on sunday */
    private class SundayDecorator implements DayViewDecorator {
        private final Calendar calendar = Calendar.getInstance();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Success to receive data **/
        if(resultCode == resources.getInteger(R.integer.item_change_return_code)) {
            // Modified
            CalendarDay selectedDate = calendar.getSelectedDate();
            setDateAtCalendar(selectedDate.getYear(), (selectedDate.getMonth() + 1), selectedDate.getDay());
        }
        /* ~Success to receive data */
    }
}
