package com.jph.bpu.library;

import com.jph.bpu.library.callback.RequestCallBack;
import com.jph.bpu.library.entity.FileBody;
import com.jph.bpu.library.upload.UploadHandler;

import java.io.File;
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
     * @param fileBody 要上文件FileBody的实体
     * @param serverUrl 服务器路径
     * @param callBack
     */
    public void upload(FileBody fileBody,String serverUrl, RequestCallBack callBack) {
        handler=new UploadHandler(fileBody,serverUrl,callBack);
        handler.execute();
    }

    /**
     * 批量上传文件
     * @param fileBodies 要上文件FileBody的实体集合
     * @param serverUrl 服务器路径
     * @param callBack
     */
    public void upload(ArrayList<?extends FileBody>fileBodies,String serverUrl, RequestCallBack callBack) {
        handler=new UploadHandler(fileBodies,serverUrl,callBack);
        handler.execute();
    }
}
