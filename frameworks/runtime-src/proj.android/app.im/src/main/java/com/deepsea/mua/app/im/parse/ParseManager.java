package com.deepsea.mua.app.im.parse;

import android.content.Context;
import android.text.TextUtils;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.deepsea.mua.core.utils.LogUtils;
import com.deepsea.mua.core.utils.MD5Utils;
import com.deepsea.mua.im.domain.EaseUser;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpHelper;
import com.deepsea.mua.stub.utils.UserUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ParseManager {

    private static final String TAG = ParseManager.class.getSimpleName();

    private static final String ParseAppID = "UUL8TxlHwKj7ZXEUr2brF3ydOxirCXdIj9LscvJs";
    private static final String ParseClientKey = "B1jH9bmxuYyTcpoFfpeVslhmLYsytWTxqYqKQhBJ";

    private static final String CONFIG_TABLE_NAME = "hxuser";
    private static final String CONFIG_USERNAME = "username";
    private static final String CONFIG_NICK = "nickname";
    private static final String CONFIG_AVATAR = "avatar";

    private static final String parseServer = "http://parse.easemob.com/parse/";

    private static ParseManager instance = new ParseManager();


    private ParseManager() {
    }

    public static ParseManager getInstance() {
        return instance;
    }

    public void onInit(Context context) {
        Context appContext = context.getApplicationContext();
//		Parse.enableLocalDatastore(appContext);
//		Parse.initialize(new Parse.Configuration.Builder(appContext)
//		        .applicationId(ParseAppID)
//		        .server(parseServer)
//		        .build());
    }

    public boolean updateParseNickName(final String nickname) {
//		String username = EMClient.getInstance().getCurrentUser();
//		ParseQuery<ParseObject> pQuery = ParseQuery.getQuery(CONFIG_TABLE_NAME);
//		pQuery.whereEqualTo(CONFIG_USERNAME, username);
//		ParseObject pUser = null;
//		try {
//			pUser = pQuery.getFirst();
//			if (pUser == null) {
//				return false;
//			}
//			pUser.put(CONFIG_NICK, nickname);
//			pUser.save();
//			return true;
//		} catch (ParseException e) {
//			if(e.getCode()==ParseException.OBJECT_NOT_FOUND){
//				pUser = new ParseObject(CONFIG_TABLE_NAME);
//				pUser.put(CONFIG_USERNAME, username);
//				pUser.put(CONFIG_NICK, nickname);
//				try {
//					pUser.save();
//					return true;
//				} catch (ParseException e1) {
//					e1.printStackTrace();
//					EMLog.e(TAG, "parse error " + e1.getMessage());
//				}
//
//			}
//			e.printStackTrace();
//			EMLog.e(TAG, "parse error " + e.getMessage());
//		} catch(Exception e) {
//			EMLog.e(TAG, "updateParseNickName error");
//			e.printStackTrace();
//		}
        return false;
    }

    public String uploadParseAvatar(byte[] data) {
//		String username = EMClient.getInstance().getCurrentUser();
//		ParseQuery<ParseObject> pQuery = ParseQuery.getQuery(CONFIG_TABLE_NAME);
//		pQuery.whereEqualTo(CONFIG_USERNAME, username);
//		ParseObject pUser = null;
//		try {
//			pUser = pQuery.getFirst();
//			if (pUser == null) {
//				pUser = new ParseObject(CONFIG_TABLE_NAME);
//				pUser.put(CONFIG_USERNAME, username);
//			}
//			ParseFile pFile = new ParseFile(data);
//			pUser.put(CONFIG_AVATAR, pFile);
//			pUser.save();
//			return pFile.getUrl();
//		} catch (ParseException e) {
//			if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
//				try {
//					pUser = new ParseObject(CONFIG_TABLE_NAME);
//					pUser.put(CONFIG_USERNAME, username);
//					ParseFile pFile = new ParseFile(data);
//					pUser.put(CONFIG_AVATAR, pFile);
//					pUser.save();
//					return pFile.getUrl();
//				} catch (ParseException e1) {
//					e1.printStackTrace();
//					EMLog.e(TAG, "parse error " + e1.getMessage());
//				}
//			} else {
//				e.printStackTrace();
//				EMLog.e(TAG, "parse error " + e.getMessage());
//			}
//		} catch(Exception e) {
//			EMLog.e(TAG, "uploadParseAvatar error");
//			e.printStackTrace();
//		}
        return null;
    }

    private AtomicInteger mErrorNum;

    public void getContactInfos(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
        List<EaseUser> list = Collections.synchronizedList(new ArrayList<>());
        if (usernames == null) {
            callback.onSuccess(list);
            return;
        }

        mErrorNum = new AtomicInteger(0);
        for (int i = 0; i < usernames.size(); i++) {
            asyncGetUserInfo(usernames.get(i), new EMValueCallBack<EaseUser>() {
                @Override
                public void onSuccess(EaseUser easeUser) {
                    list.add(easeUser);

                    LogUtils.e(mErrorNum.intValue(), list.size(), usernames.size(), easeUser.getAvatar());

                    if (mErrorNum.intValue() + list.size() == usernames.size()) {
                        callback.onSuccess(list);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    int num = mErrorNum.getAndIncrement();

                    if (num + list.size() == usernames.size()) {
                        callback.onSuccess(list);
                    }
                }
            });
        }
    }

    public void asyncGetCurrentUserInfo(final EMValueCallBack<EaseUser> callback) {
        final String username = EMClient.getInstance().getCurrentUser();
        asyncGetUserInfo(username, callback);
    }

    public void asyncGetUserInfo(String username, EMValueCallBack<EaseUser> callback) {
        if (UserUtils.getUser() == null) {
            if (callback != null) {
                callback.onError(0, "");
            }
            return;
        }

        String token = UserUtils.getUser().getToken();
        String signature = MD5Utils.getMD5(token + username);
        if (!TextUtils.isEmpty(signature)) {
            signature = signature.toLowerCase();
        }
        HttpHelper.instance().getApi(HxApi.class).getUser(username, signature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseApiResult<HxUser>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseApiResult<HxUser> result) {
                        if (result.getCode() == 200) {
                            HxUser.UserInfoBean info = result.getData().getUser_info();

                            EaseUser user = new EaseUser(username);
                            user.setNickname(info.getNickname());
                            user.setAvatar(info.getAvatar());

                            callback.onSuccess(user);
                        } else {
                            callback.onError(result.getCode(), result.getDesc());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(-100, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
