package com.netease.commonlibrary.utils.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 后缀过滤器
 * Created by zhouchangping on 2016/1/25.
 */
public class PostfixFilenameFilter implements FilenameFilter {
    List<String> types;

    /**
     * 构造一个FileNameFilter对象，具有多个指定的文件类型。
     * @param types
     */
    public PostfixFilenameFilter(List<String> types) {
        super();
        this.types = types;
    }
    /**
     * 构造一个FileNameFilter对象，具有一个指定的文件类型。
     * @param type
     */
    public PostfixFilenameFilter(String type) {
        super();
        types = new ArrayList<String>();
        this.types.add(type);
    }

    @Override
    public boolean accept(File dir, String filename) {
        for (Iterator<String> iterator = types.iterator(); iterator.hasNext();) {
            String type = (String) iterator.next();
            if (filename.endsWith(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加指定类型的文件。
     * @param type  将添加的文件类型
     */
    public void addType(String type) {
        types.add(type);
    }

}