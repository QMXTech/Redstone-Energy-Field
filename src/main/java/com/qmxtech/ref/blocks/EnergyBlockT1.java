package com.qmxtech.ref.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import com.qmxtech.ref.References;

import java.util.Random;

public class EnergyBlockT1 extends BaseEnergyBlock {

    public EnergyBlockT1() {
        super(Tier.ONE);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        manipulateField(world, References.getRange(), References.invisibleEnergyBlock, x, y, z, false);
        notifyArea(world, this, x, y, z);
    }

}

