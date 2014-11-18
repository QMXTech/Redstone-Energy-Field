package com.qmxtech.ref.network;

import net.minecraft.entity.player.EntityPlayer;
import com.qmxtech.ref.utils.Vector;

import java.io.DataInputStream;
import java.io.IOException;

public interface ICustomPacketHandler {

    /**
     * Called when a packet is received
     */
    public void onPacket(EntityPlayer player, int type, Vector vector);

}

