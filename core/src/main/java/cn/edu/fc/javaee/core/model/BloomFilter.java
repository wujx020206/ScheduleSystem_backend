package cn.edu.fc.javaee.core.model;

import java.util.HashMap;
import java.util.Map;

public class BloomFilter {

    public static final Map<String, String> PRETECT_FILTERS = new HashMap(){
        {
            put("ProductId", "BF");
        }
    };

}
