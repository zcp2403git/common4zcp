package com.netease.commonlibrary.Net;

import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netease.commonlibrary.Net.VolleyUtils.VolleyNetManager;

import java.lang.reflect.Modifier;

/**
 * 网络请求配置类
 *
 * @author zhouchangping
 */
public class NetConfig {

    public NetworkManagerStack managerStack;
    public Gson gsonParse;//Gson对象
    public static int retryCount=0;
    public static long timeOut=10_000;

    public NetConfig(NetConfig.Builder builder) {
        this.managerStack = builder.networkManagerStack;
        this.gsonParse=builder.gson;
        this.retryCount=builder.retryCount;
        this.timeOut=builder.timeOut;
    }

    public static class Builder {
        private NetworkManagerStack networkManagerStack;
        private Gson gson;
        private Context context;
        private  int retryCount;
        private  long timeOut;

        public NetConfig.Builder networkManager(NetworkManagerStack networkManagerStack) {
            this.networkManagerStack = networkManagerStack;
            return this;
        }

        public NetConfig.Builder Gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public NetConfig.Builder retryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public NetConfig.Builder timeOut(long timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder(Context context) {
            this.networkManagerStack = null;
            this.gson=null;
            this.context = context;

        }


        public NetConfig build() {
            this.initEmptyFieldsWithDefaultValues();
            return new NetConfig(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (this.networkManagerStack == null) {
                this.networkManagerStack = VolleyNetManager.getInstance(context);
            }
            if (this.gson == null) {
                initGson();
            }

        }

        private  void initGson() {
            final int sdk = Build.VERSION.SDK_INT;
            if (sdk >= 23)
            {
                GsonBuilder gsonBuilder = new GsonBuilder()
                        .excludeFieldsWithModifiers(
                                Modifier.FINAL,
                                Modifier.TRANSIENT,
                                Modifier.STATIC);
                this.gson = gsonBuilder.create();
            } else
            {
                this.gson = new Gson();
            }
        }
    }


}