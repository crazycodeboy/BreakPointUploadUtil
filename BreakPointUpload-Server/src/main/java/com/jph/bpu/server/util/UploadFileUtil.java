package com.jph.bpu.server.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.jph.bpu.server.model.Range;
import com.jph.bpu.server.servlet.UploadServlet;

/**
 */
public class UploadFileUtil {



    
    static final Pattern RANGE_PATTERN = Pattern.compile("bytes \\d+-\\d+/\\d+");

    public static File getFile(String md5FileName,String strDirType) throws IOException {
        String filePath = getSavePath(md5FileName,strDirType);
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }
        try {
            if (file.createNewFile()) {
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IOException();
    }

    public static Range parseRange(HttpServletRequest req) throws IOException {
        String range = req.getHeader(UploadServlet.CONTENT_RANGE_HEADER);
        Matcher m = RANGE_PATTERN.matcher(range);
        if (m.find()) {
            range = m.group().replace("bytes ", "");
            String[] rangeSize = range.split("/");
            String[] fromTo = rangeSize[0].split("-");

            long from = Long.parseLong(fromTo[0]);
            long to = Long.parseLong(fromTo[1]);
            long size = Long.parseLong(rangeSize[1]);

            return new Range(from, to, size);
        }
        throw new IOException("Illegal Access!");
    }

    public static void close(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (IOException e) {
        }
    }

    public static String toLength(int iMonth) {
        String strMonth=String.valueOf(iMonth);
        if(strMonth.length()<2){
            strMonth="0"+strMonth;
        }
        return strMonth;
    }
    
    private static String getSavePath(String md5FileName,String strDirType) {
        Calendar clr=Calendar.getInstance();
        String strDir="F:/test/pic";         //正式
        //String strDir="D:/pic";
        strDir+="/"+clr.get(Calendar.YEAR)+"/"+toLength((clr.get(Calendar.MONTH)+1))+"/"+strDirType;
        
        /*String[] strs = md5FileName.split("\\.");
        String suffix = strs[1];
        String name = strs[0];
        String dir = Config.IMG_SAVE_PATH;
        
        if (suffix.equals("apk")) {
            dir = Config.APK_SAVE_PATH;
        }

        dir = dir + "/" + name.substring(0, 2) + "/"+name.substring(2,4)+"/" + name.substring(name.length() - 2);

        if (suffix.equals("zip")) {
            dir = Config.ZIP_SAVE_PATH;
        }*/

        File file = new File(strDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return strDir + "/" + md5FileName;
    }


    public static void main(String[] args) throws IOException {
        //System.out.println(getSavePath(MsgDigestUtil.MD5.digest2HEX("abc") + ".apk"));
    }
}
