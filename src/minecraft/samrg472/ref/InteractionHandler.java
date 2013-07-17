package samrg472.ref;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import samrg472.ref.api.Handler;

public class InteractionHandler {

    @ForgeSubscribe
    public void rightClickBlockHandler(PlayerInteractEvent event) {
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack stack = event.entityPlayer.getHeldItem();
        if (stack == null) return;
        if (!Handler.getHandleBlockList().contains(stack.itemID)) return;
        int x = event.x,
                y = event.y + 1,
                z = event.z,
                id = stack.itemID;
        World world = event.entityPlayer.worldObj;
        if (world.getBlockId(x, y, z) == References.invisibleEnergyBlock.blockID) {
            updateWorldAndInventory(world, event.entityPlayer, id, stack.getItemDamage(), x, y, z);
        } else if (world.getBlockId(x, y, z) == References.redstoneEnergyBlockT3.blockID) {
            updateWorldAndInventory(world, event.entityPlayer, id, stack.getItemDamage(), x, y - 2, z);
        }
        event.setCanceled(true);
    }

    private static void updateWorldAndInventory(World world, EntityPlayer player, int id, int metadata, int x, int y, int z) {
        Block block = Block.blocksList[id];
        if (block == null) return;
        if (!player.capabilities.isCreativeMode) {
            ItemStack stack = ItemStack.copyItemStack(player.getHeldItem());
            stack.stackSize--;
            if (stack.stackSize <= 0)
                stack = null;
            player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
        }
        world.setBlock(x, y, z, id, metadata, 0x02);
    }

}
