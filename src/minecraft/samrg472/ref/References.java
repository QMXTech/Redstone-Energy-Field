package samrg472.ref;

import net.minecraftforge.common.Configuration;
import samrg472.ref.blocks.*;

public class References {

    public static final String CHANNEL = "REFNETWORK";
    public static final String MOD_ID = "ref";

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
        range = config.get(Configuration.CATEGORY_GENERAL, "range", 5, "This controls how large the redstone energy field is and must be an odd number (if an even number is received, it will converted to an odd)").getInt();
        maxRange = config.get(Configuration.CATEGORY_GENERAL, "max_range", 11, "Controls the max range that in-game range controlled blocks can go up to").getInt();
        connectEverything = config.get(Configuration.CATEGORY_GENERAL, "connect_everything", false, "This does not affect the energy field block\nIf you want RP2 wiring to be powered from the field rather than just the block, and dont care if it looks ugly, use this, alternatively you can use a repeater in the field").getBoolean(false);
        particleEffects = config.get(Configuration.CATEGORY_GENERAL, "particle_effects", true, "These particles shows the field the energy field block emits power to\nYou can disable entirely if you have a slower machine").getBoolean(true);
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
