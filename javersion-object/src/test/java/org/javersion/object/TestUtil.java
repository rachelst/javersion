package org.javersion.object;

import java.util.Map;

import org.javersion.path.PropertyPath;

import com.google.common.collect.Maps;

public class TestUtil {

    public static Map<PropertyPath, Object> properties(Object... keysAndValues) {
        Map<PropertyPath, Object> map = Maps.newHashMap();
        for (int i=0; i < keysAndValues.length-1; i+=2) {
            PropertyPath propertyPath;
            Object path = keysAndValues[i];
            if (path instanceof String) {
                propertyPath = PropertyPath.parse((String) path);
            } else {
                propertyPath = (PropertyPath) path;
            }
            map.put(propertyPath, keysAndValues[i+1]);
        }
        return map;
    }

    public static PropertyPath property(String path) {
        return PropertyPath.parse(path);
    }

}
