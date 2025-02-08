package me.aidan.sydney.modules.impl.miscellaneous;

import me.aidan.sydney.Sydney;
import me.aidan.sydney.events.SubscribeEvent;
import me.aidan.sydney.events.impl.PlayerUpdateEvent;
import me.aidan.sydney.modules.Module;
import me.aidan.sydney.modules.RegisterModule;
import me.aidan.sydney.settings.impl.BooleanSetting;
import me.aidan.sydney.settings.impl.WhitelistSetting;
import me.aidan.sydney.utils.minecraft.InventoryUtils;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;

@RegisterModule(name = "InventoryCleaner", description = "Drops unwanted items from inventory.", category = Module.Category.MISCELLANEOUS)
public class InventoryCleanerModule extends Module {
    public BooleanSetting hotbar = new BooleanSetting("Hotbar", "Include the hotbar when cleaning the inventory.", false);
    public BooleanSetting require = new BooleanSetting("Require", "Only clean inventory when you are in the inventory screen.", false);
    public WhitelistSetting whitelist = new WhitelistSetting("Whitelist", "The list of whitelisted items.", WhitelistSetting.Type.ITEMS);

    private int ticks = 0;

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (mc.player == null || mc.world == null) return;

        if (ticks <= 0) {
            if (mc.currentScreen instanceof CreativeInventoryScreen || mc.currentScreen instanceof GenericContainerScreen || mc.currentScreen instanceof ShulkerBoxScreen) return;
            if (!(mc.currentScreen instanceof InventoryScreen) && require.getValue() || mc.currentScreen instanceof InventoryScreen && !require.getValue())
                return;

            for (int i = hotbar.getValue() ? 0 : 9; i < 36; i++) {
                if (mc.player.getInventory().getStack(i).isEmpty()) continue;

                Item item = mc.player.getInventory().getStack(i).getItem();
                if (whitelist.isWhitelistContains(item)) continue;

                mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, InventoryUtils.indexToSlot(i), 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, -999, 0, SlotActionType.PICKUP, mc.player);
                ticks = 2 + Sydney.SERVER_MANAGER.getPingDelay();
            }
        }

        ticks--;
    }

    @Override
    public String getMetaData() {
        return String.valueOf(whitelist.getWhitelist().size());
    }
}
