package com.deepsea.mua.core.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.deepsea.mua.core.db.table.TableModel;
import com.deepsea.mua.core.utils.AppUtils;

/**
 * Created by JUN on 2019/3/30
 */
@Database(entities = {TableModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;
    private static final String DB_NAME = "mua.db";

    public static AppDatabase create() {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(
                            AppUtils.getApp(),
                            AppDatabase.class,
                            DB_NAME)
                            .build();
                }
            }
        }
        return mInstance;
    }

    public abstract TableDao tableDao();
}
