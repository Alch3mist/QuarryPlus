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

package org.yogpstop.qp.client;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.StatCollector;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.Side;

@SideOnly(Side.CLIENT)
public class GuiError extends GuiScreenA {
	private String message1;
	private String message2;

	public GuiError(GuiScreen par1GuiScreen, String par2Str, String par3Str) {
		super(par1GuiScreen);
		this.message1 = par2Str;
		this.message2 = par3Str;
	}

	@Override
	public void initGui() {
		this.controlList.add(new GuiSmallButton(0, this.width / 2 - 75, this.height / 6 + 96, StatCollector.translateToLocal("gui.done")));
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		showParent();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.message1, this.width / 2, 70, 0xFFFFFF);
		this.drawCenteredString(this.fontRenderer, this.message2, this.width / 2, 90, 0xFFFFFF);
		super.drawScreen(par1, par2, par3);
	}
}
