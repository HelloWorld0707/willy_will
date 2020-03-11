package com.willy.will.common.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.willy.will.R;

import java.util.ArrayList;

public class GroupManagementActivity extends AppCompatActivity {
    Button bnt_color;
    ArrayList<String> Items;
    ArrayAdapter<String> Adapter;
    ListView listView;
    TextView txt_color;
    Button btnAdd, btnDel;
    private static final int REQUEST_CODE = 777;

    private String result = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add_group);

        bnt_color = (Button) findViewById(R.id.bnt_color);
        Items = new ArrayList<String>();
        Items.add("운동");
        Items.add("공부");
        Items.add("레슨");
        Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, Items);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(Adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        txt_color = (TextView) findViewById(R.id.txt_color);

        //Group_Text = findViewById(R.id.Group_Text);
        //btnAdd = (Button) findViewById(R.id.btnAdd);
        //btnDel = (Button) findViewById(R.id.btnDel);

        /**btnAdd.setOnClickListener(listener);
         //btnDel.setOnClickListener(listener);**/
    }

    public void toadd(View view) {
        // Check focusing
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        // ~Check focusing
        this.finish();
    }

    public void bringUpGroupColor(View view) {
        Intent intent = new Intent(getApplicationContext(), Group_Color.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            result = data.getStringExtra("result");
            txt_color.setText(result);

            // 색선택되면 화면에 뜨게해야함..
            }
        }

    }

    /**private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnAdd:
                    String text = editText.getText().toString();
                    if(text.length() != 0){
                        Items.add(text);
                        editText.setText("");
                        Adapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.btnDel:
                    int pos;
                    pos = listView.getCheckedItemPosition();
                    if (pos != ListView.INVALID_POSITION){
                        Items.remove(pos);
                        listView.clearChoices();
                        Adapter.notifyDataSetChanged();
                    }
                    break;
            }

        }
    };**/





