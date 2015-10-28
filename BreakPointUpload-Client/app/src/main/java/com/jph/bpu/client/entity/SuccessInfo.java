package com.jph.bpu.client.entity;

/**
 * Author: JPH
 * Date: 2015/10/28 0028 20:20
 */
public class SuccessInfo {
    private String localPath;
    private String netPath;

    public SuccessInfo(String localPath, String netPath) {
        this.localPath = localPath;
        this.netPath = netPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }
}
