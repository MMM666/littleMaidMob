package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LMM_SoundManager {
	
//	protected static final File sounddir = new File(Minecraft.getMinecraftDir(), "/resources/mod/sound/littleMaidMob");
	protected static File sounddir;

	// soundindex, value
	public static Map<Integer, String> soundsDefault = new HashMap<Integer, String>();
	// soundIndex, texturePack, color, value
	public static Map<Integer, Map<String, Map<Integer, String>>> soundsTexture = new HashMap<Integer, Map<String,Map<Integer,String>>>();
	public static float soundRateDefault;
	public static Map<String, Map<Integer, Float>> soundRateTexture = new HashMap<String,Map<Integer,Float>>();


	public static void setSoundRate(int soundindex, String value, String target) {
		// 文字列を解析して値を設定
		String arg[] = value.split(",");
		String tvalue;
		Map<Integer, Float> mif;
		if (target == null) {
			target = "";
		} else {
			target = target.trim();
		}
		
		for (String s : arg) {
			if (s.indexOf(';') == -1) {
				// テクスチャ指定詞が無い
				s = s.trim();
				float lf = s.isEmpty() ? 1.0F : Float.valueOf(s);
				if (target.isEmpty()) {
					soundRateDefault = lf;
				} else {
					mif = soundRateTexture.get(target);
					if (mif == null) {
						mif = new HashMap<Integer, Float>();
						soundRateTexture.put(target.trim(), mif);
					}
					mif.put(-1, lf);
				}
			} else {
				// テクスチャ指定詞解析
				String ss[] = s.trim().split(";");
				String ls[];
				if (ss.length < 2) continue;
				if (target.isEmpty()) {
					if (ss.length > 2) {
						ss[0] = ss[0].trim();
						ls = new String[] { ss[0].isEmpty() ? ";" : ss[0], ss[1].trim(), ss[2].trim()};
					} else {
						ls = new String[] { ";", ss[0].trim(), ss[1].trim()};
					}
				} else {
					if (ss.length > 2) {
						ls = new String[] { target, ss[1].trim(), ss[2].trim()};
					} else {
						ls = new String[] { target, ss[0].trim(), ss[1].trim()};
					}
				}
					
				int li = ls[1].isEmpty() ? -1 : Integer.valueOf(ls[1]);
				float lf = ls[2].isEmpty() ? 1.0F : Float.valueOf(ls[2]);
				mif = soundRateTexture.get(ls[0]);
				if (mif == null) {
					mif = new HashMap<Integer, Float>();
					soundRateTexture.put(ls[0], mif);
				}
				mif.put(li, lf);
			}
		}
	}

	public static float getSoundRate(String texturename, int colorvalue){
		if (texturename == null || texturename.length() == 0) texturename = ";";
		Map<Integer, Float> mif = soundRateTexture.get(texturename);
		if (mif == null) {
			// 指定詞のものが無ければ無指定のものを検索
			mif = soundRateTexture.get(";");
			if (mif == null) {
				return soundRateDefault;
			}
		}
		Float lf = mif.get(colorvalue);
		if (lf == null) {
			lf = mif.get(-1);
			if (lf == null) {
				return soundRateDefault;
			}
		}
		return lf;
	}

	public static void setSoundValue(int soundindex, String value, String target) {
		// 文字列を解析して値を設定
		String arg[] = value.split(",");
		
		for (String s : arg) {
			String tvalue;
			if (s.indexOf(';') == -1) {
				// テクスチャ指定詞が無い
				if (target == null || target.isEmpty()) {
					tvalue = value;
				} else {
					tvalue = (new StringBuilder()).append(target).append(";-1;").append(value).toString();
				}
			} else {
				// テクスチャ指定詞解析
				String ss[] = s.trim().split(";");
				if (ss.length == 2) {
					tvalue = (new StringBuilder()).append(target).append(";").append(value).toString();
				} else {
					tvalue = value;
				}
			}
			setSoundValue(soundindex, tvalue);
		}
	}

	public static void setSoundValue(int soundindex, String value) {
		// 文字列を解析して値を設定
		String arg[] = value.split(",");
		
		for (String s : arg) {
			if (s.indexOf(';') == -1) {
				// テクスチャ指定詞が無い
				soundsDefault.put(soundindex, s.trim());
			} else {
				// テクスチャ指定詞解析
				Map<String, Map<Integer, String>> msi = soundsTexture.get(soundindex);
				if (msi == null) {
					msi = new HashMap<String, Map<Integer,String>>();
					soundsTexture.put(soundindex, msi);
				}
				String ss[] = s.trim().split(";");
				if (ss.length < 2) continue;
				if (ss[0].length() == 0) ss[0] = ";";
				Map<Integer, String> mst = msi.get(ss[0]);
				if (mst == null) {
					mst = new HashMap<Integer, String>();
					msi.put(ss[0], mst);
				}
				ss[1] = ss[1].trim();
				int i = ss[1].length() == 0 ? -1 : Integer.valueOf(ss[1]);
				if (ss.length < 3) {
					mst.put(i, "");
				} else {
					mst.put(i, ss[2].trim());
				}
			}
		}
	}

	public static String getSoundValue(LMM_EnumSound enumsound, String texturename, int colorvalue){
		if (enumsound == LMM_EnumSound.Null) return null;
		
		Map<String, Map<Integer, String>> msi = soundsTexture.get(enumsound.index);
		if (msi == null) {
			return soundsDefault.get(enumsound.index);
		}
		
		if (texturename == null || texturename.length() == 0) texturename = ";";
		Map<Integer, String> mst = msi.get(texturename);
		if (mst == null) {
			// 指定詞のものが無ければ無指定のものを検索
			mst = msi.get(";");
			if (mst == null) {
				return soundsDefault.get(enumsound.index);
			}
		}
		String s = mst.get(colorvalue);
		if (s == null) {
			s = mst.get(-1);
			if (s == null) {
				return soundsDefault.get(enumsound.index);
			}
		}
		return s;
	}

	public static void rebuildSoundPack() {
		// 特殊文字を値に変換
		// Default
		Map<Integer, String> lmap = new HashMap<Integer, String>();
		lmap.putAll(soundsDefault);
		for (Entry<Integer, String> lt : soundsDefault.entrySet()) {
			int li = lt.getKey();
			if (lt.getValue().equals("^")) {
				String ls = lmap.get(li & -16);
				if (ls != null && (li & 0x0f) != 0 && !ls.equals("^")) {
					lmap.put(li, ls);
//					soundsDefault.put(li, ls);
					mod_LMM_littleMaidMob.Debug(String.format("soundsDefault[%d] = [%d]", li, li & -16));
				} else {
//					soundsDefault.remove(li);
					mod_LMM_littleMaidMob.Debug(String.format("soundsDefault[%d] removed.", li));
				}
			} else {
				lmap.put(li, lt.getValue());
			}
		}
		soundsDefault = lmap;
		
		// Texture
		for (Entry<Integer, Map<String, Map<Integer, String>>> mim : soundsTexture.entrySet()) {
			for (Entry<String, Map<Integer, String>> msm : mim.getValue().entrySet()) {
				
				for (Entry<Integer, String> mis : msm.getValue().entrySet()) {
					if (mis.getValue().equals("^")) {
						boolean lf = false;
						if ((mim.getKey() & 0x0f) != 0) {
							Map<String, Map<Integer, String>> lmsm = soundsTexture.get(mim.getKey() & -16);
							if (lmsm != null) {
								Map<Integer, String> lmis = lmsm.get(msm.getKey());
								if (lmis != null) {
									String ls = lmis.get(mis.getKey());
									if (ls != null && !ls.equals("^")) {
										msm.getValue().put(mis.getKey(), ls);
										lf = true;
										mod_LMM_littleMaidMob.Debug(String.format("soundsTexture[%d, %s, %d] = [%d]", mim.getKey(), msm.getKey(), mis.getKey(), mim.getKey() & -16));
									}
								}
							}
						}
						if (!lf) {
							msm.getValue().remove(mis.getKey());
							mod_LMM_littleMaidMob.Debug(String.format("soundsTexture[%d, %s, %d] removed.", mim.getKey(), msm.getKey(), mis.getKey()));
						}
					}
				}
			}
		}
	}

	public static void decodeSoundPack(File file, boolean isdefault) {
		// サウンドパックを解析して音声を設定
		try {
			List<LMM_EnumSound> list1 = new ArrayList<LMM_EnumSound>();
			list1.addAll(Arrays.asList(LMM_EnumSound.values()));
			list1.remove(LMM_EnumSound.Null);
			BufferedReader breader = new BufferedReader(new FileReader(file));
			boolean loadsoundrate = false;
			String str;
			String packname = file.getName();
			packname = packname.substring(0, packname.lastIndexOf("."));
			while ((str = breader.readLine()) != null) {
				str = str.trim();
				if (str.isEmpty() || str.startsWith("#")) continue;
				int i = str.indexOf('=');
				if (i > -1) {
					String name = str.substring(0, i).trim();
					String value = str.substring(i + 1).trim();
					int index = -1;
					if (name.startsWith("se_")) {
						String ss = name.substring(3);
						try {
							index = LMM_EnumSound.valueOf(ss).index;
							list1.remove(LMM_EnumSound.valueOf(ss));
						}
						catch (Exception exception) {
							mod_LMM_littleMaidMob.Debug(String.format("unknown sound parameter:%s.cfg - %s", packname, ss));
						}
					} else if (name.equals("LivingVoiceRate")) {
						if (isdefault) {
							setSoundRate(index, value, null);
						} else {
							setSoundRate(index, value, packname);
						}
						loadsoundrate = true;
					}
					if (index > -1) {
						if (isdefault) {
							setSoundValue(index, value);
						} else {
							setSoundValue(index, value, packname);
						}
//		    			mod_littleMaidMob.Debug(String.format("%s(%d) = %s", name, index, value));
					}
				}
			}
			breader.close();
			
			// 無かった項目をcfgへ追加
			if (!list1.isEmpty()) {
				BufferedWriter bwriter = new BufferedWriter(new FileWriter(file, true));
				for (int i = 0; i < list1.size(); i++) {
					writeBuffer(bwriter, list1.get(i));
				}
				bwriter.close();
			}
			if (!loadsoundrate) {
				BufferedWriter bwriter = new BufferedWriter(new FileWriter(file, true));
				writeBufferSoundRate(bwriter, 1.0F);
				bwriter.close();
			}
			
		}
		catch (Exception exception) {
			mod_LMM_littleMaidMob.Debug("decodeSound Exception.");
		}
	}

	public static void loadSoundPack() {
//		File sounddir = Minecraft.getAppDir("minecraft/resources/mod/sound/littleMaidMob"); 
//		File sounddir = new File(Minecraft.getMinecraftDir(), "/resources/mod/sound/littleMaidMob");
		if (sounddir.exists() && sounddir.isDirectory()) {
			for (File file : sounddir.listFiles()) {
				if (file.getName().compareToIgnoreCase("littleMaidMob.cfg") == 0) {
					continue;
				}
				if (file.isFile() && file.canRead() && file.getName().endsWith(".cfg")) {
					// 音声定義ファイルと認識
					mod_LMM_littleMaidMob.Debug("Load SoundPack:" + file.getName());
					decodeSoundPack(file, false);
				}
			}
		} else {
			mod_LMM_littleMaidMob.Debug("no Sound Directory.");
		}
		
		rebuildSoundPack();
	}

	public static boolean loadDefaultSoundPack() {
		// getAppDir使うとディレクトリがなければ作成される
//		File sounddir = Minecraft.getAppDir("minecraft/resources/mod/sound/littleMaidMob"); 
		File soundfile = new File(sounddir, "littleMaidMob.cfg"); 
		if (soundfile.exists() && soundfile.isFile()) {
			mod_LMM_littleMaidMob.Debug(soundfile.getName());
			decodeSoundPack(soundfile, true);
			return true;
		} else {
			mod_LMM_littleMaidMob.Debug("no Default Sound cfg.");
			createDefaultSoundPack(soundfile);
			return false;
		}
	}

	public static boolean createDefaultSoundPack(File file1) {
		// サウンドのデフォルト値を設定
		for (LMM_EnumSound eslm : LMM_EnumSound.values()) {
			if (eslm == LMM_EnumSound.Null) continue;
			setSoundValue(eslm.index, eslm.DefaultValue);
		}
		
		// デフォルトサウンドパックを作成
		if (file1.exists()) {
			return false;
		}
		try {
			if (file1.createNewFile()) {
				BufferedWriter bwriter = new BufferedWriter(new FileWriter(file1));
				
				for (LMM_EnumSound eslm : LMM_EnumSound.values()) {
					writeBuffer(bwriter, eslm);
				}
				// LivingVoiceRate
				writeBufferSoundRate(bwriter, 1.0F);
				
				bwriter.close();
				mod_LMM_littleMaidMob.Debug("Success create Default Sound cfg.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected static void writeBuffer(BufferedWriter buffer, LMM_EnumSound enumsound) throws IOException {
		// 渡されたWBufferへ書き込む
		if (enumsound == LMM_EnumSound.Null) return;
		
		buffer.write("# ");
		buffer.write(enumsound.info);
		buffer.newLine();
		
		buffer.write("se_");
		buffer.write(enumsound.name());
		buffer.write("=");
		buffer.write(enumsound.DefaultValue);
		buffer.newLine();
		buffer.newLine();
	}

	protected static void writeBufferSoundRate(BufferedWriter buffer, float prate) throws IOException {
		// 渡されたWBufferへ書き込む
		buffer.write("# Living Voice Rate. 1.0=100%, 0.5=50%, 0.0=0%");
		buffer.newLine();
		buffer.write("LivingVoiceRate=" + prate);
		buffer.newLine();
		buffer.newLine();
	}

}
