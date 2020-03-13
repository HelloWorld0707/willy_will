package com.willy.will.add.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.textfield.TextInputEditText;
import com.willy.will.R;
import com.willy.will.common.model.Location;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LocationSearchActivity extends Activity {

    private ListView searchList;
    private String searchText = "";
    private TextInputEditText textEditText = null;
    final LocationBaseAdapter locationBaseAdapter = new LocationBaseAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);


        searchList = findViewById(R.id.search_list_view);
        textEditText = findViewById(R.id.location_edit_text);
        if(textEditText.hasFocus()) {
            textEditText.clearFocus();
        }

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location location = locationBaseAdapter.getItem(position);
                Intent intent = new Intent(LocationSearchActivity.this, AddItemActivity.class);
                intent.putExtra("place", location);
                startActivity(intent);
            }
        });

    }

    public void searchLocation(View view){
        searchText = textEditText.getText().toString();
        getSearchAddress(searchText);

    }

    public void backToAdd(View view){
        View focusedView = getCurrentFocus();
        if(focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        this.finish();
    }


    /**  get Address (rest api) **/
    private void getSearchAddress(final String searchText) {
        Log.d("getLocationId", searchText);

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
                        int height = 60;// findViewById(R.id.calendarListView).getMeasuredHeight();
                        int listSize = locationBaseAdapter.getCount();
                        params.height = height * listSize;
                        searchList.setLayoutParams(params);
                        searchList.requestLayout();
                    }
                });
            }
        }).start();
    }



}
