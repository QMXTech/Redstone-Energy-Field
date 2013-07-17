package samrg472.ref.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import samrg472.ref.References;

import java.util.Random;

public class EnergyBlockT1 extends BaseEnergyBlock {

    public EnergyBlockT1(int id, Material material) {
        super(id, material, Tier.ONE);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        manipulateField(world, References.getRange(), References.invisibleEnergyBlock.blockID, x, y, z, false);
        notifyArea(world, this.blockID, x, y, z);
    }

}
