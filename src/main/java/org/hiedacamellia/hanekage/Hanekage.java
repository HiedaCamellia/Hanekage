package org.hiedacamellia.hanekage;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.hiedacamellia.hanekage.client.config.json.HanekageJsonHelper;
import org.hiedacamellia.hanekage.registries.HanekageDataComponent;
import org.slf4j.Logger;


@Mod(Hanekage.MODID)
public class Hanekage {

    public static final String MODID = "hanekage";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Hanekage(IEventBus modEventBus, ModContainer modContainer) {
        HanekageJsonHelper.init();
        HanekageDataComponent.DATA_COMPONENTS.register(modEventBus);
    }

    public static ResourceLocation rl(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }
}
