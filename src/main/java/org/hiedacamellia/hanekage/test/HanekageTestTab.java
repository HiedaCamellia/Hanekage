package org.hiedacamellia.hanekage.test;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.hiedacamellia.hanekage.Hanekage;

public class HanekageTestTab {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Hanekage.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HANEKAGE_TEST_TAB = TABS.register("",()-> CreativeModeTab.builder()
            .title(Component.literal("test"))
            .icon(() -> HanekageTestItem.TEST_ITEM.get().getDefaultInstance())
            .displayItems((params, output) -> {
                output.accept(HanekageTestItem.TEST_ITEM.get());
            })
            .build());
}
