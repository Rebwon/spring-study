package com.rebwon.toby.springbook.web.converter;

import com.rebwon.toby.springbook.domain.Type;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class TypeConverter {

    private TypeConverter() {
    }

    @Component
    public static class TypeToString implements Converter<Type, String> {

        public String convert(Type type) {
            return (type == null) ? "" : String.valueOf(type.getValue());
        }
    }

    @Component
    public static class StringToType implements Converter<String, Type> {

        public Type convert(String text) {
            return Type.valueOf(Integer.valueOf(text));
        }
    }
}
