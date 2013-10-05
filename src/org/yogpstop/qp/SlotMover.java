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

import net.minecraft.src.Container;
import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class SlotMover extends Slot {
	Container parentsC;

	public SlotMover(IInventory par1iInventory, int par2, int par3, int par4, Container c) {
		super(par1iInventory, par2, par3, par4);
		this.parentsC = c;

	}

	@Override
	public boolean isItemValid(ItemStack is) {
		switch (this.slotNumber) {
		case 0:
			if (is.itemID == Item.pickaxeDiamond.shiftedIndex) { return true; }
			return false;
		case 1:
			if (is.itemID == QuarryPlus.blockQuarry.blockID || is.itemID == QuarryPlus.blockMiningWell.blockID || is.itemID == QuarryPlus.blockPump.blockID
					|| is.itemID == QuarryPlus.blockRefinery.blockID || (is.itemID == QuarryPlus.itemTool.shiftedIndex && is.getItemDamage() == 1)) return true;
		}
		return false;
	}

	@Override
	public void onSlotChanged() {
		this.parentsC.onCraftMatrixChanged(this.inventory);
		super.onSlotChanged();
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}
}
