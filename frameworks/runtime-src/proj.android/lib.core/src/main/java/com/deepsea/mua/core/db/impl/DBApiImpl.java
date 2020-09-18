package com.deepsea.mua.core.db.impl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.WorkerThread;

import com.deepsea.mua.core.db.AppDatabase;
import com.deepsea.mua.core.db.IDBApi;
import com.deepsea.mua.core.db.TableDao;
import com.deepsea.mua.core.db.table.TableModel;
import com.deepsea.mua.core.utils.JsonConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2019/3/31
 */
public class DBApiImpl implements IDBApi {

    private TableDao mTableDao;
    private Class<TableModel> mTableClazz;

    public DBApiImpl() {
        mTableDao = AppDatabase.create().tableDao();
        mTableClazz = TableModel.class;
    }

    @Override
    public <T> void insert(T object) {
        if (object != null)
            insert(object.getClass().getName(), object);
    }

    @Override
    public <T> void delete(Class<T> clazz) {
        if (clazz != null)
            delete(clazz.getName());
    }

    @Override
    public <T> LiveData<T> find(Class<T> clazz) {
        if (clazz == null)
            return null;
        return find(clazz.getName(), clazz);
    }

    @WorkerThread
    @Override
    public <T> void insert(String key, T object) {
        if (object == null)
            return;
        try {
            TableModel model = mTableDao.find(key);
            if (model == null) {
                model = mTableClazz.newInstance();
                model.setKey(key);
                model.setValue(JsonConverter.toJson(object));
                mTableDao.insert(model);
            } else {
                model.setValue(JsonConverter.toJson(object));
                mTableDao.update(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @WorkerThread
    @Override
    public void delete(String key) {
        TableModel model = mTableDao.find(key);
        if (model != null) {
            mTableDao.delete(model);
        }
    }

    @Override
    public <T> LiveData<T> find(String key, Class<T> clazz) {
        MediatorLiveData<T> result = new MediatorLiveData<>();
        LiveData<TableModel> source = mTableDao.findLD(key);
        result.addSource(source, model -> {
            result.removeSource(source);
            if (model == null) {
                result.postValue(null);
            } else {
                result.postValue(JsonConverter.fromJson(model.getValue(), clazz));
            }
        });
        return result;
    }

    @Override
    public <T> LiveData<List<T>> findAll(String key, Class<T> clazz, boolean isLike) {
        MediatorLiveData<List<T>> result = new MediatorLiveData<>();
        LiveData<List<TableModel>> source;
        if (isLike) {
            source = mTableDao.findAllLikeLD(key);
        } else {
            source = mTableDao.findAllLD(key);
        }
        result.addSource(source, list -> {
            result.removeSource(source);
            if (list == null || list.isEmpty()) {
                result.postValue(null);
            } else {
                List<T> results = new ArrayList<>();
                for (TableModel tableModel : list) {
                    results.add(JsonConverter.fromJson(tableModel.getValue(), clazz));
                }
                result.postValue(results);
            }
        });
        return result;
    }

    @WorkerThread
    @Override
    public void deleteAll(String key, boolean isLike) {
        List<TableModel> source;
        if (isLike) {
            source = mTableDao.findAllLike(key);
        } else {
            source = mTableDao.findAll(key);
        }
        if (source != null) {
            for (TableModel tableModel : source) {
                mTableDao.delete(tableModel);
            }
        }
    }

    @Override
    public void close() {

    }
}
