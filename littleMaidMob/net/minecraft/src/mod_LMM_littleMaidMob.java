package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class mod_LMM_littleMaidMob extends BaseMod {

	@MLProp(info="Relative spawn weight. The lower the less common. 10=pigs. 0=off")
	public static int spawnWeight = 5;
	@MLProp(info="Maximum spawn count in the World.")
	public static int spawnLimit = 20;
	@MLProp(info="Minimum spawn group count.")
	public static int minGroupSize = 1;
	@MLProp(info="Maximum spawn group count.")
	public static int maxGroupSize = 3;
//    @MLProp(info="trampleCrops")
	public static boolean trampleCrops = false;
	@MLProp(info="It will despawn, if it lets things go. ")
	public static boolean canDespawn = false;
	@MLProp(info="At local, make sure the name of the owner. ")
	public static boolean checkOwnerName = false;
	@MLProp(info="Not to survive the doppelganger. ")
	public static boolean antiDoppelganger = true;
	@MLProp(info="Enable LMM SpawnEgg Recipe. ")
	public static boolean enableSpawnEgg = false;
	
	
//    @MLProp(info="Living Voice Rate. 1.0=100%, 0.5=50%, 0.0=0%", max=1.0F, min=0.0F)
//    public static float LivingVoiceRate = 1.0F;
	@MLProp(info="LittleMaid Voice distortion.")
	public static boolean VoiceDistortion = true;
	
	@MLProp(info="Default selected Texture Packege. Null is Random")
	public static String defaultTexture = "";
	@MLProp(info="Print Debug Massages.")
	public static boolean DebugMessage = true;
	@MLProp(info="Print Death Massages.")
	public static boolean DeathMessage = true;
	@MLProp(info="Spawn Anywhere.")
	public static boolean Dominant = false;
	@MLProp(info="true: AlphaBlend(request power), false: AlphaTest(more fast)")
	public static boolean AlphaBlend = true;
	@MLProp(info="true: Will be hostile, false: Is a pacifist")
	public static boolean Aggressive = true;

	@MLProp(info="used Achievement index.(0 = Disable)")
	public static int AchievementID = 222000;

	@MLProp(info="UniqueEntityId(-1 is AutoAssigned.)", max=255)
	public static int UniqueEntityId = -1;
	

	public static Achievement ac_Contract;
	public static int containerID;


	public static void Debug(String s) {
		// デバッグメッセージ
		if (DebugMessage) {
			System.out.println((new StringBuilder()).append("littleMaidMob-").append(s).toString());
		}
	}
	
	@Override
	public String getVersion() {
		return "1.4.7-2";
	}

	@Override
	public String getName() {
		return "littleMaidMob";
	}

	@Override
	public String getPriorities() {
		// MMMLibを要求
		return "required-after:mod_MMM_MMMLib";
	}

	@Override
	public void load() {
		if (UniqueEntityId == -1) {
			if (MMM_Helper.isForge) {
				UniqueEntityId = ModLoader.getUniqueEntityId();
			} else {
				UniqueEntityId = MMM_Helper.getNextEntityID();
			}
			UniqueEntityId = (byte)UniqueEntityId;
			if (UniqueEntityId == -1) {
				Debug("You can't added LittleMaidMob.(OutOfEntityID)");
				return;
			} else {
				Debug("UsingEntityID: " + UniqueEntityId);
			}
		}
		defaultTexture = defaultTexture.trim();
		containerID = 222;
		ModLoader.registerContainerID(this, containerID);
		ModLoader.registerEntityID(LMM_EntityLittleMaid.class, "LittleMaid", UniqueEntityId, 0xefffef, 0x9f5f5f);
//        ModLoader.addEntityTracker(this, LMM_EntityLittleMaid.class, var2, var3, var4, var5);
		ModLoader.addLocalization("entity.LittleMaid.name", "LittleMaid");
		ModLoader.addLocalization("entity.LittleMaid.name", "ja_JP", "リトルメイド");
		if (enableSpawnEgg) {
			// 招喚用レシピを追加
			ModLoader.addRecipe(new ItemStack(Item.monsterPlacer, 1, UniqueEntityId), new Object[] {
				"scs",
				"sbs",
				" e ",
				Character.valueOf('s'), Item.sugar,
				Character.valueOf('c'), new ItemStack(Item.dyePowder, 1, 3),
				Character.valueOf('b'), Item.slimeBall,
				Character.valueOf('e'), Item.egg,
			});
		}
		
		if (MMM_Helper.isClient) {
			// アチ実験用
			if (AchievementID != 0) {
				ac_Contract = new Achievement(AchievementID, "littleMaid", 1, -4, Item.cake, AchievementList.bakeCake).registerAchievement();
//                ModLoader.AddAchievementDesc(ac_Contract, "(21)", "Capture the LittleMaid!");
				ModLoader.addAchievementDesc(ac_Contract, "Enlightenment!", "Capture the LittleMaid!");
				ModLoader.addLocalization("achievement.littleMaid", "ja_JP", "悟り。");
				ModLoader.addLocalization("achievement.littleMaid.desc", "ja_JP", "メイドさんを入手しました。");
			}
			
			// 名称変換テーブル
			ModLoader.addLocalization("littleMaidMob.text.Health", "Health");
			ModLoader.addLocalization("littleMaidMob.text.Health", "ja_JP", "メイド強度");
			ModLoader.addLocalization("littleMaidMob.text.AP", "AP");
			ModLoader.addLocalization("littleMaidMob.text.AP", "ja_JP", "メイド装甲");
			
			// デフォルトモデルの設定
			LMM_Client.init();
		}
		
		// AIリストの追加
		LMM_EntityModeManager.init();
		
		// アイテムスロット更新用のパケット
		ModLoader.registerPacketChannel(this, "LMM|Upd");
		
	}

	@Override
	public void addRenderer(Map map) {
		LMM_Client.addRenderer(map);
	}

	@Override
	public void modsLoaded() {
		if (UniqueEntityId == -1) return;
		// Dominant
		if(spawnWeight > 0) {
			if (Dominant) {
				// あらゆる場所にスポーンする
				try {
					Field afield[] = (net.minecraft.src.BiomeGenBase.class).getDeclaredFields();
					LinkedList<BiomeGenBase> linkedlist = new LinkedList<BiomeGenBase>();
					for(int j = 0; j < afield.length; j++) {
						Class class1 = afield[j].getType();
						if((afield[j].getModifiers() & 8) != 0 && class1.isAssignableFrom(net.minecraft.src.BiomeGenBase.class)) {
							BiomeGenBase biomegenbase = (BiomeGenBase)afield[j].get(null);
							linkedlist.add(biomegenbase);
						}
					}
					BiomeGenBase[] dominateBiomes = (BiomeGenBase[])linkedlist.toArray(new BiomeGenBase[0]);
					
					ModLoader.addSpawn(net.minecraft.src.LMM_EntityLittleMaid.class, spawnWeight, minGroupSize, maxGroupSize, EnumCreatureType.creature, dominateBiomes);
				} catch (Exception exception) {
					Debug("Dominate Exception.");
				}
			} else {
				// 通常スポーン設定
				ModLoader.addSpawn(LMM_EntityLittleMaid.class, spawnWeight, minGroupSize, maxGroupSize, EnumCreatureType.creature);
			}
		}
		
		// モードリストを構築
		LMM_EntityModeManager.loadEntityMode();
		
		if (MMM_Helper.isClient) {
			// 音声の解析
			Debug("SoundDir:".concat((Minecraft.getAppDir("minecraft/resources/mod/sound").toString())));
			// サウンドパック
			LMM_SoundManager.loadDefaultSoundPack();
			LMM_SoundManager.loadSoundPack();
		}
		
		// IFFのロード
		LMM_IFF.loadIFFs();
		
	}

	@Override
	public void serverCustomPayload(NetServerHandler var1, Packet250CustomPayload var2) {
		// サーバ側の動作
		LMM_Net.serverCustomPayload(var1, var2);
	}

	@Override
	public void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2) {
		// クライアント側の特殊パケット受信動作
		LMM_Client.clientCustomPayload(var1, var2);
	}

	@Override
	public GuiContainer getContainerGUI(EntityClientPlayerMP var1, int var2,
			int var3, int var4, int var5) {
		return LMM_Client.getContainerGUI(var1, var2, var3, var4, var5);
	}

}
