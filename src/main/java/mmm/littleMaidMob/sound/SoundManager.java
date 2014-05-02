package mmm.littleMaidMob.sound;

import mmm.lib.destroyAll.DestroyAllManager;
import mmm.littleMaidMob.littleMaidMob;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class SoundManager {

	public static DestroyAllManager instance;
	public static String packetChannel = "LMM|SND";
	public static FMLEventChannel serverEventChannel;


	public static void Debug(String pText, Object... pData) {
		// デバッグメッセージ
		if (littleMaidMob.isDebugMessage) {
			System.out.println(String.format("LMM|Sound-" + pText, pData));
		}
	}

	public static void init() {
		if (instance instanceof DestroyAllManager) return;
		
		// ネットワークのハンドラを登録
		instance = new DestroyAllManager();
		serverEventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(packetChannel);
		serverEventChannel.register(instance);
	}

	public void sendSoundPacket() {
		// サーバーからクライアントへ
		
	}

	@SubscribeEvent
	public void receiveSoundPacket(FMLNetworkEvent.ClientCustomPacketEvent pEvent) {
		// クライアントでパケット受信
		if (pEvent.packet.channel().contentEquals(packetChannel)) {
			Debug("get playSOund Packet from Server.");
			Entity lentity = FMLClientHandler.instance().getWorldClient().getEntityByID(0);
			int lsoundIndex = 0;
			EnumSound lsound = EnumSound.getEnumSound(lsoundIndex);
			float lvolume = 0.0F;
			float lpitch = 0.0F;
			
			PositionedSoundRecord positionedsoundrecord =
					new PositionedSoundRecord(getSoundResource(lentity, lsound), lvolume, lpitch,
							(float)lentity.posX, (float)lentity.posY, (float)lentity.posZ);
			FMLClientHandler.instance().getClient().getSoundHandler().playSound(positionedsoundrecord);
		}
	}


	public void playSound(EntityLittleMaidBase pEntity, EnumSound pSound,
			float pVolume, float pPitch, boolean pCanOverwrite) {
		
	}

	public ResourceLocation getSoundResource(Entity pEntity, EnumSound pSound) {
		return null;
	}

}
