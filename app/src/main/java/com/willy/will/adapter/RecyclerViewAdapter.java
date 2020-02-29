package com.willy.will.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.willy.will.R;

import java.util.ArrayList;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private SelectionTracker<Long> trckr = null;

    private RecyclerViewSetter setter = null;
    private ArrayList<T> dset = null;
    private int t = 0;

    /**
     * Last Modified: -
     * Last Modified By: -
     * Created: 2020-02-09
     * Created By: Shin Minyong
     * Function: Provide a suitable constructor (depends on the kind of dataset)
     * @param dataset
     */
    public RecyclerViewAdapter(int type, ArrayList<T> dataset, RecyclerViewSetter recyclerViewSetter) {
        t = type;
        dset = dataset;
        setter = recyclerViewSetter;

        setHasStableIds(true);
    }

    /**
     * Last Modified: 2020-02-18
     * Last Modified By: Shin Minyong
     * Created: 2020-02-12
     * Created By: Shin Minyong
     * Function: Tracker Setter, Getter
     * If Tracker is set inside the constructor, an error occurs
     * @param tracker
     */
    public void setTracker(SelectionTracker tracker) {
        trckr = tracker;
    }
    public SelectionTracker getTracker() {
        return this.trckr;
    }

    /**
     * Last Modified: 2020-02-18
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * Function: Create new views (invoked by the layout manager)
     * @param parent
     * @param viewType
     * @return textViewHolder
     */
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        int layoutId = setter.getLayoutId(t);
        View view = layoutInflater.inflate(layoutId, parent, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(t, view);
        return holder;
    }

    /**
     * Last Modified: 2020-02-18
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * Function: Replace the contents of a view (invoked by the layout manager)
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        T data = dset.get(position);
        if (trckr != null) {
            holder.bind(t, data, trckr.isSelected(Long.valueOf(position)));
        }

        if (t == 1) {
            if (holder.cbDone.isChecked()) {
                holder.span = (Spannable) holder.tvName.getText();
                holder.span.setSpan(new BackgroundColorSpan(holder.resources.getColor(R.color.colorInactive))
                        , 0, (int) holder.tvName.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.tvName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tvTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.imgRoutine.setColorFilter(holder.resources.getColor(R.color.colorInactive));
                holder.imgRank.setColorFilter(holder.resources.getColor(R.color.colorInactive));
            } else {
                holder.tvName.setPaintFlags(0);
                holder.tvTime.setPaintFlags(0);
            }
        }
    }

    /**
     * Last Modified: 2020-02-12
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * Function: Get the position in Long type
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }

    /**
     * Last Modified: 2020-02-09
     * Last Modified By: Shin Minyong
     * Created: -
     * Created By: -
     * Function: Return the size of the dataset (invoked by the layout manager)
     * @return dataset.size()
     */
    @Override
    public int getItemCount() {
        return dset.size();
    }

}
