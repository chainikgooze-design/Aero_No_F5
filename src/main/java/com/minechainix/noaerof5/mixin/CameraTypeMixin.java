package com.minechainix.noaerof5.mixin;

import com.minechainix.noaerof5.NoAeroF5Config;
import net.minecraft.client.CameraType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Enforces strict vanilla cycle order for F5 camera toggles:
 * FIRST_PERSON -> THIRD_PERSON_BACK -> THIRD_PERSON_FRONT -> FIRST_PERSON.
 */
@Mixin(value = CameraType.class, priority = 2000)
public abstract class CameraTypeMixin {

    @Inject(method = "cycle", at = @At("HEAD"), cancellable = true)
    private void noAeroF5$normalCycle(CallbackInfoReturnable<CameraType> cir) {
        if (!NoAeroF5Config.CLIENT.disableCustomCameraTypes.get()) {
            return;
        }

        CameraType self = (CameraType) (Object) this;
        if (self == CameraType.FIRST_PERSON) {
            cir.setReturnValue(CameraType.THIRD_PERSON_BACK);
        } else if (self == CameraType.THIRD_PERSON_BACK) {
            cir.setReturnValue(CameraType.THIRD_PERSON_FRONT);
        } else {
            // Cycle any third-party or front view directly back to first-person
            cir.setReturnValue(CameraType.FIRST_PERSON);
        }
    }
}
