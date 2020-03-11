package com.willy.will.common.view;

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
    TextView textView2;
    Button btnAdd, btnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add_group);

        bnt_color = (Button) findViewById(R.id.bnt_color);
        textView2 = (TextView) findViewById(R.id.textView3);

        Items = new ArrayList<String>();
        Items.add("운동");
        Items.add("공부");
        Items.add("레슨");

        Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, Items);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(Adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


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
        Intent intent = new Intent(this, Group_Color.class);
        startActivity(intent);
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





