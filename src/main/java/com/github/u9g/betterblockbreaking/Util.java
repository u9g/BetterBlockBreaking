package com.github.u9g.betterblockbreaking;

import java.util.LinkedHashMap;
import java.util.Map;

public class Util {
    public static <K,V> LinkedHashMap<K,V> makeMapWithMaxSize (int maxSize) {
        return new LinkedHashMap<K, V>() {
            @Override
            protected boolean removeEldestEntry(final Map.Entry eldest) {
                return size() > maxSize;
            }
        };
    }
}
