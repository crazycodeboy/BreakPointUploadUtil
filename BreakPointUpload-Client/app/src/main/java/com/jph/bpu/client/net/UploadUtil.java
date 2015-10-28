package com.jph.bpu.client.net;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jph.bpu.client.callback.RequestCallBack;
import com.jph.bpu.client.entity.FailInfo;
import com.jph.bpu.client.entity.SuccessInfo;
import com.jph.bpu.client.entity.UpdateInfo;

/**
 * 断点续传工具类
 *
 * @author JPH
 * @date 2015-5-8 下午7:06:06
 */
public class UploadUtil extends AsyncTask<String, Integer, Object> {
    public final static int WHAT_UPDATE = 0;
    private final String TAG = UploadUtil.class.getSimpleName();
    private String localFilePath;
    private RequestCallBack callBack;
    private BreakPointUploadTool uploadUtil;
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
    public UploadUtil(String localFilePath, RequestCallBack callBack) {
        this.localFilePath = localFilePath;
        this.callBack = callBack;
        uploadUtil = new BreakPointUploadTool(callBack,mHandler);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callBack.onStart();
    }

    @Override
    protected Object doInBackground(String... params) {
        return uploadUtil.uploadFile(localFilePath);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {// 执行进度
    }

    @Override
    protected void onPostExecute(Object result) {// 执行结果
        Log.i(TAG, "result:" + result);
        super.onPostExecute(result);
        if (result instanceof FailInfo) {
            callBack.onFailure((FailInfo) result);
        } else {
            callBack.onSuccess((SuccessInfo) result);
        }
    }
}
