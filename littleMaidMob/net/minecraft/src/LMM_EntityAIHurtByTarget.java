package net.minecraft.src;

public class LMM_EntityAIHurtByTarget extends EntityAIHurtByTarget {

	protected LMM_EntityLittleMaid theMaid;
	private boolean field_75303_a;
	private int field_75301_b;
	private int field_75302_c;


	public LMM_EntityAIHurtByTarget(LMM_EntityLittleMaid par1EntityLiving, boolean par2) {
		super(par1EntityLiving, par2);
		
		theMaid = par1EntityLiving;
		field_75303_a = false;
		field_75301_b = 0;
		field_75302_c = 0;
	}

	@Override
	public boolean shouldExecute() {
		if (theMaid.isContract() && !theMaid.isBlocking() && theMaid.mstatMasterEntity != null) {
			// フェンサー系は主に対する攻撃に反応
			EntityLiving lentity = theMaid.mstatMasterEntity.getAITarget();
			if (isSuitableTarget(lentity, false)) {
				theMaid.setRevengeTarget(lentity);
				return true;
			}
		}
		return super.shouldExecute();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
	}

	@Override
	public void updateTask() {
		super.updateTask();
		String s1 = taskOwner.getAITarget() == null ? "Null" : taskOwner.getAITarget().getClass().toString();
		String s2 = taskOwner.getAttackTarget() == null ? "Null" : taskOwner.getAttackTarget().getClass().toString();
//		System.out.println(String.format("ID:%d, target:%s, attack:%s", taskOwner.entityId, s1, s2));
		
		// 殴られた仕返し
		EntityLiving leliving = taskOwner.getAITarget();
		if (leliving != null && leliving != taskOwner.getAttackTarget()) {
			taskOwner.setAttackTarget(null);
			System.out.println(String.format("ID:%d, ChangeTarget.", taskOwner.entityId));
		}
		
	}
	
	@Override
	protected boolean isSuitableTarget(EntityLiving par1EntityLiving, boolean par2) {
		// LMM用にカスタム
		if (par1EntityLiving == null) {
			return false;
		}
		
		if (par1EntityLiving == taskOwner) {
			return false;
		}
		if (par1EntityLiving == theMaid.mstatMasterEntity) {
			return false;
		}
		
		if (!par1EntityLiving.isEntityAlive()) {
			return false;
		}
		
		LMM_EntityModeBase lailm = theMaid.getActiveModeClass(); 
		if (lailm != null && lailm.isSearchEntity()) {
			if (!lailm.checkEntity(theMaid.getMaidModeInt(), par1EntityLiving)) {
				return false;
			}
		} else {
			if (theMaid.getIFF(par1EntityLiving)) {
				return false;
			}
		}
		
		// 基点から一定距離離れている場合も攻撃しない
		if (!taskOwner.isWithinHomeDistance(MathHelper.floor_double(par1EntityLiving.posX), MathHelper.floor_double(par1EntityLiving.posY), MathHelper.floor_double(par1EntityLiving.posZ))) {
			return false;
		}
		
		// ターゲットが見えない
		if (shouldCheckSight && !taskOwner.getEntitySenses().canSee(par1EntityLiving)) {
			return false;
		}
		
		// 攻撃中止判定？
		if (this.field_75303_a) {
			if (--this.field_75302_c <= 0) {
				this.field_75301_b = 0;
			}
			
			if (this.field_75301_b == 0) {
				this.field_75301_b = this.func_75295_a(par1EntityLiving) ? 1 : 2;
			}
			
			if (this.field_75301_b == 2) {
				return false;
			}
		}
		
		return true;
	}

	private boolean func_75295_a(Entity par1EntityLiving) {
		this.field_75302_c = 10 + this.taskOwner.getRNG().nextInt(5);
		PathEntity var2 = taskOwner.getNavigator().getPathToXYZ(par1EntityLiving.posX, par1EntityLiving.posY, par1EntityLiving.posZ);
//		PathEntity var2 = this.taskOwner.getNavigator().getPathToEntityLiving(par1EntityLiving);
		
		if (var2 == null) {
			return false;
		} else {
			PathPoint var3 = var2.getFinalPathPoint();
			
			if (var3 == null) {
				return false;
			} else {
				int var4 = var3.xCoord - MathHelper.floor_double(par1EntityLiving.posX);
				int var5 = var3.zCoord - MathHelper.floor_double(par1EntityLiving.posZ);
				return (double)(var4 * var4 + var5 * var5) <= 2.25D;
			}
		}
	}

}
