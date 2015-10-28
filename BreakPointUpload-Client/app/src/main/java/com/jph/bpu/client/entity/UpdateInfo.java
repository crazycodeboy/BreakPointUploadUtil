package com.jph.bpu.client.entity;

/**
 * Author: JPH
 * Date: 2015/10/28 0028 21:15
 */
public class UpdateInfo {
    private  long total;
    private long current;
    private boolean isUploading;

    public UpdateInfo(long total ,long current, boolean isUploading ) {
        this.total = total;
        this.current = current;
        this.isUploading = isUploading;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public boolean isUploading() {
        return isUploading;
    }

    public void setIsUploading(boolean isUploading) {
        this.isUploading = isUploading;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
