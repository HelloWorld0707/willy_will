package com.willy.will.common.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.willy.will.R;

public class GroupColorSettingActivity extends PopupActivity {

    private Resources resources;
    private String packageName;
    private String groupColorName;

    private ImageView[] colorViews;

    private int selectedColorIndex;

    public GroupColorSettingActivity() {
        super(R.layout.activity_group_color_setting);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = getResources();
        packageName = getPackageName();
        groupColorName = resources.getString(R.string.group_color_name);
        String groupColorViewName = resources.getString(R.string.group_color_view_name);

        final int SIZE = resources.getInteger(R.integer.number_of_group_colors);
        colorViews = new ImageView[SIZE];
        int viewId, colorId, colorInt;
        for(int i = 0; i < SIZE; i++) {
            viewId = resources.getIdentifier(groupColorViewName + (i + 1), "id", packageName);
            colorViews[i] = findViewById(viewId);

            colorViews[i].setActivated(true);
            colorId = resources.getIdentifier(groupColorName + (i + 1), "color", packageName);
            colorInt = resources.getColor(colorId, null);
            colorViews[i].getDrawable().mutate().setTint(colorInt);

            colorViews[i].setOnClickListener(new GroupColorClickListener(i));
        }

        selectedColorIndex = 0;
        colorViews[selectedColorIndex].setSelected(true);
    }

    @Override
    protected boolean setResults(Intent intent) {
        int colorId = resources.getIdentifier(groupColorName + (selectedColorIndex + 1), "color", packageName);
        int colorInt = resources.getColor(colorId, null);
        intent.putExtra(resources.getString(R.string.group_color_setting_key), colorInt);
        return true;
    }

    class GroupColorClickListener implements View.OnClickListener {
        private int index;

        public GroupColorClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            selectedColorIndex = index;

            for(ImageView colorV : colorViews) {
                colorV.setSelected(false);
            }
            colorViews[index].setSelected(true);
        }
    }

}
