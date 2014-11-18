package com.qmxtech.ref.gui;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import com.qmxtech.ref.containers.T4Container;
import com.qmxtech.ref.network.MessageTileEntity;
import com.qmxtech.ref.network.PacketHandler;
import com.qmxtech.ref.tileentities.T4TE;
import com.qmxtech.ref.utils.Vector;

public class T4Gui extends GuiContainer {

    private T4TE tileEntity;
    private EntityClientPlayerMP player;

    public T4Gui(T4TE tileEntity, EntityPlayer player) {
        super(new T4Container());
        this.tileEntity = tileEntity;
        this.player = (EntityClientPlayerMP) player;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(1, getCenteredX("-", width) - 40, (height / 2) - 22, 20, 20, "-"));
        buttonList.add(new GuiButton(2, (getCenteredX("+", width)) + 35, (height / 2) - 22, 20, 20, "+"));
	buttonList.add(new GuiButton(3, getCenteredX("-", width) + 40, (height / 2) + 2, 24, 20, booleanToYesNo(tileEntity.getParticle())));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        // TODO: GUI background
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        fontRendererObj.drawString("Range: " + tileEntity.getRange(), getCenteredX("Range: "), ySize / 2 - 14, 0xFFFFFF); // TODO: Localize
	fontRendererObj.drawString("Particle Effects: ", getCenteredX("Particle Effects: ") - 6, ySize / 2 + 10, 0xFFFFFF); // TODO: Localize
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Vector vec = new Vector(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        switch (button.id) {
            case 1: // minus
                tileEntity.decreaseRange();
                PacketHandler.INSTANCE.sendToServer(new MessageTileEntity(T4TE.RequestType.DECREASE.ordinal(), vec));
                break;
            case 2: // plus
                tileEntity.increaseRange();
                PacketHandler.INSTANCE.sendToServer(new MessageTileEntity(T4TE.RequestType.INCREASE.ordinal(), vec));
                break;
	    case 3: // Toggle particle effects
	        tileEntity.toggleParticle();
		PacketHandler.INSTANCE.sendToServer(new MessageTileEntity(T4TE.RequestType.SETPARTICLE.ordinal(), vec));
		button.displayString = (booleanToYesNo(tileEntity.getParticle()));
		break;
        }
    }

    public int getCenteredX(String s) {
        return getCenteredX(s, xSize);
    }

    public int getCenteredX(String s, int size) {
        return (size - this.fontRendererObj.getStringWidth(s)) / 2;
    }
    public String booleanToYesNo(boolean b) {
        return ( b ? "Yes" : "No" );
    }
}

