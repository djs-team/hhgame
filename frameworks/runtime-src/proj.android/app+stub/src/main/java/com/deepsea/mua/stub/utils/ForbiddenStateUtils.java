package com.deepsea.mua.stub.utils;

import java.util.HashMap;
import java.util.Map;

public class ForbiddenStateUtils {
    private static Map<String, Boolean> map = new HashMap<>();
    private static Map<String, Boolean> micMap = new HashMap<>();

    public static Map<String, Boolean> getForbiddenLB() {
        return map;
    }

    public static Map<String, Boolean> getForbiddenMicMap() {
        return micMap;
    }

    public static void saveForbiddenLBState(String uid, boolean state) {
        map.put(uid, state);
    }

    public static void saveForbiddenMicState(String uid, boolean state) {
        micMap.put(uid, state);
    }

    public static boolean getForbiddenLBstate(String uid) {
        boolean value = false;
        for (String key : map.keySet()) {
            if (key.equals(uid)) {
                value = map.get(key);
            }
        }
        return value;
    }

    public static boolean getForbiddenMicState(String uid) {
        boolean value = false;
        for (String key : micMap.keySet()) {
            if (key.equals(uid)) {
                value = micMap.get(key);
            }
        }
        return value;
    }

    public static void clearForbiddenData() {
        map.clear();
        micMap.clear();
    }
}
