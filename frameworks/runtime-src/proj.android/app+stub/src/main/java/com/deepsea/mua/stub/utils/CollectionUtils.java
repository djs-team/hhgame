package com.deepsea.mua.stub.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by JUN on 2019/8/23
 */
public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> list) {
        return list == null || list.isEmpty();
    }

    public static List<String> sortRankHeads(List<String> list) {
        if (list != null) {
            if (list.size() == 1) {
                return list;
            } else if (list.size() == 2) {
                Collections.reverse(list);
                return list;
            } else if (list.size() == 3) {
                List<String> newHeads = new ArrayList<>();
                newHeads.add(list.get(1));
                newHeads.add(list.get(0));
                newHeads.add(list.get(2));
                return newHeads;
            } else {
                return list;
            }
        } else {
            return list;
        }

    }
}
