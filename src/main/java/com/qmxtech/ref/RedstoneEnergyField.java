package com.qmxtech.ref;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import com.qmxtech.ref.api.Handler;
import com.qmxtech.ref.blocks.*;
import com.qmxtech.ref.items.REFItemBlock;
import com.qmxtech.ref.network.PacketHandler;
import com.qmxtech.ref.proxies.CommonProxy;
import com.qmxtech.ref.tileentities.T4TE;

import java.io.File;
@Mod(modid = "RedstoneEnergyField", name = "Redstone Energy Field", version = "@VERSION@", useMetadata = false)
public class RedstoneEnergyField {

    @SidedProxy(clientSide = "com.qmxtech.ref.proxies.ClientProxy", serverSide = "com.qmxtech.ref.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Instance(References.MOD_ID)
    public static RedstoneEnergyField instance;
    
    public static CreativeTabs tab = new CreativeTabs(References.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(References.redstoneEnergyBlockT4);
        }
    };

    public static File configDir;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        File suggestedConfiguration = event.getSuggestedConfigurationFile();

        configDir = suggestedConfiguration.getParentFile();
        Configuration config = new Configuration(suggestedConfiguration);
        config.load();
        new References(config);
        config.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.initialize();
    }

}

