package samrg472.ref;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import samrg472.ref.api.Handler;
import samrg472.ref.blocks.*;
import samrg472.ref.items.REFItemBlock;
import samrg472.ref.network.PacketHandler;
import samrg472.ref.proxies.CommonProxy;
import samrg472.ref.tileentities.T4TE;

import java.io.File;

@Mod(modid = RedstoneEnergyField.MOD_ID, name = "Redstone Energy Field", version = "0.5", useMetadata = false)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {RedstoneEnergyField.CHANNEL}, packetHandler = PacketHandler.class)
public class RedstoneEnergyField {

    public static final String CHANNEL = "REFNETWORK";
    public static final String MOD_ID = "ref";

    @SidedProxy(clientSide = "samrg472.ref.proxies.ClientProxy", serverSide = "samrg472.ref.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Instance(RedstoneEnergyField.MOD_ID)
    public static RedstoneEnergyField instance;

    public static File configDir;

    public static InvisibleBlock invisibleEnergyBlock;
    public static EnergyBlockT1 redstoneEnergyBlockT1;
    public static EnergyBlockT2 redstoneEnergyBlockT2;
    public static EnergyBlockT3 redstoneEnergyBlockT3;
    public static EnergyBlockT4 redstoneEnergyBlockT4;

    public static int range;
    public static int maxRange;
    public static boolean connectEverything;
    public static boolean particleEffects;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File suggestedConfiguration = event.getSuggestedConfigurationFile();

        configDir = suggestedConfiguration.getParentFile();
        Configuration config = new Configuration(suggestedConfiguration);
        config.load();


        int invisibleEnergyBlockID = Handler.addIgnoreID(config.getBlock("Invisible_Block_ID", 500).getInt()),
                redstoneEnergyBlockT1ID = Handler.addIgnoreID(config.getBlock("Energy_Block_Tier_1_ID", 501).getInt()),
                redstoneEnergyBlockT2ID = Handler.addIgnoreID(config.getBlock("Energy_Block_Tier_2_ID", 502).getInt()),
                redstoneEnergyBlockT3ID = Handler.addIgnoreID(config.getBlock("Energy_Block_Tier_3_ID", 503).getInt()),
                redstoneEnergyBlockT4ID = Handler.addIgnoreID(config.getBlock("Energy_Block_Tier_4_ID", 504).getInt());

        Material stone = new Material(MapColor.stoneColor);
        invisibleEnergyBlock = new InvisibleBlock(invisibleEnergyBlockID, Material.air.setReplaceable());
        redstoneEnergyBlockT1 = new EnergyBlockT1(redstoneEnergyBlockT1ID, stone);
        redstoneEnergyBlockT2 = new EnergyBlockT2(redstoneEnergyBlockT2ID, stone);
        redstoneEnergyBlockT3 = new EnergyBlockT3(redstoneEnergyBlockT3ID, stone);
        redstoneEnergyBlockT4 = new EnergyBlockT4(redstoneEnergyBlockT4ID, stone);

        range = config.get(Configuration.CATEGORY_GENERAL, "range", 5, "This controls how large the redstone energy field is and must be an odd number (if an even number is received, it will converted to an odd)").getInt();
        maxRange = config.get(Configuration.CATEGORY_GENERAL, "max_range", 11, "Controls the max range that in-game range controlled blocks can go up to").getInt();
        connectEverything = config.get(Configuration.CATEGORY_GENERAL, "connect_everything", false, "This does not affect the energy field block\nIf you want RP2 wiring to be powered from the field rather than just the block, and dont care if it looks ugly, use this, alternatively you can use a repeater in the field").getBoolean(false);
        particleEffects = config.get(Configuration.CATEGORY_GENERAL, "particle_effects", true, "These particles shows the field the energy field block emits power to\nYou can disable entirely if you have a slower machine").getBoolean(true);

        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initialize();
        MinecraftForge.EVENT_BUS.register(new InteractionHandler());

        GameRegistry.registerBlock(redstoneEnergyBlockT1, REFItemBlock.class, BaseEnergyBlock.Tier.ONE.unlocalizedName);
        GameRegistry.registerBlock(redstoneEnergyBlockT2, REFItemBlock.class, BaseEnergyBlock.Tier.TWO.unlocalizedName);
        GameRegistry.registerBlock(redstoneEnergyBlockT3, REFItemBlock.class, BaseEnergyBlock.Tier.THREE.unlocalizedName);
        GameRegistry.registerBlock(redstoneEnergyBlockT4, REFItemBlock.class, BaseEnergyBlock.Tier.FOUR.unlocalizedName);

        GameRegistry.registerBlock(invisibleEnergyBlock, InvisibleBlock.unlocalizedName);
        GameRegistry.registerTileEntity(T4TE.class, "T4TE");

        addRecipes();
        addDefaultIgnoreList();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("RedPowerWiring")) {
            System.out.println("RedPowerWiring has been found, adding compatibility");
            File RPConfig = new File(configDir, "redpower/redpower.cfg");
            if (!RPConfig.isFile()) {
                System.out.println("Error: Unable to locate your RedPower2 configuration");
                return;
            }
            Configuration config = new Configuration(RPConfig);
            config.load();

            int id = config.get("blocks.base", "microblock.id", 0).getInt();
            Handler.addBlockHandling(id);
            Handler.addIgnoreID(id);
        } else {
            System.out.println("Disabling RedPower compatibility");
        }
    }

    public void addRecipes() {
        ItemStack redstone = new ItemStack(Item.redstone);

        GameRegistry.addRecipe(new ItemStack(redstoneEnergyBlockT1, 1), "iri", "rpr", "iri", 'i', Block.stone, 'r', redstone, 'p', new ItemStack(Item.enderPearl));
        GameRegistry.addRecipe(new ItemStack(redstoneEnergyBlockT2, 1), "iri", "rpr", "iri", 'i', Block.stone, 'r', Item.netherQuartz, 'p', new ItemStack(redstoneEnergyBlockT1));
        GameRegistry.addRecipe(new ItemStack(redstoneEnergyBlockT3, 1), "iri", "rpr", "iri", 'i', Block.stone, 'r', new ItemStack(Item.dyePowder, 1, 4), 'p', new ItemStack(redstoneEnergyBlockT2));
        GameRegistry.addRecipe(new ItemStack(redstoneEnergyBlockT4, 1), "iri", "rpr", "iri", 'i', Block.stone, 'r', Item.eyeOfEnder, 'p', new ItemStack(redstoneEnergyBlockT3));
    }

    public void addDefaultIgnoreList() {
        Handler.addIgnoreID(Block.redstoneWire.blockID);
    }

}
