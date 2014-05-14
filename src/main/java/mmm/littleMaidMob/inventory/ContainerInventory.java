package mmm.littleMaidMob.inventory;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerInventory extends Container {

	public EntityLittleMaidBase maid;
	public EntityPlayer player;


	public ContainerInventory(EntityLittleMaidBase pMaid, EntityPlayer pPlayer) {
		maid = pMaid;
		player = pPlayer;
		
		// Slotの構築
		// >
		// Forge対策、ContainerPlayer継承でなければ要らない、SlotArmor用
		// <
		
		InventoryLittleMaid linventory = maid.inventry;
		int numRows = linventory.getSizeInventory() / 9;
		linventory.openInventory();
		
		for (int ly = 0; ly < numRows; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				addSlotToContainer(new Slot(linventory, lx + ly * 9, 8 + lx * 18, 76 + ly * 18));
			}
		}
		
		int lyoffset = (numRows - 4) * 18 + 59;
		for (int ly = 0; ly < 3; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				addSlotToContainer(new Slot(player.inventory, lx + ly * 9 + 9, 8 + lx * 18, 103 + ly * 18 + lyoffset));
			}
		}
		
		for (int lx = 0; lx < 9; lx++) {
			addSlotToContainer(new Slot(player.inventory, lx, 8 + lx * 18, 161 + lyoffset));
		}
		
		for (int j = 0; j < 3; j++) {
			int j1 = j + 1;
			addSlotToContainer(new SlotArmor(linventory, linventory.getSizeInventory() - 2 - j, 8, 8 + j * 18, j1, maid));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// 開けるかどうかの判定
		if(maid.isDead) {
//		if(maid.isDead || maid.isOpenInventory()) {
			return false;
		}
		return maid.getDistanceSqToEntity(var1) <= 64D;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		// 閉じた時の処理
		super.onContainerClosed(par1EntityPlayer);
		maid.inventry.closeInventory();
	}

}
