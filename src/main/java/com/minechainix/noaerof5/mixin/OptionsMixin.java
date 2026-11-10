package com.minechainix.noaerof5.mixin;

import com.minechainix.noaerof5.NoAeroF5Config;
import com.minechainix.noaerof5.NoAeroF5Helper;
import net.minecraft.client.CameraType;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Guards Options#setCameraType to prevent other mods from forcing non-vanilla camera types onto the player.
 */
@Mixin(value = Options.class, priority = 2000)
public abstract class OptionsMixin {
    @Shadow public CameraType cameraType;

    @Inject(method = "setCameraType", at = @At("HEAD"), cancellable = true)
    private void noAeroF5$preventNonVanillaCameraTypes(CameraType type, CallbackInfo ci) {
        if (NoAeroF5Config.CLIENT.disableCustomCameraTypes.get()) {
            if (type != null && !NoAeroF5Helper.isVanillaCameraType(type)) {
                ci.cancel();
                if (!NoAeroF5Helper.isVanillaCameraType(this.cameraType)) {
                    this.cameraType = CameraType.FIRST_PERSON;
                }
            }
        }
    }
}
