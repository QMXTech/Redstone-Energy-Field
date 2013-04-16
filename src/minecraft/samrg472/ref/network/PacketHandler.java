package samrg472.ref.network;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import samrg472.ref.RedstoneEnergyField;
import samrg472.ref.utils.Vector;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (!packet.channel.equals(RedstoneEnergyField.CHANNEL))
            return;
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));
            int type = dis.readInt();
            switch (PacketType.getPacketType(type)) {
                case TILEENTITY:
                    int x = dis.readInt();
                    int y = dis.readInt();
                    int z = dis.readInt();
                    EntityPlayer player2 = (EntityPlayer) player;
                    TileEntity entity = player2.worldObj.getBlockTileEntity(x, y, z);
                    if (entity instanceof ICustomPacketHandler)
                        ((ICustomPacketHandler) entity).onPacket(manager, dis, player2, new Vector(x, y, z));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public enum PacketType {
        UNKNOWN,
        TILEENTITY;

        public static PacketType getPacketType(int type) {
            if ((type < 0) || (type >= PacketType.values().length))
                return UNKNOWN;
            return PacketType.values()[type];
        }
    }
}
