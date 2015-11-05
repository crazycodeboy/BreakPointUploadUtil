package com.jph.bpu.client;

import com.jph.bpu.client.callback.RequestCallBack;
import com.jph.bpu.client.upload.UploadHandler;

import java.util.ArrayList;

/**
 * Author: JPH
 * Date: 2015/11/5 0005 17:32
 */
public class UplaodUtil {
    private UploadHandler handler;
    public UplaodUtil() {
    }

    /**
     * 上传单个文件
     * @param localFilePath 上传文件的本地路径
     * @param callBack
     */
    public void upload(String localFilePath, RequestCallBack callBack) {
        handler=new UploadHandler(localFilePath,callBack);
        handler.execute();
    }

    /**
     * 批量上传文件
     * @param localFiles 要上文件的本地路径的集合
     * @param callBack
     */
    public void upload(ArrayList<String> localFiles, RequestCallBack callBack) {
        handler=new UploadHandler(localFiles,callBack);
        handler.execute();
    }
}
