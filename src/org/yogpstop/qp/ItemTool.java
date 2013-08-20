package org.yogpstop.qp;

import java.util.List;

import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.Side;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;

public class ItemTool extends Item {

	public ItemTool(int par1) {
		super(par1);
		setMaxStackSize(1);
		setHasSubtypes(true);
		this.setMaxDamage(0);
		setCreativeTab(CreativeTabs.tabTools);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int par1) {
		return 80 + par1;
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int side, float par8, float par9, float par10) {
		boolean s = false, f = false;
		NBTTagList nbttl = is.getEnchantmentTagList();
		if (nbttl != null) for (int i = 0; i < nbttl.tagCount(); i++) {
			short id = ((NBTTagCompound) nbttl.tagAt(i)).getShort("id");
			if (id == 33) s = true;
			if (id == 35) f = true;
		}
		if (w.getBlockTileEntity(x, y, z) instanceof TileBasic && s != f) {
			if (w.isRemote) return true;
			ep.openGui(QuarryPlus.instance, f ? QuarryPlus.guiIdFortuneList : QuarryPlus.guiIdSilktouchList, w, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public String getItemNameIS(ItemStack is) {
		switch (is.getItemDamage()) {
		case 1:
			return "item.listEditor";
		case 2:
			return "item.liquidSelector";
		}
		return "item.statusChecker";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
	}

	@Override
	public String getTextureFile() {
		return "/mods/yogpstop_qp/textures/textures.png";
	}
}
