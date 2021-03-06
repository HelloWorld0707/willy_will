package com.willy.will.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;
import com.willy.will.add.view.AddItemActivity;
import com.willy.will.common.controller.App;
import com.willy.will.common.controller.ToDoItemComparator;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.Location;
import com.willy.will.common.model.RecyclerViewItemType;
import com.willy.will.common.model.Task;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.common.view.DeleteGroupPopupActivity;
import com.willy.will.common.view.GroupManagementActivity;
import com.willy.will.database.ToDoItemDBController;
import com.willy.will.detail.view.DetailActivity;

import java.util.Collections;

public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    private static ToDoItemComparator toDoItemComparator = new ToDoItemComparator();
    private static BackgroundColorSpan inactiveColorSpan = new BackgroundColorSpan(App.getContext().getColor(R.color.colorInactive));
    private static int NO_GROUP_ID = App.getContext().getResources().getInteger(R.integer.no_group_id);

    // View of Item
    private TextView textOnlyView;

    private ImageView imgRank;
    private TextView tvName;
    private ImageView imgRoutine;
    private TextView tvTime;
    private CheckBox cbDone;

    private ImageView groupColorView;
    private TextView groupName;
    private ImageButton removeButton;
    private RadioButton radioButton;

    private ImageView groupColorCircleView;
    private TextView taskNameView;
    private TextView dDayOrAchivementView;
    private CheckBox taskCheckBox;

    private TextView placeName;
    private TextView roadAddressName;
    private TextView addressName;
    // ~View of Item

    // Initialization of THE item (Called for each item)
    public <T> RecyclerViewHolder(final RecyclerViewAdapter adapter, RecyclerViewItemType type, View view) {
        super(view);

        // To-do item (of Main or Search)
        if(type == RecyclerViewItemType.TO_DO_MAIN ||
           type == RecyclerViewItemType.TO_DO_SEARCH) {
            tvTime = view.findViewById(R.id.tv_time);
            imgRank = view.findViewById(R.id.img_rank);
            tvName = view.findViewById(R.id.tv_name);
            imgRoutine = view.findViewById(R.id.img_routine);
            cbDone = view.findViewById(R.id.cb_done);

            // To-do item of Main
            if(type == RecyclerViewItemType.TO_DO_MAIN) {
                cbDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (cbDone.isPressed()) {
                            int position = Math.toIntExact(getItemId());
                            ToDoItem toDoItem = (ToDoItem) adapter.getData(position);
                            toDoItem.setDone(b);
                            setActivation(toDoItem.getItemId(), b, toDoItem.getColor(), toDoItem.getLoop());
                            Collections.sort(adapter.getList(), toDoItemComparator);
                            /** WARNING: The OREDER is important! **/
                            cbDone.setPressed(false);
                            adapter.notifyDataSetChanged();
                            /* ~WARNING: The OREDER is important! */
                        }
                    }
                });
            }
            // To-do item of Search
            else if(type == RecyclerViewItemType.TO_DO_SEARCH) {
                cbDone.setEnabled(false);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = adapter.getActivity();
                    Resources resources = activity.getResources();

                    Intent intent = new Intent(activity, DetailActivity.class);

                    String extraName = resources.getString(R.string.request_code);
                    int code = resources.getInteger(R.integer.detail_request_code);
                    intent.putExtra(extraName, code);

                    int p = Math.toIntExact(getItemId());
                    ToDoItem toDoItem = (ToDoItem) adapter.getData(p);
                    intent.putExtra(resources.getString(R.string.item_id), toDoItem);

                    activity.startActivityForResult(intent, code);
                }
            });
        }
        // Text-only (Group, Done, or Loop)
        else if(type == RecyclerViewItemType.GROUP_SEARCH ||
                type == RecyclerViewItemType.DONE_SEARCH ||
                type == RecyclerViewItemType.LOOP_SEARCH) {
            textOnlyView = view.findViewById(R.id.text_recycler_item);
        }
        // Group Management
        else if(type == RecyclerViewItemType.GROUP) {
            groupColorView = view.findViewById(R.id.group_color);
            groupName = view.findViewById(R.id.group_name);
            removeButton = view.findViewById(R.id.remove_button);
            radioButton = view.findViewById(R.id.radio_button);

            // For mode switching
            boolean removing = GroupManagementActivity.isRemoving();
            if(removing) {
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = adapter.getActivity();

                        int position = Math.toIntExact(getItemId());
                        Group selectedGroup = (Group) adapter.getData(position);
                        Intent intent = new Intent(activity, DeleteGroupPopupActivity.class);
                        intent.putExtra(
                                activity.getResources().getString(R.string.group_removal_key),
                                selectedGroup
                        );

                        int code = activity.getResources().getInteger(R.integer.remove_group_code);
                        activity.startActivityForResult(intent, code);
                    }
                });
            }
            else {
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = Math.toIntExact(getItemId());
                        adapter.setSelectedPosition(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
        // Task
        else if(type == RecyclerViewItemType.TASK) {
            groupColorCircleView = view.findViewById(R.id.group_color);
            taskNameView = view.findViewById(R.id.task_name);
            dDayOrAchivementView = view.findViewById(R.id.d_day_or_achievement);
            taskCheckBox = view.findViewById(R.id.check_box);
            taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = Math.toIntExact(getItemId());
                    Task task = (Task) adapter.getData(position);
                    task.setChecked(isChecked);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = adapter.getActivity();
                    Resources resources = activity.getResources();

                    String extraName = resources.getString(R.string.request_code);
                    int code = resources.getInteger(R.integer.detail_request_code);

                    int p = Math.toIntExact(getItemId());
                    Task selectedTask = (Task) adapter.getData(p);
                    ToDoItem toDoItem = new ToDoItem();
                    toDoItem.setItemId(selectedTask.getItemId());

                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra(extraName, code);
                    intent.putExtra(resources.getString(R.string.item_id), toDoItem);

                    activity.startActivityForResult(intent, code);
                }
            });
        }
        // Location search
        else if(type == RecyclerViewItemType.LOCATION_SEARCH) {
            placeName = view.findViewById(R.id.place_name);
            addressName = view.findViewById(R.id.address_name);
            roadAddressName = view.findViewById(R.id.road_address_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = adapter.getActivity();

                    Intent intent = new Intent(activity, AddItemActivity.class);

                    int p = Math.toIntExact(getItemId());
                    Location selectedLocation = (Location) adapter.getData(p);
                    intent.putExtra(
                            activity.getResources().getString(R.string.location_search_key),
                            selectedLocation
                    );

                    activity.setResult(Activity.RESULT_FIRST_USER, intent);
                    activity.finish();
                }
            });
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
    public <T> void bind(RecyclerViewItemType type, T data, boolean isSelected) {
        // To-do
        if(type == RecyclerViewItemType.TO_DO_MAIN ||
           type == RecyclerViewItemType.TO_DO_SEARCH) {
            ToDoItem mitem = (ToDoItem) data;
            tvTime.setText(mitem.getEndDate());
            tvName.setText(mitem.getName());
            cbDone.setChecked(mitem.getDone());
            String color = mitem.getColor();
            int id = mitem.getItemId();
            int loop = mitem.getLoop();
            if(loop == 1) {
                imgRoutine.setVisibility(View.VISIBLE);
                imgRoutine.setImageDrawable(ResourcesCompat.getDrawable(
                        App.getContext().getResources(),
                        R.drawable.ic_loop_24px,
                        null
                ));
            }
            else {
                imgRoutine.setVisibility(View.GONE);
            }

            int rank = mitem.getRank();
            if(rank == 1) {
                imgRank.setImageDrawable(ResourcesCompat.getDrawable(App.getContext().getResources(),
                        R.drawable.important1, null));
            }
            else if(rank == 2) {
                imgRank.setImageDrawable(ResourcesCompat.getDrawable(App.getContext().getResources(),
                        R.drawable.important2, null));
            }
            else if(rank == 3) {
                imgRank.setImageDrawable(ResourcesCompat.getDrawable(App.getContext().getResources(),
                        R.drawable.important3, null));
            }
            else {
                imgRank.setImageDrawable(null);
            }
            setActivation(id, cbDone.isChecked(), color, loop);
        }
        // Group
        else if(type == RecyclerViewItemType.GROUP_SEARCH) {
            Group group = (Group) data;
            // Hide a ghost item for changing selection mode of multiple selection
            if(group.getGroupName().equals("")) {
                if(textOnlyView.getVisibility() != View.GONE) {
                    textOnlyView.setVisibility(View.GONE);
                }
            }
            else {
                if(textOnlyView.getVisibility() == View.GONE) {
                    textOnlyView.setVisibility(View.VISIBLE);
                }
                textOnlyView.setText(group.getGroupName());
            }
        }
        // Done or Loop
        else if(type == RecyclerViewItemType.DONE_SEARCH ||
                type == RecyclerViewItemType.LOOP_SEARCH) {
            String text = (String) data;
            textOnlyView.setText(text);
        }
        // Group Management
        else if(type == RecyclerViewItemType.GROUP) {
            Group group = (Group) data;

            Resources resources = App.getContext().getResources();

            int colorId = Color.parseColor(group.getGroupColor());
            if((group.getGroupId() == NO_GROUP_ID) || (colorId == Color.TRANSPARENT)) {
                groupColorView.setActivated(false);
                groupColorView.getDrawable().mutate().setTint(resources.getColor(R.color.dark_gray, null));
            }
            else {
                groupColorView.setActivated(true);
                groupColorView.getDrawable().mutate().setTint(colorId);
            }

            groupName.setText(group.getGroupName());
            // For marquee
            groupName.setSelected(true);

            boolean removing = GroupManagementActivity.isRemoving();
            if(removing) {
                if(group.getGroupId() == NO_GROUP_ID) {
                    removeButton.setVisibility(View.GONE);
                }
                else {
                    removeButton.setVisibility(View.VISIBLE);
                }
                radioButton.setVisibility(View.INVISIBLE);
            }
            else {
                removeButton.setVisibility(View.GONE);
                radioButton.setVisibility(View.VISIBLE);
                radioButton.setChecked(isSelected);
            }
        }
        // Task
        else if(type == RecyclerViewItemType.TASK) {
            Task task = (Task) data;
            int colorId = Color.parseColor(task.getGroup().getGroupColor());
            if((task.getGroup().getGroupId() == NO_GROUP_ID) || (colorId == Color.TRANSPARENT)) {
                groupColorCircleView.setActivated(false);
                groupColorCircleView.getDrawable().mutate().setTint(App.getContext().getResources().getColor(R.color.dark_gray, null));
            }
            else {
                groupColorCircleView.setActivated(true);
                groupColorCircleView.getDrawable().mutate().setTint(colorId);
            }
            taskNameView.setText(task.getName());
            dDayOrAchivementView.setText(task.getdDayOrAchievement());
            taskCheckBox.setChecked(task.isChecked());
        }
        // Location search
        else if(type == RecyclerViewItemType.LOCATION_SEARCH) {
            Location location = (Location) data;
            placeName.setText(location.getPlaceName());
            addressName.setText(location.getAddressName());
            roadAddressName.setText(location.getRoadAddressName());
        }
        // ERROR: Wrong type
        else {
            Log.e("RecyclerViewHolder", "Binding: Wrong type");
        }

        itemView.setSelected(isSelected);
        if(isSelected) {
            if (type == RecyclerViewItemType.GROUP) {
                groupColorView.setSelected(false);
            }
            else if(type == RecyclerViewItemType.TASK) {
                groupColorCircleView.setSelected(false);
            }
        }
    }

    // Activation of to-do item
    private void setActivation(int id, boolean activated, String groupColor, int loop) {
        Spannable span = (Spannable) tvName.getText();
        Resources resources = App.getContext().getResources();
        ToDoItemDBController dbController = new ToDoItemDBController(resources);


        if (activated) {
            span.setSpan(inactiveColorSpan
                    , 0, tvName.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            if (imgRoutine.getDrawable() != null && loop != 0) {
                imgRoutine.getDrawable().mutate().setTint(resources.getColor(R.color.colorInactive,null));
            }
            if (imgRank.getDrawable() != null) {
                imgRank.getDrawable().mutate().setTint(resources.getColor(R.color.colorInactive,null));
            }

            }
            else {
            span.setSpan(new BackgroundColorSpan(Color.parseColor(groupColor))
                    , 0, tvName.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvName.setPaintFlags(0);
            tvTime.setPaintFlags(0);
            if (imgRoutine.getDrawable() != null && loop != 0) {
                imgRoutine.getDrawable().mutate().setTint(resources.getColor(R.color.colorPrimary, null));
            }
            if (imgRank.getDrawable() != null) {
                imgRank.getDrawable().mutate().setTint(resources.getColor(R.color.colorPrimary, null));
            }
        }
            dbController.updateDB(id, activated);

    }
}
