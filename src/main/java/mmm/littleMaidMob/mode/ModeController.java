package mmm.littleMaidMob.mode;

import java.util.Map;

import mmm.littleMaidMob.littleMaidMob;
import net.minecraft.entity.ai.EntityAIBase;

/**
 * モード管理用クラス
 *
 */
public class ModeController {

	// default AIs
	public EntityAIBase aiSit;
	// AI
	/*
	public EntityAITempt aiTempt;
	public LMM_EntityAIBeg aiBeg;
	public LMM_EntityAIBegMove aiBegMove;
	public EntityAIOpenDoor aiOpenDoor;
	public EntityAIRestrictOpenDoor aiCloseDoor;
	public LMM_EntityAIAvoidPlayer aiAvoidPlayer;
	public LMM_EntityAIFollowOwner aiFollow;
	public LMM_EntityAIAttackOnCollide aiAttack;
	public LMM_EntityAIAttackArrow aiShooting;
	public LMM_EntityAICollectItem aiCollectItem;
	public LMM_EntityAIRestrictRain aiRestrictRain;
	public LMM_EntityAIFleeRain aiFreeRain;
	public LMM_EntityAIWander aiWander;
	public LMM_EntityAIJumpToMaster aiJumpTo;
	public LMM_EntityAIFindBlock aiFindBlock;
	public LMM_EntityAITracerMove aiTracer;
	public EntityAISwimming aiSwiming;
	public EntityAIPanic aiPanic;
*/
	
	public Map<String, EntityModeBase> modeList;
	/** 現在実行中のモード */
	public EntityModeBase activeMode;


	public void addMode(EntityModeBase pMode) {
		if (pMode.getName() != null) {
			modeList.put(pMode.getName(), pMode);
		} else {
			littleMaidMob.Debug("ModeClass is NoName: %s", pMode.toString());
		}
	}

	public void getMode(String pName) {
		
	}

	public void setMode(String pName) {
		
	}

	public void setLastMode() {
		
	}

}
