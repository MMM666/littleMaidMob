package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

/**
 * IFFを管理するためのクラス、ほぼマルチ用。
 */
public class LMM_IFF {

	public static final int iff_Enemy		= 0;
	public static final int iff_Unknown		= 1;
	public static final int iff_Friendry	= 2;

	public static Map<String, Integer> DefaultIFF = new TreeMap<String, Integer>();
	public static Map<String, Map<String, Integer>> UserIFF = new HashMap<String, Map<String,Integer>>();


	
	/**
	 * IFFのゲット
	 */
	public static Map<String, Integer> getUserIFF(String pUsername) {
		if (pUsername == null) {
			return DefaultIFF;
		}
		if (MMM_Helper.mc != null && MMM_Helper.mc.getIntegratedServer() != null) {
			pUsername = "";
		}
		
		if (!UserIFF.containsKey(pUsername)) {
			// IFFがないので作成
			if (pUsername.isEmpty()) {
				UserIFF.put(pUsername, DefaultIFF);
			} else {
				Map<String, Integer> lmap = new HashMap<String, Integer>();
				lmap.putAll(DefaultIFF);
				UserIFF.put(pUsername, lmap);
			}
		}
		// 既にある
		return UserIFF.get(pUsername);
	}

	/**
	 * IFFのゲット
	 */
	/*
	public static Map<String, Integer> getUserIFF(EntityPlayer pEntity) {
		if (pEntity == null) {
			return DefaultIFF;
		}
		if (mod_LMM_littleMaidMob.mcGame != null && mod_LMM_littleMaidMob.mcGame.getIntegratedServer() != null) {
			return getUserIFF("");
		} else {
			return getUserIFF(pEntity.username);
		}
	}

	public static void setIFFValue(EntityPlayer pPlayer, String pName, int pValue) {
		Map<String, Integer> lmap = getUserIFF(pPlayer);
		lmap.put(pName, pValue);
	}
*/
	public static void setIFFValue(String pUsername, String pName, int pValue) {
		Map<String, Integer> lmap = getUserIFF(pUsername);
		lmap.put(pName, pValue);
	}
	
    protected static int checkEntityStatic(String pName, Entity pEntity, int pIndex, Map<String, Entity> pMap) {
    	int liff = LMM_IFF.iff_Unknown;
    	if (pEntity instanceof EntityLiving) {
        	if (pEntity instanceof LMM_EntityLittleMaid) {
        		switch (pIndex) {
        		case 0:
        			// 野生種
            		liff = LMM_IFF.iff_Unknown;
            		break;
        		case 1:
        			// 自分の契約者
            		pName = (new StringBuilder()).append(pName).append(":Contract").toString();
            		((LMM_EntityLittleMaid)pEntity).setMaidContract(true);
            		liff = LMM_IFF.iff_Friendry;
            		break;
        		case 2:
        			// 他人の契約者
            		pName = (new StringBuilder()).append(pName).append(":Others").toString();
            		((LMM_EntityLittleMaid)pEntity).setMaidContract(true);
            		liff = LMM_IFF.iff_Friendry;
            		break;
        		}
        	} else if (pEntity instanceof EntityTameable) {
        		switch (pIndex) {
        		case 0:
        			// 野生種
        			break;
        		case 1:
        			// 自分の家畜
            		pName = (new StringBuilder()).append(pName).append(":Taim").toString();
            		((EntityTameable)pEntity).setTamed(true);
            		liff = LMM_IFF.iff_Friendry;
            		break;
        		case 2:
        			// 他人の家畜
            		pName = (new StringBuilder()).append(pName).append(":Others").toString();
            		((EntityTameable)pEntity).setTamed(true);
            		liff = LMM_IFF.iff_Unknown;
            		break;
        		}
        		if (pIndex != 0) {
            		if (pEntity instanceof EntityOcelot) {
                        ((EntityOcelot)pEntity).setTameSkin(1 + pEntity.worldObj.rand.nextInt(3));
            		}
        		}
        	}
        	if (pMap != null) {
            	// 表示用Entityの追加
        		pMap.put(pName, (EntityLiving)pEntity);
        		mod_LMM_littleMaidMob.Debug(pName + "added.");
        	}
        	
    		// IFFの初期値
    		if (!DefaultIFF.containsKey(pName)) {
    			if (pEntity instanceof IMob) {
    				liff = LMM_IFF.iff_Enemy;
    			}
				DefaultIFF.put(pName, liff);
    		}
    	}
    	
    	return liff;
    }
    
    /**
     * 敵味方識別判定 
     */
    public static int getIFF(String pUsername, String entityname) {
    	if (entityname == null) {
    		return mod_LMM_littleMaidMob.Aggressive ?  iff_Enemy : iff_Friendry;
    	}
		int t = iff_Enemy;
		Map<String, Integer> lmap = getUserIFF(pUsername);
    	if (lmap.containsKey(entityname)) {
    		t = lmap.get(entityname);
    	}
    	return t;
    }

    /**
     * 敵味方識別判定 
     */
    public static int getIFF(String pUsername, Entity entity) {
    	if (entity == null || !(entity instanceof EntityLiving)) {
    		return mod_LMM_littleMaidMob.Aggressive ?  iff_Enemy : iff_Friendry;
    	}
    	String lename = EntityList.getEntityString(entity);
    	String lcname = lename;
    	if (lename == null) {
    		// 名称未定義MOB、プレーヤーとか？
    		return iff_Friendry;
//    		return mod_LMM_littleMaidMob.Aggressive ?  iff_Unknown : iff_Friendry;
    	}
    	int li = 0;
    	if (entity instanceof LMM_EntityLittleMaid) {
    		if (((LMM_EntityLittleMaid)entity).isMaidContract()) {
        		if (((LMM_EntityLittleMaid)entity).getMaidMaster().contentEquals(pUsername)) {
    				// 自分の
        			lcname = (new StringBuilder()).append(lename).append(":Contract").toString();
        			li = 1;
    			} else {
    				// 他人の
        			lcname = (new StringBuilder()).append(lename).append(":Others").toString();
        			li = 2;
    			}
    		}
    	} else if (entity instanceof EntityTameable) {
    		if (((EntityTameable)entity).isTamed()) {
        		if (((EntityTameable)entity).getOwnerName().contentEquals(pUsername)) {
        			// 自分の
        			lcname = (new StringBuilder()).append(lename).append(":Taim").toString();
        			li = 1;
        		} else {
        			// 他人の
        			lcname = (new StringBuilder()).append(lename).append(":Others").toString();
        			li = 2;
        		}
    		}
    	}
    	if (!getUserIFF(pUsername).containsKey(lcname)) {
    		checkEntityStatic(lename, entity, li, null);
    	}
    	return getIFF(pUsername, lcname);
    }

	public static void loadIFFs() {
		// サーバー側の
		if (!MMM_Helper.isClient) {
			// サーバー側処理
			loadIFF("");
			File lfile = MinecraftServer.getServer().getFile("");
			for (File lf : lfile.listFiles()) {
				if (lf.getName().endsWith("littleMaidMob.iff")) {
					String ls = lf.getName().substring(17, lf.getName().length() - 20);
					mod_LMM_littleMaidMob.Debug(ls);
					loadIFF(ls);
				}
			}
		} else {
			// クライアント側
			loadIFF(null);
		}
	}

	protected static File getFile(String pUsername) {
		File lfile;
		if (pUsername == null) {
			lfile = new File(MMM_Helper.mc.getMinecraftDir(), "config/littleMaidMob.iff");
		} else {
			String lfilename;
			if (pUsername.isEmpty()) {
				lfilename = "config/littleMaidMob.iff";
			} else {
				lfilename = "config/".concat(pUsername).concat("_littleMaidMob.iff");
			}
			lfile = MinecraftServer.getServer().getFile(lfilename);
		}
		mod_LMM_littleMaidMob.Debug(lfile.getAbsolutePath());
		return lfile;
	}

    public static void loadIFF(String pUsername) {
		// IFF ファイルの読込み
    	// 動作はサーバー側で想定
    	File lfile = getFile(pUsername);
		if(!(lfile.exists() && lfile.canRead())) {
        	return;
        }
    	Map<String, Integer> lmap = getUserIFF(pUsername); 
		
        try {
            FileReader fr = new FileReader(lfile);
            BufferedReader br = new BufferedReader(fr);
            
            String s;
        	while ((s = br.readLine()) != null) {
        		String t[] = s.split("=");
        		if (t.length > 1) {
        			if (t[0].startsWith("triggerWeapon")) {
        		        LMM_GuiTriggerSelect.appendTriggerItem(t[0].substring(13), t[1]);
        				continue;
        			}
/*        			
        			if (t[0].compareTo("exclusionList") == 0) {
        				exclusionList.clear();
        				for (String ls : t[1].split(",")) {
        					exclusionList.add(ls.trim());
        				}
        				continue;
        			}
*/
        			int i = Integer.valueOf(t[1]);
        			if (i > 2) {
        				i = iff_Unknown;
        			}
        			lmap.put(t[0], i);
        		}
        	}
            
            br.close();
            fr.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	public static void saveIFF(String pUsername) {
		// IFF ファイルの書込み
		File lfile = getFile(MMM_Helper.isClient ? null : pUsername);
		Map<String, Integer> lmap = getUserIFF(pUsername); 
		
		try {
			if(!lmap.isEmpty() && (lfile.exists() || lfile.createNewFile()) && lfile.canWrite()) {
				FileWriter fw = new FileWriter(lfile);
				BufferedWriter bw = new BufferedWriter(fw);
				
				// トリガーアイテムのリスト
				for (Entry<String, List<Integer>> le : LMM_GuiTriggerSelect.selector.entrySet()) {
					StringBuilder sb = new StringBuilder();
					sb.append("triggerWeapon").append(le.getKey()).append("=");
					if (!le.getValue().isEmpty()) {
						sb.append(le.getValue().get(0));
						for (int i = 1; i < le.getValue().size(); i++) {
							sb.append(",").append(le.getValue().get(i));
						}
					}
					
					sb.append("\r\n");
					bw.write(sb.toString());
				}
/*
                // 判定除外対象リスト
            	StringBuilder lsb = new StringBuilder();
            	for (String ls : exclusionList) {
            		if (lsb.length() == 0) {
                    	lsb.append("exclusionList=").append(ls);
            		} else {
                		lsb.append(", ").append(ls);
            		}
            	}
            	if (lsb.length() > 0) {
                	bw.write(lsb.append("\r\n").toString());
            	}
*/                
				
				for (Map.Entry<String, Integer> me : lmap.entrySet()) {
					bw.write(String.format("%s=%d\r\n", me.getKey(), me.getValue()));
				}
				
				bw.close();
				fw.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
