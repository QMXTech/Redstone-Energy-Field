package samrg472.ref;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.io.File;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import samrg472.ref.api.Handler;
import samrg472.ref.blocks.*;
import samrg472.ref.proxies.CommonProxy;

@Mod(modid=RedstoneEnergyField.MOD_ID, name="Redstone Energy Field", version="0.5", useMetadata=false)
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class RedstoneEnergyField {

    public static final String MOD_ID = "REF";
    
    @SidedProxy(clientSide="samrg472.ref.proxies.ClientProxy", serverSide="samrg472.ref.proxies.CommonProxy")
    public static CommonProxy proxy;
    
    @Instance("REF")
    public static RedstoneEnergyField instance;
    
    public static File configDir;
    
    public static InvisibleBlock invisibleEnergyBlock;
    public static EnergyBlockT1 redstoneEnergyBlockT1;
    public static EnergyBlockT2 redstoneEnergyBlockT2;
    public static EnergyBlockT3 redstoneEnergyBlockT3;
    
    public static int range;
    public static boolean connectEverything;
    public static boolean particleEffects;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        File suggestedConfiguration = event.getSuggestedConfigurationFile();
        
        configDir = suggestedConfiguration.getParentFile();
        Configuration config = new Configuration(suggestedConfiguration);
        config.load();
        
        
        int invisibleEnergyBlockID = Handler.addIgnoreID(config.getBlock("Invisible_Block_ID", 500).getInt()),
        redstoneEnergyBlockT1ID = Handler.addIgnoreID(config.getBlock("Energy_Block_Tier_1_ID", 501).getInt()),
        redstoneEnergyBlockT2ID = Handler.addIgnoreID(config.getBlock("Energy_Block_Tier_2_ID", 502).getInt()),
        redstoneEnergyBlockT3ID = Handler.addIgnoreID(config.getBlock("Energy_Block_Tier_3_ID", 503).getInt());
        
        invisibleEnergyBlock = new InvisibleBlock(invisibleEnergyBlockID, Material.air.setReplaceable());
        redstoneEnergyBlockT1 = new EnergyBlockT1(redstoneEnergyBlockT1ID, new Material(MapColor.stoneColor));
        redstoneEnergyBlockT2 = new EnergyBlockT2(redstoneEnergyBlockT2ID, new Material(MapColor.stoneColor));
        redstoneEnergyBlockT3 = new EnergyBlockT3(redstoneEnergyBlockT3ID, new Material(MapColor.stoneColor));
        
        range = config.get(Configuration.CATEGORY_GENERAL, "range", 5, "This controls how large the redstone energy field is and must be an odd number (if an even number is received, it will converted to an odd)").getInt();
        connectEverything = config.get(Configuration.CATEGORY_GENERAL, "connect_everything", false, "This does not affect the energy field block\nIf you want RP2 wiring to be powered from the field rather than just the block, and dont care if it looks ugly, use this, alternatively you can use a repeater in the field").getBoolean(false);
        particleEffects = config.get(Configuration.CATEGORY_GENERAL, "particle_effects", true, "These particles shows the field the energy field block emits power to\nYou can disable entirely if you have a slower machine").getBoolean(true);
        
        config.save();
    }
    
    @Init
    public void init(FMLInitializationEvent event) {
        proxy.initialize();
        MinecraftForge.EVENT_BUS.register(new InteractionHandler());
        
        GameRegistry.registerBlock(redstoneEnergyBlockT1, "rsEnergyBlockT1");
        LanguageRegistry.addName(redstoneEnergyBlockT1, "Redstone Energy Field T1");
        
        GameRegistry.registerBlock(redstoneEnergyBlockT2, "rsEnergyBlockT2");
        LanguageRegistry.addName(redstoneEnergyBlockT2, "Redstone Energy Field T2");
        
        GameRegistry.registerBlock(redstoneEnergyBlockT3, "rsEnergyBlockT3");
        LanguageRegistry.addName(redstoneEnergyBlockT3, "Redstone Energy Field T3");
        
        GameRegistry.registerBlock(invisibleEnergyBlock, "invisiblePoweredBlock");
        
        addRecipes();
        addDefaultIgnoreList();
    }
    
    @PostInit
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
        ItemStack iron = new ItemStack(Item.ingotIron);
        ItemStack redstone = new ItemStack(Item.redstone);
        
        GameRegistry.addRecipe(new ItemStack(redstoneEnergyBlockT1, 1), "iri", "rpr", "iri", 'i', iron, 'r', redstone, 'p', new ItemStack(Item.enderPearl));
        GameRegistry.addRecipe(new ItemStack(redstoneEnergyBlockT2, 1), "iri", "rpr", "iri", 'i', iron, 'r', redstone, 'p', new ItemStack(redstoneEnergyBlockT1));
        GameRegistry.addRecipe(new ItemStack(redstoneEnergyBlockT3, 1), "iri", "rpr", "iri", 'i', iron, 'r', redstone, 'p', new ItemStack(redstoneEnergyBlockT2));
    }
    
    public void addDefaultIgnoreList() {
        Handler.addIgnoreID(Block.redstoneWire.blockID);
    }
    
}
