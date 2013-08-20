package org.yogpstop.qp.client;

import org.yogpstop.qp.CommonProxy;
import org.yogpstop.qp.EntityMechanicalArm;

import buildcraft.core.render.RenderVoid;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.Side;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	@Override
	public void registerTextures() {
		RenderingRegistry.registerEntityRenderingHandler(EntityMechanicalArm.class, new RenderVoid());
	}

}