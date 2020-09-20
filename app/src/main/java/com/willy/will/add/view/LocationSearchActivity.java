package com.willy.will.add.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.controller.AdMobController;
import com.willy.will.common.model.Location;
import com.willy.will.common.model.RecyclerViewItemType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LocationSearchActivity extends Activity {

    private TextInputEditText textEditText = null;
    private InputMethodManager inputMethodManager = null;
    private RecyclerView recyclerView = null;
    private ArrayList<Location> locationArrayList = null;
    private AdMobController adMobController = new AdMobController(this);
    private Location location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        textEditText = findViewById(R.id.location_edit_text);

        getSearchAddress("");

        if(textEditText.hasFocus()) {
            textEditText.clearFocus();
        }

        locationArrayList = new ArrayList<Location>();
        recyclerView = new RecyclerViewSetter(
                this, R.id.search_recycler_view,
                RecyclerViewItemType.LOCATION_SEARCH, R.layout.item_location,
                locationArrayList
        ).setRecyclerView();

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        /** loading Ad*/
        adMobController.callingAdmob();

    }

    public void searchLocation(View view){
        onSoftKeyboardDown(view);
        String searchText = textEditText.getText().toString();
        getSearchAddress(searchText);
    }


    public void onSoftKeyboardDown(View view) {
        inputMethodManager.hideSoftInputFromWindow(textEditText.getWindowToken(), 0);
    }


    public void backToAdd(View view){
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            onSoftKeyboardDown(view);
        }
        this.finish();
    }


    /**  get Address (rest api) **/
    private void getSearchAddress(final String searchText) {
        if(locationArrayList == null){
            locationArrayList = new ArrayList<>();
        }else if(!locationArrayList.isEmpty()){
            locationArrayList.clear();
        }

        location = new Location();
        location.setPlaceName(searchText);
        location.setUserPlaceName(searchText);
        locationArrayList.add(location);

        new Thread(new Runnable() {
            String json = null;

            @Override
            public void run() {
                try {
                    String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+ searchText; // json
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Authorization", "KakaoAK b5ef8f50c799f2e913df5481ce88bd18"); //header
                    int responseCode = con.getResponseCode();
                    BufferedReader br = null;

                    if (responseCode == 200) {
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
                    JSONObject metaObject = jsonObject.getJSONObject("meta");
                    JSONArray documentsObject = jsonObject.getJSONArray("documents");
                    int pageCount = metaObject.getInt("pageable_count");
                    int listSize = (pageCount<15)?pageCount:15;

                    for(int i=0;i<listSize;i++){
                        JSONObject dataObject = documentsObject.getJSONObject(i);
                        location = new Location();
                        location.setLocationId(dataObject.getString("id"));
                        location.setLongitude(Double.parseDouble(dataObject.getString("x")));
                        location.setLatitude(Double.parseDouble(dataObject.getString("y")));
                        location.setPlaceName(dataObject.getString("place_name"));
                        location.setAddressName(dataObject.getString("address_name"));
                        location.setRoadAddressName(dataObject.getString("road_address_name"));
                        locationArrayList.add(location);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
    /*~  get Address (rest api) ~*/

}
