package com.netease.commonlibrary.Controller.recycleList;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouchangping on 2015/12/16.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {

    protected List<T> dataList;
    public OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
        dataList=new ArrayList<>();
    }

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }

    @Override
    public abstract  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(T data) {
        insert(data, dataList.size());
    }

    public void insert(T data, int position) {
        dataList.add(position, data);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = dataList.size();
        dataList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void setList(List<T> list) {
        dataList=list;
        int size = dataList.size();
        notifyItemRangeChanged(0, size);
    }

    public void addAll(List<T> list) {
        int startIndex = dataList.size();
        dataList.addAll(list);
        notifyItemRangeInserted(startIndex, list.size());
    }

//    public  class BaseViewHolder  implements View.OnClickListener, View.OnLongClickListener {
//        private VH baseVH;
//        public int position;
//        private View itemView;
//
//        public BaseViewHolder(VH vh,View itemView) {
//            baseVH=vh;
//            this.itemView=itemView;
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (null != onRecyclerViewListener) {
//                onRecyclerViewListener.onItemClick(position);
//            }
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            if (null != onRecyclerViewListener) {
//                return onRecyclerViewListener.onItemLongClick(position);
//            }
//            return false;
//        }
//    }

}