package com.netease.commonlibrary.net.VolleyUtils;

import android.os.SystemClock;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.netease.commonlibrary.net.VolleyUtils.volley.OkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求
 * @author zhouchangping
 */
public class BaseJSONRequest extends OkRequest<JSONObject> {
    private Map<String, String> extraParams;
    private long mRequestBeginTime = 0;

    public BaseJSONRequest(int method, String url, Response.Listener<JSONObject> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        setReseponseListener(listener);
        acceptGzipEncoding();
    }

    @Override
    //解析返回的数据
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            byte[] data = response.data;
            String json = new String(data, HttpHeaderParser.parseCharset(response.headers));
            if (VolleyLog.DEBUG) {
                VolleyLog.d("response:%s", json);
            }
            return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public void addMarker(String tag) {
        super.addMarker(tag);
        if (mRequestBeginTime == 0) {
            mRequestBeginTime = SystemClock.elapsedRealtime();
        }
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        super.deliverResponse(jsonObject);
        //请求用掉的总时间
        long requestTime = SystemClock.elapsedRealtime() - mRequestBeginTime;
    }

    @Override
    //可以自己在这里完成错误的自定义处理
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        //错误发生时候response的数据 byte[]
        //error.networkResponse.data;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return params;
    }
}
