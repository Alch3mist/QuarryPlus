/*
 * Copyright (C) 2012,2013 yogpstop
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the
 * GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package org.yogpstop.qp;

import static org.yogpstop.qp.PacketHandler.*;

import java.util.LinkedList;
import java.util.List;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import static buildcraft.BuildCraftFactory.plainPipeBlock;
import static buildcraft.core.utils.Utils.addToRandomPipeEntry;
import static buildcraft.core.utils.Utils.addToRandomInventory;

public class TileMiningWell extends TileBasic {

	private boolean working;

	@Override
	protected void C_recievePacket(byte pattern, ByteArrayDataInput data) {
		super.C_recievePacket(pattern, data);
		switch (pattern) {
		case packetNow:
			this.working = data.readBoolean();
			if (this.working) PowerManager.configureW(this.pp, this.efficiency, this.unbreaking);
			else PowerManager.configure0(this.pp);
			this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
			break;
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (this.worldObj.isRemote) return;
		int depth = this.yCoord - 1;
		while (!S_checkTarget(depth)) {
			if (this.working) this.worldObj.setBlock(this.xCoord, depth, this.zCoord, plainPipeBlock.blockID);
			depth--;
		}
		if (this.working) S_breakBlock(depth);
		List<ItemStack> cache = new LinkedList<ItemStack>();
		for (ItemStack is : this.cacheItems) {
			int added = addToRandomInventory(is, this.worldObj, this.xCoord, this.yCoord, this.zCoord, ForgeDirection.UNKNOWN).stackSize;
			is.stackSize -= added;
			if (is.stackSize > 0) {
				if (!addToRandomPipeEntry(this, ForgeDirection.UNKNOWN, is)) cache.add(is);
			}
		}
		this.cacheItems = cache;
	}

	private boolean S_checkTarget(int depth) {
		if (depth < 1) {
			G_destroy();
			return true;
		}
		int bid = this.worldObj.getBlockId(this.xCoord, depth, this.zCoord);
		if (bid == 0 || bid == Block.bedrock.blockID || bid == plainPipeBlock.blockID) return false;
		if (this.pump == ForgeDirection.UNKNOWN && this.worldObj.getBlockMaterial(this.xCoord, depth, this.zCoord).isLiquid()) return false;
		if (!this.working) {
			PowerManager.configureW(this.pp, this.efficiency, this.unbreaking);
			this.working = true;
			sendNowPacket(this, (byte) 1);
		}
		return true;
	}

	private boolean S_breakBlock(int depth) {
		return S_breakBlock(this.xCoord, depth, this.zCoord, PowerManager.BreakType.MiningWell);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttc) {
		super.readFromNBT(nbttc);
		this.working = nbttc.getBoolean("working");
		if (this.working) PowerManager.configureW(this.pp, this.efficiency, this.unbreaking);
		else PowerManager.configure0(this.pp);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttc) {
		super.writeToNBT(nbttc);
		nbttc.setBoolean("working", this.working);
	}

	@Override
	protected void G_reinit() {
		PowerManager.configureW(this.pp, this.efficiency, this.unbreaking);
		this.working = true;
		sendNowPacket(this, (byte) 1);
	}

	@Override
	protected void G_destroy() {
		if (this.worldObj.isRemote) return;
		PowerManager.configure0(this.pp);
		this.working = false;
		sendNowPacket(this, (byte) 0);
		for (int depth = this.yCoord - 1; depth > 0; depth--) {
			if (this.worldObj.getBlockId(this.xCoord, depth, this.zCoord) != plainPipeBlock.blockID) {
				break;
			}
			this.worldObj.setBlock(this.xCoord, depth, this.zCoord, 0);
		}
	}

	@Override
	public boolean isActive() {
		return this.working;
	}
}
