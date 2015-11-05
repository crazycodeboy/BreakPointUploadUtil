package com.jph.bpu.library.entity;

/**
 * Author: JPH
 * Date: 2015/10/15 0015 11:17
 */
public class FailInfo {
    private String errorMsg;
    private String localPath;

    public FailInfo(String errorMsg, String localPath) {
        this.errorMsg = errorMsg;
        this.localPath = localPath;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
