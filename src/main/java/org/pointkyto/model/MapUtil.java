package org.pointkyto.model;

import java.util.Map;

public class MapUtil {
    public static boolean additionalAttributesEquals(
            Map<String, Attribute> attributeMap1,
            Map<String, Attribute> attributeMap2
    ) {
        if (attributeMap1.size() != attributeMap2.size()) {
            return false;
        }
        for (Map.Entry<String, Attribute> entry : attributeMap1.entrySet()) {
            if (!attributeMap2.containsKey(entry.getKey())) {
                return false;
            }
            if (!attributeMap2.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }
}
