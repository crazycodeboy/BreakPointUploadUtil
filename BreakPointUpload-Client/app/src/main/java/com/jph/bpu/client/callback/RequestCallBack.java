package com.jph.bpu.client.callback;

/**
 * Author: JPH
 * Date: 2015/10/14 0014 10:32
 */
public interface RequestCallBack {
    void onStart();
    void onLoading(long total, long current, boolean isUploading);
    void onSuccess(String localPath,String netPath);
    void onFailure(String error);
}