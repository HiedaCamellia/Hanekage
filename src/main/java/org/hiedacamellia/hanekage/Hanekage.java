package org.hiedacamellia.hanekage;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.hiedacamellia.hanekage.client.config.json.HanekageJsonHelper;
import org.hiedacamellia.hanekage.registries.HanekageDataComponent;
import org.hiedacamellia.hanekage.test.HanekageTestItem;
import org.hiedacamellia.hanekage.test.HanekageTestTab;
import org.slf4j.Logger;


@Mod(Hanekage.MODID)
public class Hanekage {

    public static final String MODID = "hanekage";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Hanekage(IEventBus modEventBus, ModContainer modContainer) {
        if(!FMLLoader.isProduction()) {
            HanekageTestItem.ITEMS.register(modEventBus);
            HanekageTestTab.TABS.register(modEventBus);
        }
        HanekageJsonHelper.init();
        HanekageDataComponent.DATA_COMPONENTS.register(modEventBus);
    }

    public static ResourceLocation rl(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }
}
