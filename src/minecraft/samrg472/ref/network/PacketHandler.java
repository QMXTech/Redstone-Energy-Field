package samrg472.ref.network;

import samrg472.ref.References;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;


public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(References.MOD_ID);

    public static void init() {
        INSTANCE.registerMessage(MessageTileEntity.class, MessageTileEntity.class, 0, Side.SERVER);
    }
}