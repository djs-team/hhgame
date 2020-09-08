/****************************************************************************
Copyright (c) 2008-2010 Ricardo Quesada
Copyright (c) 2010-2012 cocos2d-x.org
Copyright (c) 2011      Zynga Inc.
Copyright (c) 2013-2014 Chukong Technologies Inc.

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 ****************************************************************************/
package org.cocos2dx.javascript;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.happyplay.httpClient.httpClient;
import com.happyplay.util.AYUtil;
import com.happyplay.util.AliDunUtil;
import com.happyplay.util.ClipUtil;
import com.happyplay.util.LiaoBeiUtil;
import com.happyplay.util.LocationManager;
import com.happyplay.util.PermissionUtils;
import com.happyplay.util.TdUtil;
import com.happyplay.util.DlUtil;
import com.happyplay.util.WeChatUtil;
import com.happyplay.util.XianLiaoUtil;
import com.happyplay.util.XinGeUtil;
import com.pocketdigi.utils.FLameUtils;
import com.happyplay.util.DdUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxHelper;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.Manifest;
import com.happyplay.util.EscapeUtil;
import com.qidalin.hy.dlshare.api.IDLAPIEventHandler;
import com.qidalin.hy.dlshare.modelbase.BaseReq;
import com.qidalin.hy.dlshare.modelbase.BaseResp;
import com.qidalin.hy.dlshare.Constant;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tendcloud.tenddata.TDGAAccount;
import com.tendcloud.tenddata.TalkingDataGA;

import java.lang.String;
import org.json.JSONException;
import org.json.JSONObject;
import com.ledong.lib.leto.Leto;

public class AppActivity extends Cocos2dxActivity implements IDLAPIEventHandler{

	public static final String TAG = "xynmmj_AppActivity";

	private BatteryReceiver receiver = null;
	private VibratorUtil vibrato = null;

	// ----------------------------
	private static short[] mBuffer;
	private static AudioRecord mRecorder;
	static String tempFile;
	static String mp3Path;
	static boolean isRecording = false;
	static boolean isStartRecording = false;
	static boolean canSend = true;

	static String roomData = "";
	private static String mBuglyAppID = "b5c4b9b555";

	private static LocationManager map = null;



	private static DdUtil ddutil = null;
	private static TdUtil tdutil = null;
	private static DlUtil dlutil = null;
	private static AYUtil ayUtil = null;

	public static IWXAPI api;
	private static AppActivity instance = null;

	private static PermissionUtils permission = null;
	// phone ->start
	private ArrayList<HashMap<String, String>> contacts;
	private String data;
	// phone ->end

	// ----------------------------
	public static void LogD(String msg) {
		Log.d(TAG, msg);
	}

	public class BatteryReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int current = arg1.getExtras().getInt("level");// 
			int total = arg1.getExtras().getInt("scale");// 
			int percent = current * 100 / total;
			Log.i("battery", "battery " + percent);
			RunJS("nativePower", percent + "");
			unregisterReceiver(receiver);
		}

	}

	@Override
	public void onReq(BaseReq baseReq) {

	}

	@Override
	public void onResp(BaseResp baseResp) {
		finish();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			//失败处理
			return;
		}
		//获取多聊授权登录成功后返回的数据
		if (requestCode == Constant.THIRD_LOGIN) {

			String nickName = data.getStringExtra("nickName");//昵称
			String sex = data.getStringExtra("gender");//性别 0男 1女
			String openId = data.getStringExtra("openId");//多聊唯一id
			String code = data.getStringExtra("code");//临时授权码
			String avatar = data.getStringExtra("originalAvatar");//头像

			JSONObject result = new JSONObject();
			try {
				result.put("code", code);
				result.put("openId", openId);
				result.put("gender", sex);
				result.put("nickName", nickName);
				result.put("originalAvatar", avatar);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			RunJS_obj("DL_USER_LOGIN", result.toString());
		} else if(requestCode == 2 && resultCode == RESULT_OK) {
			// 获取照片返回结果
			Log.e("HelloOC:", "getPictureFromPhone -- 获取照片返回结果 data.getData() = " + data.getData());
			String photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(this,data.getData());
			Log.e("HelloOC:", "getPictureFromPhone -- photoPath::: " + photoPath);
			JSONObject result = new JSONObject();
			try{
				result.put("photoPath", photoPath);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			RunJS_obj("GET_PHOTO_FROM_ALBUM", result.toString());
		}

	}

	/**
	 * 
	 * 
	 * @author Administrator
	 * 
	 */
	public class VibratorUtil {

		/**
		 * final Activity activity  long
		 * milliseconds  long[] pattern
		 */

		public void Vibrate(final Activity activity, long milliseconds) {
			Vibrator vib = (Vibrator) activity
					.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(milliseconds);
		}

		public void Vibrate(final Activity activity, long[] pattern,
				boolean isRepeat) {
			Vibrator vib = (Vibrator) activity
					.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(pattern, isRepeat ? 1 : -1);
		}

	}

	public void RunJS(String name, String param) {

		Cocos2dxHelper.runOnGLThread((new Runnable() {

			String js;
			String para;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.i("weixin", "js:" + js + "para:" + para );
				String command = "cc.eventManager.dispatchCustomEvent('" + js + "','" + para + "' )";
				Log.i("command", command);
				org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge.evalString(command);
			}

			public Runnable setjs(String js, String pa) {
				this.js = js;
				this.para = pa;
				return this;
			}

		}).setjs(name, param));
	}
	
	//param 是{} 对象的
	public void RunJS_obj(String name, String param){
		Cocos2dxHelper.runOnGLThread((new Runnable() { 
			String js;
			String para;
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String command = "cc.eventManager.dispatchCustomEvent('" + js +   "'," + para + ")";
				org.cocos2dx.lib.Cocos2dxJavascriptJavaBridge.evalString(command);
			}
			
			public Runnable setjs(String js, String pa) { 
				this.js = js;
				this.para = pa;
				return this; 
			}
		}).setjs(name, param)); 
	}


	public void getBatteryCount() {
		receiver = new BatteryReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(receiver, filter);
	}

	public void onNativeVibrato(long[] pattern, boolean isRepeat) {
		vibrato.Vibrate(ccActivity, pattern, isRepeat);
	}

    public static void customExitGame() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		ccActivity.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());

		/*if(mlocationClient!=null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
			Log.i("reinitGaoDeSDK","5s 110  onDestroy 3 reInitGaoDeSDK：");
			mlocationClient = null;
			mLocationOption = null;
		}*/
	}
	/**
	 * 启动游戏中心
	 */
	public static void letoMoregames() {
		try
		{
			Leto.getInstance().startGameCenter(ccActivity);
		}
		catch (Exception e)
		{
			Log.i("Leto", "未获取存储权限");
		}

	}


	public static void NativeBattery() {
		ccActivity.getBatteryCount();
	}

	public static void NativeVibrato(String pattern, String isRepeat) {
		Log.i("vib", "1");

		boolean isRepeat_b = false;
		if (isRepeat.equals("true")) {
			isRepeat_b = true;
		}

		String[] strArry = pattern.split(",");
		long[] arry = new long[strArry.length];
		for (int i = 0; i < strArry.length; i++) {
			arry[i] = Long.valueOf(strArry[i]);
		}

		Log.i("vib", "3");

		ccActivity.onNativeVibrato(arry, isRepeat_b);
		Log.i("vib", "4");
	}

	//open url in browser
	public void openURLInBrowser(final String url){
		if(url != null && url.length() > 0){
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(url);
			intent.setData(content_url);
			startActivity(intent);
		}
	}

	
	//重启注册 微信 appID 
	public static void WXRegisterAppId(final String a_AppId){
		WeChatUtil.WXRegisterAppId(a_AppId);
	}

	public static void StartURLImageWxSceneSession(String path, boolean timeLine) {
		WeChatUtil.wxShareURLImage(path,timeLine);
	}
	public static void StartURLImageWxSceneSession(String path)
	{
		StartURLImageWxSceneSession(path, false);
	}


	public static void StartwxShareTexture_Assets_WxSceneSession(String path, boolean timeLine)
	{
	    WeChatUtil.wxShareTexture_Assets(path,timeLine);
	}

	public static void StartwxShareTexture_Assets_StartURLImageWxSceneSession(String path)
	{
		StartwxShareTexture_Assets_WxSceneSession(path, false);
	}


	public static void StartwxShareTexture_Bytes(String path, boolean timeLine)
	{
	    WeChatUtil.wxShareTexture_Bytes(path,timeLine);
	}

	public static void StartwxShareTexture_Bytes(String path)
	{
		StartwxShareTexture_Bytes(path, false);
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.JPEG, 80, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static AppActivity ccActivity;

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	//open url in browser
	public static void StartOpenURLInBrowser(final String url){
		if(ccActivity != null){
			ccActivity.openURLInBrowser(url);
		}
	}

	public static String isWxInstall() {
		return WeChatUtil.isWxInstall() ? "true" : "false";
	}

	public static void StartWxLogin() {
        WeChatUtil.wxLogin();
	}

	public static void StartShareTextWxSceneSession(String path) {
        WeChatUtil.wxShareText(path,false);
	}
	public static void StartShareTextWxSceneSession(String path, String showType) {
		Log.i("weixin", "share");
		if (ccActivity != null) {
			if(!showType.equals("1"))
			{
//				AppActivity.ccActivity.isTimeSession = true;
			}
			ccActivity.wxShareText(path, showType);
		}
	}
	public static void StartShareWebViewWxSceneSession(final String url,
			final String title, final String description) {
		if (ccActivity != null) {
			ccActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
                    WeChatUtil.wxShareWebView(url, title, description,false);
				}
			});
		}
	}

	// weixin sendInvite
	public void wxShareWebView(String url, String title, String description, String showType) {
		if (showType.equals("1")){ // showtype == 1 朋友圈 字串
			WeChatUtil.wxShareWebView(url, title, description,true);
		} else {
			WeChatUtil.wxShareWebView(url, title, description,false);
		}
	}

    public static void StartShareWebViewWxTimeline(String url,String title,String description)
    {
        WeChatUtil.wxShareWebView(url, title, description,true);
    }
    
	public static void StartShareWebViewWxSceneSessionTimeline(final String url,
			final String title,final  String description) {
		if (ccActivity != null) {
			ccActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
                    WeChatUtil.wxShareWebView(url, title, description,true);
				}
			});
		}
	}

	// 微信分享邀请 --add for henanmj
	public static void StartShareWebViewWxSceneSession(final String url, final String title, final String description, final String showType) {
		if (ccActivity != null) {
			ccActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					WeChatUtil.wxShareWebView(url, title, description,showType.equals("1") ? true:false );
				}
			});
		}
	}

	//分享图片到微信好友
	public static void StartShareTextureWxSceneSession(final String path) {

		if (ccActivity != null) {
			ccActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
                    WeChatUtil.wxShareTexture(path,false);
				}
			});

		}
	}
	
	public static void StartShareTextureWxSceneSession(String path, boolean timeLine) {
        WeChatUtil.wxShareTexture(path,timeLine );
	}

	// 微信分享图片 add for henanmj
	public static void StartShareTextureWxSceneSession(String path, String showType) {
		WeChatUtil.wxShareTexture(path, showType.equals("1") ? true : false);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
		//将window的背景图设置为空
		getWindow().setBackgroundDrawable(null);
		super.onCreate(savedInstanceState, persistentState);
	}

	//分享图片到朋友圈
	public static void StartShareTextureWXSceneTimeline(final String path){

		if (ccActivity != null) {
			ccActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
                    WeChatUtil.wxShareTexture(path,true);
				}
			});

		}
	}

	/**
	 * 
	 * 
	 * @param fileName
	 *            {String} 
	 * @param url
	 *            {String} 
	 * @param eventName
	 *            {String} 
	 * */
	public static void uploadFile(final String fileName, final String url,  final String eventName) {
		new Thread() {
			public void run() {
				httpClient http = new httpClient(fileName, url);
				http.uploadFile();
				Log.i("send:", "send successful");
				if (http.ok == 1) {
					ccActivity.RunJS(eventName, http.filePath);
				}
			}
		}.start();
	}

	public static void uploadPic(final String filePath, final String actionUrlPar ,final String eventName, final String token) {
		new Thread() {
			public void run() {
				httpClient http = new httpClient(filePath,actionUrlPar,eventName,token);
				http.uploadFilePic();
				Log.i("send:", "send successful");
				Log.i("send:", "send successful http.ok = " + http.ok);
                if (http.ok == 1) {
                    ccActivity.RunJS(eventName, http.uploadPicReturnData);
                    Log.i("result 333", "getPictureFromPhone -- result hhhhh = " + http.uploadPicReturnData);
                }
                else if(http.ok == 0)
                {
					Log.i("result 444", "getPictureFromPhone -- result faild ");
					JSONObject result = new JSONObject();
					try{
						result.put("errno", 1);
					} catch (JSONException e) {
						e.printStackTrace();
					}
                    ccActivity.RunJS(eventName, result.toString());
                }
			}
		}.start();
	}

	/**
	 * 
	 * 
	 * @param fileName
	 *            {String} 
	 * @param url
	 *            {String} 
	 * @param eventName
	 *            {String} 
	 * */
	public static void downLoadFile(final String filePath, final String fileName, final String url, final String eventName) {
		new Thread() {
			public void run() {
				httpClient http = new httpClient(filePath, fileName, url);
				http.downLoadFile();
				Log.i("download:", "download successful");
				if (http.ok == 1) {
					ccActivity.RunJS(eventName, http.filePath);
				}else{
					ccActivity.RunJS(eventName, "downloadError");//add by wcx
				}
			}
		}.start();
	}




	/**
	 * 上传图片
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @param contentType 没有传入文件类型默认采用application/octet-stream
	 * contentType非空采用filename匹配默认的图片类型
	 * @return 返回response数据
	 */
	/*@SuppressWarnings("rawtypes")
	public static String formUpload(String urlStr, Map<String, String> textMap,
									Map<String, String> fileMap,String contentType) {
		String res = "";
		HttpURLConnection conn = null;
		// boundary就是request头和上传文件内容的分隔符
		String BOUNDARY = "---------------------------123821742118716";
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
					System.out.println(inputName+","+inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}
			// file
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();

					//没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
					contentType = new MimetypesFileTypeMap().getContentType(file);
					//contentType非空采用filename匹配默认的图片类型
					if(!"".equals(contentType)){
						if (filename.endsWith(".png")) {
							contentType = "image/png";
						}else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
							contentType = "image/jpeg";
						}else if (filename.endsWith(".gif")) {
							contentType = "image/gif";
						}else if (filename.endsWith(".ico")) {
							contentType = "image/image/x-icon";
						}
					}
					if (contentType == null || "".equals(contentType)) {
						contentType = "application/octet-stream";
					}
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					System.out.println(inputName+","+filename);

					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}*/


	public static String startRecord(String filePath, String nameString) {

		if (isStartRecording) {
			return "";
		}

		// String basePath = getFilesDir(ccActivity.getApplicationContext());
		String basePath = filePath; //

		tempFile = basePath + "." + "temp.raw";
		mp3Path = basePath + nameString + ".mp3";
		// 
		initRecorder();
		// 
		isRecording = true;
		isStartRecording = true;
		canSend = true;
		// 
		try {
			mRecorder.startRecording();
		} catch (Exception e) {
			canSend = false;
			requestPermission("android.permission.RECORD_AUDIO");
		}
		//File
		File rawFile = new File(tempFile);
		if (rawFile.exists()) {
			rawFile.delete();
			try {
				rawFile.createNewFile();
			} catch (IOException e) {
				rawFile = null;
				e.printStackTrace();
			}
		}
		// 
		ccActivity.startBufferedWrite(rawFile == null ? new File(tempFile)
		: rawFile);

		Log.i("fanjiaheTest", "startRecord" + tempFile + "||" + mp3Path);

		return mp3Path;
	}

	/**
	 * 
	 */
	public static void endRecord(final String eventName) {

		if (!isStartRecording || !isRecording) {
			return;
		}
		// 
		try {
			isRecording = false;
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

		new Thread() {
			public void run() {
				FLameUtils lameUtils = new FLameUtils(1, 16000, 96);
				lameUtils.raw2mp3(tempFile, mp3Path);
				Log.i("record", "stopRecord" + tempFile + "||" + mp3Path);
				ccActivity.RunJS(eventName, canSend ? mp3Path : null);
				isStartRecording = false;
			}
		}.start();
	}

	/**
	 * 
	 * 
	 * @param context
	 * @return
	 */
	public static String getFilesDir(Context context) {

		File targetDir = null;

		// sd
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// targetDir = context.getExternalFilesDir(null); // not support
			// android 2.1
			targetDir = new File(Environment.getExternalStorageDirectory(),
					"Android/data/" + context.getApplicationInfo().packageName
					+ "/files");
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
		}

		if (targetDir == null || !targetDir.exists()) {
			targetDir = context.getFilesDir();
		}

		return targetDir.getPath();
	}

	/**
	 *AudioRecorder
	 */
	public static void initRecorder() {
		int bufferSize = AudioRecord.getMinBufferSize(16000,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		mBuffer = new short[bufferSize];
		mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
				bufferSize);
	}

	/**
	 * 
	 * 
	 * @param file
	 */
	private void startBufferedWrite(final File file) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataOutputStream output = null;
				try {
					output = new DataOutputStream(new BufferedOutputStream(
							new FileOutputStream(file)));
					while (isRecording) {
						int readSize = mRecorder.read(mBuffer, 0,
								mBuffer.length);

						// for (int i = 0; i < readSize; i++) {
						// mBuffer[i] = (short)(mBuffer[i] * 2); //
						// 
						// }
						for (int i = 0; i < readSize; i++) {
							output.writeShort(mBuffer[i]);
						}
					}
				} catch (IOException e) {
					Toast.makeText(AppActivity.this, e.getMessage(),
							Toast.LENGTH_SHORT).show();
				} finally {
					if (output != null) {
						try {
							output.flush();
						} catch (IOException e) {
							Toast.makeText(AppActivity.this, e.getMessage(),
									Toast.LENGTH_SHORT).show();
						} finally {
							try {
								output.close();
							} catch (IOException e) {
								Toast.makeText(AppActivity.this,
										e.getMessage(), Toast.LENGTH_SHORT)
										.show();
							}
						}
					}
				}
			}
		}).start();
	}


	//通过 阿里盾 获取 IP
	public static String getRemoteIpByAliDun(String groupName) {
		return AliDunUtil.getRemoteIpByAliDun(groupName);
	}



	//获取本地(非线上)apk版本
	public static String getAndroidApkVersion(){
		int currentVesionCode = 0;
		String versionName = "error:";
		PackageManager pm = ccActivity.getPackageManager();
		try{
			PackageInfo info = pm.getPackageInfo(ccActivity.getPackageName(), 0);
			currentVesionCode = info.versionCode;
			versionName = info.versionName;
			Log.i("apk info", "apk_VersionCode="+currentVesionCode);
			Log.i("apk info", "apk_VersionName="+versionName);
		} catch(NameNotFoundException e)
		{

		}
		ccActivity.RunJS("getAndroidApkVersion_back", versionName);
		return versionName;
	}

	public static String getAndroidVersionName(){
		int currentVesionCode = 0;
		String versionName = "";
		PackageManager pm = ccActivity.getPackageManager();
		try{
			PackageInfo info = pm.getPackageInfo(ccActivity.getPackageName(), 0);
			currentVesionCode = info.versionCode;
			versionName = info.versionName;
			Log.i("apk info", "apk_VersionCode="+currentVesionCode);
			Log.i("apk info", "apk_VersionName="+versionName);
		} catch(NameNotFoundException e)
		{

		}
		return versionName;
	}
	public static String getAndroidVersionCode(){
		int currentVesionCode = 0;
		String versionName = "";
		PackageManager pm = ccActivity.getPackageManager();
		try{
			PackageInfo info = pm.getPackageInfo(ccActivity.getPackageName(), 0);
			currentVesionCode = info.versionCode;
			versionName = info.versionName;
			Log.i("apk info", "apk_VersionCode="+currentVesionCode);
			Log.i("apk info", "apk_VersionName="+versionName);
		} catch(NameNotFoundException e)
		{

		}
		String istr_currentVesionCode = "" + currentVesionCode;
		return istr_currentVesionCode;
	}

	/**
	 * 利用安卓反射解决android 9.0 的警告提示
	 */
	private void closeAndroidPDialog(){
		if (Build.VERSION.SDK_INT < 28) { // 27
			//  小于等于27即为9.0及以下, 直接return
			return;
		}
		try {
			Class aClass = Class.forName("android.content.pm.PackageParser$Package");
			Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
			declaredConstructor.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Class cls = Class.forName("android.app.ActivityThread");
			Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
			declaredMethod.setAccessible(true);
			Object activityThread = declaredMethod.invoke(null);
			Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
			mHiddenApiWarningShown.setAccessible(true);
			mHiddenApiWarningShown.setBoolean(activityThread, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.setEnableVirtualButton(false);
		super.onCreate(savedInstanceState);
		// Workaround in https://stackoverflow.com/questions/16283079/re-launch-of-activity-on-home-button-but-only-the-first-time/16447508
		if (!isTaskRoot()) {
			// Android launched another instance of the root activity into an existing task
			//  so just quietly finish and go away, dropping the user back into the activity
			//  at the top of the stack (ie: the last state of this task)
			// Don't need to finish it again since it's finished in super.onCreate .
			return;
		}
		initPermission();
		//关闭多指触控
		this.getGLSurfaceView().setMultipleTouchEnabled(false); // add by wcx 20190413
		ccActivity = this;
		hideSystemUI();
		closeAndroidPDialog();

		WeChatUtil.init(this);

		//AliDunSDK Init ......
		AliDunUtil.init(this);

		ClipUtil.init(this);
//		Bugly.init(getApplicationContext(), mBuglyAppID, true);
		CrashReport.initCrashReport(getApplicationContext(), mBuglyAppID, false);


		
		processRoomData();


		XinGeUtil.init(this);
			
		//baidu百度定位初始化
		map = new LocationManager(ccActivity);

		LiaoBeiUtil.init(this);

		PermissionUtils.getInstance().init(this);


		vibrato = new VibratorUtil();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		//xianLiao SDK part..
		XianLiaoUtil.init(this);

		ddutil = new DdUtil();
		ddutil.init(this);

		tdutil = new TdUtil();
		tdutil.init(this);

		dlutil = new DlUtil();
		dlutil.init(this);
		dlutil.getCurrentInst().handleIntent(getIntent(), ccActivity);

		ayUtil = new AYUtil();
		ayUtil.init(this);

		try {
			String iamhenanmj=new String("false");
			if(iamhenanmj.equals("true")){
				this.writeFileData("location.txt", "has gaode");
				this.writeFileData("iosiap.txt", "has gaode");
				this.writeFileData("jpush.txt", "has gaode");
				this.writeFileData("wxshare.txt", "has gaode");
				this.writeFileData("zogevoice.txt", "has gaode");
				this.writeFileData("wxShareType.txt", "has type");
				this.writeFileData("xianliao.txt", "has xianliao");
				this.writeFileData("liaobei.txt", "has xianliao");
				this.writeFileData("copyRoomID.txt", "has copyRoomID");
				this.writeFileData("talkingData.txt", "has talkingData");
				this.writeFileData("btn_cnChat.txt", "has btn_cnChat");
				this.writeFileData("newVersion20181025.txt", "newVersion20181025");
				this.writeFileData("countApp.txt", "has countApp");
				this.writeFileData("baiduMap.txt", "has baiduMap");
				this.writeFileData("Cocos317Engine.txt", "has Cocos317Engine");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void openWebviewActivity(String url,String orientation){
		if( url.isEmpty())
			return;

		Intent intent = new Intent(this,WebViewActivity.class);
		intent.putExtra("url",url);	
		intent.putExtra("orientation",orientation);	
		startActivity(intent);
	}


	public static void openWebVeiw(final String url,final String orientation) {
		ccActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ccActivity.openWebviewActivity(url,orientation);
			}
		});		
	}
	
	// ---------------------- xian Liao SDK -----------------------
	
	public void doWithoutXLApp(){
		//have no xl app
		Log.i("xianliao","doWithoutXLApp");	
		Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=org.xianliao"));
		startActivity(viewIntent);
	}
	public static void StartXianLiaoLogin() {
		XianLiaoUtil.xianLiaoLogin();
	}
	
	//js调用闲聊分享文本
	public static void StartShareTextToXL(String path) {
		XianLiaoUtil.xlShareText(path);
	}
	
	//js调用闲聊分享游戏邀请
	public static void StartShareInviteGameToXL(String roomId, String roomToken, String title, String description) {
		XianLiaoUtil.xlShareInviteGame(roomId, roomToken, title, description);
	}

	//js调用闲聊分享图片
	public static void StartShareTextureToXL(String path) {
		XianLiaoUtil.xlShareTexture(path);
	}
	
	//Js 调用获取闲聊的邀请信息
	public static void xlGetInviteGameInfo(){
		XianLiaoUtil.GetXLInviteGameInfo();
	}

	public static void StartShareUrlToXL(String url, String title, String description) {
		XianLiaoUtil.xlShareUrl( url, title,  description);
	}

	////////////////////////////xianLiao end////////////////////////////////////
	
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "error " + e.getMessage());
        }
        return apiKey;
    }

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);//must store the new intent unless getIntent() will return the old one

		processRoomData();
		ayUtil.handleOnIntent(intent);
	}

	private void processRoomData(){

		Intent intent = getIntent();  
		String scheme = intent.getScheme();  
		Uri uri = intent.getData();  
		System.out.println("scheme:"+scheme);  
		if (uri != null) {  
			String host = uri.getHost();  
			String dataString = intent.getDataString();  
			
			System.out.println("host:"+host);  
			System.out.println("dataString:"+dataString);  
			
			String mUriStr = "?"+host;
			Uri mUri = Uri.parse(mUriStr);

			roomData = mUri.getQueryParameter("roomdata");
		}  

	}

	public static String getRoomData(){
		System.out.println("getRoomData:"+roomData);
		if(roomData==null){
			return "";
		}else {
			String cpy = "".concat(roomData);
			roomData = "";
			return cpy;
		}
	}

	// add for henanmj
	public static void openWXappOnly(){
		WeChatUtil.openWXApp();
	}
	// wxLogin
	public void wxLogin() {
		WeChatUtil.wxLogin();
	}

	// weixin sendtext
	public void wxShareText(String path, String showType) {
		if (showType.equals("1")) {
			WeChatUtil.wxShareText(path,true);
		} else {
			WeChatUtil.wxShareText(path,false);
		}
	}


	//聊呗
	public static void StartLiaoBeiLogin() {
		LiaoBeiUtil.liaobeiLogin();
	}

	//调用聊呗分享文本
	public static void StartShareTextToLB(String path) {
		LiaoBeiUtil.lbShareText(path);
	}

	//调用聊呗分享URL
	public static void StartShareInviteGameToLB(String url, String title, String description) {
		Log.i("liaobei", " URL 分享。。 ccActivity ==  " + ccActivity);
		LiaoBeiUtil.lbShareInviteGame( url, title,  description);
	}
	
	
	//调用聊呗分享图片
	public static void StartShareTextureToLB(String path) {

		LiaoBeiUtil.lbShareTexture(path);
	}

	//聊呗关联群
	public static void lbRelevanceGroup(final String groupId, final String userId, final String userName, final String callbackUrl) {
		if (ccActivity != null) {
			ccActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LiaoBeiUtil.lbRelevanceGroup(groupId, userId, userName, callbackUrl);
				}
			});
		}
	}

	//聊呗加入群
	public static void lbJoinGroup(final String lbGroupUrl) {
		if (ccActivity != null) {
			ccActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LiaoBeiUtil.lbJoinGroup(lbGroupUrl);
				}
			});
		}
	}

	//聊呗 end

	private void initPermission() {
		//for test, can remove later
		String[] permissions = CheckPermissionUtils.checkPermission(this);
		if (permissions.length == 0) {
			//loadKPAd();
//			startGame();
		} else {
			ActivityCompat.requestPermissions(this, permissions, 100);
		}
	}

	//baidu百度定位 接口
	//重启地图,
	public static void reInitBdMapSDK() {
		if(map != null) {
			map.startLocation();
		}
	}
	// 启动定位
	public static void startLocation() {
		LogD("启动定位");
		map.startLocation();
	}

	// 停止定位
	public static void stopLocation() {
		map.stopLocation();
	}

	// 获取地理信息
	public static String getMapRegeocode() {
		return map.getMapRegeocode();
	}

	// 获取地址
	public static String getFormattedAddress() {
		return map.getCurAddressInfo();
	}

	// 获取纬度坐标
	public static String getLatitudePos() {
		return map.getLatitude();
	}

	// 获取经度坐标
	public static String getLongitudePos() {
		return map.getLongitude();
	}
    
    // 获取经纬度坐标
    public static String getCurLocation() {
        return map.getCurLocation();
    }
	
	// 获取地理信息
	public static String getRadius() {
		return map.getRadius();
	}
	
	// 清楚上次定位信息
	public static void clearCurLocation() {
		 map.clearCurLocation();
	}

	public static boolean hasPermission(String target){
		LogD("调用了权限获取情况：");
		return permission.hasPermission(target);
	}
	
	public static void requestPermission(String target){
		permission.requestPermission(target);
	}
	
	private static void showTips() {
		AlertDialog alertDialog = new AlertDialog.Builder(ccActivity)
				.setTitle("提醒")
				.setMessage("亲,真的不玩了，要退出吗？")
				.setPositiveButton("残忍退出",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(Intent.ACTION_MAIN);
								intent.addCategory(Intent.CATEGORY_HOME);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								ccActivity.startActivity(intent);
								try {
									Leto.getInstance().clearCache(ccActivity);
								}
								catch (Exception e)
								{

								}
								android.os.Process
										.killProcess(android.os.Process.myPid());


							}

						}).setNegativeButton("再玩会儿",

				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create(); // 创建对话框
		alertDialog.show(); // 显示对话框
	}

	@SuppressLint("NewApi")
	private void hideSystemUI() {
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// if (hasFocus) {
		hideSystemUI();
		// }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			//showTips();//
			RunJS("showExitGamePanel", "noPar");
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}
	

	public static void writeToClipboard(String content){
		ClipUtil.writeToClipboard(content);
	}


	public static String getFromClipboard(){
		return  ClipUtil.getFromClipboard();
	}


	//计算两点之间的距离
	public String calculateLineDistance(String latitude1, String longitude1, 
			String latitude2, String longitude2) {

		BigDecimal bLatitude1 = new BigDecimal(latitude1);
		double la1 =  bLatitude1.doubleValue();

		BigDecimal bLongitude1 = new BigDecimal(longitude1);
		double lo1 =  bLongitude1.doubleValue();

		BigDecimal bLatitude2 = new BigDecimal(latitude2);
		double la2 =  bLatitude2.doubleValue();

		BigDecimal bLongitude2 = new BigDecimal(longitude2);
		double lo2 =  bLongitude2.doubleValue();

		float distance = 0;

		LatLng start = new LatLng(la1, lo1);
		LatLng end = new LatLng(la2,lo2);
		distance = AMapUtils.calculateLineDistance(start, end);
		//保留小数点后1位
		DecimalFormat df = new DecimalFormat("0.0");
		String result = df.format(distance);

		Log.e("gaode","玩家A和玩家B之间的距离为："+ distance);

		return result;

	}
	public static String CalculateDistance(String latitude1, String longitude1, 
			String latitude2, String longitude2){

		if(ccActivity!=null)
		{
			return ccActivity.calculateLineDistance(latitude1,longitude1,latitude2,longitude2);
		}

		return "0";
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Cocos2dxHelper.onResume();
		getGLSurfaceView().onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Cocos2dxHelper.onPause();
		getGLSurfaceView().onPause();
	}

	@Override
	public Cocos2dxGLSurfaceView onCreateView() {

		Cocos2dxGLSurfaceView glSurfaceView = new Cocos2dxGLSurfaceView(this);

		// TestCpp should create stencil buffer
		glSurfaceView.setEGLConfigChooser(5, 6, 5, 0, 16, 8);

		return glSurfaceView;
	}
	

	static public String getSignature(){
        Activity activity = ccActivity;
        PackageManager manager = activity.getPackageManager();
        String md5 = "";
        StringBuilder builder = new StringBuilder();
        String signature;
        try {
            PackageInfo packageInfo = manager.getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNATURES);
            /******* 通过返回的包信息获得签名数组 *******/
            android.content.pm.Signature[] signs = packageInfo.signatures;
            android.content.pm.Signature sign = signs[0];
            
            /******* 循环遍历签名数组拼接应用签名 *******/
            for (android.content.pm.Signature aaaaa : signs) {
                builder.append(aaaaa.toString());
            }
            /************** 得到应用签名 **************/
            signature = builder.toString();
            
            Log.i("getSignature", "" + sign);
            md5 = md5( "" + sign.toString());//md5(sign.toString());
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return  md5;
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

	// 1）当orientation为1的时候，第一个设置为设置横屏，不随重力感应器切换方向。第二个为根据手机重力感应，设置横屏的上下方向
	// 2）当orientation为2的时候，第一个设置为设置竖屏，不随重力感应器切换方向。第二个为根据手机重力感应，设置竖屏的上下方向
	//设置手机的旋转方向 1：横屏，2：竖屏，3根据用户朝向
	public static String setOrientation(String orientation){
		String iRet = "-1";
		if(orientation.equals("1") ) {
			ccActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			// ccActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			iRet = "1";
		}else if (orientation.equals("2") ){
			ccActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			//ccActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
			iRet = "2";
		}else if (orientation.equals("3") ) {
			ccActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
			// ccActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
		return iRet;
	}

	public static String getOrientation(){//false:横屏 true:竖屏
		int iv = ccActivity.getRequestedOrientation();
		if(iv == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
			|| iv == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
			|| iv == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
			|| iv == ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE){
			return "false";
		}
		else {
			return "true";
		}
	}

	//钉钉begin
	public static void StartShareUrlToDD(String url, String title,String description) {
		if (ddutil != null) {
			ddutil.ddShareUrl(url,title,description);
		}
	}

	public static void StartShareTextToDD(String content) {
		if (ddutil != null) {
			ddutil.ddShareText(content);
		}
	}

	public static void StartShareTextureToDD(String path) {
		if (ddutil != null) {
			ddutil.ddShareTexture(path);
		}
	}
	//钉钉end

	public static void StartShareUrlToCN(String url, String title,String description) {
	}

	public static void StartShareTextureToCN(String path) {
	}

	public static void tdEvent(String eventName, String eventKey,String eventValue) {
		if (tdutil != null) {
			tdutil.tdEvent(eventName,eventKey,eventValue);
		}
	}

	public static void tdAccount(String uid) {
		if (tdutil != null) {
			tdutil.tdAccount(uid);
		}
	}

	public static String internetStatus(){
		String strNetworkType = "1|wifi";
		ConnectivityManager manager=(ConnectivityManager) ccActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (null == manager) { // 为空则认为无网络
					return "0|当前无网络连接";
			}

		NetworkInfo networkInfo=manager.getActiveNetworkInfo();
			if (networkInfo == null || !networkInfo.isAvailable()) {
					return "0|当前无网络连接";
			}

				// 判断是否为WIFI
			NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (null != wifiInfo) {
					NetworkInfo.State state = wifiInfo.getState();
					if (null != state) {
						if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
								return "1|wifi";
						}
					}
			}

			// 若不是WIFI，则去判断是2G、3G、4G网
			TelephonyManager telephonyManager = (TelephonyManager) ccActivity.getSystemService(Context.TELEPHONY_SERVICE);
			int networkType = telephonyManager.getNetworkType();
			switch (networkType) {
					/*
						GPRS : 2G(2.5) General Packet Radia Service 114kbps
						EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
						UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
						CDMA : 2G 电信 Code Division Multiple Access 码分多址
						EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
						EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
						1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
						HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
						HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
						HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
						IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
						EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
						LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
						EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
						HSPAP : 3G HSPAP 比 HSDPA 快些
					*/
					// 2G网络
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN:
						return "2|2G";
					// 3G网络
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B:
					case TelephonyManager.NETWORK_TYPE_EHRPD:
					case TelephonyManager.NETWORK_TYPE_HSPAP:
						return "2|3G";
					// 4G网络
					case TelephonyManager.NETWORK_TYPE_LTE:
						return "2|4G";
					default:
						return "2|蜂窝网络";
			}
		}

    //获取android 所有包名
    public static String LoadAndroidApps() {
        String strNames = "";
        try {
            List<ResolveInfo> apps = new ArrayList<ResolveInfo>();
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            apps = ccActivity.getPackageManager().queryIntentActivities(intent, 0);

            //for循环遍历ResolveInfo对象获取包名和类名
            int length = apps.size();
            for (int i = 0; i < length; i++) {
                ResolveInfo info = apps.get(i);
                //String cls = info.activityInfo.name.toString();
                //包名中文
                CharSequence cname = info.activityInfo.loadLabel(ccActivity.getPackageManager());
                String name = cname.toString();
                //包名com全拼
                //String packageName = info.activityInfo.packageName;
                //Log.i("xianliao","name:"+name+",packageName:"+packageName);
                //strp=strp+name+","+packageName+"@";
                //apps=\[“今日头条”,”抖音”,”航海王”\]要求这种格式
                if (i == length - 1) {
                    strNames = strNames + "\"" + name + "\"";
                } else {
                    strNames = strNames + "\"" + name + "\",";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strNames;
    }
    public static void getPhoneList() {
        if (ccActivity != null) {
            int i = ContextCompat.checkSelfPermission(ccActivity, Manifest.permission.READ_CONTACTS);
            if (i != PackageManager.PERMISSION_GRANTED) {
                ccActivity.toJsClient();
            } else {
                ccActivity.readContacts();
            }
        }
    }

    private void toJsClient() {
        RunJS("noReadContacts", "no");
    }

    public String getSystemModel() {
        return android.os.Build.MODEL;
    }

    private void readContacts() {
        contacts = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> phoneTypeArray = new HashMap<String, String>();
        phoneTypeArray.put("phoneType", ccActivity.getSystemModel());
        phoneTypeArray.put("phoneType", ccActivity.getSystemModel());
        contacts.add(phoneTypeArray);
        Cursor rawContactsCursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, new String[]{"contact_id", "sort_key"}, null, null, "sort_key");    //按sort_key排序
        if (rawContactsCursor != null) {
            while (rawContactsCursor.moveToNext()) {
                String contactId = rawContactsCursor.getString(0);
                // System.out.println(contactId);
                if (contactId != null) {
                    Cursor dataCursor = getContentResolver().query(
                            ContactsContract.Data.CONTENT_URI, new String[]{"data1", "mimetype"},
                            "contact_id=?", new String[]{contactId}, null);
                    if (dataCursor != null) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        while (dataCursor.moveToNext()) {
                            String data1 = dataCursor.getString(0);
                            String mimetype = dataCursor.getString(1);
                            // Gson gson = new Gson();
                            // final  String json1=gson.toJson(data1);
                            data = data1;
                            if ("vnd.android.cursor.item/phone_v2".equals(mimetype))
                                map.put("phone", data1);
                            else if ("vnd.android.cursor.item/name".equals(mimetype))
                                map.put("name", EscapeUtil.escape(data1));
                        }
                        contacts.add(map);
                    }
                    dataCursor.close();
                }
            }

            JSONArray arr = new JSONArray();
            for (Map<String, String> map : contacts) {
                JSONObject obj = new JSONObject(map);
                arr.put(obj);
            }

            JSONObject result = new JSONObject();
            try {
                result.put("phones", arr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("自定义的json", result.toString());
            RunJS("phoneList", result.toString());
        }
    }

    public static void startDlLogin() {
		dlutil.login();
	}

	public static void startDlShareImage(String path) {
		dlutil.dlShareImage(path);
	}

	public static void startDlShareUrl(String title, String desc, String url) {
		dlutil.dlShareUrl(title, desc, url);
	}

    public static void startDlShareText(String text) {
        dlutil.dlShareText(text);
    }

	public static void startAyScheme(String url) {
		ayUtil.AYScheme(url);
	}


	public static void startAySchemeImage(String url, String path, String param) {
		ayUtil.AYSchemeImage(url, path, param);
	}

	public void writeFileData(String fileName, String message)
			throws IOException {
		try {
			FileOutputStream outputStream = openFileOutput(fileName,
					MODE_PRIVATE);
			byte[] bytes = message.getBytes();
			outputStream.write(bytes);
			outputStream.flush();
			outputStream.close();
			// Toast.makeText(AppActivity.ccActivity, "淇濆瓨鎴愬姛",
			// Toast.LENGTH_LONG)
			// .show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showJavaLog(String lg, String tag) {
		Log.i(tag, lg);
	}

	//talkingData 事件打点
	//{eventName: 事件名，paraKey：事件包含参数的key paraValue：事件包含参数的value}
	//一个事件名可对应多个参数key 一个参数key可对应多个value（最多50个）详情请看官方文档
	public static void talkingDataOnClick(String eventName, String paraKey, String paraValue) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(paraKey, paraValue);
		Log.i("TDLog", eventName);
		TalkingDataGA.onEvent(eventName, map);
	}

	//talkingData 绑定账号
	public static void talkingDataSetAccount(String uId) {
		TDGAAccount account = TDGAAccount.setAccount(uId);
		account.setAccountType(TDGAAccount.AccountType.REGISTERED);
	}

	public static String getNetStauts(){
		String strNetworkType = "";
		ConnectivityManager manager=(ConnectivityManager) ccActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo=manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()){
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				strNetworkType = "WIFI";
			}
			else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				strNetworkType = "mobile";
			}
		}
		return strNetworkType;
	}

	/**
	 * @param mStrID	玩家id
	 * @param mStrName	玩家昵称
	 * @param roomid	房间id -- tableid 需要js前段调用在游戏玩家到齐开始游戏时
	 */
	public static void JoinGameVoiceRoom(final String mStrID, final String mStrName, final String roomid) { }
	//房间说话
	public static void vioceStart() { }
	//房间停止说话
	public static void voiceStop() { }
	//玩家离开房间
	public static void leaveRoom() { }
	//玩家返回房间
	public static void returnRoom() { }

	public static void HelloOC(final String strPar) {
		Log.e("HelloOC:", strPar);
	}
	public static void ShowLogOnJava(final String strPar) {
		Log.e("ShowLogOnJava:", strPar);
	}


	// 启动微信
	public static void LaunchWx(){
		boolean ret = false;
		Log.i("weixin","调起微信");
		if(!api.isWXAppInstalled()){
			// 判断是否安装微信
			Toast.makeText(instance,"没有安装微信，请先安装微信!",Toast.LENGTH_SHORT).show();
			return ;
		}
		if(api.openWXApp()){
			Log.i("weixin","open success");
			ret = true;
		}else{
			Log.i("weixin","open fails");
		}
	}

	// 调用相册获取对应的图片
	public static void getPictureFromPhoneAlbum() {
		Log.i("HelloOC","getPictureFromPhoneAlbum");
		if (ccActivity != null) {
			ccActivity.goPhotoAlbum();
		}
	}

	public void goPhotoAlbum() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, 2);
	}

}
