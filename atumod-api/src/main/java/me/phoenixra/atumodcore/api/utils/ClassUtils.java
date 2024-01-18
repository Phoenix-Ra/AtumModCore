package me.phoenixra.atumodcore.api.utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {
    public static List<Field> getAllFields(@Nullable Class<?> clazz, @Nullable Class<?> fieldType) {
        List<Field> matchingFields = new ArrayList<>();

        // Check each field to see if its type is assignable from the specified interface
        for (Field field : clazz.getDeclaredFields()) {
            if (fieldType!=null && !fieldType.isAssignableFrom(field.getType())) {
               continue;
            }
            matchingFields.add(field);
        }

        // Recursively process the superclass
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            matchingFields.addAll(getAllFields(clazz.getSuperclass(), fieldType));
        }

        return matchingFields;
    }
}
