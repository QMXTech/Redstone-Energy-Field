package samrg472.ref.gui;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import samrg472.ref.containers.T4Container;
import samrg472.ref.network.PacketBuilder;
import samrg472.ref.network.PacketHandler;
import samrg472.ref.tileentities.T4TE;
import samrg472.ref.utils.Vector;

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
        buttonList.add(new GuiButton(1, getCenteredX("-") / 2, 63, 20, 20, "-"));
        buttonList.add(new GuiButton(2, (getCenteredX("+") / 2) + 90, 63, 20, 20, "+")); // x + 90 of - button
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        // TODO: GUI background
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        drawString(fontRenderer, "Range: " + tileEntity.getRange(), (getCenteredX("Range: ") / 2) - 150, 63, 0xFFFFFF);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Vector vec = new Vector(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        switch (button.id) {
            case 1: // minus
                tileEntity.decreaseRange();
                player.sendQueue.addToSendQueue(PacketBuilder.buildPacket(PacketHandler.PacketType.TILEENTITY.ordinal(), vec, T4TE.RequestType.DECREASE.ordinal()));
                break;
            case 2: // plus
                tileEntity.increaseRange();
                player.sendQueue.addToSendQueue(PacketBuilder.buildPacket(PacketHandler.PacketType.TILEENTITY.ordinal(), vec, T4TE.RequestType.INCREASE.ordinal()));
                break;
        }
    }

    public int getCenteredX(String s) {
        return (this.xSize - fontRenderer.getStringWidth(s)) / 2;
    }
}
