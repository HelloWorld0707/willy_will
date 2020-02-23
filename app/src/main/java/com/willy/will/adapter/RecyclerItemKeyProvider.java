package com.willy.will.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemKeyProvider extends ItemKeyProvider<Long> {

    private RecyclerView recyclerView = null;

    public RecyclerItemKeyProvider(RecyclerView recyclerView) {
        super(ItemKeyProvider.SCOPE_MAPPED);
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return recyclerView.getAdapter().getItemId(position);
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(key);
        return viewHolder == null ? RecyclerView.NO_POSITION : viewHolder.getLayoutPosition();
    }

}
