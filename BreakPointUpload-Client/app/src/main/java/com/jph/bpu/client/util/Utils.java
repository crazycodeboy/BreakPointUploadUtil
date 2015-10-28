package com.jph.bpu.client.util;

/**
 * Author: JPH
 * Date: 2015/10/28 0028 15:04
 */
public class Utils {
    /**
     * 将double形式的object转换成long
     * @param object
     * @return
     * @author JPH
     * @date 2015-4-28 下午4:44:14
     */
    public static long doubleObjectToLong(Object object) {
        if (object==null) return -1000;
        return Long.parseLong(object.toString().replace(".0", ""));
    }
}
