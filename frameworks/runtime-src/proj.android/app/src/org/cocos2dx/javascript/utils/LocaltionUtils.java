package org.cocos2dx.javascript.utils;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.utils.AppConstant;

public class LocaltionUtils {
    private OnLocationResultListener mOnLocationListener;
    private static LocaltionUtils instance;

    public interface OnLocationResultListener {
        void onSuccess(LocationVo location);

        void OnFail(int code);
    }

    public static LocaltionUtils getInstance() {
        if (instance == null) {
            instance = new LocaltionUtils();
        }
        return instance;
    }

    //声明AMapLocationClient类对象
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    public void location(Context context, OnLocationResultListener listener) {
        this.mOnLocationListener = listener;
        mLocationClient = new LocationClient(context);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        option.setNeedNewVersionRgc(true);
//可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false
        option.setScanSpan(0);// 设置发起定位请求的间隔时间为5s(小于1秒则一次定位)

        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public void stop() {
//        mLocationClient.stop();
//        instance = null;
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation aMapLocation) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            mLocationClient.stop();
            if (aMapLocation == null || TextUtils.isEmpty(aMapLocation.getCity())) {
                if (mOnLocationListener != null)
                    mOnLocationListener.OnFail(-1);
            } else {
                LocationVo vo = new LocationVo();
                vo.setLongitude(aMapLocation.getLongitude());
                vo.setLatitude(aMapLocation.getLatitude());
                vo.setCity(aMapLocation.getCity() == null ? "" : aMapLocation.getCity());
                vo.setProvince(aMapLocation.getProvince() == null ? "" : aMapLocation.getProvince());
                vo.setArea(aMapLocation.getDistrict() == null ? "" : aMapLocation.getDistrict());
                String province = aMapLocation.getProvince();
                String city = aMapLocation.getCity();
                String area = aMapLocation.getDistrict();
                double latitude = aMapLocation.getLatitude();
                double longitude = aMapLocation.getLongitude();
                if (!TextUtils.isEmpty(province)) {
                    AppConstant.getInstance().getLocationVo().setProvince(province);
                }
                if (!TextUtils.isEmpty(city)) {
                    AppConstant.getInstance().getLocationVo().setCity(city);
                }
                if (!TextUtils.isEmpty(area)) {
                    AppConstant.getInstance().getLocationVo().setArea(area);
                }
                AppConstant.getInstance().getLocationVo().setLatitude(latitude);
                AppConstant.getInstance().getLocationVo().setLongitude(longitude);
                if (mOnLocationListener != null)
                    mOnLocationListener.onSuccess(vo);
            }
        }
    }

}
