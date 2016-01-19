package com.netease.commonlibrary.Utils.Common;

import android.content.Context;

/**
 * Created by zhouchangping on 2016/1/14.
 */
public class CommonTextUtils {
    /**
     * 判断是否是Emoji
     * @return
     */
    public static boolean isEmojiCharacter(Context context,String text) {
        boolean isEmoji=false;
        char[] cTemp = text.toCharArray();
        for (int i = 0; i < text.length(); i++) {
            if(!Character.isLetterOrDigit(cTemp[i])){
                isEmoji = true;
            }
        }
        return isEmoji;
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    // 中文占据两个位置算两个。
    public static int convertToChinesLenth(String strName) {
        int len = 0;
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                len += 2;
            } else {
                len += 1;
            }
        }
        return len/2;
    }

}
