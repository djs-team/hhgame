/****************************************************************************
Copyright (c) 2015-2016 Chukong Technologies Inc.
Copyright (c) 2017-2018 Xiamen Yaji Software Co., Ltd.
 
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxHelper;
import org.json.JSONException;
import org.json.JSONObject;


import com.hhgame.httpClient.httpClient;

public class AppActivity extends Cocos2dxActivity {

    public static AppActivity ccActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // DO OTHER INITIALIZATION BELOW
        ccActivity = this;


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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            //失败处理
            return;
        }
      if(requestCode == 2 && resultCode == RESULT_OK) {
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
