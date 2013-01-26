package net.minecraft.src;

public class LMM_GuiIFF extends MMM_GuiMobSelect {

	public static final String IFFString[] = { "ENEMY", // 反撃、狩
			"UNKNOWN", // 反撃
			"FRIENDLY" // 攻撃しない
	};

	protected LMM_EntityLittleMaid target;


	public LMM_GuiIFF(World world, LMM_EntityLittleMaid pEntity) {
		super(world);
		screenTitle = "LittleMaid IFF";
		target = pEntity;
	}

	@Override
	protected boolean checkEntity(String pName, Entity pEntity, int pIndex) {
		boolean lf = false;
		// Entityの値を設定
		int liff = LMM_IFF.checkEntityStatic(pName, pEntity, pIndex, entityMap);
		if (pEntity instanceof EntityLiving) {
			if (pEntity instanceof LMM_EntityLittleMaid) {
				if (pIndex == 0 || pIndex == 1) {
					// 野生種、自分契約者
					lf = true;
				} else {
					// 契約者
				}
			} else if (pEntity instanceof EntityTameable) {
				if (pIndex == 0 || pIndex == 1) {
					// 野生種、自分の
					lf = true;
				} else {
					// 他人の家畜
				}
			}
		}

		return lf;
	}

	@Override
	public void initGui() {
		super.initGui();

		StringTranslate stringtranslate = StringTranslate.getInstance();

		// controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168,
		// 200, 20, stringtranslate.translateKey("gui.done")));
		controlList.add(new GuiButton(200, width / 2 - 130, height - 40, 120, 20,
				stringtranslate.translateKey("gui.done")));
		controlList.add(new GuiButton(201, width / 2 + 10, height - 40, 120, 20,
				"Trigger Select"));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		if (guibutton.id == 200) {
			// mc.gameSettings.saveOptions();
			mc.displayGuiScreen(null);
		}
		if (guibutton.id == 201) {
			mc.displayGuiScreen(new LMM_GuiTriggerSelect(mc.thePlayer, this));
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}

	@Override
	public void onGuiClosed() {
		LMM_Net.saveIFF();
		super.onGuiClosed();
	}

	@Override
	public void clickSlot(int pIndex) {
		String s = entityMap.keySet().toArray()[pIndex].toString();
		int tt = LMM_IFF.getIFF(null, s);
		tt++;
		if (tt > 2) {
			tt = 0;
		}
		
		// LMM_GuiIFF.IFFMap.put(s, tt);
		// if (mc.getIntegratedServer() == null) {
		if (!mc.isIntegratedServerRunning()) {
			// サーバーへ変更値を送る。
			byte[] ldata = new byte[s.length() + 2];
			ldata[0] = LMM_Net.LMN_Server_SetIFFValue;
			ldata[1] = (byte) tt;
			LMM_Net.sendToServer(ldata);
		} else {
			LMM_IFF.setIFFValue(null, s, tt);
		}
		mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
	}

	@Override
	public void drawSlot(int pSlotindex, int pX, int pY, int pDrawheight,
			Tessellator pTessellator, String pName, Entity pEntity) {
		// 名前と敵味方識別の描画
		int tt = LMM_IFF.getIFF(null, pName);
		int c = 0xffffff;
		switch (tt) {
		case LMM_IFF.iff_Friendry:
			c = 0x3fff3f;
			break;
		case LMM_IFF.iff_Unknown:
			c = 0xffff00;
			break;
		case LMM_IFF.iff_Enemy:
			c = 0xff3f3f;
			break;
		}
		// drawString(fontRenderer, LMM_GuiIFF.IFFString[tt], pX + 78 + width /
		// 2, pY + 18, c);
		// drawString(fontRenderer, pName, pX + 70, pY + 6, 0xffffff);
		drawString(fontRenderer, LMM_GuiIFF.IFFString[tt],
				(width - fontRenderer.getStringWidth(LMM_GuiIFF.IFFString[tt])) / 2, pY + 18, c);
		drawString(fontRenderer, pName,
				(width - fontRenderer.getStringWidth(pName)) / 2, pY + 6, 0xffffff);
	}

}
