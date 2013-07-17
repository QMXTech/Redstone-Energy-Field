package samrg472.ref.tileentities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import samrg472.ref.References;
import samrg472.ref.blocks.BaseEnergyBlock;
import samrg472.ref.network.ICustomPacketHandler;
import samrg472.ref.network.PacketBuilder;
import samrg472.ref.network.PacketHandler;
import samrg472.ref.utils.Vector;

import java.io.DataInputStream;
import java.io.IOException;

public class T4TE extends TileEntity implements ICustomPacketHandler {

    private int range;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        range = nbt.getInteger("range");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("range", range);
    }

    public void increaseRange() {
        setRange(range + 1, false);
    }

    public void decreaseRange() {
        setRange(range - 1, true);
    }

    public int getRange() {
        return range;
    }

    public void setRange(int _range, boolean decrease) {
        if (_range % 2 == 0 && !(_range <= 0))
            if (decrease)
                _range--;
            else
                _range++;
        if ((_range < 0) || (_range > References.getMaxRange()))
            return;

        BaseEnergyBlock.breakField(getWorldObj(), range, xCoord, yCoord, zCoord);
        this.range = _range;
    }

    @Override
    public void onPacket(INetworkManager manager, DataInputStream packet, EntityPlayer player, Vector vector) throws IOException {
        RequestType type = RequestType.values()[packet.readInt()];
        switch (type) {
            case DECREASE: // Client sent only
                decreaseRange();
                break;
            case INCREASE: // Client sent only
                increaseRange();
                break;
            case GETRANGE: // Client sent only
                PacketDispatcher.sendPacketToPlayer(PacketBuilder.buildPacket(PacketHandler.PacketType.TILEENTITY.ordinal(), vector, RequestType.SETRANGE.ordinal(), range), (Player) player);
                break;
            case SETRANGE: // Server sent only
                if (FMLCommonHandler.instance().getEffectiveSide().isClient())
                    range = packet.readInt();
                break;
        }
    }

    public enum RequestType {
        DECREASE,
        INCREASE,
        GETRANGE,
        SETRANGE

    }
}
