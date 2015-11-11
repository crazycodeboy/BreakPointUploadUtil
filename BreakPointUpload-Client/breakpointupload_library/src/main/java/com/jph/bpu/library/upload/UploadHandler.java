package com.jph.bpu.library.upload;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.jph.bpu.library.callback.RequestCallBack;
import com.jph.bpu.library.entity.FailInfo;
import com.jph.bpu.library.entity.FileBody;
import com.jph.bpu.library.entity.SuccessInfo;
import com.jph.bpu.library.entity.UpdateInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * 断点续传工具类
 *
 * @author JPH
 * @date 2015-5-8 下午7:06:06
 */
public class UploadHandler extends AsyncTask<String, Integer, ArrayList> {
    public final static int WHAT_UPDATE = 0;
    private final String TAG = UploadHandler.class.getSimpleName();
    private RequestCallBack callBack;
    private BreakPointUploadTool uploadUtil;
    private ArrayList<FileBody> fileBodies;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_UPDATE://更新下载进度
                    UpdateInfo info = (UpdateInfo) msg.obj;
                    callBack.onLoading(info.getTotal(), info.getCurrent(), info.isUploading());
                    break;
            }
        }
    };

    public UploadHandler(FileBody fileBody, String serverUrl,RequestCallBack callBack) {
        this(new ArrayList<FileBody>(1),serverUrl,callBack);
        fileBodies.add(fileBody);
    }

    public UploadHandler(ArrayList<?extends FileBody> fileBodies,String serverUrl, RequestCallBack callBack) {
        this.fileBodies = (ArrayList<FileBody>) fileBodies;
        this.callBack = callBack;
        uploadUtil = new BreakPointUploadTool(mHandler,serverUrl);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callBack.onStart();
    }

    @Override
    protected ArrayList doInBackground(String... params) {
        ArrayList results = new ArrayList<>();
        for (FileBody fileBody: fileBodies) {
            results.add(uploadUtil.uploadFile(addExtra(fileBody)));
        }
        return results;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {// 执行进度
    }

    @Override
    protected void onPostExecute(ArrayList results) {// 执行结果
        super.onPostExecute(results);
        for (int i=0;i<results.size();i++){
            boolean isLast=false;
            if (i==results.size()-1)isLast=true;
            Object result=results.get(i);
            if (result instanceof FailInfo) {
                callBack.onFailure((FailInfo) result,isLast);
            } else {
                callBack.onSuccess((SuccessInfo) result,isLast);
            }
        }
    }
    private FileBody addExtra(FileBody fileBody){
        fileBody.setFileSize(new File(fileBody.getLocalFilePath()).length());
        return fileBody;
    }
}
