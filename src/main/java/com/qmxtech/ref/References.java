package com.qmxtech.ref;

import net.minecraftforge.common.config.Configuration;
import com.qmxtech.ref.blocks.*;

public class References {

    public static final String VERSION = "0.8.2-alpha";
    public static final String CHANNEL = "REF";
    public static final String MOD_ID = "REF";
    public static final String MOD_NAME = "Redstone Energy Field";

    public static InvisibleBlock invisibleEnergyBlock;
    public static EnergyBlockT1 redstoneEnergyBlockT1;
    public static EnergyBlockT2 redstoneEnergyBlockT2;
    public static EnergyBlockT3 redstoneEnergyBlockT3;
    public static EnergyBlockT4 redstoneEnergyBlockT4;

    private static References INSTANCE;

    private int range;
    private int maxRange;
    private boolean connectEverything;
    private boolean particleEffects;

    protected References(Configuration config) {
        range = config.get(Configuration.CATEGORY_GENERAL, "range", 2, "Specifies radius of non-tier 4 energy blocks.").getInt();
        maxRange = config.get(Configuration.CATEGORY_GENERAL, "max_range", 5, "Specifies maximum radius of tier 4 energy blocks.").getInt();
        connectEverything = config.get(Configuration.CATEGORY_GENERAL, "connect_everything", false, "Only necessary if you have blocks that can only receive power if directly connected.").getBoolean(false);
        particleEffects = config.get(Configuration.CATEGORY_GENERAL, "particle_effects", true, "Specifies if radius particle field is enabled.").getBoolean(true);
        INSTANCE = this;
    }

    public static int getRange() {
        return INSTANCE.range;
    }

    public static int getMaxRange() {
        return INSTANCE.maxRange;
    }

    public static boolean canConnectEverything() {
        return INSTANCE.connectEverything;
    }

    public static boolean showParticleEffects() {
        return INSTANCE.particleEffects;
    }

}

