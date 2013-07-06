package samrg472.ref.items;

import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;

import samrg472.ref.blocks.BaseEnergyBlock;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class REFItemBlock extends ItemBlock {

	public REFItemBlock(int par1) {
		super(par1);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		BaseEnergyBlock block = (BaseEnergyBlock) Block.blocksList[stack.itemID];
		if(block != null) {
			String name = block.tier.texture;
			list.add(name.substring(0, 4) + " " + name.substring(4));
		}
	}

}
