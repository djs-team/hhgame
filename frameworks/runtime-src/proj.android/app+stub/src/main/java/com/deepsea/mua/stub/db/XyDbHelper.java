package com.deepsea.mua.stub.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Created by JUN on 2019/7/29
 */
public class XyDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "xy.db";
    private static XyDbHelper instance;

    private static final String GIFT_TABLE_CREATE = "CREATE TABLE "
            + XyDao.TABLE_NAME + " ("
            + XyDao.COLUMN_NAME + " TEXT, "
            + XyDao.COLUMN_IMAGE + " TEXT, "
            + XyDao.COLUMN_ANIMATION + " TEXT, "
            + XyDao.COLUMN_SVGA + " TEXT, "
            + XyDao.COLUMN_CLASS_TYPE + " TEXT, "
            + XyDao.COLUMN_ID + " TEXT PRIMARY KEY);";

    public XyDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static XyDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new XyDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GIFT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + XyDao.TABLE_NAME + " ADD COLUMN " +
                    XyDao.COLUMN_CLASS_TYPE + " TEXT ;");
        }
    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
