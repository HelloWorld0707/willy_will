package com.willy.will.main.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.willy.will.R;
import com.willy.will.add.view.AddItemActivity;
import com.willy.will.calander.view.CalendarActivity;
import com.willy.will.database.DBAccess;
import com.willy.will.search.view.SearchActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    //var for spinner
    private Spinner sp_group;
    ArrayList<String> spgroupList;
    ArrayAdapter<String> spgroupAdapter;
    //~var for spinner

    //var for navigation drawer
    private DrawerLayout drawer;
    private View drawerView;
    //~var for navigation drawer

    //var for date txt
    TextView tv_date;
    Calendar todayDate;
    SimpleDateFormat sdf;
    SimpleDateFormat sdf2;
    String baseDate;
    String sendDate;


    MainFragment fragmentmain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        DBAccess dbHelper = new DBAccess(this, "test.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

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

    /**Setting action for drawer menu**/
    DrawerLayout.DrawerListener naviListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) { }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            drawer.openDrawer(drawerView);
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) { }

        @Override
        public void onDrawerStateChanged(int newState) { }
    };
    /*~Setting action for drawer menu*/


    /** Function: Move to SearchView */
    public void btnSearchClick(View view){
        Intent intent = new Intent(MainActivity.this , SearchActivity.class);
        intent.putExtra(getResources().getString(R.string.current_date_key),sendDate);
        Log.d("DateChecked","**********날짜"+sendDate+"*************");
        startActivity(intent);
    }
    /*~ Function: Move to SearchView */


    /** Move to CalendarView */
    public void btnCalendarClick(View view) {
        Intent intent = new Intent(MainActivity.this , CalendarActivity.class);
        intent.putExtra(getResources().getString(R.string.current_date_key),sendDate);
        Log.d("DateChecked","**********날짜"+sendDate+"*************");
        startActivity(intent);
    }
    /*~Move to CalendarView*/

    /** Open navigation drawer */
    public void btnSettingClick(View view){
        naviListener.onDrawerOpened(drawerView);
    }
    /*~ Open navigation drawer */


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

}
