package dev.byt3.codex.generated;

import com.hypixel.hytale.codec.builder.BuilderCodec;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class UICodecBuilder<T, S extends BuilderCodec.BuilderBase<T, S>> extends BuilderCodec.BuilderBase<T, S> {
    protected UICodecBuilder(Class<T> tClass, Supplier<T> supplier, @Nullable BuilderCodec<? super T> parentCodec) {
        super(tClass, supplier, parentCodec);
    }

    public UICodecBuilder(BuilderCodec<T> originalCodec) {
        super(originalCodec.getInnerClass(), originalCodec.getSupplier(), originalCodec.getParent());
        this.documentation(originalCodec.getDocumentation())
                .codecVersion(originalCodec.getCodecVersion());
        originalCodec.getEntries().forEach((key, fields) -> {
            String uiKey = "@" + key; // Prepend '@' to the key for UI mapping
            entries.put(uiKey, fields);
        });
    }
}