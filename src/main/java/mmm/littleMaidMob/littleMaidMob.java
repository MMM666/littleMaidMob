package mmm.littleMaidMob;

import java.io.File;

import mmm.lib.ProxyCommon;
import mmm.lib.multiModel.MultiModelHandler;
import mmm.lib.multiModel.texture.MultiModelData;
import mmm.littleMaidMob.entity.EntityLittleMaidBase;
import mmm.littleMaidMob.gui.GuiHandler;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
		modid	= "littleMaidMob",
		name	= "LittleMaidMob"
		)
public class littleMaidMob {

	@SidedProxy(clientSide = "mmm.littleMaidMob.ProxyClient", serverSide = "mmm.lib.ProxyCommon")
	public static ProxyCommon proxy;
	@Instance("littleMaidMob")
	public static littleMaidMob instance;

	public static boolean isDebugMessage = true;

	public static int spawnWeight = 5;
	public static int spawnMin = 1;
	public static int spawnMax = 3;
	public static String[] spawnBiomes;
	public static boolean canDespawn = false;
	public static boolean addSpawnEggRecipe = false;
	public static boolean isNetherLand = false;

	private static int gueid = 0;


	public static void Debug(String pText, Object... pData) {
		// デバッグメッセージ
		if (isDebugMessage) {
			System.out.println(String.format("littleMaidMob-" + pText, pData));
		}
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent pEvent) {
		// コンフィグの解析・設定
		String ls = "littleMaidMob";
		File configFile = pEvent.getSuggestedConfigurationFile();
		Configuration lconf = new Configuration(configFile);
		lconf.load();
		isDebugMessage	= lconf.get(ls, "isDebugMessage", false).getBoolean(false);
		
		spawnWeight	= lconf.get(ls, "spawnWeight", 5).getInt();
		spawnMin	= lconf.get(ls, "spawnMin", 1).getInt();
		spawnMax	= lconf.get(ls, "spawnMax", 3).getInt();
		spawnBiomes = lconf.get(ls, "spawnBiomes", new String[] {
				"FOREST",
				"PLAINS",
				"MOUNTAIN",
				"HILLS",
				"SWAMP",
				"WATER",
				"DESERT",
				"FROZEN",
				"JUNGLE",
				"WASTELAND",
				"BEACH",
//				"NETHER",
//				"END",
				"MUSHROOM",
				"MAGICAL"
			}).getStringList();
		canDespawn	= lconf.get(ls, "canDespawn", false).getBoolean(false);
		addSpawnEggRecipe	= lconf.get(ls, "addSpawnEggRecipe", false).getBoolean(false);
		lconf.save();
		
		gueid = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID(EntityLittleMaidBase.class, ls, gueid, 0xefffef, 0x9f5f5f);
		EntityRegistry.registerModEntity(EntityLittleMaidBase.class, ls, gueid, this, 80, 3, true);
		
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent pEvent) {
		// レンダラの登録
		proxy.init();
		
		// GUIハンドラ
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		// スポーンエッグのレシピを追加
		if (addSpawnEggRecipe) {
			GameRegistry.addRecipe(new ItemStack(Items.spawn_egg, 1, gueid), new Object[] {
				"scs",
				"sbs",
				" e ",
				Character.valueOf('s'), Items.sugar,
				Character.valueOf('c'), new ItemStack(Items.dye, 1, 3),
				Character.valueOf('b'), Items.slime_ball,
				Character.valueOf('e'), Items.egg,
			});
		}
		
		MultiModelHandler.init();
		MultiModelHandler.instance.registerEntityClass(EntityLittleMaidBase.class, MultiModelData.class, "default");
	}

	@Mod.EventHandler
	public void loaded(FMLPostInitializationEvent pEvent) {
		
		addSpawns();
	}

	private void addSpawns() {
		// スポーン領域の登録
		if (spawnWeight > 0) {
			BiomeGenBase[] lbiome;
			for (String ls : spawnBiomes) {
				BiomeDictionary.Type ltype = BiomeDictionary.Type.valueOf(ls);
				if (ltype != null) {
					lbiome = BiomeDictionary.getBiomesForType(ltype);
					EntityRegistry.addSpawn(EntityLittleMaidBase.class,
							spawnWeight, spawnMin, spawnMax,
							EnumCreatureType.creature, lbiome);
					Debug("addSpawn:%s", lbiome.toString());
				}
			}
			if (isNetherLand) {
				lbiome = BiomeDictionary.getBiomesForType(BiomeDictionary.Type.NETHER);
				EntityRegistry.addSpawn(EntityLittleMaidBase.class,
						spawnWeight, spawnMin, spawnMax,
						EnumCreatureType.creature, lbiome);
				Debug("addSpawn:%s", lbiome.toString());
			}
		}
	}

}
