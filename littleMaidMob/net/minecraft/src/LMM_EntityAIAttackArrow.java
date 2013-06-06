package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.List;

public class LMM_EntityAIAttackArrow extends EntityAIBase implements LMM_IEntityAI {

	protected boolean fEnable;
	
	protected LMM_EntityLittleMaid fMaid;
	protected LMM_EntityLittleMaidAvatar fAvatar;
	protected LMM_InventoryLittleMaid fInventory;
	protected LMM_SwingStatus swingState;
	protected World worldObj;
	protected EntityLiving fTarget;
	protected int fForget;

	
	public LMM_EntityAIAttackArrow(LMM_EntityLittleMaid pEntityLittleMaid) {
		fMaid = pEntityLittleMaid;
		fAvatar = pEntityLittleMaid.maidAvatar;
		fInventory = pEntityLittleMaid.maidInventory;
		swingState = pEntityLittleMaid.getSwingStatusDominant();
		worldObj = pEntityLittleMaid.worldObj;
		fEnable = false;
		setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute() {
		EntityLiving entityliving = fMaid.getAttackTarget();
		
		if (!fEnable || entityliving == null || entityliving.isDead) {
			fMaid.setAttackTarget(null);
			fMaid.setTarget(null);
			fMaid.getNavigator().clearPathEntity();
			return false;
		} else {
			fTarget = entityliving;
			return true;
		}
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		fMaid.playSound(fMaid.isBloodsuck() ? LMM_EnumSound.findTarget_B : LMM_EnumSound.findTarget_N, false);
	}

	@Override
	public boolean continueExecuting() {
		return shouldExecute() || !fMaid.getNavigator().noPath();
	}

	@Override
	public void resetTask() {
		fTarget = null;
	}

	@Override
	public void updateTask() {
		double lrange = 225D;
		double ldist = fMaid.getDistanceSqToEntity(fTarget);
		boolean lsee = fMaid.getEntitySenses().canSee(fTarget);
	
		// 視界の外に出たら一定時間で飽きる
		if (lsee) {
			fForget = 0;
		} else {
			fForget++;
		}
		
		// 攻撃対象を見る
		fMaid.getLookHelper().setLookPositionWithEntity(fTarget, 30F, 30F);
		
		if (ldist < lrange) {
			// 有効射程内
			double atx = fTarget.posX - fMaid.posX;
			double aty = fTarget.posY - fMaid.posY;
			double atz = fTarget.posZ - fMaid.posZ;
			if (fTarget.isEntityAlive()) {
				ItemStack litemstack = fMaid.getCurrentEquippedItem();
				// 敵とのベクトル
				double atl = atx * atx + aty * aty + atz * atz;
				double il = -1D;
				double milsq = 10D;
				Entity masterEntity = fMaid.getMaidMasterEntity();
				if (masterEntity != null) {
					// 主とのベクトル
					double amx = masterEntity.posX - fMaid.posX;
					double amy = masterEntity.posY - fMaid.posY;//-2D
					double amz = masterEntity.posZ - fMaid.posZ;
					
					// この値が０～１ならターゲットとの間に主がいる
					il = (amx * atx + amy * aty + amz * atz) / atl;
					
					// 射線ベクトルと主との垂直ベクトル
					double mix = (fMaid.posX + il * atx) - masterEntity.posX;
					double miy = (fMaid.posY + il * aty) - masterEntity.posY;// + 2D;
					double miz = (fMaid.posZ + il * atz) - masterEntity.posZ;
					// 射線から主との距離
					milsq = mix * mix + miy * miy + miz * miz;
//					mod_LMM_littleMaidMob.Debug("il:%f, milsq:%f", il, milsq);
				}
				
				
				if (litemstack != null && !(litemstack.getItem() instanceof ItemFood) && !fMaid.weaponReload) {
					int lastentityid = worldObj.loadedEntityList.size();
					int itemcount = litemstack.stackSize;
					fMaid.mstatAimeBow = true;
					fAvatar.getValueVectorFire(atx, aty, atz, atl);
					// ダイヤ、金ヘルムなら味方への誤射を気持ち軽減
					boolean fsh = true;
					int helmid = !fMaid.isMaskedMaid() ? 0 : fInventory.armorInventory[3].getItem().itemID;
					if (helmid == Item.helmetDiamond.itemID || helmid == Item.helmetGold.itemID) {
						// 射線軸の確認
						List list = worldObj.getEntitiesWithinAABBExcludingEntity(fMaid, fMaid.boundingBox.expand(16D, 16D, 16D));
						Vec3 vec3d = Vec3.createVectorHelper(fMaid.posX, fMaid.posY, fMaid.posZ);
						Vec3 vec3d1 = Vec3.createVectorHelper(fTarget.posX, fTarget.posY, fTarget.posZ);
						for(int l = 0; l < list.size(); l++) {
							Entity entity1 = (Entity)list.get(l);
							// 自分や味方以外に中るなら撃つ
							if (entity1 == masterEntity || entity1 == fMaid || entity1 == fTarget || !entity1.canBeCollidedWith() || !fMaid.getIFF(entity1)) { 
								continue;
							}
							float f5 = 0.3F;
							AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f5, f5, f5);
							MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3d, vec3d1);
							if(movingobjectposition1 == null) {
								continue;
							}
							fsh = false;
							mod_LMM_littleMaidMob.Debug("ID:%d-friendly fire to ID:%d.", fMaid.entityId, entity1.entityId);
						}
					}
					fsh &= (milsq > 3D || il < 0D);
					// 横移動
					if (!fsh) {
						// 射撃位置を確保する
						double tpx = fMaid.posX;
						double tpy = fMaid.posY;
						double tpz = fMaid.posZ;
						double tpr = Math.sqrt(atl) * 0.5D;
						if (fMaid.isBloodsuck()) {
							// 左回り
							tpx += (atz / tpr);
							tpz -= (atx / tpr);
						} else {
							// 右回り
							tpx -= (atz / tpr);
							tpz += (atx / tpr);
						}
						fMaid.getNavigator().tryMoveToXYZ(tpx, tpy, tpz, fMaid.getAIMoveSpeed());
					}
					else if (lsee & ldist < 100) {
						fMaid.getNavigator().clearPathEntity();
						mod_LMM_littleMaidMob.Debug("Shooting Range.");

					}

					fsh &= lsee;
//            		mod_littleMaidMob.Debug(String.format("id:%d at:%d", entityId, attackTime));
					if (((fMaid.weaponFullAuto && !fsh) || (fsh && fMaid.getSwingStatusDominant().canAttack())) && fAvatar.isItemTrigger) {
						// シュート
						mod_LMM_littleMaidMob.Debug("id:%d shoot.", fMaid.entityId);
						fAvatar.stopUsingItem();
						fMaid.setSwing(30, LMM_EnumSound.shoot);
					} else {
						// チャージ
						if (litemstack.getMaxItemUseDuration() > 500) {
//                			mod_littleMaidMob.Debug(String.format("non reload.%b", isMaskedMaid));
							// リロード無しの通常兵装
							if (!fAvatar.isUsingItemLittleMaid()) {
								// 構え
								if (!fMaid.weaponFullAuto || fsh) {
									// フルオート兵装の場合は射線確認
									int at = ((helmid == Item.helmetIron.itemID) || (helmid == Item.helmetDiamond.itemID)) ? 26 : 16;
									if (swingState.attackTime < at) {
										fMaid.setSwing(at, LMM_EnumSound.sighting);
										litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
										mod_LMM_littleMaidMob.Debug("id:%d redygun.", fMaid.entityId);
									}
								} else {
									mod_LMM_littleMaidMob.Debug(String.format("ID:%d-friendly fire FullAuto.", fMaid.entityId));
								}
							}
						} 
						else if (litemstack.getMaxItemUseDuration() == 0) {
							// 通常投擲兵装
							if (swingState.canAttack() && !fAvatar.isUsingItem()) {
								if (fsh) {
									litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
									// 意図的にショートスパンで音が鳴るようにしてある
									fMaid.mstatAimeBow = false;
									fMaid.setSwing(10, (litemstack.stackSize == itemcount) ? LMM_EnumSound.shoot_burst : LMM_EnumSound.Null);
									mod_LMM_littleMaidMob.Debug(String.format("id:%d throw weapon.", fMaid.entityId));
								} else {
									mod_LMM_littleMaidMob.Debug(String.format("ID:%d-friendly fire throw weapon.", fMaid.entityId));
								}
							}
						} else {
							// リロード有りの特殊兵装
							if (!fAvatar.isUsingItemLittleMaid()) {
								litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
								mod_LMM_littleMaidMob.Debug(String.format("%d reload.", fMaid.entityId));
							}
							// リロード終了まで強制的に構える
							swingState.attackTime = 5;
						}
					}
//            		maidAvatarEntity.setValueRotation();
					fAvatar.setValueVector();
					// アイテムが亡くなった
					if (litemstack.stackSize <= 0) {
						fMaid.destroyCurrentEquippedItem();
						fMaid.getNextEquipItem();
					} else {
						fInventory.setInventoryCurrentSlotContents(litemstack);
					}
					
					// 発生したEntityをチェックしてmaidAvatarEntityが居ないかを確認
					List<Entity> newentitys = worldObj.loadedEntityList.subList(lastentityid, worldObj.loadedEntityList.size());
					boolean shootingflag = false;
					if (newentitys != null && newentitys.size() > 0) {
						mod_LMM_littleMaidMob.Debug(String.format("new FO entity %d", newentitys.size()));
						for (Entity te : newentitys) {
							if (te.isDead) {
								shootingflag = true;
								continue;
							}
							try {
								// 飛翔体の主を置き換える
								Field fd[] = te.getClass().getDeclaredFields();
//                				mod_littleMaidMob.Debug(String.format("%s, %d", e.getClass().getName(), fd.length));
								for (Field ff : fd) {
									// 変数を検索しAvatarと同じ物を自分と置き換える
									ff.setAccessible(true);
									Object eo = ff.get(te);
									if (eo.equals(fAvatar)) {
										ff.set(te, this);
										mod_LMM_littleMaidMob.Debug("Replace FO Owner.");
									}
								}
							}
							catch (Exception exception) {
							}
						}
					}
					// 既に命中していた場合の処理
					if (shootingflag) {
						for (Object obj : worldObj.loadedEntityList) {
							if (obj instanceof EntityCreature && !(obj instanceof LMM_EntityLittleMaid)) {
								EntityCreature ecr = (EntityCreature)obj;
								if (ecr.entityToAttack == fAvatar) {
									ecr.entityToAttack = fMaid;
								}
							}
						}
					}
				}
			}
		} else {
			// 有効射程外
			if (fMaid.getNavigator().noPath()) {
				fMaid.getNavigator().tryMoveToEntityLiving(fTarget, fMaid.getAIMoveSpeed());
			}
			if (fMaid.getNavigator().noPath()) {
				mod_LMM_littleMaidMob.Debug("id:%d Target renge out.", fMaid.entityId);
				fMaid.setAttackTarget(null);
			}
			if (fMaid.weaponFullAuto && fAvatar.isItemTrigger) {
				fAvatar.stopUsingItem();
			} else {
				fAvatar.clearItemInUse();
			}
			
		}
		
	}

	@Override
	public void setEnable(boolean pFlag) {
		fEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return fEnable;
	}

}
