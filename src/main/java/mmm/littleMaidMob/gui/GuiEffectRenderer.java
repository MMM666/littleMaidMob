package mmm.littleMaidMob.gui;

import java.util.Collection;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * InventoryEffectRendererの丸パクリ<br>
 * プレーヤー以外のエフェクトも対象にする
 */
public abstract class GuiEffectRenderer extends GuiContainer {

	protected EntityLivingBase target;
	protected boolean isEffect;


	public GuiEffectRenderer(Container par1Container, EntityLivingBase pTarget) {
		super(par1Container);
		target = pTarget;
	}

	@Override
	public void initGui() {
		super.initGui();
		
		if (!target.getActivePotionEffects().isEmpty()) {
			guiLeft = 160 + (width - xSize - 200) / 2;
			isEffect = true;
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		
		if (isEffect) {
			drawEffects();
		}
	}

	/**
	 * ポーションエフェクトを描画する
	 */
	protected void drawEffects() {
		int i = guiLeft - 124;
		int j = guiTop;
		
		Collection<?> collection = target.getActivePotionEffects();
		
		if (!collection.isEmpty()) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			int k = 33;
			
			if (collection.size() > 5) {
				k = 132 / (collection.size() - 1);
			}
			
			for (Iterator<?> iterator = target.getActivePotionEffects().iterator();
					iterator.hasNext(); j += k) {
				PotionEffect potioneffect = (PotionEffect) iterator.next();
				Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(field_147001_a);
				drawTexturedModalRect(i, j, 0, 166, 140, 32);
				
				if (potion.hasStatusIcon()) {
					int l = potion.getStatusIconIndex();
					drawTexturedModalRect(i + 6, j + 7, 0 + l % 8 * 18,
							198 + l / 8 * 18, 18, 18);
				}
				
				String s1 = I18n.format(potion.getName(), new Object[0]);
				
				if (potioneffect.getAmplifier() == 1) {
					s1 = s1 + " II";
				} else if (potioneffect.getAmplifier() == 2) {
					s1 = s1 + " III";
				} else if (potioneffect.getAmplifier() == 3) {
					s1 = s1 + " IV";
				}
				
				fontRendererObj.drawStringWithShadow(
						s1, i + 10 + 18, j + 6, 0xffffff);
				String s = Potion.getDurationString(potioneffect);
				fontRendererObj.drawStringWithShadow(
						s, i + 10 + 18, j + 6 + 10, 0x7f7f7f);
			}
		}
	}

}
