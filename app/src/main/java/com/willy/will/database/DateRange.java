package com.willy.will.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateRange {

    public static final String MIN = "MIN";
    public static final String MAX = "MAX";

    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    private String beforeMin;
    private String afterMax;
    private long minValue;
    private long maxValue;

    public DateRange() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
    }

    public DateRange(String min, String max) throws ParseException {
        this();

        minValue = simpleDateFormat.parse(min).getTime();
        maxValue = simpleDateFormat.parse(max).getTime();

        calendar.setTime(simpleDateFormat.parse(min));
        calendar.add(Calendar.DATE, -1);
        beforeMin = simpleDateFormat.format(calendar.getTime());
        calendar.setTime(simpleDateFormat.parse(max));
        calendar.add(Calendar.DATE, 1);
        afterMax = simpleDateFormat.format(calendar.getTime());
    }

    public void setMinValue(String min) throws ParseException {
        minValue = simpleDateFormat.parse(min).getTime();

        calendar.setTime(simpleDateFormat.parse(min));
        calendar.add(Calendar.DATE, -1);
        beforeMin = simpleDateFormat.format(calendar.getTime());
    }

    public void setMaxValue(String max) throws ParseException {
        maxValue = simpleDateFormat.parse(max).getTime();

        calendar.setTime(simpleDateFormat.parse(max));
        calendar.add(Calendar.DATE, 1);
        afterMax = simpleDateFormat.format(calendar.getTime());
    }

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public String getBeforeMin() {
        return beforeMin;
    }

    public String getAfterMax() {
        return afterMax;
    }

}
