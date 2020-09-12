package com.deepsea.mua.stub.entity;

import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * author : liyaxing
 * date   : 2019/8/21 21:52
 * desc   :
 */
public class DynamicEditSaveBean {

    public String aContent;//动态文字内容
    public String aVoicePath;//动态录音地址
    public List<LocalMedia> aImageList;//动态 图片路径  可能是阿里云路径   可能为本地路径  根据 是否含有http 区分

    @Override
    public String toString() {
        return "DynamicEditSaveBean{" +
                "aContent='" + aContent + '\'' +
                ", aVoicePath='" + aVoicePath + '\'' +
                ", aImageList='" + aImageList + '\'' +
                '}';
    }


}
