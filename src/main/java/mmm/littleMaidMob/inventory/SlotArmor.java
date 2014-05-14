package mmm.littleMaidMob.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class SlotArmor extends Slot {

	protected final int type;
	protected final Entity owner;
	
	public SlotArmor(IInventory par1iInventory, int par2, int par3, int par4, int pType, Entity pOwner) {
		super(par1iInventory, par2, par3, par4);
		type = pType;
		owner = pOwner;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		if (par1ItemStack == null) return false;
		return par1ItemStack.getItem().isValidArmor(par1ItemStack, type, owner);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getBackgroundIconIndex() {
		return ItemArmor.func_94602_b(type);
	}

}
