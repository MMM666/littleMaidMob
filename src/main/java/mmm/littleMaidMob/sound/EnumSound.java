package mmm.littleMaidMob.sound;

public enum EnumSound {

	death(0x100, "Deid Voice. Null is no Voice", "mob.ghast.death", null),
	attack(0x110, "Attack Voice. Null is no Voice", "mob.ghast.charge", null),
	attack_bloodsuck(0x111, "Attack Bloodsucker Voice. Null is no Voice", "^", attack),
	laughter(0x120, "Laughter Voice. Null is no Voice", "", null),
	shoot(0x130, "shoot Voice. Null is no Voice", "mob.ghast.charge", attack),
	shoot_burst(0x131, "burst shoot Voice. Null is no Voice", "mob.ghast.charge", attack),
	sighting(0x140, "Adopt a fire Voice. Null is no Voice", "", null),
	healing(0x150, "Healing Voice. Null is no Voice", "", null),
	healing_potion(0x151, "Healing with potion Voice. Null is no Voice", "", healing),
	TNT_D(0x160, "Enable TNT-D Voice. Null is no Voice", "", null),
//	eatGunpowder(0x161, "Eat Gunpowder Voice. Null is no Voice", "^"),

	eatSugar(0x200, "Eat Sugar Voice. Null is no Voice", "", null),
	eatSugar_MaxPower(0x201, "Eat Sugar to MAX healing Voice. Null is no Voice", "", null),
	getCake(0x210, "Get Cake Voice. Null is no Voice", "", null),
	Recontract(0x211, "Recontract Voice. Null is no Voice", "", getCake),
	addFuel(0x220, "Add Fuel Voice. Null is no Voice", "", null),
	cookingStart(0x221, "Cooking Start Voice. Null is no Voice", "", null),
	cookingOver(0x222, "Cooking Over Voice. Null is no Voice", "", null),
	installation(0x230, "Installation Voice. Null is no Voice", "", null),
	collect_snow(0x240, "Collecting snow Voice. Null is no Voice", "", null),

	hurt(0x300, "Dameged Voice. Null is no Voice", "mob.ghast.scream", null),
	hurt_snow(0x301, "Dameged Voice from snowball. Null is no Voice", "", hurt),
	hurt_fire(0x302, "Dameged Voice from fire. Null is no Voice", "", hurt),
	hurt_guard(0x303, "Dameged Voice on Guard. Null is no Voice", "mob.blaze.hit", hurt),
	hurt_fall(0x304, "Dameged Voice from Fall. Null is no Voice", "", hurt),
	hurt_nodamege(0x309, "No Dameged Voice. Null is no Voice", "mob.blaze.hit", hurt),

	findTarget_N(0x400, "Find target Normal Voice. Null is no Voice", "", null),
	findTarget_B(0x401, "Find target Bloodsuck Voice. Null is no Voice", "", findTarget_N),
	findTarget_I(0x402, "Find target Item Voice. Null is no Voice", "", null),
	findTarget_D(0x403, "Find target Darkness Voice. Null is no Voice", "", null),

	living_daytime(0x500, "Living Voice(Default) in Daytime. Null is no Voice", "mob.ghast.moan", null),
	living_morning(0x501, "Living Voice in Mornig. Null is no Voice", "^", living_daytime),
	living_night(0x502, "Living Voice in Night. Null is no Voice", "^", living_daytime),
	living_whine(0x503, "Living Voice at Whine. Null is no Voice", "^", living_daytime),
	living_rain(0x504, "Living Voice at Rain. Null is no Voice", "^", living_daytime),
	living_snow(0x505, "Living Voice at Snow. Null is no Voice", "^", living_daytime),
	living_cold(0x506, "Living Voice at Cold. Null is no Voice", "^", living_daytime),
	living_hot(0x507, "Living Voice at Hot. Null is no Voice", "^", living_daytime),
	goodmorning(0x551, "Goodmorning Voice. Null is no Voice", "mob.wolf.bark", null),
	goodnight(0x561, "Goodnight Voice. Null is no Voice", "mob.ghast.affectionate scream", null),

	Null(0, "", null, null);
	
	
	public final int index;
	public final String info;
	public final String DefaultValue;
	public final EnumSound alterValue;



	private EnumSound(int pIndex, String pInfo, String pDefault, EnumSound pAlter) {
		index = pIndex;
		info = pInfo;
		DefaultValue = pDefault;
		alterValue = pAlter;
	}

	public static EnumSound getEnumSound(int pindex) {
		for (EnumSound le : EnumSound.values()) {
			if (le.index == pindex) {
				return le;
			}
		}
		return Null;
	}


}
