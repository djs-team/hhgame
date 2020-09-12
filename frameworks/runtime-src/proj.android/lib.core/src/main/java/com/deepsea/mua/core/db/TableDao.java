package com.deepsea.mua.core.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.deepsea.mua.core.db.table.TableModel;

import java.util.List;

/**
 * Created by JUN on 2019/3/30
 */
@Dao
public interface TableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TableModel model);

    @Delete
    void delete(TableModel model);

    @Update
    void update(TableModel model);

    @Query("SELECT * FROM TABLEMODEL WHERE `key` = :key")
    TableModel find(String key);

    @Query("SELECT * FROM TABLEMODEL WHERE `key` = :key")
    LiveData<TableModel> findLD(String key);

    @Query("SELECT * FROM TABLEMODEL WHERE `key` = :key")
    List<TableModel> findAll(String key);

    @Query("SELECT * FROM TABLEMODEL WHERE `key` = :key")
    LiveData<List<TableModel>> findAllLD(String key);

    @Query("SELECT * FROM TABLEMODEL WHERE `key` LIKE '%' || :key || '%'")
    List<TableModel> findAllLike(String key);

    @Query("SELECT * FROM TABLEMODEL WHERE `key` LIKE '%' || :key || '%'")
    LiveData<List<TableModel>> findAllLikeLD(String key);
}
