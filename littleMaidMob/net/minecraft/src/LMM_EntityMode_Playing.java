package net.minecraft.src;

import java.util.List;

public class LMM_EntityMode_Playing extends LMM_EntityModeBase {

	public static final int mmode_Playing	= 0x00ff;

	public static final int mpr_NULL = 0;
	public static final int mpr_QuickShooter = 0x0010;
	public static final int mpr_StockShooter = 0x0020;
	
	public int fcounter;

	public LMM_EntityMode_Playing(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
		fcounter = 0;
	}

	@Override
	public int priority() {
		return 900;
	}

	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.T-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.F-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.D-Playing", "Playing");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Playing:0x00ff
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
//		ltasks[1] = new EntityAITasks(owner.aiProfiler);
		
//		ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(owner, true));
//		ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(owner, EntityLiving.class, 16F, 0, true));
		
		owner.addMaidMode(ltasks, "Playing", mmode_Playing);
		
	}

	protected boolean checkSnows(int x, int y, int z) {
		// 周りが雪か？
		boolean f = true;
		f &= owner.worldObj.getBlockId(x, y, z) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x + 1, y, z) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x - 1, y, z) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x, y, z + 1) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x, y, z - 1) == Block.snow.blockID;
		
		return f;
	}

	protected boolean movePlaying() {
		//
		int x = MathHelper.floor_double(owner.posX);
		int y = MathHelper.floor_double(owner.posY);
		int z = MathHelper.floor_double(owner.posZ);
		PathEntity pe = null;
		
		// CW方向に検索領域を広げる 
		loop_search:
			for (int a = 2; a < 18 && pe == null; a += 2) {
				x--;
				z--;
				for (int b = 0; b < a; b++) {
					// N
					for (int c = 0; c < 4; c++) {
						if (checkSnows(x, y, z)) {
							pe = owner.worldObj.getEntityPathToXYZ(owner, x, y - 1, z, 10F, true, false, false, true);
							if (pe != null) {
								break loop_search;
							}
						}
						if (c == 0) x++;
						if (c == 1) z++;
						if (c == 2) x--;
						if (c == 3) z--;
					}
				}
			}
		
		if (pe != null) {
			owner.getNavigator().setPath(pe, owner.moveSpeed);
			mod_LMM_littleMaidMob.Debug("Find Snow Area-%d:%d, %d, %d.", owner.entityId, x, y, z);
			return true;
		} else {
			return false;
		}
	}

	protected void playingSnowWar() {
		switch (fcounter) {
		case 0:
			// 有り玉全部投げる
			owner.setSitting(false);
			owner.setSneaking(false);
			if (!owner.getNextEquipItem()) {
				owner.setAttackTarget(null);
				
				owner.getNavigator().clearPathEntity();
				fcounter = 1;
			} else if (owner.getAttackTarget() == null) {
				// メイドとプレーヤー（無差別）をターゲットに
				List<Entity> list = owner.worldObj.getEntitiesWithinAABBExcludingEntity(owner, owner.boundingBox.expand(16D, 4D, 16D));
				for (Entity e : list) {
					if (e != null && (e instanceof EntityPlayer || e instanceof LMM_EntityLittleMaid)) {
						if (owner.rand.nextBoolean()) {
							owner.setAttackTarget((EntityLiving)e);
							break;
						}
					}
				}
			}
			break;
		case 1:
			// 乱数加速
			owner.setAttackTarget(null);
			if (owner.getNavigator().noPath()) {
				fcounter = 2;
			}
			break;
		
		case 2:
			// 雪原を探す
			if (owner.getAttackTarget() == null && owner.getNavigator().noPath()) {
				if (movePlaying()) {
					fcounter = 3;
				} else {
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				}
			} else {
				owner.setAttackTarget(null);
			}
//			isMaidChaseWait = true;
			break;
		case 3:
			// 雪原へ到着
			if (owner.getNavigator().noPath()) {
				if (checkSnows(
						MathHelper.floor_double(owner.posX),
						MathHelper.floor_double(owner.posY),
						MathHelper.floor_double(owner.posZ))) {
//					owner.isMaidChaseWait = true;
					owner.attackTime = 30;
					if (owner.getPlayingRole() == mpr_QuickShooter) {
						fcounter = 8;
					} else {
						fcounter = 4;
					}
				} else {
					// 再検索
					fcounter = 2;
				}
			}
			break;
		case 4:
		case 5:
		case 6:
		case 7:
			// リロード
			if (owner.attackTime <= 0) {
				if (owner.maidInventory.addItemStackToInventory(new ItemStack(Item.snowball))) {
					owner.playSound("random.pop");
					if (owner.getPlayingRole() == mpr_StockShooter) {
						owner.setSwing(5, LMM_EnumSound.collect_snow);
						fcounter = 0;
					} else {
						owner.setSwing(30, LMM_EnumSound.collect_snow);
						fcounter++;
					}
				} else {
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				}
			}
//			owner.isMaidChaseWait = true;
			owner.isJumping = false;
			owner.getNavigator().clearPathEntity();
			owner.getLookHelper().setLookPosition(
					MathHelper.floor_double(owner.posX), 
					MathHelper.floor_double(owner.posY - 1D), 
					MathHelper.floor_double(owner.posZ), 
					30F, 40F);
			owner.setSitting(true);
			break;
		case 8:
			// リロード
//			isMaidChaseWait = true;
			if (owner.attackTime <= 0) {
				if (owner.maidInventory.addItemStackToInventory(new ItemStack(Item.snowball))) {
					owner.setSwing(5, LMM_EnumSound.collect_snow);
					owner.playSound("random.pop");
					fcounter = 0;
				} else {
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				}
			}
//			isMaidChaseWait = true;
			owner.setSneaking(true);
			owner.getLookHelper().setLookPosition(
					MathHelper.floor_double(owner.posX), 
					MathHelper.floor_double(owner.posY - 1D), 
					MathHelper.floor_double(owner.posZ), 
					30F, 40F);
			break;
		}
		
	}


	@Override
	public void updateAITick(int pMode) {
		if (owner.isFreedom()) {
			// 自由行動中の固体は虎視眈々と隙をうかがう。
			if (owner.worldObj.isDaytime()) {
				// 昼間のお遊び
				
				// 雪原判定
				if (!owner.isPlaying()) {
					// TODO:お遊び判定
					int xx = MathHelper.floor_double(owner.posX);
					int yy = MathHelper.floor_double(owner.posY);
					int zz = MathHelper.floor_double(owner.posZ);
					
					// 3x3が雪の平原ならお遊び判定が発生
					boolean f = true;
					for (int z = -1; z < 2; z++) {
						for (int x = -1; x < 2; x++) {
							f &= owner.worldObj.getBlockId(xx + x, yy, zz + z) == Block.snow.blockID;
						}
					}
					int lpr = owner.rand.nextInt(100) - 97;
					lpr = (f && lpr > 0) ? (lpr == 1 ? mpr_QuickShooter : mpr_StockShooter) : 0;
					owner.setPlayingRole(lpr);
					fcounter = 0;
					if (f) {
						// mod_littleMaidMob.Debug(String.format("playRole-%d:%d", entityId, playingRole));
					}
					
				} else if (owner.getPlayingRole() >= 0x8000) {
					// 夜の部終了
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				} else {
					// お遊びの実行をここに書く？
					if (owner.getPlayingRole() == mpr_QuickShooter || 
							owner.getPlayingRole() == mpr_StockShooter) {
						playingSnowWar();
					}
					
				}
				
			} else {
				// 夜のお遊び
				if (!owner.isPlaying()) {
					// 条件判定
					
				} else if (owner.getPlayingRole() < 0x8000) {
					// 昼の部終了
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
					
				} else {
					// お遊びの実行をここに書く？
					
				}
			}
			
			// チェスト判定
			if (owner.getAttackTarget() == null
					&& owner.maidInventory.getFirstEmptyStack() == -1) {
				
			}
		}
	}

	@Override
	public int attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (par1DamageSource.getSourceOfDamage() instanceof EntitySnowball) {
			// お遊び判定用、雪玉かどうか判定
			owner.maidDamegeSound = LMM_EnumSound.hurt_snow;
			if (!owner.isContract() || owner.isFreedom()) {
				owner.setPlayingRole(mpr_QuickShooter);
				owner.setMaidWait(false);
				owner.setMaidWaitCount(0);
				mod_LMM_littleMaidMob.Debug("playingMode Enable.");
			}
		}
		return 0;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Playing :
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(true);
			owner.setBloodsuck(false);
			return true;
		}
		
		return false;
	}

	@Override
	public int getNextEquipItem(int pMode) {
		ItemStack litemstack = null;
		if (owner.getPlayingRole() != 0) {
			for (int li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;
				
				// 雪球
				if (litemstack.getItem() instanceof ItemSnowball) {
					return li;
				}
			}
		}
		return -1;
	}

}
