package com.netease.commonlibrary.net.VolleyUtils.okhttp;
/**
 * @author zhouchangping on 2016/1/15.
 */
public  interface OKProgressRequestListener {
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }