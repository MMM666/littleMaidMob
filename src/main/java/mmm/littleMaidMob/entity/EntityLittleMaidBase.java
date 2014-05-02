package mmm.littleMaidMob.entity;

import com.mojang.authlib.GameProfile;

import mmm.lib.multiModel.model.AbstractModelBase;
import mmm.lib.multiModel.model.IModelCaps;
import mmm.lib.multiModel.texture.MultiModelContainer;
import mmm.lib.multiModel.texture.MultiModelManager;
import mmm.littleMaidMob.inventory.InventoryLittleMaid;
import mmm.littleMaidMob.mode.ModeController;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

//public class EntityLittleMaidMob extends EntityCreature implements IAnimals, IEntityOwnable {
public class EntityLittleMaidBase extends EntityTameable {

	public EntityLittleMaidAvatar avatar;
	public InventoryLittleMaid inventry;
	public MultiModelContainer multiModel;
	public int color;
	
	/** 文字しているモードの管理用クラス */
	public ModeController modeController;
	public IModelCaps modelCaps;


	public EntityLittleMaidBase(World par1World) {
		super(par1World);
		this.setSize(0.6F, 0.8F);
		
		if (par1World instanceof WorldServer) {
			avatar = new EntityLittleMaidAvatar((WorldServer)par1World, new GameProfile("10", "maid"));
		}
		inventry = new InventoryLittleMaid(this);
		
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
		setSize(lamb.getWidth(modelCaps), lamb.getWidth(modelCaps));
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
	public EntityLivingBase getOwner() {
		// TODO Auto-generated method stub
		return super.getOwner();
	}

// AI関連

	@Override
	protected boolean isAIEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		// TODO Auto-generated method stub
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
	 * 契約済みか？
	 * @return
	 */
	public boolean isContract() {
		return isTamed();
	}

	/**
	 * 待機状態であるか？
	 * @return
	 */
	public boolean isWait() {
		return false;
	}

}
