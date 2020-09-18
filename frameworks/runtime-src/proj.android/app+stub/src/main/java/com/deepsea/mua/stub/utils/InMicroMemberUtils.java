package com.deepsea.mua.stub.utils;

import android.text.TextUtils;

import com.deepsea.mua.stub.entity.socket.MicroOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class InMicroMemberUtils {
    private static InMicroMemberUtils instance;

    private InMicroMemberUtils() {
    }

    public static InMicroMemberUtils getInstance() {
        if (instance == null) {
            instance = new InMicroMemberUtils();
        }
        return instance;
    }

    private Map<String, String> micMap = new HashMap<>();

    public Map<String, String> getMicMap() {
        return micMap;
    }


    public void saveMicroMembers(String type, String uid) {
        micMap.put(type, uid);
    }

    public void removeMicroMember(String type) {
        micMap.remove(type);
    }

    public boolean judgeMicroHasMember(String sex) {
        if (micMap == null) {
            return false;
        }
        boolean hasMember = micMap.containsKey(sex);
        return hasMember;
    }
    public int judgeMicroHasMember() {
        if (micMap == null) {
            return 0;
        }
        return micMap.size();
    }

    private List<MicroOrder> microOrders = new ArrayList<>();

    public List<MicroOrder> getMicroOrders() {
        return microOrders;
    }

    public void saveMicroNum(List<MicroOrder> microOrders) {
        this.microOrders.clear();
        this.microOrders = microOrders;
    }

    public void removeMicroOrder(String uid) {
        if (microOrders != null) {
            for (int i=0;i<microOrders.size();i++){
                if (TextUtils.equals(microOrders.get(i).getUser().getUserId(), uid)) {
                    microOrders.remove(i);
                }
//                    iterator.remove();
//                    break;
//                }
            }
        }
    }

    public void clear() {
        micMap.clear();
        microOrders.clear();
    }


}
