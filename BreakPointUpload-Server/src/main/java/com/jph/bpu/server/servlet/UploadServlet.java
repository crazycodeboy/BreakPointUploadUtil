package com.jph.bpu.server.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import com.jph.bpu.server.model.JSONCode;
import com.jph.bpu.server.model.Range;
import com.jph.bpu.server.util.Config;
import com.jph.bpu.server.util.JSONUtil;
import com.jph.bpu.server.util.MsgDigestUtil;
import com.jph.bpu.server.util.UploadFileUtil;

/**
 */
public class UploadServlet extends HttpServlet {

    private static final long serialVersionUID = -8997693374942920321L;

    public static final String CONTENT_RANGE_HEADER = "content-range";

    static final int BUFFER_LENGTH = 10240;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doOptions(req, resp);
        String saveFileName = req.getParameter("saveName");
        String strDirType = req.getParameter("dirtype");
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            Range range = UploadFileUtil.parseRange(req);
            
            if (StringUtils.isBlank(saveFileName) || !Config.FILE_NAMES_CACHE.contains(saveFileName)) {
                map.put("code", JSONCode.ERROR);
                map.put("msg", "文件名在服务器端不存在");
            } else {
                OutputStream out = null;
                InputStream content = null;

                File file = UploadFileUtil.getFile(saveFileName, strDirType);
                long start = file.length();

                if (start != range.getFrom()) {
                    map.put("code", JSONCode.ERROR);
                    map.put("msg", "断点续传ERROR");
                } else {
                    out = new FileOutputStream(file, true);
                    content = req.getInputStream();

                    int read = 0;
                    final byte[] bytes = new byte[BUFFER_LENGTH];
                    while ((read = content.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    start = file.length();
                    map.put("code", JSONCode.SUCCESS);
                    map.put("start", start);

                    // 上传完成
                    if (start == range.getSize()) {
                        map.put("start",-1);
                        Config.FILE_NAMES_CACHE.remove(saveFileName);
                    }

                    // path 返回前端并保存到数据库
                    String strTemp = file.getPath();
                    Calendar clr = Calendar.getInstance();
                    map.put("path", strTemp.substring(strTemp.indexOf(String.valueOf(clr.get(Calendar.YEAR)))));

                    UploadFileUtil.close(out);
                    UploadFileUtil.close(content);
                }
                file = null;
            }
        } catch (IOException e) {
            map.put("code", JSONCode.ERROR);
            map.put("msg", e.getMessage().toString());
        }
        resp.getWriter().write(JSONUtil.beanToJson(map));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doOptions(req, resp);
        HashMap<String, Object> map = new HashMap<String, Object>();
        resp.setCharacterEncoding("utf8");
        PrintWriter writer = resp.getWriter();
        try {
            // 生成新的name
            resp.setContentType("text/html;charset=UTF-8");
            String fileName = req.getParameter("name");
            
            // 文件类型判断
            if (!(fileName.endsWith(".zip") || fileName.endsWith(".rar") || fileName.endsWith(".jpg") ||  fileName.endsWith(".png") || fileName.endsWith(".bmp") || fileName.endsWith(".jpeg")  )) {
                map.put("code", JSONCode.ERROR);
                map.put("msg", "文件类型不支持,支持(zip,rar,jpg,png,bmp,jpeg)格式");
                writer.write(JSONUtil.beanToJson(map));
                return;
            }

            String type = req.getParameter("type");
            if (null == type) {
                type = "";
            }
            String size = req.getParameter("size");
            String lastModifiedDate = req.getParameter("modified");
            String saveFileName = "";

            // 验证参数是否合理
            if (StringUtils.isBlank(fileName) || StringUtils.isBlank(size) || StringUtils.isBlank(lastModifiedDate)
                    || fileName.length() < 3 || (fileName.indexOf(".") == -1)) {
                map.put("code", JSONCode.ERROR);
                map.put("msg", "参数错误");
                writer.write(JSONUtil.beanToJson(map));
                return;
            } else {
                saveFileName = MsgDigestUtil.MD5
                        .digest2HEX(fileName + type + size + lastModifiedDate + Config.MD5_SALT)
                        + fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
                Config.FILE_NAMES_CACHE.add(saveFileName);
                map.put("fileName", saveFileName);
            }

            
            String strDirType = req.getParameter("dirtype");

            File file = UploadFileUtil.getFile(saveFileName, strDirType);
            map.put("code", JSONCode.SUCCESS);
            map.put("start", file.length());
            map.put("msg", "成功");

            // path 返回前端并保存到数据库
            String strTemp = file.getPath();
            Calendar clr = Calendar.getInstance();
            map.put("path", strTemp.substring(strTemp.indexOf(String.valueOf(clr.get(Calendar.YEAR)))));
            
            
            file = null;
        } catch (IOException e) {
            map.put("code", JSONCode.ERROR);
            map.put("msg", e.getMessage());
        }

        writer.write(JSONUtil.beanToJson(map));
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json ;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Range,Content-Type");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
    }
}
