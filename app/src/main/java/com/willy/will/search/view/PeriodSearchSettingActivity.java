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

    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    private String startOfPeriodKey = null;
    private String endOfPeriodKey = null;

    private Resources resources = null;
    private Toast dateError = null;
    private Button startOfPeriodButton = null;
    private Button endOfPeriodButton = null;
    private DatePickerDialog datePickerDialog = null;
    private DateListener dateListener = null;

    private String startOfPeriod = null;
    private String endOfPeriod = null;

    // Initialization (including layout ID)
    public PeriodSearchSettingActivity() {
        super(R.layout.activity_period_search_setting);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = getResources();
        dateError = Toast.makeText(this, resources.getString(R.string.date_error), Toast.LENGTH_SHORT);

        /** Set theme of Dialogs **/
        datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme);
        dateListener = new DateListener();
        datePickerDialog.setOnDateSetListener(dateListener);
        /* ~Set theme of Dialogs */

        /** Set Views **/
        startOfPeriodButton = findViewById(R.id.start_of_period_button);
        endOfPeriodButton = findViewById(R.id.end_of_period_button);
        /* ~Set Views */

        /** Set values **/
        startOfPeriodKey = resources.getString(R.string.start_of_period_key);
        endOfPeriodKey = resources.getString(R.string.end_of_period_key);

        Intent intent = getIntent();
        startOfPeriod = intent.getStringExtra(startOfPeriodKey);
        endOfPeriod = intent.getStringExtra(endOfPeriodKey);

        startOfPeriodButton.setText(startOfPeriod);
        endOfPeriodButton.setText(endOfPeriod);
        /* ~Set values */
    }

    // Start Date Picker Dialog for start of to-do period
    public void setStartOfPeriod(View view) {
        dateListener.setKey(startOfPeriodKey);

        try {
            Date date = simpleDateFormat.parse(startOfPeriod);
            calendar.setTime(date);
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        }
        catch (ParseException e) {
            Log.e("PeriodSearchSettingActivity", "Setting date: " + e.getMessage());
            e.printStackTrace();
        }

        datePickerDialog.setMessage(resources.getString(R.string.start_of_period_text));
        datePickerDialog.show();
    }

    // Start Date Picker Dialog for end of to-do period
    public void setEndOfPeriod(View view) {
        dateListener.setKey(endOfPeriodKey);

        try {
            calendar.setTime(simpleDateFormat.parse(endOfPeriod));
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        } catch (ParseException e) {
            Log.e("PeriodSearchSettingActivity", "Setting date: " + e.getMessage());
            e.printStackTrace();
        }

        datePickerDialog.setMessage(resources.getString(R.string.end_of_period_text));
        datePickerDialog.show();
    }

    class DateListener implements DatePickerDialog.OnDateSetListener {
        private String key = null;

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);

            if(key.equals(startOfPeriodKey)) {
                startOfPeriod = simpleDateFormat.format(calendar.getTime());
                startOfPeriodButton.setText(startOfPeriod);
            }
            else if(key.equals(endOfPeriodKey)) {
                endOfPeriod = simpleDateFormat.format(calendar.getTime());
                endOfPeriodButton.setText(endOfPeriod);
            }
            else {
                Log.e("DateListener", "Invalid Key");
            }
        }
    }

    @Override
    protected boolean setResults(Intent intent) {
        try {
            /** Check to-do period **/
            long startOfToDo = simpleDateFormat.parse(startOfPeriod).getTime();
            long endOfToDo = simpleDateFormat.parse(endOfPeriod).getTime();
            // startOfToDoPeriod <= endOfToDoPeriod ? correct date : wrong date
            if(startOfToDo > endOfToDo) {
                dateError.show();
                return false;
            }
            /* ~Check to-do period */
        }
        catch (ParseException e) {
            Log.e("PeriodSearchSettingActivity", "Results: "+e.getMessage());
            e.printStackTrace();
            return false;
        }

        /** Put results in the intent **/
        intent.putExtra(startOfPeriodKey, startOfPeriod);
        intent.putExtra(endOfPeriodKey, endOfPeriod);
        /* ~Put results in the intent */
        return true;
    }

}
