package samrg472.ref.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import samrg472.ref.utils.ContentUtils;
import net.minecraft.block.Block;

public class Handler {
    
    private static List<String> ignoreList = new ArrayList<String>();
    private static List<String> handle = new ArrayList<String>();

    /**
     * Adds an ID to the ignore list to prevent this redstone device from turning off the field
     * @param block block
     * @return param blockName
     */
    public static String addIgnore(Block block) {
    	String blockName = ContentUtils.getIdent(block);
        if (!ignoreList.contains(blockName))
            ignoreList.add(blockName);
        return blockName;
    }

    /**
     * If a block cannot be placed down in the field, you can add manual handling here
     * @param block
     */
    public static void addBlockHandling(Block block) {
    	String blockName = Block.blockRegistry.getNameForObject(block);
        if (!handle.contains(blockName))
            handle.add(blockName);
    }
    
    public static List<String> getIgnoreList() {
        return Collections.unmodifiableList(ignoreList);
    }
    
    public static List<String> getHandleBlockList() {
        return Collections.unmodifiableList(handle);
    }
    
}
