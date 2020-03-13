package com.willy.will.detail.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
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

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;



public class DetailActivity extends Activity implements MapView.MapViewEventListener{

    private ImageView important, groupColor;
    private ImageButton editButton;
    private TextView itemName, groupName, startDate, endDate, doneDate, roof,achievementRate, address;
    private RelativeLayout startDateArea, endDateArea, doneDateArea;
    private LinearLayout achievementRateArea, locationArea;
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
    private MapPoint markerPoint;
    private ScrollView scrollView;
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private MapPOIItem marker;
    private Resources resources;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailCtrl = new DetailController();
        resources = getResources();
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
        mapViewContainer = findViewById(R.id.map_view);
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


        /** access DB **/
        todoItem = detailCtrl.getToDoItemByItemId(item.getItemId());
        LocalDate localDate = getLocalDate(todoItem.getCalenderDate());
        achievementList = detailCtrl.getloopItem(todoItem.getItemId(), localDate.with(previousOrSame(SUNDAY))+"", localDate.with(nextOrSame(SATURDAY)) + "");
        /*~ access DB ~*/


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
        }else if(ImportanceValue==4){
            important.setVisibility(View.GONE); }
        /*~ set importance Image */


        /** set loopWeek (ex : 안함, 매일, 월 수 금) **/
        if(loopWeek ==null){
            achievementRateArea.setVisibility(View.GONE);
            roofDay += "반복 안함";
        }else {
            if(loopWeek.equals("1111111")){ //매일
                doneDateArea.setVisibility(View.GONE);
                roofDay += "매일";
            }else {
                doneDateArea.setVisibility(View.GONE);
                for (int i = 0; i < loopWeek.length(); i++) {
                    if (loopWeek.charAt(i)-'0'==1) roofDay += days[i] + " "; }
            }
            for(int i=0;i<achievementList.size();i++){
                int index = getLocalDate(achievementList.get(i).getCalenderDate()).getDayOfWeek().getValue();
                String doneDateValue = achievementList.get(i).getDoneDate();
                if(doneDateValue==null || doneDateValue==""){
                    day.get(index).setBackgroundResource(R.drawable.achievement_false);
                }else{
                    day.get(index).setBackgroundResource(R.drawable.achievement_true);
                    rate++;
                }
            }
            achievementRate.setText(Math.round((rate/achievementList.size())*100) +"%");
        }
        /*~ set achievementArea(rate, loop day) */



        /** set data **/
        itemName.setText(todoItem.getItemName());
        if(todoItem.getGroupId() == 0){
            groupColor.setActivated(false);
        }else{
            groupColor.setActivated(true);
            groupColor.getDrawable().mutate().setTint(Color.parseColor(todoItem.getGroupColor()));
        }
        groupName.setText((todoItem.getGroupName()==null)?resources.getString(R.string.no_group):todoItem.getGroupName());
        startDate.setText(todoItem.getStartDate());
        endDate.setText(todoItem.getEndDate());
        doneDate.setText((todoItem.getDoneDate()==null)?resources.getString(R.string.not_done):todoItem.getDoneDate());
        roof.setText(roofDay);
        if(todoItem.getLongitude()==null||todoItem.getLatitude()==null){
            locationArea.setVisibility(View.GONE);
        }else{
            latitude = 37.53737528;//Float.parseFloat(todoItem.getLocationY());
            longitude = 127.00557633;//Float.parseFloat(todoItem.getLocationX());// 경도
            markerPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

            getAddress(longitude, latitude);
            mapView = new MapView(this);
            mapView.setMapViewEventListener(this);
            mapViewContainer.addView(mapView);
        }
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
                        //modify
                        return true;
                    case R.id.btn_delete:
                        intent = new Intent(DetailActivity.this, DeletePopupActivity.class); // 수정 필요
                        intent.putExtra("todoId",todoItem.getTodoId());
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
        setResult(RESULT_FIRST_USER, intent);
        super.finish();
    }



    /**  get Address (rest api) **/
    private void getAddress(final double longitude, final double latitude) {

        new Thread(new Runnable() {
            String json = null;

            @Override
            public void run() {
                try {
                    String apiURL = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="+ longitude + "&y=" + latitude +"&input_coord=WGS84"; // json
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Authorization", "KakaoAK b5ef8f50c799f2e913df5481ce88bd18"); //header
                    int responseCode = con.getResponseCode();
                    BufferedReader br = null;

                    if (responseCode == 200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    }

                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();

                    json = response.toString();
                    if (json == null) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray resultsArray = jsonObject.getJSONArray("documents");
                    JSONObject jsonObject1 = resultsArray.getJSONObject(0);
                    JSONObject dataObject = (JSONObject) jsonObject1.get("road_address");

                    addressName = dataObject.getString("address_name");
                    buildingName = dataObject.getString("building_name");

                    address.setText(addressName);
                    marker = new MapPOIItem();
                    marker.setItemName(buildingName);
                    marker.setMapPoint(markerPoint);
                    marker.setShowDisclosureButtonOnCalloutBalloon(false);
                    mapView.addPOIItem(marker);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }




    /** kakaomap EventListener **/
    @Override
    public void onMapViewInitialized(MapView mapView) {
        mapView.setMapCenterPoint(markerPoint, true);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        scrollView.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
        scrollView.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        scrollView.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
        scrollView.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        scrollView.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        scrollView.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        scrollView.requestDisallowInterceptTouchEvent(true);

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        scrollView.requestDisallowInterceptTouchEvent(true);

    }
}
