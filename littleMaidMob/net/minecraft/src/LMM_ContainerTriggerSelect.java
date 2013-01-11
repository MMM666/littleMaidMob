package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class LMM_ContainerTriggerSelect extends ContainerCreative {

	public List<ItemStack> weaponSelect = new ArrayList<ItemStack>();
	public String weaponSelectName;
	public List<Integer> weaponSelectList;
	public int weaponOffset;
	
	
	public LMM_ContainerTriggerSelect(EntityPlayer entityplayer) {
		super(entityplayer);

		inventorySlots.clear();
        for(int l2 = 0; l2 < 5; l2++)
        {
            for(int j3 = 0; j3 < 8; j3++)
            {
            	addSlotToContainer(new Slot(LMM_GuiTriggerSelect.getInventory1(), j3 + l2 * 8, 8 + j3 * 18, 18 + l2 * 18));
            }

        }

        for(int l2 = 0; l2 < 4; l2++) {
            for(int j3 = 0; j3 < 8; j3++) {
            	addSlotToContainer(new Slot(LMM_GuiTriggerSelect.getInventory2(), j3 + l2 * 8, 8 + j3 * 18, 121 + l2 * 18));
            }

        }

        setWeaponSelect(LMM_GuiTriggerSelect.selector.keySet().toArray(new String[0])[0]);

        initAllSelections();
        scrollTo(0.0F);
        setWeaponlist(0.0F);
	}
	
    private void initAllSelections() {
    	// コンテナ表示用アイテムの設定
    	this.itemList.clear();
        Item[] var2 = Item.itemsList;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Item var5 = var2[var4];

            if (var5 != null && var5.getCreativeTab() != null) {
                var5.getSubItems(var5.itemID, (CreativeTabs)null, this.itemList);
            }
        }
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public void scrollTo(float f) {
		// スクロールポジション
        int i = (itemList.size() / 8 - 5) + 1;
        int j = (int)((double)(f * (float)i) + 0.5D);
        if(j < 0)
        {
            j = 0;
        }
        for(int k = 0; k < 5; k++)
        {
            for(int l = 0; l < 8; l++)
            {
                int i1 = l + (k + j) * 8;
                if(i1 >= 0 && i1 < itemList.size())
                {
                	LMM_GuiTriggerSelect.getInventory1().setInventorySlotContents(l + k * 8, (ItemStack)itemList.get(i1));
                } else
                {
                	LMM_GuiTriggerSelect.getInventory1().setInventorySlotContents(l + k * 8, null);
                }
            }

        }

    }

	@Override
	public ItemStack slotClick(int i, int j, int flag, EntityPlayer entityplayer) {
		if (i >= 40) {
			// セットされたアイテムを定義
			int lk = (i - 40) + weaponOffset * 8;
			for (; weaponSelect.size() <= lk + 7;) {
				weaponSelect.add(null);
			}
			weaponSelect.set(lk, entityplayer.inventory.getItemStack());
		}
		
		if (i == -999) {
			entityplayer.inventory.setItemStack(null);
		}
		ItemStack is = super.slotClick(i, j, flag, entityplayer);

		return is;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
    	// Shiftクリック時の反応
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot != null && slot.getHasStack()) {
        	if (i < 40) {
            	ItemStack itemstack1 = slot.getStack();
                itemstack = itemstack1.copy();
                mergeItemStack(itemstack1, 40, 72, false);
        	} else {
                slot.putStack(null);
        	}
        }
        return itemstack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack itemstack, int i, int j, boolean flag) {
    	// itemstack以外は無効
        boolean flag1 = false;
        int k = 0;
        
        // 同じのがあったときは追加しない
        while (itemstack.stackSize > 0 && k < weaponSelect.size()) {
            ItemStack itemstack1 = weaponSelect.get(k);
            if(itemstack1 != null) {
            	if (itemstack1.itemID == itemstack.itemID && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage())) {
                	// 同一アイテムである
                    flag1 = true;
                    break;
            	}
            } else {
            	weaponSelect.set(k, itemstack);
                flag1 = true;
                break;
            }
            k++;
        }
        if (!flag1) {
    		weaponSelect.add(itemstack);
    		setWeaponlist(1.0F);
        } else {
            int m = (weaponSelect.size() / 8 - 4) + 1;
            int n = k / 8;
            float f = (float)n / (float)m;
            if (f < 0.0F) f = 0.0F;
            if (f > 1.0F) f = 1.0F;
            setWeaponlist(f);
        }

        return flag1;
    }

	
    public void setWeaponlist(float f) {
    	// スクロールポジション
        int i = (weaponSelect.size() / 8 - 4) + 1;
        weaponOffset = (int)((double)(f * (float)i) + 0.5D);
        if(weaponOffset < 0) {
            weaponOffset = 0;
        }
        for (int k = 0; k < 4; k++) {
        	for (int l = 0; l < 8; l++) {
                int i1 = l + (k + weaponOffset) * 8;
        		if (i1 >= 0 && i1 < weaponSelect.size()) {
        			LMM_GuiTriggerSelect.getInventory2().setInventorySlotContents(k * 8 + l, weaponSelect.get(i1));
        		} else {
        			LMM_GuiTriggerSelect.getInventory2().setInventorySlotContents(k * 8 + l, null);
        		}
        	}
        }
    }
    
	public void setWeaponSelect(String pName) {
		weaponSelect.clear();
		weaponSelectName = pName;
		weaponSelectList = LMM_GuiTriggerSelect.selector.get(pName);
        for (Integer li: weaponSelectList) {
        	if (Item.itemsList[li] == null) continue;
        	weaponSelect.add(new ItemStack(Item.itemsList[li]));
        }		
	}
	
	public List getItemList() {
		return weaponSelectList;
	}
	
}
