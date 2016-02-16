package com.netease.commonlibrary.Controller.recycleList;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.swipe.SparseItemRemoveAnimator;
import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
import com.netease.commonlibrary.View.RecyclyViewSupport.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by zhouchangping on 2015/12/16.
 */
public class MyRecyclerController<T>  implements SwipeDismissRecyclerViewTouchListener.DismissCallbacks {

    private MyRecyclerView mRecycler;
    private BaseRecyclerAdapter<T>          mAdapter;
    private SparseItemRemoveAnimator mSparseAnimator;
    private RecyclerView.LayoutManager mLayoutManager;
    private Handler                    mHandler;
    private Context context;
    private List<T> dataList;
    private int leftItem =2;
    protected boolean dismissEnabled=false;

    public MyRecyclerController(Context context, BaseRecyclerAdapter mAdapter, MyRecyclerView mRecycler, RecyclerView.LayoutManager mLayoutManager){
        this.context=context;
        this.mAdapter=mAdapter;
        this.mRecycler=mRecycler;
        this.mLayoutManager=mLayoutManager;
        init();
    }

    public MyRecyclerController(Context context, BaseRecyclerAdapter mAdapter, MyRecyclerView mRecycler, List<T> dataList){
        this.context=context;
        this.mAdapter=mAdapter;
        this.mRecycler=mRecycler;
        this.dataList=dataList;
        this.mLayoutManager = new LinearLayoutManager(context);
        init();
    }

    public MyRecyclerController(Context context, BaseRecyclerAdapter mAdapter, MyRecyclerView mRecycler){
        this.mLayoutManager = new LinearLayoutManager(context);
        this.context=context;
        this.mAdapter = mAdapter;
        this.mRecycler=mRecycler;
        init();
    }
    //构造默认值
    protected void init() {

        mRecycler.setLayoutManager(mLayoutManager);
//        boolean dismissEnabled = isSwipeToDismissEnabled();
        if (dismissEnabled) {
            mRecycler.setupSwipeToDismiss(this);
            mSparseAnimator = new SparseItemRemoveAnimator();
            mRecycler.getRecyclerView().setItemAnimator(mSparseAnimator);
        }
        if (dataList==null)
            dataList=new ArrayList();
        if (mAdapter==null)
            mAdapter=new DefaultRecyclerAdapter<T>();
        mHandler = new Handler(Looper.getMainLooper());
        mRecycler.setAdapter(mAdapter);
        mAdapter.setList(dataList);


        mRecycler.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }

    public MyRecyclerController(Context context, MyRecyclerView mRecycler){
        this.mLayoutManager = new LinearLayoutManager(context);
        mAdapter=new DefaultRecyclerAdapter<T>();
        this.context=context;
        this.mRecycler=mRecycler;
        init();

    }

    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener context){
        mRecycler.setRefreshListener(context);
    }

    public void setLoadMoreListener(OnMoreListener context){
        mRecycler.setupMoreListener(context, leftItem);
    }

    public void setLoadMoreListener(OnMoreListener context,int leftItem){
        mRecycler.setupMoreListener(context, leftItem);
    }

//    public void enableMoreAndRefreshListener(boolean isNeedRefreshListener,boolean isNeedMoreistener) {
//        mRecycler.enableMoreAndRefreshListener(isNeedRefreshListener,isNeedMoreistener);
//    }
//    @Override
//    public void onRefresh() {
//
//        mHandler.postDelayed(new Runnable() {
//            public void run() {
//                CustomToast.showToast(context, "刷新");
//                mAdapter.notifyDataSetChanged();
////                mAdapter.add("New stuff");
//            }
//        }, 2000);
//    }
//    @Override
//    public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
//        mHandler.postDelayed(new Runnable() {
//            public void run() {
////                CustomToast.showToast(context, "加载更多");
////                mAdapter.notifyDataSetChanged();
////                mAdapter.add("More asked, more served");
//            }
//        }, 30);
//    }

    @Override
    public boolean canDismiss(int position) {
        return false;
    }

    @Override
    public void onDismiss(RecyclerView RecyclerView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mSparseAnimator.setSkipNext(true);
            mAdapter.remove(position);
        }
    }

    public void setList(List<T> dataList){
        mAdapter.setList(dataList);
    }

    public void setLoadLeftItem(int leftnum){
        leftItem = leftnum;
    }

    public BaseRecyclerAdapter<T> getAdapter() {
        return mAdapter;
    }

    public void setAdapterData(List dataList) {
        mAdapter.setList(dataList);
    }

    public void addAdapterData(List dataList) {
        mAdapter.addAll(dataList);
    }

    public MyRecyclerView getRecycler() {
        return mRecycler;
    }

    public void setDismissEnabled() {
        this.dismissEnabled = true;
            mRecycler.setupSwipeToDismiss(this);
            mSparseAnimator = new SparseItemRemoveAnimator();
            mRecycler.getRecyclerView().setItemAnimator(mSparseAnimator);
    }
}
