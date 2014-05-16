package mmm.littleMaidMob.entity;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.handshake.FMLHandshakeMessage.ModList;
import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mmm.lib.Client;
import mmm.lib.multiModel.model.AbstractModelBase;
import mmm.lib.multiModel.model.IModelCaps;
import mmm.lib.multiModel.texture.MultiModelContainer;
import mmm.lib.multiModel.texture.MultiModelManager;
import mmm.littleMaidMob.TileContainer;
import mmm.littleMaidMob.littleMaidMob;
import mmm.littleMaidMob.gui.GuiLittleMaidInventory;
import mmm.littleMaidMob.inventory.InventoryLittleMaid;
import mmm.littleMaidMob.mode.ModeController;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
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
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import static mmm.littleMaidMob.Statics.*;

//public class EntityLittleMaidMob extends EntityCreature implements IAnimals, IEntityOwnable {
public class EntityLittleMaidBase extends EntityTameable {

//	protected static final UUID maidUUID = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob".getBytes());
	protected static final UUID maidUUID = UUID.fromString("e2361272-644a-3028-8416-8536667f0efb");
//	protected static final UUID maidUUIDSneak = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob.sneak".getBytes());
	protected static final UUID maidUUIDSneak = UUID.fromString("5649cf91-29bb-3a0c-8c31-b170a1045560");
	protected static AttributeModifier attCombatSpeed = (new AttributeModifier(maidUUID, "Combat speed boost", 0.07D, 0)).setSaved(false);
	protected static AttributeModifier attAxeAmp = (new AttributeModifier(maidUUID, "Axe Attack boost", 0.5D, 1)).setSaved(false);
	protected static AttributeModifier attSneakingSpeed = (new AttributeModifier(maidUUIDSneak, "Sneking speed ampd", -0.4D, 2)).setSaved(false);
	
	public EntityLittleMaidAvatar avatar;
	public InventoryLittleMaid inventory;
	public MultiModelContainer multiModel;
	public int color;
	public int maidContractLimit;
	/** 主の識別 */
	public EntityPlayer mstatMasterEntity;
	/** 上司の識別 */
	public EntityLivingBase keeperEntity;
	
	/** 文字しているモードの管理用クラス */
	public ModeController modeController;
	public IModelCaps modelCaps;

	/** 処理対象となるブロック群 */
	public TileContainer tiles;


	public EntityLittleMaidBase(World par1World) {
		super(par1World);
		this.setSize(0.6F, 0.8F);
		
		if (par1World instanceof WorldServer) {
			avatar = new EntityLittleMaidAvatar((WorldServer)par1World, new GameProfile("10", "maid"));
		}
		inventory = new InventoryLittleMaid(this);
		
//		multiModel = MultiModelManager.instance.getMultiModel("MMM_SR2");
		color = 0x0c;
		setModel("MMM_Aug");
	}

// 初期化関数群

	@Override
	protected void entityInit() {
		// datawatcherの追加
		super.entityInit();
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
		// 別に通常のスポーンでも呼ばれる。
		// 個体値は持たせないのでsuperしない。
//		multiModel = MultiModelManager.instance.getMultiModel("MMM_SR2");
		setModel("MMM_Aug");
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
		multiModel = MultiModelManager.instance.getMultiModel(pName);
		AbstractModelBase lamb = multiModel.getModelClass(color)[0];
		setSize(lamb.getWidth(modelCaps), lamb.getHeight(modelCaps));
		setScale(1.0F);
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
//		textureData.setContract(flag);
		if (flag) {
//        	maidMode = mmode_Escorter;
		} else {
		}
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
		boolean lf = isContract();
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		if (true) {
			// インベントリの表示
			displayGUIInventry(par1EntityPlayer);
			return true;
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
//		if (isContract()) {
			sendToAllNear(64D, new S1DPacketEntityEffect(getEntityId(), par1PotionEffect));
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



}
