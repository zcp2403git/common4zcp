package com.netease.commonlibrary.net.VolleyUtils;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.netease.commonlibrary.callback.INetworkResultCallback;
import com.netease.commonlibrary.callback.NetworkResultCallback;
import com.netease.commonlibrary.constant.LibraryConstant;
import com.netease.commonlibrary.exception.CommonLogException;
import com.netease.commonlibrary.net.BaseNetUtilsManager;
import com.netease.commonlibrary.net.NetConfig;
import com.netease.commonlibrary.net.NetworkManagerStack;
import com.netease.commonlibrary.net.VolleyUtils.okhttp.ProgressRequestBody;
import com.netease.commonlibrary.net.VolleyUtils.okhttp.UploadFileInfo;
import com.netease.commonlibrary.net.VolleyUtils.volley.OkRequest;
import com.netease.commonlibrary.net.VolleyUtils.volley.toolbox.OkVolley;
import com.netease.commonlibrary.utils.log.L;
import com.netease.commonlibrary.utils.network.NetworkStateUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import im.amomo.volley.BuildConfig;

/**
 * 用Volley网络请求
 * @author zhouchangping
 */
public class VolleyNetManager extends BaseNetUtilsManager implements NetworkManagerStack {

    private static final int DefaultCode=100;
    private static VolleyNetManager instance = null;
    private HashMap<INetworkResultCallback, HashMap<Integer, OkRequest>> taskMap = new HashMap<INetworkResultCallback, HashMap<Integer, OkRequest>>();
    private Context context;
    OkHttpClient mOkHttpClient;
    public static synchronized VolleyNetManager getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyNetManager(context);
        }
        return instance;
    }

    private VolleyNetManager(Context context) {
        super();
        this.context=context;
        OkVolley.getInstance().init(context)
                .setUserAgent(OkVolley.generateUserAgent(context))
                .trustAllCerts();
        initOkHttpClientManager();
        VolleyLog.DEBUG = BuildConfig.DEBUG;
    }

    private void initOkHttpClientManager()
    {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mOkHttpClient.setConnectTimeout(NetConfig.timeOut, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(NetConfig.timeOut, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(NetConfig.timeOut, TimeUnit.SECONDS);

    }
    /**
     * Post方式请求数据
     * @param url
     * @param postParams
     * @param callback  结果回调
     */
    public void postNetworkData(int requestCode,String url,HashMap<String, String> postParams,INetworkResultCallback callback) {
        requestNetworkData(requestCode, null, url, postParams, Request.Method.POST, callback);
    }
    /**
     * Get方式请求数据
     * @param url
     * @param postParams
     * @param callback  结果回调
     */
    public void getNetworkData(int requestCode,String url,HashMap<String, String> postParams ,INetworkResultCallback callback) {
        requestNetworkData(requestCode, null, url, postParams, Request.Method.GET, callback);
    }

    /**
     * 获取通用返回bean数据结果
     * @param requestCode 请求code
     * @param headers      http请求头
     * @param url         请求地址
     * @param postParams  POST 请求的键值对
     * @param method      POST或者GET方式
     *                    POST: HTTP_POST
     *                    GET:HTTP_GET
     * @param callback  结果回调
     */
    public void requestNetworkData(final int requestCode, Map<String, String> headers, String url, HashMap<String, String> postParams, int method, INetworkResultCallback callback) {
        if (callback == null)
            callback = NetworkResultCallback.DEFAULT_RESULT_CALLBACK;
        final INetworkResultCallback resCallBack =callback;

        if (NetworkStateUtil.checkNetworkType(context)==NetworkStateUtil.TYPE_NET_WORK_DISABLED) {
            sendFailResultCallback(requestCode, new ConnectException(), resCallBack);
            return;
        }

        OkRequest request = new DefaultResponseRequest(method, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try
                {
                    final String string =  new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    if (resCallBack.mType == String.class)
                    {
                        sendSuccessResultCallback(requestCode,string, resCallBack);
                    } else
                    {
                        Object o = mGson.fromJson(string, resCallBack.mType);
                        sendSuccessResultCallback(requestCode,o , resCallBack);
                    }
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailResultCallback(requestCode,e, resCallBack);
                } catch (UnsupportedEncodingException e) {
                    sendFailResultCallback(requestCode,e, resCallBack);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sendFailResultCallback(requestCode,error, resCallBack);
            }
        });
        if(headers == null){
            headers = new HashMap<String, String>();
        }
        request.headers(headers);
        if(postParams == null){
            postParams = new HashMap<String, String>();
        }
        request.params(postParams);
        request.setTag(requestCode);
        OkVolley.getInstance().getRequestQueue().add(request);
        if (LibraryConstant.L_DEBUG){
                L.i(" request"+requestCode,request.getUrl());
        }
        HashMap<Integer, OkRequest> map = taskMap.get(callback);
        if (map == null) {
            map = new HashMap<Integer, OkRequest>();
            taskMap.put(callback, map);
        }
        map.put(requestCode, request);
    }
    /**
     * 取消指定requestCode的任务
     */
    public void cancelSingle( int requestCode,INetworkResultCallback callback) {
        if (callback == null)
            return;
        HashMap<Integer, OkRequest> map = taskMap.get(callback);
        if (map == null)
            return;
        OkRequest request = map.get(requestCode);
        if (request != null && !request.isCanceled()) {
            request.cancel();
        }
        map.remove(requestCode);
    }

    /**
     * 取消所有网络任务
     */
    public void cancelAll(INetworkResultCallback callback) {
        HashMap<Integer, OkRequest> map = taskMap.get(callback);
        if (map == null)
            return;
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            Integer requestCode = (Integer) it.next();
            OkRequest request = map.get(requestCode);
            if (request != null && !request.isCanceled()) {
                request.cancel();
            }
            it.remove();
        }
        taskMap.remove(callback);
    }

    /**
     * 上传多个文件，没走volley的方式
     * @param requestCode 请求code
     * @param headers      http请求头
     * @param url         请求地址
     * @param postParams  POST 请求的键值对
     * @param uploadList      上传文件的信息
     * @param callback  结果回调
     */
    public void uploadFiles(final int requestCode, HashMap<String, String> headers, String url, HashMap<String, String> postParams, List<UploadFileInfo> uploadList, final INetworkResultCallback callback){
        if (NetworkStateUtil.checkNetworkType(context)==NetworkStateUtil.TYPE_NET_WORK_DISABLED) {
            sendFailResultCallback(requestCode, new ConnectException(), callback);
            return;
        }
        com.squareup.okhttp.Callback okCallback =new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                sendFailResultCallback(requestCode, e, callback);

            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                sendSuccessResultCallback(requestCode, response.body().string(), callback);

            }
        };
        if(headers == null){
            headers = new HashMap<String, String>();
        }
        if(postParams == null){
            postParams = new HashMap<String, String>();
        }
        if(uploadList == null || uploadList.isEmpty()){
            sendFailResultCallback(requestCode, new CommonLogException("no file find"), callback);
            return;
        }

        MultipartBuilder builder=new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);
        for (int i=0;i<uploadList.size();i++) {
            File file=uploadList.get(i).file;
            if (file.exists()) {
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file);
                if (uploadList.get(i).listener!=null) {
                    ProgressRequestBody progressRequestBody = new ProgressRequestBody(fileBody, uploadList.get(i).listener);
                    builder.addFormDataPart(uploadList.get(i).param, file.getName(), progressRequestBody);
                }else {
                    builder.addFormDataPart(uploadList.get(i).param, file.getName(), fileBody);
                }
            }else{
                //文件路径错误
            }
        }

//        RequestBody formBody = new FormEncodingBuilder()
//                .add("platform", "android")
//                .build();

        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(bindParams(url, postParams))
                .post(builder.build())
                .tag(requestCode)
                .headers(Headers.of(headers))
                .build();

        if (LibraryConstant.L_DEBUG){
            L.i("request"+requestCode,bindParams(url, postParams));
        }

        Call call = mOkHttpClient.newCall(request);
        boolean isAsync=true;
        if (isAsync)
            call.enqueue(okCallback);
        else {
            try {
                com.squareup.okhttp.Response res=call.execute();
                sendSuccessResultCallback(requestCode, res.body().string(), callback);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传单个文件，没走volley的方式
     * @param requestCode 请求code
     * @param headers      http请求头
     * @param url         请求地址
     * @param postParams  POST 请求的键值对
     * @param upload      上传文件的信息
     * @param callback  结果回调
     */
    public void uploadFile(final int requestCode,HashMap<String, String> headers,String url,HashMap<String, String> postParams,UploadFileInfo upload, final INetworkResultCallback callback){
        List<UploadFileInfo> list=new ArrayList<UploadFileInfo>();
        list.add(upload);
        uploadFiles(requestCode, headers, url, postParams, list, callback);
    }


    /**
     * 取消指定requestCode的上传任务
     * @param requestCode 请求code
     */
    public void cancelUploadTask( final int requestCode) {
        mOkHttpClient.getDispatcher().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                mOkHttpClient.cancel(requestCode);
//                        call.cancel();
            }
        });
    }

    public static String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public String bindParams(String url, Map<String, String> values) {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (!values.isEmpty()) {
            if (url.contains("?")) {
            } else {
                urlBuilder.append("?");
            }
            for (Map.Entry<String, String> entry : values.entrySet()) {
                urlBuilder.append("&");
                urlBuilder.append(entry.getKey());
                urlBuilder.append("=");
                urlBuilder.append(entry.getValue());
            }
            return urlBuilder.toString();
        }else{
            return url;
        }
    }


}