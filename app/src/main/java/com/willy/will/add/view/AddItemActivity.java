package com.willy.will.add.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.willy.will.R;
import com.willy.will.common.view.GroupManagementActivity;
import com.willy.will.common.view.Group_Color;
import com.willy.will.database.DBAccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends Activity{
    private SimpleDateFormat simpleDateFormat = null;
    private Calendar today = null;
    private Calendar calendar = null;
    private String start_date_key = null;
    private String end_date_key = null;
    private Resources resources = null;
    private DatePickerDialog datePickerDialog = null;
    private DateListener dateListener = null;
    private String start_date = null;
    private String end_date = null;
    private View checkBox_group;

    private Spinner important;
    private TextView important_result;

    public static DBAccess dbHelper;

    Switch repeat_switch;
    TextView Text_start;
    TextView Text_end;
    EditText Title_editText,Group_editText;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
    String formatDate = sdfNow.format(date);
    TextView dateNow;

    private SQLiteDatabase readDatabase;
    private SQLiteDatabase writeDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemadd);

        dbHelper = DBAccess.getDbHelper();
        //dummyCreate();
        resources = getResources();


        important = (Spinner)findViewById(R.id.important);
        important_result =(TextView)findViewById(R.id.important_result);

        repeat_switch = (Switch) findViewById(R.id.repeat_switch);
        checkBox_group = findViewById(R.id.checkBox_group);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        today = Calendar.getInstance();
        calendar = Calendar.getInstance();

        /** Set theme of Dialogs **/
        datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme);
        dateListener = new DateListener();
        datePickerDialog.setOnDateSetListener(dateListener);
        /* ~Set theme of Dialogs */

        /** set view **/
        Button btn_start = findViewById(R.id.btn_start);
        Button btn_end = findViewById(R.id.btn_end);
        Text_start = findViewById(R.id.Text_start);
        Text_end = findViewById(R.id.Text_end);

        Text_start.setText(formatDate);
        Text_end.setText(formatDate);

        /** set value **/
        resources = getResources();
        start_date_key = resources.getString(R.string.start_date_key);
        end_date_key = resources.getString(R.string.end_date_key);

        start_date = getString(R.string.start_date_key);
        end_date = getString(R.string.end_date_key);

        Title_editText = (EditText)findViewById(R.id.Title_editText);
        Group_editText = (EditText)findViewById(R.id.Group_editText);

        /** edit keyboard invisible 1 **/
        Title_editText.setInputType(0);
        Title_editText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Title_editText.setInputType(1);
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(Title_editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        /** edit keyboard invisible **/
        Group_editText.setInputType(0);
        Group_editText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Group_editText.setInputType(1);
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(Group_editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        /******* Group buuton -> moving ********************/
        Button bnt_group = findViewById(R.id.bnt_group);
        bnt_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItemActivity.this, GroupManagementActivity.class);
                startActivity(intent);
            }
        });


        repeat_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // checked -> add_item_repeat
                if (repeat_switch.isChecked() == true) {
                    checkBox_group.setVisibility(View.VISIBLE);

                    final ScrollView scrollView=findViewById(R.id.AddScrollView);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                else {
                    checkBox_group.setVisibility(View.GONE);
                }
            }
        });



    }
    public void bringUpgroupcolor(View view) {
        Intent intent = new Intent(this, Group_Color.class);
    }

    // Start Date Picker Dialog for start of start date
    public void setStart(View view) {
        dateListener.setKey(start_date_key);
        if(start_date.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }
        else {
            try {
                Date date = simpleDateFormat.parse(start_date);
                calendar.setTime(date);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        datePickerDialog.setMessage(resources.getString(R.string.start_date_text));
        datePickerDialog.show();
    }

    // Start Date Picker Dialog for start of start date
    public void setEnd(View view) {
        dateListener.setKey(end_date_key);

        if(end_date.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }
        else {
            try {
                Date date = simpleDateFormat.parse(end_date);
                calendar.setTime(date);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        datePickerDialog.setMessage(resources.getString(R.string.end_date_text));
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



    class DateListener implements DatePickerDialog.OnDateSetListener {
        private String key = null;

        public void setKey(String key) {this.key = key;}
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);

            if(key.equals(start_date_key)) {
                start_date = simpleDateFormat.format(calendar.getTime());
                Text_start.setText(start_date);
            }
            else if(key.equals(end_date_key)) {
                end_date = simpleDateFormat.format(calendar.getTime());
                Text_end.setText(end_date);
            }
            else {
                Log.e("DateListener", "Invalid Key");
            }
        }
    }

    /**

     -캘린더 추가-
     (pk)"calendar_id" : INTEGER NOT NULL,
     "calendar_date" : TEXT NOT NULL,
     "item_id" : INTEGER,

     -아이템 추가-
     (pk) item_id : int / not null
     group_id : int
     item_name : string / not null
     item_important (ex.1,2,3,4) : int
     item_location_X : int
     item_location_y : int
     done_date : string
     start_date : string /not null
     end_date : string /not null
     to_do_id : int

     -그룹 추가-
     (pk)"group_id" : INTEGER NOT NULL,
     "group_name" : TEXT NOT NULL,
     "group_color" : TEXT NOT NULL,

     -반복 추가-
     (pk)"loop_id" : INTEGER NOT NULL,
     (pk)"to_do_id" : INTEGER NOT NULL,
     "loop_week" : String,

     **/


    /**
    public void dummyCreate(){
        //property 파일
        File file = new File(Environment.getDataDirectory()+"/data/"+getPackageName(), "data.properties");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ex) {

            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.execSQL("" +
                    "INSERT INTO _LOOP_INFO (loop_id, to_do_id, loop_week)" +
                    "VALUES (1, 1, '1000100');"
            );

            db.execSQL("" +
                    "INSERT INTO _LOOP_INFO (loop_id, to_do_id, loop_week)" +
                    "VALUES (2, 4, '0010001');"
            );

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(1, 1, '운동', 1, '23.123123', '23.123123', '2020-02-03', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(2, 1, '운동', 1, '23.123123', '23.123123', '2020-02-06', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(3, 1, '운동', 1, '23.123123', '23.123123', '2020-02-10', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(4, 1, '운동', 1, '23.123123', '23.123123', '2020-02-13', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(5, 1, '운동', 1, '23.123123', '23.123123', '2020-02-17', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(6, 1, '운동', 1, '23.123123', '23.123123', '2020-02-20', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(7, 1, '운동', 1, '23.123123', '23.123123', '2020-02-24', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(8, 1, '운동', 1, '23.123123', '23.123123', '2020-02-27', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(9, 0, '영어공부', 4, '', '', '2020-02-02', '2020-02-02', '2020-02-02', 2);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(10, 0, '밥', 2, '', '', '2020-02-09', '2020-02-09', '2020-02-09', 3);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(11, 2, '레슨', 3, '', '', '2020-02-11', '2020-02-11', '2020-02-18', 4);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(12, 2, '레슨', 3, '', '', '2020-02-16', '2020-02-11', '2020-02-18', 4);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(13, 2, '레슨', 3, '', '', '2020-02-18', '2020-02-11', '2020-02-18', 4);");

            db.execSQL("" +
                    "INSERT INTO _GROUP (group_id, group_name, group_color)" +
                    "VALUES(1, '운동할거야', '#80FF3C3C')"
            );

            db.execSQL("" +
                    "INSERT INTO _GROUP (group_id, group_name, group_color)" +
                    "VALUES(2, '레슨할거야', '#80FF9800');"
            );

            db.execSQL("" +
                    "INSERT INTO _GROUP (group_id, group_name, group_color)" +
                    "VALUES(3, '놀거야', '#80FFEB3B');"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(1, '2020-02-03', 1);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(2, '2020-02-06', 2);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(3, '2020-02-10', 3);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(4, '2020-02-13', 4);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(5, '2020-02-17', 5);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(6, '2020-02-20', 6);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(7, '2020-02-24', 7);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(8, '2020-02-27', 8);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(9, '2020-02-02', 9);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(10, '2020-02-09', 10);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(11, '2020-02-11', 11);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(12, '2020-02-15', 12);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(13, '2020-02-18', 13);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(14, '2020-03-11', 14);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(15, '2020-03-12', 15);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(16, '2020-03-18', 16);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(17, '2020-03-19', 16);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(18, '2020-03-19', 17);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(19, '2020-03-19', 18);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(20, '2020-03-19', 19);"
            );

            db.execSQL("" +
                    "INSERT INTO _CALENDAR(calendar_id, calendar_date, item_id)" +
                    "VALUES(21, '2020-03-19', 20);"
            );

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(14, 0, 'ENGLISH', 1, '', '', NULL, '2020-02-11', '2020-02-11', 5);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(15, 0, 'ENGLISH', 2, '', '', NULL, '2020-02-12', '2020-02-12', 6);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(16, 0, 'ENGLISH', 4, '25.225555', '22.333555', NULL, '2020-03-18', '2020-03-19', 7);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(17, 0, 'ENGLISH', 4, '25.225555', '22.333555', NULL, '2020-03-19', '2020-03-21', 8);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(18, 1, 'ENGLISH', 1, '25.225555', '22.333555', NULL, '2020-03-18', '2020-03-19', 9);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(19, 0, 'ENGLISH', 3, '25.225555', '22.333555', NULL, '2020-03-19', '2020-03-19', 10);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, latitude, longitude, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(20, 0, 'ENGLISH', 4, '25.225555', '22.333555', NULL, '2020-03-19', '2020-03-20', 11);");
        }
    }**/
}


