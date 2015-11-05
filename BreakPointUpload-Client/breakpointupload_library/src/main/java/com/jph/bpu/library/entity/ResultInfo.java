package com.jph.bpu.library.entity;

/**
 * 上传文件的描述
 * Author: JPH
 * Date: 2015/10/29 0029 10:57
 */
public class ResultInfo {
//    {"msg":"成功","path":"2015\\10\\pickup\\31f781753db9375a42b2793419d07620.png","fileName":"31f781753db9375a42b2793419d07620.png","code":1,"start":10565400}
//{"start":-1.0,"code":0,"path":"2015\\10\\pickup\\31f781753db9375a42b2793419d07620.png"}
    /**响应状态码*/
    private int code;
    /**上传的起点*/
    private long start;
    private String msg;
    /**上传的文件在服务器保存的路径*/
    private String path;
    /**上传的文件在服务器保存的文件名*/
    private String fileName;

    public ResultInfo() {
    }
    public ResultInfo(int code, String fileName, String msg, String path, long start) {
        this.code = code;
        this.fileName = fileName;
        this.msg = msg;
        this.path = path;
        this.start = start;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }
    public String toString(){
        StringBuffer sbStartPoint=new StringBuffer();
        sbStartPoint.append("code:").append(code);
        sbStartPoint.append("start:").append(start);
        sbStartPoint.append("path:").append(path);
        sbStartPoint.append("fileName:").append(fileName);
        return sbStartPoint.toString();
    }
}
