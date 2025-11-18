package com.hbm.blocks.machine;

import java.util.List;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIDMulti;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.oil.TileEntityMachineCoker;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MachineCoker extends BlockDummyable implements ITooltipProvider {

	public MachineCoker(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		
		if(meta >= 12) return new TileEntityMachineCoker();
		if(meta >= extra) return new TileEntityProxyCombo().inventory().fluid();
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (!world.isRemote) {
            
            int[] pos = this.findCore(world, x, y, z);
            if (pos == null) return false;
            
            if (player.isSneaking()) {
                TileEntityMachineCoker te = (TileEntityMachineCoker) world.getTileEntity(pos[0], pos[1], pos[2]);
                
                if (te != null) {
                    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IItemFluidIdentifier) {
                        FluidType type = ((IItemFluidIdentifier) player.getHeldItem().getItem()).getType(world, pos[0], pos[1], pos[2], player.getHeldItem());
                        
                        if (te.setOilRC(type)) {
                            te.markDirty();
                            ItemFluidIDMulti.chatOnChangeType(player, "tile.machine_coker.name", type);
                            return true;
                        }
                    }
                }
            } else {
                FMLNetworkHandler.openGui(player, MainRegistry.instance, side, world, pos[0], pos[1], pos[2]);
            }
        }
        return true;
	}

	@Override
	public int[] getDimensions() {
		return new int[] {22, 0, 1, 1, 1, 1};
	}

	@Override
	public int getOffset() {
		return 1;
	}

	@Override
	protected boolean checkRequirement(World world, int x, int y, int z, ForgeDirection dir, int o) {
		if(super.checkRequirement(world, x, y, z, dir, o)) {

			x += dir.offsetX * o;
			z += dir.offsetZ * o;
			
			return MultiblockHandlerXR.checkSpace(world, x, y + 1, z, new int[] {5, 0, 2, 2, 2, 2}, x, y, z, ForgeDirection.NORTH) &&
					MultiblockHandlerXR.checkSpace(world, x + 2, y + 1, z + 2, new int[] {0, 1, 0, 0, 0, 0}, x, y, z, ForgeDirection.NORTH) &&
					MultiblockHandlerXR.checkSpace(world, x + 2, y + 1, z - 2, new int[] {0, 1, 0, 0, 0, 0}, x, y, z, ForgeDirection.NORTH) &&
					MultiblockHandlerXR.checkSpace(world, x - 2, y + 1, z + 2, new int[] {0, 1, 0, 0, 0, 0}, x, y, z, ForgeDirection.NORTH) &&
					MultiblockHandlerXR.checkSpace(world, x - 2, y + 1, z - 2, new int[] {0, 1, 0, 0, 0, 0}, x, y, z, ForgeDirection.NORTH);
		}
		
		return false;
	}

	@Override
	protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		super.fillSpace(world, x, y, z, dir, o);

		x += dir.offsetX * o;
		z += dir.offsetZ * o;

		MultiblockHandlerXR.fillSpace(world, x, y + 1, z, new int[] {5, 0, 2, 2, 2, 2}, this, ForgeDirection.NORTH);
		MultiblockHandlerXR.fillSpace(world, x + 2, y + 1, z + 2, new int[] {0, 1, 0, 0, 0, 0}, this, ForgeDirection.NORTH);
		MultiblockHandlerXR.fillSpace(world, x + 2, y + 1, z - 2, new int[] {0, 1, 0, 0, 0, 0}, this, ForgeDirection.NORTH);
		MultiblockHandlerXR.fillSpace(world, x - 2, y + 1, z + 2, new int[] {0, 1, 0, 0, 0, 0}, this, ForgeDirection.NORTH);
		MultiblockHandlerXR.fillSpace(world, x - 2, y + 1, z - 2, new int[] {0, 1, 0, 0, 0, 0}, this, ForgeDirection.NORTH);

		this.makeExtra(world, x + 1, y, z + 1);
		this.makeExtra(world, x + 1, y, z - 1);
		this.makeExtra(world, x - 1, y, z + 1);
		this.makeExtra(world, x - 1, y, z - 1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean ext) {
		this.addStandardInfo(stack, player, list, ext);
	}
}
