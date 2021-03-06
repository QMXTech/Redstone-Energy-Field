package com.qmxtech.ref.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.qmxtech.ref.RedstoneEnergyField;
import com.qmxtech.ref.References;
import com.qmxtech.ref.api.Handler;
import com.qmxtech.ref.items.REFItemBlock;
import com.qmxtech.ref.tileentities.T4TE;
import com.qmxtech.ref.utils.ContentUtils;

import java.util.List;
import java.util.Random;

public abstract class BaseEnergyBlock extends BlockContainer {

    public Tier tier; 

    public BaseEnergyBlock(Tier tier) {
        this(tier, tier.unlocalizedName);
    }

    public BaseEnergyBlock(String unlocalizedName) {
        this(Tier.UNKNOWN, unlocalizedName);
    }
    
    public BaseEnergyBlock(Material material, String unlocalizedName) {
    	this(material, Tier.UNKNOWN, unlocalizedName);
    }
    
    private BaseEnergyBlock(Tier tier, String unlocalizedName) {
        this(new Material(MapColor.stoneColor), tier, unlocalizedName);
        GameRegistry.registerBlock(this, REFItemBlock.class, this.getUnlocalizedName());
    }

    private BaseEnergyBlock(Material material, Tier tier, String unlocalizedName) {
        super(material);
        this.tier = tier;

        this.setBlockName(unlocalizedName);
        this.setCreativeTab(RedstoneEnergyField.tab);
        this.setTickRandomly(true);
        this.setHardness(3);

        disableStats();
    }

    @Override
    public abstract void updateTick(World world, int x, int y, int z, Random rand);

    public void tickParticleEffects(World world, int x, int y, int z)
    {
        TileEntity entity = world.getTileEntity(x, y, z);
        if (References.showParticleEffects() && (!isReceivingPower(world, x, y, z))) {
            if ((tier == Tier.THREE) && (world.getBlockMetadata(x, y, z) == 1))
                sparkle(world, x, y, z, References.getRange());
	    else if ((tier == Tier.FOUR) && (entity != null) && (entity instanceof T4TE) && ((T4TE) entity).getParticle())
	        sparkle(world, x, y, z, ((T4TE) entity).getRange());
	}
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT) // setIconPath
    public void registerBlockIcons(IIconRegister register) {
        if (tier != Tier.UNKNOWN)
            blockIcon = register.registerIcon(References.MOD_ID + ":" + tier.texture);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        TileEntity entity = world.getTileEntity(x, y, z);
        if ((entity != null) && (entity instanceof T4TE)) {
            manipulateField(world, ((T4TE) entity).getRange(), References.invisibleEnergyBlock, x, y, z, false);
        } else {
            manipulateField(world, References.getRange(), References.invisibleEnergyBlock, x, y, z, false);
        }
        notifyArea(world, this, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        super.breakBlock(world, x, y, z, block, metadata);

        TileEntity entity = world.getTileEntity(x, y, z);
        if ((entity != null) && (entity instanceof T4TE)) {
            manipulateField(world, ((T4TE) entity).getRange(), block, x, y, z, true);
        } else {
            manipulateField(world, References.getRange(), block, x, y, z, true);
        }
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
        TileEntity entity = world.getTileEntity(x, y, z);
        if ((entity != null) && (entity instanceof T4TE)) {
            manipulateField(world, ((T4TE) entity).getRange(), this, x, y, z, true);
        } else {
            manipulateField(world, References.getRange(), this, x, y, z, true);
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
        return 1;
    }

    @Override
    public int getMobilityFlag() {
        return 2;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess block, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        super.onNeighborBlockChange(world, x, y, z, neighbor);
        world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        tickParticleEffects(world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    protected void sparkle(World world, int x, int y, int z, int range) {
        double baseX = x + 0.5D;
        double baseY = y + 0.5D;
        double baseZ = z + 0.5D;

        for(double z2 = (baseZ - range); z2 <= (baseZ + range); z2++ ) {
            for( double x2 = (baseX - range); x2 <= (baseX + range); x2++ ) {
                if( (x2 == (baseX - range)) || (x2 == (baseX + range)) || (z2 == (baseZ - range)) || (z2 == (baseZ + range)) )
                    world.spawnParticle("reddust", x2, baseY, z2, 0.0D, 0.0D, 0.0D);
            }

            for( double y2 = (baseY - range); y2 <= (baseY + range); y2++ ) {
                if( (y2 == (baseY - range)) || (y2 == (baseY + range)) || (z2 == (baseZ - range)) || (z2 == (baseZ + range)) )
                    world.spawnParticle("reddust", baseX, y2, z2, 0.0D, 0.0D, 0.0D);
            }
        }

        for(double y2 = (baseY - range); y2 <= (baseY + range); y2++ ) {
            for( double x2 = (baseX - range); x2 <= (baseX + range); x2++ ) {
                if( (x2 == (baseX - range)) || (x2 == (baseX + range)) || (y2 == (baseY - range)) || (y2 == (baseY + range)) )
                    world.spawnParticle("reddust", x2, y2, baseZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected boolean isReceivingPower(IBlockAccess blockAccess, int x, int y, int z) {
        List<String> ignoreList = Handler.getIgnoreList();
        if (!ignoreList.contains(ContentUtils.getIdent(blockAccess.getBlock(x, y - 1, z))) && (blockAccess.isBlockProvidingPowerTo(x, y - 1, z, 0) > 0)) // bottom
            return true;
        if (!ignoreList.contains(ContentUtils.getIdent(blockAccess.getBlock(x, y + 1, z))) && (blockAccess.isBlockProvidingPowerTo(x, y + 1, z, 1) > 0)) // top
            return true;
        if (!ignoreList.contains(ContentUtils.getIdent(blockAccess.getBlock(x, y, z - 1))) && (blockAccess.isBlockProvidingPowerTo(x, y, z - 1, 2) > 0)) // north
            return true;
        if (!ignoreList.contains(ContentUtils.getIdent(blockAccess.getBlock(x, y, z + 1))) && (blockAccess.isBlockProvidingPowerTo(x, y, z + 1, 3) > 0)) // south
            return true;
        if (!ignoreList.contains(ContentUtils.getIdent(blockAccess.getBlock(x - 1, y, z))) && (blockAccess.isBlockProvidingPowerTo(x - 1, y, z, 4) > 0)) // west
            return true;
        return !ignoreList.contains(ContentUtils.getIdent(blockAccess.getBlock(x + 1, y, z))) && (blockAccess.isBlockProvidingPowerTo(x + 1, y, z, 5) > 0);
    }

    /*
     * Args x, y, z is the current position
     */
    protected void manipulateField(World world, int range, Block block, int metadata, int x, int y, int z, boolean breakBlock) {
        for (int x2 = x - range; x2 <= x + range; x2++) {
            for (int y2 = y - range; y2 <= y + range; y2++) {
                for (int z2 = z - range; z2 <= z + range; z2++) {
                    if (breakBlock) {
                        if (world.isAirBlock(x2, y2, z2)) {
                            Block b = world.getBlock(x2, y2, z2);
                            if ((b != null) && (b == References.invisibleEnergyBlock)) {
                                world.setBlock(x2, y2, z2, Blocks.air, 0, 0x02);
                            }
                        }
                    } else {
                        if (world.isAirBlock(x2, y2, z2)) {
                            world.setBlock(x2, y2, z2, block, metadata, 0x02);
                        }
                    }
                }
            }
        }
    }

    protected void manipulateField(World world, int range, Block block, int x, int y, int z, boolean breakBlock) {
        manipulateField(world, range, block, 0, x, y, z, breakBlock);
    }

    public boolean shouldBlockBeActivated(ItemStack stack) {
        if (stack == null)
            return true;
        Block current = Block.getBlockFromItem(stack.copy().getItem());
        if (current != null) {
            if (current == Blocks.lever)
                return false;
            else if (current == Blocks.wooden_pressure_plate)
                return false;
            else if (current == Blocks.stone_pressure_plate)
                return false;
            else if (current == Blocks.light_weighted_pressure_plate)
                return false;
            else if (current == Blocks.heavy_weighted_pressure_plate)
                return false;
        }
        return true;
    }

    public static void breakField(World world, int range, int x, int y, int z) {
        References.invisibleEnergyBlock.manipulateField(world, range, Blocks.air, x, y, z, true);
    }

    @SuppressWarnings("ConstantConditions")
    protected static void notifyArea(World world, Block block, int x, int y, int z) {
        int range = 1;
        world.notifyBlocksOfNeighborChange(x, y - range, z, block);
        world.notifyBlocksOfNeighborChange(x, y + range, z, block);
        world.notifyBlocksOfNeighborChange(x - range, y, z, block);
        world.notifyBlocksOfNeighborChange(x + range, y, z, block);
        world.notifyBlocksOfNeighborChange(x, y, z - range, block);
        world.notifyBlocksOfNeighborChange(x, y, z + range, block);
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

