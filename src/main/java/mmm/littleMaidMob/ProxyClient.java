package mmm.littleMaidMob;

import mmm.lib.ProxyCommon;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon {

	@Override
	public void init() {
		// レンダラの登録
		RenderingRegistry.registerEntityRenderingHandler(EntityLittleMaidBase.class, new RenderLittleMaid(0.5F));
	}

}
