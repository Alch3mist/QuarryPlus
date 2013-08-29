package org.yogpstop.qp;

import org.yogpstop.qp.client.GuiInfMJSrc;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileInfMJSrc extends APacketTile {
	public float power = 10;
	public int interval = 1;
	private int cInterval = 1;
	public boolean active = true;

	@Override
	public void updateEntity() {
		if (!this.active) return;
		if (--this.cInterval > 0) return;
		TileEntity te;
		for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
			te = this.worldObj.getBlockTileEntity(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
			if (te instanceof IPowerReceptor) {
				IPowerProvider pr = ((IPowerReceptor) te).getPowerProvider();
				if (pr != null) pr.receiveEnergy(this.power, d.getOpposite());
			}
		}
		this.cInterval = this.interval;
	}

	@Override
	void S_recievePacket(byte pattern, ByteArrayDataInput data, EntityPlayer ep) {
		switch (pattern) {
		case PacketHandler.infmjsrc:
			this.power = data.readFloat();
			this.interval = data.readInt();
			PacketDispatcher.sendPacketToAllPlayers(PacketHandler.makeInfMJSrcPacket(this.xCoord, this.yCoord, this.zCoord, this.power, this.interval));
			break;
		case PacketHandler.infmjsrca:
			this.active = data.readBoolean();
			PacketDispatcher.sendPacketToAllPlayers(PacketHandler.makeInfMJSrcAPacket(this.xCoord, this.yCoord, this.zCoord, this.active));
		}
	}

	@Override
	void C_recievePacket(byte pattern, ByteArrayDataInput data) {
		switch (pattern) {
		case PacketHandler.infmjsrc:
			this.power = data.readFloat();
			this.interval = data.readInt();
			if (Minecraft.getMinecraft().currentScreen instanceof GuiInfMJSrc) {
				GuiInfMJSrc gims = (GuiInfMJSrc) Minecraft.getMinecraft().currentScreen;
				if (gims.x == this.xCoord && gims.y == this.yCoord && gims.z == this.zCoord) Minecraft.getMinecraft().thePlayer.openGui(QuarryPlus.instance,
						QuarryPlus.guiIdInfMJSrc, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
			break;
		case PacketHandler.infmjsrca:
			this.active = data.readBoolean();
			if (Minecraft.getMinecraft().currentScreen instanceof GuiInfMJSrc) {
				GuiInfMJSrc gims = (GuiInfMJSrc) Minecraft.getMinecraft().currentScreen;
				if (gims.x == this.xCoord && gims.y == this.yCoord && gims.z == this.zCoord) Minecraft.getMinecraft().thePlayer.openGui(QuarryPlus.instance,
						QuarryPlus.guiIdInfMJSrc, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttc) {
		super.readFromNBT(nbttc);
		this.power = nbttc.getFloat("power");
		this.interval = nbttc.getInteger("interval");
		this.active = nbttc.getBoolean("active");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttc) {
		super.writeToNBT(nbttc);
		nbttc.setFloat("power", this.power);
		nbttc.setInteger("interval", this.interval);
		nbttc.setBoolean("active", this.active);
	}

}
