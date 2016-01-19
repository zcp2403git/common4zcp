package com.netease.commonlibrary.Controller.recycleList;

import java.util.List;

/**
 * Created by zhouchangping on 2016/1/8.
 * 内部Adapter
 */
public interface IRecyclerAdapter<T> {
    /**
     * 移除
     * @param position
     */
    public void remove(int position);

    /**
     * 添加
     * @param bean
     */
    public void add(T bean);

    /**
     * 批量添加
     * @param beans
     */
    public void addAll(List<T> beans);

    /**
     *  清空
     */
    public void clear() ;

    /**
     *  填充数据
     *  * @param list
     */
    public void setList(List<T> list);

//    /**
//     *  创建viewholder
//     */
//    public VH onCreateViewHolder( ViewGroup parent, int position);
//
//    /***
//     * bind viewholder
//    */
//    public void onBindViewHolder(VH holder, int position);
}
