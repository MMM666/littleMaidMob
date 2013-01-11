package net.minecraft.src;

public class LMM_EntityAIBegMove extends EntityAIBase {

	private LMM_EntityLittleMaid theMaid;
	private EntityPlayer thePlayer;
	private float moveSpeed;
	
	public LMM_EntityAIBegMove(LMM_EntityLittleMaid pEntityLittleMaid, float pmoveSpeed) {
		theMaid = pEntityLittleMaid;
		moveSpeed = pmoveSpeed;

		setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {
		return theMaid.isLookSuger();
	}

	@Override
	public void startExecuting() {
		thePlayer = theMaid.aiBeg.getPlayer();
	}
	
	@Override
	public void resetTask() {
		thePlayer = null;
	}
	
	@Override
	public boolean continueExecuting() {
		return shouldExecute();
	}
	
	@Override
	public void updateTask() {
//		mod_LMM_littleMaidMob.Debug(String.format("begrange:%f", theMaid.aiBeg.getDistanceSq()));
		// îáÇ¢äÒÇÍÅI
		if (theMaid.aiBeg.getDistanceSq() < 3.5D) {
			theMaid.getNavigator().clearPathEntity();
		} else {
			theMaid.getNavigator().tryMoveToEntityLiving(thePlayer, moveSpeed);
		}
	}
	
}
