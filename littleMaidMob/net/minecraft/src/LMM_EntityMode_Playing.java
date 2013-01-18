package net.minecraft.src;

public class LMM_EntityMode_Playing extends LMM_EntityModeBase {

    public static final int mpr_NULL			= 0;
    public static final int mpr_QuickShooter	= 10;
    public static final int mpr_StockShooter	= 20;
    

	public LMM_EntityMode_Playing(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 900;
	}

	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Playing", "Playing");
//		ModLoader.addLocalization("littleMaidMob.mode.T-Playing", "Playing");
//		ModLoader.addLocalization("littleMaidMob.mode.F-Playing", "Playing");
//		ModLoader.addLocalization("littleMaidMob.mode.D-Playing", "Playing");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		

	}

	
	@Override
	public void updateAITick(int pMode) {
		if (!owner.isPlaying() && owner.isFreedom()) {
			// 自由行動中の固体は虎視眈々と隙をうかがう。
			
			// 雪原判定
			if (owner.getPlayingRole() == 0) {
	    		// TODO:お遊び判定
	    		int xx = MathHelper.floor_double(owner.posX);
	        	int yy = MathHelper.floor_double(owner.posY);
	        	int zz = MathHelper.floor_double(owner.posZ);
	        	
				// 5x5が雪の平原ならお遊び判定が発生
	        	boolean f = true;
	        	for (int z = -1; z < 2; z++)
	        	{
	            	for (int x = -1; x < 2; x++)
	            	{
	        			f &= owner.worldObj.getBlockId(xx + x, yy, zz + z) == Block.snow.blockID;
	            	}
	        	}
	        	int lpr = owner.rand.nextInt(100) - 97;
	        	lpr = (f && lpr > 0) ? lpr * 10 : 0;
	        	owner.setPlayingRole(lpr);
	        	if (f) {
//	        		mod_littleMaidMob.Debug(String.format("playRole-%d:%d", entityId, playingRole));
	        	}

				
				
			}
			
			// チェスト判定
			if (owner.getAttackTarget() == null && owner.maidInventory.getFirstEmptyStack() == -1) {
				
			}
			
			
		}
	}
	
}
