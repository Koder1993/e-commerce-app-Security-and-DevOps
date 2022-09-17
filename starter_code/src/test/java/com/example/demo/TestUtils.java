package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject)
            throws NoSuchFieldException, IllegalAccessException {
        boolean wasPrivate = false;
        Field field = target.getClass().getDeclaredField(fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
            wasPrivate = true;
        }
        field.set(target, toInject);

        if (wasPrivate) {
            field.setAccessible(false);
        }
    }
}
