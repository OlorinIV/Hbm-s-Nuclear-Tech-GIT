package com.hbm.items.machine;

import java.util.Locale;

import com.hbm.items.ItemEnumMulti;
import com.hbm.util.EnumUtil;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemRecipeIcon extends ItemEnumMulti {

	public ItemRecipeIcon() {
		super(EnumCustomRecipeIcon.class, true, true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		Enum[] enums = theEnum.getEnumConstants();
		this.icons = new IIcon[enums.length];

		for(int i = 0; i < icons.length; i++) {
			Enum num = enums[i];
			this.icons[i] = reg.registerIcon(this.getIconString() + "_" + num.name().toLowerCase(Locale.US));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		Enum num = EnumUtil.grabEnumSafely(theEnum, stack.getItemDamage());
		return "item.recipe_icon_" + num.name().toLowerCase(Locale.US);
	}

	public static enum EnumCustomRecipeIcon {
		fp_heavyoil,
		fp_heavyoil_vacuum,
		fp_smear,
		fp_naphtha,
		fp_naphtha_ds,
		fp_naphtha_crack,
		fp_naphtha_coker,
		fp_oil_coker,
		fp_gas_coker,
		fp_lightoil,
		fp_lightoil_ds,
		fp_lightoil_crack,
		fp_lightoil_vacuum,
		fp_coalcreosote,
		fp_coaloil,
		fp_reformate,
		fp_egg,
		fp_chlorocalcite_mix,
		fp_bauxite_solution;

		private EnumCustomRecipeIcon() {
		}
	}
}
