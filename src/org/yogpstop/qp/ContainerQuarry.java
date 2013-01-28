package org.yogpstop.qp;

import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;

public class ContainerQuarry extends Container {
	private IInventory playerInventory;
	private TileQuarry tileQuarry;
	private World world;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int buttonId = -1;

	public ContainerQuarry(EntityPlayer player, World world, int x, int y, int z) {
		this.tileQuarry = (TileQuarry) world.getBlockTileEntity(x, y, z);
		this.playerInventory = player.inventory;
		this.world = world;
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;

		for (int rows = 0; rows < 2; ++rows) {
			for (int columns = 0; columns < 4; ++columns) {

				addSlotToContainer(new SlotQuarry(tileQuarry, columns + rows
						* 4, 8 + rows * 144, 8 + columns * 18));
			}
		}

		for (int rows = 0; rows < 3; ++rows) {
			for (int slotIndex = 0; slotIndex < 9; ++slotIndex) {
				addSlotToContainer(new Slot(playerInventory, slotIndex + rows
						* 9 + 9, 8 + slotIndex * 18, 84 + rows * 18));
			}
		}

		for (int slotIndex = 0; slotIndex < 9; ++slotIndex) {
			addSlotToContainer(new Slot(playerInventory, slotIndex,
					8 + slotIndex * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer) {
		return this.world.getBlockId(this.xCoord, this.yCoord, this.zCoord) != QuarryPlus.blockQuarry.blockID ? false
				: entityPlayer.getDistanceSq((double) this.xCoord + 0.5D,
						(double) this.yCoord + 0.5D,
						(double) this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if (buttonId > -1) {
			if (buttonId == 0) {
				this.buttonId = -1;
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		return null;
	}

	public void onButtonPushed(int buttonId) {
		this.buttonId = (byte) buttonId;
		PacketDispatcher.sendPacketToServer(PacketHandler.getPacket(this));
	}

	public void readPacketData(ByteArrayDataInput data) {
		try {
			this.buttonId = data.readByte();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writePacketData(DataOutputStream dos) {
		try {
			dos.writeByte(this.buttonId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}