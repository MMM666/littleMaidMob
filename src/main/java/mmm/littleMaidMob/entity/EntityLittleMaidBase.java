package mmm.littleMaidMob.entity;

import static mmm.littleMaidMob.Statics.dataWatch_Absoption;
import static mmm.littleMaidMob.Statics.dataWatch_Color;
import static mmm.littleMaidMob.Statics.dataWatch_DominamtArm;
import static mmm.littleMaidMob.Statics.dataWatch_ExpValue;
import static mmm.littleMaidMob.Statics.dataWatch_Flags;
import static mmm.littleMaidMob.Statics.dataWatch_Flags_Freedom;
import static mmm.littleMaidMob.Statics.dataWatch_Flags_Wait;
import static mmm.littleMaidMob.Statics.dataWatch_Flags_remainsContract;
import static mmm.littleMaidMob.Statics.dataWatch_Free;
import static mmm.littleMaidMob.Statics.dataWatch_Gotcha;
import static mmm.littleMaidMob.Statics.dataWatch_ItemUse;
import static mmm.littleMaidMob.Statics.dataWatch_Mode;
import static mmm.littleMaidMob.Statics.dataWatch_Parts;
import static mmm.littleMaidMob.Statics.dataWatch_Texture;

import java.util.UUID;

import mmm.lib.Client;
import mmm.lib.multiModel.MultiModelManager;
import mmm.lib.multiModel.model.AbstractModelBase;
import mmm.lib.multiModel.model.IModelCaps;
import mmm.lib.multiModel.texture.IMultiModelEntity;
import mmm.lib.multiModel.texture.MultiModelData;
import mmm.littleMaidMob.TileContainer;
import mmm.littleMaidMob.littleMaidMob;
import mmm.littleMaidMob.inventory.InventoryLittleMaid;
import mmm.littleMaidMob.mode.ModeController;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//public class EntityLittleMaidMob extends EntityCreature implements IAnimals, IEntityOwnable {
public class EntityLittleMaidBase extends EntityTameable implements IMultiModelEntity {

//	protected static final UUID maidUUID = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob".getBytes());
	protected static final UUID maidUUID = UUID.fromString("e2361272-644a-3028-8416-8536667f0efb");
//	protected static final UUID maidUUIDSneak = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob.sneak".getBytes());
	protected static final UUID maidUUIDSneak = UUID.fromString("5649cf91-29bb-3a0c-8c31-b170a1045560");
	protected static AttributeModifier attCombatSpeed = (new AttributeModifier(maidUUID, "Combat speed boost", 0.07D, 0)).setSaved(false);
	protected static AttributeModifier attAxeAmp = (new AttributeModifier(maidUUID, "Axe Attack boost", 0.5D, 1)).setSaved(false);
	protected static AttributeModifier attSneakingSpeed = (new AttributeModifier(maidUUIDSneak, "Sneking speed ampd", -0.4D, 2)).setSaved(false);
	
	public EntityLittleMaidAvatar avatar;
	public InventoryLittleMaid inventory;
//	public MultiModelContainer multiModel;
//	public int color;
	public MultiModelData multiModel;
	
	/** 契約限界時間 */
	public int maidContractLimit;
	/** 主の識別 */
	public EntityPlayer mstatMasterEntity;
	/** 上司の識別 */
	public EntityLivingBase keeperEntity;
	/** 待機状態 */
	protected boolean maidWait;
	protected int mstatWaitCount;
	/** 動作状態 */
	protected short maidMode;
	/** 待機判定 */
	protected boolean maidFreedom;
	
	/** 文字しているモードの管理用クラス */
	public ModeController modeController;
	public IModelCaps modelCaps;

	/** 処理対象となるブロック群 */
	public TileContainer tiles;


	public EntityLittleMaidBase(World par1World) {
		super(par1World);
		this.setSize(0.6F, 2.8F);
		
		if (par1World instanceof WorldServer) {
			avatar = new EntityLittleMaidAvatar((WorldServer)par1World, new GameProfile("10", "maid"));
		}
		inventory = new InventoryLittleMaid(this);
		
//		multiModel = MultiModelManager.instance.getMultiModel("MMM_SR2");
//		setModel("MMM_Aug");
		
		initMultiModel();
		// TODO 付けないと無限落下する・・・意味解らん
//		setSize(width, height);
//		setScale(1.0F);	// ダメ
//		moveEntity(posX, posY, posZ);	// ダメ
	}


// 初期化関数群

	@Override
	protected void entityInit() {
		super.entityInit();
		/*
		 * DataWatcherはクライアントからサーバーへは値を渡さない、渡せない。
		 */
		
		// 使用中リスト
		// 0:Flags
		// 1:Air
		// 2, 3, 4, 5,
		// 6: HP
		// 7, 8:PotionMap
		// 9: ArrowCount
		// 10: 固有名称
		// 11: 名付判定
		// 12: GrowingAge
		// 16: Tame(4), Sit(1) 
		// 17: ownerName
		
		// maidAvater用EntityPlayer互換変数
		// 17 -> 18
		// 18 : Absoption効果をクライアント側へ転送するのに使う（拡張HP）
		dataWatcher.addObject(dataWatch_Absoption, Float.valueOf(0.0F));
		
		// 独自分
		// 19:maidColor
		dataWatcher.addObject(dataWatch_Color, Byte.valueOf((byte)0));
		// 20:選択テクスチャインデックス
		dataWatcher.addObject(dataWatch_Texture, Integer.valueOf(0));
		// 21:モデルパーツの表示フラグ
		dataWatcher.addObject(dataWatch_Parts, Integer.valueOf(0));
		// 22:状態遷移フラグ群(32Bit)、詳細はStatics参照
		dataWatcher.addObject(dataWatch_Flags, Integer.valueOf(0));
		// 23:GotchaID
		dataWatcher.addObject(dataWatch_Gotcha, Integer.valueOf(0));
		// 24:メイドモード
		dataWatcher.addObject(dataWatch_Mode, Short.valueOf((short)0));
		// 25:利き腕
		dataWatcher.addObject(dataWatch_DominamtArm, Byte.valueOf((byte)0));
		// 26:アイテムの使用判定
		dataWatcher.addObject(dataWatch_ItemUse, Integer.valueOf(0));
		// 27:保持経験値
		dataWatcher.addObject(dataWatch_ExpValue, Integer.valueOf(0));
		
		// TODO:test
		// 31:自由変数、EntityMode等で使用可能な変数。
		dataWatcher.addObject(dataWatch_Free, new Integer(0));
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
		// 別に通常のスポーンでも呼ばれる。
		// 個体値は持たせないのでsuperしない。
//		multiModel = MultiModelManager.instance.getMultiModel("MMM_SR2");
		multiModel.setColor(0x08);
		setModel("MMM_Aug");
//		multiModel.forceChanged(true);
		return par1EntityLivingData;
	}

// 固有値関数群

	@Override
	protected Item getDropItem() {
		// ドロップは砂糖
		return Items.sugar;
	}

	@Override
	public boolean getCanSpawnHere() {
		// TODO Auto-generated method stub
		return super.getCanSpawnHere();
	}

	@Override
	public EntityAgeable createChild(EntityAgeable var1) {
		// TODO Auto-generated method stub
		return null;
	}

// 形態形成場

	public boolean setModel(String pName) {
		multiModel.setModelFromName(pName);
//		AbstractModelBase lamb = multiModel.model.getModelClass(multiModel.getColor())[0];
//		setScale(0.1F);
//		setSize(lamb.getWidth(modelCaps), lamb.getHeight(modelCaps));
//		setScale(1.0F);
//		littleMaidMob.Debug("setSize:%f,  %f - %s", width, height, isClientWorld() ? "server" : "client");
		return MultiModelManager.instance.isMultiModel(pName);
	}

	@Override
	public ItemStack getHeldItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getEquipmentInSlot(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentItemOrArmor(int var1, ItemStack var2) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack[] getLastActiveItems() {
		// 被ダメ時に此処を参照するのでNULL以外を返すこと。
		return new ItemStack[0];
	}

// 契約関係

	@Override
	public String getOwnerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTamed() {
		return isContract();
	}
	/**
	 * 契約済みか？
	 * @return
	 */
	public boolean isContract() {
		return super.isTamed();
	}
	public boolean isContractEX() {
		return isContract() && isRemainsContract();
	}

	@Override
	public void setTamed(boolean par1) {
		setContract(par1);
	}
//	@Override
	public void setContract(boolean flag) {
		super.setTamed(flag);
	}

	/**
	 * 契約期間の残りがあるかを確認
	 */
	protected void updateRemainsContract() {
		boolean lflag = false;
		if (maidContractLimit > 0) {
			maidContractLimit--;
			lflag = true;
		}
		if (getMaidFlags(dataWatch_Flags_remainsContract) != lflag) {
			setMaidFlags(lflag, dataWatch_Flags_remainsContract);
		}
	}
	/**
	 * ストライキに入っていないか判定
	 * @return
	 */
	public boolean isRemainsContract() {
		return getMaidFlags(dataWatch_Flags_remainsContract);
	}

	public float getContractLimitDays() {
		return maidContractLimit > 0 ? ((float)maidContractLimit / 24000F) : -1F;
	}

	public boolean updateMaidContract() {
		// TODO 同一性のチェック
//		boolean lf = isContract();
//		if (textureData.isContract() != lf) {
//			textureData.setContract(lf);
//			return true;
//		}
		return false;
	}

	@Override
	public EntityLivingBase getOwner() {
		return getMaidMasterEntity();
	}
	public String getMaidMaster() {
		return getOwnerName();
	}

	public EntityPlayer getMaidMasterEntity() {
		// 主を獲得
		if (isContract()) {
			EntityPlayer entityplayer = mstatMasterEntity;
			if (mstatMasterEntity == null || mstatMasterEntity.isDead) {
				String lname; 
				// インターナルサーバーならオーナ判定しない、オフライン対策
				if (!Client.isIntegratedServerRunning()) {
					lname = getMaidMaster();
				} else {
					lname = ((EntityPlayer)worldObj.playerEntities.get(0)).getCommandSenderName();
				}
				entityplayer = worldObj.getPlayerEntityByName(lname);
				
				// クリエイティブモードの状態を主とあわせる
				if (entityplayer != null && avatar != null) {
					avatar.capabilities.isCreativeMode = entityplayer.capabilities.isCreativeMode;
				}
			}
			return entityplayer;
		} else {
			return null;
		}
	}

	public boolean isMaidContractOwner(String pname) {
		return pname.equalsIgnoreCase(getMaidMaster());
	}

	public boolean isMaidContractOwner(EntityPlayer pentity) {
		return pentity == getMaidMasterEntity();
		
//		return pentity == mstatMasterEntity;
	}


// AI関連

	@Override
	protected boolean isAIEnabled() {
		// TODO 設定変えること
		return false;
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		if (true) {
			ItemStack lis = par1EntityPlayer.getCurrentEquippedItem();
			if (isContractEX()) {
				if (lis.getItem() == Items.cake) {
					
				} else {
					// インベントリの表示
					displayGUIInventry(par1EntityPlayer);
					return true;
				}
			} else {
				if (lis.getItem() == Items.cake) {
					// 契約
					setOwner("");
					setContract(true);
				}
			}
		}
		
		return super.interact(par1EntityPlayer);
	}

// 状態識別変数郡

	/**
	 * 血に飢えているか？
	 * @return
	 */
	public boolean isBloodsuck() {
		return false;
	}

	/**
	 * 待機状態であるか？
	 * @return
	 */
	public boolean isWait() {
		return false;
	}

// GUI関連

	/**
	 * インベントリの表示
	 * @param pPlayer
	 */
	@SideOnly(Side.CLIENT)
	public void displayGUIInventry(EntityPlayer pPlayer) {
		pPlayer.openGui(littleMaidMob.instance, 0, worldObj, getEntityId(), 0, 0);
//		FMLClientHandler.instance().displayGuiScreen(pPlayer, new GuiLittleMaidInventory(this, pPlayer));
	}

	/**
	 * IFF設定の表示
	 * @param pPlayer
	 */
	@SideOnly(Side.CLIENT)
	public void displayGUIIFF(EntityPlayer pPlayer) {
		pPlayer.openGui(littleMaidMob.instance, 1, worldObj, getEntityId(), 0, 0);
//		FMLClientHandler.instance().displayGuiScreen(pPlayer, new GuiLittleMaidInventory(this, pPlayer));
	}


// イベント関連


	/**
	 * 周囲のプレーヤーにパケットを送信する
	 * @param pRange 射程距離
	 * @param pPacket
	 */
	public void sendToAllNear(double pRange, Packet pPacket) {
		MinecraftServer lms = FMLCommonHandler.instance().getMinecraftServerInstance();
		lms.getConfigurationManager().sendToAllNear(posX, posY, posZ, pRange, dimension, pPacket);
	}

	public void sendToMaster(Packet pPacket) {
		if (mstatMasterEntity instanceof EntityPlayerMP) {
			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacket(pPacket);
		}
	}

// ポーションエフェクト

	@Override
	protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
		super.onNewPotionEffect(par1PotionEffect);
//		if (isContract()) {
			sendToAllNear(64D, new S1DPacketEntityEffect(getEntityId(), par1PotionEffect));
//		}
	}

	@Override
	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
		super.onChangedPotionEffect(par1PotionEffect, par2);
		// なんかエンドレスで再設定されるので更新なし。
//		if (isContract()) {
//			sendToAllNear(64D, new S1DPacketEntityEffect(getEntityId(), par1PotionEffect));
//		}
	}

	@Override
	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
		super.onFinishedPotionEffect(par1PotionEffect);
//		if (isContract()) {
			sendToAllNear(64D, new S1EPacketRemoveEntityEffect(getEntityId(), par1PotionEffect));
//		}
	}

	/**
	 * フラグ群に値をセット。
	 * @param pCheck： 対象値。
	 * @param pFlags： 対象フラグ。
	 */
	public void setMaidFlags(boolean pFlag, int pFlagvalue) {
		int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
		li = pFlag ? (li | pFlagvalue) : (li & ~pFlagvalue);
		dataWatcher.updateObject(dataWatch_Flags, Integer.valueOf(li));
	}

	/**
	 * 指定されたフラグを獲得
	 */
	public boolean getMaidFlags(int pFlagvalue) {
		return (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & pFlagvalue) > 0;
	}

	// メイドの待機設定
	public boolean isMaidWait() {
		return maidWait;
	}

	public boolean isMaidWaitEx() {
		return isMaidWait() | (mstatWaitCount > 0) | isOpenInventory();
	}

	public void setMaidWait(boolean pflag) {
		// 待機常態の設定、 isMaidWait系でtrueを返すならAIが勝手に移動を停止させる。
		maidWait = pflag;
		setMaidFlags(pflag, dataWatch_Flags_Wait);
		
		aiSit.setSitting(pflag);
		maidWait = pflag;
		isJumping = false;
		setAttackTarget(null);
		setRevengeTarget(null);
		setPathToEntity(null);
		getNavigator().clearPathEntity();
		velocityChanged = true;
	}

	public void setMaidWaitCount(int count) {
		mstatWaitCount = count;
	}

	// インベントリの表示関係
	// まさぐれるのは一人だけ
	public boolean isOpenInventory() {
		return inventory.isOpen;
	}

	/**
	 * GUIを開いた時にサーバー側で呼ばれる。
	 */
	public void onGuiOpened() {
	}

	/**
	 * GUIを閉めた時にサーバー側で呼ばれる。
	 */
	public void onGuiClosed() {
		setMaidWaitCount(modeController.activeMode.getWaitDelayTime());
	}

	// 自由行動
	public void setFreedom(boolean pFlag) {
		// AI関連のリセットもここで。
		maidFreedom = pFlag;
//		aiRestrictRain.setEnable(pFlag);
//		aiFreeRain.setEnable(pFlag);
//		aiWander.setEnable(pFlag);
//		aiJumpTo.setEnable(!pFlag);
//		aiAvoidPlayer.setEnable(!pFlag);
//		aiFollow.setEnable(!pFlag);
//		aiTracer.setEnable(false);
//		setAIMoveSpeed(pFlag ? moveSpeed_Nomal : moveSpeed_Max);
//		setMoveForward(0.0F);
		
		if (maidFreedom && isContract()) {
//			func_110171_b(
			setHomeArea(
					MathHelper.floor_double(posX),
					MathHelper.floor_double(posY),
					MathHelper.floor_double(posZ), 16);
		} else {
//			func_110177_bN();
			detachHome();
//			setPlayingRole(0);
		}
		
		setMaidFlags(maidFreedom, dataWatch_Flags_Freedom);
	}

	public boolean isFreedom() {
		return maidFreedom;
	}

	public void setAbsorptionAmount(float par1) {
		if (par1 < 0.0F) {
			par1 = 0.0F;
		}
		
		dataWatcher.updateObject(dataWatch_Absoption, Float.valueOf(par1));
	}

	public float getAbsorptionAmount() {
		return dataWatcher.getWatchableObjectFloat(dataWatch_Absoption);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		// TODO Auto-generated method stub
		super.readEntityFromNBT(par1nbtTagCompound);
		multiModel.setChange();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		// TODO Auto-generated method stub
		super.writeEntityToNBT(par1nbtTagCompound);
	}


// MultiModel関連

	@Override
	public MultiModelData getMultiModel() {
		return multiModel;
	}

	@Override
	public void setMultiModelData(MultiModelData pMultiModelData) {
		multiModel = pMultiModelData;
	}

	@Override
	public void initMultiModel() {
		// 値の初期化
		multiModel.setColor(0x0c);
		setModel("MMM_Aug");
//		multiModel.setModelFromName("MMM_Aug");
		multiModel.forceChanged(false);
	}

	@Override
	public void onMultiModelChange() {
		// TODO Auto-generated method stub
		
	}

}
