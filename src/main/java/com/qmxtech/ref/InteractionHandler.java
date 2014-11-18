package com.qmxtech.ref;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import com.qmxtech.ref.api.Handler;
import com.qmxtech.ref.utils.ContentUtils;

public class InteractionHandler {

    @SubscribeEvent
    public void rightClickBlockHandler(PlayerInteractEvent event) {
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack stack = event.entityPlayer.getHeldItem();
        if (stack == null) return;
        if (!Handler.getHandleBlockList().contains(ContentUtils.getIdent(stack))) return;
        int x = event.x,
            y = event.y + 1,
            z = event.z;
        World world = event.entityPlayer.worldObj;
        if (world.getBlock(x, y, z) == References.invisibleEnergyBlock) {
            updateWorldAndInventory(world, event.entityPlayer, stack.getItem(), stack.getItemDamage(), x, y, z);
        } else if (world.getBlock(x, y, z) == References.redstoneEnergyBlockT3) {
            updateWorldAndInventory(world, event.entityPlayer, stack.getItem(), stack.getItemDamage(), x, y - 2, z);
        }
        event.setCanceled(true);
    }

    private static void updateWorldAndInventory(World world, EntityPlayer player, Item item, int metadata, int x, int y, int z) {
        Block block = Block.getBlockFromItem(item);
        if (block == null) return;
        if (!player.capabilities.isCreativeMode) {
            ItemStack stack = ItemStack.copyItemStack(player.getHeldItem());
            stack.stackSize--;
            if (stack.stackSize <= 0)
                stack = null;
            player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
        }
        world.setBlock(x, y, z, block, metadata, 0x02);
    }

}

