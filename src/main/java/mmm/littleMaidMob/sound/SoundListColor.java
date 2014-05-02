package mmm.littleMaidMob.sound;

import java.util.Map;

public class SoundListColor {

	public boolean canUseDefault;
	public Map<EnumSound, SoundEntry> defaultSounds;
	public Map<EnumSound, SoundEntry> sounds;

	public SoundEntry getSound(EnumSound pSound) {
		if (sounds.containsKey(pSound)) {
			return sounds.get(pSound);
		} else if (canUseDefault && defaultSounds.containsKey(pSound)) {
			return defaultSounds.get(pSound);
		}
		return null;
	}

}
