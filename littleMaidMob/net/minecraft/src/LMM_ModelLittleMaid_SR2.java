package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * 瞬き付き基本形
 */
public class LMM_ModelLittleMaid_SR2 extends LMM_ModelLittleMaid {

	public MMM_ModelRenderer eyeR;
	public MMM_ModelRenderer eyeL;


	public LMM_ModelLittleMaid_SR2() {
		super();
	}
	public LMM_ModelLittleMaid_SR2(float psize) {
		super(psize);
	}
	public LMM_ModelLittleMaid_SR2(float psize, float pyoffset) {
		super(psize, pyoffset);
	}


	@Override
	public void initModel(float psize, float pyoffset) {
		super.initModel(psize, pyoffset);
		
		// 追加パーツ
		eyeR = new MMM_ModelRenderer(this, 32, 19);
		eyeR.addPlate(-4.0F, -5.0F, -4.001F, 4, 4, 0, psize);
		eyeR.setRotationPointLM(0.0F, 0.0F, 0.0F);
		eyeL = new MMM_ModelRenderer(this, 42, 19);
		eyeL.addPlate(0.0F, -5.0F, -4.001F, 4, 4, 0, psize);
		eyeL.setRotationPointLM(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(eyeR);
		bipedHead.addChild(eyeL);
	}

	@Override
	public void setLivingAnimations(EntityLiving entityliving, float f,
			float f1, float renderPartialTicks) {
		super.setLivingAnimations(entityliving, f, f1, renderPartialTicks);
		
		float f3 = (float)entityliving.ticksExisted + renderPartialTicks + entityIdFactor;
		// 目パチ
		if( 0 > MathHelper.sin(f3 * 0.05F) + MathHelper.sin(f3 * 0.13F) + MathHelper.sin(f3 * 0.7F) + 2.55F) { 
			eyeR.setVisible(true);
			eyeL.setVisible(true);
		} else { 
			eyeR.setVisible(false);
			eyeL.setVisible(false);
		}
	}

	@Override
	public void setRotationAngles(float f, float f1, float ticksExisted, float pheadYaw,
			float pheadPitch, float f5, Entity pEntity) {
		super.setRotationAngles(f, f1, ticksExisted, pheadYaw, pheadPitch, f5, pEntity);
		
		if (aimedBow && modelCaps != null) {
			if (modelCaps.getCapsValueInt(caps_dominantArm) == 0) {
				eyeL.setVisible(true);
			} else {
				eyeR.setVisible(true);
			}
		}
	}
	
}
