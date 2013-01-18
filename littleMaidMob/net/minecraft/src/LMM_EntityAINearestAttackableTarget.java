package net.minecraft.src;

import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LMM_EntityAINearestAttackableTarget extends EntityAINearestAttackableTarget {

	protected LMM_EntityLittleMaid theMaid;
	Entity targetEntity;
	Class targetClass;
	int targetChance;
	private final IEntitySelector field_82643_g;
	private EntityAINearestAttackableTargetSorter theNearestAttackableTargetSorter;

	private boolean field_75303_a;
	private int field_75301_b;
	private int field_75302_c;


	public LMM_EntityAINearestAttackableTarget(LMM_EntityLittleMaid par1EntityLiving, Class par2Class, float par3, int par4, boolean par5) {
		this(par1EntityLiving, par2Class, par3, par4, par5, false);
	}

	public LMM_EntityAINearestAttackableTarget(LMM_EntityLittleMaid par1EntityLiving, Class par2Class, float par3, int par4, boolean par5, boolean par6) {
		this(par1EntityLiving, par2Class, par3, par4, par5, par6, (IEntitySelector)null);
	}

	public LMM_EntityAINearestAttackableTarget(LMM_EntityLittleMaid par1, Class par2, float par3, int par4, boolean par5, boolean par6, IEntitySelector par7IEntitySelector) {
		super(par1, par2, par3, par4, par5, par6, par7IEntitySelector);
		targetClass = par2;
		targetDistance = par3;
		targetChance = par4;
		theNearestAttackableTargetSorter = new EntityAINearestAttackableTargetSorter(this, par1);
		field_82643_g = par7IEntitySelector;
		field_75301_b = 0;
		field_75302_c = 0;
		field_75303_a = par6;
		theMaid = par1;
		
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
			return false;
		} else {
			List var5 = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand((double)this.targetDistance, 4.0D, (double)this.targetDistance));
			if (theMaid.mstatMasterEntity != null && !theMaid.isBloodsuck()) {
				// ソーターを主中心へ
				Collections.sort(var5, new EntityAINearestAttackableTargetSorter(this, theMaid.mstatMasterEntity));
			} else {
				// 自分中心にソート
				Collections.sort(var5, this.theNearestAttackableTargetSorter);
			}
			Iterator var2 = var5.iterator();
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
		if (targetEntity instanceof EntityLiving) {
			theMaid.setAttackTarget((EntityLiving)targetEntity);
		} else {
			theMaid.setTarget(targetEntity);
		}
	}

//	@Override
	protected boolean isSuitableTargetLM(Entity par1EntityLiving, boolean par2) {
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
