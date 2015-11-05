package com.jph.bpu.library.callback;

import com.jph.bpu.library.entity.FailInfo;
import com.jph.bpu.library.entity.SuccessInfo;

/**
 * Author: JPH
 * Date: 2015/10/14 0014 10:32
 */
public interface RequestCallBack {
    void onStart();
    void onLoading(long total, long current, boolean isUploading);
    void onSuccess(SuccessInfo info, boolean isLast);
    void onFailure(FailInfo error, boolean isLast);
}