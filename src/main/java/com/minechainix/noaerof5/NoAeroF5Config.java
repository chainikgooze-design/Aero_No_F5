package com.minechainix.noaerof5;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Client-side configuration for No Aero F5.
 * Provides fine-grained toggles for camera distance clamping and custom camera mode prevention.
 */
public class NoAeroF5Config {
    public static final ClientConfig CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;

    static {
        Pair<ClientConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = pair.getLeft();
        CLIENT_SPEC = pair.getRight();
    }

    public static class ClientConfig {
        public final ModConfigSpec.BooleanValue disableCustomCameraTypes;
        public final ModConfigSpec.BooleanValue disableContraptionZoom;
        public final ModConfigSpec.BooleanValue alwaysClampExcessiveDistance;
        public final ModConfigSpec.DoubleValue defaultCameraDistance;

        public ClientConfig(ModConfigSpec.Builder builder) {
            builder.push("general");

            disableCustomCameraTypes = builder
                .comment("Prevent F5 from cycling into custom non-vanilla camera types (such as Sable Sub-Level / Ship view).")
                .define("disableCustomCameraTypes", true);

            disableContraptionZoom = builder
                .comment("Prevent third-person camera from zooming out to frame Create contraptions, trains, or Sable ships.")
                .define("disableContraptionZoom", true);

            alwaysClampExcessiveDistance = builder
                .comment("Clamp any third-person camera distance that exceeds standard distance back to normal.")
                .define("alwaysClampExcessiveDistance", true);

            defaultCameraDistance = builder
                .comment("Standard third-person camera distance in blocks (Vanilla is 4.0).")
                .defineInRange("defaultCameraDistance", 4.0, 1.0, 32.0);

            builder.pop();
        }
    }
}
