package net.minecraft.src;

import java.util.List;

import javax.activation.MailcapCommandMap;

public class LMM_EntityAICollectItem extends EntityAIBase {
	
	protected LMM_EntityLittleMaid theMaid;
	protected float moveSpeed;
	protected EntityItem targetItem;
	protected boolean lastAvoidWater;
	
	
	public LMM_EntityAICollectItem(LMM_EntityLittleMaid pEntityLittleMaid, float pmoveSpeed) {
		theMaid = pEntityLittleMaid;
		moveSpeed = pmoveSpeed;
		setMutexBits(3);
	}


	@Override
	public boolean shouldExecute() {
		if (theMaid.maidInventory.getFirstEmptyStack() > -1) {
			List llist = theMaid.worldObj.getEntitiesWithinAABB(EntityItem.class, theMaid.boundingBox.expand(8F, 2D, 8F));
			if (!llist.isEmpty()) {
				int li = theMaid.rand.nextInt(llist.size());
				EntityItem ei = (EntityItem)llist.get(li);
				EntityPlayer ep = theMaid.mstatMasterEntity != null ? theMaid.mstatMasterEntity : theMaid.worldObj.getClosestPlayerToEntity(theMaid, 16F);
				
				if (!ei.isDead && ei.onGround && ei.delayBeforeCanPickup <= 0 && !ei.isBurning()
						&& canEntityItemBeSeen(ei) && (ep == null ||
						ep.getDistanceSq(
								ei.posX + MathHelper.sin(ep.rotationYaw * 0.01745329252F) * 2.0D,
								ei.posY,
								ei.posZ - MathHelper.cos(ep.rotationYaw * 0.01745329252F) * 2.0D) > 7.5D))
				{
					ItemStack lstack = ei.getEntityItem();
					if ((lstack.itemID != Item.sugar.itemID)) {
						if ((theMaid.maidActiveModeClass == null)) {
							return false;
						}
						if ((!theMaid.maidActiveModeClass.checkItemStack(lstack))) {
							return false;
						}
					}
					theMaid.playSound(LMM_EnumSound.findTarget_I, false);
					targetItem = ei;
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void startExecuting() {
		lastAvoidWater = theMaid.getNavigator().getAvoidsWater();
		theMaid.getNavigator().setAvoidsWater(true);
	}

	@Override
	public boolean continueExecuting() {
		return !targetItem.isDead && (theMaid.maidInventory.getFirstEmptyStack() > -1) && theMaid.getDistanceSqToEntity(targetItem) < 100D;
	}

	@Override
	public void resetTask() {
		targetItem = null;
		theMaid.getNavigator().clearPathEntity();
		theMaid.getNavigator().setAvoidsWater(lastAvoidWater);
	}

	@Override
	public void updateTask() {
		theMaid.getLookHelper().setLookPositionWithEntity(targetItem, 30F, theMaid.getVerticalFaceSpeed());
		
		PathNavigate lnavigater = theMaid.getNavigator();
		if (lnavigater.noPath()) {
			if (targetItem.inWater) {
				lnavigater.setAvoidsWater(false);
			}
			PathEntity lpath = lnavigater.getPathToXYZ(targetItem.posX, targetItem.posY, targetItem.posZ);
			lnavigater.setPath(lpath, moveSpeed);
		}
	}

	public boolean canEntityItemBeSeen(Entity entity) {
		// ƒAƒCƒeƒ€‚Ì‰ÂŽ‹”»’è
		return theMaid.worldObj.rayTraceBlocks(Vec3.createVectorHelper(theMaid.posX, theMaid.posY + (double)theMaid.getEyeHeight(), theMaid.posZ), Vec3.createVectorHelper(entity.posX, entity.posY + ((entity.boundingBox.minY - entity.boundingBox.minY) / 2), entity.posZ)) == null;
	}

}
