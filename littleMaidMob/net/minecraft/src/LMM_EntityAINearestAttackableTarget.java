package net.minecraft.src;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LMM_EntityAINearestAttackableTarget extends EntityAINearestAttackableTarget {

	protected LMM_EntityLittleMaid theMaid;
	protected Entity targetEntity;
	protected Class targetClass;
	protected int targetChance;
	protected LMM_EntityAINearestAttackableTargetSorter theNearestAttackableTargetSorter;

	private boolean fretarget;
	private int fcanAttack;
	private int fretryCounter;

	public LMM_EntityAINearestAttackableTarget(LMM_EntityLittleMaid par1EntityLiving, Class par2Class, int par4, boolean par5) {
		this(par1EntityLiving, par2Class, par4, par5, false);
	}

	public LMM_EntityAINearestAttackableTarget(LMM_EntityLittleMaid par1, Class par2, int par4, boolean par5, boolean par6) {
		super(par1, par2, par4, par5, par6, null);
		targetClass = par2;
		targetChance = par4;
		theNearestAttackableTargetSorter = new LMM_EntityAINearestAttackableTargetSorter(par1);
		fretarget = par6;
		theMaid = par1;
		
		setMutexBits(1);
	}

	
	@Override
	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
//		} else if (theMaid.getAttackTarget() != null) {
//			return true;
		} else {
			double lfollowRange = this.func_111175_f();
			List llist = this.taskOwner.worldObj.getEntitiesWithinAABB(targetClass, taskOwner.boundingBox.expand(lfollowRange, 8.0D, lfollowRange));
			if (theMaid.mstatMasterEntity != null && !theMaid.isBloodsuck()) {
				// ソーターを主中心へ
				theNearestAttackableTargetSorter.setEntity(theMaid.mstatMasterEntity);
			} else {
				// 自分中心にソート
				theNearestAttackableTargetSorter.setEntity(theMaid);
			}
			Collections.sort(llist, theNearestAttackableTargetSorter);
			Iterator var2 = llist.iterator();
			while (var2.hasNext()) {
				Entity var3 = (Entity)var2.next();
				if (var3.isEntityAlive() && this.isSuitableTargetLM(var3, false)) {
					this.targetEntity = var3;
					return true;
				}
			}
			
			return false;
		}
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		if (targetEntity instanceof EntityLivingBase) {
			theMaid.setAttackTarget((EntityLivingBase)targetEntity);
		} else {
			theMaid.setTarget(targetEntity);
		}
		fcanAttack = 0;
		fretryCounter = 0;
	}

//	@Override
	protected boolean isSuitableTargetLM(Entity pTarget, boolean par2) {
		// LMM用にカスタム
		// 非生物も対象のため別クラス
		if (pTarget == null) {
			return false;
		}
		
		if (pTarget == taskOwner) {
			return false;
		}
		if (pTarget == theMaid.mstatMasterEntity) {
			return false;
		}
		
		if (!pTarget.isEntityAlive()) {
			return false;
		}
		
		LMM_EntityModeBase lailm = theMaid.getActiveModeClass(); 
		if (lailm != null && lailm.isSearchEntity()) {
			if (!lailm.checkEntity(theMaid.getMaidModeInt(), pTarget)) {
				return false;
			}
		} else {
			if (theMaid.getIFF(pTarget)) {
				return false;
			}
		}
/*		
		// 基点から一定距離離れている場合も攻撃しない
		if (!taskOwner.func_110176_b(MathHelper.floor_double(pTarget.posX), MathHelper.floor_double(pTarget.posY), MathHelper.floor_double(pTarget.posZ))) {
//		if (!taskOwner.isWithinHomeDistance(MathHelper.floor_double(par1EntityLiving.posX), MathHelper.floor_double(par1EntityLiving.posY), MathHelper.floor_double(par1EntityLiving.posZ))) {
			return false;
		}
*/		
		// ターゲットが見えない
		if (shouldCheckSight && !taskOwner.getEntitySenses().canSee(pTarget)) {
			return false;
		}
		
		// 攻撃中止判定？
		if (this.fretarget) {
			if (--this.fretryCounter <= 0) {
				this.fcanAttack = 0;
			}
			
			if (this.fcanAttack == 0) {
				this.fcanAttack = this.func_75295_a(pTarget) ? 1 : 2;
			}
			
			if (this.fcanAttack == 2) {
				return false;
			}
		}
		
		return true;
	}

	// 最終位置が攻撃の間合いでなければ失敗
	protected boolean func_75295_a(Entity par1EntityLiving) {
		this.fretryCounter = 10 + this.taskOwner.getRNG().nextInt(5);
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
