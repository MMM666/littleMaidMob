package net.minecraft.src;

public class LMM_ContainerInventory extends ContainerPlayer {
	
	protected LMM_InventoryLittleMaid littlemaidInventory;
	protected int numRows;
	protected LMM_EntityLittleMaid owner;


	public LMM_ContainerInventory(IInventory iinventory, LMM_EntityLittleMaid pEntity) {
		// >
		// Forge対策、ContainerPlayer継承でなければ要らない、SlotArmor用
		super(pEntity.maidInventory, !pEntity.worldObj.isRemote, pEntity.maidAvatar);
		inventorySlots.clear();
		inventoryItemStacks.clear();
		// <
		
		LMM_InventoryLittleMaid linventory = pEntity.maidInventory;
		owner = pEntity;
		numRows = linventory.getSizeInventory() / 9;
		littlemaidInventory = linventory;
		littlemaidInventory.openChest();
		
		for (int ly = 0; ly < numRows; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				addSlotToContainer(new Slot(linventory, lx + ly * 9, 8 + lx * 18, 76 + ly * 18));
			}
		}
		
		int lyoffset = (numRows - 4) * 18 + 59;
		for (int ly = 0; ly < 3; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				addSlotToContainer(new Slot(iinventory, lx + ly * 9 + 9, 8 + lx * 18, 103 + ly * 18 + lyoffset));
			}
		}
		
		for (int lx = 0; lx < 9; lx++) {
			addSlotToContainer(new Slot(iinventory, lx, 8 + lx * 18, 161 + lyoffset));
		}
		
		for (int j = 0; j < 3; j++) {
			int j1 = j + 1;
			addSlotToContainer(new SlotArmor(this, linventory, linventory.getSizeInventory() - 2 - j, 8, 8 + j * 18, j1));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// 開けるかどうかの判定
		LMM_EntityLittleMaid entitylittlemaid = littlemaidInventory.entityLittleMaid; 
		if(entitylittlemaid.isDead) {
//		if(entitylittlemaid.isDead || entitylittlemaid.isOpenInventory()) {
			return false;
		}
		return entityplayer.getDistanceSqToEntity(entitylittlemaid) <= 64D;
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

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);
		littlemaidInventory.closeChest();
	}

	@Override
	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot) {
		return true;
	}

}
