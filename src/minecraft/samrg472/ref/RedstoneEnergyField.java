package samrg472.ref;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
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

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version = References.VERSION, useMetadata = false)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {References.CHANNEL}, packetHandler = PacketHandler.class)
public class RedstoneEnergyField {

    @SidedProxy(clientSide = "samrg472.ref.proxies.ClientProxy", serverSide = "samrg472.ref.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Instance(References.MOD_ID)
    public static RedstoneEnergyField instance;

    public static File configDir;

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
        References.invisibleEnergyBlock = new InvisibleBlock(invisibleEnergyBlockID, Material.air.setReplaceable());
        References.redstoneEnergyBlockT1 = new EnergyBlockT1(redstoneEnergyBlockT1ID, stone);
        References.redstoneEnergyBlockT2 = new EnergyBlockT2(redstoneEnergyBlockT2ID, stone);
        References.redstoneEnergyBlockT3 = new EnergyBlockT3(redstoneEnergyBlockT3ID, stone);
        References.redstoneEnergyBlockT4 = new EnergyBlockT4(redstoneEnergyBlockT4ID, stone);

        new References(config);

        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initialize();
        MinecraftForge.EVENT_BUS.register(new InteractionHandler());

        GameRegistry.registerBlock(References.redstoneEnergyBlockT1, REFItemBlock.class, BaseEnergyBlock.Tier.ONE.unlocalizedName);
        GameRegistry.registerBlock(References.redstoneEnergyBlockT2, REFItemBlock.class, BaseEnergyBlock.Tier.TWO.unlocalizedName);
        GameRegistry.registerBlock(References.redstoneEnergyBlockT3, REFItemBlock.class, BaseEnergyBlock.Tier.THREE.unlocalizedName);
        GameRegistry.registerBlock(References.redstoneEnergyBlockT4, REFItemBlock.class, BaseEnergyBlock.Tier.FOUR.unlocalizedName);

        GameRegistry.registerBlock(References.invisibleEnergyBlock, InvisibleBlock.unlocalizedName);
        GameRegistry.registerTileEntity(T4TE.class, "T4TE");

        addRecipes();
        addDefaultIgnoreList();
    }

    public void addRecipes() {
        GameRegistry.addRecipe(new ItemStack(References.redstoneEnergyBlockT1, 1), "iri", "rpr", "iri", 'i', Block.stone, 'r', new ItemStack(Item.redstone), 'p', new ItemStack(Item.enderPearl));
        GameRegistry.addRecipe(new ItemStack(References.redstoneEnergyBlockT2, 1), "iri", "rpr", "iri", 'i', Block.stone, 'r', Item.netherQuartz, 'p', new ItemStack(References.redstoneEnergyBlockT1));
        GameRegistry.addRecipe(new ItemStack(References.redstoneEnergyBlockT3, 1), "iri", "rpr", "iri", 'i', Block.stone, 'r', new ItemStack(Item.dyePowder, 1, 4), 'p', new ItemStack(References.redstoneEnergyBlockT2));
        GameRegistry.addRecipe(new ItemStack(References.redstoneEnergyBlockT4, 1), "iri", "rpr", "iri", 'i', Block.stone, 'r', Item.eyeOfEnder, 'p', new ItemStack(References.redstoneEnergyBlockT3));
    }

    public void addDefaultIgnoreList() {
        Handler.addIgnoreID(Block.redstoneWire.blockID);
    }

}
