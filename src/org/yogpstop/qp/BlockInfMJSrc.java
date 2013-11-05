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

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockInfMJSrc extends BlockContainer {

	public BlockInfMJSrc(int par1) {
		super(par1, Material.iron);
		setHardness(1.5F);
		setResistance(10F);
		setStepSound(soundStoneFootstep);
		setCreativeTab(CreativeTabs.tabRedstone);
		setBlockName("InfMJSrc");
		this.blockIndexInTexture = Block.portal.blockIndexInTexture;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileInfMJSrc();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer ep, int par6, float par7, float par8, float par9) {
		if (!world.isRemote) ((TileInfMJSrc) world.getBlockTileEntity(x, y, z)).S_openGUI(ep);
		return true;
	}
}
