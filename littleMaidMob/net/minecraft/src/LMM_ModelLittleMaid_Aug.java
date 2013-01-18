package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class LMM_ModelLittleMaid_Aug extends LMM_ModelLittleMaid_SR2 {
	
	public MMM_ModelRenderer shaggyB;
	public MMM_ModelRenderer shaggyR;
	public MMM_ModelRenderer shaggyL;
	
	public MMM_ModelRenderer SideTailR;
	public MMM_ModelRenderer SideTailL;

	public MMM_ModelRenderer sensor1;
	public MMM_ModelRenderer sensor2;
	public MMM_ModelRenderer sensor3;
	public MMM_ModelRenderer sensor4;

	
	public LMM_ModelLittleMaid_Aug() {
		super();
	}
	public LMM_ModelLittleMaid_Aug(float psize) {
		super(psize);
	}
	public LMM_ModelLittleMaid_Aug(float psize, float pyoffset) {
		super(psize, pyoffset);
	}


	@Override
	public void initModel(float psize, float pyoffset) {
		super.initModel(psize, pyoffset);
		
		// 再構成パーツ
		SideTailR = new MMM_ModelRenderer(this);
		SideTailR.setTextureOffset(46, 20).addBox(-1.5F, -0.5F, -1.0F, 2, 10, 2, psize);
		SideTailR.setRotationPointLM(-5F, -7.8F, 1.9F);
		SideTailL = new MMM_ModelRenderer(this);
		SideTailL.setTextureOffset(54, 20).addBox(0.5F, -0.5F, -1.0F, 2, 10, 2, psize);
		SideTailL.setRotationPointLM(4F, -7.8F, 1.9F);

		
		// 増加パーツ
		shaggyB = new MMM_ModelRenderer(this, 24, 0);
		shaggyB.addPlate(-5.0F, 0.0F, 0.0F, 10, 4, 4, psize);
		shaggyB.setRotationPointLM(0.0F, -1.0F, 4.0F);
		shaggyB.setRotateAngleX(0.4F);
		shaggyR = new MMM_ModelRenderer(this, 34, 4);
		shaggyR.addPlate(0.0F, 0.0F, -5.0F, 10, 4, 1, psize);
		shaggyR.setRotationPointLM(4.0F, -1.0F, 0.0F);
		shaggyR.setRotateAngleZ(-0.4F);
		shaggyL = new MMM_ModelRenderer(this, 24, 4);
		shaggyL.addPlate(0.0F, 0.0F, -5.0F, 10, 4, 5, psize);
		shaggyL.setRotationPointLM(-4.0F, -1.0F, 0.0F);
		shaggyL.setRotateAngleZ(0.4F);

		sensor1 = new MMM_ModelRenderer(this, 0, 0);
		sensor1.addPlate(-8.0F, -4.0F, 0.0F, 8, 4, 0);
		sensor1.setRotationPointLM(0.0F, -8.0F + pyoffset, 0.0F);
		sensor2 = new MMM_ModelRenderer(this, 0, 4);
		sensor2.addPlate(0.0F, -4.0F, 0.0F, 8, 4, 0);
		sensor2.setRotationPointLM(0.0F, -8.0F + pyoffset, 0.0F);
		sensor3 = new MMM_ModelRenderer(this, 44, 0);
		sensor3.addPlate(0.0F, -7.0F, -4.0F, 4, 8, 1);
		sensor3.setRotationPointLM(0.0F, -8.0F + pyoffset, 0.0F);
		sensor4 = new MMM_ModelRenderer(this, 34, 0);
		sensor4.addPlate(0.0F, -4.0F, -10.0F, 10, 4, 1);
		sensor4.setRotationPointLM(0.0F, -8.0F + pyoffset, 0.0F);

		
		// 変更パーツ
		bipedHead.clearCubeList();
		bipedHead.mirror = false;
		bipedHead.setTextureOffset(0, 0).addBox(-4F, -8F, -4F, 8, 8, 8, psize);			// Head
		bipedHead.setTextureOffset(0, 18).addBox(-5F, -8.5F, 0.2F, 1, 3, 3, psize);		// ChignonR
		bipedHead.setTextureOffset(24, 18).addBox(4F, -8.5F, 0.2F, 1, 3, 3, psize);		// ChignonL
		bipedHead.setTextureOffset(52, 10).addBox(-7.5F, -9.5F, 0.9F, 4, 3, 2, psize);	// sidetailUpperR
		bipedHead.setTextureOffset(52, 15).addBox(3.5F, -9.5F, 0.9F, 4, 3, 2, psize);	// sidetailUpperL
		bipedHead.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(HeadMount);
		bipedHead.addChild(SideTailR);
		bipedHead.addChild(SideTailL);
		bipedHead.addChild(shaggyB);
		bipedHead.addChild(shaggyR);
		bipedHead.addChild(shaggyL);
		bipedHead.addChild(sensor1);
		bipedHead.addChild(sensor2);
		bipedHead.addChild(sensor3);
		bipedHead.addChild(sensor4);
		bipedHead.addChild(eyeR);
		bipedHead.addChild(eyeL);
		
	}
	
	@Override
	public void setLivingAnimations(EntityLiving entityliving, float f,
			float f1, float renderPartialTicks) {
		super.setLivingAnimations(entityliving, f, f1, renderPartialTicks);

		float f3 = (float)entityliving.ticksExisted + renderPartialTicks + entityIdFactor;
		float f4;
		if (modelCaps != null && modelCaps.getCapsValueBoolean(caps_isLookSuger)) {
			f3 *= 8.0F;
			f4 = -0.2F;
		} else {
			f4 = (1F - (float)entityliving.health / 20F) * 0.5F;
		}
		float f5 = MathHelper.sin(f3 * 0.067F) * 0.05F - f4;
		float f6 = 40.0F / 57.29578F;
		sensor1.setRotateAngle(f5, -f6, f5);
		sensor2.setRotateAngle(-f5, f6, -f5);
		sensor3.setRotateAngle(MathHelper.sin(f3 * 0.067F) * 0.05F - 1.2F - f4, MathHelper.sin(f3 * 0.09F) * 0.4F, MathHelper.cos(f3 * 0.09F) * 0.2F);
		sensor4.setRotateAngle(MathHelper.sin(f3 * 0.067F) * 0.05F + f4, MathHelper.cos(f3 * 0.09F) * 0.5F, MathHelper.sin(f3 * 0.09F) * 0.2F);
	}

	@Override
	public void setRotationAngles(float f, float f1, float ticksExisted,
			float pheadYaw, float pheadPitch, float f5, Entity pEntity) {
		super.setRotationAngles(f, f1, ticksExisted, pheadYaw, pheadPitch, f5, pEntity);
		
		SideTailR.rotateAngleX =  SideTailL.rotateAngleX = -bipedHead.rotateAngleX / 1.5F;
	}
	
}
