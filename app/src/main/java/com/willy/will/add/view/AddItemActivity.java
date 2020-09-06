package com.willy.will.add.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.willy.will.R;
import com.willy.will.add.controller.AddItemController;
import com.willy.will.common.controller.App;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.Location;
import com.willy.will.common.view.GroupManagementActivity;
import com.willy.will.detail.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends Activity {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final int ADD_CODE = App.getContext().getResources().getInteger(R.integer.add_item_request_code);
    private final int MODIFY_CODE = App.getContext().getResources().getInteger(R.integer.modify_item_request_code);

    private Resources resources;
    private AddItemController addCtrl;
    private Calendar today;
    private String startDateKey;
    private String endDateKey;
    private int code;

    private int itemId;
    private String itemName;
    private int important;
    private Group selectedGroup;
    private String startDate;
    private String endDate;
    private double latitudeNum;
    private double longitudeNum;
    private String loopWeek;
    private String itemMemo;

    private EditText titleEditText;
    private Spinner importantSpinner;
    private TextView groupTextView;
    private TextView startTextView;
    private TextView endTextView;
    private TextView addressTextView;
    private TextView roadAddressTextView;
    private LinearLayout repeatLayout;
    private Switch repeatSwitch;
    private View repeatCheckBoxesLayout;
    private CheckBox[] dayCheckBoxes;
    private Switch memoSwitch;
    private EditText memoEditText;

    private DatePickerDialog datePickerDialog = null;
    private DateListener dateListener = null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        resources = getResources();
        code = getIntent().getIntExtra(resources.getString(R.string.request_code), ADD_CODE);

        today = Calendar.getInstance();

        /** Set data **/
        String address;
        String roadAddress;
        if(code == ADD_CODE) {
            itemName = "";

            important = 1;

            selectedGroup = new Group(
                    resources.getInteger(R.integer.no_group_id),
                    resources.getString(R.string.no_group),
                    String.format("#%08X", (0xFFFFFFFF & resources.getColor(R.color.colorNoGroup, null)))
            );

            startDate = simpleDateFormat.format(today.getTime());;
            endDate = simpleDateFormat.format(today.getTime());;

            address = resources.getString(R.string.no_location);
            roadAddress = "";
            latitudeNum = resources.getInteger(R.integer.default_location);
            longitudeNum = resources.getInteger(R.integer.default_location);

            itemMemo = "";
        }
        else if(code == MODIFY_CODE) {
            Item selectedItem = getIntent().getParcelableExtra(resources.getString(R.string.selected_item_key));

            itemId = selectedItem.getItemId();

            itemName = selectedItem.getItemName();

            important = selectedItem.getImportant();

            selectedGroup = new Group(
                    selectedItem.getGroupId(),
                    selectedItem.getGroupName(),
                    selectedItem.getGroupColor()
            );

            startDate = selectedItem.getStartDate();
            endDate = selectedItem.getEndDate();

            address = selectedItem.getAddressName();
            roadAddress = selectedItem.getRoadAddressName();
            latitudeNum = Double.parseDouble(selectedItem.getLatitude());
            longitudeNum = Double.parseDouble(selectedItem.getLongitude());

            loopWeek = selectedItem.getLoopWeek();

            itemMemo = selectedItem.getItemMemo();
        }
        else {
            Log.e("AddItemActivity", "Initialization: Wrong code");
            setResult(RESULT_CANCELED);
            this.finish();
            return;
        }
        /* ~Set data */

        addCtrl = new AddItemController(resources);

        /** Set views **/
        titleEditText = (EditText)findViewById(R.id.title_edit_text);
        titleEditText.setText(itemName);
        // edit keyboard invisible 1
        titleEditText.setInputType(0);
        titleEditText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                titleEditText.setInputType(1);
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(titleEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        importantSpinner = findViewById(R.id.important_spinner);
        final String[] importantArr = resources.getStringArray(R.array.importance);
        importantSpinner.setSelection(important - 1);
        importantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int SIZE = importantArr.length;
                for(int i = 0; i < SIZE; i++) {
                    if(i == position) {
                        important = position + 1;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        groupTextView = findViewById(R.id.group_name);
        groupTextView.setText(selectedGroup.getGroupName());

        startTextView = findViewById(R.id.start_text_view);
        startTextView.setText(startDate);
        endTextView = findViewById(R.id.end_text_view);
        endTextView.setText(endDate);

        addressTextView = (TextView) findViewById(R.id.address_name);
        addressTextView.setText(address);
        roadAddressTextView = (TextView) findViewById(R.id.road_address_name);
        roadAddressTextView.setText(roadAddress);

        if (code == ADD_CODE) {
            repeatSwitch = findViewById(R.id.repeat_switch);
            repeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    // checked -> add_item_repeat
                    if (repeatSwitch.isChecked() == true) {
                        repeatCheckBoxesLayout.setVisibility(View.VISIBLE);

                        final ScrollView scrollView = findViewById(R.id.add_scroll_view);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    } else {
                        for (CheckBox dayCheckBox : dayCheckBoxes) {
                            dayCheckBox.setChecked(false);
                        }
                        repeatCheckBoxesLayout.setVisibility(View.GONE);
                    }
                }
            });
            repeatCheckBoxesLayout = findViewById(R.id.repeat_check_boxes_layout);
            dayCheckBoxes = new CheckBox[7];
            String dayViewName = "day";
            String packageName = getPackageName();
            int viewId;
            for (int i = 0; i < 7; i++) {
                viewId = resources.getIdentifier(dayViewName + i, "id", packageName);
                dayCheckBoxes[i] = findViewById(viewId);
            }
        }
        else {
            repeatLayout = findViewById(R.id.repeat_layout);
            repeatLayout.setVisibility(View.GONE);
        }

        memoSwitch = findViewById(R.id.memo_switch);
        memoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // checked -> add_item_repeat
                if (memoSwitch.isChecked() == true) {
                    memoEditText.setVisibility(View.VISIBLE);

                    final ScrollView scrollView=findViewById(R.id.add_scroll_view);
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                else {
                    memoEditText.setVisibility(View.GONE);
                }
            }
        });
        memoEditText = (EditText)findViewById(R.id.memo_edit_text);
        memoEditText.setText(itemMemo);
        if (itemMemo != null && !(itemMemo.equals(""))) {
            memoEditText.setVisibility(View.VISIBLE);
            memoSwitch.setChecked(true);
        }
        /* ~Set views */

        /** Set theme of Dialogs **/
        datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme);
        dateListener = new DateListener();
        datePickerDialog.setOnDateSetListener(dateListener);
        /* ~Set theme of Dialogs */

        /** Set values **/
        startDateKey = resources.getString(R.string.start_date_key);
        endDateKey = resources.getString(R.string.end_date_key);
        /* ~Set values */
    }

    class DateListener implements DatePickerDialog.OnDateSetListener {
        private String key = null;

        public void setKey(String key) {this.key = key;}
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);

            if(key.equals(startDateKey)) {
                startDate = simpleDateFormat.format(calendar.getTime());
                startTextView.setText(startDate);
            }
            else if(key.equals(endDateKey)) {
                endDate = simpleDateFormat.format(calendar.getTime());
                endTextView.setText(endDate);
            }
            else {
                Log.e("DateListener", "Invalid Key");
            }
        }
    }

    public void backToMain(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        // ~Check focusing
        setResult(RESULT_CANCELED);
        this.finish();
    }

    public void submit(View view) {
        itemName = titleEditText.getText().toString();
        if(itemName.equals("") || (itemName == null)) {
            Toast.makeText(getApplicationContext(), resources.getString(R.string.item_name_is_null), Toast.LENGTH_SHORT).show();
        }
        else {
            long startTime;
            long endTime;
            try {
                startTime = simpleDateFormat.parse(startDate).getTime();
                endTime = simpleDateFormat.parse(endDate).getTime();
            } catch (ParseException e) {
                Log.e("AddItemActivity", e.toString());
                return;
            }
            if (startTime > endTime) {
                Toast.makeText(getApplicationContext(),resources.getString(R.string.item_date_mismatch), Toast.LENGTH_SHORT).show();
            }
            else {
                String latitude, longitude;
                if(latitudeNum == resources.getInteger(R.integer.default_location)) {
                    latitude = null;
                }
                else {
                    latitude = Double.toString(latitudeNum);
                }
                if(longitudeNum == resources.getInteger(R.integer.default_location)) {
                    longitude = null;
                }
                else {
                    longitude = Double.toString(longitudeNum);
                }

                if(!memoSwitch.isChecked()) {
                    itemMemo = null;
                }
                else {
                    itemMemo = memoEditText.getText().toString();
                }

                ArrayList<String> checkedDays;
                if (code == ADD_CODE) {
                    if (repeatSwitch.isChecked()) {
                        loopWeek = "";
                        checkedDays = new ArrayList<>();
                        for (CheckBox dayCheckBox : dayCheckBoxes) {
                            if (dayCheckBox.isChecked()) {
                                loopWeek += "1";
                                checkedDays.add(dayCheckBox.getText().toString());
                            }
                            else {
                                loopWeek += "0";
                            }
                        }
                    }
                    else {
                        loopWeek = null;
                        checkedDays = null;
                    }

                    addCtrl.addItem(
                            selectedGroup.getGroupId(), itemName, important, startDate, endDate,
                            latitude, longitude, loopWeek, checkedDays, itemMemo
                    );
                    Toast.makeText(getApplicationContext(), resources.getString(R.string.successful_add), Toast.LENGTH_SHORT).show();
                    setResult(resources.getInteger(R.integer.item_change_return_code));
                    this.finish();
                }
                else if (code == MODIFY_CODE) {
                    if(loopWeek == null) {
                        checkedDays = null;
                    }
                    else {
                        checkedDays = new ArrayList<>();
                        final int LOOP_WEEK_LENGTH = loopWeek.length();
                        for(int i = 0; i < LOOP_WEEK_LENGTH; i++) {
                            if(loopWeek.charAt(i) == '1') {
                                checkedDays.add(dayCheckBoxes[i].getText().toString());
                            }
                        }
                    }

                    addCtrl.modifyItem(
                            itemId, selectedGroup.getGroupId(), itemName, important,
                            latitude, longitude, startDate, endDate, loopWeek, checkedDays, itemMemo);

                    Intent intent = new Intent();
                    intent.putExtra(resources.getString(R.string.modified_item_key), setModifiedItem());
                    setResult(RESULT_FIRST_USER, intent);
                    this.finish();
                }
            }
        }
    }

    private Item setModifiedItem() {
        Item modifiedItem = new Item();

        modifiedItem.setItemName(itemName);
        modifiedItem.setImportant(important);
        modifiedItem.setGroupId(selectedGroup.getGroupId());
        modifiedItem.setGroupName(selectedGroup.getGroupName());
        modifiedItem.setGroupColor(selectedGroup.getGroupColor());
        modifiedItem.setStartDate(startDate);
        modifiedItem.setEndDate(endDate);
        modifiedItem.setItemMemo(itemMemo);

        return modifiedItem;
    }

    public void bringUpGroupSetting(View view) {
        Intent intent = new Intent(this, GroupManagementActivity.class);

        int code = resources.getInteger(R.integer.group_setting_code);
        intent.putExtra(resources.getString(R.string.request_code), code);
        startActivityForResult(intent, code);
    }

    // Start Date Picker Dialog for start of start date
    public void setStart(View view) {
        dateListener.setKey(startDateKey);
        if(startDate.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
            startDate = simpleDateFormat.format(today.getTime());
        }
        else {
            try {
                Date date = simpleDateFormat.parse(startDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        datePickerDialog.setMessage(resources.getString(R.string.start_date));
        datePickerDialog.show();
    }

    // Start Date Picker Dialog for start of start date
    public void setEnd(View view) {
        dateListener.setKey(endDateKey);

        if(endDate.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
            endDate = simpleDateFormat.format(today.getTime());
        }
        else {
            try {
                Date date = simpleDateFormat.parse(endDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        datePickerDialog.setMessage(resources.getString(R.string.end_date));
        datePickerDialog.show();
    }

    public void bringUpLocationSearch(View view){
        Intent intent = new Intent(this, LocationSearchActivity.class);
        int code = resources.getInteger(R.integer.location_search_code);
        intent.putExtra(resources.getString(R.string.location_search_code),code);
        startActivityForResult(intent, code);
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
                Location location = data.getParcelableExtra(resources.getString(R.string.location_search_key));
                latitudeNum = location.getLatitude();
                longitudeNum = location.getLongitude();
                addressTextView.setText(location.getAddressName());
                if(location.getRoadAddressName()!=null){
                    roadAddressTextView.setVisibility(View.VISIBLE);
                    roadAddressTextView.setText(location.getRoadAddressName());
                }
            }
        }
        /* ~Success to receive data */
    }

}



