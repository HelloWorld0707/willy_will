package com.willy.will.add.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Iterator;

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
    private int important_result;
    private double latitudeNum;
    private double longitudeNum;
    private String item_name=null;
    public static DBAccess dbHelper;

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
    private CheckBox[] dayCheckBoxes;
    private TextView place_name;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        dbHelper = DBAccess.getDbHelper();
        resources = getResources();
        place_name = (TextView)findViewById(R.id.place_name);


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
        dayCheckBoxes = new CheckBox[7];
        String dayViewName = "day";
        String packageName = getPackageName();
        int viewId;
        for(int i = 0; i < 7; i++) {
            viewId = resources.getIdentifier(dayViewName + i, "id", packageName);
            dayCheckBoxes[i] = findViewById(viewId);
        }

        repeat_switch = findViewById(R.id.repeat_switch);
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
        //Button btn_start = findViewById(R.id.btn_start);
        //Button btn_end = findViewById(R.id.btn_end);
        Text_start = findViewById(R.id.Text_start);
        Text_end = findViewById(R.id.Text_end);

        Text_start.setText(formatDate);
        Text_end.setText(formatDate);

        /** set value **/
        resources = getResources();
        start_date_key = resources.getString(R.string.start_date_key);
        end_date_key = resources.getString(R.string.end_date_key);

        start_date = simpleDateFormat.format(today.getTime());;
        end_date = simpleDateFormat.format(today.getTime());;

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
                    for(CheckBox dayCheckBox : dayCheckBoxes) {
                        dayCheckBox.setChecked(false);
                    }
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
            start_date = simpleDateFormat.format(today.getTime());
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
            end_date = simpleDateFormat.format(today.getTime());
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

    public String convertDate(String checkDate) {
        String loop_check_date=null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nDate = null;
        try {
            nDate = dateFormat.parse(checkDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayNum ) {
            case 1: loop_check_date = "일"; break;
            case 2: loop_check_date = "월"; break;
            case 3: loop_check_date = "화"; break;
            case 4: loop_check_date = "수"; break;
            case 5: loop_check_date = "목"; break;
            case 6: loop_check_date = "금"; break;
            case 7: loop_check_date = "토"; break;
        }
        return loop_check_date;
    }

    public void add_insert(View view) throws ParseException {
        ArrayList<String> checkedDays = new ArrayList<>();
        if(repeat_switch.isChecked()) {
            check_result = "";
            for(CheckBox dayCheckBox : dayCheckBoxes) {
                if(dayCheckBox.isChecked()) {
                    check_result += "1";
                    checkedDays.add(dayCheckBox.getText().toString());
                }
                else {
                    check_result += "0";
                }
            }
        }
        else {
            check_result = null;
        }

        SQLiteDatabase db=DBAccess.getDbHelper().getWritableDatabase();

        //for _CALENDAR
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = sdf.parse(start_date);
        Date edate = sdf.parse(end_date);
        long calDate = edate.getTime() - sdate.getTime();
        long dateGap = calDate / (24*60*60*1000);
        dateGap = Math.abs(dateGap);
        String calenderDate = null;

        //for _ITEM : LOOP OFF
        int group_id = selectedGroup.getGroupId(); //group_id
        item_name = Title_editText.getText().toString(); // item_name
        int item_important = important_result; //item_important
        String latitude = latitudeNum+""; //latitude
        String longitude = longitudeNum+""; //longitude
        String done_date = null; //done_date
        String startDate = start_date;
        String endDate = end_date;
        String loopweek = check_result;

        if(item_name.equals("") || item_name == null) {
            Toast.makeText(getApplicationContext(), "할 일 이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }

        else if(sdf.parse(start_date).getTime() > sdf.parse(end_date).getTime()) {
            Toast.makeText(getApplicationContext(), "날짜를 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        else {
            Cursor cursor = DBAccess.getDbHelper().getReadableDatabase().rawQuery("SELECT max(to_do_id) as to_do_id FROM _ITEM", null);
            cursor.moveToNext();
            int toDoId = cursor.getInt(cursor.getColumnIndexOrThrow("to_do_id")) + 1 ;

            /** ADD NOT LOOP ITEM */
            if(loopweek == null || loopweek.equals("0000000")) {
                /** Insert into _ITEM */
                db.execSQL("" +
                        "INSERT INTO _ITEM(group_id, item_name,item_important,latitude,longitude,done_date,start_date,end_date,to_do_id)" +
                        "VALUES(" +
                        group_id + ", '" +
                        item_name + "', '" +
                        item_important + "', " + latitude + ", " + longitude + ", " + done_date + ", '" +
                        startDate + "', '" +
                        endDate + "', " +
                        toDoId + ");"
                );
                /* ~Insert into _ITEM */

                /** Insert into _Calandar*/

                //setting startDate for DB
                calendar.setTime(sdate);
                calendar.add(Calendar.DATE,-1); // for문 돌리면 startdate 안들어가서 미리 setting

                for(int i=0; i<dateGap+1; i++) {
                    calendar.add(Calendar.DATE,1);
                    calenderDate = sdf.format(calendar.getTime());

                    db.execSQL(
                            "INSERT INTO _CALENDAR(calendar_date, item_id)\n" +
                            "VALUES('" + calenderDate + "'," +
                            "(Select item_id\n" +
                            "from _ITEM\n" +
                            "WHERE item_id = (SELECT MAX(item_id) FROM _ITEM)));"
                    );
                }
                /* ~Insert into _Calandar*/
            }
            /* ~ADD NOT LOOP ITEM */

            /** ADD LOOP ITEM */
            else {
                calendar.setTime(sdate);
                calendar.add(Calendar.DATE,-1); // for문 돌리면 startdate 안들어가서 미리 setting

                Iterator<String> iterCheckedDays;
                String curStr;
                for (int i = 0; i < dateGap + 1; i++) {
                    calendar.add(Calendar.DATE, 1);
                    calenderDate = sdf.format(calendar.getTime());

                    iterCheckedDays = checkedDays.iterator(); // 월 화 수
                    curStr = null;
                    while(iterCheckedDays.hasNext()) {
                        curStr = iterCheckedDays.next();
                        if (convertDate(calenderDate).equals(curStr)) {
                            db.execSQL(
                                    "INSERT INTO _ITEM(group_id, item_name,item_important,latitude,longitude,done_date,start_date,end_date,to_do_id)" +
                                    "VALUES(" +
                                    group_id + ", '" +
                                    item_name + "', '" +
                                    item_important + "', " + latitude + ", " + longitude + ", " + done_date + ", '" +
                                    startDate + "', '" +
                                    endDate + "', " +
                                    toDoId + ");"
                            );

                            db.execSQL(
                                    "INSERT INTO _CALENDAR(calendar_date, item_id)\n" +
                                    "VALUES('" + calenderDate + "'," +
                                    "(Select item_id\n" +
                                    "from _ITEM\n" +
                                    "WHERE item_id = (SELECT MAX(item_id) FROM _ITEM)));"
                            );

                        }
                    }
                }
                /* ~Insert into _Calandar*/
                /** Insert in to Loop*/
                ContentValues contentValues = new ContentValues();
                contentValues.put("to_do_id", toDoId);
                contentValues.put("loop_week", loopweek);
                db.insert("_LOOP_INFO", null, contentValues);
            }
            Toast.makeText(getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();
            this.setResult(R.integer.item_change_return_code);
            finish();
        }
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
                place_name.setText(location.getPlaceName());
            }
        }
        /* ~Success to receive data */
    }

}



