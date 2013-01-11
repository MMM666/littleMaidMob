package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class LMM_EntityLittleMaidAvatar extends EntityPlayer {

	public Minecraft mc;
	public LMM_EntityLittleMaid avatar;
	public boolean isItemTrigger;
	public boolean isItemReload;
	private boolean isItemPreReload;
	private double appendX;
	private double appendY;
	private double appendZ;

	
	public LMM_EntityLittleMaidAvatar(World par1World, Minecraft pminecraft, LMM_EntityLittleMaid par2EntityLittleMaid) {
		super(par1World);

		// 初期設定
		avatar = par2EntityLittleMaid;
		mc = pminecraft;
		
		inventory = avatar.maidInventory;
		inventory.player = this;
		username = "";
	}

	@Override
	public float getEyeHeight() {
		return avatar.getEyeHeight();
	}

	@Override
	protected String getLivingSound() {
		return null;
	}

	@Override
	protected String getHurtSound() {
		return null;
	}

	@Override
	protected String getDeathSound() {
		return null;
	}

	@Override
	public void sendChatToPlayer(String var1) {
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return false;
	}


	@Override
	public void onUpdate() {
//		posX = avatar.posX;
		EntityPlayer lep = avatar.getMaidMasterEntity();
		entityId = avatar.entityId;
		if (lep != null) {
			capabilities.isCreativeMode = lep.capabilities.isCreativeMode;
		}
		
		if (isUsingItem()) {
			ItemStack itemstack = inventory.getCurrentItem();
			
			if (itemstack != getItemInUse()) {
				clearItemInUse();
			} else {
				int itemInUseCount = getItemInUseCount();
				if (itemInUseCount <= 25 && itemInUseCount % 4 == 0) {
					updateItemUse(itemstack, 5);
				}
				itemInUseCount--;
				super.clearItemInUse();
				setItemInUse(itemstack, itemInUseCount);
				if (itemInUseCount == 0 && !worldObj.isRemote) {
					onItemUseFinish();
				}
			}
		}
		
		if (xpCooldown > 0) {
			xpCooldown--;
		}
		avatar.experienceValue = experienceTotal;
		
	}

	@Override
	public void triggerAchievement(StatBase par1StatBase) {
		// アチーブメント殺し
	}

	@Override
	public void onItemPickup(Entity entity, int i) {
		// アイテム回収のエフェクト
		if (worldObj.isRemote) {
			// Client
			LMM_Client.onItemPickup(this, entity, i);
		} else {
			super.onItemPickup(entity, i);
		}
	}

	@Override
	public void onCriticalHit(Entity par1Entity) {
		if (worldObj.isRemote) {
			// Client
			LMM_Client.onCriticalHit(this, par1Entity);
		} else {
			((WorldServer)worldObj).getEntityTracker().sendPacketToAllAssociatedPlayers(avatar, new Packet18Animation(par1Entity, 6));
		}
	}

	@Override
	public void onEnchantmentCritical(Entity par1Entity) {
		if (worldObj.isRemote) {
			LMM_Client.onEnchantmentCritical(this, par1Entity);
		} else {
			((WorldServer)worldObj).getEntityTracker().sendPacketToAllAssociatedPlayers(avatar, new Packet18Animation(par1Entity, 7));
		}
	}

	@Override
	public void attackTargetEntityWithCurrentItem(Entity par1Entity) {
		// TODO:
		int ll = 0;
		if (par1Entity instanceof EntityLiving) {
			ll = ((EntityLiving)par1Entity).health;
		}
		super.attackTargetEntityWithCurrentItem(par1Entity);
		if (par1Entity instanceof EntityLiving) {
			((EntityLiving)par1Entity).setRevengeTarget(avatar);
		}
		if (par1Entity instanceof EntityCreature) {
			((EntityCreature)par1Entity).setTarget(avatar);
		}
		if (ll > 0) {
			mod_LMM_littleMaidMob.Debug(String.format("ID:%d Given Damege:%d", avatar.entityId, ll - ((EntityLiving)par1Entity).health));
		}
		
	}
	
	@Override
	protected void alertWolves(EntityLiving par1EntityLiving, boolean par2) {
	}
	
	@Override
	public void destroyCurrentEquippedItem() {
		// アイテムが壊れたので次の装備を選択
		super.destroyCurrentEquippedItem();
		avatar.getNextEquipItem();
	}
	
	@Override
	public void onKillEntity(EntityLiving entityliving) {
	}
	
    @Override
	public void clearItemInUse() {
    	super.clearItemInUse();
    	isItemTrigger = false;
    	isItemReload = isItemPreReload = false;
    }

    @Override
    public void stopUsingItem() {
    	super.stopUsingItem();
    }
    
    @Override
    public void setItemInUse(ItemStack itemstack, int i) {
        super.setItemInUse(itemstack, i);
        isItemTrigger = true;
        isItemReload = isItemPreReload;
    }

    public boolean isUsingItemLittleMaid() {
        return super.isUsingItem() | isItemTrigger;
    }

	public void getValue() {
		// EntityLittleMaidから値をコピー
		setPosition(avatar.posX, avatar.posY, avatar.posZ);
		prevPosX = avatar.prevPosX;
		prevPosY = avatar.prevPosY;
		prevPosZ = avatar.prevPosZ;
		rotationPitch = avatar.rotationPitch;
		rotationYaw = avatar.rotationYaw;
		prevRotationPitch = avatar.prevRotationPitch;
		prevRotationYaw = avatar.prevRotationYaw;
		yOffset = avatar.yOffset;
		renderYawOffset = avatar.renderYawOffset;
		prevRenderYawOffset = avatar.prevRenderYawOffset;
		attackTime = avatar.attackTime;
	}


	public void getValueVector(double atx, double aty, double atz, double atl) {
		// EntityLittleMaidから値をコピー
		double l = MathHelper.sqrt_double(atl);
		appendX = atx / l;
		appendY = aty / l;
		appendZ = atz / l;
		posX = avatar.posX + appendX;
		posY = avatar.posY + appendY;
		posZ = avatar.posZ + appendZ;
		prevPosX = avatar.prevPosX + appendX;
		prevPosY = avatar.prevPosY + appendY;
		prevPosZ = avatar.prevPosZ + appendZ;
		rotationPitch		= avatar.rotationPitch;
		prevRotationPitch	= avatar.prevRotationPitch;
		rotationYaw			= avatar.rotationYaw;
		prevRotationYaw		= avatar.prevRotationYaw;
		renderYawOffset		= avatar.renderYawOffset;
		prevRenderYawOffset	= avatar.prevRenderYawOffset;
		rotationYawHead		= avatar.rotationYawHead;
		prevRotationYawHead	= avatar.prevRotationYawHead;
		yOffset = avatar.yOffset;
		motionX = avatar.motionX;
		motionY = avatar.motionY;
		motionZ = avatar.motionZ;
		isSwingInProgress = avatar.getSwinging();
	}

	/**
	 * 射撃管制用、rotationを頭に合わせる
	 */
	public void getValueVectorFire(double atx, double aty, double atz, double atl) {
		// EntityLittleMaidから値をコピー
		double l = MathHelper.sqrt_double(atl);
		appendX = atx / l;
		appendY = aty / l;
		appendZ = atz / l;
		posX = avatar.posX + appendX;
		posY = avatar.posY + appendY;
		posZ = avatar.posZ + appendZ;
		prevPosX = avatar.prevPosX + appendX;
		prevPosY = avatar.prevPosY + appendY;
		prevPosZ = avatar.prevPosZ + appendZ;
		rotationPitch		= updateDirection(avatar.rotationPitch);
		prevRotationPitch	= updateDirection(avatar.prevRotationPitch);
		rotationYaw			= updateDirection(avatar.rotationYawHead);
		prevRotationYaw		= updateDirection(avatar.prevRotationYawHead);
		renderYawOffset		= updateDirection(avatar.renderYawOffset);
		prevRenderYawOffset	= updateDirection(avatar.prevRenderYawOffset);
		rotationYawHead		= updateDirection(avatar.rotationYawHead);
		prevRotationYawHead	= updateDirection(avatar.prevRotationYawHead);
		yOffset = avatar.yOffset;
		motionX = avatar.motionX;
		motionY = avatar.motionY;
		motionZ = avatar.motionZ;
		isSwingInProgress = avatar.getSwinging();
	}

	protected float updateDirection(float pValue) {
		pValue %= 360F;
		if (pValue < 0) pValue += 360F;
		return pValue;
	}

	
	public void setValue() {
		// EntityLittleMiadへ値をコピー
		avatar.setPosition(posX, posY, posZ);
		avatar.prevPosX = prevPosX;
		avatar.prevPosY = prevPosY;
		avatar.prevPosZ = prevPosZ;
		avatar.rotationPitch = rotationPitch;
		avatar.rotationYaw = rotationYaw;
		avatar.prevRotationPitch = prevRotationPitch;
		avatar.prevRotationYaw = prevRotationYaw;
		avatar.yOffset = yOffset;
		avatar.renderYawOffset = renderYawOffset;
		avatar.prevRenderYawOffset = prevRenderYawOffset;
		avatar.getSwingStatusDominant().attackTime = avatar.attackTime = attackTime;
	}

	public void setValueRotation() {
		// EntityLittleMiadへ値をコピー
		avatar.rotationPitch = rotationPitch;
		avatar.rotationYaw = rotationYaw;
		avatar.prevRotationPitch = prevRotationPitch;
		avatar.prevRotationYaw = prevRotationYaw;
		avatar.renderYawOffset = renderYawOffset;
		avatar.prevRenderYawOffset = prevRenderYawOffset;
		avatar.motionX = motionX;
		avatar.motionY = motionY;
		avatar.motionZ = motionZ;
		if (isSwingInProgress) avatar.setSwinging();
		
	}

	public void setValueVector() {
		// EntityLittleMiadへ値をコピー
		avatar.posX = posX - appendX;
		avatar.posY = posY - appendY;
		avatar.posZ = posZ - appendZ;
		avatar.prevPosX = prevPosX - appendX;
		avatar.prevPosY = prevPosY - appendY;
		avatar.prevPosZ = prevPosZ - appendZ;
		avatar.rotationPitch	 = rotationPitch;
		avatar.prevRotationPitch = prevRotationPitch;
//		avatar.rotationYaw			= rotationYaw;
//		avatar.prevRotationYaw		= prevRotationYaw;
//		avatar.renderYawOffset		= renderYawOffset;
//		avatar.prevRenderYawOffset	= prevRenderYawOffset;
		avatar.motionX = motionX;
		avatar.motionY = motionY;
		avatar.motionZ = motionZ;
		if (isSwingInProgress) avatar.setSwinging();
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return null;
	}


}
