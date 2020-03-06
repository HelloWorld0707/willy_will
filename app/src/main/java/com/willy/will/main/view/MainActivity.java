package com.willy.will.main.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.willy.will.R;
import com.willy.will.add.view.AddItemActivity;
import com.willy.will.calander.view.CalendarActivity;
import com.willy.will.common.view.GroupManagementActivity;
import com.willy.will.database.DBAccess;
import com.willy.will.search.view.SearchActivity;
import com.willy.will.setting.AlarmActivity;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    public static DBAccess dbHelper;

    private Spinner sp_group;
    private ArrayList<String> spgroupList;
    private ArrayAdapter<String> spgroupAdapter;

    private DrawerLayout drawer;
    private View drawerView;

    private TextView tv_date;
    private Calendar todayDate;
    private SimpleDateFormat sdf;
    private SimpleDateFormat sdf2;
    private String baseDate;
    private String sendDate;


    MainFragment fragmentmain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        dbHelper = DBAccess.getDbHelper();
        dummyCreate();

        /**set navigation Drawer**/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.nav_view);
        /*~set navigation Drawer*/

        /**setDate**/
        tv_date = (TextView) findViewById(R.id.tv_date);
        todayDate = Calendar.getInstance();
        sdf = new SimpleDateFormat("MM.dd");
        sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        baseDate = sdf.format(todayDate.getTime());
        sendDate = sdf2.format(todayDate.getTime());
        tv_date.setText(baseDate);
        /*~setDate*/

        /**open picker & change txt**/
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, datepicker,
                        todayDate.get(Calendar.YEAR), todayDate.get(Calendar.MONTH), todayDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        /*~open picker & change txt*/


        /**set sp_group **/
        //(fix later)
        spgroupList = new ArrayList<>();
        for(int i=0; i<5;i++){
            spgroupList.add("그룹"+i);
        }

        spgroupAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                spgroupList);

        sp_group = (Spinner)findViewById(R.id.sp_group);
        sp_group.setAdapter(spgroupAdapter);
        sp_group.setSelection(0,false);
        sp_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int i, long id) {
                Toast.makeText(getApplicationContext(),
                        "선택된 아이템 : "+sp_group.getItemAtPosition(i),Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        /*Set sp_group*/

        /**set fragment (don't change position)**/
        fragmentmain = MainFragment.getInstance(sendDate);

        //add the fragment to container(frame layout)
        getSupportFragmentManager()
                .beginTransaction().add(R.id.fragmentcontainer,fragmentmain).commit();
        /*~set fragment*/

        /** set fab event Listener **/
        FloatingActionButton fab = findViewById(R.id.fabItemAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show Item add Activity
                Intent intent = new Intent(MainActivity.this , AddItemActivity.class);
                startActivity(intent);
            }
        });
        /* ~set fab event Listener */
    }

    /** Function: Move to SearchView */
    public void btnSearchClick(View view){
        Intent intent = new Intent(MainActivity.this , SearchActivity.class);
        intent.putExtra(getResources().getString(R.string.current_date_key),sendDate);
//        Log.d("DateChecked","**********날짜"+sendDate+"*************");
        startActivity(intent);
    }
    /*~ Function: Move to SearchView */


    /** Move to CalendarView */
    public void btnCalendarClick(View view) {
        Intent intent = new Intent(MainActivity.this , CalendarActivity.class);
        intent.putExtra(getResources().getString(R.string.current_date_key),sendDate);
//        Log.d("DateChecked","**********날짜"+sendDate+"*************");
        startActivity(intent);
    }
    /*~Move to CalendarView*/

    /** Open navigation drawer */
    public void btnSettingClick(View view){
        if(!drawer.isDrawerOpen(drawerView)) {
            drawer.openDrawer(drawerView);
        }
    }
    /*~ Open navigation drawer */

    /** Move to GroupManagementActivity */
    public void btnGrSettingClick(View view){
        Intent intent = new Intent(MainActivity.this , GroupManagementActivity.class);
        drawer.closeDrawer(drawerView);
        startActivity(intent);
    }
    /* ~Move to GroupManagementActivity */

    /** Move to TaskManagementActivity */
    public void btnTaskSettingClick(View view){
        Intent intent = new Intent(MainActivity.this , GroupManagementActivity.class);
        drawer.closeDrawer(drawerView);
        startActivity(intent);
    }
    /* ~Move to GroupManagementActivity */

    /** Move to AlarmManagementActivity */
    public void btnArsettingClick(View view){
        Intent intent = new Intent(MainActivity.this , AlarmActivity.class);
        drawer.closeDrawer(drawerView);
        startActivity(intent);
    }
    /* ~Move to GroupManagementActivity */

    /** Open Date picker and deleting original fragment
        and creating new fragment of selected date **/

    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            todayDate.set(Calendar.YEAR, y);
            todayDate.set(Calendar.MONTH, m);
            todayDate.set(Calendar.DAY_OF_MONTH, d);
            updateLabel();

            //delete fragment(now using)
            getSupportFragmentManager()
                    .beginTransaction().remove(fragmentmain).commit();
            Log.d("Fragment deleted","***********프래그먼트 삭제*************");

            fragmentmain = MainFragment.getInstance(sendDate);

            //make new fragment
            getSupportFragmentManager()
                    .beginTransaction().add(R.id.fragmentcontainer,fragmentmain).commit();


        }
    };
    /*~ Open Date picker and deleting original fragment
     and creating new fragment of selected date */

    /** Change Date Using from selected date by Date Picker **/
    private void updateLabel(){
        baseDate = sdf.format(todayDate.getTime());
        sendDate = sdf2.format(todayDate.getTime());
        tv_date.setText(baseDate);
    }
    /* ~ Change Date*/

    /** terminate application */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("앱을 종료하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbHelper.close();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    /* ~terminate application */

    /** Create Sample Data*/
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
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(1, 1, '운동', 1, '123.123123', '123.123123', '2020-02-03', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(2, 1, '운동', 1, '123.123123', '123.123123', '2020-02-06', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(3, 1, '운동', 1, '123.123123', '123.123123', '2020-02-10', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(4, 1, '운동', 1, '123.123123', '123.123123', '2020-02-13', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(5, 1, '운동', 1, '123.123123', '123.123123', '2020-02-17', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(6, 1, '운동', 1, '123.123123', '123.123123', '2020-02-20', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(7, 1, '운동', 1, '123.123123', '123.123123', '2020-02-24', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(8, 1, '운동', 1, '123.123123', '123.123123', '2020-02-27', '2020-02-01', '2020-02-28', 1);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(9, 0, '영어공부', 4, '', '', '2020-02-02', '2020-02-02', '2020-02-02', 2);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(10, 0, '밥', 2, '', '', '2020-02-09', '2020-02-09', '2020-02-09', 3);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(11, 2, '레슨', 3, '', '', '2020-02-11', '2020-02-11', '2020-02-18', 4);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
                    "VALUES(12, 2, '레슨', 3, '', '', '2020-02-16', '2020-02-11', '2020-02-18', 4);");

            db.execSQL("" +
                    "INSERT INTO _ITEM " +
                    "(item_id, group_id, item_name, item_important, item_location_X, item_location_Y, done_date, start_date, end_date, to_do_id)" +
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
        }
    }
    /* ~dummyCreate */
}
