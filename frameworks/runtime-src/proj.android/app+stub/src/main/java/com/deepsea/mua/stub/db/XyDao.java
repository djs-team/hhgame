package com.deepsea.mua.stub.db;

import com.deepsea.mua.stub.entity.GiftBean;

import java.util.List;
import java.util.Map;

/**
 * Created by JUN on 2019/7/29
 */
public class XyDao {

    public static final String TABLE_NAME = "gift";
    public static final String COLUMN_ID = "gift_id";
    public static final String COLUMN_NAME = "gift_name";
    public static final String COLUMN_IMAGE = "gift_image";
    public static final String COLUMN_ANIMATION = "animation";
    public static final String COLUMN_SVGA = "gift_animation";
    public static final String COLUMN_CLASS_TYPE = "gift_class_type";

    private static volatile XyDao sInstance;

    private XyDao() {
    }

    public static XyDao getInstance() {
        if (sInstance == null) {
            synchronized (XyDao.class) {
                if (sInstance == null) {
                    sInstance = new XyDao();
                }
            }
        }
        return sInstance;
    }

    public void saveGifts(List<GiftBean> list) {
        XyDBManager.getInstance().saveGifts(list);
    }

    public Map<String, GiftBean> getGifts() {
        return XyDBManager.getInstance().getGifts();
    }

    public void saveGift(GiftBean bean) {
        XyDBManager.getInstance().saveGift(bean);
    }

    public GiftBean getGift(String giftId) {
        return XyDBManager.getInstance().getGift(giftId);
    }
}
