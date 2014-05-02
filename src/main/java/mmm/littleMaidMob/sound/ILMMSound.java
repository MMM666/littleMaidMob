package mmm.littleMaidMob.sound;

import net.minecraft.client.audio.ISound;

public interface ILMMSound {

	public ISound getLastSound();
	public void setLastSound(ISound pSound);
	
	public String getTextureName();
	public int getColor();

}
