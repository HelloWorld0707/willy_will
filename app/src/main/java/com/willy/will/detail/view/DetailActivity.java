package com.willy.will.detail.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import com.willy.will.R;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.detail.controller.DetailController;
import com.willy.will.detail.model.Item;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;



public class DetailActivity extends Activity {

    private ImageView important, groupColor;
    private ImageButton editButton;
    private TextView itemName, groupName, startDate, endDate, doneDate, roof,achievementRate, address;
    private RelativeLayout achievementRateArea,startDateArea, endDateArea, doneDateArea;
    private LinearLayout locationArea;
    private String roofDay = "";
    private String addressName, buildingName;
    private String[] days = {"일","월","화","수","목","금","토"};
    private double latitude, longitude;
    private int ImportanceValue, rate;
    private String loopWeek;
    private List<TextView> day = new ArrayList<>();
    private static Item todoItem;
    private static List<Item> achievementList;
    private DetailController detailCtrl = null;
    private ScrollView scrollView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailCtrl = new DetailController();

        important = findViewById(R.id.important);
        itemName = findViewById(R.id.item_name);
        groupName = findViewById(R.id.group_name);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        doneDate = findViewById(R.id.done_date);
        achievementRate = findViewById(R.id.achievement_rate);
        achievementRateArea = findViewById(R.id.achievement_rate_area);
        startDateArea = findViewById(R.id.start_date_area);
        endDateArea = findViewById(R.id.end_date_area);
        doneDateArea = findViewById(R.id.done_date_area);
        locationArea = findViewById(R.id.location_area);
        roof = findViewById(R.id.loof);
        address = findViewById(R.id.address);
        groupColor = findViewById(R.id.group_color);
        editButton = findViewById(R.id.edit_button);
        scrollView = findViewById(R.id.scroll_view);
        day.add(0,(TextView)findViewById(R.id.sunday));
        day.add(1,(TextView)findViewById(R.id.monday));
        day.add(2,(TextView)findViewById(R.id.tuesday));
        day.add(3,(TextView)findViewById(R.id.wednesday));
        day.add(4,(TextView)findViewById(R.id.thursday));
        day.add(5,(TextView)findViewById(R.id.friday));
        day.add(6,(TextView)findViewById(R.id.saturday));

        /** get intent(item_id) from mainActivity **/ //수정 필요
        Intent intent = getIntent();
        ToDoItem item =null;
        item = (ToDoItem) intent.getSerializableExtra(getResources().getString(R.string.item_id));
        //int itemId = intent.getIntExtra("itemId",-1);
        /*~ get itemId, calendar_date from mainActivity */



        todoItem = detailCtrl.getToDoItemByItemId(item.getItemId());
        LocalDate localDate = getLocalDate(todoItem.getCalenderDate());
        achievementList = detailCtrl.getloopItem(todoItem.getItemId(), localDate.with(previousOrSame(SUNDAY))+"", localDate.with(nextOrSame(SATURDAY)) + "");
        /** access DB **/


        /** set DB data **/
        ImportanceValue = todoItem.getImportant();
        loopWeek = todoItem.getLoopWeek();
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
            doneDateArea.setVisibility(View.GONE);
            roofDay += "매일";
        }else {
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


        /** set data **/
        itemName.setText(todoItem.getItemName());
        groupColor.getDrawable().setTint(Color.parseColor(todoItem.getGroupColor()));
        groupName.setText((todoItem.getGroupName()==null)?"그룹미정":todoItem.getGroupName());
        startDate.setText(todoItem.getStartDate());
        endDate.setText(todoItem.getEndDate());
        doneDate.setText((todoItem.getDoneDate()==null)?"미완료":todoItem.getDoneDate());
        roof.setText(roofDay);
        achievementRate.setText(Math.round((rate/achievementList.size())*100) +"%");

        /*~ set data */




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
                        //intent = new Intent(DetailActivity.this, DeletePopupActivity.class); // 수정 필요
                        //intent.putExtra("itemId",todoItem.getItemId());
                        //startActivity(intent);
                        return true;
                    case R.id.btn_delete:
                        intent = new Intent(DetailActivity.this, DeletePopupActivity.class); // 수정 필요
                        intent.putExtra("todoId",todoItem.getTodoId());
                        Log.d("todo", "onMenuItemClick: " + todoItem.getTodoId());
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



    /** Set results **/
    @Override
    public void finish() {
        Intent intent = new Intent();
        //intent.putExtra("itemId", itemId);
        setResult(RESULT_FIRST_USER, intent);
        super.finish();
    }

}