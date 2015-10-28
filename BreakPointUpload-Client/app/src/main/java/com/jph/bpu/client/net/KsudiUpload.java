package com.jph.bpu.client.net;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jph.bpu.client.util.GsonUtil;
import com.jph.bpu.client.util.Utils;
import java.util.HashMap;
import java.util.Map;

/**
 * 取件和签收照片后台断点上传
 * @author JPH
 * @date 2015-5-8 下午7:06:06
 */
public class KsudiUpload extends AsyncTask<String, Integer, String> {
	private String localFilePath;
	private final String TAG=KsudiUpload.class.getSimpleName();
	/**异步上传测试**/
//	private String strSerUrl = "http://122.226.100.115:8080/ksudi-upload/";
	/**异步上传正式**/
	private String strSerUrl = "http://pic.ksudi.com:8025/ksudi-upload/";
	private Context context;
	private BreakPointUploadUtil uploadUtil;
	public KsudiUpload(Context context,String localFilePath) {
		this.localFilePath=localFilePath;
		this.context=context;
		uploadUtil=new BreakPointUploadUtil();
	}
	@Override
	protected void onPreExecute() {// 执行前的初始化
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		uploadFile(localFilePath);
		return "success";
	}

	private boolean uploadFile(String localFilePath) {
		String startInfo = uploadUtil.getStartPos(localFilePath, "pickup");// 向服务器获取要上传文件的起点信息
		Map<String, Object> tempMap = (Map<String, Object>) GsonUtil
				.convertJson2Object(startInfo, HashMap.class,
						GsonUtil.JSON_JAVABEAN);
		long codeStr = Utils.doubleObjectToLong(tempMap.get("code"));// 返回状态码
		if (codeStr != 0) {// 网络异常
			Log.i(TAG,startInfo);
			return false;
		}
		long strStart = Utils.doubleObjectToLong(tempMap.get("startsize"));// 起点位置
		long strTot = Utils.doubleObjectToLong(tempMap.get("totsize"));// 总计大小
		String saveName = (String) tempMap.get("savename");// 服务器保存的文件名
		Log.i(TAG,"filename:" + saveName);
		if (codeStr == 0) {// 获取起点位置成功
			boolean isContinue = true;
			while (isContinue) {
				Log.i(TAG,"begin upload");
				String uploadResult = uploadUtil.upload(localFilePath,"pickup", saveName,
						strStart, strTot);
				Log.i(TAG,"上传返回值:" + uploadResult);
				Map<String, Object> resultMap = (Map<String, Object>) GsonUtil
						.convertJson2Object(uploadResult, HashMap.class,
								GsonUtil.JSON_JAVABEAN);
				strStart = Utils.doubleObjectToLong(resultMap.get("start"));
				long uploadResultCode = Utils.doubleObjectToLong(resultMap
						.get("code"));
				if (uploadResultCode == 0) {// 本段上传完成
					if (strStart == -1) {// 此文件的所有部分全部上传完成
						isContinue = false;
						Log.i(TAG,"upload finished");
//						if(!TextUtils.isEmpty((String) resultMap.get("path")))picture.setImgpath((String) resultMap.get("path"));//设置此图片在服务器上保存的路径
						Log.i(TAG,"图片在服务器上保存的路径:"+resultMap.get("path"));
						return true;
					} else {// 继续上传
						Log.i(TAG,"continue upload");
					}
				} else {// 网络异常
					isContinue = false;
				}
			}
		}
		return false;		
	}
	@Override
	protected void onProgressUpdate(Integer... values) {// 执行进度
	}
	@Override
	protected void onPostExecute(String result) {// 执行结果
		Log.i(TAG,"result:" + result);
		super.onPostExecute(result);
	}

}
