package mmm.littleMaidMob.inventory;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerInventory extends Container {

	public EntityLittleMaidBase maid;
	public EntityPlayer player;
	protected int numRows;


	public ContainerInventory(EntityLittleMaidBase pMaid, EntityPlayer pPlayer) {
		maid = pMaid;
		player = pPlayer;
		
		// Slotの構築
		maid.inventory.openInventory();
		
		numRows = maid.inventory.getSizeInventory() / 9;
		for (int ly = 0; ly < numRows; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				addSlotToContainer(new Slot(maid.inventory, lx + ly * 9, 8 + lx * 18, 76 + ly * 18));
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
			addSlotToContainer(new SlotArmor(maid.inventory, maid.inventory.getSizeInventory() - 2 - j, 8, 8 + j * 18, j + 1, maid));
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
		maid.inventory.closeInventory();
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3,
			EntityPlayer par4EntityPlayer) {
		// TODO Auto-generated method stub
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int pIndex) {
		ItemStack litemstack = null;
		Slot slot = (Slot)inventorySlots.get(pIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			litemstack = itemstack1.copy();
			int lline = numRows * 9;
			if (pIndex < lline) {
				if (!this.mergeItemStack(itemstack1, lline, lline + 36, true)) {
					return null;
				}
			} else if (pIndex >= lline && pIndex < lline + 36) {
				if (!this.mergeItemStack(itemstack1, 0, lline, false)) {
					return null;
				}
			} else {
				if (!this.mergeItemStack(itemstack1, 0, lline + 36, false)) {
					return null;
				}
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return litemstack;
	}


}
