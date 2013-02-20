package net.minecraft.src;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class LMM_GuiInventory extends GuiContainer {
	// Field
	private Random rand;
	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;
	private float xSize_lo;
	private float ySize_lo;
	private int ySizebk;
	private int updateCounter;
	public LMM_EntityLittleMaid entitylittlemaid;
	
	public GuiButtonNextPage txbutton[] = new GuiButtonNextPage[4];
	public GuiButton selectbutton;

	// Method
	public LMM_GuiInventory(EntityPlayer pPlayer, LMM_EntityLittleMaid elmaid) {
		super(new LMM_ContainerInventory(pPlayer.inventory, elmaid.maidInventory));
		rand = new Random();
		upperChestInventory = pPlayer.inventory;
		lowerChestInventory = elmaid.maidInventory;
		allowUserInput = false;
		updateCounter = 0;
		ySizebk = ySize;
		ySize = 207;

		entitylittlemaid = elmaid;
		// entitylittlemaid.setOpenInventory(true);
	}

	@Override
	public void initGui() {
		super.initGui();
		if (!entitylittlemaid.getActivePotionEffects().isEmpty()) {
			guiLeft = 160 + (width - xSize - 200) / 2;
		}
		controlList.add(txbutton[0] = new GuiButtonNextPage(100, guiLeft + 25, guiTop + 7, false));
		controlList.add(txbutton[1] = new GuiButtonNextPage(101, guiLeft + 55, guiTop + 7, true));
		controlList.add(txbutton[2] = new GuiButtonNextPage(110, guiLeft + 25, guiTop + 47, false));
		controlList.add(txbutton[3] = new GuiButtonNextPage(111, guiLeft + 55, guiTop + 47, true));
		controlList.add(selectbutton = new GuiButton(200, guiLeft + 25, guiTop + 25, 53, 17, "select"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String ls;
		fontRenderer.drawString(StatCollector.translateToLocal(
				lowerChestInventory.getInvName()), 8, 64, 0x404040);
		fontRenderer.drawString(StatCollector.translateToLocal(
				upperChestInventory.getInvName()), 8, 114, 0x404040);
		fontRenderer.drawString(StatCollector.translateToLocal(
				"littleMaidMob.text.Health"), 86, 8, 0x404040);
		fontRenderer.drawString(StatCollector.translateToLocal(
				"littleMaidMob.text.AP"), 86, 32, 0x404040);
		
		fontRenderer.drawString(StatCollector.translateToLocal(
				"littleMaidMob.mode.".concat(entitylittlemaid.getMaidModeString())), 86, 56, 0x404040);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		// キャラ
		int lj = 0;
		int lk = 0;
		GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(lj + 51, lk + 57, 50F);
		float f1 = 30F;
		GL11.glScalef(-f1, f1, f1);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		float f2 = entitylittlemaid.renderYawOffset;
		float f3 = entitylittlemaid.rotationYaw;
		float f4 = entitylittlemaid.rotationYawHead;
		float f5 = entitylittlemaid.rotationPitch;
//		float f8 = (float) (lj + 51) - xSize_lo;
//		float f9 = (float) (lk + 75) - 50 - ySize_lo;
		float f8 = (float)(guiLeft + 51 - par1);
		float f9 = (float)(guiTop + 22 - par2);
		GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-(float) Math.atan(f9 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
		entitylittlemaid.renderYawOffset = (float) Math.atan(f8 / 40F) * 20F;
		entitylittlemaid.rotationYawHead = entitylittlemaid.rotationYaw = (float) Math.atan(f8 / 40F) * 40F;
		entitylittlemaid.rotationPitch = -(float) Math.atan(f9 / 40F) * 20F;
		GL11.glTranslatef(0.0F, entitylittlemaid.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180F;
		RenderManager.instance.renderEntityWithPosYaw(entitylittlemaid, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		entitylittlemaid.renderYawOffset = f2;
		entitylittlemaid.rotationYaw = f3;
		entitylittlemaid.rotationYawHead = f4;
		entitylittlemaid.rotationPitch = f5;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		// 背景
		String s = MMM_TextureManager.getTextureName(entitylittlemaid.textureName, MMM_TextureManager.tx_gui);
		if (s == null) {
			s = "/gui/littlemaidinventory.png";
		}
		int li = mc.renderEngine.getTexture(s);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(li);
		int lj = guiLeft;
		int lk = guiTop;
		drawTexturedModalRect(lj, lk, 0, 0, xSize, ySize);
		
		// PotionEffect
		displayDebuffEffects();
		
		// LP/AP
		li = mc.renderEngine.getTexture("/gui/icons.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(li);
		
		boolean flag1 = (entitylittlemaid.hurtResistantTime / 3) % 2 == 1;
		if (entitylittlemaid.hurtResistantTime < 10) {
			flag1 = false;
		}
		int i1 = entitylittlemaid.health;
		int j1 = entitylittlemaid.prevHealth;
		rand.setSeed(updateCounter * 0x4c627);
		
		// AP
		int k1 = entitylittlemaid.getTotalArmorValue();
		for (int j2 = 0; j2 < 10; j2++) {
			int k3 = 43 + lk;
			if (k1 > 0) {
				// int j5 = j + 158 - j2 * 8;
				int j5 = lj + 86 + j2 * 8;
				if (j2 * 2 + 1 < k1) {
					drawTexturedModalRect(j5, k3, 34, 9, 9, 9);
				}
				if (j2 * 2 + 1 == k1) {
					drawTexturedModalRect(j5, k3, 25, 9, 9, 9);
				}
				if (j2 * 2 + 1 > k1) {
					drawTexturedModalRect(j5, k3, 16, 9, 9, 9);
				}
			}
			
			// LP
			int k5 = 0;
			if (flag1) {
				k5 = 1;
			}
			int i6 = lj + 86 + j2 * 8;
			k3 = 19 + lk;
			if (i1 <= 4) {
				k3 += rand.nextInt(2);
			}
			drawTexturedModalRect(i6, k3, 16 + k5 * 9, 0, 9, 9);
			if (flag1) {
				if (j2 * 2 + 1 < j1) {
					drawTexturedModalRect(i6, k3, 70, 0, 9, 9);
				}
				if (j2 * 2 + 1 == j1) {
					drawTexturedModalRect(i6, k3, 79, 0, 9, 9);
				}
			}
			if (j2 * 2 + 1 < i1) {
				drawTexturedModalRect(i6, k3, 52, 0, 9, 9);
			}
			if (j2 * 2 + 1 == i1) {
				drawTexturedModalRect(i6, k3, 61, 0, 9, 9);
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		xSize_lo = i;
		ySize_lo = j;
		
		int ii = i - guiLeft;
		int jj = j - guiTop;
		if (ii > 25 && ii < 78 && jj > 7 && jj < 60) {
			// ボタンの表示
			txbutton[0].drawButton = true;
			txbutton[1].drawButton = true;
			txbutton[2].drawButton = true;
			txbutton[3].drawButton = true;
			selectbutton.drawButton = true;
			
			// テクスチャ名称の表示
			GL11.glPushMatrix();
			GL11.glTranslatef(i - ii, j - jj, 0.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			if (entitylittlemaid.textureName != null) {
				int ltw1 = fontRenderer.getStringWidth(entitylittlemaid.textureName);
				int ltw2 = fontRenderer.getStringWidth(entitylittlemaid.textureArmorName);
				int ltwmax = (ltw1 > ltw2) ? ltw1 : ltw2;
				int lbx = 52 - ltwmax / 2;
				int lby = 68;
				int lcolor;
				lcolor = jj < 20 ? 0xc0882222 : 0xc0000000;
				drawGradientRect(lbx - 3, lby - 4, lbx + ltwmax + 3, lby + 8, lcolor, lcolor);
				fontRenderer.drawStringWithShadow(
						entitylittlemaid.textureName, 52 - ltw1 / 2, lby - 2, -1);
				lcolor = jj > 46 ? 0xc0882222 : 0xc0000000;
				drawGradientRect(lbx - 3, lby + 8, lbx + ltwmax + 3, lby + 16 + 4, lcolor, lcolor);
				fontRenderer.drawStringWithShadow(
						entitylittlemaid.textureArmorName, 52 - ltw2 / 2, lby + 10, -1);
			}
			
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		} else {
			txbutton[0].drawButton = false;
			txbutton[1].drawButton = false;
			txbutton[2].drawButton = false;
			txbutton[3].drawButton = false;
			selectbutton.drawButton = false;
		}
		
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		updateCounter++;
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
/*
		// 26,8-77,59
		int ii = i - guiLeft;
		int jj = j - guiTop;
		
		// TODO:メイドアセンブル画面を作る
		if (ii > 25 && ii < 78 && jj > 7 && jj < 60) {
			// 伽羅表示領域
			if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
				// Shift+で逆周り
				LMM_Client.setPrevTexturePackege(entitylittlemaid, k);
			} else {
				LMM_Client.setNextTexturePackege(entitylittlemaid, k);
			}
			LMM_Client.setTextureValue(entitylittlemaid);
		}
*/
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		switch (par1GuiButton.id) {
		case 100:
			LMM_Client.setNextTexturePackege(entitylittlemaid, 0);
			LMM_Client.setTextureValue(entitylittlemaid);
			break;
		case 101:
			LMM_Client.setPrevTexturePackege(entitylittlemaid, 0);
			LMM_Client.setTextureValue(entitylittlemaid);
			break;
		case 110:
			LMM_Client.setNextTexturePackege(entitylittlemaid, 1);
			LMM_Client.setTextureValue(entitylittlemaid);
			break;
		case 111:
			LMM_Client.setPrevTexturePackege(entitylittlemaid, 1);
			LMM_Client.setTextureValue(entitylittlemaid);
			break;
		case 200:
			int ldye = 0;
			if (mc.thePlayer.capabilities.isCreativeMode) {
				ldye = 0xffff;
			} else {
				for (ItemStack lis : mc.thePlayer.inventory.mainInventory) {
					if (lis != null && lis.itemID == Item.dyePowder.itemID) {
						ldye |= (1 << (15 - lis.getItemDamage()));
					}
				}
			}
			mc.displayGuiScreen(new LMM_GuiTextureSelect(this, entitylittlemaid, ldye, true));
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		// entitylittlemaid.onGuiClosed();
		entitylittlemaid.sendTextureToServer();
	}

	private void displayDebuffEffects() {
		// ポーションエフェクトの表示
		int i = guiLeft - 124;
		int j = guiTop;
		int k = mc.renderEngine.getTexture("/gui/inventory.png");
		Collection collection = entitylittlemaid.getActivePotionEffects();
		if (collection.isEmpty()) {
			return;
		}
		int l = 33;
		if (collection.size() > 5) {
			l = 132 / (collection.size() - 1);
		}
		for (Iterator iterator = entitylittlemaid.getActivePotionEffects().iterator(); iterator.hasNext();) {
			PotionEffect potioneffect = (PotionEffect) iterator.next();
			Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.renderEngine.bindTexture(k);
			drawTexturedModalRect(i, j, 0, ySizebk, 140, 32);
			if (potion.hasStatusIcon()) {
				int i1 = potion.getStatusIconIndex();
				drawTexturedModalRect(i + 6, j + 7, 0 + (i1 % 8) * 18, ySizebk
						+ 32 + (i1 / 8) * 18, 18, 18);
			}
			String s = StatCollector.translateToLocal(potion.getName());
			if (potioneffect.getAmplifier() > 0) {
				s = (new StringBuilder())
						.append(s)
						.append(" ")
						.append(StatCollector.translateToLocal((new StringBuilder())
								.append("potion.potency.")
								.append(potioneffect.getAmplifier())
								.toString())).toString();
			}
			fontRenderer.drawStringWithShadow(s, i + 10 + 18, j + 6, 0xffffff);
			String s1 = Potion.getDurationString(potioneffect);
			fontRenderer.drawStringWithShadow(s1, i + 10 + 18, j + 6 + 10,
					0x7f7f7f);
			j += l;
		}
	}

}
