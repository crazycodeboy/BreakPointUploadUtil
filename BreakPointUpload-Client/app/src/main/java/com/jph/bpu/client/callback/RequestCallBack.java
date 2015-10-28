package com.jph.bpu.client.callback;

import com.jph.bpu.client.entity.FailInfo;
import com.jph.bpu.client.entity.SuccessInfo;

/**
 * Author: JPH
 * Date: 2015/10/14 0014 10:32
 */
public interface RequestCallBack {
    void onStart();
    void onLoading(long total, long current, boolean isUploading);
    void onSuccess(SuccessInfo info);
    void onFailure(FailInfo error);
}