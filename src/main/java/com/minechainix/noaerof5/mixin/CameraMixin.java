package com.minechainix.noaerof5.mixin;

import com.minechainix.noaerof5.NoAeroF5Config;
import com.minechainix.noaerof5.NoAeroF5Helper;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Intercepts camera setup distance calculations to clamp zoom multipliers applied by contraption seats or moving structures.
 */
@Mixin(value = Camera.class, priority = 2000)
public abstract class CameraMixin {
    @Shadow private Entity entity;

    @ModifyArg(
        method = "setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(F)F"),
        index = 0
    )
    private float noAeroF5$clampDistance(float distance) {
        if (!NoAeroF5Config.CLIENT.disableContraptionZoom.get()) {
            return distance;
        }

        float vanillaDistance = NoAeroF5Helper.getVanillaDistance(this.entity);
        if (distance > vanillaDistance + 0.05F) {
            if (NoAeroF5Helper.isOnContraptionOrShip(this.entity) || NoAeroF5Config.CLIENT.alwaysClampExcessiveDistance.get()) {
                return vanillaDistance;
            }
        }
        return distance;
    }
}
