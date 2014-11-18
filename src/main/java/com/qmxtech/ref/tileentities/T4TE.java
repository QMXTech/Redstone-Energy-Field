package com.qmxtech.ref.tileentities;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import com.qmxtech.ref.References;
import com.qmxtech.ref.blocks.BaseEnergyBlock;
import com.qmxtech.ref.network.ICustomPacketHandler;
import com.qmxtech.ref.network.MessageTileEntity;
import com.qmxtech.ref.network.PacketHandler;
import com.qmxtech.ref.utils.Vector;

import java.io.DataInputStream;
import java.io.IOException;

public class T4TE extends TileEntity implements ICustomPacketHandler {

    private int range;
    private boolean particle;

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
    public void toggleParticle() {
        particle = (particle ? false : true);
    }

    public Boolean getParticle() {
        return particle;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int _range, boolean decrease) {
        if ((_range < 0) || (_range > References.getMaxRange()))
            return;

        if (_range < range)
            BaseEnergyBlock.breakField(getWorldObj(), range, xCoord, yCoord, zCoord);
        this.range = _range;
    }

    public enum RequestType {
        DECREASE,
        INCREASE,
        SETRANGE,
	SETPARTICLE
    }

	@Override
	public void onPacket(EntityPlayer player, int type, Vector vector) {
        switch (RequestType.values()[type]) {
        	case DECREASE: // Client sent only
        		decreaseRange();
        		break;
        	case INCREASE: // Client sent only
        		increaseRange();
        		break;
        	case SETRANGE: // Server sent only
        		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        			range = type;
        		break;
		case SETPARTICLE: // Client sent only
		        toggleParticle();
			break;
        }	
	}
	
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }
}

