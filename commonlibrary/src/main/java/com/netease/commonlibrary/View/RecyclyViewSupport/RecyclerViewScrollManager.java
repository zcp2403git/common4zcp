//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.netease.commonlibrary.View.RecyclyViewSupport;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecyclerViewScrollManager {
    private List<OnRecyclerViewScrollListener> scrollListeners;
    private OnRecyclerViewScrollLocationListener onRecyclerViewScrollLocationListener;
    private RecyclerViewScrollManager.OnScrollManagerLocation onScrollManagerLocation;
    private boolean isScrolling;
    OnScrollListener onScrollListener;

    public RecyclerViewScrollManager() {
    }

    public void setOnScrollManagerLocation(RecyclerViewScrollManager.OnScrollManagerLocation onScrollManagerLocation) {
        this.onScrollManagerLocation = onScrollManagerLocation;
    }

    public void setOnRecyclerViewScrollLocationListener(OnRecyclerViewScrollLocationListener onRecyclerViewScrollLocationListener) {
        this.onRecyclerViewScrollLocationListener = onRecyclerViewScrollLocationListener;
    }

    public boolean isScrolling() {
        return this.isScrolling;
    }

    public void registerScrollListener(RecyclerView recyclerView) {
        this.addScrollListener(recyclerView, new OnRecyclerViewScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == 0) {
                    RecyclerViewScrollManager.this.isScrolling = false;
                    if(null != RecyclerViewScrollManager.this.onRecyclerViewScrollLocationListener) {
                        this.checkTopWhenScrollIdle(recyclerView);
                        this.checkBottomWhenScrollIdle(recyclerView);
                    }
                } else {
                    RecyclerViewScrollManager.this.isScrolling = true;
                }

            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }

            private void checkBottomWhenScrollIdle(RecyclerView recyclerView) {
                if(RecyclerViewScrollManager.this.onScrollManagerLocation.isBottom(recyclerView)) {
                    RecyclerViewScrollManager.this.onRecyclerViewScrollLocationListener.onBottomWhenScrollIdle(recyclerView);
                }

            }

            private void checkTopWhenScrollIdle(RecyclerView recyclerView) {
                if(RecyclerViewScrollManager.this.onScrollManagerLocation.isTop(recyclerView)) {
                    RecyclerViewScrollManager.this.onRecyclerViewScrollLocationListener.onTopWhenScrollIdle(recyclerView);
                }

            }
        });
    }

    public void addScrollListener(RecyclerView recyclerView, OnRecyclerViewScrollListener onRecyclerViewScrollListener) {
        if(null != onRecyclerViewScrollListener) {
            if(null == this.scrollListeners) {
                this.scrollListeners = new ArrayList();
            }

            this.scrollListeners.add(onRecyclerViewScrollListener);
            this.ensureScrollListener(recyclerView);
        }
    }

    private void ensureScrollListener(RecyclerView recyclerView) {
        if(null == this.onScrollListener) {
            this.onScrollListener = new OnScrollListener() {
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Iterator var3 = RecyclerViewScrollManager.this.scrollListeners.iterator();

                    while(var3.hasNext()) {
                        OnRecyclerViewScrollListener listener = (OnRecyclerViewScrollListener)var3.next();
                        listener.onScrollStateChanged(recyclerView, newState);
                    }

                }

                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Iterator var4 = RecyclerViewScrollManager.this.scrollListeners.iterator();

                    while(var4.hasNext()) {
                        OnRecyclerViewScrollListener listener = (OnRecyclerViewScrollListener)var4.next();
                        listener.onScrolled(recyclerView, dx, dy);
                    }

                }
            };
            recyclerView.setOnScrollListener(this.onScrollListener);
        }

    }

    public interface OnScrollManagerLocation {
        boolean isTop(RecyclerView var1);

        boolean isBottom(RecyclerView var1);
    }
}
