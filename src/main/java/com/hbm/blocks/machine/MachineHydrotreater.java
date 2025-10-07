package com.hbm.blocks.machine;

import com.hbm.blocks.BlockDummyable;
import com.hbm.blocks.IPersistentInfoProvider;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemFluidIDMulti;
import com.hbm.main.MainRegistry;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.hbm.tileentity.machine.oil.TileEntityMachineHydrotreater;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class MachineHydrotreater extends BlockDummyable implements IPersistentInfoProvider {

	public MachineHydrotreater(Material mat) {
		super(mat);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if(meta >= 12) return new TileEntityMachineHydrotreater();
		if(meta >= 6) return new TileEntityProxyCombo().fluid().power();
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        
        if (!world.isRemote) {
            
            int[] pos = this.findCore(world, x, y, z);
            if (pos == null) return false;
            
            if (player.isSneaking()) {
                TileEntityMachineHydrotreater te = (TileEntityMachineHydrotreater) world.getTileEntity(pos[0], pos[1], pos[2]);
                
                if (te != null) {
                    if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IItemFluidIdentifier) {
                        FluidType type = ((IItemFluidIdentifier) player.getHeldItem().getItem()).getType(world, pos[0], pos[1], pos[2], player.getHeldItem());
                        
                        if (te.setOilRC(type)) {
                            te.markDirty();
                            ItemFluidIDMulti.chatOnChangeType(player, "tile.machine_hydrotreater.name", type);
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

	@Override public int[] getDimensions() { return new int[] {6, 0, 1, 1, 1, 1}; }
	@Override public int getOffset() { return 1; }

	@Override
	protected void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
		super.fillSpace(world, x, y, z, dir, o);

		this.makeExtra(world, x - dir.offsetX + 1, y, z - dir.offsetZ + 1);
		this.makeExtra(world, x - dir.offsetX + 1, y, z - dir.offsetZ - 1);
		this.makeExtra(world, x - dir.offsetX - 1, y, z - dir.offsetZ + 1);
		this.makeExtra(world, x - dir.offsetX - 1, y, z - dir.offsetZ - 1);
	}

	@Override
	public void addInformation(ItemStack stack, NBTTagCompound persistentTag, EntityPlayer player, List list, boolean ext) {

		for(int i = 0; i < 4; i++) {
			FluidTank tank = new FluidTank(Fluids.NONE, 0);
			tank.readFromNBT(persistentTag, "" + i);
			list.add(EnumChatFormatting.YELLOW + "" + tank.getFill() + "/" + tank.getMaxFill() + "mB " + tank.getTankType().getLocalizedName());
		}
	}
}
