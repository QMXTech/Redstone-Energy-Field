package samrg472.ref.blocks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import samrg472.ref.RedstoneEnergyField;
import samrg472.ref.References;
import samrg472.ref.network.MessageTileEntity;
import samrg472.ref.network.PacketHandler;
import samrg472.ref.tileentities.T4TE;
import samrg472.ref.utils.Vector;

import java.util.Random;

public class EnergyBlockT4 extends BaseEnergyBlock {

    public EnergyBlockT4() {
        super(Tier.FOUR);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        int metadata = world.getBlockMetadata(x, y, z);
        TileEntity entity = world.getTileEntity(x, y, z);
        if ((entity == null) || !(entity instanceof T4TE))
            return;
        if (metadata == 0) {
            manipulateField(world, ((T4TE) entity).getRange(), References.invisibleEnergyBlock, x, y, z, isReceivingPower(world, x, y, z));
        } else if (metadata == 1) {
            manipulateField(world, ((T4TE) entity).getRange(), References.invisibleEnergyBlock, 1, x, y, z, isReceivingPower(world, x, y, z));
        }
        notifyArea(world, this, x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (!shouldBlockBeActivated(player.getHeldItem()))
            return false;

        TileEntity entity = world.getTileEntity(x, y, z);
        if ((entity == null) || !(entity instanceof T4TE))
            return false;

        RedstoneEnergyField.proxy.openGUI(player, 0, world, x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new T4TE();
    }
}
