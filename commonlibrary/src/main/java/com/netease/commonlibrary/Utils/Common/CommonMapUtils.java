package com.netease.commonlibrary.utils.common;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouchangping on 2016/2/2.
 */
public class CommonMapUtils {

    public static Map nullToEmpty(@Nullable Map map) {
        if (map == null ) {
            return map=new HashMap();
        }
        return map;
    }

}
