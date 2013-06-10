package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class LMM_EntityMode_Pharmacist extends LMM_EntityModeBase {

	public static final int mmode_Pharmacist = 0x0022;
	private TileEntityBrewingStand myTile;
	private TileEntityBrewingStand mySerch;
	private double myleng;
	private boolean isWorking;

	protected int maidSearchCount;


	public LMM_EntityMode_Pharmacist(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 6100;
	}

	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Pharmacist", "Pharmacist");
		ModLoader.addLocalization("littleMaidMob.mode.T-Pharmacist", "T-Pharmacist");
		ModLoader.addLocalization("littleMaidMob.mode.F-Pharmacist", "F-Pharmacist");
		ModLoader.addLocalization("littleMaidMob.mode.F-Pharmacist", "D-Pharmacist");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Pharmacist:0x0022
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
		
		owner.addMaidMode(ltasks, "Pharmacist", mmode_Pharmacist);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (litemstack.getItem() instanceof ItemPotion && !MMM_Helper.hasEffect(litemstack)) {
				owner.setMaidMode("Pharmacist");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Pharmacist :
			owner.setBloodsuck(false);
			owner.aiJumpTo.setEnable(false);
			owner.aiFollow.setEnable(false);
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
		case mmode_Pharmacist : 
			for (li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack != null) {
					// 対象は水ポーション
					if (litemstack.getItem() instanceof ItemPotion && !MMM_Helper.hasEffect(litemstack)) {
						return li;
					}
				}
			}
			break;
		}
		
		return -1;
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return false;
	}

	@Override
	public boolean isSearchBlock() {
		mySerch = null;
		owner.setSneaking(false);
		return true;
	}

	@Override
	public boolean shouldBlock(int pMode) {
		// 実行中判定
/*
		if (myTile.getBrewTime() > 0 
				|| myTile.getStackInSlot(0) != null | myTile.getStackInSlot(1) != null || myTile.getStackInSlot(2) != null 
				|| myTile.getStackInSlot(3) != null) {
			return true;
		}
		if (owner.getCurrentEquippedItem() != null && owner.maidInventory.getSmeltingItem() > -1) {
			return true;
		}
*/
		return false;
	}

	@Override
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		if (owner.getCurrentEquippedItem() == null) {
			return false;
		}
		TileEntity ltile = owner.worldObj.getBlockTileEntity(px, py, pz);
		if (!(ltile instanceof TileEntityBrewingStand)) {
			return false;
		}
		
		// 世界のメイドから
		for (Object lo : owner.worldObj.loadedEntityList) {
			if (lo == owner) continue;
			if (lo instanceof LMM_EntityLittleMaid) {
				LMM_EntityLittleMaid lem = (LMM_EntityLittleMaid)lo;
				if (lem.isUsingTile(ltile)) {
					return false;
				}
				if (lem.isUsingTile(myTile)) {
					myTile = null;
				}
			}
		}
		if (myTile != null) {
			return myTile == ltile;
		}

		if (mySerch != null) {
//			double lleng = ltile.getDistanceFrom(owner.posX, owner.posY, owner.posZ);
			double lleng = owner.getDistance(ltile.xCoord + 0.5D, ltile.yCoord + 0.5D, ltile.zCoord + 0.5D);
			if (lleng < myleng) {
				mySerch = (TileEntityBrewingStand)ltile;
				myleng = lleng;
			}
		} else {
			mySerch = (TileEntityBrewingStand)ltile;
//			myleng = mySerch.getDistanceFrom(owner.posX, owner.posY, owner.posZ);
			myleng = owner.getDistance(mySerch.xCoord + 0.5D, mySerch.yCoord + 0.5D, mySerch.zCoord + 0.5D);
		}
		return false;
	}

	@Override
	public TileEntity overlooksBlock(int pMode) {
		return myTile = mySerch;
	}

	@Override
	public boolean executeBlock(int pMode, int px, int py, int pz) {
		TileEntityBrewingStand ltile = myTile;
		if (owner.worldObj.getBlockTileEntity(px, py, pz) != ltile) {
			return false;
		}		
		
		ItemStack litemstack1;
		boolean lflag = false;
		LMM_SwingStatus lswing = owner.getSwingStatusDominant();
		
		// 蒸留待機
//    	isMaidChaseWait = true;
		if (ltile.getStackInSlot(0) != null || ltile.getStackInSlot(1) != null || ltile.getStackInSlot(2) != null || ltile.getStackInSlot(3) != null || !lswing.canAttack()) {
			// お仕事中
			owner.setWorking(true);
		}
		
		if (lswing.canAttack()) {
			ItemStack litemstack2 = ltile.getStackInSlot(3);
			
			if (litemstack2 != null && ltile.getBrewTime() <= 0) {
				// 蒸留不能なので回収
				if (py <= owner.posY) {
					owner.setSneaking(true);
				}
				lflag = true;
				if (owner.maidInventory.addItemStackToInventory(litemstack2)) {
					ltile.setInventorySlotContents(3, null);
					owner.playSound("random.pop");
					owner.setSwing(5, LMM_EnumSound.Null);
				}
			}
			// 完成品
			if (!lflag && maidSearchCount > owner.maidInventory.mainInventory.length) {
				// ポーションの回収
				for (int li = 0; li < 3 && !lflag; li ++) {
					litemstack1 = ltile.getStackInSlot(li);
					if (litemstack1 != null && owner.maidInventory.addItemStackToInventory(litemstack1)) {
						ltile.setInventorySlotContents(li, null);
						owner.playSound("random.pop");
						owner.setSwing(5, LMM_EnumSound.Null);
						lflag = true;
					}
				}
				if (!lflag) {
					owner.getNextEquipItem();
					maidSearchCount = 0;
					lflag = true;
				}
			}
			
			litemstack1 = owner.maidInventory.getCurrentItem();
			if (!lflag && (litemstack1 != null && litemstack1.getItem() instanceof ItemPotion && !MMM_Helper.hasEffect(litemstack1))) {
				// 水瓶をげっとれでぃ
				int li = 0;
				for (li = 0; li < 3 && !lflag; li++) {
					if (ltile.getStackInSlot(li) == null) {
						ltile.setInventorySlotContents(li, litemstack1);
						owner.maidInventory.setInventoryCurrentSlotContents(null);
						owner.playSound("random.pop");
						owner.setSwing(5, LMM_EnumSound.Null);
						owner.getNextEquipItem();
						lflag = true;
					}
				}
			}
			if (!lflag && (ltile.getStackInSlot(0) != null || ltile.getStackInSlot(1) != null || ltile.getStackInSlot(2) != null)
					&& (owner.maidInventory.currentItem == -1 || (litemstack1 != null && litemstack1.getItem() instanceof ItemPotion && !MMM_Helper.hasEffect(litemstack1)))) {
				// ポーション以外を検索
				for (maidSearchCount = 0; maidSearchCount < owner.maidInventory.mainInventory.length; maidSearchCount++) {
					litemstack1 = owner.maidInventory.getStackInSlot(maidSearchCount);
					if (litemstack1 != null && !(litemstack1.getItem() instanceof ItemPotion)) {
						owner.setEquipItem(maidSearchCount);
//						owner.maidInventory.currentItem = maidSearchCount;
						lflag = true;
						break;
					}
				}
			}
			
			if (!lflag && litemstack2 == null && (ltile.getStackInSlot(0) != null || ltile.getStackInSlot(1) != null || ltile.getStackInSlot(2) != null)) {
				// 手持ちのアイテムをぽーい
				if (litemstack1 != null && !(litemstack1.getItem() instanceof ItemPotion) && litemstack1.getItem().isPotionIngredient()) {
					ltile.setInventorySlotContents(3, litemstack1);
					owner.maidInventory.setInventorySlotContents(maidSearchCount, null);
					owner.playSound("random.pop");
					owner.setSwing(15, LMM_EnumSound.Null);
					lflag = true;
				} 
				else if (litemstack1 == null || (litemstack1.getItem() instanceof ItemPotion && MMM_Helper.hasEffect(litemstack1)) || !litemstack1.getItem().isPotionIngredient()) {
					// 対象外アイテムを発見した時に終了
					maidSearchCount = owner.maidInventory.mainInventory.length;
					lflag = false;
				}
				maidSearchCount++;
//				owner.maidInventory.currentItem = maidSearchCount;
				owner.setEquipItem(maidSearchCount);
			}
			
			
			// 終了状態のキャンセル
			if (owner.getSwingStatusDominant().index == -1 && litemstack2 == null) {
				owner.getNextEquipItem();
			}
		} else {
			lflag = true;
		}
		if (ltile.getBrewTime() > 0) {
			owner.setWorking(true);
			lflag = true;
		}
		return lflag;
	}

	@Override
	public void startBlock(int pMode) {
		isWorking = true;
		maidSearchCount = 0;
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
