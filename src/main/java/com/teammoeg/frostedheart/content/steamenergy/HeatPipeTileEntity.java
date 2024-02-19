/*
 * Copyright (c) 2021-2024 TeamMoeg
 *
 * This file is part of Frosted Heart.
 *
 * Frosted Heart is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Frosted Heart is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Frosted Heart. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.teammoeg.frostedheart.content.steamenergy;

import com.teammoeg.frostedheart.FHTileTypes;
import com.teammoeg.frostedheart.base.block.PipeTileEntity;
import com.teammoeg.frostedheart.content.steamenergy.capabilities.HeatCapabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class HeatPipeTileEntity extends PipeTileEntity implements ITickableTileEntity,EnergyNetworkProvider, INetworkConsumer {
	HeatEnergyNetwork ntwk;
    public HeatPipeTileEntity() {
        super(FHTileTypes.HEATPIPE.get());
    }

    @Override
    public boolean canConnectAt(Direction to) {
        return true;
    }

    public boolean connect(HeatEnergyNetwork network,Direction to, int ndist) {
        if(ntwk==null||ntwk.getNetworkSize()<network.getNetworkSize()) {
        	ntwk=network;
        }
    	if (ntwk.shouldPropagate(getPos(),ndist)) {
	        this.propagate(to, ntwk, ndist);
        }
        return true;
    }
    public void connectTo(Direction d, HeatEnergyNetwork network, int lengthx) {
    	BlockPos n = this.getPos().offset(d);

        d=d.getOpposite();
        HeatCapabilities.connect(network, getWorld(), n, d, lengthx+1);

    }
    protected void propagate(Direction from, HeatEnergyNetwork network, int lengthx) {
        for (Direction d : Direction.values()) {
            if (from == d) continue;
            connectTo(d,network,lengthx);
        }
        return;
    }

    @Override
    public void readCustomNBT(CompoundNBT nbt, boolean descPacket) {
        if (descPacket) return;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void writeCustomNBT(CompoundNBT nbt, boolean descPacket) {
        if (descPacket) return;
    }

	@Override
	public void onFaceChange(Direction dir, boolean isConnect) {
		if(ntwk==null)return;
		if(isConnect)
			ntwk.startPropagation(this, dir);
		else
			ntwk.requestUpdate();
	}

	@Override
	public HeatEnergyNetwork getNetwork() {
		return ntwk;
	}
}
