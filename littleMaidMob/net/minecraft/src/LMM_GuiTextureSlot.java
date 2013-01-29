package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

public class LMM_GuiTextureSlot extends GuiSlot {

	public LMM_GuiTextureSelect owner;
	protected int selected;
	protected LMM_EntityLittleMaid maid;
	protected List<Integer> indexTexture;
	protected List<Integer> indexArmor;
	public boolean mode;
	public int texsel[] = new int[2];
	public int color;
	private ItemStack armors[] = new ItemStack[] {
			new ItemStack(Item.bootsLeather),
			new ItemStack(Item.legsLeather),
			new ItemStack(Item.plateLeather),
			new ItemStack(Item.helmetLeather)
	};


	public LMM_GuiTextureSlot(LMM_GuiTextureSelect pOwner) {
		super(pOwner.mc, pOwner.width, pOwner.height, 16, pOwner.height - 64, 36);
		owner = pOwner;
		maid = new LMM_EntityLittleMaid(pOwner.mc.theWorld);
		color = owner.owner.entitylittlemaid.maidColor;
		
		texsel[0] = -1;
		texsel[1] = -1;
		indexTexture = new ArrayList<Integer>();
		indexArmor = new ArrayList<Integer>();
		for (int li = 0; li < MMM_TextureManager.textures.size(); li++) {
			MMM_TextureBox lbox = MMM_TextureManager.textures.get(li);
			if (lbox.getContractColorBits() > 0) {
				indexTexture.add(li);
			}
			if (lbox.hasArmor()) {
				indexArmor.add(li);
			}
			if (lbox.packegeName.equals(owner.owner.entitylittlemaid.textureName)) {
				texsel[0] = indexTexture.size() - 1;
			}
			if (lbox.packegeName.equals(owner.owner.entitylittlemaid.textureArmorName)) {
				texsel[1] = indexArmor.size() - 1;
			}
		}
		setMode(false);
	}

	@Override
	protected int getSize() {
		return mode ? indexArmor.size() : indexTexture.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2) {
		if (mode || getSelectedBox(var1).hasColor(color)) {
			selected = var1;
			if (mode) {
				texsel[1] = var1;
			} else {
				texsel[0] = var1;
			}
		}
	}

	@Override
	protected boolean isSelected(int var1) {
		return selected == var1;
	}

	@Override
	protected void drawBackground() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
		GL11.glPushMatrix();
		
		if (!mode) {
			int lx = var2 + 15 + 12 * color;
			owner.drawRect(lx, var3, lx + 11, var3 + 36, 0x88882222);
		}
		
		MMM_TextureBox lbox;
		if (mode) {
			lbox = MMM_TextureManager.textures.get(indexArmor.get(var1));
		} else {
			lbox = MMM_TextureManager.textures.get(indexTexture.get(var1));
		}
		GL11.glDisable(GL11.GL_BLEND);
		owner.fontRenderer.drawStringWithShadow(lbox.packegeName, var2 + 16, var3 + 25, -1);
		GL11.glTranslatef(var2 + 8F, var3 + 25F, 50F);
		GL11.glScalef(12F, -12F, 12F);
		maid.textureName = lbox.packegeName;
		maid.maidContract = true;
		maid.renderYawOffset = 30F;
		maid.rotationYawHead = 15F;
//		RenderHelper.enableStandardItemLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		if (mode) {
			// アーマー
			GL11.glTranslatef(1F, 0, 0);
			maid.textureModel0 = null;
			Map<Integer, String> lmap = lbox.armors.get("default");
			if (lmap != null) {
				maid.textureArmor1[0] = maid.textureArmor1[1] = 
						maid.textureArmor1[2] = maid.textureArmor1[3] =
						(new StringBuilder()).append(lbox.textureDir[1]).append(lbox.packegeName.replace('.', '/')).append(lmap.get(0x0040)).toString();
				maid.textureArmor2[0] = maid.textureArmor2[1] = 
						maid.textureArmor2[2] = maid.textureArmor2[3] =
						(new StringBuilder()).append(lbox.textureDir[1]).append(lbox.packegeName.replace('.', '/')).append(lmap.get(0x0050)).toString();
				maid.textureModel1 = lbox.models[1];
				maid.textureModel2 = lbox.models[2];
				RenderManager.instance.renderEntityWithPosYaw(maid, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
			}
			for (String ls : MMM_TextureManager.armorFilenamePrefix) {
				GL11.glTranslatef(1F, 0, 0);
				lmap = lbox.armors.get(ls);
				if (lmap != null) {
					maid.textureArmor1[0] = maid.textureArmor1[1] = 
							maid.textureArmor1[2] = maid.textureArmor1[3] = lmap.get(0x0040);
					maid.textureArmor2[0] = maid.textureArmor2[1] = 
							maid.textureArmor2[2] = maid.textureArmor2[3] = lmap.get(0x0050);
					maid.textureModel1 = lbox.models[1];
					maid.textureModel2 = lbox.models[2];
//					LMM_Client.setArmorTextureValue(maid);
					RenderManager.instance.renderEntityWithPosYaw(maid, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
				}
			}
			
		} else {
			// テクスチャ表示
			for (int li = 0; li < 16; li++) {
				GL11.glTranslatef(1F, 0, 0);
				if (lbox.hasColor(li)) {
					maid.maidColor = li;
					LMM_Client.setTextureValue(maid);
					RenderManager.instance.renderEntityWithPosYaw(maid, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
				}
			}
		}
		
		GL11.glPopMatrix();
	}

	public MMM_TextureBox getSelectedBox() {
		return getSelectedBox(selected);
	}

	public MMM_TextureBox getSelectedBox(int pIndex) {
		return MMM_TextureManager.textures.get(mode ? indexArmor.get(pIndex) : indexTexture.get(pIndex));
	}

	public MMM_TextureBox getSelectedBox(boolean pMode) {
		return MMM_TextureManager.textures.get(pMode ? indexArmor.get(texsel[1]) : indexTexture.get(texsel[0]));
	}

	public void setMode(boolean pFlag) {
		if (pFlag) {
			selected = texsel[1];
			mode = true;
			maid.maidInventory.armorInventory[0] = armors[0];
			maid.maidInventory.armorInventory[1] = armors[1];
			maid.maidInventory.armorInventory[2] = armors[2];
			maid.maidInventory.armorInventory[3] = armors[3];
		} else {
			selected = texsel[0];
			mode = false;
			maid.maidInventory.armorInventory[0] = null;
			maid.maidInventory.armorInventory[1] = null;
			maid.maidInventory.armorInventory[2] = null;
			maid.maidInventory.armorInventory[3] = null;
		}
	}

}
