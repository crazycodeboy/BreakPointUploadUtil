package com.jph.bpu.client.net;

import com.jph.bpu.client.util.Constant;
import com.jph.bpu.client.util.GsonUtil;
import com.jph.bpu.client.util.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * 断点上传工具类
 *
 * @author JPH
 * @date 2015-5-8 下午7:03:25
 */
public class BreakPointUploadUtil {
	public String CODE = "code";
	/** 和服务器建立连接 **/
	public int STATUS_SUCCESS = 0;
	/** 网络异常 **/
	public int STATUS_NETWORK_ERROR = 1;
	/** 获取数据失败 **/
	public int STATUS_FETCHDATA_ERROR = 2;
	/** 文件已存在 **/
	public int STATUS_FILEISEXISTS = 3;
	/** 分片大小 **/
	private long lPiece = 1024 * 1024 * 10;
	/** 设置socket 超时时长为60s秒 **/
	private final int SO_TIMEOUT = 60 * 1000;
	/**
	 * 获取上传起始点（code: 0 成功！ 1 网球请求异常，请重试！ 2 返回数据失败，请重试！）
	 *
	 * @param localFilePath
	 *            本地文件路径
	 * @param strModuleType
	 *            要将文件上传到的文件名（模块名 id,sign,image,jzimage,pickup）
	 * @return 如：{"start":8338974,"path":
	 *         "/home/software/ftp/pic/2015/04/dir/351f0914cb1161e2f40b5f50dc23c955.zip"
	 *         ,"fileName":"351f0914cb1161e2f40b5f50dc23c955.zip","code":1}
	 * @author JPH
	 * @date 2015-4-28 下午1:57:12
	 */
	@SuppressWarnings("unchecked")
	public String getStartPos(String localFilePath, String strModuleType) {
		HttpClient httpclient = new DefaultHttpClient(); // 开启一个客户端 HTTP 请求
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		// httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECTION_TIMEOUT);//
		// 设置连接超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				SO_TIMEOUT);// 设置socket 超时时间
		HttpResponse response = null;
		HttpEntity httpEntity = null;
		String strResponseContent = null;
		Map<String, Object> resulMap = new HashMap<String, Object>();
		try {
			File file = new File(localFilePath); // 初始化 File
			String localFileName = file.getName();
			StringBuffer sbUrl = new StringBuffer(Constant.strSerUrl);
			sbUrl.append("upload?name=")
					.append(localFileName)
					.append("&dirtype=")
					.append(strModuleType)
					.append("&type=")
					.append(localFileName.substring(localFileName
							.lastIndexOf('.') + 1)).append("&size=")
					.append(file.length()).append("&modified=")
					.append(file.lastModified());
			HttpGet httpGet = new HttpGet(sbUrl.toString());
			response = httpclient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				httpEntity = response.getEntity();
				if (httpEntity != null) {
					strResponseContent = EntityUtils.toString(httpEntity,
							"utf8");
					Map<String, Object> map = (Map<String, Object>) GsonUtil
							.convertJson2Object(strResponseContent,
									HashMap.class, GsonUtil.JSON_JAVABEAN);
					if (Utils.doubleObjectToLong(map.get("code")) == 1) { // 服务器端处理成功
						resulMap.put(CODE, STATUS_SUCCESS);
						resulMap.put("savename", map.get("fileName"));
						// resulMap.put("path", map.get("path"));
						resulMap.put("startsize", map.get("start"));
						resulMap.put("totsize", String.valueOf(file.length()));
					} else {
						resulMap.put(CODE, STATUS_FETCHDATA_ERROR);
						resulMap.put("msg", map.get("msg"));
					}
				} else {
					resulMap.put(CODE, STATUS_FETCHDATA_ERROR);
				}
			} else {
				resulMap.put(CODE, STATUS_NETWORK_ERROR);
			}
			file = null;
		} catch (Exception e) {
			e.printStackTrace();
			resulMap.put(CODE, STATUS_NETWORK_ERROR);
		} finally {
			if (httpclient != null && httpclient.getConnectionManager() != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
		return GsonUtil.convertObject2Json(resulMap);
	}

	/**
	 * 断点续传（code: 0 成功！ 1 网球请求异常，请重试！ 2 返回数据失败，请重试！）
	 *
	 * @param localFilePath
	 *            本地文件路径
	 * @param strModuleType
	 *            要将文件上传到的文件名（模块名 id,sign,image,jzimage,pickup）
	 * @param strReturnFileName
	 *            远程文件名从getStartPos返回值中得到
	 * @param strStartSize
	 *            起始字节数 1234(字节)
	 * @param strTotSize
	 *            文件总大小 234433(字节)
	 * @return {"code":"0","start":-1}code=0表示连接服务器成功，start=-1 表示上传完成
	 * @author JPH
	 * @date 2015-4-28 下午2:12:34
	 */
	@SuppressWarnings("unchecked")
	public String upload(String localFilePath, String strModuleType,
						 String strReturnFileName, long strStartSize, long strTotSize) {
		// 定义全局变量
		HttpClient httpclient = new DefaultHttpClient();// 开启一个客户端 HTTP 请求
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		// httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,CONNECTION_TIMEOUT);//
		// 设置连接超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				SO_TIMEOUT);// 设置socket 超时时间
		HttpPost httppost = null;
		HttpResponse response = null;
		HttpEntity httpEntity = null;
		String strResponseContent = null;
		Map<String, Object> map = null;
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			StringBuffer sbUrl = new StringBuffer(Constant.strSerUrl);
			sbUrl.append("upload?saveName=").append(strReturnFileName)
					.append("&dirtype=").append(strModuleType);
			httppost = new HttpPost(sbUrl.toString());

			// 计算本次上传的范围
			long lStart = strStartSize;
			long lEnd = lStart + lPiece;
			long lTotSize = strTotSize;

			if (lEnd >= lTotSize) {
				lEnd = lTotSize;
			}

			if (lStart == lTotSize) {
				retMap.put("start", -1);
				retMap.put(CODE, STATUS_SUCCESS);
				return GsonUtil.convertObject2Json(retMap);
			}

			String strRange = strStartSize + "-" + String.valueOf(lEnd) + "/"
					+ strTotSize;
			strRange = "bytes " + strRange;
			httppost.addHeader("Content-Range", strRange);

			// 把文件一定范围内的字节数据放到字节数组中
			int iLenght = (int) (lEnd - lStart);
			byte[] bytes = new byte[iLenght];
			RandomAccessFile raf = new RandomAccessFile(localFilePath, "r");// 负责读取数据
			raf.seek(lStart);
			raf.read(bytes, 0, (int) iLenght);
			raf.close();

			ByteArrayEntity bEntity = new ByteArrayEntity(bytes);
			httppost.setEntity(bEntity);

			response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				httpEntity = response.getEntity();
				if (httpEntity != null) {
					strResponseContent = EntityUtils.toString(httpEntity,
							"utf8");
					map = (Map<String, Object>) GsonUtil.convertJson2Object(
							strResponseContent, HashMap.class,
							GsonUtil.JSON_JAVABEAN);

					if (Utils.doubleObjectToLong(map.get("code")) == 1) { // 服务器端处理成功
						retMap.put("path", map.get("path"));
						retMap.put("start", map.get("start"));
						retMap.put(CODE, STATUS_SUCCESS);
					} else {
						retMap.put(CODE, STATUS_FETCHDATA_ERROR);
						retMap.put("msg", map.get("msg"));
					}

				} else {
					retMap.put(CODE, STATUS_FETCHDATA_ERROR);
				}
			} else {
				retMap.put(CODE, STATUS_NETWORK_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put(CODE, STATUS_NETWORK_ERROR);
		} finally {
			if (httpclient != null && httpclient.getConnectionManager() != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}
		return GsonUtil.convertObject2Json(retMap);
	}
}
