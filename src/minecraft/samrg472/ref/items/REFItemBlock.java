package samrg472.ref.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import samrg472.ref.References;
import samrg472.ref.blocks.BaseEnergyBlock;

import java.util.List;

public class REFItemBlock extends ItemBlock {

    public REFItemBlock(Block block) {
        super(block);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        BaseEnergyBlock block = (BaseEnergyBlock) Block.getBlockFromItem(stack.getItem());
        if (block != null) {
            String name = block.tier.texture;
            list.add(name.substring(0, 4) + " " + name.substring(4));
            switch (block.tier) {
                case FOUR:
                    list.add("\u00A76Ability to customize field range (max " + References.getMaxRange() + ").");
                case THREE:
                    list.add("\u00A7dAbility to see the field's range.");
                case TWO:
                    list.add("\u00A7fAbility to be turned off.");
                    break;
                case ONE:
                    break;
            }
        }
    }

}
