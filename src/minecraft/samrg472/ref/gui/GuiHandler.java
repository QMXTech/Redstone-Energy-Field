package samrg472.ref.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import samrg472.ref.containers.T4Container;
import samrg472.ref.tileentities.T4TE;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if (entity instanceof T4TE)
            return new T4Container();
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if (entity instanceof T4TE)
            return new T4Gui((T4TE) entity, player);
        return null;
    }
}
