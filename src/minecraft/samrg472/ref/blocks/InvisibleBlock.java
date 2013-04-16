package samrg472.ref.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import samrg472.ref.RedstoneEnergyField;

import java.util.Random;

public class InvisibleBlock extends BaseEnergyBlock {

    public static final String unlocalizedName = "invisiblePoweredBlock";

    public InvisibleBlock(int id, Material material) {
        super(id, material, unlocalizedName);
        setCreativeTab(null);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean canProvidePower() {
        return RedstoneEnergyField.connectEverything;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        notifyArea(world, 1, this.blockID, x, y, z);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        notifyArea(world, 1, this.blockID, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        notifyArea(world, 1, this.blockID, x, y, z);
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
        world.setBlock(x, y, z, 0, 0, 0x02);
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random) {
        return 0;
    }

    @Override
    public boolean isAirBlock(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float par7, float par8, float par9) {
        return false;
    }

}
