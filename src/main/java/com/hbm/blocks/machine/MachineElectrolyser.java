package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIDMulti;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityElectrolyser;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MachineElectrolyser extends BlockDummyable {

	public MachineElectrolyser() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta >= 12) return new TileEntityElectrolyser();
		if(meta >= 6) return new TileEntityProxyCombo().inventory().power().fluid();
		return null;
	}

	@Override
	public int[] getDimensions() {
		return new int[] {0, 0, 5, 5, 1, 3};
	}

	@Override
	public int getOffset() {
		return 5;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (!world.isRemote) {
            
            int[] pos = this.findCore(world, x, y, z);
            if (pos == null) return false;
            
            if (player.isSneaking()) {
                TileEntityElectrolyser te = (TileEntityElectrolyser) world.getTileEntity(pos[0], pos[1], pos[2]);
                
                if (te != null) {
                    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IItemFluidIdentifier) {
                        FluidType type = ((IItemFluidIdentifier) player.getHeldItem().getItem()).getType(world, pos[0], pos[1], pos[2], player.getHeldItem());
                        
                        if (te.setFluidRC(type)){
                            te.markDirty();
                            ItemFluidIDMulti.chatOnChangeType(player, "chat.machine_electrolyser.abbr", type);
                            return true;
                        }
                    }
                }
            } else {
                FMLNetworkHandler.openGui(player, MainRegistry.instance, -1, world, pos[0], pos[1], pos[2]);
            }
        }
        return true;
	}

	@Override
	public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		super.fillSpace(world, x, y, z, dir, o);

		x += dir.offsetX * o;
		z += dir.offsetZ * o;

		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {2, -1, 5, 5, 1, 1}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {3, -3, 5, 5, 0, 0}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {3, -1, 4, -4, -3, 3}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {3, -1, 2, -2, -3, 3}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {3, -1, 0, 0, -3, 3}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {3, -1, -2, 2, -3, 3}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y, z, new int[] {3, -1, -4, 4, -3, 3}, this, dir);
		MultiblockHandlerXR.fillSpace(world,x + dir.offsetX * 4, y + 3, z + dir.offsetZ * 4, new int[] {0, 0, 0, 0, -1, 2}, this, dir);
		MultiblockHandlerXR.fillSpace(world,x + dir.offsetX * 2, y + 3, z + dir.offsetZ * 2, new int[] {0, 0, 0, 0, -1, 2}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x, y + 3, z, new int[] {0, 0, 0, 0, -1, 2}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x - dir.offsetX * 2, y + 3, z - dir.offsetZ * 2, new int[] {0, 0, 0, 0, -1, 2}, this, dir);
		MultiblockHandlerXR.fillSpace(world, x - dir.offsetX * 4, y + 3, z - dir.offsetZ * 4, new int[] {0, 0, 0, 0, -1, 2}, this, dir);

		ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

		this.makeExtra(world, x - dir.offsetX * 5, y, z - dir.offsetZ * 5);
		this.makeExtra(world, x - dir.offsetX * 5 + rot.offsetX, y, z - dir.offsetZ * 5 + rot.offsetZ);
		this.makeExtra(world, x - dir.offsetX * 5 - rot.offsetX, y, z - dir.offsetZ * 5 - rot.offsetZ);
		this.makeExtra(world, x + dir.offsetX * 5, y, z + dir.offsetZ * 5);
		this.makeExtra(world, x + dir.offsetX * 5 + rot.offsetX, y, z + dir.offsetZ * 5 + rot.offsetZ);
		this.makeExtra(world, x + dir.offsetX * 5 - rot.offsetX, y, z + dir.offsetZ * 5 - rot.offsetZ);
	}

	@Override
	protected boolean checkRequirement(World world, int x, int y, int z, ForgeDirection dir, int o) {

		x += dir.offsetX * o;
		z += dir.offsetZ * o;

		if(!MultiblockHandlerXR.checkSpace(world, x, y , z, getDimensions(), x, y, z, dir)) return false;

		if(!MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {2, -1, 5, 5, 1, 1}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {3, -3, 5, 5, 0, 0}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {3, -1, 4, -4, -3, 3}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {3, -1, 2, -2, -3, 3}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {3, -1, 0, 0, -3, 3}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {3, -1, -2, 2, -3, 3}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x, y, z, new int[] {3, -1, -4, 4, -3, 3}, x, y, z, dir)) return false;

		if(!MultiblockHandlerXR.checkSpace(world, x + dir.offsetX * 4, y + 3, z + dir.offsetZ * 4, new int[] {0, 0, 0, 0, -1, 2}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x + dir.offsetX * 2, y + 3, z + dir.offsetZ * 2, new int[] {0, 0, 0, 0, -1, 2}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x, y + 3, z, new int[] {0, 0, 0, 0, -1, 2}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x - dir.offsetX * 2, y + 3, z - dir.offsetZ * 2, new int[] {0, 0, 0, 0, -1, 2}, x, y, z, dir)) return false;
		if(!MultiblockHandlerXR.checkSpace(world, x - dir.offsetX * 4, y + 3, z - dir.offsetZ * 4, new int[] {0, 0, 0, 0, -1, 2}, x, y, z, dir)) return false;

		return true;
	}

}
