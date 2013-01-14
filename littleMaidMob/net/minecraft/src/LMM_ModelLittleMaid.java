package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * LMM用に最適化
 */
public class LMM_ModelLittleMaid extends MMM_ModelBiped {

	//fields
	public MMM_ModelRenderer mainFrame;
	public MMM_ModelRenderer bipedHead;
	public MMM_ModelRenderer bipedBody;
	public MMM_ModelRenderer bipedRightArm;
	public MMM_ModelRenderer bipedLeftArm;
	public MMM_ModelRenderer bipedRightLeg;
	public MMM_ModelRenderer bipedLeftLeg;
	public MMM_ModelRenderer Skirt;

	
	/**
	 * コンストラクタは全て継承させること
	 */
	public LMM_ModelLittleMaid() {
		super();
	}
	/**
	 * コンストラクタは全て継承させること
	 */
	public LMM_ModelLittleMaid(float psize) {
		super(psize);
	}
	/**
	 * コンストラクタは全て継承させること
	 */
	public LMM_ModelLittleMaid(float psize, float pyoffset) {
		super(psize, pyoffset);
	}


	@Override
	public void initModel(float psize, float pyoffset) {
		// 標準型
		textureHeight = 32;
		textureWidth = 64;
		
		
		Arms = new MMM_ModelRenderer[18];
		// 手持ち
		Arms[0] = new MMM_ModelRenderer(this, 0, 0);
		Arms[0].setRotationPointLM(-1F, 5F, -1F);
		Arms[1] = new MMM_ModelRenderer(this, 0, 0);
		Arms[1].setRotationPoint(1F, 5F, -1F);
		Arms[1].isInvertX = true;
		// バイプロダクトエフェクター
		Arms[2] = new MMM_ModelRenderer(this, 0, 0);
		Arms[2].setRotationPoint(-3F, 9F, 6F);
		Arms[2].setRotateAngleDeg(45F, 0F, 0F);
		Arms[3] = new MMM_ModelRenderer(this, 0, 0);
		Arms[3].setRotationPoint(3F, 9F, 6F);
		Arms[3].setRotateAngleDeg(45F, 0F, 0F);
		Arms[3].isInvertX = true;
		// テールソード
		Arms[4] = new MMM_ModelRenderer(this, 0, 0);
		Arms[4].setRotationPoint(-2F, 0F, 0F);
		Arms[4].setRotateAngleDeg(180F, 0F, 0F);
		Arms[5] = new MMM_ModelRenderer(this, 0, 0);
		Arms[5].setRotationPoint(2F, 0F, 0F);
		Arms[5].setRotateAngleDeg(180F, 0F, 0F);
		
		
//		Arms[8] = new MMM_ModelRenderer(this, "HeadTop");
//		Arms[8].setRotationPoint(0F, -3F, 1F);
		HeadMount.setRotationPoint(0F, -4F, 0F);
		
		
		bipedHead = new MMM_ModelRenderer(this, 0, 0);
		bipedHead.setTextureOffset(0, 0).addBox(-4F, -8F, -4F, 8, 8, 8, psize);			// Head
		bipedHead.setTextureOffset(24, 0).addBox(-4F, 0F, 1F, 8, 4, 3, psize);			// Hire
		bipedHead.setTextureOffset(24, 18).addBox(-5F, -7F, 0.2F, 1, 3, 3, psize);		// ChignonR
		bipedHead.setTextureOffset(24, 18).addBox(4F, -7F, 0.2F, 1, 3, 3, psize);		// ChignonL
		bipedHead.setTextureOffset(52, 10).addBox(-2F, -7.2F, 4F, 4, 4, 2, psize);		// ChignonB
		bipedHead.setTextureOffset(46, 20).addBox(-1.5F, -6.8F, 4F, 3, 9, 3, psize);	// Tail
		bipedHead.setTextureOffset(58, 21).addBox(-5.5F, -6.8F, 0.9F, 1, 8, 2, psize);	// SideTailR
		bipedHead.setMirror(true);
		bipedHead.setTextureOffset(58, 21).addBox(4.5F, -6.8F, 0.9F, 1, 8, 2, psize);	// SideTailL
		bipedHead.setRotationPoint(0F, 0F, 0F);
//		bipedHead.addChild(Arms[8]);
		bipedHead.addChild(HeadMount);
		
		bipedRightArm = new MMM_ModelRenderer(this, 48, 0);
		bipedRightArm.addBox(-2.0F, -1F, -1F, 2, 8, 2, psize);
		bipedRightArm.setRotationPoint(-3.0F, 1.5F, 0F);
		bipedRightArm.addChild(Arms[0]);
		bipedRightArm.addChild(Arms[2]);
		
		bipedLeftArm = new MMM_ModelRenderer(this, 56, 0);
		bipedLeftArm.addBox(0.0F, -1F, -1F, 2, 8, 2, psize);
		bipedLeftArm.setRotationPoint(3.0F, 1.5F, 0F);
		bipedLeftArm.addChild(Arms[1]);
		bipedLeftArm.addChild(Arms[3]);
		
		bipedRightLeg = new MMM_ModelRenderer(this, 32, 19);
		bipedRightLeg.addBox(-2F, 0F, -2F, 3, 9, 4, psize);
		bipedRightLeg.setRotationPoint(-1F, 7F, 0F);
		
		bipedLeftLeg = new MMM_ModelRenderer(this, 32, 19);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-1F, 0F, -2F, 3, 9, 4, psize);
		bipedLeftLeg.setRotationPoint(1F, 7F, 0F);
		
		Skirt = new MMM_ModelRenderer(this, 0, 16);
		Skirt.addBox(-4F, -2F, -4F, 8, 8, 8, psize);
		Skirt.setRotationPoint(0F, 7F, 0F);
		
		bipedBody = new MMM_ModelRenderer(this, 32, 8);
		bipedBody.addBox(-3F, 0F, -2F, 6, 7, 4, psize);
		bipedBody.setRotationPoint(0F, 0F, 0F);
		bipedBody.addChild(bipedRightArm);
		bipedBody.addChild(bipedLeftArm);
		bipedBody.addChild(Arms[4]);
		bipedBody.addChild(Arms[5]);
		
		mainFrame = new MMM_ModelRenderer(this, 0, 0);
		mainFrame.setRotationPoint(0F, 0F + pyoffset + 8F, 0F);
		mainFrame.addChild(bipedHead);
		mainFrame.addChild(bipedBody);
		mainFrame.addChild(bipedRightLeg);
		mainFrame.addChild(bipedLeftLeg);
		mainFrame.addChild(Skirt);
		
		
	}

	@Override
	public float[] getArmorModelsSize() {
		return new float[] {0.1F, 0.5F};
	}

	@Override
	public float getHeight() {
		return 1.35F;
	}

	@Override
	public float getWidth() {
		return 0.5F;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		// TODO:このへん改修してLMM以外から呼ばれた時に描画されるようにする
		if (entity instanceof LMM_EntityLittleMaid) {
			renderLM((LMM_EntityLittleMaid)entity, f, f1, f2, f3, f4, f5);
		}
	}

	public void renderLM(LMM_EntityLittleMaid pentitylittlemaid, float f,
			float f1, float ticksExisted, float pheadYaw, float pheadPitch, float f5) {
		setRotationAnglesLM(f, f1, ticksExisted, pheadYaw, pheadPitch, f5, pentitylittlemaid);
		mainFrame.render(f5, pentitylittlemaid);

		renderStabilizer(pentitylittlemaid, pentitylittlemaid.maidStabilizer, f, f1, ticksExisted, pheadYaw, pheadPitch, f5);
	}
	
	@Override
	public void setLivingAnimations(EntityLiving entityliving, float f, float f1, float f2) {
		if (entityliving instanceof LMM_EntityLittleMaid) {
			setLivingAnimationsLM((LMM_EntityLittleMaid)entityliving, f, f1, f2);
		}
	}
	
	public void setLivingAnimationsLM(LMM_EntityLittleMaid pentitylittlemaid, float f, float f1, float renderPartialTicks) {
		float angle = pentitylittlemaid.getInterestedAngle(renderPartialTicks);
		bipedHead.rotateAngleZ = angle;
	}

	/**
	 * 姿勢制御用
	 * 独自追加分
	 * setRotationAnglesの代わりにこちらを使う。
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float par3,
			float par4, float par5, float par6, Entity par7Entity) {
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
		setRotationAnglesLM(par1, par2, par3, par4, par5, par6, (LMM_EntityLittleMaid)par7Entity);
	}

	public void setRotationAnglesLM(float f, float f1, float ticksExisted, float pheadYaw, float pheadPitch, float f5, LMM_EntityLittleMaid pentitylittlemaid) {
		bipedHead.rotateAngleY = pheadYaw / 57.29578F;
		bipedHead.rotateAngleX = pheadPitch / 57.29578F;
		bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F;
		bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		bipedRightLeg.rotateAngleY = 0.0F;
		bipedLeftLeg.rotateAngleY = 0.0F;
		
		if (isRiding) {
			// 乗り物に乗っている
			bipedRightArm.rotateAngleX += -0.6283185F;
			bipedLeftArm.rotateAngleX += -0.6283185F;
			bipedRightLeg.rotateAngleX = -1.256637F;
			bipedLeftLeg.rotateAngleX = -1.256637F;
			bipedRightLeg.rotateAngleY = 0.3141593F;
			bipedLeftLeg.rotateAngleY = -0.3141593F;
		}
		
		
		// アイテム持ってるときの腕振りを抑える+表示角オフセット
		if (heldItemLeft != 0) {
			bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)heldItemLeft;
		}
		if (heldItemRight != 0) {
			bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)heldItemRight;
		}
		
		bipedRightArm.rotateAngleY = 0.0F;
		bipedLeftArm.rotateAngleY = 0.0F;
		float onGroundR = pentitylittlemaid.getSwingStatus(0).onGround;
		float onGroundL = pentitylittlemaid.getSwingStatus(1).onGround;
		
		if ((onGroundR > -9990F || onGroundL > -9990F) && !aimedBow) {
			// 腕振り
//            float f6 = 1.0F + onGroundR - onGroundL;
			float f6, f7, f8;
			f6 = MathHelper.sin(MathHelper.sqrt_float(onGroundR) * (float)Math.PI * 2.0F);
			f7 = MathHelper.sin(MathHelper.sqrt_float(onGroundL) * (float)Math.PI * 2.0F);
			bipedBody.rotateAngleY = (f6 - f7) * 0.2F;
//            bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * (float)Math.PI * 2.0F) * 0.2F;
			Skirt.rotateAngleY = bipedBody.rotateAngleY;
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
			// R
			if (onGroundR > 0F) {
				f6 = 1.0F - onGroundR;
				f6 *= f6;
				f6 *= f6;
				f6 = 1.0F - f6;
				f7 = MathHelper.sin(f6 * (float)Math.PI);
				f8 = MathHelper.sin(onGroundR * (float)Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
				bipedRightArm.rotateAngleX -= (double)f7 * 1.2D + (double)f8;
				bipedRightArm.rotateAngleY += bipedBody.rotateAngleY * 2.0F;
				bipedRightArm.rotateAngleZ = MathHelper.sin(onGroundR * 3.141593F) * -0.4F;
			} else {
				bipedRightArm.rotateAngleX += bipedBody.rotateAngleY;
			}
			// L
			if (onGroundL > 0F) {
				f6 = 1.0F - onGroundL;
				f6 *= f6;
				f6 *= f6;
				f6 = 1.0F - f6;
				f7 = MathHelper.sin(f6 * (float)Math.PI);
				f8 = MathHelper.sin(onGroundL * (float)Math.PI) * -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
				bipedLeftArm.rotateAngleX -= (double)f7 * 1.2D + (double)f8;
				bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY * 2.0F;
				bipedLeftArm.rotateAngleZ = MathHelper.sin(onGroundL * 3.141593F) * 0.4F;
			} else {
				bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;
			}
		}
		if(isSneak) {
			// しゃがみ
			bipedBody.rotateAngleX = 0.5F;
			bipedRightLeg.rotateAngleX -= 0.0F;
			bipedLeftLeg.rotateAngleX -= 0.0F;
			bipedRightArm.rotateAngleX += 0.2F;
			bipedLeftArm.rotateAngleX += 0.2F;
			bipedRightLeg.rotationPointZ = 3F;
			bipedLeftLeg.rotationPointZ = 3F;
			bipedRightLeg.rotationPointY = 6F;
			bipedLeftLeg.rotationPointY = 6F;
			bipedHead.rotationPointY = 1.0F;
			Skirt.rotationPointY = 5.8F;
			Skirt.rotationPointZ = 2.7F;
			Skirt.rotateAngleX = 0.2F;
		} else {
			// 通常立ち
			bipedBody.rotateAngleX = 0.0F;
			bipedRightLeg.rotationPointZ = 0.0F;
			bipedLeftLeg.rotationPointZ = 0.0F;
			bipedRightLeg.rotationPointY = 7F;
			bipedLeftLeg.rotationPointY = 7F;
			bipedHead.rotationPointY = 0.0F;
			Skirt.rotationPointY = 7.0F;
			Skirt.rotationPointZ = 0.0F;
			Skirt.rotateAngleX = 0.0F;
		}
		if (isWait) {
			//待機状態の特別表示
			bipedRightArm.rotateAngleX = MathHelper.sin(ticksExisted * 0.067F) * 0.05F -0.7F;
			bipedRightArm.rotateAngleY = 0.0F;
			bipedRightArm.rotateAngleZ = -0.4F;
			bipedLeftArm.rotateAngleX = MathHelper.sin(ticksExisted * 0.067F) * 0.05F -0.7F;
			bipedLeftArm.rotateAngleY = 0.0F;
			bipedLeftArm.rotateAngleZ = 0.4F;
		} else {
			if (aimedBow) {
				// 弓構え
				float f6 = MathHelper.sin(onGround * 3.141593F);
				float f7 = MathHelper.sin((1.0F - (1.0F - onGround) * (1.0F - onGround)) * 3.141593F);
				bipedRightArm.rotateAngleZ = 0.0F;
				bipedLeftArm.rotateAngleZ = 0.0F;
				bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
				bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
				bipedRightArm.rotateAngleX = -1.470796F;
				bipedLeftArm.rotateAngleX = -1.470796F;
				bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
				bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
				bipedRightArm.rotateAngleZ += MathHelper.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
				bipedLeftArm.rotateAngleZ -= MathHelper.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
				bipedRightArm.rotateAngleX += MathHelper.sin(ticksExisted * 0.067F) * 0.05F;
				bipedLeftArm.rotateAngleX -= MathHelper.sin(ticksExisted * 0.067F) * 0.05F;
				bipedRightArm.rotateAngleX += bipedHead.rotateAngleX;
				bipedLeftArm.rotateAngleX += bipedHead.rotateAngleX;
				bipedRightArm.rotateAngleY += bipedHead.rotateAngleY;
				bipedLeftArm.rotateAngleY += bipedHead.rotateAngleY;
			} else {
				// 通常
				bipedRightArm.rotateAngleZ += 0.5F;
				bipedLeftArm.rotateAngleZ -= 0.5F;
				bipedRightArm.rotateAngleZ += MathHelper.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
				bipedLeftArm.rotateAngleZ -= MathHelper.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
				bipedRightArm.rotateAngleX += MathHelper.sin(ticksExisted * 0.067F) * 0.05F;
				bipedLeftArm.rotateAngleX -= MathHelper.sin(ticksExisted * 0.067F) * 0.05F;
			}
		}
		
		
		//
		Arms[2].setRotateAngle(-0.78539816339744830961566084581988F - bipedRightArm.getRotateAngleX(), 0F, 0F);
		Arms[3].setRotateAngle(-0.78539816339744830961566084581988F - bipedLeftArm.getRotateAngleX(), 0F, 0F);
		
		
	}

	@Override
	public void showAllParts() {
		// 表示制限を解除してすべての部品を表示
		bipedHead.setVisible(true);
		bipedBody.setVisible(true);
		bipedRightArm.setVisible(true);
		bipedLeftArm.setVisible(true);
		Skirt.setVisible(true);
		bipedRightLeg.setVisible(true);
		bipedLeftLeg.setVisible(true);
	}

	@Override
	public int showArmorParts(int parts) {
		// 鎧の表示用
		boolean f;
		// 兜
		f = parts == 3 ? true : false;
		bipedHead.setVisible(f);
		// 鎧
		f = parts == 2 ? true : false;
		bipedBody.setVisible(f);
		bipedRightArm.setVisible(f);
		bipedLeftArm.setVisible(f);
		// 脚甲
		f = parts == 1 ? true : false;
		Skirt.setVisible(f);
		// 臑当
		f = parts == 0 ? true : false;
		bipedRightLeg.setVisible(f);
		bipedLeftLeg.setVisible(f);
		
		return -1;
	}	

	@Override
	public void renderItems(EntityLiving pEntity, Render pRender) {
		super.renderItems(pEntity, pRender);
		if (pEntity instanceof LMM_EntityLittleMaid) {
			renderItemsLM((LMM_EntityLittleMaid)pEntity, pRender);
		}
	}
	
	public void renderItemsLM(LMM_EntityLittleMaid pEntity, Render pRender) {
		// 手持ちの表示
		GL11.glPushMatrix();

		ItemStack litemstack;
		EnumAction laction;
//		Arms[0].setRotationPointLM(-1F, 5F, -1F);
//		Arms[1].setRotationPointLM(1F, 5F, -1F);
		// R
		litemstack = pEntity.mstatSwingStatus[0].getItemStack(pEntity);
		laction = (pEntity.maidDominantArm == 0 && pEntity.maidAvatar.getItemInUseCount() > 0) ? litemstack.getItemUseAction() : null;
		Arms[0].loadMatrix().renderItems(pEntity, pRender, false, laction ,litemstack);
		// L
		litemstack = pEntity.mstatSwingStatus[1].getItemStack(pEntity);
		laction = (pEntity.maidDominantArm == 1 && pEntity.maidAvatar.getItemInUseCount() > 0) ? litemstack.getItemUseAction() : null;
		Arms[1].loadMatrix().renderItems(pEntity, pRender, false, laction, litemstack);
		// 頭部装飾品
		if (pEntity.isCamouflage() || pEntity.isPlanter()) {
			HeadMount.loadMatrix();
			if (pEntity.isPlanter()) {
				GL11.glTranslatef(0F, -0.56F, 0F);
			}
			HeadMount.renderItems(pEntity, pRender, true, null, pEntity.maidInventory.getHeadMount());
		}
		
		GL11.glPopMatrix();
	}


	// 不要部品
	@Override
	public void renderEars(float par1) {
	}
	@Override
	public void renderCloak(float par1) {
	}


}
