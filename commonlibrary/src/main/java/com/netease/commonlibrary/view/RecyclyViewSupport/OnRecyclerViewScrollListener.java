package com.netease.commonlibrary.view.recyclyviewsupport;

import android.support.v7.widget.RecyclerView;

public interface OnRecyclerViewScrollListener {
    void onScrollStateChanged(RecyclerView var1, int var2);

    void onScrolled(RecyclerView var1, int var2, int var3);
}
