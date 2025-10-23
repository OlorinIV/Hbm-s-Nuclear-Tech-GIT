package com.hbm.blocks.machine.rbmk;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIDMulti;
import com.hbm.items.machine.ItemRBMKLid;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.rbmk.TileEntityRBMKHeater;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RBMKHeater extends RBMKBase {

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		
		if(meta >= this.offset)
			return new TileEntityRBMKHeater();
		
		if(hasExtra(meta))
			return new TileEntityProxyCombo(false, false, true);

		return null;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (!world.isRemote) {
            
            int[] pos = this.findCore(world, x, y, z);
            if (pos == null) return false;
            TileEntityRBMKHeater te = (TileEntityRBMKHeater) world.getTileEntity(pos[0], pos[1], pos[2]);
            if (te == null) return false;
            
            if (player.getHeldItem() != null) { 
                
                if (player.getHeldItem().getItem() instanceof ItemRBMKLid) {
                    if (!te.hasLid()) return false;
                    
                } else if (player.isSneaking() && player.getHeldItem().getItem() instanceof IItemFluidIdentifier) {
                    FluidType type = ((IItemFluidIdentifier) player.getHeldItem().getItem()).getType(world, pos[0], pos[1], pos[2], player.getHeldItem());
                    
                    if (te.setCoolantRC(type)) {
                        te.markDirty();
                        ItemFluidIDMulti.chatOnChangeType(player, "chat.rbmk_heater.abbr", type);
                        return true;
                    }
                }
            }
            
            if (!player.isSneaking()) {
                FMLNetworkHandler.openGui(player, MainRegistry.instance, 0, world, pos[0], pos[1], pos[2]);
                return true;
            }
        }
        return true;
	}
	
	@Override
	public int getRenderType(){
		return this.renderIDControl;
	}
}
