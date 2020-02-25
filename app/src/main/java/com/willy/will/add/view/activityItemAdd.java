package com.willy.will.add.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

import static androidx.constraintlayout.widget.Constraints.TAG;

public class activityItemAdd extends Activity implements MapView.MapViewEventListener {
    int y=0, m=0, d=0, h=0, mi=0;
    MapPoint markerPoint;
    MapView mapView;
    ViewGroup mapViewContainer;
    MapPOIItem marker;
    double latitude, longitude;
    String addressName, buildingName;
    TextView address;
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemadd);

        latitude = 37.53737528;
        longitude = 127.00557633;
        mapViewContainer = findViewById(R.id.map_view);
        markerPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        address = findViewById(R.id.address);

        /******* Group buuton -> moving ********************/
        Button bnt_group = findViewById(R.id.bnt_group);
        bnt_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityItemAdd.this, activityItemAdd_group.class);
                startActivity(intent);
            }
        });

        /******* Start_celander ********************/
        Button button = findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Start_Date_Show();
            }
        });

        /******* End_celander ********************/
        Button button1 = findViewById(R.id.btn_end);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                End_Date_Show();
            }
        });

        //6. get Address
        getAddress(longitude, latitude);

        //7. kakao map
        mapView = new MapView(this);
        mapView.setMapViewEventListener(this);
        mapViewContainer.addView(mapView);

    }

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


    void Start_Date_Show() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

            }
        },2019, 1, 11);

        datePickerDialog.setMessage("시작 날짜");
        datePickerDialog.show();
    }

    void End_Date_Show(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

            }
        },2019, 1, 11);

        datePickerDialog.setMessage("종료 날짜");
        datePickerDialog.show();
    }

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
