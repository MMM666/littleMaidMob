package net.minecraft.src;

public class LMM_Statics {

	// TODO:定数は要修正
	protected static final float moveSpeed_Nomal		= 0.23F;
	protected static final float moveSpeed_Max		= 0.3F;
	protected static final float moveSpeed_Overdrive	= 1.0F;

	/**
	 * HPをサーバーからクライアントへ通知するのに使う。
	 */
//	protected static final int dataWatch_Health		= 18;
	protected static final int dataWatch_ColorMode	= 19;
	/**
	 * MSB|0x0000 0000|LSB
	 *       |    |本体のテクスチャインデックス
	 *       |アーマーのテクスチャインデックス
	 */
	protected static final int dataWatch_Texture	= 20;
//    protected static final int dataWatch_TexArmar	= 21;
	/**
	 * 各種フラグを一纏めにしたもの。
	 */
	protected static final int dataWatch_Flags		= 22;
	protected static final int dataWatch_Flags_looksWithInterest		= 0x00000001;
	protected static final int dataWatch_Flags_looksWithInterestAXIS	= 0x00000002;
	protected static final int dataWatch_Flags_Aimebow					= 0x00000004;
	protected static final int dataWatch_Flags_Freedom					= 0x00000008;
	protected static final int dataWatch_Flags_Tracer					= 0x00000010;
	protected static final int dataWatch_Flags_remainsContract			= 0x00000020;
	protected static final int dataWatch_Flags_PlayingMode				= 0x00000040;
	protected static final int dataWatch_Flags_Working					= 0x00000080;
	protected static final int dataWatch_Flags_Wait						= 0x00000100;
	protected static final int dataWatch_Flags_WaitEx					= 0x00000200;
	protected static final int dataWatch_Flags_LooksSugar				= 0x00000400;
	protected static final int dataWatch_Flags_Bloodsuck				= 0x00000800;
	protected static final int dataWatch_Flags_OverDrive				= 0x00001000;
	/**
	 * 紐の持ち主のEntityID。
	 */
	protected static final int dataWatch_Gotcha		= 23;
	/**
	 * 自由設定値。
	 */
	protected static final int dataWatch_Free		= 31;
	
	protected static final int dataFlags_ForceUpdateInventory	= 0x80000000;

// NetWork

	/*
	 * 動作用定数、8bit目を立てるとEntity要求
	 */
	
	/*
	 * LMMPacetのフォーマット
	 * (Byte)
	 * 0	: 識別(1byte)
	 * 1 - 4: EntityID(4Byte)場合に寄っては省略 
	 * 5 - 	: Data
	 * 
	 */
	/**
	 * サーバー側へ対象のインベントリを送信するように指示する。
	 * スポーン時点ではインベントリ情報が無いため。
	 * [0]		: 0x00;
	 * [1..4]	: EntityID(int);
	 */
	public static final byte LMN_Server_UpdateSlots		= (byte)0x80;
	/**
	 * クライアント側へ腕振りを指示する。
	 * 振った時の再生音声も指定する。
	 * [0]		: 0x81;
	 * [1..4]	: EntityID(int);
	 * [5]		: ArmIndex(byte);
	 * [6..9]	: SoundIndex(int);
	 */
	public static final byte LMN_Client_SwingArm		= (byte)0x81;
	/**
	 * サーバー側へ染料の使用を通知する。
	 * GUISelect用。
	 * [0]		: 0x02;
	 * [1]		: color(byte);
	 */
	public static final byte LMN_Server_DecDyePowder	= (byte)0x02;
	/**
	 * サーバーへIFFの設定値が変更されたことを通知する。
	 * [0]		: 0x04;
	 * [1]		: IFFValue(byte);
	 * [2..5]	: Index(int);
	 * [6..]	: TargetName(str);
	 */
	public static final byte LMN_Server_SetIFFValue		= (byte)0x04;
	/**
	 * クライアントへIFFの設定値を通知する。
	 * [0]		: 0x04;
	 * [1]		: IFFValue(byte);
	 * [2..5]	: Index(int);
	 */
	public static final byte LMN_Client_SetIFFValue		= (byte)0x04;
	/**
	 * サーバーへ現在のIFFの設定値を要求する。
	 * 要求時は一意な識別番号を付与すること。
	 * [0]		: 0x05;
	 * [1..4]	: Index(int);
	 * [5..]	: TargetName(str);
	 */
	public static final byte LMN_Server_GetIFFValue		= (byte)0x05;
	/**
	 * サーバーへIFFの設定値を保存するように指示する。
	 * [0]		: 0x06;
	 */
	public static final byte LMN_Server_SaveIFF			= (byte)0x06;
	/**
	 * クライアント側へ音声を発生させるように指示する。
	 * 音声の自体はクライアント側の登録音声を使用するため標準の再生手順だと音がでないため。
	 * [0]		: 0x07;
	 * [1..4]	: EntityID(int);
	 * [5..8]	: SoundIndex(int);
	 */
	public static final byte LMN_Client_PlaySound		= (byte)0x89;


}
