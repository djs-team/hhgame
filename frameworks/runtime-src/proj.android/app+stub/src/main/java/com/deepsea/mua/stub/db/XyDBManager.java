package com.deepsea.mua.stub.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.stub.entity.GiftBean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JUN on 2019/7/29
 */
public class XyDBManager {

    private static volatile XyDBManager dbMgr = new XyDBManager();
    private XyDbHelper dbHelper;

    private XyDBManager() {
        dbHelper = XyDbHelper.getInstance(AppUtils.getApp());
    }

    public static synchronized XyDBManager getInstance() {
        if (dbMgr == null) {
            synchronized (XyDbHelper.class) {
                if (dbMgr == null) {
                    dbMgr = new XyDBManager();
                }
            }
        }
        return dbMgr;
    }

    public synchronized void saveGifts(List<GiftBean> list) {
        if (list == null)
            return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(XyDao.TABLE_NAME, null, null);
            for (GiftBean bean : list) {
                ContentValues values = new ContentValues();
                values.put(XyDao.COLUMN_ID, bean.getGift_id());
                values.put(XyDao.COLUMN_NAME, bean.getGift_name());
                values.put(XyDao.COLUMN_IMAGE, bean.getGift_image());
                values.put(XyDao.COLUMN_ANIMATION, bean.getAnimation());
                values.put(XyDao.COLUMN_SVGA, bean.getGift_animation());
                values.put(XyDao.COLUMN_CLASS_TYPE, bean.getClass_type());
                db.replace(XyDao.TABLE_NAME, null, values);
            }
        }
    }

    public synchronized Map<String, GiftBean> getGifts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, GiftBean> resultMap = new LinkedHashMap<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + XyDao.TABLE_NAME, null);
            while (cursor.moveToNext()) {
                String gid = cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_ID));
                GiftBean bean = new GiftBean();
                bean.setGift_id(gid);
                bean.setGift_name(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_NAME)));
                bean.setGift_image(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_IMAGE)));
                bean.setAnimation(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_ANIMATION)));
                bean.setGift_animation(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_SVGA)));
                bean.setClass_type(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_CLASS_TYPE)));

                resultMap.put(gid, bean);
            }
            cursor.close();
        }
        return resultMap;
    }

    public synchronized void saveGift(GiftBean bean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(XyDao.COLUMN_ID, bean.getGift_id());
        values.put(XyDao.COLUMN_NAME, bean.getGift_name());
        values.put(XyDao.COLUMN_IMAGE, bean.getGift_image());
        values.put(XyDao.COLUMN_ANIMATION, bean.getAnimation());
        values.put(XyDao.COLUMN_SVGA, bean.getGift_animation());
        values.put(XyDao.COLUMN_CLASS_TYPE, bean.getClass_type());
        if (db.isOpen()) {
            db.replace(XyDao.TABLE_NAME, null, values);
        }
    }

    public synchronized GiftBean getGift(String giftId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + XyDao.TABLE_NAME + " where " + XyDao.COLUMN_ID + "=?", new String[]{giftId});
        GiftBean bean = null;
        if (cursor.moveToNext()) {
            bean = new GiftBean();
            bean.setGift_id(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_ID)));
            bean.setGift_name(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_NAME)));
            bean.setGift_image(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_IMAGE)));
            bean.setAnimation(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_ANIMATION)));
            bean.setGift_animation(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_SVGA)));
            bean.setClass_type(cursor.getString(cursor.getColumnIndex(XyDao.COLUMN_CLASS_TYPE)));
        }
        cursor.close();

        return bean;
    }
}
