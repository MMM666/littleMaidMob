package net.minecraft.src;

/**
 * 選択時にサーバーへ染料の使用を通知するための処理。
 */
public class LMM_GuiTextureSelect extends MMM_GuiTextureSelect {

	public LMM_GuiTextureSelect(GuiScreen pOwner, MMM_ITextureEntity pTarget,
			int pColor, boolean pToServer) {
		super(pOwner, pTarget, pColor, pToServer);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		super.actionPerformed(par1GuiButton);
		switch (par1GuiButton.id) {
		case 200:
			if (toServer) {
				if (selectColor != selectPanel.color) {
					// 色情報の設定
//					theMaid.maidColor = selectPanel.color | 0x010000 | (selectColor << 8);
					// サーバーへ染料の使用を通知
					byte ldata[] = new byte[2];
					ldata[0] = LMM_Statics.LMN_Server_DecDyePowder;
					ldata[1] = (byte)selectColor;
					LMM_Net.sendToServer(ldata);
				}
			}
			break;
		}
	}

}
