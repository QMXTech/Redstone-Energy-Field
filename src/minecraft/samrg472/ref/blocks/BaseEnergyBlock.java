package samrg472.ref.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import samrg472.ref.RedstoneEnergyField;
import samrg472.ref.api.Handler;
import samrg472.ref.tileentities.T4TE;

import java.util.List;
import java.util.Random;

public abstract class BaseEnergyBlock extends BlockContainer {

    private Tier tier;

    public BaseEnergyBlock(int id, Material material, Tier tier) {
        this(id, material, tier, tier.unlocalizedName);
    }

    public BaseEnergyBlock(int id, Material material, String unlocalizedName) {
        this(id, material, Tier.UNKNOWN, unlocalizedName);
    }

    private BaseEnergyBlock(int id, Material material, Tier tier, String unlocalizedName) {
        super(id, material);
        this.tier = tier;

        setUnlocalizedName(unlocalizedName);
        setCreativeTab(CreativeTabs.tabRedstone);
        setTickRandomly(true);
        setHardness(3);

        disableStats();
    }

    @Override
    public abstract void updateTick(World world, int x, int y, int z, Random rand);

    @Override
    public TileEntity createNewTileEntity(World world) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT) // setIconPath
    public void registerIcons(IconRegister register) {
        if (tier != Tier.UNKNOWN)
            blockIcon = register.registerIcon(RedstoneEnergyField.MOD_ID + ":" + tier.texture);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if ((entity != null) && (entity instanceof T4TE)) {
            manipulateField(world, ((T4TE) entity).getRange(), RedstoneEnergyField.invisibleEnergyBlock.blockID, x, y, z, false);
        } else {
            manipulateField(world, RedstoneEnergyField.range, RedstoneEnergyField.invisibleEnergyBlock.blockID, x, y, z, false);
        }
        notifyArea(world, 1, this.blockID, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int metadata) {
        super.breakBlock(world, x, y, z, par5, metadata);

        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if ((entity != null) && (entity instanceof T4TE)) {
            manipulateField(world, ((T4TE) entity).getRange(), this.blockID, x, y, z, true, true);
        } else {
            manipulateField(world, RedstoneEnergyField.range, this.blockID, x, y, z, true, true);
        }
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if ((entity != null) && (entity instanceof T4TE)) {
            manipulateField(world, ((T4TE) entity).getRange(), this.blockID, x, y, z, true, true);
        } else {
            manipulateField(world, RedstoneEnergyField.range, this.blockID, x, y, z, true, true);
        }
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return 15;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return 15;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int tickRate(World w) {
        return 4;
    }

    @Override
    public int getMobilityFlag() {
        return 2;
    }

    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
        super.onNeighborBlockChange(world, x, y, z, neighborID);
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if ((world.getBlockMetadata(x, y, z) == 1) && (RedstoneEnergyField.particleEffects))
            this.sparkle(world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    protected void sparkle(World world, int x, int y, int z) {
        Random rand = world.rand;
        double riser = 0.01D;

        for (int i = 0; i <= 5; i++) {

            double x2 = x + rand.nextDouble(),
                    y2 = y + rand.nextDouble(),
                    z2 = z + rand.nextDouble();

            if (i == 0 && !world.isBlockOpaqueCube(x, y + 1, z)) // top
                y2 = (y + 1.0D) + riser;
            if (i == 1 && !world.isBlockOpaqueCube(x, y - 1, z)) // bottom
                y2 = y - riser;
            if (i == 2 && !world.isBlockOpaqueCube(x, y, z + 1)) // north
                z2 = (z + 1.0D) + riser;
            if (i == 3 && !world.isBlockOpaqueCube(x, y, z - 1)) // south
                z2 = z - riser;
            if (i == 4 && !world.isBlockOpaqueCube(x + 1, y, z)) // west
                x2 = (x + 1.0D) + riser;
            if (i == 5 && !world.isBlockOpaqueCube(x - 1, y, z)) // east
                x2 = x - riser;

            if (((x2 < (x - 0.3D)) || x2 > (x + 1.3D)) || (y2 < 0.3D || y2 > (y + 1.0D)) || (z2 < (z - 0.3D) || z2 > (z + 1.0D)))
                world.spawnParticle("reddust", x2, y2, z2, 0.0D, 0.0D, 0.0D);
        }
    }

    protected boolean isReceivingPower(IBlockAccess blockAccess, int x, int y, int z) {
        List<Integer> ignoreList = Handler.getIgnoreList();
        if (!ignoreList.contains(blockAccess.getBlockId(x, y - 1, z)) && (blockAccess.isBlockProvidingPowerTo(x, y - 1, z, 0) > 0)) // bottom
            return true;
        if (!ignoreList.contains(blockAccess.getBlockId(x, y + 1, z)) && (blockAccess.isBlockProvidingPowerTo(x, y + 1, z, 1) > 0)) // top
            return true;
        if (!ignoreList.contains(blockAccess.getBlockId(x, y, z - 1)) && (blockAccess.isBlockProvidingPowerTo(x, y, z - 1, 2) > 0)) // north
            return true;
        if (!ignoreList.contains(blockAccess.getBlockId(x, y, z + 1)) && (blockAccess.isBlockProvidingPowerTo(x, y, z + 1, 3) > 0)) // south
            return true;
        if (!ignoreList.contains(blockAccess.getBlockId(x - 1, y, z)) && (blockAccess.isBlockProvidingPowerTo(x - 1, y, z, 4) > 0)) // west
            return true;
        if (!ignoreList.contains(blockAccess.getBlockId(x + 1, y, z)) && (blockAccess.isBlockProvidingPowerTo(x + 1, y, z, 5) > 0)) // east
            return true;
        return false;
    }

    /*
     * Args x, y, z is the current position
     */
    protected void manipulateField(World world, int range, int id, int metadata, int x, int y, int z, boolean breakBlock, boolean force) {
        if (range % 2 == 0)
            range++;
        range = (range - 1) / 2;
        if (!isSurrounded(world, x, y, z) && breakBlock && !force)
            return;
        for (int x2 = x - range; x2 <= x + range; x2++) {
            for (int y2 = y - range; y2 <= y + range; y2++) {
                for (int z2 = z - range; z2 <= z + range; z2++) {
                    if (breakBlock) {
                        if (world.isAirBlock(x2, y2, z2)) {
                            Block b = blocksList[world.getBlockId(x2, y2, z2)];
                            if ((b != null) && (b.blockID == RedstoneEnergyField.invisibleEnergyBlock.blockID)) {
                                world.setBlock(x2, y2, z2, 0, 0, 0x02);
                            }
                        }
                    } else {
                        if (world.isAirBlock(x2, y2, z2)) {
                            world.setBlock(x2, y2, z2, id, metadata, 0x02);
                        }
                    }
                }
            }
        }
    }

    protected void manipulateField(World world, int range, int id, int x, int y, int z, boolean breakBlock, boolean force) {
        manipulateField(world, range, id, 0, x, y, z, breakBlock, force);
    }

    protected void manipulateField(World world, int range, int id, int x, int y, int z, boolean breakBlock) {
        manipulateField(world, range, id, 0, x, y, z, breakBlock, false);
    }

    protected void manipulateField(World world, int range, int id, int metadata, int x, int y, int z, boolean breakBlock) {
        manipulateField(world, range, id, metadata, x, y, z, breakBlock, false);
    }

    public static void breakField(World world, int range, int x, int y, int z) {
        RedstoneEnergyField.invisibleEnergyBlock.manipulateField(world, range, 0, x, y, z, true, true);
    }

    protected static void notifyArea(World world, int range, int id, int x, int y, int z) {
        world.notifyBlocksOfNeighborChange(x, y - range, z, id);
        world.notifyBlocksOfNeighborChange(x, y + range, z, id);
        world.notifyBlocksOfNeighborChange(x - range, y, z, id);
        world.notifyBlocksOfNeighborChange(x + range, y, z, id);
        world.notifyBlocksOfNeighborChange(x, y, z - range, id);
        world.notifyBlocksOfNeighborChange(x, y, z + range, id);
    }

    protected static boolean isSurrounded(World world, int x, int y, int z) {
        if (world.getBlockId(x, y - 1, z) == RedstoneEnergyField.invisibleEnergyBlock.blockID)
            return true;
        if (world.getBlockId(x, y + 1, z) == RedstoneEnergyField.invisibleEnergyBlock.blockID)
            return true;
        if (world.getBlockId(x, y, z - 1) == RedstoneEnergyField.invisibleEnergyBlock.blockID)
            return true;
        if (world.getBlockId(x, y, z + 1) == RedstoneEnergyField.invisibleEnergyBlock.blockID)
            return true;
        if (world.getBlockId(x - 1, y, z) == RedstoneEnergyField.invisibleEnergyBlock.blockID)
            return true;
        if (world.getBlockId(x + 1, y, z) == RedstoneEnergyField.invisibleEnergyBlock.blockID)
            return true;
        return false;
    }

    public enum Tier {

        UNKNOWN(null, null),
        ONE("Tier1", "rsEnergyBlockT1"),
        TWO("Tier2", "rsEnergyBlockT2"),
        THREE("Tier3", "rsEnergyBlockT3"),
        FOUR("Tier4", "rsEnergyBlockT4");

        public String texture;
        public String unlocalizedName;

        private Tier(String texture, String unlocalizedName) {
            this.texture = texture;
            this.unlocalizedName = unlocalizedName;
        }

    }

}
