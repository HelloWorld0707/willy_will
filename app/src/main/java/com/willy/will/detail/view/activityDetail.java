package com.willy.will.detail.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.willy.will.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class activityDetail extends Activity implements MapView.MapViewEventListener {

    ImageView important, groupColor;
    TextView itemName, groupName, startDate, endDate, doneDate, roof,achievementRate, address;
    RelativeLayout achievementRateArea, startDateArea, endDateArea, doneDateArea;
    String roofDay = "";
    String addressName, buildingName;
    String[] days = {"일","월","화","수","목","금","토"};
    MapPoint markerPoint;
    ScrollView scrollView;
    double latitude, longitude;

    MapView mapView;
    ViewGroup mapViewContainer;
    MapPOIItem marker;

    int tmpImportance;
    String tmpItemName, tmpGroupName, tmpRoof, tmpDate;
    String[] tmpRoofDay;

    /**
     * Last Modified: 2020-02-19
     * Last Modified By: Kim Mikyung
     * Created: -
     * Created By: -
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // findViewById
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
        roof = findViewById(R.id.loof);
        address = findViewById(R.id.address);
        mapViewContainer = findViewById(R.id.map_view);
        scrollView = findViewById(R.id.scroll_view);
        groupColor = findViewById(R.id.group_color);


        // db data
        //intent = getIntent();
        //int itemId = intent.getIntExtra("itemId",-1);
        tmpImportance = 1;
        tmpItemName = "취뽀 프로젝트";
        tmpGroupName = "willy";
        tmpDate = "2019-03-03";
        tmpRoof = "0111111";
        latitude = 37.53737528;
        longitude = 127.00557633;
        markerPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);


        // 1. set importance
        if(tmpImportance==1) {
            important.setImageResource(R.drawable.important1);
        }else if(tmpImportance==2){
            important.setImageResource(R.drawable.important2);
        }else if(tmpImportance==3){
            important.setImageResource(R.drawable.important3);
        }else {
            important.setVisibility(View.GONE);
        }


        //2. set itemName, groupname
        itemName.setText(tmpItemName);
        itemName.setText(tmpGroupName);


        //3. set groupColor
        groupColor.getDrawable().setTint(Color.parseColor("#FF0000"));
        //groupColor.getDrawable().setTint(Color.parseColor("@colors/colorGroup1"));


        //4 set date
        startDate.setText(tmpDate);
        endDate.setText(tmpDate);
        doneDate.setText(tmpDate);


        //5. set loofDate
        if(tmpRoof.equals("0000000")){ // 안함
            startDateArea.setVisibility(View.GONE);
            endDateArea.setVisibility(View.GONE);
            achievementRateArea.setVisibility(View.GONE);
            roofDay += "안함";
        }else if(tmpRoof.equals("1111111")){ //매일
            doneDateArea.setVisibility(View.GONE);
            roofDay += "매일";
        }else {
            doneDateArea.setVisibility(View.GONE);
            for (int i = 0; i < tmpRoof.length(); i++) {
                if (tmpRoof.charAt(i)-'0'==1) {
                    roofDay += days[i] + " ";
                }
            }
        }
        roof.setText(roofDay);


        //6. set week achievement
/*
        LocalDate today = LocalDate.now();
        String tmp = today.with(previousOrSame(SUNDAY))+"";
        today.with(nextOrSame(SATURDAY));
        // Toast.makeText(this,tmp,Toast.LENGTH_LONG).show();
        //roofDay2[0].setBackgroundResource(R.drawable.gravity1);
        achievementRate.setText(Math.round((rate/7.0)*100) +"%");

*/



        //6. get Address
        getAddress(longitude, latitude);



        //7. kakao map
        mapView = new MapView(this);
        mapView.setMapViewEventListener(this);
        mapViewContainer.addView(mapView);


    }




    /**
     * Last Modified: 2020-02-20
     * Last Modified By: -
     * Created: 2020-02-17
     * Created By: Kim Mikyung
     * Function: Back to BaseActivity (Main View)
     * Called when the user taps the back_button
     * @param view
     */

    public void backToMain(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        // ~Check focusing
        this.finish();
    }



    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-20
     * Created By: Kim Mikyung
     * Function: Convert longitude and latitude to address
     * @param longitude, latitude
     */
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
                        Log.d(TAG, "getPointFromNaver: 정상호출");
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




    /**
     * Last Modified:
     * Last Modified By: -
     * Created: 2020-02-20
     * Created By: Kim Mikyung
     * Function: kakaomap EventListener
     */
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


