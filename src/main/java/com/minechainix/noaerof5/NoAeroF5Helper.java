package com.minechainix.noaerof5;

import net.minecraft.client.CameraType;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Utility methods for validating camera modes, calculating vanilla camera distances,
 * and detecting entity mounts on contraptions, trains, or airship sub-levels.
 */
public class NoAeroF5Helper {

    /**
     * Determines whether the given camera type is one of the three vanilla Minecraft modes.
     */
    public static boolean isVanillaCameraType(CameraType type) {
        if (type == null) return false;
        return type == CameraType.FIRST_PERSON
            || type == CameraType.THIRD_PERSON_BACK
            || type == CameraType.THIRD_PERSON_FRONT;
    }

    /**
     * Calculates the baseline camera distance for an entity, taking model scale into account.
     */
    public static float getVanillaDistance(Entity entity) {
        float scale = entity instanceof LivingEntity living ? living.getScale() : 1.0F;
        float baseDistance = NoAeroF5Config.CLIENT.defaultCameraDistance.get().floatValue();
        return baseDistance * scale;
    }

    /**
     * Checks if the entity is currently mounted on or contained within a Create contraption, train carriage,
     * or a Sable aeronautics sub-level.
     */
    public static boolean isOnContraptionOrShip(Entity entity) {
        if (entity == null) return false;

        if (entity.isPassenger()) {
            Entity vehicle = entity.getVehicle();
            if (vehicle != null) {
                String vehicleClass = vehicle.getClass().getName().toLowerCase(Locale.ROOT);
                if (vehicleClass.contains("contraption")
                    || vehicleClass.contains("seat")
                    || vehicleClass.contains("train")
                    || vehicleClass.contains("carriage")
                    || vehicleClass.contains("sublevel")
                    || vehicleClass.contains("ship")
                    || vehicleClass.contains("sable")) {
                    return true;
                }
            }
        }

        return isSableSubLevel(entity);
    }

    /**
     * Safely checks via reflection whether the entity resides in a Sable sublevel without a hard runtime dependency.
     */
    private static boolean isSableSubLevel(Entity entity) {
        try {
            if (ModList.get().isLoaded("sable")) {
                Class<?> sableClass = Class.forName("dev.ryanhcode.sable.Sable");
                Object helper = sableClass.getField("HELPER").get(null);
                if (helper != null) {
                    Method getVehicleSubLevel = helper.getClass().getMethod("getVehicleSubLevel", Entity.class);
                    Object vehicleSub = getVehicleSubLevel.invoke(helper, entity);
                    if (vehicleSub != null) return true;

                    Method getContaining = helper.getClass().getMethod("getContaining", Level.class, Position.class);
                    Object containingSub = getContaining.invoke(helper, entity.level(), entity.position());
                    if (containingSub != null) return true;
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }
}
