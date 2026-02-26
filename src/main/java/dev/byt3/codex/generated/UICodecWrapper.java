package dev.byt3.codex.generated;

import com.hypixel.hytale.codec.builder.BuilderCodec;

public class UICodecWrapper<T> extends BuilderCodec<T> {
    public UICodecWrapper(BuilderCodec<T> originalCodec) {
        super(new UICodecBuilder<>(originalCodec));
    }

    public void merge(T currentData, T data) {
        if (currentData == null || data == null) {
            return;
        }
        Class<?> clazz = data.getClass();
        while (clazz != null && clazz != Object.class) {
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(data);
                    if (value != null) {
                        field.set(currentData, value);
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}