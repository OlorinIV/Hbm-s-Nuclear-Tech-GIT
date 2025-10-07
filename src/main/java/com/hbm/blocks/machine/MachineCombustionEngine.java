package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIDMulti;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.TileEntityMachineCombustionEngine;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MachineCombustionEngine extends BlockDummyable {

	public MachineCombustionEngine() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta >= 12)
			return new TileEntityMachineCombustionEngine();
		if(hasExtra(meta))
			return new TileEntityProxyCombo().power().fluid();
		return null;
	}

	@Override
	public int[] getDimensions() {
		return new int[] {1, 0, 1, 0, 3, 2};
	}

	@Override
	public int getOffset() {
		return 0;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (!world.isRemote) {
            
            int[] pos = this.findCore(world, x, y, z);
            if (pos == null) return false;
            
            if (player.isSneaking()) {
                TileEntityMachineCombustionEngine te = (TileEntityMachineCombustionEngine) world.getTileEntity(pos[0], pos[1], pos[2]);
                
                if (te != null) {
                    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IItemFluidIdentifier) {
                        FluidType type = ((IItemFluidIdentifier) player.getHeldItem().getItem()).getType(world, pos[0], pos[1], pos[2], player.getHeldItem());
                        
                        if (te.setFuelRC(type)) {
                            te.markDirty();
                            ItemFluidIDMulti.chatOnChangeType(player, "chat.machine_combustion_engine.abbr", type);
                            return true;
                        }
                    }
                }
            } else {
                FMLNetworkHandler.openGui(player, MainRegistry.instance, 0, world, pos[0], pos[1], pos[2]);
            }
        }
        return true;
	}

	protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		super.fillSpace(world, x, y, z, dir, o);

		ForgeDirection rot = dir.getRotation(ForgeDirection.UP);

		this.makeExtra(world, x + rot.offsetX, y, z + rot.offsetZ);
		this.makeExtra(world, x - rot.offsetX, y, z - rot.offsetZ);
		this.makeExtra(world, x - dir.offsetX + rot.offsetX, y, z - dir.offsetZ + rot.offsetZ);
		this.makeExtra(world, x - dir.offsetX - rot.offsetX, y, z - dir.offsetZ - rot.offsetZ);
	}
}
