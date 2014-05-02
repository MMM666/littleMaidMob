package mmm.littleMaidMob.sound;



public class SoundListName {

	public SoundListColor value;
	public SoundListColor[] colors = new SoundListColor[16];

	public SoundListName() {
		value = new SoundListColor();
		for (int li = 0; li < colors.length; li++) {
			colors[li] = value;
		}
	}

	public void load() {
	}

}
