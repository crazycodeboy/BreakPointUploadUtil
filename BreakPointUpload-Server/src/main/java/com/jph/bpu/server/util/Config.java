package com.jph.bpu.server.util;

import java.util.HashSet;

public class Config {
	
	
	/**
	 * save path
	 */
    public static final String IMG_SAVE_PATH = "/data/dev_upload/img";

    public static final String APK_SAVE_PATH = "/data/dev_upload/apk";

    public static final String ZIP_SAVE_PATH = "/data/dev_upload/zip";
    
    public static final String MD5_SALT = "jph.com";
    
    public static final HashSet<String> FILE_NAMES_CACHE = new HashSet<String>();

}
