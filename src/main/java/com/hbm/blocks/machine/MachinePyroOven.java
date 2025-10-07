package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIDMulti;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.oil.TileEntityMachinePyroOven;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MachinePyroOven extends BlockDummyable {

	public MachinePyroOven(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta >= 12) return new TileEntityMachinePyroOven();
		if(meta >= 6) return new TileEntityProxyCombo().inventory().power().fluid();
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (!world.isRemote) {
            
            int[] pos = this.findCore(world, x, y, z);
            if (pos == null) return false;
            
            if (player.isSneaking()) {
                TileEntityMachinePyroOven te = (TileEntityMachinePyroOven) world.getTileEntity(pos[0], pos[1], pos[2]);
                
                if (te != null) {
                    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IItemFluidIdentifier) {
                        FluidType type = ((IItemFluidIdentifier) player.getHeldItem().getItem()).getType(world, pos[0], pos[1], pos[2], player.getHeldItem());
                        
                        te.tanks[0].setTankType(type);
                        te.markDirty();
                        ItemFluidIDMulti.chatOnChangeType(player, "chat.machine_pyrooven.abbr", type);
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
		return new int[] {2, 0, 3, 3, 2, 2};
	}

	@Override
	public int getOffset() {
		return 3;
	}

	@Override
	protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		super.fillSpace(world, x, y, z, dir, o);
		x += dir.offsetX * o;
		z += dir.offsetZ * o;
		
		ForgeDirection rot = dir.getRotation(ForgeDirection.DOWN);
		
		for(int i = -2; i <= 2; i++) {
			this.makeExtra(world, x + dir.offsetX * i + rot.offsetX * 2, y, z + dir.offsetZ * i + rot.offsetZ * 2);
		}
		
		this.makeExtra(world, x - rot.offsetX, y + 2, z - rot.offsetZ);
	}
}
