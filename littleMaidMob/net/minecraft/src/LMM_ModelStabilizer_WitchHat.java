package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class LMM_ModelStabilizer_WitchHat extends MMM_ModelStabilizerBase {

	public MMM_ModelRenderer WitchHat;
	public MMM_ModelRenderer WitchHat1;
	public MMM_ModelRenderer WitchHat2;
	public MMM_ModelRenderer WitchHat3;
	
	public LMM_ModelStabilizer_WitchHat() {
		// ‚Ü‚¶‚å‚±‚Ú‚¤
		textureWidth = 64;
		textureHeight = 32;
		
		WitchHat = new MMM_ModelRenderer(this, 0, 0);
		WitchHat1 = new MMM_ModelRenderer(this, 0, 0);
		WitchHat2 = new MMM_ModelRenderer(this, 0, 0);
		WitchHat3 = new MMM_ModelRenderer(this, 0, 0);
		WitchHat.setTextureOffset(0, 15).addBox(-8F, 0F, -8F, 16, 1, 16, 0.0F);
		WitchHat.setTextureOffset(0, 0).addBox(-4.5F, -4F, -4.5F, 9, 4, 9);
		WitchHat1.setTextureOffset(40, 4).addBox(-3F, -3F, -3F, 6, 3, 6).setRotationPointLM(0F, -4F, 0F);
		WitchHat2.setTextureOffset(28, 0).addBox(-2F, -2F, -2F, 4, 2, 4).setRotationPointLM(0F, -3F, 0F);
		WitchHat3.setTextureOffset(0, 0).addBox(-1F, -2F, -1F, 2, 2, 2).setRotationPointLM(0F, -2F, 0F);
		
		WitchHat.addChild(WitchHat1);
		WitchHat1.addChild(WitchHat2);
		WitchHat2.addChild(WitchHat3);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		GL11.glTranslatef(0F, -0.1F, 0F);
		WitchHat.render(f5);
	}
	
	@Override
	public String getTexture() {
		return "/mob/littleMaid/ALTERNATIVE/Stabilizer_MagicHat.png";
	}
	
	@Override
	public String getName() {
		return "WitchHat";
	}
	
	@Override
	public boolean isLoadAnotherTexture() {
		return true;
	}
	
}
