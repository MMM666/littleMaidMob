package mmm.littleMaidMob.inventory;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerLittleMaid extends Container {

	public EntityLittleMaidBase maid;
	public EntityPlayer player;


	public ContainerLittleMaid(EntityLittleMaidBase pMaid, EntityPlayer pPlayer) {
		maid = pMaid;
		player = pPlayer;
		
		// Slotの構築
		
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO Auto-generated method stub
		return false;
	}

}
