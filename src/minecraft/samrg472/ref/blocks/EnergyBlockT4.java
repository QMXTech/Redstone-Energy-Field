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
import samrg472.ref.network.PacketBuilder;
import samrg472.ref.network.PacketHandler;
import samrg472.ref.tileentities.T4TE;
import samrg472.ref.utils.Vector;

import java.util.Random;

public class EnergyBlockT4 extends BaseEnergyBlock {

    public EnergyBlockT4(int id, Material material) {
        super(id, material, Tier.FOUR);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        int metadata = world.getBlockMetadata(x, y, z);
        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if ((entity == null) || !(entity instanceof T4TE))
            return;
        if (metadata == 0) {
            manipulateField(world, ((T4TE) entity).getRange(), References.invisibleEnergyBlock.blockID, x, y, z, isReceivingPower(world, x, y, z));
        } else if (metadata == 1) {
            manipulateField(world, ((T4TE) entity).getRange(), References.invisibleEnergyBlock.blockID, 1, x, y, z, isReceivingPower(world, x, y, z));
        }
        notifyArea(world, this.blockID, x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (!shouldBlockBeActivated(player.getHeldItem()))
            return false;

        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if ((entity == null) || !(entity instanceof T4TE))
            return false;

        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            dispatchRequestPacket(player, x, y, z);
        RedstoneEnergyField.proxy.openGUI(player, 0, world, x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new T4TE();
    }

    @SideOnly(Side.CLIENT)
    private void dispatchRequestPacket(EntityPlayer player, int x, int y, int z) {
        ((EntityClientPlayerMP) player).sendQueue.addToSendQueue(PacketBuilder.buildPacket(PacketHandler.PacketType.TILEENTITY.ordinal(), new Vector(x, y, z), T4TE.RequestType.GETRANGE.ordinal()));
    }
}
