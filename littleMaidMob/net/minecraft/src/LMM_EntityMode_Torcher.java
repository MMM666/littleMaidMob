package net.minecraft.src;

public class LMM_EntityMode_Torcher extends LMM_EntityModeBase {
	
	public static final int mmode_Torcher = 0x0020;


	public LMM_EntityMode_Torcher(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 6200;
	}
	
	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Torcher", "Torcher");
		LMM_GuiTriggerSelect.appendTriggerItem("Torch", "");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Torcher:0x0020
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;

		owner.addMaidMode(ltasks, "Torcher", mmode_Torcher);
	}
	
	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (litemstack.itemID == Block.torchWood.blockID || LMM_GuiTriggerSelect.checkWeapon("Torch", litemstack)) {
				owner.setMaidMode("Torcher");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Torcher :
			owner.setBloodsuck(false);
			return true;
		}
		
		return false;
	}
	
	@Override
	public int getNextEquipItem(int pMode) {
		int li;
		ItemStack litemstack;

		// モードに応じた識別判定、速度優先
		switch (pMode) {
		case mmode_Torcher : 
			for (li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;

				// 松明
				if (litemstack.itemID == Block.torchWood.blockID || LMM_GuiTriggerSelect.checkWeapon("Torch", litemstack)) {
					return li;
				}
			}
			break;
		}

		return -1;
	}
	
	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return pItemStack.itemID == Block.torchWood.blockID;
	}
	
	@Override
	public boolean isSearchBlock() {
		return true;
	}
	
	@Override
	public boolean shouldBlock(int pMode) {
		return !(owner.getCurrentEquippedItem() == null);
	}
	
    protected int getBlockLighting(int i, int j, int k) {
    	World worldObj = owner.worldObj;
    	if (worldObj.getBlockMaterial(i, j - 1, k).isSolid() && worldObj.getBlockId(i, j, k) == 0) {
    		return worldObj.getBlockLightValue(i, j, k);
    	}
    	return 32;
    }
	
    /**
     * do1:当たり判定のチェック
     * do2:常時ブロク判定、透過判定も当たり判定も無視。
     */
    protected boolean canBlockBeSeen(int x, int y, int z, boolean toTop, boolean do1, boolean do2) {
    	// ブロックの可視判定
    	World worldObj = owner.worldObj;
    	Block lblock = Block.blocksList[worldObj.getBlockId(x, y, z)];
        lblock.setBlockBoundsBasedOnState(worldObj, x, y, z);

    	Vec3 vec3d = Vec3.createVectorHelper(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper((double)x + 0.5D, (double)y + ((lblock.maxY + lblock.minY) * (toTop ? 0.9D : 0.5D)), (double)z + 0.5D);
    	MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks_do_do(vec3d, vec3d1, do1, do2);

    	if (movingobjectposition != null && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
        	// 接触ブロックが指定したものならば
        	if (movingobjectposition.blockX == x && 
        		movingobjectposition.blockY == y &&
        		movingobjectposition.blockZ == z) {
        		return true;
        	}
        }
        return false;
    }

	@Override
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		int v = getBlockLighting(px, py, pz);
		if (v < 8 && canBlockBeSeen(px, py - 1, pz, true, true, false)) {
			return owner.getNavigator().tryMoveToXYZ(px, py, pz, owner.getAIMoveSpeed());
		}

		return false;
	}
	
	@Override
	public boolean executeBlock(int pMode, int px, int py, int pz) {
		ItemStack lis = owner.getCurrentEquippedItem();
		if (lis == null) return false;
		
		int li = lis.stackSize;
		// TODO:当たり判定をどうするか
		if (lis.tryPlaceItemIntoWorld(owner.maidAvatar, owner.worldObj, px, py - 1, pz, 1, 0.5F, 1.0F, 0.5F)) {
			owner.setSwing(10, LMM_EnumSound.installation);

			if (owner.maidAvatar.capabilities.isCreativeMode) {
				lis.stackSize = li;
			}
			if (lis.stackSize <= 0) {
				owner.maidInventory.setInventoryCurrentSlotContents(null);
				owner.getNextEquipItem();
			}
		}
		return false;
	}

}
