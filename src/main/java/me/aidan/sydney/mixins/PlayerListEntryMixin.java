package me.aidan.sydney.mixins;

import com.mojang.authlib.GameProfile;
import me.aidan.sydney.Sydney;
import me.aidan.sydney.modules.impl.core.CapesModule;
import me.aidan.sydney.utils.IMinecraft;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin implements IMinecraft {
    @Shadow @Final private GameProfile profile;

    @Inject(method = "getSkinTextures", at = @At("TAIL"), cancellable = true)
    private void getSkinTextures(CallbackInfoReturnable<SkinTextures> info) {
        if (((profile.getName().equals(mc.player.getGameProfile().getName()) && profile.getId().equals(mc.player.getGameProfile().getId()))) && Sydney.MODULE_MANAGER.getModule(CapesModule.class).isToggled() && Sydney.MODULE_MANAGER.getModule(CapesModule.class).getCapeTexture() != null) {
            Identifier identifier = Sydney.MODULE_MANAGER.getModule(CapesModule.class).getCapeTexture();
            SkinTextures texture = info.getReturnValue();

            info.setReturnValue(new SkinTextures(texture.texture(), texture.textureUrl(), identifier, identifier, texture.model(), texture.secure()));
        }
    }
}
