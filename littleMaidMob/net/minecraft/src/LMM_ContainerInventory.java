package net.minecraft.src;

public class LMM_ContainerInventory extends Container {
	
	private LMM_InventoryLittleMaid littlemaidInventory;
	private int numRows;


	public LMM_ContainerInventory(IInventory iinventory, LMM_InventoryLittleMaid iinventory1) {
		numRows = iinventory1.getSizeInventory() / 9;
		littlemaidInventory = iinventory1;
		littlemaidInventory.openChest();
		
		for (int j = 0; j < numRows; j++) {
			for (int i1 = 0; i1 < 9; i1++) {
				addSlotToContainer(new Slot(iinventory1, i1 + j * 9, 8 + i1 * 18, 76 + j * 18));
			}
		}
		
		int i = (numRows - 4) * 18 + 59;
		for (int k = 0; k < 3; k++) {
			for (int j1 = 0; j1 < 9; j1++) {
				addSlotToContainer(new Slot(iinventory, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
			}
		}
		
		for (int l = 0; l < 9; l++) {
			addSlotToContainer(new Slot(iinventory, l, 8 + l * 18, 161 + i));
		}
		
		for (int j = 0; j < 3; j++) {
			int j1 = j + 1;
			addSlotToContainer(new SlotArmor(null, iinventory1, iinventory1.getSizeInventory() - 2 - j, 8, 8 + j * 18, j1));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// ŠJ‚¯‚é‚©‚Ç‚¤‚©‚Ì”»’è
		LMM_EntityLittleMaid entitylittlemaid = littlemaidInventory.entityLittleMaid; 
		if(entitylittlemaid.isDead) {
//		if(entitylittlemaid.isDead || entitylittlemaid.isOpenInventory()) {
			return false;
		}
		return entityplayer.getDistanceSqToEntity(entitylittlemaid) <= 64D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int j = numRows * 9;
            if(i < j)
            {
                mergeItemStack(itemstack1, j, j + 36, true);
            } else
            if(i >= j && i < j + 36)
            {
            	mergeItemStack(itemstack1, 0, j, false);
            } else {
            	mergeItemStack(itemstack1, 0, j + 36, false);
            }
            if(itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            } else
            {
                slot.onSlotChanged();
            }
            if(itemstack1.stackSize != itemstack.stackSize)
            {
            	slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
            } else
            {
                return null;
            }
        }
        return itemstack;
    }

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		// TODO Auto-generated method stub
		super.onCraftGuiClosed(par1EntityPlayer);
		littlemaidInventory.closeChest();
	}

}
