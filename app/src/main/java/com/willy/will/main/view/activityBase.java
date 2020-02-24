package com.willy.will.main.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.willy.will.R;
import com.willy.will.add.view.activityItemAdd;
import com.willy.will.search.view.SearchActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class activityBase extends AppCompatActivity{

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
    String dateString;


    fragmentMain fragmentmain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */

        //set navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.nav_view);
        //~set navigation Drawer

        //setDate

        tv_date = (TextView) findViewById(R.id.tv_date);
        todayDate = Calendar.getInstance();
        sdf = new SimpleDateFormat("MM.dd");
        dateString = sdf.format(todayDate.getInstance().getTime());
        tv_date.setText(dateString);
        //~setDate

        //set fragment
        fragmentmain = fragmentMain.getInstance(dateString);

        //add the fragment to container(frame layout)
        getSupportFragmentManager()
                .beginTransaction().add(R.id.fragmentcontainer,fragmentmain).commit();
        //~set fragment

        //open picker & change txt
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(activityBase.this, datepicker,
                        todayDate.get(Calendar.YEAR), todayDate.get(Calendar.MONTH), todayDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        //set sp_group (fix later)
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
        //~Set sp_group

        // set fab event Listener
        FloatingActionButton fab = findViewById(R.id.fabItemAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show Item add Activity
                Intent intent = new Intent(activityBase.this , activityItemAdd.class);
                startActivity(intent);
            }
        });
        // ~set fab event Listener
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-19
     * Created By: Lee Jaeeun
     * Function: Setting action for drawer menu
     */
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    public void btnSearchClick(View view){
        Intent intent = new Intent(activityBase.this , SearchActivity.class);
        startActivity(intent);
    }


    /**
     * Last Modified: 20.02.24
     * Last Modified By: Lee Jaeeun
     * Created: -
     * Created By: Lee Jaeeun
     * Function: Move to CalendarView (temporary connect to search)
     * @param view
     */
    public void btnCalendarClick(View view) {
        Intent intent = new Intent(activityBase.this , SearchActivity.class);
        startActivity(intent);
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-19
     * Created By: Lee Jaeeun
     * Function: Open navigation drawer
     * @param view
     */
    public void btnSettingClick(View view){
        naviListener.onDrawerOpened(drawerView);
    }


    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-24
     * Created By: Lee Jaeeun
     * Function: Open Date picker
     * @param y (year)
     * @param m (month)
     * @param d (day)
     */

    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            todayDate.set(Calendar.YEAR, y);
            todayDate.set(Calendar.MONTH, m);
            todayDate.set(Calendar.DAY_OF_MONTH, d);
            updateLabel();
            getSupportFragmentManager()
                    .beginTransaction().add(R.id.fragmentcontainer,fragmentMain.getInstance(dateString)).commit();

        }
    };

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-24
     * Created By: Lee Jaeeun
     * Function: Change Date Using by Date Picker
     */
    private void updateLabel(){
        dateString = sdf.format(todayDate.getTime());
        tv_date.setText(dateString);
    }

    /*
    @Override
    // tool bar
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //
        if (id == R.id.itemSearch){
            // show search activity
            Intent intent = new Intent(activityBase.this , SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
