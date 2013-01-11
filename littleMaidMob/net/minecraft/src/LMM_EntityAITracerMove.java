package net.minecraft.src;

public class LMM_EntityAITracerMove extends EntityAIBase implements LMM_IEntityAI {
	
	protected LMM_EntityLittleMaid theMaid; 
	protected World world;
	protected boolean isEnable;
	protected int tileX;
	protected int tileY;
	protected int tileZ;

	
	public LMM_EntityAITracerMove(LMM_EntityLittleMaid pEntityLittleMaid) {
		theMaid = pEntityLittleMaid;
		world = pEntityLittleMaid.worldObj;
		isEnable = false;
		
		setMutexBits(1);
	}

	@Override
	public void setEnable(boolean pFlag) {
		isEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return isEnable;
	}

	@Override
	public boolean shouldExecute() {
		return isEnable && !theMaid.isMaidWaitEx() &&  theMaid.getNavigator().noPath();
	}
	
	@Override
	public boolean continueExecuting() {
		return !theMaid.getNavigator().noPath();
	}
	
	@Override
	public void startExecuting() {
		// ルート策定
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
			for (int a = 2; a < 14; a += 2) {
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
	    				if (checkBlock(xx, yy, zz)) {
	    					if (doFindBlock(xx, yy, zz)) {
	    						// TODO:Dummay
		    					MMM_EntityDummy.setDummyEntity(theMaid, 0x004f4fff, xx, yy, zz);
    				    		flagdammy = true;
	    						return;
	    					}
    						// TODO:Dummay
	    					MMM_EntityDummy.setDummyEntity(theMaid, 0x004fff4f, xx, yy, zz);
				    		flagdammy = true;
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
	}

	/**
	 * 指定座標のブロックは探しているものか？
	 */
	protected boolean checkBlock(int px, int py, int pz) {
		return world.isBlockIndirectlyProvidingPowerTo(px, py, pz, 0) && (world.getBlockMaterial(px, py + 1, pz) == Material.air);
	}
	
	/**
	 * 見つけたブロックに対する動作。
	 * trueを返すとループ終了。
	 */
	protected boolean doFindBlock(int px, int py, int pz) {
		return theMaid.getNavigator().tryMoveToXYZ(px, py, pz, theMaid.getAIMoveSpeed());
	}
	
}
