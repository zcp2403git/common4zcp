
package com.netease.commonlibrary.View.RecyclyViewSupport;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ABaseLinearLayoutManager extends LinearLayoutManager implements RecyclerViewScrollManager.OnScrollManagerLocation {
    private static final String TAG = ABaseLinearLayoutManager.class.getSimpleName();
    private RecyclerViewScrollManager recyclerViewScrollManager;

    public void setOnRecyclerViewScrollLocationListener(RecyclerView recyclerView, OnRecyclerViewScrollLocationListener onRecyclerViewScrollLocationListener) {
        this.ensureRecyclerViewScrollManager();
        this.recyclerViewScrollManager.setOnRecyclerViewScrollLocationListener(onRecyclerViewScrollLocationListener);
        this.recyclerViewScrollManager.setOnScrollManagerLocation(this);
        this.recyclerViewScrollManager.registerScrollListener(recyclerView);
    }

    public ABaseLinearLayoutManager(Context context) {
        super(context);
    }

    public ABaseLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public boolean isScrolling() {
        return null != this.recyclerViewScrollManager?this.recyclerViewScrollManager.isScrolling():false;
    }

    public RecyclerViewScrollManager getRecyclerViewScrollManager() {
        this.ensureRecyclerViewScrollManager();
        return this.recyclerViewScrollManager;
    }

    private void ensureRecyclerViewScrollManager() {
        if(null == this.recyclerViewScrollManager) {
            this.recyclerViewScrollManager = new RecyclerViewScrollManager();
        }

    }

    public boolean isTop(RecyclerView recyclerView) {
        return 0 == this.findFirstVisibleItemPosition();
    }

    public boolean isBottom(RecyclerView recyclerView) {
        int lastVisiblePosition = this.findLastCompletelyVisibleItemPosition();
        int lastPosition = recyclerView.getAdapter().getItemCount() - 1;
        return lastVisiblePosition == lastPosition;
    }
}
