package mmm.littleMaidMob.gui;

import org.lwjgl.opengl.GL11;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.inventory.ContainerInventory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiLittleMaidInventory extends GuiEffectRenderer {

	protected static ResourceLocation defGuiTex = new ResourceLocation("mmm", "textures/gui/container/littlemaidinventory.png");

	public EntityLittleMaidBase maid;
	public GuiButtonNextPage txbutton[] = new GuiButtonNextPage[4];
	public GuiButton selectbutton;


	public GuiLittleMaidInventory(EntityLittleMaidBase pMaid, EntityPlayer pPlayer) {
		super(new ContainerInventory(pMaid, pPlayer), pMaid);
		maid = pMaid;
		ySize = 207;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(txbutton[0] = new GuiButtonNextPage(100, guiLeft + 25, guiTop + 7, false));
		buttonList.add(txbutton[1] = new GuiButtonNextPage(101, guiLeft + 55, guiTop + 7, true));
		buttonList.add(txbutton[2] = new GuiButtonNextPage(110, guiLeft + 25, guiTop + 47, false));
		buttonList.add(txbutton[3] = new GuiButtonNextPage(111, guiLeft + 55, guiTop + 47, true));
		buttonList.add(selectbutton = new GuiButton(200, guiLeft + 25, guiTop + 25, 53, 17, "select"));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		// 背景
//		ResourceLocation lrl = maid.textureData.getGUITexture();
		ResourceLocation lrl = null;
		if (lrl == null) {
			lrl = defGuiTex;
		}
		mc.getTextureManager().bindTexture(lrl);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int lx = guiLeft;
		int ly = guiTop;
		drawTexturedModalRect(lx, ly, 0, 0, xSize, ySize);
		
		// LP/AP
//		drawHeathArmor(0, 0);
	}

}
