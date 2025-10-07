package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIDMulti;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityMachineArcWelder;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MachineArcWelder extends BlockDummyable {

	public MachineArcWelder(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta >= 12) return new TileEntityMachineArcWelder();
		return new TileEntityProxyCombo().inventory().power().fluid();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (!world.isRemote) {
            
            int[] pos = this.findCore(world, x, y, z);
            if (pos == null) return false;
            
            if (player.isSneaking()) {
                TileEntityMachineArcWelder te = (TileEntityMachineArcWelder) world.getTileEntity(pos[0], pos[1], pos[2]);
                
                if (te != null) {
                    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IItemFluidIdentifier) {
                        FluidType type = ((IItemFluidIdentifier) player.getHeldItem().getItem()).getType(world, pos[0], pos[1], pos[2], player.getHeldItem());
                        
                        te.tank.setTankType(type);
                        te.markDirty();
                        ItemFluidIDMulti.chatOnChangeType(player, "tile.machine_arc_welder.name", type);
                        return true;
                    }
                }
            } else {
                FMLNetworkHandler.openGui(player, MainRegistry.instance, 0, world, pos[0], pos[1], pos[2]);
            }
        }
        return true;
	}

	@Override
	public int[] getDimensions() {
		return new int[] {1, 0, 1, 0, 1, 1};
	}

	@Override
	public int getOffset() {
		return 0;
	}
}
