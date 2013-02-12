package net.minecraft.src;

public class LMM_EntityAISwimming extends EntityAISwimming {

	protected EntityLiving theEntity;
	
	public LMM_EntityAISwimming(EntityLiving par1EntityLiving) {
		super(par1EntityLiving);
		theEntity = par1EntityLiving;
	}

	@Override
	public boolean shouldExecute() {
		// ‘«‚ª‚Â‚­‚È‚ç‰j‚ª‚È‚¢
		return (theEntity.getNavigator().noPath() ?
				(!theEntity.onGround || theEntity.isInsideOfMaterial(Material.water)) : theEntity.isInWater())
				|| theEntity.handleLavaMovement();
	}

}
