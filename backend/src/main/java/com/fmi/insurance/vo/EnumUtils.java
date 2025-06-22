package com.fmi.insurance.vo;

public class EnumUtils {
    public static <T extends Enum<T>> T fromString(Class<T> classType, String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        try {
            return Enum.valueOf(classType, type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid fuel type: " + type);
        }
    }
}
