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
	protected List<MMM_TextureBox> indexTexture;
	protected List<MMM_TextureBox> indexArmor;
	public boolean mode;
	public int texsel[] = new int[2];
	public int color;
	protected int selectColor;
	private ItemStack armors[] = new ItemStack[] {
			new ItemStack(Item.bootsLeather),
			new ItemStack(Item.legsLeather),
			new ItemStack(Item.plateLeather),
			new ItemStack(Item.helmetLeather)
	};
	protected boolean isContract;
	protected static MMM_TextureBox blankBox;


	public LMM_GuiTextureSlot(LMM_GuiTextureSelect pOwner) {
		super(pOwner.mc, pOwner.width, pOwner.height, 16, pOwner.height - 64, 36);
		owner = pOwner;
		maid = new LMM_EntityLittleMaid(pOwner.mc.theWorld);
		color = owner.theMaid.maidColor;
		selectColor = -1;
		blankBox = new MMM_TextureBox();
		blankBox.models = new MMM_ModelMultiBase[] {null, null, null};
		
		texsel[0] = -1;
		texsel[1] = -1;
		indexTexture = new ArrayList<MMM_TextureBox>();
		indexArmor = new ArrayList<MMM_TextureBox>();
		isContract = owner.theMaid.maidContract;
		maid.maidContract = isContract;
		for (int li = 0; li < MMM_TextureManager.textures.size(); li++) {
			MMM_TextureBox lbox = MMM_TextureManager.textures.get(li);
			if (isContract) {
				if (lbox.getContractColorBits() > 0) {
					indexTexture.add(lbox);
				}
			} else {
				if (lbox.getWildColorBits() > 0) {
					indexTexture.add(lbox);
				}
			}
			if (lbox.hasArmor()) {
				indexArmor.add(lbox);
			}
			if (lbox == owner.theMaid.textureBox[0]) {
				texsel[0] = indexTexture.size() - 1;
			}
			if (lbox == owner.theMaid.textureBox[1]) {
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
		if (mode) {
			selected = var1;
			texsel[1] = var1;
		} else {
			MMM_TextureBox lbox = getSelectedBox(var1);
			if (lbox.hasColor(selectColor) && (owner.canSelectColor & (1 << selectColor)) > 0) {
				selected = var1;
				texsel[0] = var1;
				owner.selectColor = selectColor;
			} else if (lbox.hasColor(color)) {
				selected = var1;
				texsel[0] = var1;
				owner.selectColor = color;
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
			for (int li = 0; li < 16; li++) {
				int lx = var2 + 15 + 12 * li;
				selectColor = (mouseX - (var2 + 15)) / 12;
				if ((selectColor < 0) && (selectColor > 15)) {
					selectColor = -1;
				}
				if (color == li) {
					owner.drawRect(lx, var3, lx + 11, var3 + 36, 0x88882222);
				} else if (owner.selectColor == li) {
					owner.drawRect(lx, var3, lx + 11, var3 + 36, 0x88226622);
				} else if ((owner.canSelectColor & (1 << li)) > 0) {
					owner.drawRect(lx, var3, lx + 11, var3 + 36, 0x88222288);
				}
			}
		}
		
		MMM_TextureBox lbox;
		if (mode) {
			lbox = indexArmor.get(var1);
			maid.textureBox[0] = blankBox;
			maid.textureBox[1] = lbox;
		} else {
			lbox = indexTexture.get(var1);
			maid.textureBox[0] = lbox;
			maid.textureBox[1] = blankBox;
		}
		MMM_TextureManager.checkTextureBoxServer(lbox);
		GL11.glDisable(GL11.GL_BLEND);
		owner.fontRenderer.drawStringWithShadow(lbox.textureName, var2 + 16, var3 + 25, -1);
		GL11.glTranslatef(var2 + 8F, var3 + 25F, 50F);
		GL11.glScalef(12F, -12F, 12F);
		maid.renderYawOffset = 30F;
		maid.rotationYawHead = 15F;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		if (mode) {
			// アーマー
			GL11.glTranslatef(1F, 0, 0);
			Map<Integer, String> lmap = lbox.armors.get("default");
			if (lmap != null) {
				maid.textureArmor1[0] = maid.textureArmor1[1] = 
						maid.textureArmor1[2] = maid.textureArmor1[3] =
								lbox.getArmorTextureName(true, "default", 0);
				maid.textureArmor2[0] = maid.textureArmor2[1] = 
						maid.textureArmor2[2] = maid.textureArmor2[3] =
								lbox.getArmorTextureName(false, "default", 0);
				RenderManager.instance.renderEntityWithPosYaw(maid, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
			}
			for (String ls : MMM_TextureManager.armorFilenamePrefix) {
				GL11.glTranslatef(1F, 0, 0);
				if (lbox.armors.containsKey(ls)) {
					maid.textureArmor1[0] = maid.textureArmor1[1] = 
							maid.textureArmor1[2] = maid.textureArmor1[3] = lbox.getArmorTextureName(true, ls, 0);
					maid.textureArmor2[0] = maid.textureArmor2[1] = 
							maid.textureArmor2[2] = maid.textureArmor2[3] = lbox.getArmorTextureName(false, ls, 0);
					RenderManager.instance.renderEntityWithPosYaw(maid, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
				}
			}
		} else {
			// テクスチャ表示
			for (int li = 0; li < 16; li++) {
				GL11.glTranslatef(1F, 0, 0);
				if (lbox.hasColor(li, isContract)) {
					maid.maidColor = li;
					maid.maidContract = isContract;
					maid.texture = lbox.getTextureName(li + (isContract ? 0 : MMM_TextureManager.tx_wild));
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
		return mode ? indexArmor.get(pIndex) : indexTexture.get(pIndex);
	}

	public MMM_TextureBox getSelectedBox(boolean pMode) {
		return pMode ? indexArmor.get(texsel[1]) : indexTexture.get(texsel[0]);
	}

	public void setMode(boolean pFlag) {
		func_77208_b(slotHeight * -getSize());
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
		func_77208_b(slotHeight * selected);
	}

}
