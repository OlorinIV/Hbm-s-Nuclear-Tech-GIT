package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIDMulti;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityMachineExcavator;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MachineExcavator extends BlockDummyable {

	public MachineExcavator() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {

		if(meta >= 12) return new TileEntityMachineExcavator();
		if(meta >= 6) return new TileEntityProxyCombo().power().fluid();
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (!world.isRemote) {
            
            int[] pos = this.findCore(world, x, y, z);
            if (pos == null) return false;
            
            if (player.isSneaking()) {
                TileEntityMachineExcavator te = (TileEntityMachineExcavator) world.getTileEntity(pos[0], pos[1], pos[2]);
                
                if (te != null) {
                    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IItemFluidIdentifier) {
                        FluidType type = ((IItemFluidIdentifier) player.getHeldItem().getItem()).getType(world, pos[0], pos[1], pos[2], player.getHeldItem());
                        
                        te.tank.setTankType(type);
                        te.markDirty();
                        ItemFluidIDMulti.chatOnChangeType(player, "chat.machine_excavator.abbr", type);
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
		return new int[] {3, 0, 3, 3, 3, 3};
	}

	@Override
	public int getOffset() {
		return 3;
	}

	@Override
	public int getHeightOffset() {
		return 3;
	}

	@Override
	protected boolean checkRequirement(World world, int x, int y, int z, ForgeDirection dir, int o) {
		x += dir.offsetX * o;
		y += dir.offsetY * o;
		z += dir.offsetZ * o;
		
		return MultiblockHandlerXR.checkSpace(world, x, y, z, getDimensions(), x, y, z, dir) && 
				MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {-1, 3, 3, -2, 3, -2}, x, y, z, dir) &&
				MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {-1, 3, 3, -2, -2, 3}, x, y, z, dir) &&
				MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {-1, 3, -2, 3, 3, 3}, x, y, z, dir);
	}

	@Override
	protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		x += dir.offsetX * o;
		y += dir.offsetY * o;
		z += dir.offsetZ * o;

		MultiblockHandlerXR.fillSpace(world, x, y, z, getDimensions(), this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {-1, 3, 3, -2, 3, -2}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {-1, 3, 3, -2, -2, 3}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {-1, 3, -2, 3, 3, 3}, this, dir);
		
		ForgeDirection rot = dir.getRotation(ForgeDirection.UP);
		this.makeExtra(world, x + dir.offsetX * 3 + rot.offsetX, y + 1, z + dir.offsetZ * 3 + rot.offsetZ);
		this.makeExtra(world, x + dir.offsetX * 3 - rot.offsetX, y + 1, z + dir.offsetZ * 3 - rot.offsetZ);
		this.makeExtra(world, x + rot.offsetX * 3, y + 1, z + rot.offsetZ * 3);
		this.makeExtra(world, x - rot.offsetX * 3, y + 1, z - rot.offsetZ * 3);
	}
}
