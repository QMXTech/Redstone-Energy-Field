package samrg472.ref.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import samrg472.ref.RedstoneEnergyField;

public class EnergyBlockT1 extends BaseEnergyBlock {

    public EnergyBlockT1(int id, Material material) {
        super(id, material, Tier.ONE);
        setUnlocalizedName("rsEnergyBlockT1");
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        manipulateField(world, RedstoneEnergyField.range, RedstoneEnergyField.invisibleEnergyBlock.blockID, x, y, z, false);
        notifyArea(world, 1, this.blockID, x, y, z);
    }
    
}
