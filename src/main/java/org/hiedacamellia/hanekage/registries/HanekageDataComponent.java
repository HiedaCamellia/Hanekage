package org.hiedacamellia.hanekage.registries;

import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.hiedacamellia.hanekage.Hanekage;

public class HanekageDataComponent {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS= DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Hanekage.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<java.util.UUID>> UUID = DATA_COMPONENTS.registerComponentType(
            "uuid",
            builder -> builder
                    .persistent(UUIDUtil.CODEC)
                    .networkSynchronized(UUIDUtil.STREAM_CODEC)
    );
}
