package net.minecraft.src;

import java.util.Map.Entry;

public class LMM_Net {
	
	/*
	 * 動作用定数、8bit目を立てるとEntity要求
	 */
	public static final byte LMN_Server_UpdateSlots		= (byte)0x80;
	public static final byte LMN_Server_SetTexture		= (byte)0x81;
	public static final byte LMN_Client_SwingArm		= (byte)0x81;
	public static final byte LMN_Client_UpdateTexture	= (byte)0x83;
	public static final byte LMN_Server_SetIFFValue		= (byte)0x04;
	public static final byte LMN_Client_SetIFFValue		= (byte)0x04;
	public static final byte LMN_Server_SaveIFF			= (byte)0x05;
	public static final byte LMN_Server_GetIFFValue		= (byte)0x06;
	public static final byte LMN_Server_GetTextureIndex	= (byte)0x07;
	public static final byte LMN_Client_SetTextureIndex	= (byte)0x87;
	public static final byte LMN_Server_GetTextureStr	= (byte)0x08;
	public static final byte LMN_Client_SetTextureStr	= (byte)0x08;
	


	
	
	/*
	 * LMMPacetのフォーマット
	 * (Byte)
	 * 0	: 識別(1byte)
	 * 1 - 4: EntityID(4Byte)場合に寄っては省略 
	 * 5 - 	: Data
	 * 
	 */
			
	
	
	
	/**
	 * 渡されたデータの先頭に自分のEntityIDを付与して全てのクライアントへ送信
	 */
	public static void sendToAllEClient(LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.entityId);
		((WorldServer)pEntity.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(pEntity, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * 渡されたデータの先頭に自分のEntityIDを付与して特定ののクライアントへ送信
	 */
	public static void sendToEClient(NetServerHandler pHandler, LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.entityId);
		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	public static void sendToClient(NetServerHandler pHandler, byte[] pData) {
		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * 渡されたデータの先頭にEntityIDを付与してサーバーへ送信。
	 * 0:Mode, 1-4:EntityID, 5-:Data
	 */
	public static void sendToEServer(LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.entityId);
		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd:send:%2x:%d", pData[0], pEntity.entityId));
	}

	public static void sendToServer(byte[] pData) {
		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd:%2x:NOEntity", pData[0]));
	}

	/**
	 * サーバーへIFFのセーブをリクエスト
	 */
	public static void saveIFF() {
		sendToServer(new byte[] {LMN_Server_SaveIFF});
	}
	
	/**
	 * Entityを返す。
	 */
	public static Entity getEntity(byte[] pData, int pIndex, World pWorld) {
		return pWorld.getEntityByID(MMM_Helper.getInt(pData, pIndex));
	}
	
	/**
	 * littleMaidのEntityを返す。
	 */
	public static LMM_EntityLittleMaid getLittleMaid(byte[] pData, int pIndex, World pWorld) {
		Entity lentity = getEntity(pData, pIndex, pWorld);
		if (lentity instanceof LMM_EntityLittleMaid) {
			return (LMM_EntityLittleMaid)lentity;
		} else {
			return null;
		}
	}

	// 受信パケットの処理
	
	public static void serverCustomPayload(NetServerHandler var1, Packet250CustomPayload var2) {
		// サーバ側の動作
		byte lmode = var2.data[0];
		int leid = 0;
		LMM_EntityLittleMaid lemaid = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(var2.data, 1);
			lemaid = getLittleMaid(var2.data, 1, var1.playerEntity.worldObj);
			if (lemaid == null) return;
		}
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd Srv Call[%2x:%d].", lmode, leid));
		byte[] ldata;
		
		switch (lmode) {
		case LMN_Server_UpdateSlots : 
			// 初回更新とか
			// インベントリの更新
			lemaid.maidInventory.clearChanged();
			for (LMM_SwingStatus lswing : lemaid.mstatSwingStatus) {
				lswing.lastIndex = -1;
			}
			break;
			
		case LMN_Server_SetTexture:
			// テクスチャ番号をクライアントから受け取る
			int lindex = MMM_Helper.getShort(var2.data, 5);
			int larmor = MMM_Helper.getShort(var2.data, 7);
			lemaid.setTextureIndex(lindex, larmor);
			break;
			
		case LMN_Server_SetIFFValue:
			// IFFの設定値を受信
			int lval = var2.data[1];
			String lname = "";
			for (int li = 6; li < var2.data.length; li++) {
				lname += (char)var2.data[li];
			}
			LMM_IFF.setIFFValue(var1.playerEntity.username, lname, lval);
			break;
		case LMN_Server_SaveIFF:
			// IFFファイルの保存
			LMM_IFF.saveIFF(var1.playerEntity.username);
			break;
		case LMN_Server_GetIFFValue:
			// IFFGUI open
			for (Entry<String, Integer> le : LMM_IFF.DefaultIFF.entrySet()) {
				ldata = new byte[le.getKey().length() + 2];
				ldata[0] = LMN_Client_SetIFFValue;
				ldata[1] = (byte)le.getValue().intValue();
				LMM_Net.sendToClient(var1, ldata);
			}
			break;
		
		case LMN_Server_GetTextureIndex:
			// テクスチャ名称のリクエストに対して番号を返す
			/*
			 * 0:ID
			 * 1-4:EntityID
			 * 5:index 要求かけた時の番号
			 * 6-9:colorBits
			 * 10-:Str
			 */
			String ls = MMM_Helper.getStr(var2.data, 10);
			int lc = MMM_Helper.getInt(var2.data, 6);
			int li = MMM_TextureManager.setStringToIndex(ls, lc);
			mod_LMM_littleMaidMob.Debug(String.format("%d : %d : %04x : %s", li, var2.data[5], lc, ls == null ? "NULL" : ls));
			ldata = new byte[] {
					LMN_Client_SetTextureIndex,
					var2.data[1], var2.data[2], var2.data[3], var2.data[4],
					var2.data[5],
					0, 0
			};
			MMM_Helper.setShort(ldata, 6, li);
			sendToClient(var1, ldata);
			break;
		case LMN_Server_GetTextureStr:
			// インデックスからテクスチャ名称を返す
			/*
			 * 0:ID
			 * 1-2:index 登録テクスチャ番号
			 */
			int li8 = MMM_Helper.getShort(var2.data, 1);
			String ls8 = MMM_TextureManager.getIndexToString(li8);
			mod_LMM_littleMaidMob.Debug(String.format("%d : %s", li8, ls8 == null ? "NULL" : ls8));
			ldata = new byte[3 + ls8.getBytes().length];
			ldata[0] = LMN_Client_SetTextureStr;
			ldata[1] = var2.data[1];
			ldata[2] = var2.data[2];
			MMM_Helper.setStr(ldata, 3, ls8);
			sendToClient(var1, ldata);
			break;
			
		}
	}

	
}
