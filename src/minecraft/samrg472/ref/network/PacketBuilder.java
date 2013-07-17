package samrg472.ref.network;

import net.minecraft.network.packet.Packet250CustomPayload;
import samrg472.ref.References;
import samrg472.ref.utils.Vector;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketBuilder {

    public static Packet250CustomPayload buildPacket(int type, Vector vector, int... others) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        try {
            out.writeInt(type);
            out.writeInt(vector.getX());
            out.writeInt(vector.getY());
            out.writeInt(vector.getZ());
            for (int i : others)
                out.writeInt(i);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Packet250CustomPayload(References.CHANNEL, baos.toByteArray());
    }

}
