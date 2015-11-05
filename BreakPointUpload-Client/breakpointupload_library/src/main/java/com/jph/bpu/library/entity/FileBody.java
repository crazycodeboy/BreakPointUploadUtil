package com.jph.bpu.library.entity;

/**
 * Author: JPH
 * Date: 2015/11/5 0005 18:47
 */
public class FileBody {
    //本地文件路径
    private String localFilePath;
    //文件上传到的网络路径
    private String netPath;
    //模块名
    private String moduleType;
    //文件大小
    private long fileSize;

    public FileBody(String localFilePath, String moduleType) {
        this.localFilePath = localFilePath;
        this.moduleType = moduleType;
    }

    public FileBody(long fileSize, String localFilePath, String moduleType, String netPath) {
        this.fileSize = fileSize;
        this.localFilePath = localFilePath;
        this.moduleType = moduleType;
        this.netPath = netPath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
}
