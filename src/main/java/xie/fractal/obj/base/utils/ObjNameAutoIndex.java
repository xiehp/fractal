package xie.fractal.obj.base.utils;

import java.util.HashMap;
import java.util.Map;

public class ObjNameAutoIndex {

    private static Map<String, Index> indexMap = new HashMap<>();

    public static synchronized int getNextIndex(String key) {
        Index index = indexMap.get(key);
        if (index == null) {
            index = new Index();
            indexMap.put(key, index);
        }
        index.next();

        return index.get();
    }

    public static class Index {
        int index = 0;

        public int next() {
            index++;
            return index;
        }

        public int get() {
            return index;
        }
    }
}
