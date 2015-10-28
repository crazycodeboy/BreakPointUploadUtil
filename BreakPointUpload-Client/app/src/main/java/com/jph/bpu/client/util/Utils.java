package com.jph.bpu.client.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    /**
     * 根据流返回一个字符串信息
     * @param is
     * @return
     * @throws IOException
     */
    public static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        String data = bos.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        bos.close();
        return data;
    }
}
