package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;


/**
 * Entityのデータ読み取り用のクラス
 * 別にEntityにインターフェース付けてもOK
 */
public class LMM_EntityCaps implements MMM_IModelCaps {

	private LMM_EntityLittleMaid owner;
	private static Map<String, Integer> caps;

	static {
		caps = new HashMap<String, Integer>();
		caps.put("isBloodsuck", caps_isBloodsuck);
		caps.put("isFreedom", caps_isFreedom);
		caps.put("isTracer", caps_isTracer);
		caps.put("isPlaying", caps_isPlaying);
		caps.put("isLookSuger", caps_isLookSuger);
		caps.put("isBlocking", caps_isBlocking);
		caps.put("isWait", caps_isWait);
		caps.put("isWaitEX", caps_isWaitEX);
		caps.put("isOpenInv", caps_isOpenInv);
		caps.put("isWorking", caps_isWorking);
		caps.put("isWorkingDelay", caps_isWorkingDelay);
		caps.put("isContract", caps_isContract);
		caps.put("isContractEX", caps_isContractEX);
		caps.put("isRemainsC", caps_isRemainsC);
		caps.put("isClock", caps_isClock);
		caps.put("isMasked", caps_isMasked);
		caps.put("isCamouflage", caps_isCamouflage);
		caps.put("isPlanter", caps_isPlanter);
		caps.put("entityIdFactor", caps_entityIdFactor);
		caps.put("height", caps_height);
		caps.put("width", caps_width);
		caps.put("YOffset", caps_YOffset);
//		caps.put("mountedYOffset", caps_mountedYOffset);
		caps.put("dominantArm", caps_dominantArm);
//		caps.put("render", caps_render);
//		caps.put("Arms", caps_Arms);
		caps.put("HeadMount", caps_HeadMount);
//		caps.put("HardPoint", caps_HardPoint);
		caps.put("stabiliser", caps_stabiliser);
		caps.put("Items", caps_Items);
		caps.put("Actions", caps_Actions);
		caps.put("Grounds", caps_Grounds);
		caps.put("Inventory", caps_Inventory);
		caps.put("interestedAngle", caps_interestedAngle);
	}

	public LMM_EntityCaps(LMM_EntityLittleMaid pOwner) {
		owner = pOwner;
	}

	@Override
	public Map<String, Integer> getModelCaps() {
		return caps;
	}

	@Override
	public Object getCapsValue(int pIndex, Object ...pArg) {
		int li = 0;
		
		switch (pIndex) {
		case caps_isBloodsuck:
			return owner.isBloodsuck();
		case caps_isFreedom:
			return owner.isFreedom();
		case caps_isTracer:
			return owner.isTracer();
		case caps_isPlaying:
			return owner.isPlaying();
		case caps_isLookSuger:
			return owner.isLookSuger();
		case caps_isBlocking:
			return owner.isBlocking();
		case caps_isWait:
			return owner.isMaidWait();
		case caps_isWaitEX:
			return owner.isMaidWaitEx();
		case caps_isOpenInv:
			return owner.isOpenInventory();
		case caps_isWorking:
			return owner.isWorking();
		case caps_isWorkingDelay:
			return owner.isWorkingDelay();
		case caps_isContract:
			return owner.isMaidContract();
		case caps_isContractEX:
			return owner.isMaidContractEX();
		case caps_isRemainsC:
			return owner.isRemainsContract();
		case caps_isClock:
			return owner.isClockMaid();
		case caps_isMasked:
			return owner.isMaskedMaid();
		case caps_isCamouflage:
			return owner.isCamouflage();
		case caps_isPlanter:
			return owner.isPlanter();
		case caps_entityIdFactor:
			return owner.entityIdFactor;
		case caps_height:
			return owner.textureModel0 == null ? null : owner.textureModel0.getHeight();
		case caps_width:
			return owner.textureModel0 == null ? null : owner.textureModel0.getWidth();
		case caps_YOffset:
			return owner.textureModel0 == null ? null : owner.textureModel0.getyOffset();
		case caps_dominantArm:
			return owner.maidDominantArm;
//		case caps_mountedYOffset:
//			return owner.textureModel0 == null ? null : owner.textureModel0.getHeight();
//		case caps_render:
//		case caps_Arms:
		case caps_HeadMount:
			return owner.maidInventory.armorInventory[3];
//		case caps_HardPoint:
		case caps_stabiliser:
			return owner.maidStabilizer;
		case caps_Items:
			ItemStack[] lstacks = new ItemStack[owner.mstatSwingStatus.length];
			for (LMM_SwingStatus ls : owner.mstatSwingStatus) {
				lstacks[li++] = ls.getItemStack(owner);
			}
			return lstacks;
		case caps_Actions:
			EnumAction[] lactions = new EnumAction[owner.mstatSwingStatus.length];
			for (LMM_SwingStatus ls : owner.mstatSwingStatus) {
				lactions[li++] = ls.isUsingItem() ? ls.getItemStack(owner).getItemUseAction() : null;
			}
			return lactions;
		case caps_Grounds:
			float[] lgrounds = new float[owner.mstatSwingStatus.length];
			for (LMM_SwingStatus ls : owner.mstatSwingStatus) {
				lgrounds[li++] = ls.onGround;
			}
			return lgrounds;
		case caps_Inventory:
			return owner.maidInventory;
		case caps_interestedAngle:
			return owner.getInterestedAngle((Float)pArg[0]);
		}
		
		return null;
	}

	@Override
	public Object getCapsValue(String pCapsName, Object ...pArg) {
		return getCapsValue(caps.get(pCapsName), pArg);
	}

	@Override
	public int getCapsValueInt(int pIndex, Object ...pArg) {
		Integer li = (Integer)getCapsValue(pIndex, pArg);
		return li == null ? 0 : li;
	}

	@Override
	public float getCapsValueFloat(int pIndex, Object ...pArg) {
		Float lf = (Float)getCapsValue(pIndex, pArg);
		return lf == null ? 0F : lf;
	}

	@Override
	public double getCapsValueDouble(int pIndex, Object ...pArg) {
		Double ld = (Double)getCapsValue(pIndex, pArg);
		return ld == null ? 0D : ld;
	}

	@Override
	public boolean getCapsValueBoolean(int pIndex, Object ...pArg) {
		Boolean lb = (Boolean)getCapsValue(pIndex, pArg);
		return lb == null ? false : lb;
	}

	@Override
	public boolean setCapsValue(int pIndex, Object... pArg) {
		return false;
	}

	@Override
	public boolean setCapsValue(String pCapsName, Object... pArg) {
		return setCapsValue(caps.get(pCapsName), pArg);
	}

}
