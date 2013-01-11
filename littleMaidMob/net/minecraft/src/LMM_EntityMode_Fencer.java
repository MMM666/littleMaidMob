package net.minecraft.src;

import java.util.List;

/**
 * 独自基準としてモード定数は0x0080は平常、0x00c0は血まみれモードと区別。
 */
public class LMM_EntityMode_Fencer extends LMM_EntityModeBase{

	public static final int mmode_Fencer		= 0x0080;
	public static final int mmode_Bloodsucker	= 0x00c0;

	
	public LMM_EntityMode_Fencer(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 3000;
	}
	
	@Override
	public void init() {
		// 登録モードの名称追加
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "ja_JP", "護衛剣士");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "ja_JP", "血に飢えた冥土");
		LMM_GuiTriggerSelect.appendTriggerItem("Sword", "");
		LMM_GuiTriggerSelect.appendTriggerItem("Axe", "");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Fencer:0x0080
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = new EntityAITasks(owner.aiProfiler);

		ltasks[1].addTask(1, new EntityAIOwnerHurtByTarget(owner));
		ltasks[1].addTask(2, new EntityAIOwnerHurtTarget(owner));
		ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(owner, true));
		ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(owner, EntityLiving.class, 16F, 0, true));

		owner.addMaidMode(ltasks, "Fencer", mmode_Fencer);


		// Bloodsucker:0x00c0
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = pDefaultMove;
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);

		ltasks2[1].addTask(1, new LMM_EntityAIHurtByTarget(owner, true));
		ltasks2[1].addTask(2, new LMM_EntityAINearestAttackableTarget(owner, EntityLiving.class, 16F, 0, true));

		owner.addMaidMode(ltasks2, "Bloodsucker", mmode_Bloodsucker);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (litemstack.getItem() instanceof ItemSword || LMM_GuiTriggerSelect.checkWeapon("Sword", litemstack)) {
				owner.setMaidMode("Fencer");
				return true;
			} else  if (litemstack.getItem() instanceof ItemAxe || LMM_GuiTriggerSelect.checkWeapon("Axe", litemstack)) {
				owner.setMaidMode("Bloodsucker");
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Fencer :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(false);
			return true;
		case mmode_Bloodsucker :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(true);
			return true;
		}
		
		return false;
	}
	
	@Override
	public int getNextEquipItem(int pMode) {
		int li;
		int ll = -1;
		int ld = 0;
		int lld;
		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Fencer : 
			for (li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;

				// 剣
				if (litemstack.getItem() instanceof ItemSword || LMM_GuiTriggerSelect.checkWeapon("Sword", litemstack)) {
					return li;
				}

				// 攻撃力な高いものを記憶する
				lld = 1;
				try {
					lld = litemstack.getDamageVsEntity(null);
				}
				catch (Exception e) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		case mmode_Bloodsucker :
			for (li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;
				
				// 斧
				if (litemstack.getItem() instanceof ItemAxe || LMM_GuiTriggerSelect.checkWeapon("Axe", litemstack)) {
					return li;
				}

				// 攻撃力な高いものを記憶する
				lld = 1;
				try {
					lld = litemstack.getDamageVsEntity(null);
				}
				catch (Exception e) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		}

		return ll;
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		// 装備アイテムを回収
		return pItemStack.getItem() instanceof ItemSword || pItemStack.getItem() instanceof ItemAxe;
	}
	
}
