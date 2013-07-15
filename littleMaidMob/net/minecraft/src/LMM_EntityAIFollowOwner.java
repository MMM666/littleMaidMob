package net.minecraft.src;

public class LMM_EntityAIFollowOwner extends EntityAIBase implements LMM_IEntityAI {

	private LMM_EntityLittleMaid theMaid;
	private Entity theOwner;
	private World theWorld;
	private float moveSpeed;
	private PathNavigate petPathfinder;
	private int field_48310_h;
	protected double maxDist;
	protected double minDist;
	protected double sprintDist;
	protected double toDistance;
	private boolean lastAvoidWater;
	protected boolean isEnable;

	public LMM_EntityAIFollowOwner(LMM_EntityLittleMaid par1EntityLittleMaid,
			float pSpeed, double pMin, double pMax, double pSprintDistSQ) {
		theMaid = par1EntityLittleMaid;
		theWorld = par1EntityLittleMaid.worldObj;
		moveSpeed = pSpeed;
		petPathfinder = par1EntityLittleMaid.getNavigator();
		minDist = pMin;
		maxDist = pMax;
		sprintDist = pSprintDistSQ;
		isEnable = true;
		setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (!isEnable)
			return false;
		
		Entity entityliving = theMaid.getOwner();
		if (entityliving == null) {
			return false;
		}
		
		if (theMaid.isSitting()) {
			return false;
		}
		
		toDistance = theMaid.getDistanceSqToEntity(entityliving);
		if (toDistance < minDist) {
			return false;
		} else {
			theOwner = entityliving;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		toDistance = theMaid.getDistanceSqToEntity(theOwner);
		return !petPathfinder.noPath()
				&& toDistance > maxDist
				&& !theMaid.isSitting();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		field_48310_h = 0;
		lastAvoidWater = petPathfinder.getAvoidsWater();
		petPathfinder.setAvoidsWater(false);
	}

	/**
	 * Resets the task
	 */
	public void resetTask() {
		theMaid.setSprinting(false);
		theOwner = null;
		petPathfinder.clearPathEntity();
		petPathfinder.setAvoidsWater(lastAvoidWater);
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
		theMaid.getLookHelper().setLookPositionWithEntity(theOwner, 10F,
				theMaid.getVerticalFaceSpeed());
		
		if (theMaid.isSitting()) {
			return;
		}
		// 指定距離以上ならダッシュ
		theMaid.setSprinting(toDistance > sprintDist);
		if (--field_48310_h > 0) {
			return;
		}
		
		field_48310_h = 10;
		
		petPathfinder.tryMoveToEntityLiving(theOwner, moveSpeed);
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
