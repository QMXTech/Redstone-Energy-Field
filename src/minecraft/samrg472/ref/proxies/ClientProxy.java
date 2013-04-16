package samrg472.ref.proxies;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import samrg472.ref.RedstoneEnergyField;
import samrg472.ref.gui.GuiHandler;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void initialize() {
        NetworkRegistry.instance().registerGuiHandler(RedstoneEnergyField.instance, new GuiHandler());
    }

    @Override
    public void openGUI(EntityPlayer player, int guiID, World world, int x, int y, int z) {
        player.openGui(RedstoneEnergyField.instance, guiID, world, x, y, z);
    }

}
