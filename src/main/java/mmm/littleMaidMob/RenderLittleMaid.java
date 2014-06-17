package mmm.littleMaidMob;

import mmm.lib.multiModel.RenderMultiModel;
import mmm.lib.multiModel.model.mc162.ModelMultiBase;
import mmm.lib.multiModel.texture.MultiModelContainer;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderLittleMaid extends RenderMultiModel {

	public MultiModelContainer modelContainer;


	public RenderLittleMaid(float pShadowSize) {
		super(pShadowSize);
		
		modelMain.textures = new ResourceLocation[0];
//		modelMain.textures = new ResourceLocation[] { textures };
/*
		modelMain.model = new ModelLittleMaid_Aug();
		try {
			ClassLoader lcl = getClass().getClassLoader();
			Class lc = lcl.loadClass("ModelLittleMaid_Kelo");
			Constructor<ModelMultiBase> lcc = lc.getConstructor();
			modelMain.model = lcc.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
	}

	@Override
	public void doRender(EntityLiving par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9) {
		doRender((EntityLittleMaidBase)par1EntityLiving, par2, par4, par6, par8, par9);
	}

	public void doRender(EntityLittleMaidBase pEntity, double pX, double pY, double pZ, float par8, float par9) {
		modelMain.isRendering = true;
//		modelMain.model = pEntity.getModel();
//		modelContainer = MultiModelManager.instance.getMultiModel("MMM_Aug");
		modelContainer = pEntity.multiModel.model;
		modelMain.model = (ModelMultiBase)modelContainer.getModelClass()[0];
		
		super.doRender(pEntity, pX, pY, pZ, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		// TODO Auto-generated method stub
//		return ((EntityLittleMaidMob)var1).getTexture();
//		return textures;
		return modelContainer.getTexture(((EntityLittleMaidBase)var1).multiModel.getColor());
	}

}
