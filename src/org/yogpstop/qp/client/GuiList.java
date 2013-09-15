package org.yogpstop.qp.client;

import org.yogpstop.qp.PacketHandler;
import org.yogpstop.qp.TileBasic;

import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.Side;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StatCollector;

import static org.yogpstop.qp.QuarryPlus.getname;

@SideOnly(Side.CLIENT)
public class GuiList extends GuiScreen {
	private GuiSlotList oreslot;
	private GuiButton delete;
	private TileBasic tile;
	private byte targetid;

	public GuiList(byte id, TileBasic tq) {
		super();
		this.targetid = id;
		this.tile = tq;
	}

	public boolean include() {
		if (this.targetid == 0) return this.tile.fortuneInclude;
		return this.tile.silktouchInclude;
	}

	@Override
	public void initGui() {
		this.controlList.add(new GuiButton(-1, this.width / 2 - 125, this.height - 26, 250, 20, StatCollector.translateToLocal("gui.done")));
		this.controlList.add(new GuiButton(PacketHandler.fortuneTInc + this.targetid, this.width * 2 / 3 + 10, 50, 100, 20, StatCollector
				.translateToLocal(include() ? "tof.include" : "tof.exclude")));
		this.controlList.add(new GuiButton(-2, this.width * 2 / 3 + 10, 80, 100, 20, StatCollector.translateToLocal("tof.addnewore") + "("
				+ StatCollector.translateToLocal("tof.manualinput") + ")"));
		this.controlList.add(new GuiButton(-3, this.width * 2 / 3 + 10, 50, 100, 20, StatCollector.translateToLocal("tof.addnewore") + "("
				+ StatCollector.translateToLocal("tof.fromlist") + ")"));
		this.controlList.add(this.delete = new GuiButton(PacketHandler.fortuneRemove + this.targetid, this.width * 2 / 3 + 10, 110, 100, 20, StatCollector
				.translateToLocal("selectServer.delete")));
		this.oreslot = new GuiSlotList(this.mc, this.width * 3 / 5, this.height, 30, this.height - 30, 18, this, this.targetid == 0 ? this.tile.fortuneList
				: this.tile.silktouchList);
	}

	@Override
	public void actionPerformed(GuiButton par1) {
		switch (par1.id) {
		case -1:
			this.mc.displayGuiScreen(null);
			break;
		case -2:
			this.mc.displayGuiScreen(new GuiManual(this, this.targetid, this.tile));
			break;
		case -3:
			this.mc.displayGuiScreen(new GuiSelectBlock(this, this.tile, this.targetid));
			break;
		case PacketHandler.fortuneRemove:
		case PacketHandler.silktouchRemove:
			this.mc.displayGuiScreen(new GuiYesNo(this, StatCollector.translateToLocal("tof.deleteblocksure"),
					getname((this.targetid == 0 ? this.tile.fortuneList : this.tile.silktouchList).get(this.oreslot.currentore)), par1.id));
			break;
		default:
			PacketHandler.sendPacketToServer(this.tile, (byte) par1.id);
			break;
		}
	}

	@Override
	public void drawScreen(int i, int j, float k) {
		this.drawDefaultBackground();
		this.oreslot.drawScreen(i, j, k);
		this.drawCenteredString(
				this.fontRenderer,
				StatCollector.translateToLocal("qp.list.setting")
						+ StatCollector.translateToLocal(this.targetid == 0 ? "enchantment.lootBonusDigger" : "enchantment.untouching"), this.width / 2, 8,
				0xFFFFFF);
		if ((this.targetid == 0 ? this.tile.fortuneList : this.tile.silktouchList).isEmpty()) {
			this.delete.enabled = false;
		}
		super.drawScreen(i, j, k);
	}

	@Override
	public void confirmClicked(boolean par1, int par2) {
		if (par1) {
			PacketHandler.sendPacketToServer(this.tile, (byte) par2, this.oreslot.target.get(this.oreslot.currentore));
		}
		this.mc.displayGuiScreen(this);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
			this.mc.thePlayer.closeScreen();
		}
	}
}
