package com.willy.will.add.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import com.google.android.material.textfield.TextInputEditText;
import com.willy.will.R;
import com.willy.will.common.model.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LocationSearchActivity extends Activity {

    private ScrollView scrollView;
    private ListView searchList;
    private String searchText = "";
    private TextInputEditText textEditText = null;
    private InputMethodManager inputMethodManager = null;
    LocationBaseAdapter locationBaseAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        /** Set Views **/
        scrollView = findViewById(R.id.search_result_layout);
        searchList = findViewById(R.id.search_list_view);
        textEditText = findViewById(R.id.location_edit_text);
        if(textEditText.hasFocus()) {
            textEditText.clearFocus();
        }



        /** click listView **/
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                ArrayList<Location> locationArrayList = new ArrayList<>();
                Location location = locationBaseAdapter.getItem(position);
                locationArrayList.add(location);
                intent.putParcelableArrayListExtra(getResources().getString(R.string.location_search_key), locationArrayList);
                setResult(RESULT_FIRST_USER,intent);
                finish();
            }
        });



        /** hide keyboard when touch scrollview **/
        searchList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE)
                    onSoftKeyboardDown(view);
                return false;
            }
        });


    }

    public void searchLocation(View view){
        onSoftKeyboardDown(view);
        searchText = textEditText.getText().toString();
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

        new Thread(new Runnable() {
            String json = null;

            @Override
            public void run() {
                try {
                    locationBaseAdapter = new LocationBaseAdapter();
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
                    int resultCnt = metaObject.getInt("pageable_count")<15?metaObject.getInt("pageable_count"):15;

                    JSONArray documentsObject = jsonObject.getJSONArray("documents");



                    for(int i=0;i<resultCnt;i++){
                        JSONObject dataObject = documentsObject.getJSONObject(i);

                        Location location = new Location();
                        location.setLocationId(dataObject.getString("id"));
                        location.setLongitude(Double.parseDouble(dataObject.getString("x")));
                        location.setLatitude(Double.parseDouble(dataObject.getString("y")));
                        location.setPlaceName(dataObject.getString("place_name"));
                        location.setAddressName(dataObject.getString("road_address_name"));
                        locationBaseAdapter.addItem(location);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {

                        searchList.setAdapter(locationBaseAdapter);
                        ViewGroup.LayoutParams params = searchList.getLayoutParams();
                        int height = 70;
                        int listSize = locationBaseAdapter.getCount();
                        params.height = height * listSize;
                        searchList.setLayoutParams(params);
                        searchList.requestLayout();
                    }
                });
            }
        }).start();
    }
    /*~  get Address (rest api) ~*/



}
