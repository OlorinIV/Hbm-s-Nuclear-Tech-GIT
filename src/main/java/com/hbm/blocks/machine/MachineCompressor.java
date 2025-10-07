package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIDMulti;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityMachineCompressor;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MachineCompressor extends BlockDummyable {

	public MachineCompressor() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta >= 12) return new TileEntityMachineCompressor();
		if(meta >= extra) return new TileEntityProxyCombo().fluid().power();
		
		return null;
	}

	@Override
	public int[] getDimensions() {
		return new int[] {2, 0, 1, 2, 1, 1};
	}

	@Override
	public int getOffset() {
		return 2;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (!world.isRemote) {
            
            int[] pos = this.findCore(world, x, y, z);
            if (pos == null) return false;
            
            if (player.isSneaking()) {
                TileEntityMachineCompressor te = (TileEntityMachineCompressor) world.getTileEntity(pos[0], pos[1], pos[2]);
                
                if (te != null) {
                    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IItemFluidIdentifier) {
                        FluidType type = ((IItemFluidIdentifier) player.getHeldItem().getItem()).getType(world, pos[0], pos[1], pos[2], player.getHeldItem());
                        
                        te.tanks[0].setTankType(type);
                        te.markDirty();
                        ItemFluidIDMulti.chatOnChangeType(player, "tile.machine_compressor.name", type);
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
	protected boolean checkRequirement(World world, int x, int y, int z, ForgeDirection dir, int o) {
		return super.checkRequirement(world, x, y, z, dir, o) &&
				MultiblockHandlerXR.checkSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[] {3, -3, 1, 1, 1, 1}, x, y, z, dir) &&
				MultiblockHandlerXR.checkSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[] {8, -4, 0, 0, 1, 1}, x, y, z, dir);
	}

	@Override
	public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		super.fillSpace(world, x, y, z, dir, o);

		MultiblockHandlerXR.fillSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[] {3, -3, 1, 1, 1, 1}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x + dir.offsetX * o, y + dir.offsetY * o, z + dir.offsetZ * o, new int[] {8, -4, 0, 0, 1, 1}, this, dir);

		x += dir.offsetX * o;
		z += dir.offsetZ * o;
		
		ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

		this.makeExtra(world, x - dir.offsetX, y, z - dir.offsetZ);
		this.makeExtra(world, x + rot.offsetX, y, z + rot.offsetZ);
		this.makeExtra(world, x - rot.offsetX, y, z - rot.offsetZ);
	}
}
