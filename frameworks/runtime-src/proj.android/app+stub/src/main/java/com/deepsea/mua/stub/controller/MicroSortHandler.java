package com.deepsea.mua.stub.controller;

import android.text.TextUtils;
import android.util.Log;

import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by JUN on 2019/3/26
 */
public class MicroSortHandler {

    private List<MicroOrder> mOrders;

    /**
     * 添加非自由麦申请列表
     *
     * @param sorts
     */
    public synchronized void setNewMicroSorts(List<MicroOrder> sorts) {
        this.mOrders = sorts;
    }

    /**
     * 添加非自由麦申请
     *
     * @param order
     */
    public synchronized void addMicroOrder(MicroOrder order) {
        if (order != null) {
            if (mOrders == null) {
                mOrders = new ArrayList<>();
            }

            mOrders.add(order);
        }
    }

    /**
     * 移除非自由麦申请
     *
     * @param uid
     */
    public synchronized void removeMicroSort(String uid) {
        Log.d("AG_EX_AV","remove_micro"+uid);
        if (mOrders != null) {
            for (int i=0;i<mOrders.size();i++){
                if (TextUtils.equals(mOrders.get(i).getUser().getUserId(), uid)) {
                    mOrders.remove(i);
                }
//                    iterator.remove();
//                    break;
//                }
            }
//            ListIterator<MicroOrder> iterator = mOrders.listIterator();
//            while (iterator.hasNext()) {
//                String userId = iterator.next().getUser().getUserId();
//                if (TextUtils.equals(userId, uid)) {
//                    iterator.remove();
//                    break;
//                }
//            }
        }
    }

    /**
     * 置顶
     *
     * @param uid
     */
    public synchronized void topMicroSort(String uid) {
        if (mOrders != null&&mOrders.size()>1) {
            MicroOrder top = null;
            for (MicroOrder sort : mOrders) {
                if (uid.equals(sort.getUser().getUserId())) {
                    top = sort;
                    break;
                }
            }

            if (top != null) {
                mOrders.remove(top);
                mOrders.add(0, top);
            }
        }
    }

    /**
     * 当前用户在排序中索引
     *
     * @return
     */
    public synchronized int indexOfBySex() {
        int index = 0;
        boolean contains = false;

        User user = UserUtils.getUser();
        if (mOrders != null && user != null) {
            for (MicroOrder order : mOrders) {
                if (TextUtils.equals(user.getSex(), order.getSex() + "")) {
                    index++;

                    if (user.getUid().equals(order.getUser().getUserId())) {
                        contains = true;
                        break;
                    }
                }
            }
        }

        if (!contains) {
            index = 0;
        }

        return index;
    }

    /**
     * 是否已经排麦
     */
    public synchronized boolean sortContainsMySelf() {
        User user = UserUtils.getUser();
        if (mOrders != null && user != null) {
            for (MicroOrder order : mOrders) {
                if (user.getUid().equals(order.getUser().getUserId()))
                    return true;
            }
        }
        return false;
    }

    /**
     * 获取非自由麦申请列表
     *
     * @return
     */
    public synchronized List<MicroOrder> getSorts() {
        return mOrders;
    }

    /**
     * 根据当前用户性别返回排序列表
     *
     * @return
     */
    public synchronized List<MicroOrder> getSortsBySex() {
        List<MicroOrder> list = null;

        if (!CollectionUtils.isEmpty(mOrders)) {
            list = new ArrayList<>();
            for (MicroOrder order : mOrders) {
                if (TextUtils.equals(UserUtils.getUser().getSex(), String.valueOf(order.getSex()))) {
                    list.add(order);
                }
            }
        }

        return list;
    }

    /**
     * 排序列表
     *
     * @return
     */
    public synchronized List<List<MicroOrder>> getOrders() {
        List<List<MicroOrder>> list =new ArrayList<>();
        if (!CollectionUtils.isEmpty(mOrders)) {
            list = new ArrayList<>();

            List<MicroOrder> boys = new ArrayList<>();
            List<MicroOrder> girls = new ArrayList<>();

            for (MicroOrder order : mOrders) {
                if (order.getSex() == 1) {
                    boys.add(order);
                } else {
                    girls.add(order);
                }
            }

            list.add(boys);
            list.add(girls);
        }

        return list;
    }

    public synchronized void clearOrders() {
        if (mOrders != null) {
            mOrders.clear();
            Log.d("AG_EX_AV","clear_micro");
        }
    }
}
