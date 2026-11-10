package com.minechainix.noaerof5.client;

import com.minechainix.noaerof5.NoAeroF5;
import com.minechainix.noaerof5.NoAeroF5Config;
import com.minechainix.noaerof5.NoAeroF5Helper;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.CalculateDetachedCameraDistanceEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/**
 * Client-side event listeners for camera validation and distance calculations.
 */
@EventBusSubscriber(modid = NoAeroF5.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    /**
     * Reverts non-vanilla camera types back to first-person if enforced by external sources.
     */
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options != null && NoAeroF5Config.CLIENT.disableCustomCameraTypes.get()) {
            CameraType current = mc.options.getCameraType();
            if (current != null && !NoAeroF5Helper.isVanillaCameraType(current)) {
                mc.options.setCameraType(CameraType.FIRST_PERSON);
            }
        }
    }

    /**
     * Intercepts NeoForge camera distance calculation to ensure standard player camera distance
     * when riding Create contraptions or Sable ships.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCalculateDetachedCameraDistance(CalculateDetachedCameraDistanceEvent event) {
        if (!NoAeroF5Config.CLIENT.disableContraptionZoom.get()) return;

        Entity entity = event.getCamera().getEntity();
        if (entity == null) return;

        float vanillaDistance = NoAeroF5Helper.getVanillaDistance(entity);
        if (event.getDistance() > vanillaDistance + 0.05F) {
            if (NoAeroF5Helper.isOnContraptionOrShip(entity) || NoAeroF5Config.CLIENT.alwaysClampExcessiveDistance.get()) {
                event.setDistance(vanillaDistance);
            }
        }
    }
}
