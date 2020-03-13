package com.willy.will.add.view;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.willy.will.R;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.Location;
import com.willy.will.common.view.GroupManagementActivity;
import com.willy.will.database.DBAccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private String important_string = null;
    private int important_result;
    private double latitudeNum;
    private double longitudeNum;

    public static DBAccess dbHelper;

    ImageButton check_button;
    private String[] check_value = new String[7];
    private String check_result = null;

    Switch repeat_switch;
    TextView Text_start;
    TextView Text_end;
    EditText Title_editText;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
    String formatDate = sdfNow.format(date);
    TextView dateNow;

    private TextView groupTextView;
    private Group selectedGroup;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemadd);

        dbHelper = DBAccess.getDbHelper();
        //dummyCreate();
        resources = getResources();

        /************************* 중요도 data **************************************/
        important = (Spinner)findViewById(R.id.important);
        important.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    important_result=1;
                }
                else if(position==1){
                    important_result=2;
                }
                else if(position==2){
                    important_result=3;
                }else{
                    important_result=4;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /************************* 반복 data **************************************/
        final CheckBox Monday = (CheckBox)findViewById(R.id.Monday);
        final CheckBox Tuesday = (CheckBox)findViewById(R.id.Tuesday);
        final CheckBox Wednesday = (CheckBox)findViewById(R.id.Wednesday);
        final CheckBox Thursday = (CheckBox)findViewById(R.id.Thursday);
        final CheckBox Friday = (CheckBox)findViewById(R.id.Friday);
        final CheckBox Saturday = (CheckBox)findViewById(R.id.Saturday);
        final CheckBox sunday = (CheckBox)findViewById(R.id.sunday);
        check_button = (ImageButton)findViewById(R.id.check_button);
        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";  // 결과를 출력할 문자열  ,  항상 스트링은 빈문자열로 초기화 하는 습관을 가지자
                if(Monday.isChecked() == true) {
                    check_value[0] = "1";
                }else{
                    check_value[0] = "0";
                }
                if(Tuesday.isChecked() == true) {
                    check_value[1] = "1";
                }else{
                    check_value[1] = "0";
                }
                if(Wednesday.isChecked() == true) {
                    check_value[2] = "1";
                }else{
                    check_value[2] = "0";
                }
                if(Thursday.isChecked() == true) {
                    check_value[3] = "1";
                }else{
                    check_value[3] = "0";
                }
                if(Friday.isChecked() == true) {
                    check_value[4] = "1";
                }else{
                    check_value[4] = "0";
                }
                if(Saturday.isChecked() == true) {
                    check_value[5] = "1";
                }else{
                    check_value[5] = "0";
                }
                if(sunday.isChecked() == true) {
                    check_value[6] = "1";
                }else{
                    check_value[6] = "0";
                }
                check_result="";
                for(int i=0; i<check_value.length;i++){

                    check_result += check_value[i];
                }
                Toast.makeText(getApplicationContext(),check_result, Toast.LENGTH_SHORT).show();
            }
        });

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
        selectedGroup = new Group(
                resources.getInteger(R.integer.no_group_id),
                resources.getString(R.string.no_group),
                String.format("#%08X", (0xFFFFFFFF & resources.getColor(R.color.colorNoGroup, null)))
        );

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

        /** Group **/
        groupTextView = findViewById(R.id.group_textview);
        groupTextView.setText(selectedGroup.getGroupName());
        /*Button bnt_group = findViewById(R.id.bnt_group);
        bnt_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItemActivity.this, GroupManagementActivity.class);
                startActivity(intent);
            }
        });*/


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

    public void bringUpGroupSetting(View view) {
        Intent intent = new Intent(this, GroupManagementActivity.class);

        int code = resources.getInteger(R.integer.group_setting_code);
        intent.putExtra(resources.getString(R.string.request_code), code);
        startActivityForResult(intent, code);
    }

    public void bringUplocationSearch(View view){
        Intent intent = new Intent(this, LocationSearchActivity.class);
        int code = resources.getInteger(R.integer.location_search_code);
        intent.putExtra(resources.getString(R.string.location_search_code),code);
        startActivityForResult(intent, code);
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

    public void add_insert(View view){
        SQLiteDatabase db=DBAccess.getDbHelper().getWritableDatabase();

        /** 테이블 : _CALENDAR  삽입******************************************************************/
        String calendar_dates = "2020-02-13";
        int item_id=100;
        db.execSQL("" +
                "INSERT INTO _CALENDAR(calendar_date, item_id)" +
                "VALUES('" + calendar_dates + "', '" + item_id+ "')"
        );

        /** 테이블 : _ITEM  삽입***********************************************************************/
        int group_id = selectedGroup.getGroupId(); //group_id
        String item_name = Title_editText.getText().toString(); //item_name
        int item_important = important_result; //item_important
        String latitude = latitudeNum+""; //latitude
        String longitude = longitudeNum+""; //longitude
        String done_date = null; //done_date
        String StartDate = start_date;
        String EndDate = end_date;
        int to_do_id = 100; // to_do_id

        db.execSQL("" +
                "INSERT INTO _ITEM(group_id, item_name,item_important,latitude,longitude,done_date,start_date,end_date,to_do_id)" +
                "VALUES(" +
                group_id + ", '" +
                item_name + "', '" +
                item_important + "', " + latitude + ", " + longitude + ", " + done_date + ", '" +
                StartDate + "', '" +
                EndDate + "', '" +
                to_do_id + "')"
        );
        Toast.makeText(getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Receive result data from other Activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Success to receive data **/
        if(resultCode == Activity.RESULT_FIRST_USER) {
            // Group setting (to move tasks to this group)
            if(requestCode == resources.getInteger(R.integer.group_setting_code)) {
                String groupSettingKey = resources.getString(R.string.group_setting_key);
                selectedGroup = data.getParcelableExtra(groupSettingKey);
                groupTextView.setText(selectedGroup.getGroupName());
            }else if(requestCode == resources.getInteger(R.integer.location_search_code)){
                ArrayList<Location> locationArrayList = data.getParcelableArrayListExtra(resources.getString(R.string.location_search_key));
                Location location = locationArrayList.get(0);
                latitudeNum = location.getLatitude();
                longitudeNum = location.getLongitude();


            }
        }
        /* ~Success to receive data */
    }

}



