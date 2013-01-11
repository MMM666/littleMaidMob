package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class LMM_EntityMode_Cooking extends LMM_EntityModeBase {

	public static final int mmode_Cooking = 0x0021;
	private TileEntityFurnace myTile;
	private TileEntityFurnace mySerch;
	private double myleng;
	private boolean isWorking;
	
	public LMM_EntityMode_Cooking(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
		myTile = null;
		mySerch = null;
		isWorking = false;
	}

	@Override
	public int priority() {
		return 6000;
	}

	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Cooking", "Cooking");
		ModLoader.addLocalization("littleMaidMob.mode.T-Cooking", "T-Cooking");
		ModLoader.addLocalization("littleMaidMob.mode.F-Cooking", "F-Cooking");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Cooking:0x0021
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = new EntityAITasks(owner.aiProfiler);
		
		owner.addMaidMode(ltasks, "Cooking", mmode_Cooking);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (owner.maidInventory.isItemBurned(0)) {
				owner.setMaidMode("Cooking");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Cooking :
			owner.setBloodsuck(false);
			owner.aiJumpTo.setEnable(false);
			owner.aiFollow.setEnable(false);
			owner.aiAvoidPlayer.setEnable(false);
			return true;
		}
		
		return false;
	}

	@Override
	public int getNextEquipItem(int pMode) {
		int li;
		ItemStack litemstack;
		
		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Cooking :
			for (li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				// 調理
				if (owner.maidInventory.isItemBurned(li)) {
					return li;
				}
			}
			break;
		}

		return -1;
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return LMM_InventoryLittleMaid.isItemBurned(pItemStack) || LMM_InventoryLittleMaid.isItemSmelting(pItemStack);
	}

	@Override
	public boolean isSearchBlock() {
		// 燃焼アイテムを持っている？
		if (owner.getCurrentEquippedItem() != null && owner.maidInventory.getSmeltingItem() > -1) {
			mySerch = null;
			owner.setSneaking(false);
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldBlock(int pMode) {
		return false;
	}

	@Override
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		TileEntity ltile = owner.worldObj.getBlockTileEntity(px, py, pz);
		if (!(ltile instanceof TileEntityFurnace)) {
			return false;
		}
		
		// 世界のメイドから
		for (Object lo : owner.worldObj.getLoadedEntityList()) {
			if (lo == owner) continue;
			if (lo instanceof LMM_EntityLittleMaid) {
				LMM_EntityLittleMaid lem = (LMM_EntityLittleMaid)lo;
				if (lem.isUsingTile(ltile)) {
					// 誰かが使用中
					return false;
				}
				if (myTile != null && lem.isUsingTile(myTile)) {
					// 手持ちを誰かが使ってるならクリア
					myTile = null;
				}
			}
		}
		if (myTile != null) {
			// 使用していた竈ならそこで終了
			return myTile == ltile;
		}
		
		if (mySerch != null) {
			double lleng = ltile.getDistanceFrom(owner.posX, owner.posY, owner.posZ);
			if (lleng < myleng) {
				mySerch = (TileEntityFurnace)ltile;
				myleng = lleng;
			}
		} else {
			mySerch = (TileEntityFurnace)ltile;
			myleng = mySerch.getDistanceFrom(owner.posX, owner.posY, owner.posZ);
		}
		
//		owner.setSneaking(false);
		return false;
	}

	@Override
	public TileEntity overlooksBlock(int pMode) {
		return myTile = mySerch;
	}

	@Override
	public boolean executeBlock(int pMode, int px, int py, int pz) {
		TileEntityFurnace ltile = myTile;
		if (owner.worldObj.getBlockTileEntity(px, py, pz) != ltile) {
			return false;
		}		
		
		ItemStack litemstack;
		boolean lflag = false;
		int li;
		
		if (owner.getSwingStatusDominant().canAttack()) {
			// 完成品回収
			litemstack = ltile.getStackInSlot(2);
			if (litemstack != null) {
				if (litemstack.stackSize > 0) {
					if (owner.maidInventory.addItemStackToInventory(litemstack)) {
						owner.playSoundAtEntity("random.pop");
						owner.setSwing(5, LMM_EnumSound.cookingOver);
//                    	if (!pEntityLittleMaid.maidInventory.isItemBurned(pEntityLittleMaid.maidInventory.currentItem)) {
						owner.getNextEquipItem();
//                    	}
						lflag = true;
					}
				}
				ltile.setInventorySlotContents(2, null);
			}
				
			// 調理可能品を竈にぽーい
			if (!lflag && ltile.getStackInSlot(0) == null) {
				litemstack = ltile.getStackInSlot(2);
				li = owner.maidInventory.getSmeltingItem();
				owner.setEquipItem(li);
				if (li > -1) {
					litemstack = owner.maidInventory.getStackInSlot(li);
					// レシピ対応品
					if (litemstack.stackSize >= ltile.getInventoryStackLimit()) {
						ltile.setInventorySlotContents(0, litemstack.splitStack(ltile.getInventoryStackLimit()));
					} else {
						ltile.setInventorySlotContents(0, litemstack.splitStack(litemstack.stackSize));
					}
					if (litemstack.stackSize <= 0) {
						owner.maidInventory.setInventorySlotContents(li, null);
					}
					owner.playSoundAtEntity("random.pop");
					owner.setSwing(5, LMM_EnumSound.cookingStart);
					lflag = true;
				}
			}
			
			// 手持ちの燃料をぽーい
			if (!lflag && ltile.getStackInSlot(1) == null && ltile.getStackInSlot(0) != null) {
				owner.getNextEquipItem();
				litemstack = owner.getCurrentEquippedItem();
				if (LMM_InventoryLittleMaid.isItemBurned(litemstack)) {
					if (litemstack.stackSize >= ltile.getInventoryStackLimit()) {
						ltile.setInventorySlotContents(1, litemstack.splitStack(ltile.getInventoryStackLimit()));
					} else {
						ltile.setInventorySlotContents(1, litemstack.splitStack(litemstack.stackSize));
					}
					if (litemstack.stackSize <= 0) {
						owner.maidInventory.setInventoryCurrentSlotContents(null);
					}
					owner.getNextEquipItem();
					owner.playSoundAtEntity("random.pop");
					owner.setSwing(5, LMM_EnumSound.addFuel);
					lflag = true;
				} else {
					if (ltile.isBurning()) {
						lflag = true;
					} else {
						// 燃やせるアイテムを持ってないので調理可能品を回収
						ItemStack litemstack2 = ltile.getStackInSlotOnClosing(0);
						if (owner.maidInventory.addItemStackToInventory(litemstack2)) {
							owner.playSoundAtEntity("random.pop");
							owner.setSwing(5, LMM_EnumSound.Null);
							owner.getNextEquipItem();
							lflag = false;
						} else {
							ltile.setInventorySlotContents(0, litemstack2);
						}
					}
				}
			} 
			
			// 燃え終わってるのに燃料口に何かあるなら回収する
			if (!lflag && !ltile.isBurning() && ltile.getStackInSlot(1) != null) {
				ItemStack litemstack2 = ltile.getStackInSlotOnClosing(1);
				if (owner.maidInventory.addItemStackToInventory(litemstack2)) {
					owner.playSoundAtEntity("random.pop");
					owner.setSwing(5, LMM_EnumSound.Null);
					owner.getNextEquipItem();
					lflag = owner.maidInventory.isItemBurned(owner.getCurrentEquippedItem());
				} else {
					ltile.setInventorySlotContents(1, litemstack2);
				}
			}
		} else {
			lflag = true;
		}
		if (ltile.isBurning()) {
			owner.setWorking(true);
			owner.setSneaking(py - (int)owner.posY <= 0);
			lflag = true;
		}
//mod_LMM_littleMaidMob.Debug("work" + lflag);
		return lflag;
	}

	@Override
	public void startBlock(int pMode) {
		isWorking = true;
		mySerch = null;
	}

	@Override
	public void resetBlock(int pMode) {
		isWorking = false;
		owner.setSneaking(false);
	}

	@Override
	public boolean isUsingTile(TileEntity pTile) {
		return isWorking && myTile == pTile;
	}
	
}
