package net.minecraft.src;

public class LMM_SwingStatus {

	public int index;
	public int lastIndex;
	public boolean isSwingInProgress;
	public float swingProgress;
	public float prevSwingProgress;
	public int swingProgressInt;
	public float onGround;
	public int attackTime;


	public LMM_SwingStatus() {
		index = lastIndex = -1;
		isSwingInProgress = false;
		swingProgress = prevSwingProgress = 0.0F;
		onGround = 0F;
		attackTime = 0;
	}

	/**
	 * TODO:数値の更新用、onEntityUpdate内で呼ぶ事:いらんか？
	 */
	public void onEntityUpdate(LMM_EntityLittleMaid pEntity) {
		prevSwingProgress = swingProgress;
	}

	/**
	 * 数値の更新用、onUpdate内で呼ぶ事
	 */
	public void onUpdate(LMM_EntityLittleMaid pEntity) {
		prevSwingProgress = swingProgress;
		if (attackTime > 0) {
			attackTime--;
		}
		
		// 腕振り
		int li = pEntity.getSwingSpeedModifier();
		if (isSwingInProgress) {
			if (swingProgressInt == 0) {
				pEntity.playLittleMaidSound(pEntity.maidAttackSound, true);
			}
			pEntity.maidAttackSound = LMM_EnumSound.Null;
			
			swingProgressInt++;
			if(swingProgressInt >= li) {
				swingProgressInt = 0;
				isSwingInProgress = false;
			}
		} else {
			swingProgressInt = 0;
		}
		swingProgress = (float)swingProgressInt / (float)li;
	}

	/**
	 * 選択中のスロット番号を設定
	 */
	public void setSlotIndex(int pIndex) {
		index = pIndex;
		lastIndex = -2;
	}

	/**
	 * 選択中のインベントリ内アイテムスタックを返す
	 */
	public ItemStack getItemStack(LMM_EntityLittleMaid pEntity) {
		if (index > -1) {
			return pEntity.maidInventory.getStackInSlot(index);
		} else {
			return null;
		}
	}

	public boolean canAttack() {
		return attackTime <= 0;
	}

	public float getSwingProgress(float ltime) {
		float lf = swingProgress - prevSwingProgress;
		
		if (lf < 0.0F) {
			++lf;
		}
		
		return onGround = prevSwingProgress + lf * ltime;
	}

	/**
	 * 変更があるかどうかを返し、フラグをクリアする。
	 */
	public boolean checkChanged() {
		boolean lflag = index != lastIndex;
		lastIndex = index;
		return lflag;
	}

}
