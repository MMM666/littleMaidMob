package net.minecraft.src;

public class LMM_EntityAIFindBlock extends EntityAIBase implements LMM_IEntityAI {

	protected boolean isEnable;
	protected LMM_EntityLittleMaid theMaid;
	protected LMM_EntityModeBase llmode;
	protected MovingObjectPosition theBlock;
	protected int tileX;
	protected int tileY;
	protected int tileZ;
//	protected boolean isFind;
	
	
	public LMM_EntityAIFindBlock(LMM_EntityLittleMaid pEntityLittleMaid) {
		theMaid = pEntityLittleMaid;
		isEnable = true;
		theBlock = null;
		
		setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute() {
		llmode = theMaid.getActiveModeClass();
//		LMM_EntityModeBase llmode = theMaid.getActiveModeClass();
//		if (!isEnable || theMaid.isWait() || theMaid.getActiveModeClass() == null || !theMaid.getActiveModeClass().isSearchBlock() || theMaid.getCurrentEquippedItem() == null) {
		if (!isEnable || theMaid.isMaidWait() || llmode == null) {
			return false;
		}
		if (!llmode.isSearchBlock()) {
			return llmode.shouldBlock(theMaid.getMaidModeInt());
		}
		
		// ターゲットをサーチ
		tileX = MathHelper.floor_double(theMaid.posX);
		tileY = MathHelper.floor_double(theMaid.posY);
		tileZ = MathHelper.floor_double(theMaid.posZ);
		int vt = MathHelper.floor_float(((theMaid.rotationYawHead * 4F) / 360F) + 2.5F) & 3;
		int xx = tileX;
		int yy = tileY;
		int zz = tileZ;
		
		// TODO:Dummy
		MMM_EntityDummy.clearDummyEntity(theMaid);
		boolean flagdammy = false;
		
		// CW方向に検索領域を広げる 
		for (int d = 0; d < 4; d++) {
			for (int a = 0; a < 18; a += 2) {
				int del = a / 2;
				if (vt == 0) {
					xx = tileX - del;
					zz = tileZ - del;
				} 
				else if (vt == 1) { 
					xx = tileX + del;
					zz = tileZ - del;
				} 
				else if (vt == 2) { 
					xx = tileX + del;
					zz = tileZ + del;
				} 
				else if (vt == 3) { 
					xx = tileX - del;
					zz = tileZ + del;
				}
				// TODO:Dummay
				if (!flagdammy) {
					MMM_EntityDummy.setDummyEntity(theMaid, 0x00ff4f4f, xx, tileY, zz);
					flagdammy = true;
				}
				int b = 0;
				do {
					for (int c = 0; c < 3; c++) {
						yy = tileY + (c == 2 ? -1 : c);
						if (llmode.checkBlock(theMaid.getMaidModeInt(), xx, yy, zz)) {
							if (llmode.outrangeBlock(theMaid.getMaidModeInt(), xx, yy, zz)) {
								tileX = xx;
								tileY = yy;
								tileZ = zz;
								// TODO:Dummay
								MMM_EntityDummy.setDummyEntity(theMaid, 0x004fff4f, xx, yy, zz);
								flagdammy = true;
								return true;
							}
						}
					}
					// TODO:Dummay
					if (!flagdammy) {
						MMM_EntityDummy.setDummyEntity(theMaid, 0x00ffffcf, xx, tileY, zz);
						flagdammy = true;
					}
					// TODO:dammy
					flagdammy = false;
					
					if (vt == 0) {
						xx++;
					} 
					else if (vt == 1) { 
						zz++;
					} 
					else if (vt == 2) { 
						xx--;
					} 
					else if (vt == 3) { 
						zz--;
					}
					
				} while(++b < a);
			}
			vt = (vt + 1) & 3;
		}
		TileEntity ltile = llmode.overlooksBlock(theMaid.getMaidModeInt());
		if (ltile != null) {
			tileX = ltile.xCoord;
			tileY = ltile.yCoord;
			tileZ = ltile.zCoord;
			// TODO:Dummay
			MMM_EntityDummy.setDummyEntity(theMaid, 0x004fff4f, tileX, tileY, tileZ);
			flagdammy = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean continueExecuting() {
		// 移動中は継続
		if (!theMaid.getNavigator().noPath()) return true;
		
		double ld = theMaid.getDistanceSq(tileX, tileY, tileZ);
		if (ld > 100.0D) {
			// 索敵範囲外
			theMaid.getActiveModeClass().farrangeBlock();
			return false;
		} else if (ld > 5.0D) {
			// 射程距離外
			return theMaid.getActiveModeClass().outrangeBlock(theMaid.getMaidModeInt(), tileX, tileY, tileZ);
		} else {
			// 射程距離
			return theMaid.getActiveModeClass().executeBlock(theMaid.getMaidModeInt(), tileX, tileY, tileZ);
		}
	}

	@Override
	public void startExecuting() {
		llmode.startBlock(theMaid.getMaidModeInt());
	}

	@Override
	public void resetTask() {
		llmode.resetBlock(theMaid.getMaidModeInt());
	}

	@Override
	public void updateTask() {
		// ターゲットを見つけている
		theMaid.getLookHelper().setLookPosition(tileX + 0.5D, tileY + 0.5D, tileZ + 0.5D, 10F, theMaid.getVerticalFaceSpeed());
	}


	@Override
	public void setEnable(boolean pFlag) {
		isEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return isEnable;
	}

}
