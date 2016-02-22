package com.netease.commonlibrary.utils.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

import com.netease.commonlibrary.utils.log.L;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统环境工具类.
 * 主要用于获取系统信息,如设备ID、操作系统版本等
 * @author zhouchangping
 */
public class SystemUtils {

    /**
     * 判断是否为平板设备
     * 
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取设备ID.
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = tm.getDeviceId();
        L.i(SystemUtils.class, "deviceId " + id);
        return id;
    }

    public static String getSystemName() {

        return Build.PRODUCT;
    }

    /**
     * 获取设备SDK版本号.
     *
     * @return
     */
    public static int getBuildVersionSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备系统版本号.
     *
     * @return
     */
    public static String getBuildVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getBuildMode() {
        L.i(SystemUtils.class, "android.os.Build.MODEL");
        return Build.MODEL;
    }

    /**
     * 判断SD卡是否插入 即是否有SD卡
     */
    public static boolean isSDCardMounted() {
        return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
    }

    /**
     * 是否：已经挂载,但只拥有可读权限
     */
    public static boolean isSDCardMountedReadOnly() {
        return android.os.Environment.MEDIA_MOUNTED_READ_ONLY.equals(android.os.Environment.getExternalStorageState());
    }

    /**
     * 获取android当前可用内存大小
     */
    public static String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    public static long getTotalMemory() {
            String str1 = "/proc/meminfo";
            String str2;
            String[] arrayOfString;
            long initial_memory = -1;
            FileReader localFileReader = null;
            try {
                localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                str2 = localBufferedReader.readLine();

                if (str2 != null) {
                    arrayOfString = str2.split("\\s+");
                    initial_memory = Integer.valueOf(arrayOfString[1]);
                }
                localBufferedReader.close();

            } catch (IOException e) {
                L.e(SystemUtils.class, "getTotalMemory exception = ", e);
            } finally {
                if (localFileReader != null) {
                    try {
                        localFileReader.close();
                    } catch (IOException e) {
                        L.e(SystemUtils.class, "close localFileReader exception = ", e);
                    }
                }
            }
        return initial_memory; // Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 获取屏幕的亮度
     */
    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 获取手机屏幕的宽和高
     *
     * @param c
     * @return map("w",width) map("h",height);
     */
    public static HashMap<String, Integer> getWidth_Height(Context c) {
        DisplayMetrics metrics = new DisplayMetrics();
        metrics = c.getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        HashMap<String, Integer> m = new HashMap<String, Integer>();
        m.put("w", width);
        m.put("h", height);
        return m;
    }

    /**
     * 获取手机屏幕的宽和高size wxh
     *
     * @param c
     * @return width X height
     */
    public static String getWidthXHeight(Context c) {
        Map<String, Integer> m = getWidth_Height(c);
        String size = m.get("w") + "x" + m.get("h");
        return size;
    }

    /**
     * 获取手机分辨率宽度大小
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getApplicationContext().getResources().getDisplayMetrics();
            return dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取手机分辨率宽度大小
     * @param context
     * @return
     */
    public static int getScreenHeigh(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
      }

    /**
     * 获取手机分辨率宽
     *
     * @param context
     * @return
     */
    public static String getScreenResolve(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels + "x" + dm.widthPixels;
    }
    public static int heightPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels ;
    }

    /**
     * 获得设备html宽度
     *
     * @param context
     * @return
     */
    public static int getDeviceHtmlWidth(Context context) {

        int width = (int) (getScreenWidth(context) / context.getResources().getDisplayMetrics().density);

        return width;
    }

    /**
     * 得到dimen定义的大小
     *
     * @param context
     * @param dimenId
     * @return
     */
    public static int getDimension(Context context, int dimenId) {
        return (int) context.getResources().getDimension(dimenId);
    }


    public static int getVersionCode(Context context) {
        PackageManager pManager = context.getPackageManager();
        try {
            PackageInfo packinfo = pManager.getPackageInfo(context.getPackageName(), 0);
            if (null != packinfo) {
                return packinfo.versionCode;
            } else {
                return -1;
            }
        } catch (NameNotFoundException e) {
            return -1;
        }
    }

    public static String getMAC(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();
    }


}
