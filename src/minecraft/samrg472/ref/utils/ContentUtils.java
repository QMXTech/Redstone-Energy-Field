package samrg472.ref.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * A helper class for blocks and items, but not manipulating objects themselves. Right now,
 * all it does it provide a clean way to get a block identifier (as in, minecraft:water).
 *
 * @author TheMike
 */
public class ContentUtils {
	
    public static String getIdent(Block block) {
        return block == null ? null : Block.blockRegistry.getNameForObject(block);
    }

    public static String getIdent(Item item) {
        return item == null ? null : Item.itemRegistry.getNameForObject(item);
    }
    
    public static String getIdent(ItemStack item) {
        return getIdent(item.getItem());
    }

    public static boolean areItemsEqual(Item item1, Item item2) {
        return getIdent(item1).equals(getIdent(item2));
    }

    public static boolean areBlocksEqual(Block block1, Block block2) {
        return getIdent(block1).equals(getIdent(block2));
    }

}