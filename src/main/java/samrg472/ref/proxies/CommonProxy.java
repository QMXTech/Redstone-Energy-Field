package samrg472.ref.proxies;

import cpw.mods.fml.common.registry.GameRegistry;
import samrg472.ref.InteractionHandler;
import samrg472.ref.References;
import samrg472.ref.api.Handler;
import samrg472.ref.blocks.*;
import samrg472.ref.items.REFItemBlock;
import samrg472.ref.network.PacketHandler;
import samrg472.ref.tileentities.T4TE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void initialize() {
        References.invisibleEnergyBlock = new InvisibleBlock(Material.air.setReplaceable());
        References.redstoneEnergyBlockT1 = new EnergyBlockT1();
        References.redstoneEnergyBlockT2 = new EnergyBlockT2();
        References.redstoneEnergyBlockT3 = new EnergyBlockT3();
        References.redstoneEnergyBlockT4 = new EnergyBlockT4();

        MinecraftForge.EVENT_BUS.register(new InteractionHandler());
        GameRegistry.registerBlock(References.invisibleEnergyBlock, InvisibleBlock.unlocalizedName);
        GameRegistry.registerTileEntity(T4TE.class, "T4TE");
        
        GameRegistry.addRecipe(new ItemStack(References.redstoneEnergyBlockT1, 1), "iri", "rpr", "iri", 'i', Blocks.stone, 'r', new ItemStack(Items.redstone), 'p', new ItemStack(Items.ender_pearl));
        GameRegistry.addRecipe(new ItemStack(References.redstoneEnergyBlockT2, 1), "iri", "rpr", "iri", 'i', Blocks.stone, 'r', Items.quartz, 'p', new ItemStack(References.redstoneEnergyBlockT1));
        GameRegistry.addRecipe(new ItemStack(References.redstoneEnergyBlockT3, 1), "iri", "rpr", "iri", 'i', Blocks.stone, 'r', new ItemStack(Items.dye, 1, 4), 'p', new ItemStack(References.redstoneEnergyBlockT2));
        GameRegistry.addRecipe(new ItemStack(References.redstoneEnergyBlockT4, 1), "iri", "rpr", "iri", 'i', Blocks.stone, 'r', Items.ender_eye, 'p', new ItemStack(References.redstoneEnergyBlockT3));
    
        Handler.addIgnore(Blocks.redstone_wire);
        Handler.addIgnore(References.invisibleEnergyBlock);
        PacketHandler.init();
    }

    public void openGUI(EntityPlayer player, int guiID, World world, int x, int y, int z) {

    }

}
