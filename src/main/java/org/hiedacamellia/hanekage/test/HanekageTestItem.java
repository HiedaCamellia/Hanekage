package org.hiedacamellia.hanekage.test;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.hiedacamellia.hanekage.Hanekage;

public class HanekageTestItem {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Hanekage.MODID);

    public static final DeferredItem<TestItem> TEST_ITEM = ITEMS.register("test_item", () -> new TestItem(new Item.Properties().stacksTo(64)));
}
