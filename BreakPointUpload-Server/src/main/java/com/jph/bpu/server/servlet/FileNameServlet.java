package com.jph.bpu.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jph.bpu.server.model.JSONCode;
import com.jph.bpu.server.util.Config;
import com.jph.bpu.server.util.JSONUtil;
import com.jph.bpu.server.util.MsgDigestUtil;

/**
 * 服务器生成的文件名称
 */
public class FileNameServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 444485694777548066L;


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String fileName = req.getParameter("name");
        String type = req.getParameter("type");
        if(null==type){
            type="";
        }
        String size = req.getParameter("size");
        String lastModifiedDate = req.getParameter("modified");

        PrintWriter writer = resp.getWriter();

        HashMap<String, Object> map = new HashMap<String, Object>();

        //验证参数是否合理
        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(size) || StringUtils.isBlank(lastModifiedDate) || fileName.length() < 3 || (fileName.indexOf(".") == -1)) {
            map.put("code", JSONCode.ERROR);
            map.put("msg", "参数错误");
        } else {
            String saveFileName = MsgDigestUtil.MD5.digest2HEX(fileName + type + size + lastModifiedDate + Config.MD5_SALT) + fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
            Config.FILE_NAMES_CACHE.add(saveFileName);
            map.put("code", JSONCode.SUCCESS);
            map.put("fileName", saveFileName);
        }

        String outStr = JSONUtil.beanToJson(map);

        String callback = req.getParameter("callback");
        if (callback != null) {
            outStr = callback + "(" + outStr + ")";
        }
        writer.write(outStr);
    }
}
