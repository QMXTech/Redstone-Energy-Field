package samrg472.ref.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Handler {
    
    private static List<Integer> ignoreList = new ArrayList<Integer>();
    private static List<Integer> handle = new ArrayList<Integer>();

    /**
     * Adds an ID to the ignore list to prevent this redstone device from turning off the field
     * @param id block id
     * @return param id
     */
    public static int addIgnoreID(int id) {
        if (!ignoreList.contains(id))
            ignoreList.add(id);
        return id;
    }

    /**
     * If a block cannot be placed down in the field, you can add manual handling here
     * @param id
     */
    public static void addBlockHandling(int id) {
        if (!handle.contains(id))
            handle.add(id);
    }
    
    public static List<Integer> getIgnoreList() {
        return Collections.unmodifiableList(ignoreList);
    }
    
    public static List<Integer> getHandleBlockList() {
        return Collections.unmodifiableList(handle);
    }
    
}
