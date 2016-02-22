package com.netease.commonlibrary.exception;

import com.netease.commonlibrary.constant.LibraryConstant;
import com.netease.commonlibrary.utils.log.ExceptionUtil;
import com.netease.commonlibrary.utils.log.L;

/**
 * Created by zhouchangping on 2015/12/15.
 */
public class CommonLogException extends Exception {

    private static final String Tag="CommonLogException";

    public CommonLogException() {
    }

    public CommonLogException(String message) {
        super (message);
        if (LibraryConstant.L_DEBUG)
            L.i(Tag,message);
    }

    public CommonLogException(Throwable cause) {
        super (cause);
        if (LibraryConstant.L_DEBUG)
            L.i(Tag, ExceptionUtil.getStackMsg(cause));
    }

    public CommonLogException(String message, Throwable cause) {
        super(message, cause);
        if (LibraryConstant.L_DEBUG){
            L.i(Tag,message);
            L.i(Tag,ExceptionUtil.getStackMsg(cause));
        }
    }

}