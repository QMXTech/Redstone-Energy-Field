package samrg472.ref.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import samrg472.ref.References;
import samrg472.ref.blocks.BaseEnergyBlock;

import java.util.List;

public class REFItemBlock extends ItemBlock {

    public REFItemBlock(int par1) {
        super(par1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        BaseEnergyBlock block = (BaseEnergyBlock) Block.blocksList[stack.itemID];
        if (block != null) {
            String name = block.tier.texture;
            list.add(name.substring(0, 4) + " " + name.substring(4));
            switch (block.tier) {
                case FOUR:
                    list.add("Ability to customize field range (max range " + References.getMaxRange() + ")");
                case THREE:
                    list.add("Ability to spawn particles of the field");
                case TWO:
                    list.add("Ability to be turned off");
                    break;
                case ONE:
                    break;
            }
        }
    }

}
