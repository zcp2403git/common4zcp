package com.netease.commonlibrary.utils.log;

/**
 * Created by zhouchangping on 2016/1/25.
 */
public class ExceptionUtil {

    public static String getStackMsg(Exception e) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }

    public static String getStackMsg(Throwable t) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray =t.getStackTrace();
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }
}
