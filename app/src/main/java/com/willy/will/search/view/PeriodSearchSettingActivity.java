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

    // Initialization (including layout ID)
    public PeriodSearchSettingActivity() {
        super(R.layout.activity_period_search_setting);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        today = Calendar.getInstance();
        calendar = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Set theme of Dialogs **/
        datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme);
        dateListener = new DateListener();
        datePickerDialog.setOnDateSetListener(dateListener);
        /* ~Set theme of Dialogs */

        /** Set Views **/
        startOfStartButton = findViewById(R.id.start_of_start_button);
        endOfStartButton = findViewById(R.id.end_of_start_button);
        startOfEndButton = findViewById(R.id.start_of_end_button);
        endOfEndButton = findViewById(R.id.end_of_end_button);
        startOfDoneButton = findViewById(R.id.start_of_done_button);
        endOfDoneButton = findViewById(R.id.end_of_done_button);
        /* ~Set Views */

        /** Set values **/
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
        /* ~Set values */
    }

    // Start Date Picker Dialog for start of start date
    public void setStartOfStart(View view) {
        dateListener.setKey(startOfStartDateKey);

        if(startOfStartDate.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }
        else {
            try {
                Date date = simpleDateFormat.parse(startOfStartDate);
                calendar.setTime(date);
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }

        datePickerDialog.setMessage(resources.getString(R.string.start_of_start_date_text));
        datePickerDialog.show();
    }

    // Start Date Picker Dialog for end of start date
    public void setEndOfStart(View view) {
        dateListener.setKey(endOfStartDateKey);

        if(endOfStartDate.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }
        else {
            try {
                calendar.setTime(simpleDateFormat.parse(endOfStartDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }

        datePickerDialog.setMessage(resources.getString(R.string.end_of_start_date_text));
        datePickerDialog.show();
    }

    // Start Date Picker Dialog for start of end date
    public void setStartOfEnd(View view) {
        dateListener.setKey(startOfEndDateKey);

        if(startOfEndDate.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }
        else {
            try {
                calendar.setTime(simpleDateFormat.parse(startOfEndDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }

        datePickerDialog.setMessage(resources.getString(R.string.start_of_end_date_text));
        datePickerDialog.show();
    }

    // Start Date Picker Dialog for end of end date
    public void setEndOfEnd(View view) {
        dateListener.setKey(endOfEndDateKey);

        if(endOfEndDate.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }
        else {
            try {
                calendar.setTime(simpleDateFormat.parse(endOfEndDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }

        datePickerDialog.setMessage(resources.getString(R.string.end_of_end_date_text));
        datePickerDialog.show();
    }

    // Start Date Picker Dialog for start of done date
    public void setStartOfDone(View view) {
        dateListener.setKey(startOfDoneDateKey);

        if(startOfDoneDate.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }
        else {
            try {
                calendar.setTime(simpleDateFormat.parse(startOfDoneDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }

        datePickerDialog.setMessage(resources.getString(R.string.start_of_done_date_text));
        datePickerDialog.show();
    }

    // Start Date Picker Dialog for end of done date
    public void setEndOfDone(View view) {
        dateListener.setKey(endOfDoneDateKey);

        if(endOfDoneDate.isEmpty()) {
            datePickerDialog.updateDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE));
        }
        else {
            try {
                calendar.setTime(simpleDateFormat.parse(endOfDoneDate));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            } catch (ParseException e) {
                Log.e("PeriodSearchSettingActivity", "Setting date: "+e.getMessage());
                e.printStackTrace();
            }
        }

        datePickerDialog.setMessage(resources.getString(R.string.end_of_done_date_text));
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

    @Override
    protected boolean setResults(Intent intent) {
        try {
            long comparison = 0L;
            long sComparison = 0L;
            long eComparison = 0L;
            long current = 0L;
            /** Check start date **/
            // Check start of start date
            if(!startOfStartDate.isEmpty()) {
                comparison = simpleDateFormat.parse(startOfStartDate).getTime();
                sComparison = comparison;
            }
            // Check end of start date
            if(!endOfStartDate.isEmpty()) {
                current = simpleDateFormat.parse(endOfStartDate).getTime();
                // startOfStartDate <= endOfStartDate ? correct date : wrong date
                if(comparison > current) {
                    Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            /* ~Check start date */
            /** Check end date **/
            // Check start of end date
            if(!startOfEndDate.isEmpty()) {
                current = simpleDateFormat.parse(startOfEndDate).getTime();
                // startOfStartDate <= startOfEndDate ?
                if(comparison <= current) {
                    comparison = current;
                }
                else {
                    Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            // Check end of end date
            if(!endOfEndDate.isEmpty()) {
                current = simpleDateFormat.parse(endOfEndDate).getTime();
                // comparison (startOfStartDate or startOfEndDate) <= endOfEndDate ?
                if(comparison <= current) {
                    eComparison = current;
                }
                else {
                    Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            /* ~Check end date */
            /** Check done date **/
            if(onlyDone) {
                comparison = 0L;
                // Check start of done date
                if(!startOfDoneDate.isEmpty()) {
                    current = simpleDateFormat.parse(startOfDoneDate).getTime();
                    // startOfStartDate <= startOfDoneDate ?
                    if(sComparison <= current) {
                        comparison = current;
                    }
                    else {
                        Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if(endOfDoneDate.isEmpty()) {
                        // startOfDoneDate <= endOfEndDate ? correct date : wrong date
                        if(current > eComparison) {
                            Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }
                // Check end of done date
                if(!endOfDoneDate.isEmpty()) {
                    current = simpleDateFormat.parse(endOfDoneDate).getTime();
                    // comparison (startOfStartDate or startOfDoneDate) <= endOfDoneDate ? correct date : wrong date
                    if (comparison > current) {
                        Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    // endOfDoneDate <= endOfEndDate ? correct date : wrong date
                    if(current > eComparison) {
                        Toast.makeText(getBaseContext(), resources.getString(R.string.date_error), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
            /* ~Check done date */
        }
        catch (ParseException e) {
            Log.e("PeriodSearchSettingActivity", "Results: "+e.getMessage());
            e.printStackTrace();
            return false;
        }

        /** Put results in the intent **/
        intent.putExtra(startOfStartDateKey, startOfStartDate);
        intent.putExtra(endOfStartDateKey, endOfStartDate);
        intent.putExtra(startOfEndDateKey, startOfEndDate);
        intent.putExtra(endOfEndDateKey, endOfEndDate);
        intent.putExtra(startOfDoneDateKey, startOfDoneDate);
        intent.putExtra(endOfDoneDateKey, endOfDoneDate);
        /* ~Put results in the intent */
        return true;
    }

}
