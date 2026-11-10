package com.minechainix.noaerof5;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for No Aero F5.
 * A lightweight, client-side utility mod designed to preserve standard third-person (F5) camera behavior
 * and prevent external mods (e.g. Create, Create: Aeronautics / Sable) from hijacking camera distance or camera types.
 */
@Mod(value = NoAeroF5.MOD_ID, dist = Dist.CLIENT)
public class NoAeroF5 {
    public static final String MOD_ID = "no_aero_f5";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public NoAeroF5(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, NoAeroF5Config.CLIENT_SPEC);
        LOGGER.info("No Aero F5 initialized - standard third-person camera preserved.");
    }
}
