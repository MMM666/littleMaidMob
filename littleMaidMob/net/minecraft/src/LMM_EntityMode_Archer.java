package net.minecraft.src;

import java.lang.reflect.Method;
import java.util.List;

public class LMM_EntityMode_Archer extends LMM_EntityModeBase {

	public static final int mmode_Archer		= 0x0083;
	public static final int mmode_Blazingstar	= 0x00c3;
	
	
	@Override
	public int priority() {
		return 3200;
	}

	public LMM_EntityMode_Archer(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public void init() {
		// 登録モードの名称追加
		ModLoader.addLocalization("littleMaidMob.mode.Archer", "Archer");
		ModLoader.addLocalization("littleMaidMob.mode.F-Archer", "F-Archer");
		ModLoader.addLocalization("littleMaidMob.mode.T-Archer", "T-Archer");
		ModLoader.addLocalization("littleMaidMob.mode.D-Archer", "D-Archer");
//		ModLoader.addLocalization("littleMaidMob.mode.Archer", "ja_JP", "射手");
		ModLoader.addLocalization("littleMaidMob.mode.Blazingstar", "Blazingstar");
		ModLoader.addLocalization("littleMaidMob.mode.F-Blazingstar", "F-Blazingstar");
		ModLoader.addLocalization("littleMaidMob.mode.T-Blazingstar", "T-Blazingstar");
		ModLoader.addLocalization("littleMaidMob.mode.D-Blazingstar", "D-Blazingstar");
//		ModLoader.addLocalization("littleMaidMob.mode.Blazingstar", "ja_JP", "刃鳴散らす者");
		LMM_GuiTriggerSelect.appendTriggerItem("Bow", "");
		LMM_GuiTriggerSelect.appendTriggerItem("Arrow", "");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Archer:0x0083
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = new EntityAITasks(owner.aiProfiler);
		
//		ltasks[1].addTask(1, new EntityAIOwnerHurtByTarget(owner));
//		ltasks[1].addTask(2, new EntityAIOwnerHurtTarget(owner));
		ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(owner, true));
		ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(owner, EntityLiving.class, 16F, 0, true));
		
		owner.addMaidMode(ltasks, "Archer", mmode_Archer);
		
		
		// Blazingstar:0x00c3
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = pDefaultMove;
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);
		
		ltasks2[1].addTask(1, new LMM_EntityAIHurtByTarget(owner, true));
		ltasks2[1].addTask(2, new LMM_EntityAINearestAttackableTarget(owner, EntityLiving.class, 16F, 0, true));
		
		owner.addMaidMode(ltasks2, "Blazingstar", mmode_Blazingstar);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (litemstack.getItem() instanceof ItemBow || LMM_GuiTriggerSelect.checkWeapon("Bow", litemstack)) {
				if (owner.maidInventory.getInventorySlotContainItem(ItemFlintAndSteel.class) > -1) {
					owner.setMaidMode("Blazingstar");
				} else {
					owner.setMaidMode("Archer");
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Archer :
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(true);
			owner.setBloodsuck(false);
			return true;
		case mmode_Blazingstar :
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(true);
			owner.setBloodsuck(true);
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
		case mmode_Archer :
		case mmode_Blazingstar :
			for (li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;

				// 射手
				if (litemstack.getItem() instanceof ItemBow || LMM_GuiTriggerSelect.checkWeapon("Bow", litemstack)) {
					return li;
				}
			}
			break;
		}

		return -1;
	}
	
	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return (pItemStack.getItem() instanceof ItemBow) || (pItemStack.itemID == Item.arrow.itemID) 
				|| LMM_GuiTriggerSelect.checkWeapon("Bow", pItemStack) || LMM_GuiTriggerSelect.checkWeapon("Arrow", pItemStack);
	}
	
	@Override
	public void updateAITick(int pMode) {
		switch (pMode) {
		case mmode_Archer:
			owner.getWeaponStatus();
			updateGuns();
			break;
		case mmode_Blazingstar:
			owner.getWeaponStatus();
			updateGuns();
			// Blazingstarの特殊効果
			World lworld = owner.worldObj;
			List<Entity> llist = lworld.getEntitiesWithinAABB(Entity.class, owner.boundingBox.expand(16D, 16D, 16D));
			for (int li = 0; li < llist.size(); li++) {
				Entity lentity = llist.get(li); 
				if (lentity.isEntityAlive() && lentity.isBurning() && owner.rand.nextFloat() > 0.9F) {
					// 発火！
					int lx = (int)owner.posX;
					int ly = (int)owner.posY;
					int lz = (int)owner.posZ;
					if (lworld.getBlockId(lx, ly, lz) == 0 || lworld.getBlockMaterial(lx, ly, lz).getCanBurn()) {
						lworld.playSoundEffect((double)lx + 0.5D, (double)ly + 0.5D, (double)lz + 0.5D, "fire.ignite", 1.0F, owner.rand.nextFloat() * 0.4F + 0.8F);
						lworld.setBlockWithNotify(lx, ly, lz, Block.fire.blockID);
					}
				}
			}
			break;
		}
	}
	
	protected void updateGuns() {
		if (owner.getAttackTarget() == null || !owner.getAttackTarget().isEntityAlive()) {
			// 対象が死んだ
			if (!owner.weaponReload) {
				if (owner.maidAvatar.isUsingItem()) {
					// ターゲットが死んでいる時はアイテムの使用をクリア
					if (owner.maidAvatar.isItemReload) {
						owner.maidAvatar.stopUsingItem();
						mod_LMM_littleMaidMob.Debug(String.format("id:%d cancel reload.", owner.entityId));
					} else {
						owner.maidAvatar.clearItemInUse();
						mod_LMM_littleMaidMob.Debug(String.format("id:%d clear.", owner.entityId));
					}
				}
			} else {
				owner.mstatAimeBow = true;
			}
		}
		if (owner.weaponReload && !owner.maidAvatar.isUsingItem()) {
			// 特殊リロード
			owner.maidInventory.getCurrentItem().useItemRightClick(owner.worldObj, owner.maidAvatar);
			mod_LMM_littleMaidMob.Debug(String.format("id:%d force reload.", owner.entityId));
			owner.mstatAimeBow = true;
		}

	}
	
}
