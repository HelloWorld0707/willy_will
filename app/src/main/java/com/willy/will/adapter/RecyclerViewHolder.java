package com.willy.will.adapter;

import android.content.res.Resources;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.common.model.Group;
import com.willy.will.main.model.mainListItem;
import com.willy.will.search.model.Distance;

public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    // Common (also written in RecyclerViewSetter)
    Resources resources = null;
    private int TO_DO_CODE = 0;
    private int GROUP_CODE = 0;
    private int DONE_CODE = 0;
    private int DISTANCE_CODE = 0;
    // ~Common (also written in RecyclerViewSetter)

    // View of Item
    private TextView textOnlyView;

    ImageView imgRank;
    TextView tvName;
    ImageView imgRoutine;
    TextView tvTime;
    CheckBox cbDone;
    Spannable span;
    // ~View of Item

    /**
     * Last Modified: 2020-02-17
     * Last Modified By: Shin Minyong
     * Created: 2020-02-11
     * Created By: Shin Minyong
     * Function: Initialization (including Item View)
     * @param type
     * @param view
     * @param <T>
     */
    public <T> RecyclerViewHolder(int type, View view) {
        super(view);
        resources = view.getContext().getResources();

        // Set codes by type (also written in RecyclerViewSetter:RecyclerViewSetter)
        TO_DO_CODE = resources.getInteger(R.integer.to_do_recycler_item_type);
        GROUP_CODE = resources.getInteger(R.integer.group_search_setting_recycler_item_type);
        DONE_CODE = resources.getInteger(R.integer.done_search_setting_recycler_item_type);
        DISTANCE_CODE = resources.getInteger(R.integer.distance_search_setting_recycler_item_type);
        // ~Set codes by type (also written in RecyclerViewSetter:RecyclerViewSetter)

        // To-do
        if(type == TO_DO_CODE) {
            tvTime = view.findViewById(R.id.tv_time);
            imgRank = view.findViewById(R.id.img_rank);
            tvName = view.findViewById(R.id.tv_name);
            imgRoutine = view.findViewById(R.id.img_routine);
            cbDone = view.findViewById(R.id.cb_done);

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

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-12
     * Created By: Shin Minyong
     * Function: Get Item Details (To build Tracker)
     * @return details
     */
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

    /**
     * Last Modified: 2020-02-17
     * Last Modified By: Shin Minyong
     * Created: 2020-02-12
     * Created By: Shin Minyong
     * Function: Bind data to the item (com.willy.will.adapter)
     * @param type
     * @param data
     * @param isSelected
     * @param <T>
     */
    public <T> void bind(int type, T data, boolean isSelected) {
        // To-do
        if(type == TO_DO_CODE) {
            mainListItem mitem = (mainListItem) data;
            tvTime.setText(mitem.getTime());
            imgRank.setImageDrawable(mitem.getRank());
            tvName.setText(mitem.getName());
            imgRoutine.setImageDrawable(mitem.getRoutine());
            cbDone.setActivated(mitem.getDone());
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


}
