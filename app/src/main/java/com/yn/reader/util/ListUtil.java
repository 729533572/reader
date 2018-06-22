package com.yn.reader.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhe on 2018/5/25.
 */

public class ListUtil {
    public static List reverse(List list) {
        List ls = new ArrayList();
        for (int i = list.size() - 1; i >= 0; i--) {
            Object obj = list.get(i);
            ls.add(obj);
        }
        return ls;
    }
}
