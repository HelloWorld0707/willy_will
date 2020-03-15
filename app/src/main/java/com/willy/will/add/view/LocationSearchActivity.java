package com.willy.will.add.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.willy.will.R;
import com.willy.will.adapter.RecyclerViewAdapter;
import com.willy.will.adapter.RecyclerViewSetter;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.Location;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.Task;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.setting.view.ManageTasksPopupActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


public class LocationSearchActivity extends Activity {

    private TextInputEditText textEditText = null;
    private InputMethodManager inputMethodManager = null;
    private RecyclerView recyclerView = null;
    private ArrayList<Location> locationArrayList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);

        getSearchAddress("");


        /** Set Views **/
        textEditText = findViewById(R.id.location_edit_text);
        if(textEditText.hasFocus()) {
            textEditText.clearFocus();
        }


        RecyclerViewSetter recyclerViewSetter = new RecyclerViewSetter(
                R.id.search_recycler_view, getWindow().getDecorView(),
                RecyclerViewItemType.LOCATION_SEARCH, locationArrayList,
                R.string.selection_id_location,false
        );
        recyclerView = recyclerViewSetter.setRecyclerView();
        RecyclerViewAdapter test = ((RecyclerViewAdapter) recyclerView.getAdapter());
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


    }

    public void searchLocation(View view){ // 검색 버튼 클릭시
        onSoftKeyboardDown(view); // 키보드 다
        String searchText = textEditText.getText().toString(); //에디트텍스트에 있는 텍스를 가져와서
        getSearchAddress(searchText); // 검색어 쿼리로 보낸다
        recyclerView.getAdapter().notifyDataSetChanged();


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
        locationArrayList = new ArrayList<Location>();

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
                        Location location = new Location();
                        location.setLocationId(dataObject.getString("id"));
                        location.setLongitude(Double.parseDouble(dataObject.getString("x")));
                        location.setLatitude(Double.parseDouble(dataObject.getString("y")));
                        location.setPlaceName(dataObject.getString("place_name"));
                        location.setAddressName(dataObject.getString("road_address_name"));
                        locationArrayList.add(location);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();



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
