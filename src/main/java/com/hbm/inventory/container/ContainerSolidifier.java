package com.hbm.inventory.container;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.inventory.SlotUpgrade;
import com.hbm.items.ModItems;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.ItemMachineUpgrade;
import com.hbm.tileentity.machine.oil.TileEntityMachineSolidifier;

import com.hbm.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSolidifier extends Container {
	
	private TileEntityMachineSolidifier solidifier;

	public ContainerSolidifier(InventoryPlayer playerInv, TileEntityMachineSolidifier tile) {
		solidifier = tile;
		
		//Input
		this.addSlotToContainer(new Slot(tile, 0, 71, 45));
		//Battery
		this.addSlotToContainer(new Slot(tile, 1, 134, 72));
		//Upgrades
		this.addSlotToContainer(new SlotUpgrade(tile, 2, 98, 36));
		this.addSlotToContainer(new SlotUpgrade(tile, 3, 98, 54));
		//ID
		this.addSlotToContainer(new Slot(tile, 4, 71, 72));

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 122 + i * 18));
			}
		}

		for(int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 180));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return solidifier.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack rStack = null;
        Slot slot = (Slot) this.inventorySlots.get(index);
        
        if(slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            rStack = stack.copy();
            
            if(index <= 4) {
                if(!this.mergeItemStack(stack, 5, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else {
                
                if(rStack.getItem() instanceof IBatteryItem || rStack.getItem() == ModItems.battery_creative) {
                    if(!InventoryUtil.mergeItemStack(this.inventorySlots, stack, 1, 2, false)) return null;
                } else if(rStack.getItem() instanceof ItemMachineUpgrade) {
                    if(!InventoryUtil.mergeItemStack(this.inventorySlots, stack, 2, 4, false)) return null;
                } else if (rStack.getItem() instanceof IItemFluidIdentifier) {
                    if(!InventoryUtil.mergeItemStack(this.inventorySlots, stack, 4, 5, false)) return null;
                } else {
                    if(!InventoryUtil.mergeItemStack(this.inventorySlots, stack, 0, 1, false)) return null;
                }
            }
            
            if(stack == null || stack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }
        
        return rStack;
	}
}
