package com.willy.will.adapter;

import android.content.res.Resources;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.common.controller.App;
import com.willy.will.common.model.Group;
import com.willy.will.main.model.ToDoItem;
import com.willy.will.search.model.Distance;

public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    private static BackgroundColorSpan inactiveColorSpan = new BackgroundColorSpan(App.getContext().getColor(R.color.colorInactive));
    private BackgroundColorSpan activeColorSpan = null;

    private RecyclerViewAdapter rcyclrVAdapter = null;
    // Common (also written in RecyclerViewSetter)
    private Resources resources = null;
    private int TO_DO_CODE = 0;
    private int GROUP_CODE = 0;
    private int DONE_CODE = 0;
    private int DISTANCE_CODE = 0;
    // ~Common (also written in RecyclerViewSetter)

    // View of Item
    private TextView textOnlyView;

    private ImageView imgRank;
    private TextView tvName;
    private ImageView imgRoutine;
    private TextView tvTime;
    private CheckBox cbDone;
    // ~View of Item

    // Initialization of THE item (Called for each item)
    public <T> RecyclerViewHolder(RecyclerViewAdapter adapter, int type, View view) {
        super(view);
        rcyclrVAdapter = adapter;
        resources = view.getContext().getResources();

        /** Set codes by type (also written in RecyclerViewSetter:RecyclerViewSetter) **/
        TO_DO_CODE = resources.getInteger(R.integer.to_do_recycler_item_type);
        GROUP_CODE = resources.getInteger(R.integer.group_search_setting_recycler_item_type);
        DONE_CODE = resources.getInteger(R.integer.done_search_setting_recycler_item_type);
        DISTANCE_CODE = resources.getInteger(R.integer.distance_search_setting_recycler_item_type);
        /* ~Set codes by type (also written in RecyclerViewSetter:RecyclerViewSetter) */

        // To-do
        if(type == TO_DO_CODE) {
            tvTime = view.findViewById(R.id.tv_time);
            imgRank = view.findViewById(R.id.img_rank);
            tvName = view.findViewById(R.id.tv_name);
            imgRoutine = view.findViewById(R.id.img_routine);
            cbDone = view.findViewById(R.id.cb_done);

            activeColorSpan = new BackgroundColorSpan(resources.getColor(R.color.colorGroup7));
            cbDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(cbDone.isPressed()) {
                        setActivation(b);
                        ToDoItem toDoItem = (ToDoItem) rcyclrVAdapter.getData(getItemId());
                        toDoItem.setDone(b);
                    }
                }
            });
        }
        // Text-only (Group, Done, Distance)
        else if(type == GROUP_CODE || type == DONE_CODE || type == DISTANCE_CODE) {
            textOnlyView = view.findViewById(R.id.text_recycler_item);
        }
        // ERROR: Wrong type
        else {
            Log.e("RecyclerViewHolder", "Initializing: Wrong type");
        }
    }

    // Get Item Details to build Tracker (Called for each item)
    public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
        ItemDetailsLookup.ItemDetails<Long> details = new ItemDetailsLookup.ItemDetails<Long>() {
            @Override
            public int getPosition() {
                return getAdapterPosition();
            }

            @Nullable
            @Override
            public Long getSelectionKey() {
                return getItemId();
            }
        };

        return details;
    }

    // Bind data to THE item (Called for each item)
    public <T> void bind(int type, T data, boolean isSelected) {
        // To-do
        if(type == TO_DO_CODE) {
            ToDoItem mitem = (ToDoItem) data;
            tvTime.setText(mitem.getTime());
            tvName.setText(mitem.getName());
            cbDone.setChecked(mitem.getDone());
            setActivation(cbDone.isChecked());
            //imgRank.setImageDrawable(mitem.getRank());
            //imgRoutine.setImageDrawable(mitem.getRoutine());

        }
        // Group
        else if(type == GROUP_CODE) {
            Group group = (Group) data;
            textOnlyView.setText(group.getGroupName());
        }
        // Done
        else if(type == DONE_CODE) {
            String text = (String) data;
            textOnlyView.setText(text);
        }
        // Distance
        else if(type == DISTANCE_CODE) {
            Distance distance = (Distance) data;
            textOnlyView.setText(distance.getText());
        }
        // ERROR: Wrong type
        else {
            Log.e("RecyclerViewHolder", "Binding: Wrong type");
        }

        itemView.setSelected(isSelected);
    }

    // Activation of to-do item
    private void setActivation(boolean activated) {
        Spannable span = (Spannable) tvName.getText();

        if(activated) {
            span.setSpan(inactiveColorSpan
                    , 0, tvName.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            imgRoutine.setColorFilter(resources.getColor(R.color.colorInactive));
            imgRank.setColorFilter(resources.getColor(R.color.colorInactive));
        }
        else {
            span.setSpan(activeColorSpan
                    , 0, tvName.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE); // need to fix (color)
            tvName.setPaintFlags(0);
            tvTime.setPaintFlags(0);
        }
    }

}
