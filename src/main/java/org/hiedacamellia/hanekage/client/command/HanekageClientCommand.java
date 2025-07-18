package org.hiedacamellia.hanekage.client.command;


import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import org.hiedacamellia.hanekage.Hanekage;
import org.hiedacamellia.hanekage.client.config.json.SwordTrailConfig;

@EventBusSubscriber(Dist.CLIENT)
public class HanekageClientCommand {

    @SubscribeEvent
    public static void register(RegisterClientCommandsEvent event) {

        event.getDispatcher().register(Commands.literal(Hanekage.MODID).then(Commands.literal("sword_trail")
                .then(Commands.literal("reload").executes(
                        context -> {
                            SwordTrailConfig.reload();
                            return 1;
                        }
                ))
        ));
    }

}
