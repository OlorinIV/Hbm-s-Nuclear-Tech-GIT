package com.hbm.tileentity.machine.albion;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.inventory.fluid.trait.FT_Heatable.*;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.BobMathUtil;
import com.hbm.util.fauxpointtwelve.DirPos;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityCooledBase extends TileEntityMachineBase implements IEnergyReceiverMK2, IFluidStandardTransceiverMK2 {
	
	public FluidTank[] coolantTanks; // originally just named "tanks" which would confuse the fuck out of me when working with child classes
	
	public long power;
	
	public static final float KELVIN = 273F;
	public float temperature = KELVIN + 20;
	public static final float temperature_target = KELVIN - 150F;
	public static final float temp_change_per_mb = 0.5F;
	public static final float temp_passive_heating = 2.5F;
	public static final float temp_change_max = 5F + temp_passive_heating;

	public TileEntityCooledBase(int slotCount) {
		super(slotCount);
		coolantTanks = new FluidTank[2];
		coolantTanks[0] = new FluidTank(Fluids.PERFLUOROMETHYL_COLD, 4_000);
		coolantTanks[1] = new FluidTank(Fluids.PERFLUOROMETHYL, 4_000);
	}

	@Override
	public void updateEntity() {
		
		if(!worldObj.isRemote) {

			for(DirPos pos : this.getConPos()) {
				this.trySubscribe(worldObj, pos.getX(), pos.getY(), pos.getZ(), pos.getDir());
				this.trySubscribe(coolantTanks[0].getTankType(), worldObj, pos.getX(), pos.getY(), pos.getZ(), pos.getDir());
				this.tryProvide(coolantTanks[1], worldObj, pos.getX(), pos.getY(), pos.getZ(), pos.getDir());
			}
			
			this.temperature += this.temp_passive_heating;
			if(this.temperature > KELVIN + 20) this.temperature = KELVIN + 20;
			
			if(this.temperature > this.temperature_target) {
                //Changeable cooling fluid
                FT_Heatable trait = coolantTanks[0].getTankType().getTrait(FT_Heatable.class);
                if(trait.getEfficiency(HeatingType.PA) == 0) {
                    this.networkPackNT(50);
                    return;
                }
                HeatingStep step = trait.getFirstStep();
                coolantTanks[1].setTankType(step.typeProduced);

                int cyclesTemp = (int) Math.ceil((Math.min(this.temperature - temperature_target, temp_change_max)) / temp_change_per_mb / step.amountReq);
				int cyclesCool = coolantTanks[0].getFill() / step.amountReq;
				int cyclesHot = step.amountProduced == 0 ? cyclesCool : (coolantTanks[1].getMaxFill() - coolantTanks[1].getFill()) / step.amountProduced;
				int cycles = BobMathUtil.min(cyclesTemp, cyclesCool, cyclesHot);

				coolantTanks[0].setFill(coolantTanks[0].getFill() - cycles * step.amountReq);
				coolantTanks[1].setFill(coolantTanks[1].getFill() + cycles * step.amountProduced);
				this.temperature -= this.temp_change_per_mb * cycles;
			}
			
			this.networkPackNT(50);
		}
	}
	
	public boolean isCool() {
		return this.temperature <= this.temperature_target;
	}

    public boolean setCoolantRC(FluidType type) {
        FT_Heatable trait = type.getTrait(FT_Heatable.class);
        if(trait != null && trait.getEfficiency(HeatingType.PA) > 0) {
            coolantTanks[0].setTankType(type);
            return true;
        }
        return false;
    }

	public abstract DirPos[] getConPos();

	@Override
	public void serialize(ByteBuf buf) {
		super.serialize(buf);
		coolantTanks[0].serialize(buf);
		coolantTanks[1].serialize(buf);
		buf.writeFloat(temperature);
		buf.writeLong(power);
	}

	@Override
	public void deserialize(ByteBuf buf) {
		super.deserialize(buf);
		coolantTanks[0].deserialize(buf);
		coolantTanks[1].deserialize(buf);
		this.temperature = buf.readFloat();
		this.power = buf.readLong();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		coolantTanks[0].readFromNBT(nbt, "t0");
		coolantTanks[1].readFromNBT(nbt, "t1");
		this.temperature = nbt.getFloat("temperature");
		this.power = nbt.getLong("power");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		coolantTanks[0].writeToNBT(nbt, "t0");
		coolantTanks[1].writeToNBT(nbt, "t1");
		nbt.setFloat("temperature", temperature);
		nbt.setLong("power", power);
	}
	
	@Override public long getPower() { return this.power; }
	@Override public void setPower(long power) { this.power = power; }

	@Override public FluidTank[] getSendingTanks() { return new FluidTank[] {coolantTanks[1]}; }
	@Override public FluidTank[] getReceivingTanks() { return new FluidTank[] {coolantTanks[0]}; }
	@Override public FluidTank[] getAllTanks() { return coolantTanks; }
}
