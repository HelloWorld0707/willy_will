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
    private String groupColorSettingKey;

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
        groupColorSettingKey = resources.getString(R.string.group_color_setting_key);

        /** Set Color Views **/
        final int SIZE = resources.getInteger(R.integer.number_of_group_colors);
        colorViews = new ImageView[SIZE];
        // Set the transparent option
        colorViews[0] = findViewById(R.id.color0);
        colorViews[0].setOnClickListener(new GroupColorClickListener(0));
        // Set the others
        final String groupColorViewName = resources.getString(R.string.group_color_view_name);
        int viewId, colorId, colorInt;
        for(int i = 1; i < SIZE; i++) {
            viewId = resources.getIdentifier(groupColorViewName + i, "id", packageName);
            colorViews[i] = findViewById(viewId);

            colorViews[i].setActivated(true);
            colorId = resources.getIdentifier(groupColorName + i, "color", packageName);
            colorInt = resources.getColor(colorId, null);
            colorViews[i].getDrawable().mutate().setTint(colorInt);

            colorViews[i].setOnClickListener(new GroupColorClickListener(i));
        }
        /* ~Set Color Views */

        /** Set selected color **/
        selectedColorIndex = getIntent().getIntExtra(groupColorSettingKey, 0);
        colorViews[selectedColorIndex].setSelected(true);
        /* ~Set selected color */
    }

    @Override
    protected boolean setResults(Intent intent) {
        intent.putExtra(resources.getString(R.string.group_color_setting_key), selectedColorIndex);
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
