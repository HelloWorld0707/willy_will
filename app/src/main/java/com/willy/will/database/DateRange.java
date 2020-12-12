package com.willy.will.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateRange {

    public static final String MIN = "MIN";
    public static final String MAX = "MAX";

    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    private String oneDayBeforeMin;
    private String oneDayAfterMax;
    private String min;
    private String max;
    private long minValue;
    private long maxValue;

    public DateRange() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
    }

    public DateRange(String min, String max) throws ParseException {
        this();

        this.min = min;
        this.max = max;
        minValue = simpleDateFormat.parse(min).getTime();
        maxValue = simpleDateFormat.parse(max).getTime();

        calendar.setTime(simpleDateFormat.parse(min));
        calendar.add(Calendar.DATE, -1);
        oneDayBeforeMin = simpleDateFormat.format(calendar.getTime());
        calendar.setTime(simpleDateFormat.parse(max));
        calendar.add(Calendar.DATE, 1);
        oneDayAfterMax = simpleDateFormat.format(calendar.getTime());
    }

    public void setMin(String min) throws ParseException {
        this.min = min;
        minValue = simpleDateFormat.parse(min).getTime();

        calendar.setTime(simpleDateFormat.parse(min));
        calendar.add(Calendar.DATE, -1);
        oneDayBeforeMin = simpleDateFormat.format(calendar.getTime());
    }

    public void setMax(String max) throws ParseException {
        this.max = max;
        maxValue = simpleDateFormat.parse(max).getTime();

        calendar.setTime(simpleDateFormat.parse(max));
        calendar.add(Calendar.DATE, 1);
        oneDayAfterMax = simpleDateFormat.format(calendar.getTime());
    }

    public String getOneDayBeforeMin() {
        return oneDayBeforeMin;
    }

    public String getOneDayAfterMax() {
        return oneDayAfterMax;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

}
