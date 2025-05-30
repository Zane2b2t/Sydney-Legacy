package me.aidan.sydney.modules.impl.miscellaneous;

import me.aidan.sydney.Sydney;
import me.aidan.sydney.events.SubscribeEvent;
import me.aidan.sydney.events.impl.PlayerUpdateEvent;
import me.aidan.sydney.modules.Module;
import me.aidan.sydney.modules.RegisterModule;
import me.aidan.sydney.settings.impl.NumberSetting;
import me.aidan.sydney.utils.minecraft.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

//@RegisterModule(name = "TableTroll", description = "Those who know", category = Module.Category.MISCELLANEOUS)
public class TableTrollModule extends Module {
    public NumberSetting limit = new NumberSetting("Limit", "The maximum number of blocks that can be placed each group.", 4, 1, 20);
    public NumberSetting delay = new NumberSetting("Delay", "The delay in ticks between each group of placements.", 0, 0, 20);

    private int ticks = 0;

    @SubscribeEvent
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (ticks < delay.getValue().intValue()) {
            ticks++;
            return;
        }

        int bps = 0;
        for (int i = 0; i < Sydney.WORLD_MANAGER.getRadius(5); i++) {
            if (bps > limit.getValue().intValue()) break;
            BlockPos position = mc.player.getBlockPos().add(Sydney.WORLD_MANAGER.getOffset(i));

            if (!WorldUtils.isPlaceable(position)) continue;
            if (mc.world.getBlockState(position.down()).isReplaceable() || mc.world.getBlockState(position.down()).getBlock() == Blocks.ENDER_CHEST) continue;

            WorldUtils.placeBlock(position, WorldUtils.getDirection(position, false), Hand.MAIN_HAND, false, false);

            ticks = delay.getValue().intValue();
            bps++;
        }
    }
}
