package com.qmxtech.ref.proxies;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import com.qmxtech.ref.RedstoneEnergyField;
import com.qmxtech.ref.gui.GuiHandler;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void initialize() {
    	super.initialize();
        NetworkRegistry.INSTANCE.registerGuiHandler(RedstoneEnergyField.instance, new GuiHandler());
    }

    @Override
    public void openGUI(EntityPlayer player, int guiID, World world, int x, int y, int z) {
        player.openGui(RedstoneEnergyField.instance, guiID, world, x, y, z);
    }

}

