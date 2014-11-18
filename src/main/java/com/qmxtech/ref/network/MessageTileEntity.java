package com.qmxtech.ref.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import com.qmxtech.ref.utils.Vector;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageTileEntity implements IMessage, IMessageHandler<MessageTileEntity, IMessage> {
	
	int type;
	Vector vec;
	int[] others;
	
	// needed for reconstructing
	public MessageTileEntity() {
	}
	
	public MessageTileEntity(int type, Vector vector, int... others) {
		this.type = type;
		this.vec = vector;
		this.others = others;
	}

	@Override
	public IMessage onMessage(MessageTileEntity message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().playerEntity;
		TileEntity entity = player.worldObj.getTileEntity(message.vec.getX(), message.vec.getY(), message.vec.getZ());
        if (entity instanceof ICustomPacketHandler) {
			((ICustomPacketHandler) entity).onPacket(player, message.type, message.vec);
        }
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.type = buf.readInt();
		this.vec = new Vector(buf.readInt(), buf.readInt(), buf.readInt());
		
		List<Integer> othersList = new ArrayList<Integer>();
		try {
			while(true) {
				othersList.add(buf.readInt());
			}
		} catch(Exception e) {
		} finally {
			
		}
	}

	@Override
	public void toBytes(ByteBuf out) {
        out.writeInt(type);
        out.writeInt(vec.getX());
        out.writeInt(vec.getY());
        out.writeInt(vec.getZ());
        for (int i : others) {
            out.writeInt(i);
        }
	}

}

