package com.willy.will.common.controller;

import android.view.View;

public interface ListViewHolder<T> {

    void setView(View convertView);
    void bindData(T data);

}
