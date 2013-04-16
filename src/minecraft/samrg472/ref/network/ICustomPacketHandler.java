package samrg472.ref.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import samrg472.ref.utils.Vector;

import java.io.DataInputStream;
import java.io.IOException;

public interface ICustomPacketHandler {

    /**
     * Called when a packet is received
     */
    public void onPacket(INetworkManager manager, DataInputStream packet, EntityPlayer player, Vector vector) throws IOException;

}
