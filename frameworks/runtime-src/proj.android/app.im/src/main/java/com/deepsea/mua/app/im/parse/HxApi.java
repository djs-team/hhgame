package com.deepsea.mua.app.im.parse;

import com.deepsea.mua.stub.data.BaseApiResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by JUN on 2019/5/28
 */
public interface HxApi {

    /**
     * 用户信息
     *
     * @param user_id
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/user_info")
    Observable<BaseApiResult<HxUser>> getUser(
            @Field("user_id") String user_id,
            @Field("signature") String signature);
}
