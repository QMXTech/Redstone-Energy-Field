package samrg472.ref.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import samrg472.ref.References;

import java.util.Random;

public class EnergyBlockT3 extends BaseEnergyBlock {

    public EnergyBlockT3(int id, Material material) {
        super(id, material, Tier.THREE);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 0) {
            manipulateField(world, References.getRange(), References.invisibleEnergyBlock.blockID, x, y, z, isReceivingPower(world, x, y, z));
        } else if (metadata == 1) {
            manipulateField(world, References.getRange(), References.invisibleEnergyBlock.blockID, 1, x, y, z, isReceivingPower(world, x, y, z));
        }
        notifyArea(world, this.blockID, x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        if (!shouldBlockBeActivated(player.getHeldItem()))
            return false;

        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 0) {
            world.setBlock(x, y, z, References.redstoneEnergyBlockT3.blockID, 1, 0x02);
            manipulateField(world, References.getRange(), References.invisibleEnergyBlock.blockID, 1, x, y, z, isReceivingPower(world, x, y, z));
        } else if (metadata == 1) {
            world.setBlock(x, y, z, References.redstoneEnergyBlockT3.blockID, 0, 0x02);
            manipulateField(world, References.getRange(), References.invisibleEnergyBlock.blockID, 0, x, y, z, isReceivingPower(world, x, y, z));
        }
        return true;
    }

}