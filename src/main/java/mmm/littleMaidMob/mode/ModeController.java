package mmm.littleMaidMob.mode;

import java.util.List;
import java.util.Map;

import mmm.littleMaidMob.littleMaidMob;

/**
 * モード管理用クラス
 *
 */
public class ModeController {

	public Map<String, EntityModeBase> modeList;


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
