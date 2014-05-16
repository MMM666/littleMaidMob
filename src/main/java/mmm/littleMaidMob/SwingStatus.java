package mmm.littleMaidMob;

import java.lang.reflect.Method;

import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class SwingStatus {

	/** 使用中のアイテムスロット */
	public int index;
	public int lastIndex;
	/* 腕振り関連変数 */
	public boolean isSwingInProgress;
	public float swingProgress;
	public float prevSwingProgress;
	public int swingProgressInt;
	public float onGround;
	/** クールタイム */
	public int attackTime;
//	public int usingCount;
	public int itemInUseCount;
	protected ItemStack itemInUse;



	public SwingStatus() {
		index = lastIndex = -1;
		isSwingInProgress = false;
		swingProgress = prevSwingProgress = 0.0F;
		onGround = 0F;
		attackTime = 0;
		itemInUseCount = 0;
		itemInUse = null;
	}

	/**
	 * TODO:数値の更新用、onEntityUpdate内で呼ぶ事:いらんか？
	 */
	public void onEntityUpdate(EntityLittleMaidBase pEntity) {
		prevSwingProgress = swingProgress;
	}

	/**
	 * 数値の更新用、onUpdate内で呼ぶ事
	 */
	public void onUpdate(EntityLittleMaidBase pEntity) {
		prevSwingProgress = swingProgress;
		if (attackTime > 0) {
			attackTime--;
		}
		
		// 腕振り
//		int li = pEntity.getSwingSpeedModifier();
//		int li = pEntity.getArmSwingAnimationEnd();
		int li = 0;
		try {
			Method lmethod = ReflectionHelper.findMethod(EntityLivingBase.class, pEntity, new String[] {"getArmSwingAnimationEnd"});
			li = (Integer)lmethod.invoke(pEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isSwingInProgress) {
			swingProgressInt++;
			if(swingProgressInt >= li) {
				swingProgressInt = 0;
				isSwingInProgress = false;
			}
		} else {
			swingProgressInt = 0;
		}
		swingProgress = (float)swingProgressInt / (float)li;
		
		if (isUsingItem()) {
			ItemStack itemstack = pEntity.inventory.getStackInSlot(index);
			Entity lrentity = pEntity.worldObj.isRemote ? null : pEntity;
			
			if (itemstack != itemInUse) {
				clearItemInUse(lrentity);
			} else {
				if (itemInUseCount <= 25 && itemInUseCount % 4 == 0) {
					// 食べかすとか
					updateItemUse(pEntity, 5);
				}
				if (--itemInUseCount <= 0 && lrentity != null) {
					onItemUseFinish(pEntity.avatar);
				}
			}
		}
	}

	/**
	 * 選択中のスロット番号を設定
	 */
	public void setSlotIndex(int pIndex) {
		index = pIndex;
		lastIndex = -2;
	}

	/**
	 * 選択中のインベントリ内アイテムスタックを返す
	 */
	public ItemStack getItemStack(EntityLittleMaidBase pEntity) {
		if (index > -1) {
			return pEntity.inventory.getStackInSlot(index);
		} else {
			return null;
		}
	}

	public boolean canAttack() {
		return attackTime <= 0;
	}



// 腕振り関係


	public float getSwingProgress(float ltime) {
		float lf = swingProgress - prevSwingProgress;
		
		if (lf < 0.0F) {
			++lf;
		}
		
		return onGround = prevSwingProgress + lf * ltime;
	}

	public boolean setSwinging() {
		if (!isSwingInProgress || swingProgressInt < 0) {
			swingProgressInt = -1;
			isSwingInProgress = true;
			return true;
		}
		return false;
	}


	/**
	 * 変更があるかどうかを返し、フラグをクリアする。
	 */
	public boolean checkChanged() {
		boolean lflag = index != lastIndex;
		lastIndex = index;
		return lflag;
	}

// アイテムの使用に関わる関数群

	public ItemStack getItemInUse() {
		return itemInUse;
	}

	public int getItemInUseCount() {
		return itemInUseCount;
	}

	public boolean isUsingItem() {
		return itemInUse != null;
	}

	public int getItemInUseDuration() {
		return isUsingItem() ? itemInUse.getMaxItemUseDuration() - itemInUseCount : 0;
	}

	/**
	 * 
	 * @param pEntity
	 * サーバーの時はEntityを設定する。
	 */
	public void stopUsingItem(Entity pEntity) {
		if (itemInUse != null && pEntity instanceof EntityPlayer) {
			itemInUse.onPlayerStoppedUsing(pEntity.worldObj, (EntityPlayer)pEntity, itemInUseCount);
		}
		
		clearItemInUse(pEntity);
	}

	/**
	 * 
	 * @param pEntity
	 * サーバーの時はEntityを設定する。
	 */
	public void clearItemInUse(Entity pEntity) {
		itemInUse = null;
		itemInUseCount = 0;
		
		if (pEntity != null) {
			pEntity.setEating(false);
		}
	}

	public boolean isBlocking() {
		return isUsingItem() && itemInUse.getItem().getItemUseAction(itemInUse) == EnumAction.block;
	}

	/**
	 * 
	 * @param par1ItemStack
	 * @param par2
	 * @param pEntity
	 * サーバーの時はEntityを設定する。
	 */
	public void setItemInUse(ItemStack par1ItemStack, int par2, Entity pEntity) {
		if (par1ItemStack != itemInUse) {
			itemInUse = par1ItemStack;
			itemInUseCount = par2;
			
			if (pEntity != null) {
				pEntity.setEating(true);
			}
		}
	}

	/**
	 * アイテムの使用中処理
	 * @param pEntity
	 * @param par2
	 */
	protected void updateItemUse(EntityLivingBase pEntity, int par2) {
		// EntityPlayerの丸パクリ
		if (itemInUse.getItemUseAction() == EnumAction.drink) {
			pEntity.playSound("random.drink", 0.5F, pEntity.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}
		
		if (itemInUse.getItemUseAction() == EnumAction.eat) {
			for (int j = 0; j < par2; ++j) {
				Vec3 vec3 = pEntity.worldObj.getWorldVec3Pool().getVecFromPool(
						((double) pEntity.getRNG().nextFloat() - 0.5D) * 0.1D,
						Math.random() * 0.1D + 0.1D, 0.0D);
				vec3.rotateAroundX(-pEntity.rotationPitch * (float) Math.PI / 180.0F);
				vec3.rotateAroundY(-pEntity.rotationYaw * (float) Math.PI / 180.0F);
				Vec3 vec31 = pEntity.worldObj.getWorldVec3Pool().getVecFromPool(
						((double) pEntity.getRNG().nextFloat() - 0.5D) * 0.3D,
						(double) (-pEntity.getRNG().nextFloat()) * 0.6D - 0.3D, 0.6D);
				vec31.rotateAroundX(-pEntity.rotationPitch * (float) Math.PI / 180.0F);
				vec31.rotateAroundY(-pEntity.rotationYaw * (float) Math.PI / 180.0F);
				vec31 = vec31.addVector(pEntity.posX,
						pEntity.posY + (double) pEntity.getEyeHeight(), pEntity.posZ);
				String s = "iconcrack_" + Item.getIdFromItem(itemInUse.getItem());
				
				if (itemInUse.getHasSubtypes()) {
					s = s + "_" + itemInUse.getItemDamage();
				}
				
				pEntity.worldObj.spawnParticle(s,
						vec31.xCoord, vec31.yCoord, vec31.zCoord,
						vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord);
			}
			
			pEntity.playSound(
					"random.eat",
					0.5F + 0.5F * (float) pEntity.getRNG().nextInt(2),
					(pEntity.getRNG().nextFloat() - pEntity.getRNG().nextFloat()) * 0.2F + 1.0F);
		}
		
	}

	protected void onItemUseFinish(EntityPlayer pEntityPlayer) {
		if (this.itemInUse != null) {
			this.updateItemUse(pEntityPlayer, 16);
			int var1 = this.itemInUse.stackSize;
			ItemStack var2 = itemInUse.onFoodEaten(pEntityPlayer.worldObj, pEntityPlayer);
			
			if (var2 != this.itemInUse || var2 != null && var2.stackSize != var1) {
				if (var2.stackSize == 0) {
					pEntityPlayer.inventory.setInventorySlotContents(index, null);
				} else {
					pEntityPlayer.inventory.setInventorySlotContents(index, var2);
				}
			}
			
			clearItemInUse(pEntityPlayer);
		}
	}

}
