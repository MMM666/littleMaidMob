package mmm.littleMaidMob.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * NextButtonの丸パクリ
 *
 */
public class GuiButtonNextPage extends GuiButton {

	private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");
	private final boolean direction;


	public GuiButtonNextPage(int par1, int par2, int par3, boolean par4) {
		super(par1, par2, par3, 23, 13, "");
		this.direction = par4;
	}

	@Override
	public void drawButton(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_) {
		if (visible) {
			boolean flag = p_146112_2_ >= xPosition
					&& p_146112_3_ >= yPosition
					&& p_146112_2_ < xPosition + width
					&& p_146112_3_ < yPosition + height;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			p_146112_1_.getTextureManager().bindTexture(bookGuiTextures);
			int k = 0;
			int l = 192;
			
			if (flag) {
				k += 23;
			}
			
			if (!direction) {
				l += 13;
			}
			
			drawTexturedModalRect(xPosition, yPosition, k, l, 23, 13);
		}
	}

}
