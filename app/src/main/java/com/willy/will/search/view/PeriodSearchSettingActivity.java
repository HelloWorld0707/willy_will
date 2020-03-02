package com.willy.will.search.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.willy.will.R;
import com.willy.will.common.model.PopupActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PeriodSearchSettingActivity extends PopupActivity {

    private SimpleDateFormat simpleDateFormat = null;
    private Calendar today = null;
    private Calendar calendar = null;

    private String startOfStartDateKey = null;
    private String endOfStartDateKey = null;
    private String startOfEndDateKey = null;
    private String endOfEndDateKey = null;
    private String onlyDoneKey = null;
    private String startOfDoneDateKey = null;
    private String endOfDoneDateKey = null;

    private Resources resources = null;
    private Button startOfStartButton = null;
    private Button endOfStartButton = null;
    private Button startOfEndButton = null;
    private Button endOfEndButton = null;
    private Button startOfDoneButton = null;
    private Button endOfDoneButton = null;
    private DatePickerDialog datePickerDialog = null;
    private DateListener dateListener = null;

    private String startOfStartDate = null;
    private String endOfStartDate = null;
    private String startOfEndDate = null;
    private String endOfEndDate = null;
    private boolean onlyDone;
    private String startOfDoneDate = null;
    private String endOfDoneDate = null;

    /**
     * Last Modified: 2020-02-26
     * Last Modified By: Shin Minyong
     * Created: 2020-02-17
     * Created By: Shin Minyong
     * Function: Initialization (including layout ID)
     */
    public PeriodSearchSettingActivity() {
        super(R.layout.activity_period_search_setting);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        today = Calendar.getInstance();
        calendar = Calendar.getInstance();
    }

    /**
     * Last Modified: 2020-02-26
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set theme of Dialogs
        datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme);
        dateListener = new DateListener();
        datePickerDialog.setOnDateSetListener(dateListener);
        // ~Set theme of Dialogs

        // Set Views
        startOfStartButton = findViewById(R.id.start_of_start_button);
        endOfStartButton = findViewById(R.id.end_of_start_button);
        startOfEndButton = findViewById(R.id.start_of_end_button);
        endOfEndButton = findViewById(R.id.end_of_end_button);
        startOfDoneButton = findViewById(R.id.start_of_done_button);
        endOfDoneButton = findViewById(R.id.end_of_done_button);
        // ~Set Views

        // Set values
        resources = getResources();
        startOfStartDateKey = resources.getString(R.string.start_of_start_date_key);
        endOfStartDateKey = resources.getString(R.string.end_of_start_date_key);
        startOfEndDateKey = resources.getString(R.string.start_of_end_date_key);
        endOfEndDateKey = resources.getString(R.string.end_of_end_date_key);
        onlyDoneKey = resources.getString(R.string.only_done_key);
        startOfDoneDateKey = resources.getString(R.string.start_of_done_date_key);
        endOfDoneDateKey = resources.getString(R.string.end_of_done_date_key);

        Intent intent = getIntent();
        startOfStartDate = intent.getStringExtra(startOfStartDateKey);
        endOfStartDate = intent.getStringExtra(endOfStartDateKey);
        startOfEndDate = intent.getStringExtra(startOfEndDateKey);
        endOfEndDate = intent.getStringExtra(endOfEndDateKey);
        onlyDone = intent.getBooleanExtra(onlyDoneKey, false);
        startOfDoneDate = intent.getStringExtra(startOfDoneDateKey);
        endOfDoneDate = intent.getStringExtra(endOfDoneDateKey);

        startOfStartButton.setText(startOfStartDate);
        endOfStartButton.setText(endOfStartDate);
        startOfEndButton.setText(startOfEndDate);
        endOfEndButton.setText(endOfEndDate);
        startOfDoneButton.setText(startOfDoneDate);
        endOfDoneButton.setText(endOfDoneDate);

        if(!onlyDone) {
            startOfDoneButton.setEnabled(false);
            endOfDoneButton.setEnabled(false);
        }
        // ~Set values
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-26
     * Created By: Shin Minyong
     * Function: Start Date Picker Dialog for start of start date
     * @param view
     */
    public void setStartOfStart(View view) {
        dateListener.setKey(startOfStartDateKey);

        if(startOfStartDate.length() > 0) {
            try {
                Date date = simpleDateFormat.parse(startOfStartDate);
                calendar.setTime(date);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }

        datePickerDialog.setMessage(resources.getString(R.string.start_of_start_date_text));
        datePickerDialog.show();
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-26
     * Created By: Shin Minyong
     * Function: Start Date Picker Dialog for end of start date
     * @param view
     */
    public void setEndOfStart(View view) {
        dateListener.setKey(endOfStartDateKey);

        if(endOfStartDate.length() > 0) {
            try {
                calendar.setTime(simpleDateFormat.parse(endOfStartDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }

        datePickerDialog.setMessage(resources.getString(R.string.end_of_start_date_text));
        datePickerDialog.show();
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-26
     * Created By: Shin Minyong
     * Function: Start Date Picker Dialog for start of end date
     * @param view
     */
    public void setStartOfEnd(View view) {
        dateListener.setKey(startOfEndDateKey);

        if(startOfEndDate.length() > 0) {
            try {
                calendar.setTime(simpleDateFormat.parse(startOfEndDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }

        datePickerDialog.setMessage(resources.getString(R.string.start_of_end_date_text));
        datePickerDialog.show();
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-26
     * Created By: Shin Minyong
     * Function: Start Date Picker Dialog for end of end date
     * @param view
     */
    public void setEndOfEnd(View view) {
        dateListener.setKey(endOfEndDateKey);

        if(endOfEndDate.length() > 0) {
            try {
                calendar.setTime(simpleDateFormat.parse(endOfEndDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }

        datePickerDialog.setMessage(resources.getString(R.string.end_of_end_date_text));
        datePickerDialog.show();
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-26
     * Created By: Shin Minyong
     * Function: Start Date Picker Dialog for start of done date
     * @param view
     */
    public void setStartOfDone(View view) {
        dateListener.setKey(startOfDoneDateKey);

        if(startOfDoneDate.length() > 0) {
            try {
                calendar.setTime(simpleDateFormat.parse(startOfDoneDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }

        datePickerDialog.setMessage(resources.getString(R.string.start_of_done_date_text));
        datePickerDialog.show();
    }

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-26
     * Created By: Shin Minyong
     * Function: Start Date Picker Dialog for end of done date
     * @param view
     */
    public void setEndOfDone(View view) {
        dateListener.setKey(endOfDoneDateKey);

        if(endOfDoneDate.length() > 0) {
            try {
                calendar.setTime(simpleDateFormat.parse(endOfDoneDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }

        datePickerDialog.setMessage(resources.getString(R.string.end_of_done_date_text));
        datePickerDialog.show();
    }

    /**
     * Last Modified: 2020-02-26
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * @param intent
     * @return
     */
    @Override
    protected boolean setResults(Intent intent) {
        try {
            if (!startOfStartDate.equals("") && !endOfStartDate.equals("")) {
                if (simpleDateFormat.parse(startOfStartDate).getTime() > simpleDateFormat.parse(endOfStartDate).getTime()) {
                    Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if (!startOfEndDate.equals("") && !endOfEndDate.equals("")) {
                if (simpleDateFormat.parse(startOfEndDate).getTime() > simpleDateFormat.parse(endOfEndDate).getTime()) {
                    Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if (onlyDone && !startOfDoneDate.equals("") && !endOfDoneDate.equals("")) {
                if (simpleDateFormat.parse(startOfDoneDate).getTime() > simpleDateFormat.parse(endOfDoneDate).getTime()) {
                    Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        catch (ParseException e) {
            Log.e("PeriodSearchSettingActivity", "Results: "+e.getMessage());
            e.printStackTrace();
            return false;
        }

        intent.putExtra(startOfStartDateKey, startOfStartDate);
        intent.putExtra(endOfStartDateKey, endOfStartDate);
        intent.putExtra(startOfEndDateKey, startOfEndDate);
        intent.putExtra(endOfEndDateKey, endOfEndDate);
        intent.putExtra(startOfDoneDateKey, startOfDoneDate);
        intent.putExtra(endOfDoneDateKey, endOfDoneDate);
        return true;
    }

    class DateListener implements DatePickerDialog.OnDateSetListener {
        private String key = null;

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);

            if(key.equals(startOfStartDateKey)) {
                startOfStartDate = simpleDateFormat.format(calendar.getTime());
                startOfStartButton.setText(startOfStartDate);
            }
            else if(key.equals(endOfStartDateKey)) {
                endOfStartDate = simpleDateFormat.format(calendar.getTime());
                endOfStartButton.setText(endOfStartDate);
            }
            else if(key.equals(startOfEndDateKey)) {
                startOfEndDate = simpleDateFormat.format(calendar.getTime());
                startOfEndButton.setText(startOfEndDate);
            }
            else if(key.equals(endOfEndDateKey)) {
                endOfEndDate = simpleDateFormat.format(calendar.getTime());
                endOfEndButton.setText(endOfEndDate);
            }
            else if(key.equals(startOfDoneDateKey)) {
                startOfDoneDate = simpleDateFormat.format(calendar.getTime());
                startOfDoneButton.setText(startOfDoneDate);
            }
            else if(key.equals(endOfDoneDateKey)) {
                endOfDoneDate = simpleDateFormat.format(calendar.getTime());
                endOfDoneButton.setText(endOfDoneDate);
            }
            else {
                Log.e("DateListener", "Invalid Key");
            }
        }
    }

}
