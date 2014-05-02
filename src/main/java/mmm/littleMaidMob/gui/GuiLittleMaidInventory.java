package mmm.littleMaidMob.gui;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.inventory.ContainerLittleMaid;
import net.minecraft.entity.player.EntityPlayer;

public class GuiLittleMaidInventory extends GuiEffectRenderer {

	public EntityLittleMaidBase maid;


	public GuiLittleMaidInventory(EntityLittleMaidBase pMaid, EntityPlayer pPlayer) {
		super(new ContainerLittleMaid(pMaid, pPlayer), pMaid);
		maid = pMaid;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		// TODO Auto-generated method stub
		
	}

}
