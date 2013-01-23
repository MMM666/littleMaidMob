package net.minecraft.src;

public class LMM_EntityAIWander extends EntityAIWander implements LMM_IEntityAI {

	protected boolean isEnable;
	
	public LMM_EntityAIWander(EntityCreature par1EntityCreature, float par2) {
		super(par1EntityCreature, par2);
		
		isEnable = false;
	}

	@Override
	public boolean shouldExecute() {
		return isEnable && super.shouldExecute();
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
