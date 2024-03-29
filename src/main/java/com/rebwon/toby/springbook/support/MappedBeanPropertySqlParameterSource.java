package com.rebwon.toby.springbook.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

public class MappedBeanPropertySqlParameterSource extends BeanPropertySqlParameterSource {

    private Map<String, String> propertyMap;

    public MappedBeanPropertySqlParameterSource(Object object) {
        super(object);
        this.propertyMap = new HashMap<>();
    }

    public MappedBeanPropertySqlParameterSource(Object object, Map<String, String> propertyMap) {
        super(object);
        this.propertyMap = propertyMap;
    }

    public MappedBeanPropertySqlParameterSource map(String paramName, String mappedPropertyName) {
        propertyMap.put(paramName, mappedPropertyName);
        return this;
    }

    public Object getValue(String paramName) throws IllegalArgumentException {
        String mappedPropertyName = this.propertyMap.get(paramName);
        return mappedPropertyName != null ? super.getValue(mappedPropertyName)
            : super.getValue(paramName);
    }

    public String[] getReadablePropertyNames() {
        String[] propertyNames = super.getReadablePropertyNames();
        String[] mappedPropertyNames = Arrays.copyOf(propertyNames,
            propertyNames.length + propertyMap.size());
        int idx = propertyNames.length;
        for (String s : propertyMap.keySet()) {
            mappedPropertyNames[idx++] = s;
        }
        return mappedPropertyNames;
    }

    public int getSqlType(String paramName) {
        String mappedPropertyName = this.propertyMap.get(paramName);
        return (mappedPropertyName == null) ? super.getSqlType(paramName)
            : super.getSqlType(mappedPropertyName);
    }
}
