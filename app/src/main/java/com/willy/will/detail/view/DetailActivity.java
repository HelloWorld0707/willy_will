package com.willy.will.detail.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import com.willy.will.R;
import com.willy.will.database.DBAccess;
import com.willy.will.detail.model.Item;
import com.willy.will.setting.AlarmActivity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static com.willy.will.main.view.MainActivity.dbHelper;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;


public class DetailActivity extends Activity {

    private ImageView important, groupColor;
    private ImageButton editButton;
    private TextView itemName, groupName, calendarDate, startDate, endDate, doneDate, roof,achievementRate, address;
    private RelativeLayout achievementRateArea,calendarDateArea,startDateArea, endDateArea, doneDateArea;
    private String roofDay = "";
    private String addressName, buildingName;
    private String[] days = {"일","월","화","수","목","금","토"};
    private double latitude, longitude;
    private int ImportanceValue, rate;
    private String loopWeek, doneDateValue;
    private List<TextView> day = new ArrayList<>();
    private static Item item1;
    private static List<Item> achievementList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        important = findViewById(R.id.important);
        itemName = findViewById(R.id.item_name);
        groupName = findViewById(R.id.group_name);
        calendarDate = findViewById(R.id.calendar_date);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        doneDate = findViewById(R.id.done_date);
        achievementRate = findViewById(R.id.achievement_rate);
        achievementRateArea = findViewById(R.id.achievement_rate_area);
        calendarDateArea = findViewById(R.id.calendar_date_area);
        startDateArea = findViewById(R.id.start_date_area);
        endDateArea = findViewById(R.id.end_date_area);
        doneDateArea = findViewById(R.id.done_date_area);
        roof = findViewById(R.id.loof);
        address = findViewById(R.id.address);
        groupColor = findViewById(R.id.group_color);
        editButton = findViewById(R.id.edit_button);
        day.add(0,(TextView)findViewById(R.id.sunday));
        day.add(1,(TextView)findViewById(R.id.monday));
        day.add(2,(TextView)findViewById(R.id.tuesday));
        day.add(3,(TextView)findViewById(R.id.wednesday));
        day.add(4,(TextView)findViewById(R.id.thursday));
        day.add(5,(TextView)findViewById(R.id.friday));
        day.add(6,(TextView)findViewById(R.id.saturday));

        /** get intent(item_id) from mainActivity **/ //수정 필요
        //intent = getIntent();
        //int itemId = intent.getIntExtra("itemId",-1);
        //dbHelper = DBAccess.getDbHelper();
        /*~ get itemId, calendar_date from mainActivity */


        /** acess DB **/
        dbHelper = DBAccess.getDbHelper();
        item1 = getItemByItemId(1);
        LocalDate localDate = getLocalDate(item1.getCalenderDate());
        achievementList = getloopItem(item1.getItemId(), localDate.with(previousOrSame(SUNDAY))+"", localDate.with(nextOrSame(SATURDAY)) + "");
        /*~ get DB data */


        /** set DB data **/
        ImportanceValue = item1.getImportant();
        loopWeek = item1.getLoopWeek();
        latitude = Float.parseFloat(item1.getLocationY());
        longitude = Float.parseFloat(item1.getLocationX());// 경도
        /*~ set DB data */


        /** set importance Image **/
        if(ImportanceValue==1) {
            important.setImageResource(R.drawable.important1);
        }else if(ImportanceValue==2){
            important.setImageResource(R.drawable.important2);
        }else if(ImportanceValue==3){
            important.setImageResource(R.drawable.important3);
        }else {
            important.setVisibility(View.GONE); }
        /*~ set importance Image */


        /** set loopWeek (ex : 안함, 매일, 월 수 금) **/
        if(loopWeek.equals("0000000")){
            startDateArea.setVisibility(View.GONE);
            endDateArea.setVisibility(View.GONE);
            achievementRateArea.setVisibility(View.GONE);
            roofDay += "안함";
        }else if(loopWeek.equals("1111111")){ //매일
            calendarDateArea.setVisibility(View.GONE);
            doneDateArea.setVisibility(View.GONE);
            roofDay += "매일";
        }else {
            calendarDateArea.setVisibility(View.GONE);
            doneDateArea.setVisibility(View.GONE);
            for (int i = 0; i < loopWeek.length(); i++) {
                if (loopWeek.charAt(i)-'0'==1) roofDay += days[i] + " "; } }
        /*~ set loopWeek */


        /** set achievementArea(rate, loop day) **/
        for(int i=0;i<achievementList.size();i++){
            int index = getLocalDate(achievementList.get(i).getCalenderDate()).getDayOfWeek().getValue();
            if(achievementList.get(i).getDoneDate() == null){
                day.get(index).setBackgroundResource(R.drawable.achievement_false);
            }else{
                day.get(index).setBackgroundResource(R.drawable.achievement_true);
                rate++;
            }
        }
        /*~ set achievementArea(rate, loop day) */


        /** set date **/
        doneDateValue = item1.getDoneDate();
        if(doneDateValue == null) doneDateValue = "미완료";
        itemName.setText(item1.getItemName());
        groupColor.getDrawable().setTint(Color.parseColor(item1.getGroupColor()));
        groupName.setText(item1.getGroupName());
        calendarDate.setText(item1.getCalenderDate());
        startDate.setText(item1.getStartDate());
        endDate.setText(item1.getEndDate());
        doneDate.setText(doneDateValue);
        roof.setText(roofDay);
        achievementRate.setText(Math.round((rate/achievementList.size())*100) +"%");
        /*~ set date */

    }

    /** convert a String to LocalDate  **/
    public LocalDate getLocalDate(String date){
        String[] dateArr = date.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(dateArr[0]),Integer.parseInt(dateArr[1]),Integer.parseInt(dateArr[2]));
        return localDate;
    }


    /** open option menu -> item modify, item delete) **/
    public void openOptionMenu(View view){
        PopupMenu menu = new PopupMenu(DetailActivity.this,editButton);
        menu.getMenuInflater().inflate(R.menu.menu_edit,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.btn_modify:
                        intent = new Intent(DetailActivity.this, DeletePopupActivity.class); // 수정 필요
                        intent.putExtra("itemId",item1.getItemId());
                        startActivity(intent);
                        return true;
                    case R.id.btn_delete:
                        intent = new Intent(DetailActivity.this, DeletePopupActivity.class); // 수정 필요
                        intent.putExtra("itemId",item1.getTodoId());
                        Log.d("todo", "onMenuItemClick: " + item1.getTodoId());
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
        menu.show();
    }
    /*~ open option menu -> item modify, item delete) */




    /** Back to MainActivity **/
    public void backToMain(View view) {
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        this.finish();
    }
    /*~ Back to MainActivity (Main View) */



    /** get Item by itemId from DB **/
    public Item getItemByItemId(int itemId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =
                "SELECT i.item_name, i.item_location_x, i.item_location_y, i.done_date, " +
                        "i.start_date, i.end_date, g.group_name, g.group_color, c.calendar_date, l.loop_week, i.item_id, i.to_do_id\n" +
                "FROM _ITEM i, _GROUP g, _LOOP_INFO l, _CALENDAR c\n" +
                "WHERE i.group_id = g.group_id \n" +
                "AND i.to_do_id = l.to_do_id \n" +
                "AND i.item_id = c.item_id \n" +
                "AND i.item_id = "+itemId+";";

        Cursor cursor = db.rawQuery(selectQuery,null);
        Item item = new Item();

        if(cursor.moveToFirst()){
            do {
                item.setItemName(cursor.getString(0));
                item.setLocationX(cursor.getString(1));
                item.setLocationY(cursor.getString(2));
                item.setDoneDate(cursor.getString(3));
                item.setStartDate(cursor.getString(4));
                item.setEndDate(cursor.getString(5));
                item.setGroupName(cursor.getString(6));
                item.setGroupColor(cursor.getString(7));
                item.setCalenderDate(cursor.getString(8));
                item.setLoopWeek(cursor.getString(9));
                item.setItemId(cursor.getInt(10));
                item.setTodoId(cursor.getInt(11));
            }while (cursor.moveToNext());
        }
        return item;
    }
    /*~ get Item by itemId from DB */



    /** get getloopItem by itemId from DB **/
    public List<Item> getloopItem(int itemId, String startOfWeek, String endOfWeek){
        List<Item> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery =
                "SELECT i.done_date , c.calendar_date " +
                "FROM _ITEM i, _CALENDAR c " +
                "WHERE i.item_id = c.item_id " +
                        "AND  c.calendar_date BETWEEN \"2020-02-12\" AND \"2020-02-17\"" + //수정
                        "AND i.to_do_id = (SELECT to_do_id " +
                                            "FROM _ITEM " +
                                            "WHERE item_id="+itemId+");";

        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                Item item = new Item();
                item.setDoneDate(cursor.getString(0));
                item.setCalenderDate(cursor.getString(1));
                list.add(item);
            }while (cursor.moveToNext());
        }
        return list;
    }
    /*~ get getloopItem by itemId from DB */


}