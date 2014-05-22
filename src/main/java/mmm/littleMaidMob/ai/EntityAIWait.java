package mmm.littleMaidMob.ai;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.ai.EntityAISit;

public class EntityAIWait extends EntityAISit {

	protected EntityLittleMaidBase maid;


	public EntityAIWait(EntityLittleMaidBase pMaid) {
		super(pMaid);
		
		setMutexBits(5);
		maid = pMaid;
	}

	@Override
	public boolean shouldExecute() {
		return maid.isMaidWaitEx() || (!maid.isFreedom() && maid.mstatMasterEntity == null);
	}

}
