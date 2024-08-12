package com.hbm.items.armor;

import com.hbm.handler.ArmorModHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemModNightVisionMK2 extends ItemModNightVision {
	public ItemModNightVisionMK2() {
		super();
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool) {
		list.add(EnumChatFormatting.AQUA + I18n.format("item.night_vision_mk2.description.item"));
		list.add("");
		super.addInformation(itemstack, player, list, bool);
	}

	@Override
	public void modUpdate(EntityLivingBase entity, ItemStack armor) {
		if(!entity.worldObj.isRemote && entity instanceof EntityPlayer) {
			entity.addPotionEffect(new PotionEffect(Potion.nightVision.id, 15 * 20, 0));
		}
	}
}
