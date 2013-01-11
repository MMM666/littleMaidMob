package net.minecraft.src;

public class LMM_EntityAIJumpToMaster extends EntityAIBase implements LMM_IEntityAI {

	protected LMM_EntityLittleMaid theMaid;
	protected EntityLiving theOwner;
	protected World theWorld;
	protected boolean isEnable;
	private boolean jumpTarget;
	protected AxisAlignedBB boundingBox;
	

	public LMM_EntityAIJumpToMaster(LMM_EntityLittleMaid pEntityLittleMaid) {
		super();
		
		theMaid = pEntityLittleMaid;
		theWorld = pEntityLittleMaid.worldObj;
		isEnable = true;
		boundingBox = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
	}
	
	@Override
	public boolean shouldExecute() {
		if (!isEnable || !theMaid.isMaidContractEX() || theMaid.isMaidWaitEx()) {
			// 契約個体のみが跳ぶ
			return false;
		}
		
		if (theMaid.isFreedom()) {
			// 自由行動の子は基点へジャンプ
			if (theMaid.getHomePosition().getDistanceSquared(MathHelper.floor_double(theMaid.posX), MathHelper.floor_double(theMaid.posY), MathHelper.floor_double(theMaid.posZ)) > 400D) {
				jumpTarget = false;
				mod_LMM_littleMaidMob.Debug(String.format("ID:%d(%s) Jump To Home.",theMaid.entityId, theMaid.worldObj.isRemote ? "C" : "W"));
				return true;
			}
		} else {
			jumpTarget = true;
			theOwner = theMaid.getMaidMasterEntity();
			if (theMaid.getAttackTarget() == null) {
				if (theMaid.mstatMasterDistanceSq < 144D) {
					return false;
				}
			} else {
				// ターゲティング中は距離が伸びる
				if (theMaid.mstatMasterDistanceSq < (theMaid.isBloodsuck() ? 1024D : 256D)) {
					return false;
				}
			}
			mod_LMM_littleMaidMob.Debug(String.format("ID:%d(%s) Jump To Master.",theMaid.entityId, theMaid.worldObj.isRemote ? "C" : "W"));
			return true;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		if (jumpTarget) {
			int i = MathHelper.floor_double(theOwner.posX) - 2;
	        int j = MathHelper.floor_double(theOwner.posZ) - 2;
	        int k = MathHelper.floor_double(theOwner.boundingBox.minY);

	        for (int l = 0; l <= 4; l++) {
	            for (int i1 = 0; i1 <= 4; i1++) {
	                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && theWorld.isBlockNormalCube(i + l, k - 1, j + i1) && !theWorld.isBlockNormalCube(i + l, k, j + i1) && !theWorld.isBlockNormalCube(i + l, k + 1, j + i1)) {
	                	// 主の前に跳ばない
	                	double dd = theOwner.getDistanceSq((double)(i + l) + 0.5D + MathHelper.sin(theOwner.rotationYaw * 0.01745329252F) * 2.0D, (double)k, (double)(j + i1) - MathHelper.cos(theOwner.rotationYaw * 0.01745329252F) * 2.0D);
						if (dd > 8D) {
							theMaid.setTarget(null);
							theMaid.setRevengeTarget(null);
							theMaid.setAttackTarget(null);
		                    theMaid.setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, theMaid.rotationYaw, theMaid.rotationPitch);
		                    theMaid.getNavigator().clearPathEntity();
		                    return;
						}
	                }
	            }
	        }
		} else {
        	// ホームポジションエリア外で転移
    		int lx = theMaid.getHomePosition().posX;
    		int ly = theMaid.getHomePosition().posY;
    		int lz = theMaid.getHomePosition().posZ;
    		if (!(isCanJump(lx, ly, lz))) {
    			// ホームポジション消失
    			mod_LMM_littleMaidMob.Debug(String.format("ID:%d(%s) home lost.",theMaid.entityId, theMaid.worldObj.isRemote ? "C" : "W"));
    			int a;
    			int b;
//    			int c;
    			boolean f = false;
    			// ｙ座標で地面を検出
    			for (a = 1; a < 6 && !f; a++) {
            		if (isCanJump(lx, ly + a, lz)) {
            			f = true;
            			ly += a;
            			break;
            		}
    			}
    			for (a = -1; a > -6 && !f; a--) {
            		if (isCanJump(lx, ly + a, lz)) {
            			f = true;
            			ly += a;
            			break;
            		}
    			}
    			
    			// CW方向に検索領域を広げる 
    			loop_search:
    				for (a = 2; a < 18 && !f; a += 2) {
    					lx--;
    					lz--;
    					for (int c = 0; c < 4; c++) {
    						for (b = 0; b <= a; b++) {
    							// N
    							if (isCanJump(lx, ly + a, lz)) {
    								f = true;
    								break loop_search;
    							}
    							if (c == 0) lx++;
    							else if (c == 1) lz++;
    							else if (c == 2) lx--;
    							else if (c == 3) lz--;
    						}
    					}
    				}
    			if (f) {
    				theMaid.getHomePosition().set(lx, ly, lz);
    				mod_LMM_littleMaidMob.Debug(String.format("Find new position:%d, %d, %d.", lx, ly, lz));
    			} else {
    				if (isCanJump(lx, ly - 6, lz)) {
                		ly -= 6;
    				}
    				mod_LMM_littleMaidMob.Debug(String.format("loss new position:%d, %d, %d.", lx, ly, lz));
    			}
    		} else {
    			mod_LMM_littleMaidMob.Debug(String.format("ID:%d(%s) home solid.",theMaid.entityId, theMaid.worldObj.isRemote ? "C" : "W"));
    		}
    		
    		theMaid.setTarget(null);
    		theMaid.setAttackTarget(null);
    		theMaid.getNavigator().clearPathEntity();
    		theMaid.setLocationAndAngles((double)lx + 05D, (double)ly, (double)lz + 0.5D, theMaid.rotationYaw, theMaid.rotationPitch);
			
		}
		
		mod_LMM_littleMaidMob.Debug(String.format("ID:%d(%s) Jump Fail.",theMaid.entityId, theMaid.worldObj.isRemote ? "C" : "W"));
	}

	/**
	 * 転移先のチェック 
	 */
	protected boolean isCanJump(int px, int py, int pz) {
		double lw = (double)theMaid.width / 2D;
		double ly = (double)py - (double)(theMaid.yOffset + theMaid.ySize);
		boundingBox.setBounds((double)px - lw, ly, (double)pz - lw, (double)px + lw, ly + (double)theMaid.height, (double)pz + lw);
		
		return theWorld.getBlockMaterial(px, py - 1, pz).isSolid() 
				&& theWorld.getAllCollidingBoundingBoxes(boundingBox).isEmpty();
	}
	
	@Override
	public boolean continueExecuting() {
		return false;
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
	public boolean isContinuous() {
		return true;
	}

}
